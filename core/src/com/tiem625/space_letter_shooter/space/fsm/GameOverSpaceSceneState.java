package com.tiem625.space_letter_shooter.space.fsm;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;
import com.tiem625.space_letter_shooter.resource.Colors;
import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.ColorOverlay;
import com.tiem625.space_letter_shooter.space.GameOverText;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.ship.EnemyShip;

import static java.util.function.Predicate.not;

public class GameOverSpaceSceneState extends SceneState<SpaceScene> {

    public static final String KEY = "GAME_OVER_SPACE_SCENE";

    public GameOverSpaceSceneState() {
        super(KEY);
    }

    @Override
    public void enterState(String prevStateKey) {
        super.enterState(prevStateKey);
        stopShipsWithSmiles();
        startGameOverStages();
    }

    private void startGameOverStages() {
        startGameOverOverlayStage();
        startGameOverTextStage();
    }

    private void startGameOverOverlayStage() {
        var overlayStage = entity.addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
        ColorOverlay colorOverlay = ColorOverlay.fullScreen(Colors.BLACK_ALPHA01);
        overlayStage.addActor(colorOverlay);
        colorOverlay.addAction(Actions.color(Colors.BLACK_ALPHA75, 1, Interpolation.fastSlow));
    }

    private void startGameOverTextStage() {
        var gameOverTextStage = entity.addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
        //create animations for stage root since scaling text
        //only works when mapped to viewport
        var textMoveDuration = 1.0f;
        var textTravelExtent = 150.0f;
        var root = gameOverTextStage.getRoot();
        var resolution = GamePropsHolder.props.getResolution();
        root.setPosition(resolution.x / 2, resolution.y / 2 + textTravelExtent);
        root.setScale(0.0f);
        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, -2 * textTravelExtent, textMoveDuration, Interpolation.fastSlow),
                        Actions.scaleTo(0.25f, 0.25f, textMoveDuration, Interpolation.slowFast)
                ),
                Actions.parallel(
                        Actions.moveBy(0, textTravelExtent, textMoveDuration, Interpolation.slowFast),
                        Actions.scaleTo(1, 1, textMoveDuration, Interpolation.fastSlow)
                )
        ));
        GameOverText gameOverText = new GameOverText();
        gameOverTextStage.addActor(gameOverText);
        gameOverTextStage.addListener(new GameOverText.RestartSceneInputListener());
        InputProcessorManager.setCurrentInputProcessors(gameOverTextStage);
    }

    private void stopShipsWithSmiles() {
        entity.enemyShips()
                .filter(not(EnemyShip::isShipDisposing))
                .forEach(ship -> {
                    ship.stopActions();
                    replaceShipWithSmiling(ship);
                });
    }

    private void replaceShipWithSmiling(EnemyShip originalShip) {
        var smilingShip = originalShip.cloneShip(":)");
        entity.addEnemyShipToScene(smilingShip);
        smilingShip.setSamePosition(originalShip);
        originalShip.remove();
    }
}
