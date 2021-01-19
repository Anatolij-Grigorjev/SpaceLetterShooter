package com.tiem625.space_letter_shooter.space.spec;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class SceneScript {

    private final String scriptId;
    private final String nextScriptId;
    private final String sceneName;
    private final List<ShipPlacement> shipPlacements;
    private final List<ShipDescentSpec> shipDescentSpecs;
    @JsonIgnore
    private final Map<String, ShipDescentSpec> shipDescentSpecsMap;

    @JsonCreator(mode = JsonCreator.Mode.DEFAULT)
    public SceneScript(
            @JsonProperty("scriptId") String scriptId,
            @JsonProperty("nextScriptId") String nextScriptId,
            @JsonProperty("sceneName") String sceneName,
            @JsonProperty("shipPlacements") List<ShipPlacement> shipPlacements,
            @JsonProperty("shipDescentSpecs") List<ShipDescentSpec> shipDescentSpecs
    ) {
        this.scriptId = scriptId;
        this.nextScriptId = nextScriptId;
        this.sceneName = sceneName;
        this.shipPlacements = shipPlacements;
        this.shipDescentSpecs = ofNullable(shipDescentSpecs).orElse(List.of());
        this.shipDescentSpecsMap = this.shipDescentSpecs.stream()
                .collect(Collectors.toMap(ShipDescentSpec::getShipId, Function.identity()));
    }

    public String getScriptId() {
        return scriptId;
    }

    public String getNextScriptId() {
        return nextScriptId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public List<ShipPlacement> getShipPlacements() {
        return shipPlacements;
    }

    @JsonIgnore
    public Map<String, ShipDescentSpec> getShipDescentSpecsMap() {
        return shipDescentSpecsMap;
    }

    //required by jackson
    public List<ShipDescentSpec> getShipDescentSpecs() {
        return shipDescentSpecs;
    }

    public static class ShipPlacement {

        private final String shipId;
        private final String text;
        private final ShipPosition position;
        private final String shipSpecId;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public ShipPlacement(
                @JsonProperty("shipId") String shipId,
                @JsonProperty("text") String text,
                @JsonProperty("position") ShipPosition position,
                @JsonProperty("shipSpecId") String shipSpecId
        ) {
            this.shipId = shipId;
            this.text = text;
            this.position = position;
            this.shipSpecId = shipSpecId;
        }

        public String getShipId() {
            return shipId;
        }

        public String getText() {
            return text;
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
