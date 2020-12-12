package com.tiem625.space_letter_shooter.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public enum ScenesManager {

    INSTANCE;

    private final Set<Scene> alwaysOnScenes;

    public Optional<Scene> currentScene() {
        return Optional.ofNullable(currentScene);
    }

    private Scene currentScene;

    ScenesManager() {
        alwaysOnScenes = new HashSet<>();
        currentScene = null;
    }

    public void addAlwaysOnScene(Scene scene) {
        Objects.requireNonNull(scene);
        alwaysOnScenes.add(scene);
    }

    public void setCurrentScene(Scene scene) {
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
        currentScene()
                .map(Scene::stages)
                .ifPresent(stages -> stages.forEach(renderStage));
    }

    private void renderAlwaysOnScenes() {
        alwaysOnScenes.stream()
                .flatMap(Scene::stages)
                .forEach(renderStage);
    }
}
