package com.hasanatmomin.mcpvp.service;

import com.hasanatmomin.mcpvp.model.MatchPairing;

import java.util.List;

public interface MatchmakingService {
    List<MatchPairing> producePairings();
}
