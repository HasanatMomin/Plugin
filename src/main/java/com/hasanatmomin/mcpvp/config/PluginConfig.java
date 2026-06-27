package com.hasanatmomin.mcpvp.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginConfig {
    private final JavaPlugin plugin;

    public PluginConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private FileConfiguration cfg() {
        return plugin.getConfig();
    }

    public String databaseFile() {
        return cfg().getString("database.file", "plugins/McPvP/playerdata.db");
    }

    public int maxQueueSize() {
        return cfg().getInt("queue.max-size", 500);
    }

    public boolean autoMatch() {
        return cfg().getBoolean("queue.auto-match", true);
    }

    public int maxRatingGap() {
        return cfg().getInt("queue.max-rating-gap", 200);
    }

    public int countdownSeconds() {
        return cfg().getInt("duel.countdown-seconds", 5);
    }

    public boolean enableTeleport() {
        return cfg().getBoolean("duel.enable-teleport", false);
    }

    public int startingRating() {
        return cfg().getInt("ranking.starting-rating", 1000);
    }

    public int winGain() {
        return cfg().getInt("ranking.win-gain", 25);
    }

    public int lossLoss() {
        return cfg().getInt("ranking.loss-loss", 20);
    }

    public int drawGain() {
        return cfg().getInt("ranking.draw-gain", 5);
    }

    public String worldName() {
        return cfg().getString("arena.world-name", "world");
    }

    public String prefix() {
        return color(cfg().getString("messages.prefix", "&8[&bMcPvP&8] "));
    }

    public static String color(String input) {
        return input == null ? "" : input.replace("&", "§");
    }
}
