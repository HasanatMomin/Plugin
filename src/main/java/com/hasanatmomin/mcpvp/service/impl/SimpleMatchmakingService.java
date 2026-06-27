package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.model.MatchPairing;
import com.hasanatmomin.mcpvp.model.QueueEntry;
import com.hasanatmomin.mcpvp.service.MatchmakingService;
import com.hasanatmomin.mcpvp.service.QueueService;

import java.util.ArrayList;
import java.util.List;

public class SimpleMatchmakingService implements MatchmakingService {
    private final QueueService queueService;
    private final int maxTierGap;

    public SimpleMatchmakingService(QueueService queueService, int maxTierGap) {
        this.queueService = queueService;
        this.maxTierGap = maxTierGap;
    }

    @Override
    public List<MatchPairing> producePairings() {
        List<QueueEntry> queued = queueService.snapshot();
        List<MatchPairing> pairings = new ArrayList<>();
        boolean[] used = new boolean[queued.size()];

        for (int i = 0; i < queued.size(); i++) {
            if (used[i]) {
                continue;
            }
            QueueEntry first = queued.get(i);
            for (int j = i + 1; j < queued.size(); j++) {
                if (used[j]) {
                    continue;
                }
                QueueEntry second = queued.get(j);
                if (!first.region().equalsIgnoreCase(second.region())) {
                    continue;
                }
                int tierGap = Math.abs(first.tier().ordinal() - second.tier().ordinal());
                if (tierGap > maxTierGap) {
                    continue;
                }
                pairings.add(new MatchPairing(first, second));
                used[i] = true;
                used[j] = true;
                break;
            }
        }

        for (MatchPairing pairing : pairings) {
            queueService.remove(pairing.left().playerId());
            queueService.remove(pairing.right().playerId());
        }
        return pairings;
    }
}
