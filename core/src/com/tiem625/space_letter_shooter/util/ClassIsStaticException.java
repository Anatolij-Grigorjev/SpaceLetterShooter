package com.tiem625.space_letter_shooter.util;

public class ClassIsStaticException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Class '%s' is static!";
    private final Class<?> clazz;

    public ClassIsStaticException(Class<?> clazz) {
        super(String.format(MESSAGE_FORMAT, clazz.getSimpleName()));
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
