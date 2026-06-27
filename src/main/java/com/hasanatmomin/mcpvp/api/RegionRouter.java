package com.hasanatmomin.mcpvp.api;

import com.hasanatmomin.mcpvp.model.QueueEntry;

public interface RegionRouter {
    String routeRegion(QueueEntry left, QueueEntry right);
}
