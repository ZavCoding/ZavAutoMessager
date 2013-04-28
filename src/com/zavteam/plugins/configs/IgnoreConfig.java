package com.zavteam.plugins.configs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.zavteam.plugins.Main;

public class IgnoreConfig {
	private static File ignoreFile;
	private static FileConfiguration config;
	public static void loadConfig() {
		InputStream defaultIgnoreConfigStream;
		if (ignoreFile == null) {
			ignoreFile = new File(Main.plugin.getDataFolder(), "ignore.yml");
		}
		config = YamlConfiguration.loadConfiguration(ignoreFile);
		defaultIgnoreConfigStream = Main.plugin.getResource("ignore.yml");
		if (defaultIgnoreConfigStream != null) {
			config.setDefaults(YamlConfiguration.loadConfiguration(defaultIgnoreConfigStream));
		}
		saveConfig();
	}
	public static void saveConfig() {
		if (config == null || ignoreFile == null) {
			Main.log.severe(Main.plugin + " is unable to save the config.");
			return;
		}
		try {
			config.save(ignoreFile);
		} catch (IOException e) {
			e.printStackTrace();
			Main.log.severe(Main.plugin + " is unable to save config.");
		}
	}
	public static List<String> getIgnorePlayers() {
		return config.getStringList("players");
	}
	public static void set(String s, Object o) {
		config.set(s, o);
		saveConfig();
	}
	
}