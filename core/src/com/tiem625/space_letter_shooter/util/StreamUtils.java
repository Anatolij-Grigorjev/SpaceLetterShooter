package com.tiem625.space_letter_shooter.util;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtils {

    private StreamUtils() {
        throw new ClassIsStaticException(getClass());
    }

    public static <T> Optional<T> findLast(Stream<T> inStream) {
        return inStream.reduce((first, second) -> second);
    }

    public static <E> BinaryOperator<List<E>> concatLists() {
        return (list1, list2) ->
            Stream.concat(list1.stream(), list2.stream())
                    .collect(Collectors.toList());
    }

    /**
     * A {@link Supplier} that consecutively provides values from the initial non-empty values array. <br/>
     * If more values are needed, the supplier restarts from the beginning.
     * @param <T> the type of values provided by this {@link Supplier}
     */
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
