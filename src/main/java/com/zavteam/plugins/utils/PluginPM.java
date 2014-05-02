package com.zavteam.plugins.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginPM {
	
	public enum MessageType {
		SUCCESS,
		WARNING,
		INFO,
        NO_FORMATTING
	}

    public static String name = "ZavAutoMessager";
	public static Logger log = Bukkit.getLogger();
	
	/**
	 * 
	 * @param messageType The messsage type that you're trying to broadcast
	 * @param message The message to broadcast
	 */
	public static void sendMessage(MessageType messageType, String message) {
		String tag = "[" + name + "] ";
		switch (messageType) {
		case SUCCESS:
			tag = ChatColor.GREEN + tag;
			break;
		case WARNING:
			tag = ChatColor.RED + tag;
			break;
        case NO_FORMATTING:
            tag = "";
            break;
		default:
			tag = ChatColor.YELLOW + tag;
			break;
		}
		Bukkit.broadcastMessage(tag + message);
	}
	
	/**
	 * @param level The level at which a message will be logged
	 * @param message The message to be logged
	 */
	public static void sendMessage(Level level, String message) {
		log.log(level, message);
	}
	
	/**
	 * 
	 * @param messageType The message type that you're trying to send
	 * @param sender The CommandSender to send it to
	 * @param message The message being sent
	 */
	public static void sendMessage(MessageType messageType, CommandSender sender, String message) {
		String tag = "[" + name + "] ";
		switch (messageType) {
		case SUCCESS:
			tag = ChatColor.GREEN + tag;
			break;
		case WARNING:
			tag = ChatColor.RED + tag;
			break;
        case NO_FORMATTING:
            tag = "";
		default:
			tag = ChatColor.YELLOW + tag;
			break;
		}
		sender.sendMessage(tag + message);
	}
	
}