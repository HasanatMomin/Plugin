package com.hasanatmomin.mcpvp.api;

import java.util.UUID;

public interface RankingSignalProvider {
    int signalFor(UUID winner, UUID loser);
}
