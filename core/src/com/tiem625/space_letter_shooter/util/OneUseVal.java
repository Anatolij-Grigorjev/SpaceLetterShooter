package com.tiem625.space_letter_shooter.util;

public class OneUseVal<T> {

    private T currentValue;
    private final T defaultValue;

    public static <T> OneUseVal<T> withDefault(T value) {
        return new OneUseVal<>(value);
    }

    private OneUseVal(T defaultValue) {
        this.defaultValue = defaultValue;
        resetValue();
    }

    public void set(T value) {
        this.currentValue = value;
    }

    public T get() {
        var current = this.currentValue;
        resetValue();
        return current;
    }

    private void resetValue() {
        this.currentValue = this.defaultValue;
    }
}
