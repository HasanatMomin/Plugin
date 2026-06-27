package com.hasanatmomin.mcpvp.api;

import java.util.UUID;

public interface ClientFeatureBridge {
    void notifyDuelStarted(UUID playerId, String duelId);
}
