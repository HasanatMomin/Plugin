package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.model.QueueEntry;
import com.hasanatmomin.mcpvp.service.QueueService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryQueueService implements QueueService {
    private final Map<UUID, QueueEntry> queueByPlayer = new LinkedHashMap<>();

    @Override
    public synchronized boolean enqueue(QueueEntry entry) {
        if (queueByPlayer.containsKey(entry.playerId())) {
            return false;
        }
        queueByPlayer.put(entry.playerId(), entry);
        return true;
    }

    @Override
    public synchronized boolean remove(UUID playerId) {
        return queueByPlayer.remove(playerId) != null;
    }

    @Override
    public synchronized Optional<QueueEntry> get(UUID playerId) {
        return Optional.ofNullable(queueByPlayer.get(playerId));
    }

    @Override
    public synchronized List<QueueEntry> snapshot() {
        return new ArrayList<>(queueByPlayer.values());
    }
}
