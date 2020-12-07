package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.resource.Textures;
import com.tiem625.space_letter_shooter.util.Point;

public class EnemyShip extends Actor {

    private final String text;
    private final Point textSpriteCenterOffset;
    private final Sprite shipSprite;
    private String spelledCharacters;

    public EnemyShip(String text, String spriteKey, Point textSpriteCenterOffset) {
        super();
        this.text = text;
        this.spelledCharacters = "";
        this.shipSprite = Textures.buildAndGetAtlasRegionSprite(spriteKey);
        this.textSpriteCenterOffset = textSpriteCenterOffset;
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
        var spriteCenter = new Point(getX() - shipSprite.getWidth() / 2, getY() - shipSprite.getHeight() / 2);
        //draw sprite with actor origin at sprite center
        batch.draw(shipSprite, spriteCenter.x, spriteCenter.y);
        var font = Fonts.ENEMY_TEXT_NORMAL_FONT;
        Fonts.useFontWithColor(font, Color.YELLOW, colorFont -> {
            var drawString = text.substring(spelledCharacters.length());
            colorFont.draw(
                    batch,
                    //text
                    drawString,
                    //position
                    spriteCenter.x + textSpriteCenterOffset.x, spriteCenter.y + textSpriteCenterOffset.y,
                    //substring indexes
                    0, drawString.length(),
                    //width
                    50.0f,
                    //align, wrap, truncate
                    Align.center, false, "..."
            );
        });
    }
}
