package com.tiem625.space_letter_shooter.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tiem625.space_letter_shooter.resource.GameFonts;

public class EnemyShip extends Actor {

    private final String text;

    public EnemyShip(String text) {
        super();
        this.text = text;
        this.setPosition(300, 250);
        this.setZIndex(999);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        GameFonts.ENEMY_TEXT_NORMAL_FONT.draw(batch, text, getX(), getY());
    }
}
