package com.wenkrang.faClip.Moudle.FaCommand.Helper;

import com.wenkrang.faClip.Moudle.FaCommand.Annotation.Cmd;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdNodeHelper {
    /**
     * 检查字符串是否只包含字母、数字和点号
     * @param s 待检查的字符串，不能为空
     * @return 如果字符串只包含字母、数字和点号则返回true，否则返回false
     */
    public static @NotNull Boolean check(@NotNull String s) {
        String regex = "^[a-zA-Z0-9.]+$"; // 定义匹配规则：仅允许字母、数字和点号
        return s.matches(regex); // 使用正则表达式验证字符串是否匹配
    }

    /**
     * 将字符串按点号分割成字符串数组
     * @param s 待分割的字符串，不能为空
     * @return 分割后的字符串数组
     */
    public static String @NotNull [] separateString(@NotNull String s) {
        return s.split("\\.");
    }

    /**
     * 获取命令字符串中的根命令（第一个点号前的部分）
     * @param s 命令字符串
     * @return 根命令部分
     */
    public static String getRootCommand(@NotNull String s) {
        String[] strings = separateString(s);
        return strings[0];
    }

    /**
     * 获取命令字符串中的子命令（最后一个点号后的部分）
     * @param s 命令字符串
     * @return 子命令部分
     */
    public static String getSubCommand(@NotNull String s) {
        String[] strings = separateString(s);
        return strings[strings.length - 1];
    }

    /**
     * 判断方法是否被CmdNode注解标记
     * @param method 待检查的方法
     * @return 如果方法被CmdNode注解标记则返回true，否则返回false
     */
    public static boolean isCmdNode(@NotNull Method method) {
        return method.getAnnotation(Cmd.class) != null;
    }

    /**
     * 将松散字符串转换为命令节点
     *
     * @param strings 待转换的松散字符串
     * @return 转换后的命令节点
     */
    public static String formNode(@NotNull List<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        String node;

        for (String s : strings) {
            stringBuilder.append(s).append(".");
        }

        node = stringBuilder.substring(0, stringBuilder.length() - 1);

        return node;
    }

    /**
     * 将松散字符串转换为命令节点
     *
     * @param strings 待转换的松散字符串
     * @return 转换后的命令节点
     */
    public static String formNode(String[] strings) {
        return formNode(new ArrayList<>(Arrays.asList(strings)));
    }


    public static @NotNull List<String> removeNode(@NotNull String node, @NotNull List<String> args) {
        return args.stream().skip(separateNode(node).size()).toList();
    }

    /**
     * 分割命令节点
     *
     * @param node 待分割的命令节点
     * @return 分割后的命令节点数组
     */
    public static @NotNull ArrayList<String> separateNode(@NotNull String node) {
        String[] split = node.split("\\.");
        return new ArrayList<>(Arrays.asList(split));
    }

    /**
     * 移除列表中的空字符串
     *
     * @param list 待处理的列表
     * @return 处理后的列表，其中空字符串已被移除
     */
    public static @NotNull List<String> removeEmpty(@NotNull List<String> list) {
        return list.stream().filter(i -> !i.isEmpty()).toList();
    }
}
