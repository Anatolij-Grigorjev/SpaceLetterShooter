package com.tiem625.space_letter_shooter.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.resource.Fonts;

public class SpellableText extends Actor {

    private String text;
    private final BitmapFont font;

    private final float drawOffsetX;
    private final float drawOffsetY;
    private final int targetWidth;

    private int numSpelledCharacters;

    public SpellableText(String text, TextRenderSpec textRenderSpec) {
        this.text = text;
        this.font = Fonts.ENEMY_TEXT_NORMAL_FONT;
        this.drawOffsetX = textRenderSpec.getOffsetX();
        this.drawOffsetY = textRenderSpec.getOffsetY();
        this.targetWidth = textRenderSpec.getTargetWidth();

        this.numSpelledCharacters = 0;
    }

    public void setText(String newText) {
        text = newText;
        numSpelledCharacters = 0;
    }

    public boolean canSpellCharacter(char input) {
        if (isSpelled()) {
            return false;
        }
        var nextShipTextChar = text.substring(numSpelledCharacters).charAt(0);
        return Character.toUpperCase(nextShipTextChar) == Character.toUpperCase(input);
    }

    public void spellCharacter() {
        numSpelledCharacters++;
    }

    public boolean isSpelled() {
        return text.length() <= numSpelledCharacters;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isSpelled()) {
            return;
        }

        Fonts.useFontWithColor(font, Color.YELLOW, colorFont -> {
            var drawString = text.substring(numSpelledCharacters);
            colorFont.draw(
                    batch,
                    //text
                    drawString,
                    //position
                    drawOffsetX, drawOffsetY,
                    //substring indexes
                    0, drawString.length(),
                    //width
                    targetWidth,
                    //align, wrap, truncate
                    Align.center, true, "..."
            );
        });
    }

    @Override
    public String toString() {
        return "SpellableText{" +
                "text='" + text + '\'' +
                ", drawOffsetX=" + drawOffsetX +
                ", drawOffsetY=" + drawOffsetY +
                ", targetWidth=" + targetWidth +
                ", numSpelledCharacters=" + numSpelledCharacters +
                '}';
    }
}
