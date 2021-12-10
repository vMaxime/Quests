package fr.vmaxime.quests.quest;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class WalkQuest extends Quest {

    public WalkQuest() {
        super(QuestType.WALK);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        if (event.getFrom().getBlock().getLocation().distanceSquared(event.getTo().getBlock().getLocation()) >= 1)
            addProgression(player.getUniqueId(), 1);
    }

    /**
     * Gets the amount of blocks traveled by the player
     * @param player Player we want to get his blocks traveled amount
     * @return Integer of the blocks traveled
     */
    public int getBlocksTraveled(Player player) {
        return player.getStatistic(Statistic.SPRINT_ONE_CM) + player.getStatistic(Statistic.SWIM_ONE_CM) + player.getStatistic(Statistic.WALK_ONE_CM);
    }

}
