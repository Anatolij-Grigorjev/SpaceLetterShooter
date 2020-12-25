package com.tiem625.space_letter_shooter.events;

import com.tiem625.space_letter_shooter.scene.SceneId;

import java.util.Map;

public class GameEvent {

    public final SceneId destination;
    public final GameEventType type;
    public final Map<String, Object> payload;

    public GameEvent(SceneId destination, GameEventType type, Map<String, Object> payload) {
        this.destination = destination;
        this.type = type;
        this.payload = payload;
    }
}
