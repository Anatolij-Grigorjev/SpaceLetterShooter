package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class RuntimeConfig {

    private static final String KEY_RESOLUTION_WIDTH = "screen.resolution.x";
    private static final String KEY_RESOLUTION_HEIGHT = "screen.resolution.y";
    private static final String KEY_GAME_TITLE = "game.title";

    private static final String PROPS_FILE_LOCATION = "game_props.properties";

    private static final Map<String, Object> configValues = new HashMap<>();

    static {
        Properties propertiesSource;
        try {
            var propsFile = new Properties();
            propsFile.load(Gdx.files.internal(PROPS_FILE_LOCATION).reader());
            propertiesSource = propsFile;
        } catch (Exception propsProblem) {
            propsProblem.printStackTrace();
            propertiesSource = buildDefaultProps();
        }
        loadPropertiesIntoMap(propertiesSource, configValues);
    }

    private static Properties buildDefaultProps() {
        var props = new Properties();
        props.put(KEY_RESOLUTION_WIDTH, 1280);
        props.put(KEY_RESOLUTION_HEIGHT, 720);
        props.put(KEY_GAME_TITLE, "The Shooting Type");

        return props;
    }

    private static void loadPropertiesIntoMap(Properties source, Map<String, Object> destination) {
        destination.put(KEY_RESOLUTION_WIDTH, source.get(KEY_RESOLUTION_WIDTH));
        destination.put(KEY_RESOLUTION_HEIGHT, source.get(KEY_RESOLUTION_HEIGHT));
        destination.put(KEY_GAME_TITLE, source.get(KEY_GAME_TITLE));
    }

    private RuntimeConfig() {}

    public static Integer getScreenX() {
        return (Integer) configValues.get(KEY_RESOLUTION_WIDTH);
    }

    public static Integer getScreenY() {
        return (Integer) configValues.get(KEY_RESOLUTION_HEIGHT);
    }

    public static String getGameTitle() {
        return (String) configValues.get(KEY_GAME_TITLE);
    }
}
