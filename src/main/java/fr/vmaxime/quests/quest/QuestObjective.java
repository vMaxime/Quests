package fr.vmaxime.quests.quest;

import org.bukkit.Sound;

import java.util.List;

public class QuestObjective {

    private final int amount;
    private final String message;
    private final Sound sound;
    private final List<String> commands;

    public QuestObjective(int amount, String message, Sound sound, List<String> commands) {
        this.amount = amount;
        this.message = message;
        this.sound = sound;
        this.commands = commands;
    }

    /**
     * Gets the objective amount to reach for the quest
     * @return Integer of the objective
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the message sent on quest finished
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the sound played on quest finished
     * @return Sound or null
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Gets command executed on quest finished
     * @return List of command
     */
    public List<String> getCommands() {
        return commands;
    }

}
