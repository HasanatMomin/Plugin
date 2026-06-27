package com.hasanatmomin.mcpvp.duel;

import com.hasanatmomin.mcpvp.matchmaking.QueueEntry;
import com.hasanatmomin.mcpvp.model.ServerRepresentation;

import java.util.UUID;

public class DuelSession {
    private final UUID duelId;
    private final QueueEntry first;
    private final QueueEntry second;
    private final ServerRepresentation server;
    private DuelState state;

    public DuelSession(UUID duelId, QueueEntry first, QueueEntry second, ServerRepresentation server, DuelState state) {
        this.duelId = duelId;
        this.first = first;
        this.second = second;
        this.server = server;
        this.state = state;
    }

    public UUID getDuelId() {
        return duelId;
    }

    public QueueEntry getFirst() {
        return first;
    }

    public QueueEntry getSecond() {
        return second;
    }

    public ServerRepresentation getServer() {
        return server;
    }

    public DuelState getState() {
        return state;
    }

    public void setState(DuelState state) {
        this.state = state;
    }
}
