package com.tiem625.space_letter_shooter.scene;

import com.tiem625.space_letter_shooter.util.fsm.State;

public abstract class SceneState<S extends Scene> extends State<S> {

    public SceneState(String key, S scene) {
        super(key);
        this.entity = scene;
    }

    @Override
    public void act(float delta) {
        entity.render();
    }
}
