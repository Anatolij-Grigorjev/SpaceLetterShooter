package com.tiem625.space_letter_shooter.space.dto;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SceneConfigureSpec {

    public final String sceneId;
    public final String sceneName;
    public final List<ShipPlacement> shipPlacements;

    public SceneConfigureSpec(
            @JsonProperty("sceneId") String sceneId,
            @JsonProperty("sceneName") String sceneName,
            @JsonProperty("shipPlacements") List<ShipPlacement> shipPlacements
    ) {
        this.sceneId = sceneId;
        this.sceneName = sceneName;
        this.shipPlacements = shipPlacements;
    }

    public static class ShipPlacement {

        public final ShipPosition position;
        public final String shipSpecId;

        public ShipPlacement(
                @JsonProperty("position") ShipPosition position,
                @JsonProperty("shipSpecId") String shipSpecId
        ) {
            this.position = position;
            this.shipSpecId = shipSpecId;
        }

        public static class ShipPosition {
            public final float x;
            public final float y;

            public ShipPosition(
                    @JsonProperty("x") float x,
                    @JsonProperty("y") float y
            ) {
                this.x = x;
                this.y = y;
            }

            public Vector2 toVector2() {
                return new Vector2(x, y);
            }

        }
    }
}
