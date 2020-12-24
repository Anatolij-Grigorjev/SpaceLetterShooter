package com.tiem625.space_letter_shooter.space;

import com.tiem625.space_letter_shooter.events.GameEvent;
import com.tiem625.space_letter_shooter.scene.SceneId;

public class ShipReachBottomOfScreenEvent extends GameEvent {

    public final EnemyShip ship;

    protected ShipReachBottomOfScreenEvent(SceneId sceneId, EnemyShip ship) {
        super(sceneId);
        this.ship = ship;
    }
}
