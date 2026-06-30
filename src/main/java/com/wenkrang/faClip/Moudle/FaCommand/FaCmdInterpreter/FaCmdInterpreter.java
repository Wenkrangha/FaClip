package com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter;

import com.wenkrang.faClip.Moudle.FaCommand.AnnotationHandler.FaAnnotationHandler;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmd;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInstance;
import com.wenkrang.faClip.Moudle.FaCommand.FaHelperGenerator.FaHelperGenerator;
import com.wenkrang.faClip.Moudle.FaCommand.Helper.CmdHandleHelper;
import com.wenkrang.faClip.Moudle.FaCommand.Helper.CmdNodeHelper;
import com.wenkrang.faClip.Moudle.FaCommand.FaParam.FaParam;
import com.wenkrang.faClip.Moudle.FaMessage.Fm;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

import static com.wenkrang.faClip.Moudle.FaMessage.Helper.i18nHelper.t;
import static com.wenkrang.faClip.Moudle.FaCommand.Helper.CmdHandleHelper.handleRootCommand;

public class FaCmdInterpreter {
    private final FaCmdInstance faCmdInstance;

    private final Plugin plugin;

    public FaCmdInterpreter(FaCmdInstance faCmdInstance, Plugin p) {
        this.faCmdInstance = faCmdInstance;
        this.plugin = p;
    }

    private ArrayList<FaAnnotationHandler> annotationHandlers = new ArrayList<>();

    public static @NotNull ArrayList<String> getCompleteParam(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // 还原完整的命令参数列表
        ArrayList<String> params = new ArrayList<>();
        params.add(commandLabel);
        params.addAll(List.of(args));

        return params;
    }

    public static @NotNull Object[] convertParams(
            @NotNull CommandSender sender
            , @NotNull Method method, String @NotNull [] args, @NotNull String node) {
        // 准备参数转换器和实际参数
        FaParam faChecker = new FaParam();

        String[] removedNodeArgs = CmdNodeHelper.removeNode(node, Arrays.stream(args).toList()).toArray(String[]::new);

        // 转换参数类型
        Object[] convertedArgs = new Object[method.getParameterCount()];

        // 设置FaCmdContext
        IntStream.range(0, method.getParameterCount())
                .forEach(i -> {
                    if(method.getParameters()[i].getType().equals(FaCmdContext.class))
                        convertedArgs[i] = new FaCmdContext(sender, Arrays.stream(args).skip(1).toArray(String[]::new));
                });
        // 获取真正的参数位
        List<Integer> nonNull = IntStream.range(0, method.getParameterCount())
                .filter(i -> convertedArgs[i] != null)
                .boxed().toList();
        // 转换参数
        for (int i = 0;i < nonNull.size(); i++) {
            if (i >= removedNodeArgs.length) {
                break;
            }
            Object parse = faChecker.parse(removedNodeArgs[i], method.getParameters()[nonNull.get(i)].getType());
            if (parse == null) {
                Fm.waring(t("FaCommand.Error.Interpreter.ArgsNPEWarning"));
            }
            convertedArgs[nonNull.get(i)] = parse;
        }


//        int argIndex = 0; // 添加参数索引计数器
//        for (int i = 0; i < method.getParameters().length; i++) {
//            if (method.getParameters()[i].getType().equals(FaCmdContext.class)) {
//                convertedArgs.add(new FaCmdContext(sender, Arrays.stream(args).skip(1).toArray(String[]::new)));
//                continue;
//            }
//            // 使用argIndex而不是i来访问realArgs
//            convertedArgs.add(faChecker.parse(removedNodeArgs[argIndex], method.getParameters()[i].getType()));
//            argIndex++; // 只有非FaCmdHandle参数才增加索引
//        }

        return convertedArgs;
    }

    public void addAnnotationHandlers(FaAnnotationHandler annotationHandler) {
        this.annotationHandlers.add(annotationHandler);
    }

    public void setAnnotationHandlers(ArrayList<FaAnnotationHandler> annotationHandlers) {
        this.annotationHandlers = annotationHandlers;
    }

    public ArrayList<FaAnnotationHandler> getAnnotationHandlers() {
        return annotationHandlers;
    }

