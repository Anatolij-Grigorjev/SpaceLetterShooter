package com.tiem625.space_letter_shooter.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.stream.IntStream;

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

                    seq.addAction(Actions.moveBy(shakeCoords.x, shakeCoords.y, halfShake));
                    var moveBackToCenter = MathUtils.inverse(shakeCoords);
                    seq.addAction(Actions.moveBy(moveBackToCenter.x, moveBackToCenter.y, halfShake));
                    return seq;

                }, Actions::sequence);
    }
}
