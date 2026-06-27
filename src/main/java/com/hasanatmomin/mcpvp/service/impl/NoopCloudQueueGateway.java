package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.api.CloudQueueGateway;
import com.hasanatmomin.mcpvp.model.QueueEntry;

import java.util.List;

public class NoopCloudQueueGateway implements CloudQueueGateway {
    @Override
    public List<QueueEntry> pullRemoteQueueSnapshot() {
        return List.of();
    }

    @Override
    public void publishQueueEntry(QueueEntry entry) {
        // TODO: wire this to Redis/Kafka/NATS for cross-server queue federation.
    }
}
