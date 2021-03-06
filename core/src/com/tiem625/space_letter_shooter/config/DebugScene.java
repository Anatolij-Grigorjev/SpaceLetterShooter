package com.tiem625.space_letter_shooter.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.scene.Scene;

public class DebugScene extends Scene {

    public DebugScene() {
        super("debug");
        addAndGetStage(buildDebugInputsStage());
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
                    GamePropsHolder.props.toggleEnabledDynamicBg();
                    break;
                case Input.Keys.Q:
                    System.out.println("!!!DEBUG EXIT GAME!!!");
                    Gdx.app.exit();
                    break;
                case Input.Keys.SPACE:
                    EventsHandling.postEvent(GameEventType.SCENE_TOGGLE_PAUSE.makeEvent());
                default:
                    break;
            }

            return true;
        }
    }
}
