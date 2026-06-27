package com.hasanatmomin.mcpvp.model;

import java.util.UUID;

public record PvPTeam(
        UUID teamId,
        String name,
        String serverName
) {
}
