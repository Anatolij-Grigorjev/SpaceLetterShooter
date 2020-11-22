package com.tiem625.space_letter_shooter.util;

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
}
