package com.hasanatmomin.mcpvp.service;

import com.hasanatmomin.mcpvp.model.LeaderboardEntry;

import java.util.List;
import java.util.UUID;

public interface RankingService {
    void ensurePlayer(UUID playerId);

    void recordResult(UUID winner, UUID loser);

    int ratingOf(UUID playerId);

    List<LeaderboardEntry> top(int limit);
}
