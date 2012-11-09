package com.feildmaster.lib.configuration;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents an EnhancedConfiguration
 *
 * @author feildmaster
 */
public interface EnhancedConfigurationSection extends ConfigurationSection {
    EnhancedConfigurationSection getConfigurationSection(String path);

    EnhancedConfigurationSection createSection(String path);

    EnhancedConfigurationSection createLiteralSection(String key);
}
