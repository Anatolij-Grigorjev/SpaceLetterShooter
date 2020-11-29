package com.tiem625.space_letter_shooter.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

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
    }

    public void setScene(Scene scene) {
        currentScene = scene;
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
                .ifPresent(stages -> {
                    stages.forEach(renderStage);
                });
    }

    private void renderAlwaysOnScenes() {
        alwaysOnScenes.stream()
                .map(Scene::getStages)
                .flatMap(Collection::stream)
                .forEach(renderStage);
    }
}
