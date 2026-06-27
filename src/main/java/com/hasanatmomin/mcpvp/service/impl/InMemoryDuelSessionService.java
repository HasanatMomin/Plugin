package com.hasanatmomin.mcpvp.service.impl;

import com.hasanatmomin.mcpvp.api.ClientFeatureBridge;
import com.hasanatmomin.mcpvp.api.MatchServerAllocator;
import com.hasanatmomin.mcpvp.api.RegionRouter;
import com.hasanatmomin.mcpvp.model.DuelSession;
import com.hasanatmomin.mcpvp.model.DuelState;
import com.hasanatmomin.mcpvp.model.MatchPairing;
import com.hasanatmomin.mcpvp.model.TeamRepresentation;
import com.hasanatmomin.mcpvp.service.DuelSessionService;
import com.hasanatmomin.mcpvp.service.RankingService;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDuelSessionService implements DuelSessionService {
    private final Map<UUID, DuelSession> sessions = new ConcurrentHashMap<>();
    private final RegionRouter regionRouter;
    private final MatchServerAllocator matchServerAllocator;
    private final RankingService rankingService;
    private final ClientFeatureBridge clientFeatureBridge;

    public InMemoryDuelSessionService(
            RegionRouter regionRouter,
            MatchServerAllocator matchServerAllocator,
            RankingService rankingService,
            ClientFeatureBridge clientFeatureBridge
    ) {
        this.regionRouter = regionRouter;
        this.matchServerAllocator = matchServerAllocator;
        this.rankingService = rankingService;
        this.clientFeatureBridge = clientFeatureBridge;
    }

    @Override
    public DuelSession create(MatchPairing pairing) {
        String region = regionRouter.routeRegion(pairing.left(), pairing.right());
        UUID duelId = UUID.randomUUID();

        DuelSession session = new DuelSession(
                duelId,
                pairing.left(),
                pairing.right(),
                new TeamRepresentation(UUID.nameUUIDFromBytes((pairing.left().playerId().toString() + "-team").getBytes()), "Alpha", "primary"),
                new TeamRepresentation(UUID.nameUUIDFromBytes((pairing.right().playerId().toString() + "-team").getBytes()), "Bravo", "primary"),
                matchServerAllocator.allocate(pairing, region)
                        .orElseThrow(() -> new IllegalStateException("No server available for duel")),
                DuelState.CREATED
        );
        session.transitionTo(DuelState.PREPARING);
        session.transitionTo(DuelState.ACTIVE);
        sessions.put(duelId, session);

        clientFeatureBridge.notifyDuelStarted(pairing.left().playerId(), duelId.toString());
        clientFeatureBridge.notifyDuelStarted(pairing.right().playerId(), duelId.toString());

        rankingService.ensurePlayer(pairing.left().playerId());
        rankingService.ensurePlayer(pairing.right().playerId());
        return session;
    }

    @Override
    public Optional<DuelSession> get(UUID duelId) {
        return Optional.ofNullable(sessions.get(duelId));
    }

    @Override
    public Collection<DuelSession> activeSessions() {
        return sessions.values();
    }

    @Override
    public boolean complete(UUID duelId, UUID winner) {
        DuelSession session = sessions.get(duelId);
        if (session == null || session.getState() != DuelState.ACTIVE) {
            return false;
        }

        UUID leftId = session.getFirstPlayer().playerId();
        UUID rightId = session.getSecondPlayer().playerId();
        UUID loser = leftId.equals(winner) ? rightId : leftId;

        rankingService.recordResult(winner, loser);
        session.transitionTo(DuelState.COMPLETED);
        sessions.remove(duelId);
        return true;
    }
}
