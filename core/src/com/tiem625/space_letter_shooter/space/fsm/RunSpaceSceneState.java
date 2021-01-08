package com.tiem625.space_letter_shooter.space.fsm;

import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.SpaceScene;

public class RunSpaceSceneState extends SceneState<SpaceScene> {

    public static final String KEY = "RUN_SPACE_SCENE";

    public RunSpaceSceneState() {
        super(KEY);
    }
}
