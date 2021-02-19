package com.tiem625.space_letter_shooter.resource;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.tiem625.space_letter_shooter.resource.make.TextureAtlasMaker;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Textures {

    private Textures() {
        throw new ClassIsStaticException(getClass());
    }

    private static Map<String, Sprite> spritesCache = new ConcurrentHashMap<>();

    private static final TextureAtlas atlas = TextureAtlasMaker.buildForInternalPackFile("atlas/pack.atlas");

    public static TextureAtlas getAtlas() {
        return atlas;
    }

    public static Sprite buildAndGetAtlasRegionSprite(String atlasRegionKey) {
        var sprite = spritesCache.computeIfAbsent(atlasRegionKey, atlas::createSprite);
        return Optional
                .ofNullable(sprite)
                .orElseThrow(() -> new RuntimeException(String.format("No sprite to build at region key '%s'!", atlasRegionKey)));
    }
}
