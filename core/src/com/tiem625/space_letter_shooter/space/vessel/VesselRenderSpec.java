package com.tiem625.space_letter_shooter.space.vessel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VesselRenderSpec {

    private final String spriteKey;
    private final float width;
    private final float height;

    public VesselRenderSpec(
            @JsonProperty("spriteKey") String spriteKey,
            @JsonProperty("width") float width,
            @JsonProperty("height") float height
    ) {
        this.spriteKey = spriteKey;
        this.width = width;
        this.height = height;
    }

    public String getSpriteKey() {
        return spriteKey;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
