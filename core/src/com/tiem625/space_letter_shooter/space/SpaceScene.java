package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;
import com.tiem625.space_letter_shooter.resource.Colors;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.space.ship.EnemyShip;
import com.tiem625.space_letter_shooter.space.ship.ShipDescentLoader;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;
import com.tiem625.space_letter_shooter.util.StreamUtils;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class SpaceScene extends Scene {

    private final Stage enemyShipsStage;

    public SpaceScene(SceneConfigureSpec sceneConfigureSpec) {
        super(sceneConfigureSpec.getSceneId());
        enemyShipsStage = addEmptyShipsStage();
        enemyShipsStage.addListener(new ShipTextCharsCaptureListener());
        load(sceneConfigureSpec);
        EventsHandling.addEventHandler(GameEventType.SHIP_REACH_BOTTOM_SCREEN, gameEvent -> {
            stopShipsWithSmiles();
            startGameOverStages();
        });
    }

    public EnemyShip addEnemyShipToScene(EnemyShip ship) {
        Objects.requireNonNull(ship);
        enemyShipsStage.addActor(ship);
        return ship;
    }

    public Stream<EnemyShip> enemyShips() {
        return stages.stream()
                .map(Stage::getActors)
                .flatMap(StreamUtils::stream)
                .filter(actor -> actor instanceof EnemyShip)
                .map(actor -> (EnemyShip) actor);
    }

    private void startGameOverStages() {
        startGameOverOverlayStage();
        startGameOverTextStage();
    }

    private void startGameOverOverlayStage() {
        var overlayStage = addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
        ColorOverlay colorOverlay = ColorOverlay.fullScreen(Colors.BLACK_ALPHA01);
        overlayStage.addActor(colorOverlay);
        colorOverlay.addAction(Actions.color(Colors.BLACK_ALPHA75, 1, Interpolation.fastSlow));
    }

    private void startGameOverTextStage() {
        var gameOverTextStage = addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
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
        enemyShips()
                .filter(not(EnemyShip::isShipDisposing))
                .forEach(ship -> {
            ship.stopActions();
            replaceShipWithSmiling(ship);
        });
    }

    private void replaceShipWithSmiling(EnemyShip originalShip) {
        var smilingShip = originalShip.cloneShip(":)");
        addEnemyShipToScene(smilingShip);
        smilingShip.setSamePosition(originalShip);
        originalShip.remove();
    }

    private void load(SceneConfigureSpec spec) {
        var descentLoader = new ShipDescentLoader();
        var descendingShips = descentLoader.loadDescendingShipsFromSpec(spec);
        descendingShips.forEach(this::addEnemyShipToScene);
    }

    private Stage addEmptyShipsStage() {
        return addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
    }

    private class ShipTextCharsCaptureListener extends InputListener {

        @Override
        public boolean keyTyped(InputEvent event, char character) {

            enemyShips()
                    .filter(ship -> ship.canHitCharacter(character))
                    .findFirst()
                    .ifPresent(enemyShip -> enemyShip.hitCharacter(character));

            return true;
        }
    }

}
