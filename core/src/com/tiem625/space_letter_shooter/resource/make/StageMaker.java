package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class StageMaker extends ResourceMaker {

    /**
     * Build a new {@link Stage}, using a stretching {@link com.badlogic.gdx.utils.viewport.Viewport} and
     * an internal {@link com.badlogic.gdx.graphics.g2d.SpriteBatch}
     * @return the created {@link Stage} instance, managed by the general {@link com.tiem625.space_letter_shooter.resource.ResourcesManager}
     */
    public static Stage buildDefault() {
        return makeResource(Stage::new);
    }
}
