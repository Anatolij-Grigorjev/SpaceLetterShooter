package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class ShipTextCharsCaptureListener extends InputListener {

    private final SpaceScene scene;

    public ShipTextCharsCaptureListener(SpaceScene scene) {
        this.scene = scene;
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {

        scene.enemyShips()
                .filter(ship -> ship.canHitCharacter(character))
                .findFirst()
                .ifPresent(enemyShip -> enemyShip.hitCharacter(character));

        return true;
    }
}
