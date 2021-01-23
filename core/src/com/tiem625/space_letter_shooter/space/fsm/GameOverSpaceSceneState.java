package com.tiem625.space_letter_shooter.space.fsm;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;
import com.tiem625.space_letter_shooter.resource.Colors;
import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.ColorOverlay;
import com.tiem625.space_letter_shooter.space.FlyInCenterText;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.enemy.EnemyShip;

import static java.util.function.Predicate.not;

public class GameOverSpaceSceneState extends SceneState<SpaceScene> {

    public static final String KEY = "GAME_OVER_SPACE_SCENE";
    private final String[] centerTextLines = {
            "GAME OVER",
            "Press 'R' to try again...",
            "Press 'Q' to quit..."
    };

    public GameOverSpaceSceneState() {
        super(KEY);
    }

    @Override
    public void enterState(String prevStateKey) {
        stopShipsWithSmiles();
        startGameOverStages();
    }

    private void startGameOverStages() {
        startGameOverOverlayStage();
        startGameOverTextStage();
    }

    private void startGameOverOverlayStage() {
        var overlayStage = entity.addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
        ColorOverlay colorOverlay = ColorOverlay.fullScreen(Colors.DARK_RED_ALPHA75);
        overlayStage.addActor(colorOverlay);
        colorOverlay.addAction(Actions.color(colorOverlay.getColor(), 1, Interpolation.fastSlow));
    }

    private void startGameOverTextStage() {
        var gameOverTextStage = entity.addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
        FlyInCenterText flyInCenterText = new FlyInCenterText(centerTextLines);
        flyInCenterText.setAnimateOnStage(gameOverTextStage);
        gameOverTextStage.addListener(new RestartSceneInputListener());
        InputProcessorManager.setCurrentInputProcessors(gameOverTextStage);
    }

    private void stopShipsWithSmiles() {
        entity.enemyShips()
                .filter(not(EnemyShip::isShipDisposing))
                .forEach(liveShip -> {
                    liveShip.stopActions();
                    liveShip.setShipText(":)");
                });
    }

    private static class RestartSceneInputListener extends InputListener {

        @Override
        public boolean keyTyped(InputEvent event, char character) {
            switch (character) {
                case 'R':
                case 'r':
                    EventsHandling.postEvent(GameEventType.SCENE_RESTART.makeEvent());
                    break;
                case 'Q':
                case 'q':
                    EventsHandling.postEvent(GameEventType.SCENE_QUIT.makeEvent());
                    break;
                default:
                    break;
            }

            return true;
        }
    }
}
