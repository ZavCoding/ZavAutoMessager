package com.feildmaster.lib.configuration;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

public class NullEnhancedMemorySection extends EnhancedMemorySection implements NullConfigurationSection {
    public NullEnhancedMemorySection(EnhancedConfiguration superParent, MemorySection parent, String path) {
        super(superParent, parent, path);
    }

    @Override
    public void set(String path, Object value) {
        Validate.notNull(path, "Path cannot be null");
        Validate.notEmpty(path, "Cannot set to an empty path");

        if (!value.equals(get(path))) {
            superParent.modified = true;
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
            this.map.put(key, value);
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

        if (superParent.cache.containsKey(path)) {
            return superParent.cache.get(path);
        }

        Object value = super.get(path, def);
        if (!(value instanceof ConfigurationSection)) {
            superParent.cache.put(path, value);
        }

        return value;
    }

    /**
     * Removes the specified path from the configuration.
     *
     * @param path The path to remove
     */
    @Override
    public void unset(String path) {
        Validate.notNull(path, "Path cannot be null");
        Validate.notEmpty(path, "Cannot set to an empty path");

        // We're removing something... so it's (probably) modified
        superParent.modified = true;
        superParent.cache.remove(path);

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
        superParent.modified = true;

        final char seperator = getRoot().options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        NullConfigurationSection section = this;
        while ((i1 = path.indexOf(seperator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            NullConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createLiteralSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            return createLiteralSection(key);
        }
        return section.createLiteralSection(key);
    }

    @Override
    public NullConfigurationSection createLiteralSection(String key) {
        NullConfigurationSection section = new NullEnhancedMemorySection(superParent, this, key);
        map.put(key, section);
        return section;
    }
}
