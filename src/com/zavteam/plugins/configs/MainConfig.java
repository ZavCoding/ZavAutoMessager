package com.zavteam.plugins.configs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.zavteam.plugins.Main;

public class MainConfig {
	// Main Config Handlers
	public Main plugin;
	public static FileConfiguration config;
	public MainConfig(Main instance) {
		plugin = instance;
	}
	public void loadConfig() {
		plugin.reloadConfig();
		config = plugin.getConfig();
		plugin.messages = getMessages();
	}
	public String getChatFormat() {
		return config.getString("chatformat");
	}

	public int getDelay() {
		return config.getInt("delay");
	}

	public List<String> getMessages() {
		List<String> temp = new ArrayList<String>();
		temp.addAll(config.getConfigurationSection("messages").getKeys(false));
		return temp;
	}
	public boolean getMessageRandom() {
		return config.getBoolean("messageinrandomorder");
	}
	public boolean getChatWrap() {
		return config.getBoolean("wordwrap");
	}
	public boolean getEnabled() {
		return config.getBoolean("enabled");
	}
	public boolean getPermissionEnabled() {
		return config.getBoolean("permissionsenabled");
	}
	public boolean getUpdateChecking() {
		return config.getBoolean("updatechecking");
	}
	public boolean getMessagesInConsole() {
		return config.getBoolean("messagesinconsole");
	}
	public boolean getGroupBasedMessaging() {
		return config.getBoolean("groupbasedmessaging");
	}
	public boolean getRequirePlayers() {
		return config.getBoolean("requireplayersonline");
	}
	public boolean getForceRandom() {
		return config.getBoolean("dontrepeatrandommessages");
	}
	public void set(String s, Object o) {
		config.set(s, o);
		plugin.saveConfig();
		loadConfig();
	}
}