package com.hasanatmomin.mcpvp.model;

import java.util.UUID;

public record LeaderboardEntry(
        UUID playerId,
        int rating,
        int wins,
        int losses,
        CompetitiveTier tier
) {
}
