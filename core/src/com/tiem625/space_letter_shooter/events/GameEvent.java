package com.tiem625.space_letter_shooter.events;

import com.tiem625.space_letter_shooter.scene.SceneId;

public abstract class GameEvent {

    final SceneId destination;

    protected GameEvent(SceneId sceneId) {
        this.destination = sceneId;
    }
}
