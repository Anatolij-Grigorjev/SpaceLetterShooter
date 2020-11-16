package com.tiem625.space_letter_shooter.stages;

import com.badlogic.gdx.Gdx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public void actCurrentStages() {
        actCurrentStages(Gdx.graphics.getDeltaTime());
    }

    public void actCurrentStages(float delta) {
        currentStages.values().forEach(stage -> stage.act(delta));
    }
}
