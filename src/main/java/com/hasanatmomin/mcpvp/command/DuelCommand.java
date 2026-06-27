package com.hasanatmomin.mcpvp.command;

import com.hasanatmomin.mcpvp.duel.DuelSessionManager;
import com.hasanatmomin.mcpvp.model.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DuelCommand implements CommandExecutor, TabCompleter {
    private final DuelSessionManager duelSessionManager;

    public DuelCommand(DuelSessionManager duelSessionManager) {
        this.duelSessionManager = duelSessionManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUsage: /duel <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("§cThat player is not online.");
            return true;
        }

        PlayerProfile first = new PlayerProfile(player.getUniqueId(), player.getName(), 1000, 0, 0, 0);
        PlayerProfile second = new PlayerProfile(target.getUniqueId(), target.getName(), 1000, 0, 0, 0);

        duelSessionManager.startDuel(first, second);
        player.sendMessage("§aDuel started with " + target.getName());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return List.of();
    }
}
