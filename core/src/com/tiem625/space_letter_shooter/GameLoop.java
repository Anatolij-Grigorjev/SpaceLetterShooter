package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.resource.ResourcesDisposer;
import com.tiem625.space_letter_shooter.resource.make.BitmapFontMaker;
import com.tiem625.space_letter_shooter.resource.make.SceneMaker;
import com.tiem625.space_letter_shooter.resource.make.SpriteBatchMaker;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.scene.ScenesManager;

import java.io.IOException;
import java.util.Objects;

public class GameLoop extends ApplicationAdapter {
    SpriteBatch batch;

    @Override
    public void create() {
        batch = SpriteBatchMaker.buildDefault();
        Fonts.ENEMY_TEXT_NORMAL_FONT = BitmapFontMaker.buildEnemyShipNormalFont();
        Fonts.MAIN_UI_FONT = BitmapFontMaker.buildMain();

        if (isGameDebugProfile()) {
            addAlwaysOnDebugScene();
        }

        try {
            var spaceScene = SceneMaker.buildSpaceScene("space_scene_1.json");
            ScenesManager.INSTANCE.setCurrentScene(spaceScene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var alwaysOnBGScene = SceneMaker.buildAlwaysOnBGScene();
        ScenesManager.INSTANCE.addAlwaysOnScene(alwaysOnBGScene);

        setCurrentSceneAsInput();
        GamePropsHolder.applyCurrentGameConfig();
    }

    private boolean isGameDebugProfile() {
        return Objects.equals(System.getProperty("game.profile.active"), "debug");
    }

    private void addAlwaysOnDebugScene() {
        System.out.println("Game in debug profile! Enabling debug config changer...");
        Scene debugScene = SceneMaker.buildDebugScene();
        ScenesManager.INSTANCE.addAlwaysOnScene(debugScene);
        InputProcessorManager.addAlwaysOnInputProcessor(debugScene.getFirstStage());
    }

    private void setCurrentSceneAsInput() {
        ScenesManager.INSTANCE.currentScene()
                .ifPresent(scene ->
                        InputProcessorManager.setCurrentInputProcessors(scene.stages().toArray(Stage[]::new)));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        ScenesManager.INSTANCE.renderActiveScenes();
        batch.end();
    }

    @Override
    public void dispose() {
        ResourcesDisposer.INSTANCE.disposeAll();
        GamePropsHolder.writeOutConfig();
    }
}
