package com.tiem625.space_letter_shooter.space.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiem625.space_letter_shooter.util.Point;

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

        public final Point position;
        public final String shipSpecId;

        public ShipPlacement(
                @JsonProperty("position") Point position,
                @JsonProperty("shipSpecId") String shipSpecId
        ) {
            this.position = position;
            this.shipSpecId = shipSpecId;
        }
    }
}
