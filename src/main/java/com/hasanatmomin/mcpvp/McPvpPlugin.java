package com.hasanatmomin.mcpvp;

import com.hasanatmomin.mcpvp.command.McPvpCommand;
import com.hasanatmomin.mcpvp.config.McPvpConfig;
import com.hasanatmomin.mcpvp.model.MatchPairing;
import com.hasanatmomin.mcpvp.service.DuelSessionService;
import com.hasanatmomin.mcpvp.service.MatchmakingService;
import com.hasanatmomin.mcpvp.service.QueueService;
import com.hasanatmomin.mcpvp.service.RankingService;
import com.hasanatmomin.mcpvp.service.impl.DefaultRankingSignalProvider;
import com.hasanatmomin.mcpvp.service.impl.InMemoryDuelSessionService;
import com.hasanatmomin.mcpvp.service.impl.InMemoryQueueService;
import com.hasanatmomin.mcpvp.service.impl.InMemoryRankingService;
import com.hasanatmomin.mcpvp.service.impl.NoopClientFeatureBridge;
import com.hasanatmomin.mcpvp.service.impl.NoopCloudQueueGateway;
import com.hasanatmomin.mcpvp.service.impl.SimpleMatchmakingService;
import com.hasanatmomin.mcpvp.service.impl.StaticMatchServerAllocator;
import com.hasanatmomin.mcpvp.service.impl.StaticRegionRouter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class McPvpPlugin extends JavaPlugin {
    private QueueService queueService;
    private MatchmakingService matchmakingService;
    private DuelSessionService duelSessionService;
    private RankingService rankingService;
    private McPvpConfig config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.config = new McPvpConfig(
                getConfig().getString("default-region", "global"),
                getConfig().getInt("matchmaking-interval-ticks", 20),
                getConfig().getInt("max-tier-gap", 1),
                getConfig().getInt("leaderboard-size", 10)
        );

        this.queueService = new InMemoryQueueService();
        this.matchmakingService = new SimpleMatchmakingService(queueService, config.maxTierGap());
        this.rankingService = new InMemoryRankingService();
        this.duelSessionService = new InMemoryDuelSessionService(
                new StaticRegionRouter(config.defaultRegion()),
                new StaticMatchServerAllocator(),
                rankingService,
                new NoopClientFeatureBridge()
        );

        new NoopCloudQueueGateway();
        new DefaultRankingSignalProvider();

        McPvpCommand command = new McPvpCommand(queueService, duelSessionService, rankingService, config.defaultRegion(), config.leaderboardSize());
        if (getCommand("mcpvp") != null) {
            getCommand("mcpvp").setExecutor(command);
            getCommand("mcpvp").setTabCompleter(command);
        }

        getServer().getScheduler().runTaskTimer(this, this::runMatchmakingTick, 20L, config.matchmakingIntervalTicks());
        getLogger().info("McPvp competitive infrastructure plugin enabled.");
    }

    private void runMatchmakingTick() {
        List<MatchPairing> pairings = matchmakingService.producePairings();
        for (MatchPairing pairing : pairings) {
            duelSessionService.create(pairing);
        }
    }
}
