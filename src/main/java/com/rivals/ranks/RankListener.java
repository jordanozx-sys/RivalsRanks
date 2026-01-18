package com.rivals.ranks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public final class RankListener implements Listener {
    private final RivalsRanksPlugin plugin;

    public RankListener(RivalsRanksPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        plugin.applyFormats(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!plugin.getConfig().getBoolean("chat.enabled", true)) return;

        Player p = event.getPlayer();
        String prefix = plugin.getPrefix(p);
        String fmt = plugin.getConfig().getString("chat.format", "{prefix}&f{player}&7: &f{message}");

        // Bukkit format placeholders: %1$s = player, %2$s = message
        String out = fmt
                .replace("{prefix}", prefix)
                .replace("{player}", "%1$s")
                .replace("{message}", "%2$s");

        event.setFormat(ColorUtil.color(out));
    }
}
