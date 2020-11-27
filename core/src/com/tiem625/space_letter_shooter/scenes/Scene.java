package com.tiem625.space_letter_shooter.scenes;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Scene implements Disposable {

    protected final SceneId sceneId;
    protected final Set<Stage> stages;

    public Scene(SceneId sceneId) {
        this.sceneId = sceneId;
        this.stages = new HashSet<>();
    }

    public void addStage(Stage stage) {
        Objects.requireNonNull(stage);
        stages.add(stage);
    }

    public SceneId getSceneId() {
        return sceneId;
    }

    public Set<Stage> getStages() {
        return stages;
    }

    /**
     * Get first {@link Stage} in this scene or an exception if no stages in this scene
     * @return the first present {@link Stage} in this scene
     */
    public Stage getFirstStage() {
        return stages.stream()
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void dispose() {
        stages.forEach(Stage::dispose);
    }
}
