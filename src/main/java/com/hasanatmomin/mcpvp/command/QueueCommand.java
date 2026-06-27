package com.hasanatmomin.mcpvp.command;

import com.hasanatmomin.mcpvp.matchmaking.QueueEntry;
import com.hasanatmomin.mcpvp.matchmaking.QueueService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QueueCommand implements CommandExecutor, TabCompleter {
    private final QueueService queueService;

    public QueueCommand(QueueService queueService) {
        this.queueService = queueService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        String action = args.length == 0 ? "status" : args[0].toLowerCase();

        switch (action) {
            case "join" -> {
                QueueEntry entry = new QueueEntry(player.getUniqueId(), player.getName(), 1000);
                if (queueService.join(entry)) {
                    player.sendMessage("§aYou joined the queue.");
                } else {
                    player.sendMessage("§cYou are already queued or the queue is full.");
                }
            }
            case "leave" -> {
                if (queueService.leave(player.getUniqueId())) {
                    player.sendMessage("§eYou left the queue.");
                } else {
                    player.sendMessage("§cYou are not in the queue.");
                }
            }
            case "status" -> {
                if (queueService.get(player.getUniqueId()).isPresent()) {
                    player.sendMessage("§aYou are queued.");
                } else {
                    player.sendMessage("§7You are not queued.");
                }
            }
            default -> player.sendMessage("§cUsage: /queue [join|leave|status]");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("join", "leave", "status");
        }
        return List.of();
    }
}