    public void initialize(@NotNull Method method) {
        if (!CmdNodeHelper.isCmdNode(method)) return;

        FaCmd faCmd = new FaCmd(this);

        annotationHandlers.stream()
                .filter(i -> method.isAnnotationPresent(i.getAnnotationClass()))
                .forEach(i -> i.handle(faCmd, method));

        register(faCmd);
    }

    public void register(@NotNull FaCmd faCmd) {
        //检查命令节点是否设置
        if (faCmd.getNode() != null){
            //检查根命令是否注册
            String rootCommand = CmdNodeHelper.getRootCommand(faCmd.getNode());

            if (CmdHandleHelper.isUnregistered(rootCommand)) {
                handleRootCommand(rootCommand, faCmd);
            }

            faCmdInstance.addFaCmd(faCmd);
        }
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
        // 命令执行器的args，由于Spigot没传根命令，需要自己加一下commandLabel
        // 这里构建完整参数列表


        try {
            // 获取完整参数列表
            ArrayList<String> params = getCompleteParam(sender, commandLabel, args);

            // 创建命令节点解析器
            FaGuesser faGuesser = new FaGuesser(faCmdInstance);

            List<FaCmd> faCmds = faGuesser.guessFaCmd(params, FaGuesser.guessMode.full);

            if (faCmds == null) return false;

            // 命令冲突
            if (faCmds.size() > 1) {
                Fm.error(sender,t("FaCommand.Error.Interpreter.Conflict") + " " + faCmds);
                return false;
            }

            Optional<FaCmd> faCmd = faCmds.stream().findFirst();

            // 命令存在
            if (faCmd.isPresent()) {
                // 权限检查
                if (faCmd.get().isRequireOP() && !sender.isOp()) {
                    Fm.log(sender, t("FaCommand.Error.Interpreter.RequireOP"));
                    return false;
                }
                // 权限检查
                if (faCmd.get().getPermission() != null && !sender.hasPermission(faCmd.get().getPermission())) {
                    Fm.log(sender, t("FaCommand.Error.Interpreter.NoPermission"));
                    return false;
                }
                // 输出帮助
                if (faCmd.get().isOnlyForHelp()) {
                    FaHelperGenerator faHelperGenerator = new FaHelperGenerator(faCmdInstance);
                    for (String help : faHelperGenerator.generate(faCmd.get().getNode())) {
                        Fm.log(sender, help);
                    }
                    return false;
                }


                Method method = faCmd.get().getMethod();

                // 转换为方法参数
                Object[] objects = convertParams(sender, method, params.toArray(String[]::new), faCmd.get().getNode());

                try {
                    // 执行方法
                    method.invoke(faCmd.get(), objects);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // 命令不存在
                Fm.log(sender, t("FaCommand.Error.Interpreter.NotFound"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // 这里的args就是完整（吗？）的参数，包括""（没输入但写了空格）
        // Spigot是直接split了空格然后传过来

        // 也是分成三种情况
        // 1. 根命令为空（本补全器不处理，因为Bukkit会自己处理）
        // 2. 子命令为空
        // 3. 参数为空
        // 这里2和3可以一起处理，因为只要根命令不为空，补全器会获取完整用法列表，再根据输入，选择列表中的项

        ArrayList<String> cArgs = getCompleteParam(sender, commandLabel, args);

        // 跳过根命令为空的情况
        if (cArgs.isEmpty()) {return null;}

        // 构建猜测器
        FaGuesser faGuesser = new FaGuesser(faCmdInstance);

        // 获取命令，使用模糊模式
        List<FaCmd> faCmds = faGuesser.guessFaCmd(cArgs, FaGuesser.guessMode.fuzzy);

        if (faCmds == null) return new ArrayList<>();

        // 然后获取所有命令的用法
        FaParam faParam = new FaParam();

        Object[] usages = faCmds.stream()
                .filter(i -> !(i.isRequireOP() && !sender.isOp())) // 跳过权限检查失败的
                .filter(i -> !(i.getPermission() != null && !sender.hasPermission(i.getPermission()))) //TODO:处理这个玩意，啥东西啊
                .map(i -> faParam.getUsage(i, new FaCmdContext(sender, args), true))
                .filter(i -> i.length >= cArgs.size())
                .map(i -> i[cArgs.size() - 1])
                .filter(Objects::nonNull)
                .toArray();

        return faParam.convert(usages);
    }

    public FaCmdInstance getFaCmdInstance() {
        return faCmdInstance;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
