package com.hasanatmomin.mcpvp.leaderboard;

import com.hasanatmomin.mcpvp.model.PlayerProfile;
import com.hasanatmomin.mcpvp.storage.PlayerProfileRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class RankingService {
    private final PlayerProfileRepository repository;
    private final int winGain;
    private final int lossLoss;
    private final int drawGain;

    public RankingService(PlayerProfileRepository repository, int winGain, int lossLoss, int drawGain) {
        this.repository = repository;
        this.winGain = winGain;
        this.lossLoss = lossLoss;
        this.drawGain = drawGain;
    }

    public PlayerProfile getOrCreate(UUID uuid, String name) {
        return repository.getOrCreate(uuid, name);
    }

    public PlayerProfile getOrCreate(UUID uuid) {
        return repository.getOrCreate(uuid, "Unknown");
    }

    public void recordWin(UUID winner, UUID loser) {
        PlayerProfile w = getOrCreate(winner);
        PlayerProfile l = getOrCreate(loser);
        w.setWins(w.getWins() + 1);
        w.setRating(w.getRating() + winGain);
        l.setLosses(l.getLosses() + 1);
        l.setRating(l.getRating() - lossLoss);
        repository.save(w);
        repository.save(l);
    }

    public void recordDraw(UUID first, UUID second) {
        PlayerProfile a = getOrCreate(first);
        PlayerProfile b = getOrCreate(second);
        a.setDraws(a.getDraws() + 1);
        b.setDraws(b.getDraws() + 1);
        a.setRating(a.getRating() + drawGain);
        b.setRating(b.getRating() + drawGain);
        repository.save(a);
        repository.save(b);
    }

    public int ratingOf(UUID uuid) {
        return getOrCreate(uuid).getRating();
    }

    public List<LeaderboardEntry> top(int limit) {
        return repository.all().stream()
                .sorted(Comparator.comparingInt(PlayerProfile::getRating).reversed())
                .limit(limit)
                .map(p -> new LeaderboardEntry(
                        p.getUuid(),
                        p.getName(),
                        p.getRating(),
                        p.getWins(),
                        p.getLosses(),
                        p.getDraws()
                ))
                .toList();
    }
}
