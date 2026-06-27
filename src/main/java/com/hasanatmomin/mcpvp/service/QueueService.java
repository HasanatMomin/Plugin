package com.hasanatmomin.mcpvp.service;

import com.hasanatmomin.mcpvp.model.QueueEntry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueService {
    boolean enqueue(QueueEntry entry);

    boolean remove(UUID playerId);

    Optional<QueueEntry> get(UUID playerId);

    List<QueueEntry> snapshot();
}
