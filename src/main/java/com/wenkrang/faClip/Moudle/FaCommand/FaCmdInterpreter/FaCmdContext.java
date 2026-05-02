package com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter;

import org.bukkit.command.CommandSender;

/**
 * 命令输入句柄
 *
 * @param sender 命令发送者
 * @param args   命令参数
 */
public record FaCmdContext(CommandSender sender, String[] args) {
}
