package com.hasanatmomin.mcpvp.model;

import java.time.Instant;
import java.util.UUID;

public record QueueEntry(
        UUID playerId,
        String region,
        CompetitiveTier tier,
        Instant joinedAt
) {
}
