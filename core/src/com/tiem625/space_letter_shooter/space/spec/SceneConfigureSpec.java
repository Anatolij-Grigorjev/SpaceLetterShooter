package com.tiem625.space_letter_shooter.space.spec;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SceneConfigureSpec {

    private final String sceneId;
    private final String sceneName;
    private final List<ShipPlacement> shipPlacements;
    private final List<ShipDescentSpec> shipDescentSpecs;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SceneConfigureSpec(
            @JsonProperty("sceneId") String sceneId,
            @JsonProperty("sceneName") String sceneName,
            @JsonProperty("shipPlacements") List<ShipPlacement> shipPlacements,
            @JsonProperty("shipDescentSpec") List<ShipDescentSpec> shipDescentSpecs
    ) {
        this.sceneId = sceneId;
        this.sceneName = sceneName;
        this.shipPlacements = shipPlacements;
        this.shipDescentSpecs = shipDescentSpecs;
    }

    public String getSceneId() {
        return sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public List<ShipPlacement> getShipPlacements() {
        return shipPlacements;
    }

    public List<ShipDescentSpec> getShipDescentSpecs() {
        return shipDescentSpecs;
    }

    public static class ShipPlacement {

        private final String shipId;
        private final ShipPosition position;
        private final String shipSpecId;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public ShipPlacement(
                @JsonProperty("shipId") String shipId,
                @JsonProperty("position") ShipPosition position,
                @JsonProperty("shipSpecId") String shipSpecId
        ) {
            this.shipId = shipId;
            this.position = position;
            this.shipSpecId = shipSpecId;
        }

        public String getShipId() {
            return shipId;
        }

        public ShipPosition getPosition() {
            return position;
        }

        public String getShipSpecId() {
            return shipSpecId;
        }

        public static class ShipPosition {
            private final float x;
            private final float y;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public ShipPosition(
                    @JsonProperty("x") float x,
                    @JsonProperty("y") float y
            ) {
                this.x = x;
                this.y = y;
            }

            public float getX() {
                return x;
            }

            public float getY() {
                return y;
            }

            public Vector2 toVector2() {
                return new Vector2(x, y);
            }

        }
    }

    public static class ShipDescentSpec {

        private final String shipId;
        private final float stepMin;
        private final float stepMax;
        private final float speedMin;
        private final float speedMax;
        private final List<Integer> descentStepsX;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public ShipDescentSpec(
                @JsonProperty("shipId") String shipId,
                @JsonProperty("stepMin") float stepMin,
                @JsonProperty("stepMax") float stepMax,
                @JsonProperty("speedMin") float speedMin,
                @JsonProperty("speedMax") float speedMax,
                @JsonProperty("descentStepsX") List<Integer> descentStepsX
        ) {
            this.shipId = shipId;
            this.stepMin = stepMin;
            this.stepMax = stepMax;
            this.speedMin = speedMin;
            this.speedMax = speedMax;
            this.descentStepsX = descentStepsX;
        }

        public String getShipId() {
            return shipId;
        }

        public float getStepMin() {
            return stepMin;
        }

        public float getStepMax() {
            return stepMax;
        }

        public float getSpeedMin() {
            return speedMin;
        }

        public float getSpeedMax() {
            return speedMax;
        }

        public List<Integer> getDescentStepsX() {
            return descentStepsX;
        }
    }
}
