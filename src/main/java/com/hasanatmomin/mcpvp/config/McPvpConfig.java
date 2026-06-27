package com.hasanatmomin.mcpvp.config;

public record McPvpConfig(
        String defaultRegion,
        int matchmakingIntervalTicks,
        int maxTierGap,
        int leaderboardSize
) {
}
