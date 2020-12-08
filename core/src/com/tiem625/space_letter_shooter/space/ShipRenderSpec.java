package com.tiem625.space_letter_shooter.space;

public class ShipRenderSpec {

    final String spriteKey;
    final float textShipOffsetX;
    final float textShipOffsetY;
    final float textTargetWidth;

    public ShipRenderSpec(String spriteKey, float textShipOffsetX, float textShipOffsetY, float textTargetWidth) {
        this.spriteKey = spriteKey;
        this.textShipOffsetX = textShipOffsetX;
        this.textShipOffsetY = textShipOffsetY;
        this.textTargetWidth = textTargetWidth;
    }
}
