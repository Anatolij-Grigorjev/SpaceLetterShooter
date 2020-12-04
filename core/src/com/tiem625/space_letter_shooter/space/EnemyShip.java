package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tiem625.space_letter_shooter.config.GameFonts;

public class EnemyShip extends Actor {

    private final String text;
    private String spelledCharacters;

    public EnemyShip(String text) {
        super();
        this.text = text;
        this.spelledCharacters = "";
    }

    public boolean tryHitCharacter(char input) {
        if (shipTextIsSpelled()) {
            return false;
        }
        var nextShipTextChar = text.substring(spelledCharacters.length()).charAt(0);
        if (input == nextShipTextChar) {
            spelledCharacters = spelledCharacters + input;
            disposeIfSpelled();
            return true;
        } else {
            return false;
        }
    }

    private boolean shipTextIsSpelled() {
        return spelledCharacters.length() >= text.length();
    }

    private void disposeIfSpelled() {
        if (shipTextIsSpelled()) {
            //TODO: ship dispose logic here
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        GameFonts.ENEMY_TEXT_NORMAL_FONT.draw(batch, text, getX(), getY());
        GameFonts.ENEMY_TEXT_NORMAL_FONT.setColor(Color.RED);
        GameFonts.ENEMY_TEXT_NORMAL_FONT.draw(batch, spelledCharacters, getX(), getY());
    }
}
