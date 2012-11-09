package com.feildmaster.lib.configuration;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.*;
import org.bukkit.plugin.Plugin;

/* __ Things to do __
 *
 * Path Comments
 * - The next thing to code
 *
 * Case Insensitivity
 * - The "proper" way for this would increase configuration memory. Look into further.
 *
 * Lowercase Keys
 * - This is terrible to implement as well...
 *
 * Comments. Steal from here
 * https://github.com/dumptruckman/PluginTemplate/blob/master/src/main/java/com/dumptruckman/plugintemplate/config/CommentedConfig.java
 *
 * I want to add 'literalsections' :D
 */

/**
 * Enhancing configuration to do the following:
 * <li>Stores a file for configuration to use.</li>
 * <li>Self contained "load," "reload," and "save" functions.</li>
 * <li>Self contained "loadDefaults" functions that set defaults.</li>
 * <li>Adds "getLastException" to return the last exception from self contained functions.</li>
 * <li>Adds "options().header(String, String)" to build multiline headers easier(?)</li>
 *
 * @author Feildmaster
 */
public class  EnhancedConfiguration extends org.bukkit.configuration.file.YamlConfiguration implements EnhancedConfigurationSection {
    private final Pattern pattern = Pattern.compile("\n"); // Static? Maybe bad? I'm not sure.
    private final File file;
    private final Plugin plugin;
    private Exception exception;
    protected final Map<String, Object> cache = new HashMap<String, Object>();
    protected boolean modified = false;
    private long last_modified = -1L;

    /**
     * Creates a new EnhancedConfiguration with a file named "config.yml," stored in the plugin DataFolder
     * <p />
     * Will fail if plugin is null.
     *
     * @param plugin The plugin registered to this Configuration
     */
    public EnhancedConfiguration(Plugin plugin) {
        this("config.yml", plugin);
    }

    public EnhancedConfiguration(Plugin plugin, boolean load) {
        this("config.yml", plugin, load);
    }

    /**
     * Creates a new EnhancedConfiguration with a file stored in the plugin DataFolder
     * <p />
     * Will fail if plugin is null.
     *
     * @param file The name of the file
     * @param plugin The plugin registered to this Configuration
     */
    public EnhancedConfiguration(String file, Plugin plugin) {
        this(new File(plugin.getDataFolder(), file), plugin);
    }

    public EnhancedConfiguration(String file, Plugin plugin, boolean load) {
        this(new File(plugin.getDataFolder(), file), plugin, load);
    }

    /**
     * Creates a new EnhancedConfiguration with the file provided and a null {@link Plugin}
     *
     * @param file The file to store in this configuration
     */
    public EnhancedConfiguration(File file) {
        this(file, null);
    }

    public EnhancedConfiguration(File file, boolean load) {
        this(file, null, load);
    }

    /**
     * Creates a new EnhancedConfiguration with given File and Plugin.
     *
     * @param file The file to store in this configuration
     * @param plugin The plugin registered to this Configuration
     */
    public EnhancedConfiguration(File file, Plugin plugin) {
        this(file, plugin, true);
    }

    /**
     * Creates a new EnhancedConfiguration, with an option to load in the constructor
     *
     * @param file The file to store in this configuration
     * @param plugin The plugin registered to this Configuration
     * @param load True to load configuration in constructor
     */
    public EnhancedConfiguration(File file, Plugin plugin, boolean load) {
        this.file = file;
        this.plugin = plugin;
        options = new EnhancedConfigurationOptions(this);

        if (load) {
            load();
        }
    }

