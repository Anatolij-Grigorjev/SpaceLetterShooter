package com.tiem625.space_letter_shooter.events;

import java.util.Map;

public enum GameEventType {

    SHIPS_AT_START,
    SHIP_REACH_BOTTOM_SCREEN,
    SHIP_SPELLED,
    SHIP_GONE,
    SCRIPT_CLEAR,
    SCENE_TOGGLE_PAUSE,
    SCENE_RESTART,
    SCENE_QUIT,
    FSM_STATE_CHANGE,
    ;

    public GameEvent makeEvent() {
        return makeEvent(Map.of());
    }

    public GameEvent makeEvent(Map<String, Object> payload) {
        return new GameEvent(this, payload);
    }
}
