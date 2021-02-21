package com.tiem625.space_letter_shooter.space.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
        var newShooterAngle = startLookAtTarget(ship);
        createProjectileToPosition(newShooterAngle, ship);
        startShotFeedback();
    }

    private float startLookAtTarget(EnemyShip target) {
        var enemyPosition = target.getPosition();
        var position = new Vector2(getX(), getY());
        var angle = enemyPosition.angle(position);
        var angleSide = enemyPosition.x < position.x ? -1 : 1;
        var actualAngleDegrees = angle * angleSide;
        addAction(Actions.rotateTo(actualAngleDegrees, Gdx.graphics.getDeltaTime(), Interpolation.slowFast));

        return actualAngleDegrees;
    }

    private void createProjectileToPosition(float angle, EnemyShip ship) {
        var projectile = new ShotProjectile();
        projectile.setPosition(getX(), getY());
        projectile.setRotation(angle);
        projectile.startShotTo(ship.getPosition().sub(ship.getExtents()));
        getStage().addActor(projectile);
    }

    private void startShotFeedback() {
        addAction(buildShoveAction(0f, -25f, 0.16f));
    }
}
