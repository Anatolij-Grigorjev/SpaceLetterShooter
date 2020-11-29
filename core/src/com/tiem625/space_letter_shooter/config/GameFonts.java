package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

public class GameFonts {

    private GameFonts() {
        throw new ClassIsStaticException(getClass());
    }

    public static BitmapFont MAIN_UI_FONT;
    public static BitmapFont ENEMY_TEXT_NORMAL_FONT;
}
