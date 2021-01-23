package com.tiem625.space_letter_shooter.resource;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

public class Fonts {

    private Fonts() {
        throw new ClassIsStaticException(getClass());
    }

    public static BitmapFont MAIN_UI_FONT;
    public static BitmapFont ENEMY_TEXT_NORMAL_FONT;
}
