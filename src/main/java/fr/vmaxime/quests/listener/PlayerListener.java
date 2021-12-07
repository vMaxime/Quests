package fr.vmaxime.quests.listener;

import fr.vmaxime.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        Quests.getInstance().loadProgressions(player.getUniqueId());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(Quests.getInstance(), () -> Quests.getInstance().saveProgressions(player.getUniqueId()));

    }

}
