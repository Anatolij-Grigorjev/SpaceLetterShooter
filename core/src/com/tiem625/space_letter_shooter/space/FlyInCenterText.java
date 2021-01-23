package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.resource.Fonts;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class FlyInCenterText extends Actor {

    private final String[] text;
    private final float textMoveDuration;
    private final float textTravelExtent;

    private final BitmapFont font;
    private final float lineVertSpace;

    public FlyInCenterText(String[] textLines) {
        this.text = ofNullable(textLines).orElse(new String[0]);
        textMoveDuration = 1.0f;
        textTravelExtent = 150.0f;
        this.font = Fonts.MAIN_UI_FONT;
        this.lineVertSpace = this.font.getCapHeight() + this.font.getLineHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        IntStream.range(0, text.length).forEachOrdered(idx -> {
            font.draw(
                    batch,
                    //text
                    text[idx],
                    //position
                    getX(), getY() - (idx * lineVertSpace)
            );
        });
    }

    public void setAnimateOnStage(Stage stage) {
        var root = stage.getRoot();
        setTextCenterPosition(root);
        root.setScale(0.0f);
        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, -2 * textTravelExtent, textMoveDuration, Interpolation.fastSlow),
                        Actions.scaleTo(0.25f, 0.25f, textMoveDuration, Interpolation.slowFast)
                ),
                Actions.parallel(
                        Actions.moveBy(0, textTravelExtent, textMoveDuration, Interpolation.slowFast),
                        Actions.scaleTo(1, 1, textMoveDuration, Interpolation.fastSlow)
                )
        ));
        stage.addActor(this);
    }

    private void setTextCenterPosition(Actor actor) {
        var resolution = GamePropsHolder.props.getResolution();
        var screenCenterPoint = new Vector2(resolution.x / 2, resolution.y / 2 + textTravelExtent);

        var layoutSizeCalc = new GlyphLayout();
        var totalTextHeight = Stream.of(text).reduce(0.0f, (accum, nextLine) -> {
            layoutSizeCalc.setText(font, nextLine);
            return layoutSizeCalc.height + font.getLineHeight();
        }, Float::sum) - font.getLineHeight();

        actor.setPosition(screenCenterPoint.x, screenCenterPoint.y + totalTextHeight);
    }

}
