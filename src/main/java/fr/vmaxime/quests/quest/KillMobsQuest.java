package fr.vmaxime.quests.quest;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class KillMobsQuest extends Quest {

    public KillMobsQuest() {
        super(QuestType.KILL_MOBS);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDeath(EntityDamageByEntityEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Monster) || event.getDamager().getType() != EntityType.PLAYER)
            return;

        if (((Monster) event.getEntity()).getHealth() - event.getFinalDamage() > 0)
            return;

        Player player = (Player) event.getDamager();
        addProgression(player.getUniqueId(), 1);

    }

}
