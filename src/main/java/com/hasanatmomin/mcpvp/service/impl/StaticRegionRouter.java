package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.api.RegionRouter;
import com.hasanatmomin.mcpvp.model.QueueEntry;

public class StaticRegionRouter implements RegionRouter {
    private final String fallbackRegion;

    public StaticRegionRouter(String fallbackRegion) {
        this.fallbackRegion = fallbackRegion;
    }

    @Override
    public String routeRegion(QueueEntry left, QueueEntry right) {
        if (left.region().equalsIgnoreCase(right.region())) {
            return left.region();
        }
        // TODO: add ping-based dynamic routing for multi-region optimization.
        return fallbackRegion;
    }
}
