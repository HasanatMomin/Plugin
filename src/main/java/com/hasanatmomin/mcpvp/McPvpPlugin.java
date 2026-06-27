package com.hasanatmomin.mcpvp;

import com.hasanatmomin.mcpvp.command.DuelCommand;
import com.hasanatmomin.mcpvp.command.McPvpCommand;
import com.hasanatmomin.mcpvp.command.QueueCommand;
import com.hasanatmomin.mcpvp.config.PluginConfig;
import com.hasanatmomin.mcpvp.duel.DuelSessionManager;
import com.hasanatmomin.mcpvp.leaderboard.RankingService;
import com.hasanatmomin.mcpvp.matchmaking.MatchmakingService;
import com.hasanatmomin.mcpvp.matchmaking.QueueService;
import com.hasanatmomin.mcpvp.storage.Database;
import com.hasanatmomin.mcpvp.storage.PlayerProfileRepository;
import com.hasanatmomin.mcpvp.storage.SQLiteDatabase;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class McPvpPlugin extends JavaPlugin {

    private PluginConfig config;
    private Database database;
    private PlayerProfileRepository profileRepository;
    private QueueService queueService;
    private RankingService rankingService;
    private DuelSessionManager duelSessionManager;
    private MatchmakingService matchmakingService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.config = new PluginConfig(this);
        this.database = new SQLiteDatabase(this, config.databaseFile());
        this.profileRepository = new PlayerProfileRepository(database, config.startingRating());
        this.rankingService = new RankingService(profileRepository, config.winGain(), config.lossLoss(), config.drawGain());
        this.queueService = new QueueService(config.maxQueueSize(), config.maxRatingGap());
        this.duelSessionManager = new DuelSessionManager(this, config, rankingService);
        this.matchmakingService = new MatchmakingService(queueService, profileRepository, duelSessionManager, config);

        database.initialize();
        profileRepository.createTableIfNeeded();

        registerCommands();

        getServer().getScheduler().runTaskTimer(this, () -> {
            if (config.autoMatch()) {
                matchmakingService.tick();
            }
        }, 20L, 20L);

        getLogger().info("McPvP enabled.");
    }

    @Override
    public void onDisable() {
        if (duelSessionManager != null) {
            duelSessionManager.shutdown();
        }
        if (database != null) {
            database.close();
        }
        getLogger().info("McPvP disabled.");
    }

    private void registerCommands() {
        PluginCommand mcpvp = getCommand("mcpvp");
        if (mcpvp != null) {
            McPvpCommand command = new McPvpCommand(queueService, rankingService, duelSessionManager);
            mcpvp.setExecutor(command);
            mcpvp.setTabCompleter(command);
        }

        PluginCommand queue = getCommand("queue");
        if (queue != null) {
            QueueCommand command = new QueueCommand(queueService);
            queue.setExecutor(command);
            queue.setTabCompleter(command);
        }

        PluginCommand duel = getCommand("duel");
        if (duel != null) {
            DuelCommand command = new DuelCommand(duelSessionManager);
            duel.setExecutor(command);
            duel.setTabCompleter(command);
        }
    }
}
