package com.hasanatmomin.mcpvp.model;

import java.util.UUID;

public class PlayerProfile {
    private final UUID uuid;
    private String name;
    private int rating;
    private int wins;
    private int losses;
    private int draws;

    public PlayerProfile(UUID uuid, String name, int rating, int wins, int losses, int draws) {
        this.uuid = uuid;
        this.name = name;
        this.rating = rating;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = Math.max(0, rating);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = Math.max(0, wins);
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = Math.max(0, losses);
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = Math.max(0, draws);
    }
}
