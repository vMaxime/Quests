package fr.vmaxime.quests.quest;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakQuest extends Quest {

    public BreakQuest() {
        super(QuestType.BREAK);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        addProgression(player.getUniqueId(), 1);

    }

}
