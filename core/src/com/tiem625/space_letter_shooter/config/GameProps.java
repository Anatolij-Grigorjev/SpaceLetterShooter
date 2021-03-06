package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.math.Vector2;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class GameProps {

    private String gameTitle;
    private Boolean enabledDynamicBg;
    private int resolutionWidth;
    private int resolutionHeight;

    public GameProps() {
        this.gameTitle = "The Typed Shooter";
        this.enabledDynamicBg = true;
        this.resolutionWidth = 1280;
        this.resolutionHeight = 720;
    }

    public GameProps(Properties properties) {
        Objects.requireNonNull(properties, "Received null properties for config!");
        this.gameTitle = properties.getProperty("gameTitle");
        this.enabledDynamicBg = Boolean.valueOf(properties.getProperty("enableDynamicBg"));
        this.resolutionWidth = Integer.parseInt(properties.getProperty("resolutionWidth"));
        this.resolutionHeight = Integer.parseInt(properties.getProperty("resolutionHeight"));
    }

    public String getGameTitle() {
        return gameTitle;
    }

    void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public Boolean isEnabledDynamicBg() {
        return enabledDynamicBg;
    }

    void setEnabledDynamicBg(Boolean enableDynamicBg) {
        this.enabledDynamicBg = enableDynamicBg;
    }

    public void toggleEnabledDynamicBg() {
        this.enabledDynamicBg = !this.enabledDynamicBg;
    }

    public int getResolutionWidth() {
        return resolutionWidth;
    }

    void setResolutionWidth(int resolutionWidth) {
        this.resolutionWidth = resolutionWidth;
    }

    public int getResolutionHeight() {
        return resolutionHeight;
    }

    void setResolutionHeight(int resolutionHeight) {
        this.resolutionHeight = resolutionHeight;
    }

    Map<String, String> toPropsMap() {
        return Map.of(
                "gameTitle", gameTitle,
                "enableDynamicBg", enabledDynamicBg.toString(),
                "resolutionWidth", "" + resolutionWidth,
                "resolutionHeight", "" + resolutionHeight
        );
    }

    public Vector2 getResolution() {
        return new Vector2(resolutionWidth, resolutionHeight);
    }
}
