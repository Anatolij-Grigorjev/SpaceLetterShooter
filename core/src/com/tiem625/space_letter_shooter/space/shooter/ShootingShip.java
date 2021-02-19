package com.tiem625.space_letter_shooter.space.shooter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.space.enemy.EnemyShip;
import com.tiem625.space_letter_shooter.space.vessel.Vessel;

import static com.tiem625.space_letter_shooter.util.CommonActionsBuilders.buildShoveAction;

public class ShootingShip extends Group {

    private final Vessel shipModel;

    public ShootingShip(Vessel shipModel) {
        this.shipModel = shipModel;
        setWidth(shipModel.getWidth());
        setHeight(shipModel.getHeight());
        setOrigin(Align.center);
        addActor(shipModel);
    }

    public void shootAt(EnemyShip ship) {
        lookAt(ship);
        startShotFeedback();
    }

    private void lookAt(EnemyShip ship) {
        var enemyPosition = ship.getPosition();
        var position = new Vector2(getX(), getY());
        var angle = enemyPosition.angle(position);
        System.out.println(
                "enemy: " + enemyPosition
                        + " shooter: " + position
                        + " angle: " + angle
        );
        var angleSide = enemyPosition.x < position.x ? -1 : 1;
        setRotation(angleSide * angle);
    }

    private void startShotFeedback() {
        addAction(buildShoveAction(0f, -25f, 0.16f));
    }
}
