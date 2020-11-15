package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

public class RuntimeConfig {

    private static final String LOADING_PROP_MESSAGE_FORMAT = "Loading prop {%s=%s}\n";
    private static final String PROPS_FILE_LOCATION = "game_props.properties";

    private static final Map<ConfigProperty, Object> configValues = new HashMap<>();

    static {
        Properties propertiesSource;
        try {
            var propsFile = new Properties();
            FileHandle fileHandle = Gdx.files.external(PROPS_FILE_LOCATION);
            System.out.println("Reading props from file: " + fileHandle.file().getAbsolutePath());
            propsFile.load(fileHandle.reader());
            propertiesSource = propsFile;
        } catch (RuntimeException | IOException propsProblem) {
            propsProblem.printStackTrace();
            propertiesSource = buildDefaultProps();
        }
        loadPropertiesIntoMap(propertiesSource, configValues);
    }

    private static Properties buildDefaultProps() {
        var props = new Properties();
        Stream.of(ConfigProperty.values())
                .forEach(configProperty -> props.put(
                        configProperty.getPropKey(),
                        configProperty.getDefaultRaw()
                ));

        return props;
    }

    private static void loadPropertiesIntoMap(Properties source, Map<ConfigProperty, Object> destination) {
        Stream.of(ConfigProperty.values()).forEach(configProperty -> {
            String key = configProperty.getPropKey();
            String rawPropValue = source.getProperty(key);
            System.out.printf(LOADING_PROP_MESSAGE_FORMAT, key, rawPropValue);
            destination.put(configProperty, configProperty.loadProperty(rawPropValue));
        });
    }

    private RuntimeConfig() {
    }

    public static Integer getScreenX() {
        return (Integer) configValues.get(ConfigProperty.RESOLUTION_WIDTH);
    }

    public static Integer getScreenY() {
        return (Integer) configValues.get(ConfigProperty.RESOLUTION_HEIGHT);
    }

    public static String getGameTitle() {
        return (String) configValues.get(ConfigProperty.GAME_TITLE);
    }

    public static boolean isDynamicBackgroundEnabled() {
        return (boolean) configValues.get(ConfigProperty.ENABLE_DYNAMIC_BG);
    }

    public static void writeOutConfig() {
        FileHandle fileHandle = Gdx.files.external(PROPS_FILE_LOCATION);
        System.out.println("writing props to file: " + fileHandle.file().getAbsolutePath());
        var propsFileWriter = fileHandle.writer(false);
        try (propsFileWriter) {
            configValues.forEach((key, value) -> {
                try {
                    String propKey = key.getPropKey();
                    String propLine = String.format("%s=%s\n", propKey, value);
                    propsFileWriter.write(propLine);
                    System.out.print("Added prop to file: " + propLine);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
