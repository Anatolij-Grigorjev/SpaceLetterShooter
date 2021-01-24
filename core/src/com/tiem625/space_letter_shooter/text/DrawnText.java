package com.tiem625.space_letter_shooter.text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import static java.util.Optional.ofNullable;

public class DrawnText extends Actor {

    protected String text;
    protected BitmapFont font;
    protected GlyphLayout layout;

    protected int targetWidth;

    protected int drawSubstringStartIdx;
    protected int drawSubstringEndIdx;

    public DrawnText(String text, BitmapFont font) {
        this.font = font;
        this.text = ofNullable(text).orElse("");
        this.layout = new GlyphLayout(font, text);

        targetWidth = (int) layout.width;
        drawSubstringStartIdx = 0;
        drawSubstringEndIdx = this.text.length();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        font.draw(
                batch,
                //text
                text,
                //position
                getX(), getY(),
                //substring indexes
                drawSubstringStartIdx, drawSubstringEndIdx,
                //width
                targetWidth,
                //align, wrap, truncate
                Align.center, true, "..."
        );
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public BitmapFont getFont() {
        return font;
    }

    public GlyphLayout getLayout() {
        return layout;
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    @Override
    public String toString() {
        return "DrawnText{" +
                "text='" + text + '\'' +
                ", font=" + font +
                ", X=" + getX() +
                ", Y=" + getY() +
                ", targetWidth=" + targetWidth +
                '}';
    }
}
