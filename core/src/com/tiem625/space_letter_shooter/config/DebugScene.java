package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.scene.Scene;

public class DebugScene extends Scene {

    public DebugScene() {
        super();
        addStage(buildDebugInputsStage());
    }

    private Stage buildDebugInputsStage() {
        var stage = new Stage();
        stage.addListener(new ChangeGameConfigInputListener());
        return stage;
    }


    private static class ChangeGameConfigInputListener extends InputListener {
        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            switch (keycode) {

                case Input.Keys.P:
                    ConfigHolder.config.toggleEnabledDynamicBg();
                    break;
                default:
                    System.out.println("Got key input with no action: " + keycode);
            }

            return true;
        }
    }
}
