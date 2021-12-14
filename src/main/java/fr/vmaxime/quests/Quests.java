package fr.vmaxime.quests;

import fr.vmaxime.quests.listener.PlayerListener;
import fr.vmaxime.quests.quest.Quest;
import fr.vmaxime.quests.quest.QuestObjective;
import fr.vmaxime.quests.quest.QuestType;
import fr.vmaxime.quests.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Quests extends JavaPlugin {

    // INSTANCE
    private static Quests INSTANCE;

    // CONFIGS
    private Configuration questsConfiguration;

    // DATABASE
    private QuestsDatabase database;

    // QUESTS
    private List<Quest> quests;

    @Override
    public void onEnable() {

        // INSTANCE
        INSTANCE = this;

        // CONFIGS
        saveDefaultConfig();
        questsConfiguration = new Configuration(this, "quests");
        questsConfiguration.saveDefaultConfig();

        // QUESTS
        quests = new ArrayList<>();
        for (QuestType questType : QuestType.values()) {
            Quest quest = questType.newInstance();
            if (quest != null)
                quests.add(quest);
            else
                getLogger().severe("Couldn't instantiate quest " + questType.name() + ".");
        }
        loadQuests();

        // DATABASE
        database = new QuestsDatabase(getConfig().getString("host"), getConfig().getInt("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"));
        if (database.getConnection() == null && !database.isConnectionClosed()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // load online players quests progressions
        Bukkit.getOnlinePlayers().forEach(player -> loadProgressions(player.getUniqueId()));

        // LISTENER
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

    }

    @Override
    public void onDisable() {
        if (database != null && !database.isConnectionClosed())
            Bukkit.getOnlinePlayers().forEach(player -> saveProgressions(player.getUniqueId()));
    }

    /**
     * Gets the plugin instance
     * @return Instance of the plugin
     */
    public static Quests getInstance() {
        return INSTANCE;
    }

    /**
     * Gets registered quests
     * @return List of registered quests
     */
    public List<Quest> getQuests() {
        return quests;
    }

    /**
     * Loads all quests and their objectives from quests.yml
     */
    public void loadQuests() {
        FileConfiguration config = questsConfiguration.getConfig();
        quests.forEach(quest -> {
            // register quest listener
            getServer().getPluginManager().registerEvents(quest, this);
            // load objectives
            String key = quest.getType().name().toLowerCase(Locale.ROOT);
            config.getConfigurationSection(key).getKeys(false).forEach(objectiveAmountStr -> {
                int objectiveAmount = Integer.parseInt(objectiveAmountStr);
                String message = config.getString(key + "." + objectiveAmountStr + ".message");
                Sound sound = Sound.valueOf(config.getString(key + "." + objectiveAmountStr + ".sound", null));
                List<String> commands = config.getStringList(key + "." + objectiveAmountStr + ".commands");
                quest.addObjective(new QuestObjective(objectiveAmount, message, sound, commands));
            });
        });
    }

    /**
     * Loads progressions of all quests of a player from the database (asynchronously)
     * @param uuid Unique id of the player
     */
    public void loadProgressions(UUID uuid) {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            database.openConnection();
            getQuests().forEach(quest -> {
                int progression = database.getProgression(uuid, quest.getType());
                quest.setProgression(uuid, progression);
            });
            database.closeConnection();
        });
    }

    /**
     * Saves progressions of all quests of a player into the database (not asynchronously).
     * The player progression is removed from the cache.
     * @param uuid Unique id of the player
     */
    public void saveProgressions(UUID uuid) {
        database.openConnection();
        getQuests().forEach(quest -> {
            int progression = quest.getProgression(uuid);
            database.setProgression(uuid, quest.getType(), progression);
            quest.removeProgression(uuid);
        });
        database.closeConnection();
    }

}
