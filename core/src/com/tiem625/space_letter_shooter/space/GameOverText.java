package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tiem625.space_letter_shooter.resource.Fonts;

public class GameOverText extends Actor {

    private final String text = "GAME OVER";
    private final BitmapFont font;

    public GameOverText() {
        this.font = Fonts.MAIN_UI_FONT;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(
                batch,
                //text
                text,
                //position
                getX(), getY()
        );
    }
}
