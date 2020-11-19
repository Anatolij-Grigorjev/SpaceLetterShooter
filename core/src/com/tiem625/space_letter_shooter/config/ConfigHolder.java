package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.Properties;

public class ConfigHolder {

    private static final String PROPS_FILE_LOCATION = "game_props.properties";

    public static final GameConfig config = loadAvailableProperties();

    private static GameConfig loadAvailableProperties() {
        try {
            var propertiesSource = new Properties();
            FileHandle fileHandle = Gdx.files.external(PROPS_FILE_LOCATION);
            System.out.println("Reading props from file: " + fileHandle.file().getAbsolutePath());
            propertiesSource.load(fileHandle.reader());
            return new GameConfig(propertiesSource);
        } catch (RuntimeException | IOException propsProblem) {
            return new GameConfig();
        }
    }

    private ConfigHolder() {
    }

    public static void writeOutConfig() {
        FileHandle fileHandle = Gdx.files.external(PROPS_FILE_LOCATION);
        System.out.println("writing props to file: " + fileHandle.file().getAbsolutePath());
        var propsFileWriter = fileHandle.writer(false);
        try (propsFileWriter) {
            config.toPropsMap().forEach((key, value) -> {
                try {
                    String propLine = String.format("%s=%s\n", key, value);
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
