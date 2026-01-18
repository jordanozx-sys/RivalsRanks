package com.rivals.ranks;

import java.util.Locale;

public enum Rank {
    OWNER(400),
    ADMIN(300),
    MOD(200),
    MEMBER(100);

    private final int weight;

    Rank(int weight) {
        this.weight = weight;
    }

    public int weight() {
        return weight;
    }

    public static Rank parse(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toUpperCase(Locale.ROOT);
        for (Rank r : values()) {
            if (r.name().equals(s)) return r;
        }
        return null;
    }
}
