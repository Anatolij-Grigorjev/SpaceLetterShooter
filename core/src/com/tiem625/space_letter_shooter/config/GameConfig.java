package com.tiem625.space_letter_shooter.config;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class GameConfig {

    private String gameTitle;
    private Boolean enableDynamicBg;
    private int resolutionWidth;
    private int resolutionHeight;

    public GameConfig() {
        this.gameTitle = "The Typed Shooter";
        this.enableDynamicBg = true;
        this.resolutionWidth = 1280;
        this.resolutionHeight = 720;
    }

    public GameConfig(Properties properties) {
        Objects.requireNonNull(properties, "Received null properties for config!");
        this.gameTitle = properties.getProperty("gameTitle");
        this.enableDynamicBg = Boolean.valueOf(properties.getProperty("enableDynamicBg"));
        this.resolutionWidth = Integer.parseInt(properties.getProperty("resolutionWidth"));
        this.resolutionHeight = Integer.parseInt(properties.getProperty("resolutionHeight"));
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public Boolean isDynamicBgEnabled() {
        return enableDynamicBg;
    }

    public void setEnableDynamicBg(Boolean enableDynamicBg) {
        this.enableDynamicBg = enableDynamicBg;
    }

    public int getResolutionWidth() {
        return resolutionWidth;
    }

    public void setResolutionWidth(int resolutionWidth) {
        this.resolutionWidth = resolutionWidth;
    }

    public int getResolutionHeight() {
        return resolutionHeight;
    }

    public void setResolutionHeight(int resolutionHeight) {
        this.resolutionHeight = resolutionHeight;
    }

    public Map<String, String> toPropsMap() {
        return Map.of(
                "gameTitle", gameTitle,
                "enableDynamicBg", enableDynamicBg.toString(),
                "resolutionWidth", "" + resolutionWidth,
                "resolutionHeight", "" + resolutionHeight
        );
    }
}
