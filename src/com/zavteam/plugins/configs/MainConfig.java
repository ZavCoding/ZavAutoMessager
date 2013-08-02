package com.zavteam.plugins.configs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.zavteam.plugins.ChatMessage;
import com.zavteam.plugins.ZavAutoMessager;

public class MainConfig {
	// Main Config Handlers
	public static FileConfiguration config;
	public static void loadConfig() {
		ZavAutoMessager.plugin.reloadConfig();
		config = ZavAutoMessager.plugin.getConfig();
		ZavAutoMessager.plugin.messages = getMessages();
	}
	public static String getChatFormat() {
		return config.getString("chatformat");
	}

	public static int getDelay() {
		return config.getInt("delay");
	}
	
	public static List<ChatMessage> getMessages() {
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		try {
			for (String permission : config.getConfigurationSection("messages").getKeys(false)) {
				for (String message : config.getStringList("messages." + permission)) {
					messages.add(new ChatMessage(message, permission));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ZavAutoMessager.log.severe(ZavAutoMessager.plugin + " has encountered a sever error. No messages are in the config");
			ZavAutoMessager.log.severe(ZavAutoMessager.plugin + " If you are updating from a version 2.2 or below please update your config to the new layout");
		}
		return messages;
	}
	public static boolean getMessageRandom() {
		return config.getBoolean("messageinrandomorder");
	}
	public static boolean getChatWrap() {
		return config.getBoolean("wordwrap");
	}
	public static boolean getEnabled() {
		return config.getBoolean("enabled");
	}
	public static boolean getPermissionEnabled() {
		return config.getBoolean("permissionsenabled");
	}
	public static boolean getUpdateChecking() {
		return config.getBoolean("updatechecking");
	}
	public static boolean getMessagesInConsole() {
		return config.getBoolean("messagesinconsole");
	}
	public static boolean getGroupBasedMessaging() {
		return config.getBoolean("groupbasedmessaging");
	}
	public static boolean getRequirePlayers() {
		return config.getBoolean("requireplayersonline");
	}
	public static boolean getForceRandom() {
		return config.getBoolean("dontrepeatrandommessages");
	}
	public static void set(String s, Object o) {
		config.set(s, o);
		ZavAutoMessager.plugin.saveConfig();
		loadConfig();
	}
}