package com.tiem625.space_letter_shooter.events;

import java.util.Map;
import java.util.Optional;

public class GameEvent {

    public final GameEventType type;
    public final Map<String, Object> payload;

    public GameEvent(GameEventType type, Map<String, Object> payload) {
        this.type = type;
        this.payload = Optional.ofNullable(payload).orElse(Map.of());
    }
}
