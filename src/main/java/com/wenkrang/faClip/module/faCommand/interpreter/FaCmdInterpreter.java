package com.wenkrang.faClip.module.faCommand.interpreter;

import com.wenkrang.faClip.module.FaClip;
import com.wenkrang.faClip.module.faCommand.annotation.Debug;
import com.wenkrang.faClip.module.faCommand.annotationHandler.CmdAnnotationHandler;
import com.wenkrang.faClip.module.faCommand.FaCmd;
import com.wenkrang.faClip.module.faCommand.FaCmdInstance;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.SimpleStage;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.interpreter.AuthorizationStage;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.interpreter.ConflictCheckStage;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.interpreter.EmptyCheckStage;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.interpreter.OnlyForHelpStage;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.tabComplete.TabAuthStage;
import com.wenkrang.faClip.module.faCommand.helper.FaHelperGenerator;
import com.wenkrang.faClip.module.faCommand.helper.CmdHandleHelper;
import com.wenkrang.faClip.module.faCommand.helper.CmdParamHelper;
import com.wenkrang.faClip.module.faCommand.helper.NodeHelper;
import com.wenkrang.faClip.module.faInterface.FaIntf;
import com.wenkrang.faClip.module.faMessage.exception.FaCmdException;
import com.wenkrang.faClip.module.faMessage.exception.FaException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wenkrang.faClip.module.faCommand.helper.CmdHandleHelper.handleRootCommand;

public class FaCmdInterpreter {
    // 解释器管线
    private final ArrayList<SimpleStage> interpreterPipe = new ArrayList<>();

    // 补全管线
    private final ArrayList<SimpleStage> tabPipe = new ArrayList<>();

    private final FaCmdInstance faCmdInstance;

    private final Plugin plugin;

    public FaCmdInterpreter(FaCmdInstance faCmdInstance, Plugin p) {
        this.faCmdInstance = faCmdInstance;
        this.plugin = p;
        initPipes();
    }

    public void addInterpreterStage(SimpleStage stage) {
        interpreterPipe.add(stage);
    }
    
    public void addTabCompleteStage(SimpleStage stage) {
        tabPipe.add(stage);
    }
    
    /**
     * 初始化解释器管线和补全管线，注册所有检查阶段
     */
    private void initPipes() {
        addInterpreterStage(new EmptyCheckStage());
        addInterpreterStage(new ConflictCheckStage());
        addInterpreterStage(new AuthorizationStage());
        addInterpreterStage(new OnlyForHelpStage());

        addTabCompleteStage(new TabAuthStage());
    }

    private ArrayList<CmdAnnotationHandler> annotationHandlers = new ArrayList<>();

    public void addAnnotationHandlers(CmdAnnotationHandler annotationHandler) {
        this.annotationHandlers.add(annotationHandler);
    }

    public void setAnnotationHandlers(ArrayList<CmdAnnotationHandler> annotationHandlers) {
        this.annotationHandlers = annotationHandlers;
    }

    public ArrayList<CmdAnnotationHandler> getAnnotationHandlers() {
        return annotationHandlers;
    }

    public void initialize(@NotNull Method method) {
        if (!NodeHelper.isCmdNode(method)) return;

        // 创建 Builder
        FaCmd.Builder builder = FaCmd.builder(this);

        // 注解 Handler 填充 Builder
        annotationHandlers.stream()
                .filter(i -> method.isAnnotationPresent(i.getAnnotationClass()))
                .forEach(i -> i.handle(builder, method));

        // 初始化接口并设置到 Builder
        if (builder.getNode() == null) {
            throw new FaCmdException("FaCommand.Error.Interpreter.NodeNotInit");
        }

        FaIntf faIntf = getFaCmdInstance().getFaInterfaceInstance().registerFaIntf(method, builder.getNode());
        builder.faIntf(faIntf);

        // 构建不可变的 FaCmd
        FaCmd faCmd = builder.build();

        // 如果没启用调试模式，就不启用调试命令
        if (method.getAnnotation(Debug.class) != null && !FaClip.debug) return;

        register(faCmd);
    }

    public void register(@NotNull FaCmd faCmd) {
        //检查命令节点是否设置
        //检查根命令是否注册
        String rootCommand = NodeHelper.getRoot(faCmd.getNode());

        if (CmdHandleHelper.isUnregistered(rootCommand)) {
            handleRootCommand(rootCommand, faCmd, this);
        }

        faCmdInstance.addFaCmd(faCmd);
    }

