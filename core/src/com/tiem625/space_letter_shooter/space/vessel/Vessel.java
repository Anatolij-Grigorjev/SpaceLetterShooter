package com.tiem625.space_letter_shooter.space.vessel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.resource.Textures;

public class Vessel extends Actor {

    private final Sprite texture;

    public Vessel(VesselRenderSpec vesselRenderSpec) {
        this.texture = Textures.buildAndGetAtlasRegionSprite(vesselRenderSpec.getSpriteKey());
        var scaleX = vesselRenderSpec.getWidth() / texture.getWidth();
        var scaleY = vesselRenderSpec.getHeight() / texture.getHeight();
        this.texture.setScale(scaleX, scaleY);

        this.setOrigin(Align.center);
        this.texture.setOrigin(this.getOriginX(), this.getOriginY());
        this.setWidth(vesselRenderSpec.getWidth());
        this.setHeight(vesselRenderSpec.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        texture.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.texture.setColor(getColor());
    }

    @Override
    public String toString() {
        return "Vessel{" +
                "texture=" + texture +
                ", width=" + getWidth() +
                ", height=" + getHeight() +
                '}';
    }
}
