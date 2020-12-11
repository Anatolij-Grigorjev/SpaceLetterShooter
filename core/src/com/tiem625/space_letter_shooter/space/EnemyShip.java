package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.resource.Textures;
import com.tiem625.space_letter_shooter.space.dto.ShipRenderSpec;
import com.tiem625.space_letter_shooter.util.Point;

public class EnemyShip extends Actor {

    private final String text;
    private String spelledCharacters;
    private final Sprite shipSprite;
    private final ShipRenderSpec shipRenderSpec;

    public EnemyShip(String text, ShipRenderSpec shipRenderSpec) {
        super();
        this.text = text;
        this.spelledCharacters = "";
        this.shipRenderSpec = shipRenderSpec;
        this.shipSprite = Textures.buildAndGetAtlasRegionSprite(shipRenderSpec.spriteKey);
    }

    public Point getShipSize() {
        return new Point(shipSprite.getWidth(), shipSprite.getHeight());
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
        //draw sprite with actor origin at sprite center
        batch.draw(shipSprite, getX(), getY());
        var font = Fonts.ENEMY_TEXT_NORMAL_FONT;
        Fonts.useFontWithColor(font, Color.YELLOW, colorFont -> {
            var drawString = text.substring(spelledCharacters.length());
            colorFont.draw(
                    batch,
                    //text
                    drawString,
                    //position
                    getX() + shipRenderSpec.textShipOffsetX, getY() + shipRenderSpec.textShipOffsetY,
                    //substring indexes
                    0, drawString.length(),
                    //width
                    shipRenderSpec.textTargetWidth,
                    //align, wrap, truncate
                    Align.center, true, "..."
            );
        });
    }

    @Override
    public String toString() {
        return "EnemyShip{" +
                "text='" + text + '\'' +
                ", shipSprite=" + shipSprite +
                '}';
    }
}
