package com.tiem625.space_letter_shooter.resource.make;

import com.tiem625.space_letter_shooter.resource.ResourceMaker;
import com.tiem625.space_letter_shooter.scenes.DebugScene;
import com.tiem625.space_letter_shooter.scenes.SpaceScene;

public class SceneMaker extends ResourceMaker {

    /**
     * Build an empty {@link SpaceScene} with an empty stage ready to put ships into.
     *
     * @return a newly constructed {@link SpaceScene}
     */
    public static SpaceScene buildSpaceScene() {
        return makeResource(SpaceScene::new);
    }

    public static DebugScene buildDebugScene() {
        return makeResource(DebugScene::new);
    }


}
