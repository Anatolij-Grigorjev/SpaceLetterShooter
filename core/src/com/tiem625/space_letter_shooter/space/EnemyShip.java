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

    public boolean canHitCharacter(char input) {
        if (shipTextIsSpelled()) {
            return false;
        }
        var nextShipTextChar = text.substring(spelledCharacters.length()).charAt(0);
        return nextShipTextChar == input;
    }

    public void hitCharacter(char input) {
        spelledCharacters += input;
        disposeIfSpelled();
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

        var font = GameFonts.ENEMY_TEXT_NORMAL_FONT;

        font.draw(batch, text, getX(), getY());
        GameFonts.useFontWithColor(font, Color.RED, coloredFont -> {
            coloredFont.draw(batch, spelledCharacters, getX(), getY());
        });
    }
}
