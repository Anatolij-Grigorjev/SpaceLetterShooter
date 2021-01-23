package com.tiem625.space_letter_shooter.text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tiem625.space_letter_shooter.resource.Fonts;

public class SpellableText extends DrawnText {

    private int numSpelledCharacters;

    public SpellableText(String text, TextRenderSpec textRenderSpec) {
        super(text, Fonts.ENEMY_TEXT_NORMAL_FONT);
        this.drawOffsetX = textRenderSpec.getOffsetX();
        this.drawOffsetY = textRenderSpec.getOffsetY();
        this.targetWidth = textRenderSpec.getTargetWidth();

        this.numSpelledCharacters = 0;
    }

    @Override
    public void setText(String newText) {
        super.setText(newText);
        numSpelledCharacters = 0;
        drawSubstringStartIdx = 0;
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
        drawSubstringStartIdx++;
    }

    public boolean isSpelled() {
        return text.length() <= numSpelledCharacters;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isSpelled()) {
            return;
        }

        super.draw(batch, parentAlpha);
    }
}
