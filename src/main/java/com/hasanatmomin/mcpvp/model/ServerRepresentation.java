package com.hasanatmomin.mcpvp.model;

public record ServerRepresentation(
        String serverId,
        String region,
        int maxConcurrentDuels
) {
}
