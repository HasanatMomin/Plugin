package com.hasanatmomin.mcpvp.matchmaking;

import java.util.UUID;

public record QueueEntry(
        UUID playerId,
        String playerName,
        int rating
) {
}
