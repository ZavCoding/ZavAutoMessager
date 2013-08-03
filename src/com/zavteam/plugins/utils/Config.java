package com.zavteam.plugins.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class Config {

	private JavaPlugin plugin;
	private String fileName;
	private FileConfiguration configFile;

	public Config(JavaPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.loadConfig();
	}
	
	/**
	 * Loads and saves configs with defaults if needed
	 */
	public void loadConfig() {
		File file = new File(plugin.getDataFolder(), fileName);
		if (file.exists())
		{
			configFile = YamlConfiguration.loadConfiguration(file);
		}
		else
		{
			InputStream defaultsStream = plugin.getResource(fileName);
			if (defaultsStream != null) {
				configFile = YamlConfiguration.loadConfiguration(defaultsStream);
			}
		}
		saveConfig();
	}
	
	/**
	 * 
	 * @return Name of file including extension
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * 
	 * @return The file configuration for this wrapper
	 */
	public FileConfiguration getConfig() {
		if (configFile == null) {
			loadConfig();
		}
		return configFile;
	}
	
	/**
	 * Saves file
	 */
	public void saveConfig() {
		File file = new File(plugin.getDataFolder(), fileName);
		try {
			configFile.save(file);
		} catch (IOException e) {
			plugin.getLogger().severe("Could not save config. Printing stack trace: ");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param path The path of the config item to get
	 * @return the object at that location or null
	 */
	public Object get(String path) {
		return get(path, null);
	}
	
	/**
	 * 
	 * @param path The path of the config item to get
	 * @param def The default object if nothing is found
	 * @return the object at that location, or the default, or null
	 */
	public Object get(String path, @Nullable Object def) {
		return configFile.get(path, def);
	}
	
	/**
	 * 
	 * @param path The path of the config item to set
	 * @param object The object to place in that location
	 */
	public void set(String path, Object object) {
		configFile.set(path, object);
	}

}