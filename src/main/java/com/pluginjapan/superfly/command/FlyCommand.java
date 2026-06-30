package com.pluginjapan.superfly.command;

import com.pluginjapan.superfly.SuperFlyPlugin;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    private final SuperFlyPlugin plugin;

    public FlyCommand(SuperFlyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMiniMessage().deserialize("<red>このコマンドはプレイヤーのみ実行可能です。</red>"));
            return true;
        }

        if (!player.hasPermission("superfly.use")) {
            player.sendMessage(plugin.getMiniMessage().deserialize("<red>権限がありません。</red>"));
            return true;
        }

        boolean isFlying = player.getAllowFlight();

        if (!isFlying) {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(plugin.getMiniMessage().deserialize("<gradient:#00ff00:#55ff55><bold>[SuperFly]</bold> 飛行モードが有効になりました！</gradient>"));
            startParticleTask(player);
        } else {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage(plugin.getMiniMessage().deserialize("<gradient:#ff3333:#ff7777><bold>[SuperFly]</bold> 飛行モードが無効になりました。</gradient>"));
            plugin.cancelTask(player);
        }

        return true;
    }

    private void startParticleTask(Player player) {
        plugin.cancelTask(player);

        ScheduledTask task = player.getScheduler().runAtFixedRate(plugin, (scheduledTask) -> {
            if (!player.isOnline() || !player.isAllowFlight() || !player.isFlying()) {
                scheduledTask.cancel();
                plugin.getActiveParticleTasks().remove(player.getUniqueId());
                return;
            }

            Location loc = player.getLocation();
            player.getWorld().spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    loc.getX(),
                    loc.getY(),
                    loc.getZ(),
                    8,
                    0.3,
                    0.1,
                    0.3,
                    0.0
            );
        }, null, 1L, 4L);

        plugin.getActiveParticleTasks().put(player.getUniqueId(), task);
    }
}