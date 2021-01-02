package com.tiem625.space_letter_shooter.scene;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public enum ScenesManager {

    api;

    private final List<Scene> alwaysOnScenes;

    public Optional<Scene> currentScene() {
        return Optional.ofNullable(currentScene);
    }

    private Scene currentScene;

    ScenesManager() {
        alwaysOnScenes = new CopyOnWriteArrayList<>();
        currentScene = null;
    }

    public void addAlwaysOnScene(Scene scene) {
        Objects.requireNonNull(scene);
        alwaysOnScenes.add(scene);
    }

    public void setCurrentScene(Scene scene) {
        currentScene = scene;
    }

    public void renderActiveScenes() {

        renderAlwaysOnScenes();
        renderCurrentScene();
    }

    private void renderCurrentScene() {
        currentScene().ifPresent(Scene::render);
    }

    private void renderAlwaysOnScenes() {
        alwaysOnScenes.forEach(Scene::render);
    }
}
