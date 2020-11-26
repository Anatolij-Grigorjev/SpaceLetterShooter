package com.tiem625.space_letter_shooter.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;

import java.util.*;
import java.util.function.Consumer;

public enum ScenesManager {

    INSTANCE;

    private final Set<Scene> alwaysOnScenes;
    private Scene currentScene;

    ScenesManager() {
        alwaysOnScenes = new HashSet<>();
        currentScene = null;
    }

    public void addAlwaysOnScene(Scene scene) {
        Objects.requireNonNull(scene);
        alwaysOnScenes.add(scene);
        scene.getStages().forEach(InputProcessorManager::addAlwaysOnInputProcessor);
    }

    public boolean currentSceneHasId(SceneId sceneId) {
        return Optional.ofNullable(currentScene)
                .map(scene -> scene.getSceneId() == sceneId)
                .orElse(false);
    }

    public void setScene(Scene scene) {
        currentScene = scene;
        Optional.ofNullable(scene)
                .ifPresent(presentScene -> {
                    InputProcessorManager.setCurrentInputProcessors(
                            presentScene.getStages().toArray(new InputProcessor[0]));
                });
    }

    private final Consumer<Stage> renderStage = stage -> {
      stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();
    };

    public void renderActiveScenes() {

        renderAlwaysOnScenes();
        renderCurrentScene();
    }

    private void renderCurrentScene() {
        Optional.ofNullable(currentScene)
                .map(Scene::getStages)
                .ifPresentOrElse(stages -> {
                    stages.forEach(renderStage);
                }, () -> {
                    System.out.println("No current scene stages!");
                });
    }

    private void renderAlwaysOnScenes() {
        alwaysOnScenes.stream()
                .map(Scene::getStages)
                .flatMap(Collection::stream)
                .forEach(renderStage);
    }
}
