package com.hasanatmomin.mcpvp.command;

import com.hasanatmomin.mcpvp.duel.DuelSessionManager;
import com.hasanatmomin.mcpvp.leaderboard.LeaderboardEntry;
import com.hasanatmomin.mcpvp.leaderboard.RankingService;
import com.hasanatmomin.mcpvp.matchmaking.QueueService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class McPvpCommand implements CommandExecutor, TabCompleter {
    private final QueueService queueService;
    private final RankingService rankingService;
    private final DuelSessionManager duelSessionManager;

    public McPvpCommand(QueueService queueService, RankingService rankingService, DuelSessionManager duelSessionManager) {
        this.queueService = queueService;
        this.rankingService = rankingService;
        this.duelSessionManager = duelSessionManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§bMcPvP §7- competitive PvP infrastructure");
            sender.sendMessage("§7Queued players: §f" + queueService.snapshot().size());
            sender.sendMessage("§7Active duels: §f" + duelSessionManager.activeSessions().size());
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "leaderboard" -> {
                sender.sendMessage("§6Top players:");
                List<LeaderboardEntry> top = rankingService.top(10);
                for (int i = 0; i < top.size(); i++) {
                    LeaderboardEntry e = top.get(i);
                    sender.sendMessage("§e#" + (i + 1) + " §f" + e.name() + " §7(" + e.rating() + " RP)");
                }
            }
            default -> sender.sendMessage("§cUsage: /mcpvp [leaderboard]");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("leaderboard");
        }
        return List.of();
    }
}
