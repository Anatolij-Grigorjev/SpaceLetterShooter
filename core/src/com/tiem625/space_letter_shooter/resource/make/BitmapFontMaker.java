package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tiem625.space_letter_shooter.resource.ResourceMaker;

public class BitmapFontMaker extends ResourceMaker {

    public static BitmapFont buildMain() {
        var newFont = makeResource(BitmapFont::new);
        return newFont;
    }

    public static BitmapFont buildEnemyShipNormalFont() {
        var normalEnemyFont = makeResource(BitmapFont::new);
        normalEnemyFont.setColor(Color.WHITE);
        return normalEnemyFont;
    }
}
