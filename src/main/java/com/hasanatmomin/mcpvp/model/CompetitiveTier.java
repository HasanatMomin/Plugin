package com.hasanatmomin.mcpvp.model;

public enum CompetitiveTier {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND,
    MASTER;

    public static CompetitiveTier fromRating(int rating) {
        if (rating < 900) return BRONZE;
        if (rating < 1100) return SILVER;
        if (rating < 1300) return GOLD;
        if (rating < 1500) return PLATINUM;
        if (rating < 1700) return DIAMOND;
        return MASTER;
    }
}
