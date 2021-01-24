package com.tiem625.space_letter_shooter.text;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.resource.Fonts;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class FlyInCenterText extends Group {

    private final DrawnText[] drawnTexts;
    private final float textMoveDuration;
    private final float textTravelExtent;

    public FlyInCenterText(String[] textLines) {
        var font = Fonts.MAIN_UI_FONT;
        var lineVertSpace = font.getCapHeight() + font.getLineHeight();

        this.drawnTexts = ofNullable(textLines).stream()
                .flatMap(Stream::of)
                .map(line -> new DrawnText(line, Fonts.MAIN_UI_FONT))
                .toArray(DrawnText[]::new);
        IntStream.range(0, drawnTexts.length).forEachOrdered(idx -> {
            var drawnText = drawnTexts[idx];
            drawnText.setPosition(0, -idx * lineVertSpace);
            this.addActor(drawnText);
        });

        textMoveDuration = 1.0f;
        textTravelExtent = 150.0f;
        buildAnimatedFlyIn();
    }

    private void buildAnimatedFlyIn() {

        setTextCenterPosition();
        setScale(0.0f);
        addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, -2 * textTravelExtent, textMoveDuration, Interpolation.fastSlow),
                        Actions.scaleTo(0.25f, 0.25f, textMoveDuration, Interpolation.slowFast)
                ),
                Actions.parallel(
                        Actions.moveBy(0, textTravelExtent, textMoveDuration, Interpolation.slowFast),
                        Actions.scaleTo(1, 1, textMoveDuration, Interpolation.fastSlow)
                )
        ));
    }

    private void setTextCenterPosition() {
        var resolution = GamePropsHolder.props.getResolution();
        var screenCenterPoint = new Vector2(resolution.x / 2, resolution.y / 2 + textTravelExtent);

        var totalTextHeight = Stream.of(drawnTexts).reduce(0.0f, (accum, nextLine) -> {
            return nextLine.layout.height + nextLine.font.getLineHeight();
        }, Float::sum);

        setPosition(screenCenterPoint.x, screenCenterPoint.y + totalTextHeight);
    }

}
