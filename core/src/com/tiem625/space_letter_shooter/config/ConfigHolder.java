package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;
import com.tiem625.space_letter_shooter.resource.make.SceneMaker;
import com.tiem625.space_letter_shooter.scenes.Scene;
import com.tiem625.space_letter_shooter.scenes.SceneId;
import com.tiem625.space_letter_shooter.scenes.ScenesManager;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ConfigHolder {

    private static final String PROPS_FILE_LOCATION = "game_props.properties";

    public static final GameConfig config = loadAvailableConfig();

    private static GameConfig loadAvailableConfig() {
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

    static {
        var isGameDebugProfile = Objects.equals(System.getProperty("game.profile.active"), "debug");
        if (isGameDebugProfile) {
            System.out.println("Game in debug profile! Enabling debug config changer...");
            Scene debugScene = buildDebugScene();
            ScenesManager.INSTANCE.addAlwaysOnScene(debugScene);
            InputProcessorManager.addAlwaysOnInputProcessor(debugScene.getFirstStage());
        }
    }

    private static Scene buildDebugScene() {

        var gameConfigChangeScene = SceneMaker.buildWithId(SceneId.DEBUG);
        var stage = new Stage();
        stage.addListener(new ChangeGameConfigInputListener());
        gameConfigChangeScene.addStage(stage);

        return gameConfigChangeScene;
    }

    private ConfigHolder() {
        throw new ClassIsStaticException(getClass());
    }

    public static void applyCurrentGameConfig() {
        Gdx.graphics.setTitle(config.getGameTitle());
        Gdx.graphics.setWindowedMode(config.getResolutionWidth(), config.getResolutionHeight());
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

    private static class ChangeGameConfigInputListener extends InputListener {
        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            switch (keycode) {

                case Input.Keys.P:
                    config.setEnabledDynamicBg(!config.isEnabledDynamicBg());
                    break;
                default:
                    System.out.println("Got key input with no action: " + keycode);
            }

            return true;
        }
    }
}
