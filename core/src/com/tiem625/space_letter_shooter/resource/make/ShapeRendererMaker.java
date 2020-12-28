package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShapeRendererMaker extends ResourceMaker {

    public static ShapeRenderer buildDefault() {
        return makeResource(ShapeRenderer::new);
    }
}
