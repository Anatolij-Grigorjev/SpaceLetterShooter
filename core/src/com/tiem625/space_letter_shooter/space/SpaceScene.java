package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.space.dto.SceneConfigureSpec;
import com.tiem625.space_letter_shooter.util.Point;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpaceScene extends Scene {

    private final Stage enemyShipsStage;

    public SpaceScene() {
        super();
        enemyShipsStage = addEmptyShipsStage();
        enemyShipsStage.addListener(new ShipTextCharsCaptureListener());
    }

    public void load(SceneConfigureSpec spec) {

        var shipDesiredPositions = spec.shipPlacements.stream()
                .map(this::placement2ShipWithPoint)
                //add to stage and hide enemy ship
                .peek(shipAndPoint ->
                        addEnemyShip(shipAndPoint.getRight())
                                .setPosition(-500, -500)
                )
                .collect(Collectors.toMap(Pair::getValue, Pair::getKey));

        setupShipsFlyToStartActions(shipDesiredPositions);
    }

    public EnemyShip addEnemyShip(EnemyShip ship) {
        Objects.requireNonNull(ship);
        enemyShipsStage.addActor(ship);

        return ship;
    }

    public Stream<EnemyShip> enemyShips() {
        return stages.stream()
                .map(Stage::getActors)
                .flatMap(actors -> Stream.of(actors.items))
                .filter(actor -> actor instanceof EnemyShip)
                .map(actor -> (EnemyShip) actor);
    }

    private Pair<Point, EnemyShip> placement2ShipWithPoint(SceneConfigureSpec.ShipPlacement placement) {
        return ImmutablePair.of(
                placement.position,
                new EnemyShip("test:" + placement.position.y, ShipRenderSpecs.getRenderSpec(placement.shipSpecId))
        );
    }

    private Stage addEmptyShipsStage() {
        return addAndGetStage(new Stage(new FitViewport(
                GamePropsHolder.props.getResolutionWidth(),
                GamePropsHolder.props.getResolutionHeight()
        )));
    }

    private void setupShipsFlyToStartActions(Map<EnemyShip, Point> shipDesiredPositions) {
        shipDesiredPositions.entrySet().stream()
                .reduce(Actions.delay(0.5f), (prevDelay, shipPositionPair) -> {
                    var ship = shipPositionPair.getKey();
                    var startPosition = shipPositionPair.getValue();
                    var moveToStartAction = Actions.moveTo(
                            startPosition.x,
                            startPosition.y,
                            1,
                            Interpolation.sine
                    );
                    var shipActions = Actions.sequence(
                            Actions.delay(prevDelay.getDuration()),
                            moveToStartAction
                    );
                    ship.addAction(shipActions);

                    return Actions.delay(prevDelay.getDuration() + 0.5f);
                }, (delay1, delay2) -> Actions.delay(delay1.getDuration() + delay2.getDuration()));
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
