package com.tiem625.space_letter_shooter.events;

import java.util.Map;

public enum GameEventType {

    SHIPS_AT_START,
    SHIP_REACH_BOTTOM_SCREEN,
    SHIP_DISPOSING,
    SCENE_RESTART,
    FSM_STATE_CHANGE,
    ;

    public GameEvent makeEvent() {
        return makeEvent(Map.of());
    }

    public GameEvent makeEvent(Map<String, Object> payload) {
        return new GameEvent(this, payload);
    }
}
