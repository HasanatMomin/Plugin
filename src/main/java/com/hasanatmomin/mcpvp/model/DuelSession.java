package com.hasanatmomin.mcpvp.model;

import java.time.Instant;
import java.util.UUID;

public class DuelSession {
    private final UUID duelId;
    private final QueueEntry firstPlayer;
    private final QueueEntry secondPlayer;
    private final TeamRepresentation firstTeam;
    private final TeamRepresentation secondTeam;
    private final ServerRepresentation server;
    private final Instant createdAt;
    private DuelState state;

    public DuelSession(
            UUID duelId,
            QueueEntry firstPlayer,
            QueueEntry secondPlayer,
            TeamRepresentation firstTeam,
            TeamRepresentation secondTeam,
            ServerRepresentation server,
            DuelState state
    ) {
        this.duelId = duelId;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.server = server;
        this.createdAt = Instant.now();
        this.state = state;
    }

    public UUID getDuelId() {
        return duelId;
    }

    public QueueEntry getFirstPlayer() {
        return firstPlayer;
    }

    public QueueEntry getSecondPlayer() {
        return secondPlayer;
    }

    public TeamRepresentation getFirstTeam() {
        return firstTeam;
    }

    public TeamRepresentation getSecondTeam() {
        return secondTeam;
    }

    public ServerRepresentation getServer() {
        return server;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public DuelState getState() {
        return state;
    }

    public void transitionTo(DuelState nextState) {
        this.state = nextState;
    }
}
