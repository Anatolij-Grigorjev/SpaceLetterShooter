package com.tiem625.space_letter_shooter.text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;

import static java.util.Optional.ofNullable;

public class DrawnText extends Actor {

    protected String text;
    protected BitmapFont font;

    protected float drawOffsetX;
    protected float drawOffsetY;
    protected int targetWidth;

    protected int drawSubstringStartIdx;
    protected int drawSubstringEndIdx;

    public DrawnText(String text, BitmapFont font) {
        this.font = font;
        this.text = ofNullable(text).orElse("");
        //defaults
        drawOffsetX = 0;
        drawOffsetY = 0;
        targetWidth = GamePropsHolder.props.getResolutionWidth();

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
                drawOffsetX, drawOffsetY,
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

    public float getDrawOffsetX() {
        return drawOffsetX;
    }

    public float getDrawOffsetY() {
        return drawOffsetY;
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    @Override
    public String toString() {
        return "DrawnText{" +
                "text='" + text + '\'' +
                ", font=" + font +
                ", drawOffsetX=" + drawOffsetX +
                ", drawOffsetY=" + drawOffsetY +
                ", targetWidth=" + targetWidth +
                '}';
    }
}
