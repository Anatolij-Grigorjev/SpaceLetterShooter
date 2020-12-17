package com.tiem625.space_letter_shooter.util;

import com.badlogic.gdx.math.Vector2;

public class MathUtils {

    private MathUtils() {
        throw new ClassIsStaticException(getClass());
    }

    public static <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (value == null) return min;
        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;

        return value;
    }

    public static Vector2 nextRandomVector2(Vector2 amplitude) {
        var randomDirection = new Vector2().setToRandomDirection();
        randomDirection.x *= amplitude.x;
        randomDirection.y *= amplitude.y;

        return randomDirection;
    }

    public static Vector2 inverse(Vector2 v) {
        return new Vector2(-v.x, -v.y);
    }

}
