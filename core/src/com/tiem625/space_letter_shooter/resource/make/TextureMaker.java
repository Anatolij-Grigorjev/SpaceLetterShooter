package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.graphics.Texture;
import com.tiem625.space_letter_shooter.resource.ResourceMaker;

import java.util.Objects;

public class TextureMaker extends ResourceMaker {

    public static Texture buildForInternalPath(String internalPath) {
        Objects.requireNonNull(internalPath);
        return makeResource(() -> new Texture(internalPath));
    }
}
