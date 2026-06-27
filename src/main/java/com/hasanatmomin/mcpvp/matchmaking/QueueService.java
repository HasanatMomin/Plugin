package com.hasanatmomin.mcpvp.matchmaking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class QueueService {
    private final int maxSize;
    private final int maxRatingGap;
    private final Map<UUID, QueueEntry> queue = new LinkedHashMap<>();

    public QueueService(int maxSize, int maxRatingGap) {
        this.maxSize = maxSize;
        this.maxRatingGap = maxRatingGap;
    }

    public synchronized boolean join(QueueEntry entry) {
        if (queue.size() >= maxSize) {
            return false;
        }
        if (queue.containsKey(entry.playerId())) {
            return false;
        }
        queue.put(entry.playerId(), entry);
        return true;
    }

    public synchronized boolean leave(UUID playerId) {
        return queue.remove(playerId) != null;
    }

    public synchronized Optional<QueueEntry> get(UUID playerId) {
        return Optional.ofNullable(queue.get(playerId));
    }

    public synchronized List<QueueEntry> snapshot() {
        return new ArrayList<>(queue.values());
    }

    public int maxRatingGap() {
        return maxRatingGap;
    }

    public synchronized void clear() {
        queue.clear();
    }
}
