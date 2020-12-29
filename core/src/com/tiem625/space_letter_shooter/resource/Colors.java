package com.tiem625.space_letter_shooter.resource;

import com.badlogic.gdx.graphics.Color;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

public class Colors {

    public static final Color BLACK_ALPHA01 = new Color(0, 0, 0, 0.01f);
    public static final Color BLACK_ALPHA75 = new Color(0.0f, 0.0f, 0.0f, 0.75f);


    private Colors() {
        throw new ClassIsStaticException(getClass());
    }
}
