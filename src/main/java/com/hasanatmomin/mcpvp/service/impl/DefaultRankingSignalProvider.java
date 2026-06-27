package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.api.RankingSignalProvider;

import java.util.UUID;

public class DefaultRankingSignalProvider implements RankingSignalProvider {
    @Override
    public int signalFor(UUID winner, UUID loser) {
        // TODO: include tournament rounds, head-to-head history, title bouts, and opponent quality.
        return 1;
    }
}
