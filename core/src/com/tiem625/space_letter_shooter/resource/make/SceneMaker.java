package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.resource.ResourceMaker;
import com.tiem625.space_letter_shooter.scenes.Scene;
import com.tiem625.space_letter_shooter.scenes.SceneId;

public class SceneMaker extends ResourceMaker {

    /**
     * Build a new {@link Scene}, using a provided {@link SceneId}
     *
     * @param sceneId the {@link SceneId} used to create the scene with
     * @return the created {@link Scene} instance, managed by the general {@link com.tiem625.space_letter_shooter.resource.ResourcesManager}
     */
    public static Scene buildEmptyWithId(SceneId sceneId) {
        return makeResource(() -> new Scene(sceneId));
    }

    public static Scene buildSpaceScene() {
        var scene = buildEmptyWithId(SceneId.SPACE);
        scene.addStage(new Stage());

        return scene;
    }
}
