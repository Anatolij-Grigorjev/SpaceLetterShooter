package com.tiem625.space_letter_shooter.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.util.fsm.StateMachine;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class Scene implements Disposable {

    protected final String sceneId;
    protected final List<Stage> stages;
    protected StateMachine<? extends Scene> fsm;

    private boolean paused;

    public Scene(String sceneId) {
        this.sceneId = sceneId;
        this.stages = new CopyOnWriteArrayList<>();
        this.paused = false;
    }

    public String getSceneId() {
        return sceneId;
    }

    public StateMachine<? extends Scene> getFsm() {
        return fsm;
    }

    public Stage addAndGetStage(Stage stage) {
        Objects.requireNonNull(stage);
        stages.add(stage);

        return stage;
    }

    protected Stage addFullScreenStage() {
        return addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
    }

    public Stream<Stage> stages() {
        return stages.stream();
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    public final void togglePause() {
        if (paused) resume();
        else pause();
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
        if (!paused) {
            stage.act(Gdx.graphics.getDeltaTime());
        }
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
