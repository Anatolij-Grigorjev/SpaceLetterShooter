package com.tiem625.space_letter_shooter.space.spec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiem625.space_letter_shooter.text.TextRenderSpec;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipRenderSpec {

    private final String specId;
    private final String spriteKey;
    private final float width;
    private final float height;
    private final TextRenderSpec textRenderSpec;

    @JsonCreator
    public ShipRenderSpec(
            @JsonProperty("specId") String specId,
            @JsonProperty("spriteKey") String spriteKey,
            @JsonProperty("width") float width,
            @JsonProperty("height") float height,
            @JsonProperty("textRenderSpec") TextRenderSpec textRenderSpec
    ) {
        this.specId = specId;
        this.spriteKey = spriteKey;
        this.width = width;
        this.height = height;
        this.textRenderSpec = textRenderSpec;
    }

    public String getSpecId() {
        return specId;
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

    public TextRenderSpec getTextRenderSpec() {
        return textRenderSpec;
    }
}
