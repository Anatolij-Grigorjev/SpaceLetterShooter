package com.tiem625.space_letter_shooter.space.spec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ShipRenderSpec {

    public final String specId;
    public final String spriteKey;
    public final float textShipOffsetX;
    public final float textShipOffsetY;
    public final float textTargetWidth;

    @JsonCreator
    public ShipRenderSpec(
            @JsonProperty("specId") String specId,
            @JsonProperty("spriteKey") String spriteKey,
            @JsonProperty("textShipOffsetX") float textShipOffsetX,
            @JsonProperty("textShipOffsetY") float textShipOffsetY,
            @JsonProperty("textTargetWidth") float textTargetWidth
    ) {
        this.specId = specId;
        this.spriteKey = spriteKey;
        this.textShipOffsetX = textShipOffsetX;
        this.textShipOffsetY = textShipOffsetY;
        this.textTargetWidth = textTargetWidth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipRenderSpec that = (ShipRenderSpec) o;
        return Float.compare(that.textShipOffsetX, textShipOffsetX) == 0 &&
                Float.compare(that.textShipOffsetY, textShipOffsetY) == 0 &&
                Float.compare(that.textTargetWidth, textTargetWidth) == 0 &&
                Objects.equals(specId, that.specId) &&
                Objects.equals(spriteKey, that.spriteKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specId, spriteKey, textShipOffsetX, textShipOffsetY, textTargetWidth);
    }
}
