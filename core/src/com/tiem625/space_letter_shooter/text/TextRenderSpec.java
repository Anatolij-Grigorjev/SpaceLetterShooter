package com.tiem625.space_letter_shooter.text;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TextRenderSpec {

    private final float offsetX;
    private final float offsetY;
    private final int targetWidth;

    @JsonCreator
    public TextRenderSpec(
            @JsonProperty("offsetX") float offsetX,
            @JsonProperty("offsetY") float offsetY,
            @JsonProperty("targetWidth") int targetWidth
    ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.targetWidth = targetWidth;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public int getTargetWidth() {
        return targetWidth;
    }
}
