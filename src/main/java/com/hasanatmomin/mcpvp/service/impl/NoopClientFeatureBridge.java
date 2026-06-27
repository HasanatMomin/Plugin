package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.api.ClientFeatureBridge;

import java.util.UUID;

public class NoopClientFeatureBridge implements ClientFeatureBridge {
    @Override
    public void notifyDuelStarted(UUID playerId, String duelId) {
        // TODO: integrate optional client mod channels for richer competitive UX.
    }
}
