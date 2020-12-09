package com.tiem625.space_letter_shooter.space.dto;

public class ShipRenderSpec {

    public final String spriteKey;
    public final float textShipOffsetX;
    public final float textShipOffsetY;
    public final float textTargetWidth;

    public ShipRenderSpec(String spriteKey, float textShipOffsetX, float textShipOffsetY, float textTargetWidth) {
        this.spriteKey = spriteKey;
        this.textShipOffsetX = textShipOffsetX;
        this.textShipOffsetY = textShipOffsetY;
        this.textTargetWidth = textTargetWidth;
    }
}
