package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiem625.space_letter_shooter.space.dto.ShipRenderSpec;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShipRenderSpecs {

    private ShipRenderSpecs() {
        throw new ClassIsStaticException(getClass());
    }

    private static final Map<String, ShipRenderSpec> loadedRenderSpecs;

    static {
        loadedRenderSpecs = loadShipSpecs().stream()
                .collect(Collectors.toMap(spec -> spec.specId, Function.identity()));
    }

    public static ShipRenderSpec getRenderSpec(String specId) {
        return Optional.ofNullable(loadedRenderSpecs.get(specId))
                .orElseThrow(() -> new RuntimeException("No ship spec found for id '" + specId + "'"));
    }

    private static Set<ShipRenderSpec> loadShipSpecs() {

        try {
            return new ObjectMapper().readValue(Gdx.files.internal("enemy_ships.json").read(), new TypeReference<>() {});
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptySet();
        }
    }
}
