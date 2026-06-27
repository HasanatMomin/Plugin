package com.hasanatmomin.mcpvp.storage;

import com.hasanatmomin.mcpvp.model.PlayerProfile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerProfileRepository {
    private final Database database;
    private final int startingRating;

    public PlayerProfileRepository(Database database, int startingRating) {
        this.database = database;
        this.startingRating = startingRating;
    }

    public void createTableIfNeeded() {
        // created by database init
    }

    public PlayerProfile getOrCreate(UUID uuid, String name) {
        PlayerProfile existing = find(uuid);
        if (existing != null) {
            if (name != null && !name.isBlank() && !existing.getName().equals(name)) {
                existing.setName(name);
                save(existing);
            }
            return existing;
        }

        PlayerProfile profile = new PlayerProfile(uuid, name, startingRating, 0, 0, 0);
        save(profile);
        return profile;
    }

    public PlayerProfile find(UUID uuid) {
        try (PreparedStatement ps = database.getConnection().prepareStatement(
                "SELECT uuid, name, rating, wins, losses, draws FROM player_profiles WHERE uuid = ?"
        )) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new PlayerProfile(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("name"),
                        rs.getInt("rating"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("draws")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load player profile", e);
        }
    }

    public void save(PlayerProfile profile) {
        try (PreparedStatement ps = database.getConnection().prepareStatement("""
            INSERT INTO player_profiles (uuid, name, rating, wins, losses, draws)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT(uuid) DO UPDATE SET
                name = excluded.name,
                rating = excluded.rating,
                wins = excluded.wins,
                losses = excluded.losses,
                draws = excluded.draws
        """)) {
            ps.setString(1, profile.getUuid().toString());
            ps.setString(2, profile.getName());
            ps.setInt(3, profile.getRating());
            ps.setInt(4, profile.getWins());
            ps.setInt(5, profile.getLosses());
            ps.setInt(6, profile.getDraws());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save player profile", e);
        }
    }

    public List<PlayerProfile> all() {
        List<PlayerProfile> result = new ArrayList<>();
        try (PreparedStatement ps = database.getConnection().prepareStatement(
                "SELECT uuid, name, rating, wins, losses, draws FROM player_profiles"
        );
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new PlayerProfile(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("name"),
                        rs.getInt("rating"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("draws")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list player profiles", e);
        }
        return result;
    }
}
