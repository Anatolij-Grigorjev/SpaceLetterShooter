package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiem625.space_letter_shooter.resource.ResourceLoader;
import com.tiem625.space_letter_shooter.space.spec.EnemyShipRenderSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ShipRenderSpecs implements ResourceLoader {

    api;

    private final Map<String, EnemyShipRenderSpec> loadedRenderSpecs;

    ShipRenderSpecs() {
        loadedRenderSpecs = new ConcurrentHashMap<>();
    }

    @Override
    public void loadResources() {
        var shipSpecs = loadShipSpecs();
        loadedRenderSpecs.putAll(shipSpecs.stream()
                .collect(Collectors.toMap(EnemyShipRenderSpec::getSpecId, Function.identity())));
    }

    public EnemyShipRenderSpec getRenderSpec(String specId) {
        return Optional.ofNullable(loadedRenderSpecs.get(specId))
                .orElseThrow(() -> new RuntimeException("No ship spec found for id '" + specId + "'"));
    }

    private Set<EnemyShipRenderSpec> loadShipSpecs() {

        try {
            return new ObjectMapper().readValue(Gdx.files.internal("enemy_ships.json").read(), new TypeReference<>() {});
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptySet();
        }
    }
}
