package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiem625.space_letter_shooter.resource.ResourceLoader;
import com.tiem625.space_letter_shooter.space.spec.SceneScript;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SceneScripts implements ResourceLoader {

    api;

    private final Map<String, SceneScript> loadedSceneScripts;

    SceneScripts() {
        this.loadedSceneScripts = new ConcurrentHashMap<>();
    }

    @Override
    public void loadResources() {
        Set<SceneScript> loadedSpecs = loadSceneSpecs();
        loadedSceneScripts.putAll(loadedSpecs.stream()
                .collect(Collectors.toMap(SceneScript::getScriptId, Function.identity()))
        );
    }

    public SceneScript getSceneScript(String id) {
        return Optional.ofNullable(loadedSceneScripts.get(id))
                .orElseThrow(() -> new RuntimeException("No scene spec found for id '" + id + "'"));
    }

    public SceneScript getNextSceneSpecAfter(String sceneId) {
        return getSceneScript(getSceneScript(sceneId).getNextScriptId());
    }

    private Set<SceneScript> loadSceneSpecs() {
        var sceneFiles = Set.of(
                "space_scene_1.json",
                "space_scene_2.json"
        );
        return sceneFiles.stream().map(sceneFile -> {
            try {
                return new ObjectMapper().readValue(Gdx.files.internal(sceneFile).read(), new TypeReference<SceneScript>() {
                });
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
