package com.tiem625.space_letter_shooter.space.shooter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tiem625.space_letter_shooter.space.ColorOverlay;

public class ShotProjectile extends Group {

    private final ColorOverlay body;
    //pixels / s
    private final float VELOCITY = 750;

    public ShotProjectile() {
        this.body = new ColorOverlay(Color.WHITE, new Rectangle(0, 0, 15, 30));
        addActor(body);
    }

    public void startShotTo(Vector2 target) {

        var shotDuration = Vector2.dst(getX(), getY(), target.x, target.y) / VELOCITY;
        addAction(Actions.sequence(
                Actions.moveTo(target.x, target.y, shotDuration, Interpolation.linear),
                Actions.removeActor()
        ));
    }
}
