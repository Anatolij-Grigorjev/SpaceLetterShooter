package com.tiem625.space_letter_shooter.space.spec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiem625.space_letter_shooter.space.vessel.VesselRenderSpec;
import com.tiem625.space_letter_shooter.text.TextRenderSpec;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EnemyShipRenderSpec {

    private final String specId;
    private final VesselRenderSpec vesselRenderSpec;
    private final TextRenderSpec textRenderSpec;

    @JsonCreator
    public EnemyShipRenderSpec(
            @JsonProperty("specId") String specId,
            @JsonProperty("vesselRenderSpec") VesselRenderSpec vesselRenderSpec,
            @JsonProperty("textRenderSpec") TextRenderSpec textRenderSpec
    ) {
        this.specId = specId;
        this.vesselRenderSpec = vesselRenderSpec;
        this.textRenderSpec = textRenderSpec;
    }

    public String getSpecId() {
        return specId;
    }

    public VesselRenderSpec getVesselRenderSpec() {
        return vesselRenderSpec;
    }

    public TextRenderSpec getTextRenderSpec() {
        return textRenderSpec;
    }
}
