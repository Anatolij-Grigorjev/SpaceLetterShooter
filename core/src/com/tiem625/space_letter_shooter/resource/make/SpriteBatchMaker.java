package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tiem625.space_letter_shooter.resource.ResourceMaker;

public class SpriteBatchMaker extends ResourceMaker {

    public static SpriteBatch buildDefault() {
        return makeResource(SpriteBatch::new);
    }
}
