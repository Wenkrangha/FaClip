package com.wenkrang.faClip.Moudle.FaMessage;

import com.wenkrang.faClip.Moudle.FaMessage.Helper.Scc;
import com.wenkrang.faClip.Moudle.FaMessage.Helper.i18nHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fm {
    /**
     * 消息输出时的前缀标识
     */
    public static String prefix = "[FaClip]";

    /**
     * 获取消息前缀
     *
     * @return 消息前缀字符串
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 向控制台发送普通日志消息
     *
     * @param msg 要输出的消息内容
     */
    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(prefix + " " + msg);
    }

    /**
     * 向指定玩家发送普通日志消息
     *
     * @param player 接收消息的玩家对象
     * @param msg 要输出的消息内容
     */
    public static void log(Player player, String msg) {
        player.sendMessage(prefix + " " + msg);
    }

    /**
     * 向命令发送者发送普通日志消息（自动判断是玩家还是控制台）
     *
     * @param sender 接收消息的命令发送者
     * @param msg 要输出的消息内容
     */
    public static void log(CommandSender sender, String msg) {
        sender.sendMessage(prefix + " " + msg);
    }

    /**
     * 向控制台发送信息状态消息（带蓝色[*]标记）
     *
     * @param msg 要输出的信息内容
     */
    public static void info(String msg) {
        Bukkit.getConsoleSender().sendMessage(Scc.BLUE + "[*] " + Scc.RESET + msg);
    }

    /**
     * 向指定玩家发送信息状态消息（带蓝色[*]标记）
     *
     * @param player 接收消息的玩家对象
     * @param msg 要输出的信息内容
     */
    public static void info(Player player, String msg) {
        player.sendMessage(Scc.BLUE + "[*] " + Scc.RESET + msg);
    }

    /**
     * 向命令发送者发送信息状态消息（带蓝色[*]标记）
     *
     * @param sender 接收消息的命令发送者
     * @param msg 要输出的信息内容
     */
    public static void info(CommandSender sender, String msg) {
        sender.sendMessage(prefix + " " + Scc.BLUE + "[*] " + Scc.RESET + msg);
    }

    /**
     * 向控制台发送错误状态消息（带红色[-]标记）
     *
     * @param msg 要输出的错误内容
     */
    public static void error(String msg) {
        Bukkit.getConsoleSender().sendMessage(Scc.RED + "[-] " + Scc.RESET + msg);
    }

    /**
     * 向指定玩家发送错误状态消息（带红色[-]标记）
     *
     * @param player 接收消息的玩家对象
     * @param msg 要输出的错误内容
     */
    public static void error(Player player, String msg) {
        player.sendMessage(Scc.RED + "[-] " + Scc.RESET + msg);
    }

    /**
     * 向命令发送者发送错误状态消息（带红色[-]标记）
     *
     * @param sender 接收消息的命令发送者
     * @param msg 要输出的错误内容
     */
    public static void error(CommandSender sender, String msg) {
        sender.sendMessage(prefix + " " + Scc.RED + "[-] " + Scc.RESET + msg);
    }

    /**
     * 向控制台发送警告状态消息（黄色[!]标记）
     *
     * @param msg 要输出的警告内容
     */
    public static void waring(String msg) {
        Bukkit.getConsoleSender().sendMessage(Scc.YELLOW + "[!] " + Scc.RESET + msg);
    }

    /**
     * 向指定玩家发送警告状态消息（带黄色[!]标记）
     *
     * @param player 接收消息的玩家对象
     * @param msg 要输出的警告内容
     */
    public static void waring(Player player, String msg) {
        player.sendMessage(Scc.YELLOW + "[!] " + Scc.RESET + msg);
    }

    /**
     * 向命令发送者发送警告状态消息（带黄色[!]标记）
     *
     * @param sender 接收消息的命令发送者
     * @param msg 要输出的警告内容
     */
    public static void waring(CommandSender sender, String msg) {
        sender.sendMessage(prefix + " " + Scc.YELLOW + "[!] " + Scc.RESET + msg);
    }

    /**
     * 向控制台发送调试状态消息（带灰色[/]标记）
     *
     * @param msg 要输出的调试内容
     */
    public static void debug(String msg) {
        Bukkit.getConsoleSender().sendMessage(Scc.GREY + "[/] " + Scc.RESET + msg);
    }

    /**
     * 向指定玩家发送调试状态消息（带灰色[/]标记）
     *
     * @param player 接收消息的玩家对象
     * @param msg 要输出的调试内容
     */
    public static void debug(Player player, String msg) {
        player.sendMessage(Scc.GREY + "[/] " + Scc.RESET + msg);
    }

    /**
     * 向命令发送者发送调试状态消息（带灰色[/]标记）
     *
     * @param sender 接收消息的命令发送者
     * @param msg 要输出的调试内容
     */
    public static void debug(CommandSender sender, String msg) {
        sender.sendMessage(prefix + " " + Scc.GREY + "[/] " + Scc.RESET + msg);
    }

    /**
     * 向控制台发送详细格式化的多行消息
     * 消息包含标题、副标题、主体内容和次要内容，使用分隔线框起来
     *
     * @param title 主标题（加粗显示）
     * @param subtitle 副标题（灰色显示）
     * @param body 主体内容
     * @param subBody 次要内容
     */
    public static void detail(String title, String subtitle, String body, String subBody) {
        Bukkit.getConsoleSender().sendMessage("-----------------------------------------------------------------\n\n"
                + Scc.BOLD + title + Scc.RESET + "\n" + Scc.GREY + subtitle + Scc.RESET + "\n"
        + body + "\n" + subBody + "\n\n" + "-----------------------------------------------------------------");
    }

    /**
     * 向玩家发送详细格式化的多行消息
     * 消息包含标题、副标题、主体内容和次要内容，使用分隔线框起来
     *
     * @param title 主标题（加粗显示）
     * @param subtitle 副标题（灰色显示）
     * @param body 主体内容
     * @param subBody 次要内容
     */
    public static void detail(Player player, String title, String subtitle, String body, String subBody) {
        player.sendMessage("-----------------------------------------------------------------\n\n"
                + Scc.BOLD + title + Scc.RESET + "\n" + Scc.GREY + subtitle + Scc.RESET + "\n"
                + body + "\n" + subBody + "\n\n" + "-----------------------------------------------------------------");
    }
}
