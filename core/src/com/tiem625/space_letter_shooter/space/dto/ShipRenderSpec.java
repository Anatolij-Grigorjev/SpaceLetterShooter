package com.tiem625.space_letter_shooter.space.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipRenderSpec {

    public final String spriteKey;
    public final float textShipOffsetX;
    public final float textShipOffsetY;
    public final float textTargetWidth;

    @JsonCreator
    public ShipRenderSpec(
            @JsonProperty("spriteKey") String spriteKey,
            @JsonProperty("textShipOffsetX") float textShipOffsetX,
            @JsonProperty("textShipOffsetY") float textShipOffsetY,
            @JsonProperty("textTargetWidth") float textTargetWidth
    ) {
        this.spriteKey = spriteKey;
        this.textShipOffsetX = textShipOffsetX;
        this.textShipOffsetY = textShipOffsetY;
        this.textTargetWidth = textTargetWidth;
    }
}
