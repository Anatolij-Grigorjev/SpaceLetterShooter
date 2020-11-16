package com.tiem625.space_letter_shooter.stages;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A kind of {@link Stage} that has an associated typed closed-set identifier of type {@link StageId}
 */
public class StageWithId extends Stage {

    private final StageId stageId;

    public StageId getStageId() {
        return stageId;
    }

    public StageWithId(StageId stageId) {
        super();
        this.stageId = stageId;
    }

    public StageWithId(StageId stageId, Viewport viewport) {
        super(viewport);
        this.stageId = stageId;
    }

    public StageWithId(StageId stageId, Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.stageId = stageId;
    }
}
