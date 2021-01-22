package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.resource.ResourceLoader;
import com.tiem625.space_letter_shooter.resource.ResourcesDisposer;
import com.tiem625.space_letter_shooter.resource.make.BitmapFontMaker;
import com.tiem625.space_letter_shooter.resource.make.SceneMaker;
import com.tiem625.space_letter_shooter.resource.make.SpriteBatchMaker;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.scene.ScenesManager;
import com.tiem625.space_letter_shooter.space.SceneScripts;
import com.tiem625.space_letter_shooter.space.ShipRenderSpecs;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

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
        
        loadStartupResources();

        var spaceScene = buildSpaceScene();
        setRenderAndInputToScene(spaceScene);

        var alwaysOnBGScene = SceneMaker.buildAlwaysOnBGScene();
        ScenesManager.api.addAlwaysOnScene(alwaysOnBGScene);

        GamePropsHolder.applyCurrentGameConfig();

        EventsHandling.addEventHandler(GameEventType.SCENE_RESTART, restart -> {
            var scene = buildSpaceScene();
            setRenderAndInputToScene(scene);
        });
    }

    private Scene buildSpaceScene() {
        try {
            return SceneMaker.buildSpaceScene("SS1");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load space scene at SS1");
        }
    }

    private void setRenderAndInputToScene(Scene scene) {
        ScenesManager.api.setCurrentScene(scene);
        setCurrentSceneAsInput();
    }

    private void loadStartupResources() {
        Stream.<ResourceLoader>of(
                ShipRenderSpecs.api,
                SceneScripts.api
        ).forEach(ResourceLoader::loadResources);
    }

    private boolean isGameDebugProfile() {
        return Objects.equals(System.getProperty("game.profile.active"), "debug");
    }

    private void addAlwaysOnDebugScene() {
        System.out.println("Game in debug profile! Enabling debug config changer...");
        Scene debugScene = SceneMaker.buildDebugScene();
        ScenesManager.api.addAlwaysOnScene(debugScene);
        InputProcessorManager.addAlwaysOnInputProcessor(debugScene.getFirstStage());
    }

    private void setCurrentSceneAsInput() {
        ScenesManager.api.currentScene()
                .ifPresent(scene ->
                        InputProcessorManager.setCurrentInputProcessors(scene.stages().toArray(Stage[]::new)));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        ScenesManager.api.renderActiveScenes();
        batch.end();
    }

    @Override
    public void dispose() {
        ResourcesDisposer.api.disposeAll();
        GamePropsHolder.writeOutConfig();
    }
}
