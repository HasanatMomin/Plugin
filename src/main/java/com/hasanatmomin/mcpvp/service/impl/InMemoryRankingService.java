package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.model.CompetitiveTier;
import com.hasanatmomin.mcpvp.model.LeaderboardEntry;
import com.hasanatmomin.mcpvp.service.RankingService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRankingService implements RankingService {
    private static final int BASE_RATING = 1000;
    private static final int WIN_DELTA = 18;
    private static final int LOSS_DELTA = 14;

    private final Map<UUID, RatingCard> cards = new ConcurrentHashMap<>();

    @Override
    public void ensurePlayer(UUID playerId) {
        cards.computeIfAbsent(playerId, ignored -> new RatingCard());
    }

    @Override
    public void recordResult(UUID winner, UUID loser) {
        RatingCard winnerCard = cards.computeIfAbsent(winner, ignored -> new RatingCard());
        RatingCard loserCard = cards.computeIfAbsent(loser, ignored -> new RatingCard());

        winnerCard.rating += WIN_DELTA;
        winnerCard.wins++;

        loserCard.rating = Math.max(0, loserCard.rating - LOSS_DELTA);
        loserCard.losses++;
    }

    @Override
    public int ratingOf(UUID playerId) {
        return cards.getOrDefault(playerId, new RatingCard()).rating;
    }

    @Override
    public List<LeaderboardEntry> top(int limit) {
        return cards.entrySet().stream()
                .sorted(Map.Entry.<UUID, RatingCard>comparingByValue(Comparator.comparingInt(RatingCard::rating)).reversed())
                .limit(limit)
                .map(entry -> new LeaderboardEntry(
                        entry.getKey(),
                        entry.getValue().rating,
                        entry.getValue().wins,
                        entry.getValue().losses,
                        CompetitiveTier.fromRating(entry.getValue().rating)
                ))
                .toList();
    }

    private static final class RatingCard {
        private int rating = BASE_RATING;
        private int wins;
        private int losses;

        int rating() {
            return rating;
        }
    }
}
