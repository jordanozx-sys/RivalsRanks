package com.rivals.ranks;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RivalsRanksPlugin extends JavaPlugin {
    private RankManager rankManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.rankManager = new RankManager(this);

        RankCommand cmd = new RankCommand(this);
        Objects.requireNonNull(getCommand("rank")).setExecutor(cmd);
        Objects.requireNonNull(getCommand("rank")).setTabCompleter(cmd);

        getServer().getPluginManager().registerEvents(new RankListener(this), this);

        for (Player p : getServer().getOnlinePlayers()) applyFormats(p);
        getLogger().info("RivalsRanks enabled.");
    }

    @Override
    public void onDisable() {
        if (rankManager != null) rankManager.save();
    }

    public RankManager rankManager() {
        return rankManager;
    }

    public void reloadAll() {
        reloadConfig();
        if (rankManager != null) rankManager.reload();
        for (Player p : getServer().getOnlinePlayers()) applyFormats(p);
    }

    public String getPrefix(Player player) {
        Rank rank = rankManager.getRank(player.getUniqueId(), player.isOp());
        String path = "ranks." + rank.name() + ".prefix";
        return ColorUtil.color(getConfig().getString(path, "&7[MEMBER]&r "));
    }

    public void applyFormats(Player player) {
        if (player == null) return;
        String prefix = getPrefix(player);

        if (getConfig().getBoolean("tab.enabled", true)) {
            String fmt = getConfig().getString("tab.format", "{prefix}&f{player}");
            String out = fmt.replace("{prefix}", prefix).replace("{player}", player.getName());
            try { player.setPlayerListName(ColorUtil.color(out)); } catch (Exception ignored) {}
        }

        try { player.setDisplayName(ColorUtil.color(prefix) + player.getName()); } catch (Exception ignored) {}
    }
}
