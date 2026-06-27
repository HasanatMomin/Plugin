package com.hasanatmomin.mcpvp.leaderboard;

import java.util.UUID;

public record LeaderboardEntry(
        UUID playerId,
        String name,
        int rating,
        int wins,
        int losses,
        int draws
) {
}
