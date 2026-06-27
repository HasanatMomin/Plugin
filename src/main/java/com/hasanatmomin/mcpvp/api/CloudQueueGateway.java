package com.hasanatmomin.mcpvp.api;

import com.hasanatmomin.mcpvp.model.QueueEntry;

import java.util.List;

public interface CloudQueueGateway {
    List<QueueEntry> pullRemoteQueueSnapshot();

    void publishQueueEntry(QueueEntry entry);
}
