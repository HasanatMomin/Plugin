package com.hasanatmomin.mcpvp.command;

import com.hasanatmomin.mcpvp.model.CompetitiveTier;
import com.hasanatmomin.mcpvp.model.DuelSession;
import com.hasanatmomin.mcpvp.model.LeaderboardEntry;
import com.hasanatmomin.mcpvp.model.QueueEntry;
import com.hasanatmomin.mcpvp.service.DuelSessionService;
import com.hasanatmomin.mcpvp.service.QueueService;
import com.hasanatmomin.mcpvp.service.RankingService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class McPvpCommand implements CommandExecutor, TabCompleter {
    private final QueueService queueService;
    private final DuelSessionService duelSessionService;
    private final RankingService rankingService;
    private final String defaultRegion;
    private final int leaderboardSize;

    public McPvpCommand(
            QueueService queueService,
            DuelSessionService duelSessionService,
            RankingService rankingService,
            String defaultRegion,
            int leaderboardSize
    ) {
        this.queueService = queueService;
        this.duelSessionService = duelSessionService;
        this.rankingService = rankingService;
        this.defaultRegion = defaultRegion;
        this.leaderboardSize = leaderboardSize;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /mcpvp <queue|duels|leaderboard>");
            return true;
        }

        String section = args[0].toLowerCase(Locale.ROOT);
        switch (section) {
            case "queue" -> handleQueue(sender, args);
            case "duels" -> handleDuels(sender, args);
            case "leaderboard" -> handleLeaderboard(sender);
            default -> sender.sendMessage(ChatColor.RED + "Unknown section. Use queue, duels, or leaderboard.");
        }
        return true;
    }

    private void handleQueue(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can manage queue state.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /mcpvp queue <join|leave|status> [region] [tier]");
            return;
        }

        UUID playerId = player.getUniqueId();
        String action = args[1].toLowerCase(Locale.ROOT);
        switch (action) {
            case "join" -> {
                String region = args.length > 2 ? args[2] : defaultRegion;
                CompetitiveTier tier = parseTier(args.length > 3 ? args[3] : null, rankingService.ratingOf(playerId));
                boolean added = queueService.enqueue(new QueueEntry(playerId, region, tier, Instant.now()));
                sender.sendMessage(added
                        ? ChatColor.GREEN + "Queued for competitive matchmaking in " + region + " (" + tier + ")"
                        : ChatColor.RED + "You are already in queue.");
            }
            case "leave" -> {
                boolean removed = queueService.remove(playerId);
                sender.sendMessage(removed ? ChatColor.GREEN + "Removed from queue." : ChatColor.RED + "You are not queued.");
            }
            case "status" -> {
                Optional<QueueEntry> entry = queueService.get(playerId);
                sender.sendMessage(entry
                        .map(queueEntry -> ChatColor.AQUA + "Queued in " + queueEntry.region() + " as " + queueEntry.tier())
                        .orElse(ChatColor.GRAY + "Not currently queued."));
            }
            default -> sender.sendMessage(ChatColor.RED + "Unknown queue action.");
        }
    }

    private CompetitiveTier parseTier(String value, int rating) {
        if (value == null || value.isBlank()) {
            return CompetitiveTier.fromRating(rating);
        }
        try {
            return CompetitiveTier.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return CompetitiveTier.fromRating(rating);
        }
    }

    private void handleDuels(CommandSender sender, String[] args) {
        if (args.length < 2 || !"list".equalsIgnoreCase(args[1])) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /mcpvp duels list");
            return;
        }

        List<DuelSession> sessions = new ArrayList<>(duelSessionService.activeSessions());
        if (sessions.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "No active duels.");
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "Active competitive duels:");
        for (DuelSession session : sessions) {
            sender.sendMessage(ChatColor.AQUA + "- " + session.getDuelId() + ChatColor.GRAY + " [" + session.getServer().serverId() + "@" + session.getServer().region() + "]");
        }
    }

    private void handleLeaderboard(CommandSender sender) {
        List<LeaderboardEntry> top = rankingService.top(leaderboardSize);
        if (top.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "Leaderboard has no entries yet.");
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "Top " + leaderboardSize + " competitive players:");
        for (int i = 0; i < top.size(); i++) {
            LeaderboardEntry entry = top.get(i);
            sender.sendMessage(ChatColor.YELLOW + "#" + (i + 1) + ChatColor.WHITE + " " + entry.playerId() + ChatColor.GRAY
                    + " rating=" + entry.rating() + " W/L=" + entry.wins() + "/" + entry.losses() + " tier=" + entry.tier());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("queue", "duels", "leaderboard");
        }
        if (args.length == 2 && "queue".equalsIgnoreCase(args[0])) {
            return List.of("join", "leave", "status");
        }
        if (args.length == 2 && "duels".equalsIgnoreCase(args[0])) {
            return List.of("list");
        }
        if (args.length == 4 && "queue".equalsIgnoreCase(args[0]) && "join".equalsIgnoreCase(args[1])) {
            return List.of("BRONZE", "SILVER", "GOLD", "PLATINUM", "DIAMOND", "MASTER");
        }
        return List.of();
    }
}
