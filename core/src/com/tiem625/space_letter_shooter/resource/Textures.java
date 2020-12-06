package com.tiem625.space_letter_shooter.resource;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.tiem625.space_letter_shooter.resource.make.TextureAtlasMaker;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.util.Optional;

public class Textures {

    private Textures() {
        throw new ClassIsStaticException(getClass());
    }

    private static final TextureAtlas atlas = TextureAtlasMaker.buildForInternalPackFile("atlas/pack.atlas");

    public static TextureAtlas getAtlas() {
        return atlas;
    }

    public static Sprite buildAndGetAtlasRegionSprite(String atlasRegionKey) {
        return Optional
                .ofNullable(atlas.createSprite(atlasRegionKey))
                .orElseThrow(() -> new RuntimeException(String.format("No sprite to build at region key '%s'!", atlasRegionKey)));
    }
}
