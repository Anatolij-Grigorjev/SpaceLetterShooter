package com.tiem625.space_letter_shooter.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.stream.IntStream;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.random;

public class CommonActionsBuilders {

    private CommonActionsBuilders() {
        throw new ClassIsStaticException(getClass());
    }

    public static Action buildShakeActionSequence(int numShakes, Vector2 amplitude, float duration) {

        float oneShakeDuration = duration / numShakes;
        float halfShake = oneShakeDuration / 2;

        return IntStream.range(0, numShakes)
                .mapToObj(idx -> MathUtils.nextRandomVector2(amplitude))
                .reduce(Actions.sequence(), (SequenceAction seq, Vector2 shakeCoords) -> {

                    seq.addAction(Actions.moveBy(shakeCoords.x, shakeCoords.y, halfShake, Interpolation.fastSlow));
                    var moveBackToCenter = MathUtils.inverse(shakeCoords);
                    seq.addAction(Actions.moveBy(moveBackToCenter.x, moveBackToCenter.y, halfShake, Interpolation.slowFast));
                    return seq;

                }, Actions::sequence);
    }

    public static Action buildPulseActionSequence(int numPulses, float magnitude, float duration) {

        float onePulseDuration = duration / numPulses;
        float halfPulseDuration = onePulseDuration / 2;

        return IntStream.range(0, numPulses)
                .mapToObj(idx -> random(0.0f, magnitude))
                .reduce(Actions.sequence(), (SequenceAction seq, Float pulseMagnitude) -> {
                    var pulsedScale = clamp(1.0f - pulseMagnitude, 0f, 1.0f);
                    seq.addAction(Actions.scaleTo(pulsedScale, pulsedScale, halfPulseDuration, Interpolation.fastSlow));
                    seq.addAction(Actions.scaleTo(1.0f, 1.0f, halfPulseDuration, Interpolation.slowFast));
                    return seq;
                }, Actions::sequence);
    }

    public static Action buildColorFlashAction(Color resetColor, Color flashColor, float duration) {
        return Actions.sequence(
                Actions.color(flashColor, duration / 2, Interpolation.fastSlow),
                Actions.color(resetColor, duration / 2, Interpolation.slowFast)
        );
    }

    public static Action buildShoveAction(float offsetX, float offsetY, float duration) {
        return Actions.sequence(
                Actions.moveBy(offsetX, offsetY, duration / 2, Interpolation.fastSlow),
                Actions.moveBy(-offsetX, -offsetY, duration / 2, Interpolation.slowFast)
        );
    }
}
