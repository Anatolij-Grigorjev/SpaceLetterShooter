package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.util.function.Consumer;

public class GameFonts {

    private GameFonts() {
        throw new ClassIsStaticException(getClass());
    }

    public static BitmapFont MAIN_UI_FONT;
    public static BitmapFont ENEMY_TEXT_NORMAL_FONT;

    /**
     * Temporarily set the {@link Color} of the {@link BitmapFont} <code>font</code> to be <code>color</code> and
     * perform operations with it described by <code>actions</code>. <br/>
     * Once <code>actions</code> are done, the font's <code>color</code> is reset back to its pre-invocation value.
     * @param font the font to operate on
     * @param color the color to set on the font for duration of actions
     * @param actions the actions to perform while <code>font</code> has color set to <code>color</code>
     */
    public static void useFontWithColor(BitmapFont font, Color color, Consumer<BitmapFont> actions) {
        var prevFontColor = font.getColor().cpy();
        font.setColor(color);
        actions.accept(font);
        font.setColor(prevFontColor);
    }
}
