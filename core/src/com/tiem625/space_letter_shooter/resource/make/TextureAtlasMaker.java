package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Objects;

public class TextureAtlasMaker extends ResourceMaker {

    public static TextureAtlas buildForInternalPackFile(String internalPackFilePath) {
        Objects.requireNonNull(internalPackFilePath);
        return makeResource(() -> new TextureAtlas(internalPackFilePath));
    }
}
