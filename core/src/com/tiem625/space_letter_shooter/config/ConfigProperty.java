package com.tiem625.space_letter_shooter.config;

import java.util.Objects;
import java.util.function.Function;

public enum ConfigProperty {

    RESOLUTION_WIDTH("screen.resolution.x", "1280", Integer::parseInt),
    RESOLUTION_HEIGHT("screen.resolution.y", "720", Integer::parseInt),
    ENABLE_DYNAMIC_BG("bg.stars.enable", "true", Boolean::valueOf),
    GAME_TITLE("game.title", "The Typed Shooter")
    ;

    private final String propKey;
    private final String defaultRaw;
    private final Function<String, Object> valueLoader;

    ConfigProperty(String key, String defaultRaw) {
        this(key, defaultRaw, value -> value);
    }

    ConfigProperty(String key, String defaultRaw, Function<String, Object> valueLoader) {
        Objects.requireNonNull(valueLoader);
        this.propKey = key;
        this.defaultRaw = defaultRaw;
        this.valueLoader = valueLoader;
    }

    public String getPropKey() {
        return propKey;
    }

    public String getDefaultRaw() {
        return defaultRaw;
    }

    public Object loadProperty(String rawValue) {
        return valueLoader.apply(rawValue);
    }
}
