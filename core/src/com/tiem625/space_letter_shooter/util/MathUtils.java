package com.tiem625.space_letter_shooter.util;

import com.badlogic.gdx.math.RandomXS128;

import java.util.Collection;
import java.util.Random;

public class MathUtils {

    public static final Random RNG = new RandomXS128();

    private MathUtils() {
        throw new ClassIsStaticException(getClass());
    }

    public static <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (value == null) return min;
        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;

        return value;
    }

    public static <T> T nextRandomElement(Collection<T> col) {
        return col.stream()
                .skip(RNG.nextInt(col.size()))
                .findFirst()
                .orElseThrow();
    }
}
