package com.wenkrang.faClip.module.faCommand.helper;

import com.wenkrang.faClip.module.faCommand.annotation.CustomDes;
import com.wenkrang.faClip.module.faCommand.annotation.DesProvider;
import com.wenkrang.faClip.module.faCommand.annotation.ParamArrayDes;
import com.wenkrang.faClip.module.faCommand.annotation.ParamDes;
import com.wenkrang.faClip.module.faCommand.FaCmd;
import com.wenkrang.faClip.module.faCommand.FaCmdInstance;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;
import com.wenkrang.faClip.module.faInterface.FaIntf;
import com.wenkrang.faClip.module.faInterface.FaIntfContext;
import com.wenkrang.faClip.module.faInterface.param.FaParam;
import com.wenkrang.faClip.module.faInterface.param.SimpleParam;
import com.wenkrang.faClip.module.faMessage.exception.FaCmdException;
import com.wenkrang.faClip.module.faMessage.helper.Scc;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FaHelperGenerator {
    private final FaCmdInstance faCmdInstance;

    public FaHelperGenerator(@NotNull FaCmdInstance Instance) {
        faCmdInstance = Instance;
        faParam = new FaParam();
    }

    public FaCmdInstance getFaCmdInstance() {
        return faCmdInstance;
    }

    public FaParam faParam;

    /**
     * 生成命令用法
     * @param node 节点
     * @return 命令用法
     */
    public String generateUsage(String node) {
        FaCmd faCmd = getFaCmdInstance().getFaCmds().stream()
                .filter(i -> i.getNode().equals(node))
                .findFirst()
                .orElseThrow(() -> new FaCmdException("FaCommand.Error.Interpreter.NotFound"));

        FaParam faParam = new FaParam();

        Object[] usage = getUsage(faCmd.getFaIntf(), new FaCmdContext(Bukkit.getConsoleSender(), new String[0]), false);
        List<String> convert = CmdParamHelper.flat(usage);

        return String.join(" ", convert);
    }


    /**
     * 计算命令长度
     * @param names 命令列表
     * @return 命令长度
     */
    public int calculateSize(List<FaCmd> names) {
        return names.stream()
                .mapToInt(i -> i.getName().length())
                .max()
                .orElse(0) + 5;
    }

    /**
     * 生成帮助信息
     * @param node 节点
     */
    public @NotNull List<String> generate(String node) {
        // 获取节点下的命令（包括该节点的命令）
        List<FaCmd> list = faCmdInstance.getFaCmds().stream()
                .filter(i -> i.getNode().startsWith(node))
                .toList();

        // 非空检查
        if (!list.isEmpty()) {
            // 初始化输出列表
            ArrayList<String> msg = new  ArrayList<>();

            msg.add("-----------------------------------------------------");
            msg.add("");
            // 获取该节点的命令
            FaCmd ExactCmd = list
                    .stream()
                    .filter(i -> i.getNode().equals(node))
                    .findFirst()
                    .orElse(null);

            // 添加该节点的帮助信息
            if (ExactCmd != null) {
                msg.add(ExactCmd.getHelp() == null ? "" :  "  " + Scc.GREY + "[/] " + Scc.RESET + ExactCmd.getHelp());
                msg.add("");
            }

            // 获取子命令
            List<FaCmd> subCmds = list
                    .stream()
                    .filter(i -> !i.equals(ExactCmd))
                    .filter(i -> i.getNode().substring(node.length()).chars().filter(c -> c == '.').count() == 1)
                    .toList();

            // 子命令为空
            if (subCmds.isEmpty()) {
                msg.add("");
                msg.add("  " + generateUsage(node));
                msg.add("");
                msg.add("-----------------------------------------------------");
                return msg;
            };

            // 格式化
            String format = "%-" + calculateSize(subCmds) + "s";

            // 添加子命令帮助信息
            for (FaCmd cmd : subCmds) {
                // 添加
                msg.add(String.format(format, cmd.getName() == null ? "" : "  " + cmd.getName())
                        + (cmd.getHelp() == null ? "" : "  " + cmd.getHelp()));
            }
            msg.add("");
            msg.add("-----------------------------------------------------");
            return msg;
        }

        return new ArrayList<>();
    }

    /**
     * 获取命令的用法
     *
     * @param intf 命令
     * @param faCmdContext 命令上下文
     * @param detail 是否显示详细用法
     * @return 命令的用法
     */
    public Object[] getUsage(@NotNull FaIntf intf, @NotNull FaCmdContext faCmdContext, boolean detail) {
        // 切割节点
        List<String> node = NodeHelper.separateNode(intf.getNode());

        // 获取命令的参数名称
        List<Object> paramNames = Arrays.stream(intf.getMethod().getParameters()) // 获取命令的方法参数
                .filter(i -> !(FaIntfContext.class.isAssignableFrom(i.getType()))) // 过滤掉 FaCmdContext 参数
                .map(i ->
                        {
                            if (i.getAnnotation(CustomDes.class) != null) {
                                try {
                                    DesProvider value = i.getAnnotation(CustomDes.class).value().getDeclaredConstructor().newInstance();
                                    return (Object) value.getDes(faCmdContext);
                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                         NoSuchMethodException e) {
                                    throw new FaCmdException("FaCommand.Error.Helper.DesProviderInitFailed", e,
                                            i.getAnnotation(CustomDes.class).value().getName());
                                }
                            } else if (i.getAnnotation(ParamDes.class) != null) {
                                return i.getAnnotation(ParamDes.class).value();
                            } else if (i.getAnnotation(ParamArrayDes.class) != null && detail) {
                                return i.getAnnotation(ParamArrayDes.class).value();
                            } else {
                                // 准备结果列表
                                ArrayList<String> result = new ArrayList<>();

                                // 获取符合的处理器
                                List<SimpleParam> handlers =
                                        faParam.getSimpleParams().stream()
                                                .filter(j -> j.getType().contains(i.getType()))
                                                .toList();

                                // 历遍处理器
                                for (SimpleParam j : handlers) {
                                    // 检查处理器是否是提供描述的
                                    if (j instanceof DesProvider value && detail) {
                                        @NotNull String[] des = value.getDes(faCmdContext);
                                        result.addAll(Arrays.asList(des));
                                    } else {
                                        // 没有描述就显示参数名称
                                        result.add("<" + j.getName(i.getType()) + ">");
                                    }
                                }

                                // 如果连参数名称为空，则返回参数类型名称（完全限定名）
                                if (result.isEmpty()) {
                                    return "<" + i.getType().getName() + ">";
                                } else {
                                    return result;
                                }
                            }
                        }
                ).toList();

        // 把节点和参数名称拼接起来
        return Stream.concat(node.stream(), paramNames.stream()).toArray(Object[]::new);
    }
}
