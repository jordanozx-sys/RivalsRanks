package com.rivals.ranks;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public final class RankManager {
    private final RivalsRanksPlugin plugin;
    private final File dataFile;
    private FileConfiguration data;

    public RankManager(RivalsRanksPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        reload();
    }

    public void reload() {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create data.yml: " + e.getMessage());
            }
        }
        this.data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void save() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml: " + e.getMessage());
        }
    }

    public Rank getRank(OfflinePlayer player) {
        return getRank(player.getUniqueId(), player.isOp());
    }

    public Rank getRank(UUID uuid, boolean isOp) {
        String raw = data.getString("players." + uuid);
        Rank parsed = Rank.parse(raw);
        if (parsed != null) return parsed;

        if (isOp) {
            Rank opDefault = Rank.parse(plugin.getConfig().getString("op-default-rank", "ADMIN"));
            return opDefault != null ? opDefault : Rank.ADMIN;
        }
        return Rank.MEMBER;
    }

    public void setRank(UUID uuid, Rank rank) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(rank);
        data.set("players." + uuid, rank.name());
        save();
    }
}
