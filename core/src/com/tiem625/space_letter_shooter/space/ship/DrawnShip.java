package com.tiem625.space_letter_shooter.space.ship;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.resource.Textures;
import com.tiem625.space_letter_shooter.space.spec.ShipRenderSpec;

public class DrawnShip extends Actor {

    private final TextureRegion texture;

    public DrawnShip(ShipRenderSpec shipRenderSpec) {
        this.texture = Textures.buildAndGetAtlasRegionSprite(shipRenderSpec.getSpriteKey());
        this.setWidth(shipRenderSpec.getWidth());
        this.setHeight(shipRenderSpec.getHeight());
        this.setOrigin(Align.center);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
                texture,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );
    }

    @Override
    public String toString() {
        return "DrawnShip{" +
                "texture=" + texture +
                ", width=" + getWidth() +
                ", height=" + getHeight() +
                '}';
    }
}
