package com.tiem625.space_letter_shooter.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class Scene implements Disposable {

    protected final SceneId sceneId;
    protected final List<Stage> stages;

    public Scene(SceneId sceneId) {
        this.sceneId = sceneId;
        this.stages = new CopyOnWriteArrayList<>();
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
     *
     * @return the first present {@link Stage} in this scene
     */
    public Stage getFirstStage() {
        return stages().findFirst()
                .orElseThrow();
    }

    private final Consumer<Stage> renderStage = stage -> {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    };

    public void render() {
        stages().forEachOrdered(renderStage);
    }

    @Override
    public void dispose() {
        stages().forEachOrdered(Stage::dispose);
    }
}