    /**
     * Loads set file
     * <p />
     * Does not load if file has not been changed since last load
     * <p />
     * Stores exception if possible.
     *
     * @return True on successful load
     */
    public boolean load() {
        if(last_modified != -1L && !isFileModified()) { // File hasn't been modified since last load
            return true;
        }

        try {
            clearCache();
            load(file);
            last_modified = file.lastModified();
            return true;
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Saves to the set file
     * <p />
     * Stores exception if possible.
     *
     * @return True on successful save
     */
    public boolean save() {
        try {
            save(file);
            modified = false;
            return true;
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Returns the last stored exception
     *
     * @return Last stored Exception
     */
    public Exception getLastException() {
        return exception;
    }

    /**
     * Loads defaults based off the name of stored file.
     * <p />
     * Stores exception if possible.
     * <p />
     * Will fail if Plugin is null.
     *
     * @return True on success
     */
    public boolean loadDefaults() {
        try {
            return loadDefaults(file.getName());
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Sets your defaults after loading the Plugin file.
     * <p />
     * Stores exception if possible.
     * <p />
     * Will fail if Plugin is null.
     *
     * @param filename File to load from Plugin jar
     * @return True on success
     */
    public boolean loadDefaults(String filename) {
        try {
            return loadDefaults(plugin.getResource(filename));
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Sets your defaults after loading them.
     * <p />
     * Stores exception if possible.
     *
     * @param filestream Stream to load defaults from
     * @return True on success, false otherwise.
     */
    public boolean loadDefaults(InputStream filestream) {
        try {
            setDefaults(loadConfiguration(filestream));
            clearCache();
            return true;
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Saves configuration with all defaults
     *
     * @return True if saved
     */
    public boolean saveDefaults() {
        options().copyDefaults(true); // These stay so future saves continue to copy over.
        options().copyHeader(true);
        return save();
    }

    /**
     * Check loaded defaults against current configuration
     *
     * @return false When all defaults aren't present in config
     */
    public boolean checkDefaults() {
        if (getDefaults() == null) {
            return true;
        }
        return getKeys(true).containsAll(getDefaults().getKeys(true));
    }

    /**
     * Clear the defaults from memory
     */
    public void clearDefaults() {
        setDefaults(new MemoryConfiguration());
        clearCache();
    }

    /**
     * Checks if the file exists, contains all defaults and if this configuration has been modified.
     *
     * @return True if the file should be updated (saved)
     */
    public boolean needsUpdate() {
        return !fileExists() || !checkDefaults() || isModified();
    }

    /**
     * Check if file associated with this configuration exists
     *
     * @return True if file exists, False if not, or if there was an exception.
     */
    public boolean fileExists() {
        try {
            return file.exists();
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    @Override
    public EnhancedConfigurationSection getConfigurationSection(String path) {
        // Get a casted section
        return (EnhancedConfigurationSection) super.getConfigurationSection(path);
    }

    @Override
    public EnhancedConfigurationSection createSection(String path) {
        Validate.notNull(path, "Path cannot be null");
        Validate.notEmpty(path, "Cannot create section at empty path");

        final char seperator = getRoot().options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        EnhancedConfigurationSection section = this;
        while ((i1 = path.indexOf(seperator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            EnhancedConfigurationSection subSection = section.getConfigurationSection(node);
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
    public EnhancedConfigurationSection createLiteralSection(String key) {
        EnhancedConfigurationSection section = new EnhancedMemorySection(this, this, key);
        map.put(key, section);
        return section;
    }

    /**
     * @return {@link EnhancedConfigurationOptions}
     */
    @Override
    public EnhancedConfigurationOptions options() {
        return (EnhancedConfigurationOptions) options;
    }

    @Override
    public Object get(String path, Object def) {
        Validate.notNull(path, "Path cannot be null");
        if (path.length() == 0) {
            return this;
        }

        Object value = cache.get(path);
        if (value != null) {
            return value;
        }

        final char seperator = getRoot().options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(seperator, i2 = i1 + 1)) != -1) {
            section = section.getConfigurationSection(path.substring(i2, i1));
            if (section == null) {
                return def;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            Object result = map.get(key);
            value = (result == null) ? def : result;
        } else {
            value = section.get(path, def);
        }

        if (value != null && !(value instanceof ConfigurationSection)) {
            cache.put(path, value);
        }

        return value;
    }

    @Override
    public void set(String path, Object value) {
        Validate.notNull(path, "Path cannot be null");
        Validate.notEmpty(path, "Cannot set to an empty path");

        if (value == null && cache.containsKey(path) || value != null && !value.equals(get(path))) {
            cache.remove(path);
            modified = true;
        }

        final char seperator = getRoot().options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        EnhancedConfigurationSection section = this;
        while ((i1 = path.indexOf(seperator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            EnhancedConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createLiteralSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            if (value == null) {
                map.remove(key);
            } else {
                map.put(key, value);
            }
        } else {
            section.set(key, value);
        }
    }

    /**
     * Removes the specified path from the configuration.
     * <p />
     * Equivalent to set(path, null).
     *
     * @param path The path to remove
     */
    public void unset(String path) {
        set(path, null);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        List<?> list = super.getList(path, def);
        return list == null ? new ArrayList(0) : list;
    }

    /**
     * Call this method to clear the cache manually.
     *
     * Automatically clears on "load"
     */
    public void clearCache() {
        cache.clear();
    }

    // Replaces \n with System line.separator
    @Override
    public String saveToString() { // TODO: Custom YAML loader/saver?
        String separator = System.getProperty("line.separator");
        if (separator.equals("\n")) { // Do nothing
            return super.saveToString();
        }
        return pattern.matcher(super.saveToString()).replaceAll(separator);
    }

    /**
     * @return The plugin associated with this configuration.
     */
    public Plugin getPlugin() {
        return plugin;
    }

    protected File getFile() {
        return file;
    }

    /**
     * Checks if loaded configuration (not the file) has been modified.
     *
     * @return True if local configuration has been modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Checks if file has been modified since last load().
     *
     * @return True if file has been modified
     */
    public boolean isFileModified() {
        try {
            return last_modified != file.lastModified();
        } catch (Exception e) {
            this.exception = e;
            return false;
        }
    }
}