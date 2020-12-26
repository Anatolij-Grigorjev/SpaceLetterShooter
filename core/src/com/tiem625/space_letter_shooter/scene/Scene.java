package com.tiem625.space_letter_shooter.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public abstract class Scene implements Disposable {

    protected final SceneId sceneId;

    protected final Set<Stage> stages;

    public Scene(SceneId sceneId) {
        this.sceneId = sceneId;
        this.stages = ConcurrentHashMap.newKeySet();
    }

    public Stage addAndGetStage(Stage stage) {
        Objects.requireNonNull(stage);
        stages.add(stage);

        return stage;
    }

    public Stream<Stage> stages() {
        return stages.stream();
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
