package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.resource.Textures;

public class EnemyShip extends Actor {

    private final String text;
    private final Sprite shipSprite;
    private String spelledCharacters;

    public EnemyShip(String text, String spriteKey) {
        super();
        this.text = text;
        this.spelledCharacters = "";
        this.shipSprite = Textures.buildAndGetAtlasRegionSprite(spriteKey);
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

        batch.draw(shipSprite, getX(), getY() - shipSprite.getHeight());
        var font = Fonts.ENEMY_TEXT_NORMAL_FONT;
        Fonts.useFontWithColor(font, Color.YELLOW, colorFont -> {
            colorFont.draw(batch, text.substring(spelledCharacters.length()), getX(), getY());
        });
    }
}
