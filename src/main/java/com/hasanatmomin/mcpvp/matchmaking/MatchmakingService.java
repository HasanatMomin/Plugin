package com.hasanatmomin.mcpvp.matchmaking;

import com.hasanatmomin.mcpvp.config.PluginConfig;
import com.hasanatmomin.mcpvp.duel.DuelSessionManager;
import com.hasanatmomin.mcpvp.model.PlayerProfile;
import com.hasanatmomin.mcpvp.storage.PlayerProfileRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MatchmakingService {
    private final QueueService queueService;
    private final PlayerProfileRepository profileRepository;
    private final DuelSessionManager duelSessionManager;
    private final PluginConfig config;

    public MatchmakingService(QueueService queueService,
                              PlayerProfileRepository profileRepository,
                              DuelSessionManager duelSessionManager,
                              PluginConfig config) {
        this.queueService = queueService;
        this.profileRepository = profileRepository;
        this.duelSessionManager = duelSessionManager;
        this.config = config;
    }

    public void tick() {
        List<QueueEntry> snapshot = new ArrayList<>(queueService.snapshot());
        if (snapshot.size() < 2) {
            return;
        }

        boolean[] used = new boolean[snapshot.size()];

        for (int i = 0; i < snapshot.size(); i++) {
            if (used[i]) continue;

            QueueEntry first = snapshot.get(i);
            QueueEntry best = null;
            int bestIndex = -1;
            int bestGap = Integer.MAX_VALUE;

            for (int j = i + 1; j < snapshot.size(); j++) {
                if (used[j]) continue;

                QueueEntry second = snapshot.get(j);
                int gap = Math.abs(first.rating() - second.rating());
                if (gap <= queueService.maxRatingGap() && gap < bestGap) {
                    best = second;
                    bestIndex = j;
                    bestGap = gap;
                }
            }

            if (best != null) {
                used[i] = true;
                used[bestIndex] = true;
                queueService.leave(first.playerId());
                queueService.leave(best.playerId());

                Player p1 = Bukkit.getPlayer(first.playerId());
                Player p2 = Bukkit.getPlayer(best.playerId());

                if (p1 != null && p2 != null) {
                    PlayerProfile a = profileRepository.getOrCreate(first.playerId(), first.playerName());
                    PlayerProfile b = profileRepository.getOrCreate(best.playerId(), best.playerName());
                    duelSessionManager.startDuel(a, b);
                }
            }
        }
    }
}
