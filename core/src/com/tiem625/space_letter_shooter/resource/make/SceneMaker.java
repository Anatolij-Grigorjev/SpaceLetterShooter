package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiem625.space_letter_shooter.background.AlwaysOnBGScene;
import com.tiem625.space_letter_shooter.config.DebugScene;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.dto.SceneConfigureSpec;

import java.io.IOException;

public class SceneMaker extends ResourceMaker {

    /**
     * Build an empty {@link SpaceScene} with an empty stage ready to put ships into.
     *
     * @return a newly constructed {@link SpaceScene}
     */
    public static SpaceScene buildSpaceScene(String sceneConfigSpecPath) throws IOException {
        var sceneSpec = new ObjectMapper()
                .readValue(Gdx.files.internal(sceneConfigSpecPath).read(), SceneConfigureSpec.class);
        var newScene = makeResource(SpaceScene::new);
        newScene.load(sceneSpec);
        return newScene;
    }

    public static AlwaysOnBGScene buildAlwaysOnBGScene() {
        return makeResource(AlwaysOnBGScene::new);
    }

    public static DebugScene buildDebugScene() {
        return makeResource(DebugScene::new);
    }


}
