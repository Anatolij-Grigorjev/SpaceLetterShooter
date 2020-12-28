package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tiem625.space_letter_shooter.resource.Textures;

/**
 * Rectangular overlay of specific color
 */
public class ColorOverlay extends Actor {

    private final Sprite rectSprite;

    public ColorOverlay(Color color, Rectangle bounds) {
        this.rectSprite = Textures.buildAndGetAtlasRegionSprite("white_rect");
        this.rectSprite.setPosition(bounds.x, bounds.y);
        this.rectSprite.setSize(bounds.width, bounds.height);
        setColor(color);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.rectSprite.setColor(getColor());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        rectSprite.draw(batch, parentAlpha);
    }
}