    /**
     * 解析并执行命令
     * 
     * @param sender 命令发送者，不能为空
     * @param commandLabel 命令标签，不能为空
     * @param args 命令参数数组，不能为空
     * @return 执行成功返回true，失败返回false
     */
    public boolean interpret(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // 构建完整参数列表（Spigot未传根命令，需自行拼接）
        ArrayList<String> cArgs = CmdParamHelper.getCompleteParam(sender, commandLabel, args);

        try {
            List<FaIntf> faIntfs = faCmdInstance.getFaInterfaceInstance().guessIntf(cArgs.toArray(String[]::new));
            List<FaCmd> faCmds = faIntfs.stream().map(faCmdInstance::getFaCmd).toList();

            FaCmdContext context = new FaCmdContext(sender, args);

            // 只能匹配一条命令，因此getFirst()即为所求
            // 如果匹配多条命令，管线会返回false，中断处理
            FaCmd faCmd = faCmds.stream().findFirst().orElse(null);

            // 遍历解释器管线，任意阶段返回false则终止执行
            for (SimpleStage stage : interpreterPipe) {
                if (!stage.check(faCmd, context, faCmds)) {
                    return false;
                }
            }

            try {
                if (faCmd != null) {
                    // 执行方法
                    FaIntf faIntf = faCmd.getFaIntf();
                    Object invoke = faIntf.invoke(null, context, cArgs.toArray(String[]::new));
                    if (invoke instanceof Boolean) {
                        return (Boolean) invoke;
                    }
                }
            } catch (InvocationTargetException e) {
                // 解包业务方法抛出的异常：FaException 原样上抛，其余包装为 FaCmdException
                Throwable cause = e.getCause() != null ? e.getCause() : e;
                if (cause instanceof FaException faException) throw faException;
                throw new FaCmdException("FaCommand.Error.Interpreter.interpreter", cause, cause.getMessage());
            } catch (FaException e) {
                throw e;
            } catch (Exception e) {
                throw new FaCmdException("FaCommand.Error.Interpreter.interpreter", e, e.getMessage());
            }

        } catch (FaException e) {
            // FaException 已携带 i18n 消息，直接上抛，避免二次包装
            throw e;
        } catch (Exception e) {
            throw new FaCmdException("FaCommand.Error.Interpreter.interpreter", e, e.getMessage());
        }

        return true;
    }

    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // Spigot直接split空格后传参
        ArrayList<String> cArgs = CmdParamHelper.getCompleteParam(sender, commandLabel, args);

        // 跳过根命令为空的情况
        if (cArgs.isEmpty()) {return null;}

        // 获取命令，使用模糊模式
        ArrayList<FaIntf> filteredIntfs = new ArrayList<>();

        for (FaIntf faIntf : faCmdInstance.getFaInterfaceInstance().getFaIntfs()) {
            if (faIntf.fuzzyCheck(cArgs.toArray(String[]::new)))
                filteredIntfs.add(faIntf);
        }

        List<FaCmd> faCmds = filteredIntfs.stream().map(faCmdInstance::getFaCmd).toList();

        if (faCmds.isEmpty()) return new ArrayList<>();

        FaCmdContext context = new FaCmdContext(sender, args);

        // 通过补全管线过滤命令
        List<FaCmd> filteredCmds = new ArrayList<>();
        for (FaCmd cmd : faCmds) {
            boolean passed = true;
            for (SimpleStage stage : tabPipe) {
                if (!stage.check(cmd, context, faCmds)) {
                    passed = false;
                    break;
                }
            }
            if (passed) filteredCmds.add(cmd);
        }

        if (filteredCmds.isEmpty()) return new ArrayList<>();

        // 获取所有通过过滤的命令的用法
        FaHelperGenerator faHelperGenerator = new FaHelperGenerator(faCmdInstance);

        Object[] usages = filteredCmds.stream()
                .map(i -> faHelperGenerator.getUsage(i.getFaIntf(), context, true))
                .filter(i -> i.length >= cArgs.size())
                .map(i -> i[cArgs.size() - 1])
                .filter(Objects::nonNull)
                .toArray();

        List<String> flat = CmdParamHelper.flat(usages);

        return flat.stream().filter(i -> i.startsWith(cArgs.getLast())).toList();
    }

    public FaCmdInstance getFaCmdInstance() {
        return faCmdInstance;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
