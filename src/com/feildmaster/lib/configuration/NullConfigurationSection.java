package com.feildmaster.lib.configuration;

public interface NullConfigurationSection extends EnhancedConfigurationSection {
    NullConfigurationSection getConfigurationSection(String path);

    NullConfigurationSection createSection(String path);

    NullConfigurationSection createLiteralSection(String key);

    void unset(String key);
}
