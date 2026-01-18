package com.rivals.ranks;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class RankCommand implements CommandExecutor, TabCompleter {
    private final RivalsRanksPlugin plugin;

    public RankCommand(RivalsRanksPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return help(sender);

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "list" -> {
                sender.sendMessage(ColorUtil.color("&aRanks: &fOWNER, ADMIN, MOD, MEMBER"));
                return true;
            }
            case "reload" -> {
                if (!sender.hasPermission("rivalsranks.reload")) {
                    sender.sendMessage(ColorUtil.color("&cNo permission."));
                    return true;
                }
                plugin.reloadAll();
                sender.sendMessage(ColorUtil.color("&aRivalsRanks reloaded."));
                return true;
            }
            case "get" -> {
                if (!sender.hasPermission("rivalsranks.get")) {
                    sender.sendMessage(ColorUtil.color("&cNo permission."));
                    return true;
                }
                OfflinePlayer target = (args.length >= 2) ? Bukkit.getOfflinePlayer(args[1]) : (sender instanceof Player p ? p : null);
                if (target == null) {
                    sender.sendMessage(ColorUtil.color("&cUsage: /rank get <player>"));
                    return true;
                }
                Rank r = plugin.rankManager().getRank(target);
                sender.sendMessage(ColorUtil.color("&a" + target.getName() + " &7is &f" + r.name()));
                return true;
            }
            case "set" -> {
                if (!sender.hasPermission("rivalsranks.set")) {
                    sender.sendMessage(ColorUtil.color("&cNo permission."));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(ColorUtil.color("&cUsage: /rank set <player> <OWNER|ADMIN|MOD|MEMBER>"));
                    return true;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                Rank rank = Rank.parse(args[2]);
                if (rank == null) {
                    sender.sendMessage(ColorUtil.color("&cUnknown rank. Use /rank list"));
                    return true;
                }
                plugin.rankManager().setRank(target.getUniqueId(), rank);
                sender.sendMessage(ColorUtil.color("&aSet &f" + target.getName() + " &ato &f" + rank.name()));

                if (target.isOnline()) {
                    Player tp = target.getPlayer();
                    if (tp != null) {
                        plugin.applyFormats(tp);
                        tp.sendMessage(ColorUtil.color("&aYour rank is now &f" + rank.name()));
                    }
                }
                return true;
            }
            default -> {
                return help(sender);
            }
        }
    }

    private boolean help(CommandSender sender) {
        sender.sendMessage(ColorUtil.color("&6RivalsRanks &7- commands"));
        sender.sendMessage(ColorUtil.color("&e/rank get [player] &7- show rank"));
        sender.sendMessage(ColorUtil.color("&e/rank set <player> <rank> &7- set rank"));
        sender.sendMessage(ColorUtil.color("&e/rank list &7- list ranks"));
        sender.sendMessage(ColorUtil.color("&e/rank reload &7- reload config"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("get", "set", "list", "reload").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(n -> n.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            return Arrays.stream(Rank.values()).map(Enum::name)
                    .filter(n -> n.startsWith(args[2].toUpperCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
