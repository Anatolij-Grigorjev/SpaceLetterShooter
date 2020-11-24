package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BitmapFontMaker extends ResourceMaker {

    public static BitmapFont buildDefault() {
        return makeResource(BitmapFont::new);
    }
}
