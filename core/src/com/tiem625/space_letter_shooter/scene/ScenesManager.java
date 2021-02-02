package com.tiem625.space_letter_shooter.scene;

import com.badlogic.gdx.Gdx;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public enum ScenesManager {

    api;

    private final List<Scene> alwaysOnScenes;

    private Scene currentScene;

    ScenesManager() {
        alwaysOnScenes = new CopyOnWriteArrayList<>();
        currentScene = null;
    }

    public void addAlwaysOnScene(Scene scene) {
        Objects.requireNonNull(scene);
        alwaysOnScenes.add(scene);
    }

    public Optional<Scene> currentScene() {
        return Optional.ofNullable(currentScene);
    }

    public void setCurrentScene(Scene scene) {
        currentScene = scene;
    }

    public void renderActiveScenes() {

        renderAlwaysOnScenes();
        renderCurrentScene();
    }

    private void renderCurrentScene() {
        currentScene()
                .map(Scene::getFsm)
                .ifPresent(fsm -> fsm.act(Gdx.graphics.getDeltaTime()));
    }

    private void renderAlwaysOnScenes() {
        alwaysOnScenes.forEach(Scene::render);
    }
}
