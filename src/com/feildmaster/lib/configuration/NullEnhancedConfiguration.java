package com.feildmaster.lib.configuration;

import java.io.File;
import java.lang.reflect.Field;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.*;

/**
 * Enhances Configuration further by saving null values
 *
 * @author Feildmaster
 */
public class NullEnhancedConfiguration extends EnhancedConfiguration implements NullConfigurationSection {
    /**
     * Creates a new NullEnhancedConfiguration with a file named "config.yml," stored in the plugin DataFolder
     * <p />
     * Will fail if plugin is null
     *
     * @param plugin The plugin registered to this Configuration
     */
    public NullEnhancedConfiguration(Plugin plugin) {
        this("config.yml", plugin);
    }

    /**
     * Creates a new NullEnhancedConfiguration with a file stored in the plugin DataFolder
     * <p />
     * Will fail if plugin is null.
     *
     * @param file The name of the file
     * @param plugin The plugin registered to this Configuration
     */
    public NullEnhancedConfiguration(String file, Plugin plugin) {
        this(new File(plugin.getDataFolder(), file), plugin);
    }

    /**
     * Creates a new NullEnhancedConfiguration with given File and Plugin.
     *
     * @param file The file to store in this Configuration
     * @param plugin The plugin registered to this Configuration
     */
    public NullEnhancedConfiguration(File file, Plugin plugin) {
        super(file, plugin, false);
        reflectYaml();
        load();
    }

    @Override
    public void set(String path, Object value) {
        Validate.notNull(path, "Path cannot be null");
        Validate.notEmpty(path, "Cannot set to an empty path");

        if (!value.equals(get(path))) {
            modified = true;
        }

        final char seperator = getRoot().options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(seperator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            map.put(key, value);
        } else {
            section.set(key, value);
        }
    }

    @Override
    public Object get(String path, Object def) {
        Validate.notNull(path, "Path cannot be null");
        if (path.isEmpty()) {
            return this;
        }

        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        Object value = super.get(path, def);
        if (!(value instanceof ConfigurationSection)) {
            cache.put(path, value);
        }

        return value;
    }

    /**
     * Removes the specified path from the configuration
     *
     * @param path The path to remove
     */
    @Override
    public void unset(String path) {
        Validate.notNull(path, "Path cannot be null");
        Validate.notEmpty(path, "Cannot set to an empty path");

        // We're removing something... so it's (probably) modified
        modified = true;
        cache.remove(path);

        final char seperator = getRoot().options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        NullConfigurationSection section = this;
        while ((i1 = path.indexOf(seperator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            NullConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            map.remove(key);
        } else {
            section.unset(key);
        }
    }

    @Override
    public NullConfigurationSection getConfigurationSection(String path) {
        return (NullConfigurationSection) super.getConfigurationSection(path);
    }

    @Override
    public NullConfigurationSection createSection(String path) {
        Validate.notNull(path, "Path cannot be null");
        Validate.notEmpty(path, "Cannot create section at empty path");

        // We're creating sections... This means it's modified!
        modified = true;

        final char seperator = getRoot().options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        NullConfigurationSection section = this;
        while ((i1 = path.indexOf(seperator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            NullConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == this) {
                section = section.createLiteralSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        return section.createLiteralSection(key);
    }

    @Override
    public NullConfigurationSection createLiteralSection(String key) {
        NullConfigurationSection section = new NullEnhancedMemorySection(this, this, key);
        map.put(key, section);
        return section;
    }

    private void reflectYaml() {
        try {
            Class yamlClass = this.getClass();
            while(!yamlClass.equals(YamlConfiguration.class)) {
                yamlClass = yamlClass.getSuperclass();
            }

            // Set the representer
            EnhancedRepresenter newRepresenter = new EnhancedRepresenter();
            Field representer = yamlClass.getDeclaredField("yamlRepresenter");
            representer.setAccessible(true);
            representer.set(this, newRepresenter);

            // Get the options, and reuse them! :D
            Field options = yamlClass.getDeclaredField("yamlOptions");
            options.setAccessible(true);
            DumperOptions oldOptions = (DumperOptions) options.get(this);

            // Set the yaml, too bad I can't just set my representer on the old instance. :(
            Yaml newYaml = new Yaml(new YamlConstructor(), newRepresenter, oldOptions);
            Field yaml = yamlClass.getDeclaredField("yaml");
            yaml.setAccessible(true);
            yaml.set(this, newYaml);
        } catch (Exception ex) {
            getPlugin().getLogger().log(java.util.logging.Level.SEVERE, "Sorry, something bad happened with setting up NullEnhancedConfiguration. Here's an exception:", ex);
        }
    }
}
