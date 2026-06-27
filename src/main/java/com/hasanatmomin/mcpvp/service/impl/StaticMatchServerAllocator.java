package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.api.MatchServerAllocator;
import com.hasanatmomin.mcpvp.model.MatchPairing;
import com.hasanatmomin.mcpvp.model.ServerRepresentation;

import java.util.Optional;

public class StaticMatchServerAllocator implements MatchServerAllocator {
    @Override
    public Optional<ServerRepresentation> allocate(MatchPairing pairing, String region) {
        // TODO: replace static allocation with temporary match server provisioning.
        return Optional.of(new ServerRepresentation("duel-node-1", region, 64));
    }
}
