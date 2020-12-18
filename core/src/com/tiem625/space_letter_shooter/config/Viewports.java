package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Viewports {

    public static final Viewport FIT_FULLSCREEN = new FitViewport(
            GamePropsHolder.props.getResolutionWidth(),
            GamePropsHolder.props.getResolutionHeight()
    );

}
