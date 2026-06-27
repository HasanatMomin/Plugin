package com.hasanatmomin.mcpvp.duel;

import com.hasanatmomin.mcpvp.matchmaking.QueueEntry;
import com.hasanatmomin.mcpvp.model.ServerRepresentation;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DuelSessionManager {

    // Quick lookup by Duel ID
    private final Map<UUID, DuelSession> activeSessions = new ConcurrentHashMap<>();
    
    // Quick lookup by Player UUID to find their active duel session
    private final Map<UUID, UUID> playerToDuelMap = new ConcurrentHashMap<>();

    /**
     * Creates and starts tracking a new duel session.
     */
    public DuelSession createSession(QueueEntry first, QueueEntry second, ServerRepresentation server) {
        UUID duelId = UUID.randomUUID();
        DuelSession session = new DuelSession(duelId, first, second, server, DuelState.PREPARING);
        
        activeSessions.put(duelId, session);
        
        // Map all player UUIDs in both queue entries to this duel
        first.getPlayers().forEach(playerUuid -> playerToDuelMap.put(playerUuid, duelId));
        second.getPlayers().forEach(playerUuid -> playerToDuelMap.put(playerUuid, duelId));
        
        return session;
    }

    /**
     * Gets a duel session by its unique ID.
     */
    public Optional<DuelSession> getSession(UUID duelId) {
        return Optional.ofNullable(activeSessions.get(duelId));
    }

    /**
     * Gets a duel session that a specific player is currently participating in.
     */
    public Optional<DuelSession> getSessionByPlayer(UUID playerUuid) {
        UUID duelId = playerToDuelMap.get(playerUuid);
        if (duelId == null) {
            return Optional.empty();
        }
        return getSession(duelId);
    }

    /**
     * Updates the state of an ongoing duel session.
     */
    public void updateSessionState(UUID duelId, DuelState newState) {
        getSession(duelId).ifPresent(session -> {
            session.setState(newState);
            
            // If the duel is finished or aborted, clean it up from memory
            if (newState == DuelState.COMPLETED || newState == DuelState.CANCELLED) {
                removeSession(duelId);
            }
        });
    }

    /**
     * Removes a duel session and unmaps its players.
     */
    public void removeSession(UUID duelId) {
        DuelSession session = activeSessions.remove(duelId);
        if (session != null) {
            session.getFirst().getPlayers().forEach(playerToDuelMap::remove);
            session.getSecond().getPlayers().forEach(playerToDuelMap::remove);
        }
    }

    /**
     * Returns a read-only collection of all currently tracked duel sessions.
     */
    public Collection<DuelSession> getAllActiveSessions() {
        return activeSessions.values();
    }
    
    /**
     * Checks if a player is currently in a duel.
     */
    public boolean isInDuel(UUID playerUuid) {
        return playerToDuelMap.containsKey(playerUuid);
    }
}
