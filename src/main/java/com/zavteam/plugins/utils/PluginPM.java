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
        NO_TAG,
        RAW
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
        boolean raw = false;
		switch (messageType) {
		case SUCCESS:
			tag = ChatColor.GREEN + tag;
			break;
		case WARNING:
			tag = ChatColor.RED + tag;
			break;
        case NO_TAG:
            tag = "";
            break;
		default:
			tag = "";
            raw = true;
			break;
		}
		Bukkit.broadcastMessage(tag + (raw ? message : ChatColor.translateAlternateColorCodes('&', message)));
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
        boolean raw = false;
		switch (messageType) {
		case SUCCESS:
			tag = ChatColor.GREEN + tag;
			break;
		case WARNING:
			tag = ChatColor.RED + tag;
			break;
        case NO_TAG:
            tag = "";
		default:
			tag = "";
            raw = true;
			break;
		}
		sender.sendMessage(tag + (raw ? message : ChatColor.translateAlternateColorCodes('&', message)));
	}
	
}