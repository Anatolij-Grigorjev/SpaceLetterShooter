package com.tiem625.space_letter_shooter.resource.make;

import com.tiem625.space_letter_shooter.background.AlwaysOnBGScene;
import com.tiem625.space_letter_shooter.config.DebugScene;
import com.tiem625.space_letter_shooter.space.SceneConfigureSpecs;
import com.tiem625.space_letter_shooter.space.SpaceScene;

import java.io.IOException;

public class SceneMaker extends ResourceMaker {

    /**
     * Build an empty {@link SpaceScene} with an empty stage ready to put ships into.
     *
     * @return a newly constructed {@link SpaceScene}
     */
    public static SpaceScene buildSpaceScene(String sceneId) throws IOException {
        var sceneSpec = SceneConfigureSpecs.api.getSceneConfigureSpec(sceneId);
        return makeResource(() -> new SpaceScene(sceneSpec));
    }

    public static AlwaysOnBGScene buildAlwaysOnBGScene() {
        return makeResource(AlwaysOnBGScene::new);
    }

    public static DebugScene buildDebugScene() {
        return makeResource(DebugScene::new);
    }


}
