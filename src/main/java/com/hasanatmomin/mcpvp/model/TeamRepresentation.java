package com.hasanatmomin.mcpvp.model;

import java.util.UUID;

public record TeamRepresentation(
        UUID teamId,
        String displayName,
        String serverPool
) {
}
