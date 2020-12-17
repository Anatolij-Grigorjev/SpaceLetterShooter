package com.tiem625.space_letter_shooter.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {

    public final float x;
    public final float y;

    public Point(
            @JsonProperty("x") float x,
            @JsonProperty("y") float y
    ) {
        this.x = x;
        this.y = y;
    }

    public Point inverse() {
        return new Point(-x, -y);
    }
}
