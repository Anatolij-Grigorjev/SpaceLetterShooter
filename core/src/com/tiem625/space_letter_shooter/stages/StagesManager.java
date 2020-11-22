package com.tiem625.space_letter_shooter.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public enum StagesManager {

    INSTANCE;

    private final Map<StageId, StageWithId> currentStages;

    StagesManager() {
        currentStages = new ConcurrentHashMap<>();
    }

    public boolean containsStageWithId(StageId stageId) {
        return currentStages.containsKey(stageId);
    }

    public StageWithId addStageWithId(StageWithId stage) {
        return currentStages.put(stage.getStageId(), stage);
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
