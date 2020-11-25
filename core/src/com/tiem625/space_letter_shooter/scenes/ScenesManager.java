package com.tiem625.space_letter_shooter.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Optional;
import java.util.function.Consumer;

public enum ScenesManager {

    INSTANCE;

    private Scene currentScene;

    ScenesManager() {
        currentScene = null;
    }

    public boolean currentSceneHasId(SceneId sceneId) {
        return Optional.ofNullable(currentScene)
                .map(scene -> scene.getSceneId() == sceneId)
                .orElse(false);
    }

    public void setScene(Scene scene) {
        currentScene = scene;
    }

    private final Consumer<Stage> renderStage = stage -> {
      stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();
    };

    public void renderCurrentStages() {
        forStages(renderStage);
    }

    private void forStages(Consumer<Stage> stageActions) {
        currentStages.values().forEach(stageActions);
    }
}
