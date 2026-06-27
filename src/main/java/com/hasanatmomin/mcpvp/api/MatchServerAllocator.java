package com.hasanatmomin.mcpvp.api;

import com.hasanatmomin.mcpvp.model.MatchPairing;
import com.hasanatmomin.mcpvp.model.ServerRepresentation;

import java.util.Optional;

public interface MatchServerAllocator {
    Optional<ServerRepresentation> allocate(MatchPairing pairing, String region);
}
