package com.feildmaster.lib.configuration;

public class PluginWrapper extends org.bukkit.plugin.java.JavaPlugin {
    private EnhancedConfiguration config;

    public EnhancedConfiguration getConfig() {
        if(config == null) {
            config = new EnhancedConfiguration(this);
        }
        return config;
    }

    public void reloadConfig() {
        getConfig().load();
    }

    public void saveConfig() {
        getConfig().save();
    }

    public void saveDefaultConfig() {
        getConfig().saveDefaults();
    }
}
