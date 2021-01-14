package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiem625.space_letter_shooter.resource.ResourceLoader;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SceneConfigureSpecs implements ResourceLoader {

    api;

    private final Map<String, SceneConfigureSpec> loadedSceneSpecs;

    SceneConfigureSpecs() {
        this.loadedSceneSpecs = new ConcurrentHashMap<>();
    }

    @Override
    public void loadResources() {
        Set<SceneConfigureSpec> loadedSpecs = loadSceneSpecs();
        loadedSceneSpecs.putAll(loadedSpecs.stream()
                .collect(Collectors.toMap(SceneConfigureSpec::getSceneId, Function.identity()))
        );
    }

    public SceneConfigureSpec getSceneConfigureSpec(String id) {
        return Optional.ofNullable(loadedSceneSpecs.get(id))
                .orElseThrow(() -> new RuntimeException("No scene spec found for id '" + id + "'"));
    }

    public SceneConfigureSpec getNextSceneSpecAfter(String sceneId) {
        return getSceneConfigureSpec(getSceneConfigureSpec(sceneId).getNextSceneId());
    }

    private Set<SceneConfigureSpec> loadSceneSpecs() {
        var sceneFiles = Set.of(
                "space_scene_1.json"
        );
        return sceneFiles.stream().map(sceneFile -> {
            try {
                return new ObjectMapper().readValue(Gdx.files.internal(sceneFile).read(), new TypeReference<SceneConfigureSpec>() {
                });
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
