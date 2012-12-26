package com.zavteam.plugins.configs;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.zavteam.plugins.Main;

public class MainConfig {
	// Main Config Handlers
	public static FileConfiguration config;
	public static void loadConfig() {
		Main.plugin.reloadConfig();
		config = Main.plugin.getConfig();
		Main.plugin.messages = getMessages();
	}
	public static String getChatFormat() {
		return config.getString("chatformat");
	}

	public static int getDelay() {
		return config.getInt("delay");
	}

	public static List<String> getMessages() {
		return config.getStringList("messages");
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
		Main.plugin.saveConfig();
		loadConfig();
	}
}