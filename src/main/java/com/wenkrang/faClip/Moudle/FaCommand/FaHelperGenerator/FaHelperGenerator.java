package com.wenkrang.faClip.Moudle.FaCommand.FaHelperGenerator;

import com.wenkrang.faClip.Moudle.FaCommand.FaCmd;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInstance;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Moudle.FaCommand.FaParam.FaParam;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FaHelperGenerator {
    private final FaCmdInstance faCmdInstance;

    public FaHelperGenerator(@NotNull FaCmdInstance Instance) {
        faCmdInstance = Instance;
    }

    public FaCmdInstance getFaCmdInstance() {
        return faCmdInstance;
    }

    /**
     * 生成命令用法
     * @param node 节点
     * @return 命令用法
     */
    public String generateUsage(String node) {
        FaCmd faCmd = getFaCmdInstance().getFaCmds().stream().filter(i -> i.getNode().equals(node)).findFirst().orElseThrow();

        FaParam faParam = new FaParam();

        Object[] usage = faParam.getUsage(faCmd, new FaCmdContext(Bukkit.getConsoleSender(), new String[0]), false);
        List<String> convert = faParam.convert(usage);

        return String.join(" ", node.split("\\.")) + " " + String.join(" ", convert);
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

            msg.add("----------------------------------------------------------");

            // 获取该节点的命令
            FaCmd ExactCmd = list
                    .stream()
                    .filter(i -> i.getNode().equals(node))
                    .findFirst()
                    .orElse(null);

            // 添加该节点的帮助信息
            if (ExactCmd != null) {
                msg.add(ExactCmd.getHelp() == null ? "" : ExactCmd.getHelp());
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
                msg.add(generateUsage(node));
                msg.add("----------------------------------------------------------");
                return msg;
            };

            // 格式化
            String format = "%-" + calculateSize(subCmds) + "s";

            // 添加子命令帮助信息
            for (FaCmd cmd : subCmds) {
                // 添加
                msg.add(String.format(format, cmd.getName() == null ? "" : cmd.getName())
                        + (cmd.getHelp() == null ? "" : cmd.getHelp()));
            }

            msg.add("----------------------------------------------------------");
            return msg;
        }

        return new ArrayList<>();
    }
}
