package com.pluginjapan.superfly;

import com.pluginjapan.superfly.command.FlyCommand;
import com.pluginjapan.superfly.listener.PlayerListener;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SuperFlyPlugin extends JavaPlugin {

    private final Map<UUID, ScheduledTask> activeParticleTasks = new HashMap<>();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        getLogger().info("========================================");
        getLogger().info("  SuperFly Plugin has been ENABLED!");
        getLogger().info("  Author: Steve");
        getLogger().info("  Platform: Folia Compatible");
        getLogger().info("  Version: 1.0.0");
        getLogger().info("========================================");

        this.getCommand("fly").setExecutor(new FlyCommand(this));
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("SuperFly Plugin has been DISABLED. Cleaning up tasks...");
        cancelAllTasks();
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public Map<UUID, ScheduledTask> getActiveParticleTasks() {
        return activeParticleTasks;
    }

    public void cancelTask(Player player) {
        ScheduledTask task = activeParticleTasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    private void cancelAllTasks() {
        for (ScheduledTask task : activeParticleTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        activeParticleTasks.clear();
    }
}