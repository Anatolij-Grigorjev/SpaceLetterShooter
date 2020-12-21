package com.tiem625.space_letter_shooter.util;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StreamUtils {

    private StreamUtils() {
        throw new ClassIsStaticException(getClass());
    }

    public static <T> Optional<T> findLast(Stream<T> inStream) {
        return inStream.reduce((first, second) -> second);
    }


    public static class RollingValuesSupplier<T> implements Supplier<T> {

        private final T[] values;
        private int currentValueIdx;

        @SafeVarargs
        public RollingValuesSupplier(T... values) {
            assert values.length > 0;
            this.values = values;
            this.currentValueIdx = 0;
        }

        @Override
        public T get() {
            var nextValue = values[currentValueIdx];
            updateCurrentIdx();
            return nextValue;
        }

        private void updateCurrentIdx() {
            currentValueIdx = (currentValueIdx + 1) % values.length;
        }
    }
}
