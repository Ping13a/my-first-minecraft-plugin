package com.pluginjapan.superfly.listener;

import com.pluginjapan.superfly.SuperFlyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final SuperFlyPlugin plugin;

    public PlayerListener(SuperFlyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.cancelTask(player);
    }
}