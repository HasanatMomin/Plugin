package com.hasanatmomin.mcpvp.service;

import com.hasanatmomin.mcpvp.model.DuelSession;
import com.hasanatmomin.mcpvp.model.MatchPairing;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface DuelSessionService {
    DuelSession create(MatchPairing pairing);

    Optional<DuelSession> get(UUID duelId);

    Collection<DuelSession> activeSessions();

    boolean complete(UUID duelId, UUID winner);
}
