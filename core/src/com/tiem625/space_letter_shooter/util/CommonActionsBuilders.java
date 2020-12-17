package com.tiem625.space_letter_shooter.util;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.stream.IntStream;

public class CommonActionsBuilders {

    private CommonActionsBuilders() {
        throw new ClassIsStaticException(getClass());
    }

    public static Action buildShakeActionSequence(int numShakes, Point amplitude, float duration) {

        float oneShakeDuration = duration / numShakes;
        float halfShake = oneShakeDuration / 2;

        return IntStream.range(0, numShakes)
                .mapToObj(idx -> MathUtils.nextRandomPoint(amplitude))
                .reduce(Actions.sequence(), (SequenceAction seq, Point shakeCoords) -> {

                    seq.addAction(Actions.moveBy(shakeCoords.x, shakeCoords.y, halfShake));
                    var moveBackToCenter = shakeCoords.inverse();
                    seq.addAction(Actions.moveBy(moveBackToCenter.x, moveBackToCenter.y, halfShake));
                    return seq;

                }, Actions::sequence);
    }
}
