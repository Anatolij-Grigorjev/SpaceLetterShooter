package com.tiem625.space_letter_shooter.resource.make;

import com.tiem625.space_letter_shooter.scenes.Scene;
import com.tiem625.space_letter_shooter.scenes.SceneId;

import java.util.function.Supplier;

public class SceneMaker extends ResourceMaker {

    /**
     * Build a new {@link Scene}, using a provided {@link SceneId}
     *
     * @param sceneMaker the generator to create a scene
     * @return the created {@link Scene} instance, managed by the general {@link com.tiem625.space_letter_shooter.resource.ResourcesManager}
     */
    public static Scene buildFrom(Supplier<? extends Scene> sceneMaker) {
        var scene = makeResource(sceneMaker);
        return scene;
    }
}
