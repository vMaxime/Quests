package fr.vmaxime.quests.quest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public abstract class Quest implements Listener {

    private final QuestType type;
    private final List<QuestObjective> objectives;
    private final Map<UUID, Integer> progressions;

    public Quest(QuestType type) {
        this.type = type;
        this.objectives = new ArrayList<>();
        this.progressions = new HashMap<>();
    }

    /**
     * Gets the type of the quest
     * @return Quest type
     */
    public QuestType getType() {
        return type;
    }

    /**
     * Gets all the objectives of the quest
     * @return List of quest objectives
     */
    public List<QuestObjective> getObjectives() {
        return objectives;
    }

    /**
     * Registers an objective for the quest
     * @param objective Objective we want to add to the quest
     */
    public void addObjective(QuestObjective objective) {
        objectives.add(objective);
    }

    /**
     * Gets progression of player from his unique id
     * @param uuid Unique id of the player
     * @return Integer of the progression or 0
     */
    public int getProgression(UUID uuid) {
        return progressions.getOrDefault(uuid, 0);
    }

    /**
     * Sets progression of a player from his unique id
     * @param uuid Unique id of the player
     * @param value Integer of the new player progression
     */
    public void setProgression(UUID uuid, int value) {
        progressions.put(uuid, value);
        getObjectives().forEach(objective -> {
            // if progression reaches an objective amount
            if (objective.getAmount() == value) {
                OfflinePlayer ofPlayer = Bukkit.getOfflinePlayer(uuid);
                // play sound & send message
                if (ofPlayer.isOnline()) {
                    Player player = ofPlayer.getPlayer();
                    if (objective.getSound() != null)
                        player.playSound(player.getLocation(), objective.getSound(), 2f, 2f);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', objective.getMessage()));
                }
                // execute commands
                objective.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("@player", ofPlayer.getName())));
            }
        });
    }

    /**
     * Adds a certain amount on the current player progression
     * @param uuid Unique id of the player
     * @param amount Amount we want to add to the player progression
     */
    public void addProgression(UUID uuid, int amount) {
        progressions.put(uuid, getProgression(uuid) + amount);
    }

    /**
     * Removes player progression from the cache
     * @param uuid Unique id of the player
     */
    public void removeProgression(UUID uuid) {
        progressions.remove(uuid);
    }



}
