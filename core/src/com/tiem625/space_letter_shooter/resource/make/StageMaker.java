package com.tiem625.space_letter_shooter.resource.make;

import com.tiem625.space_letter_shooter.scenes.SceneId;
import com.tiem625.space_letter_shooter.scenes.ScenesManager;
import com.tiem625.space_letter_shooter.scenes.StageWithId;

public class StageMaker extends ResourceMaker {

    /**
     * Build a new {@link StageWithId}, using a stretching {@link com.badlogic.gdx.utils.viewport.Viewport} and
     * an internal {@link com.badlogic.gdx.graphics.g2d.SpriteBatch}
     * <p>
     * Throws a {@link RuntimeException} if a stage is already registered with the specified {@link SceneId}
     *
     * @param stageId the stage identifier to use for new stage creation
     * @return the created {@link StageWithId} instance, managed by the general {@link com.tiem625.space_letter_shooter.resource.ResourcesManager}
     */
    public static StageWithId buildWithId(SceneId stageId) {
        if (ScenesManager.INSTANCE.containsStageWithId(stageId)) {
            throw new RuntimeException("Already have registered stage with id " + stageId);
        }
        var stage = makeResource(() -> new StageWithId(stageId));
        ScenesManager.INSTANCE.setScene(stage);

        return stage;
    }
}
