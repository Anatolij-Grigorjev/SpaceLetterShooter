package com.tiem625.space_letter_shooter.space.vessel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.resource.Textures;

public class Vessel extends Actor {

    private final TextureRegion texture;

    public Vessel(VesselRenderSpec vesselRenderSpec) {
        this.texture = Textures.buildAndGetAtlasRegionSprite(vesselRenderSpec.getSpriteKey());
        this.setWidth(vesselRenderSpec.getWidth());
        this.setHeight(vesselRenderSpec.getHeight());
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
