package fr.vmaxime.quests.utils;

import com.google.common.io.ByteStreams;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class Configuration {

    private final Plugin plugin;
    private final File file;
    private FileConfiguration config;

    /**
     * Creates easily Configuration file
     * @param plugin Plugin of the configuration file
     * @param fileName String without file extension
     */
    public Configuration(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "/"+fileName+".yml");
    }

    /**
     * Saves the raw contents of the default configuration file to the location
     * retrievable by {@link #getConfig()}.
     * <p>
     * This should fail silently if the config.yml already exists.
     */
    public void saveDefaultConfig() {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        try {
            if (!file.exists()) {
                file.createNewFile();
                InputStream inputStream = plugin.getResource(file.getName());
                OutputStream outputStream = new FileOutputStream(file);
                ByteStreams.copy(inputStream, outputStream);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves the {@link FileConfiguration} retrievable by {@link #getConfig()}.
     */
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the {@link FileConfiguration}, read through
     * his yaml file
     * <p>
     *
     * @return Configuration
     */
    public FileConfiguration getConfig() {
        if(config == null) this.config = YamlConfiguration.loadConfiguration(file);
        return config;
    }

    /**
     * Reloads the {@link FileConfiguration} from his file
     */
    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

}
