package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.scene.SceneId;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;
import com.tiem625.space_letter_shooter.util.StreamUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpaceScene extends Scene {

    private final Stage enemyShipsStage;

    public SpaceScene() {
        super(SceneId.SHIPS_SPACE);
        enemyShipsStage = addEmptyShipsStage();
        enemyShipsStage.addListener(new ShipTextCharsCaptureListener());
    }

    public void load(SceneConfigureSpec spec) {

        var shipDesiredPositions = spec.shipPlacements.stream()
                .map(this::placement2ShipWithPosition)
                //add to stage and hide enemy ship
                .peek(shipAndPoint ->
                        addEnemyShip(shipAndPoint.getRight())
                                .setPosition(-500, -500)
                )
                .collect(Collectors.toMap(Pair::getValue, Pair::getKey));

        var totalSetupDelayAction = setupShipsFlyToStartActions(shipDesiredPositions);

        enemyShips().forEach(ship -> {
            //wait for all ships to get in position
            ship.addAction(Actions.sequence(
                    Actions.delay(totalSetupDelayAction.getDuration() + 0.5f),
                    Actions.run(() -> {
                        addShipStepsDescentActions(ship);
                        postShipReachedBottomEvent(ship);
                    })
            ));
        });
    }

    private void postShipReachedBottomEvent(EnemyShip ship) {
        ship.addAction(Actions.after(Actions.run(() -> EventsHandling.postEvent(new ShipReachBottomOfScreenEvent(SceneId.SHIPS_SPACE, ship)))));
    }

    private void addShipStepsDescentActions(EnemyShip ship) {

        List<Vector2> descentSteps = buildShipDescentPositions(ship);

        descentSteps.stream()
                .reduce(new ArrayList<Action>(), (actions, nextStepPosition) -> {
                    final Vector2 prevStepEndPosition = findPrevStepEndPosition(actions)
                            .orElse(new Vector2(ship.getX(), ship.getY()));
                    actions.add(buildShipDescentAction(prevStepEndPosition, nextStepPosition));
                    return actions;
                }, ((actions1, actions2) -> {
                    var result = new ArrayList<Action>(actions1);
                    result.addAll(actions2);
                    return result;
                }))
                .forEach(action -> ship.addAction(Actions.after(action)));
    }

    private Optional<Vector2> findPrevStepEndPosition(ArrayList<Action> actions) {
        return StreamUtils.findLast(actions.stream())
                .filter(action -> action instanceof MoveToAction)
                .map(action -> (MoveToAction) action)
                .map(moveToAction -> new Vector2(moveToAction.getX(), moveToAction.getY()));
    }

    private List<Vector2> buildShipDescentPositions(EnemyShip ship) {
        final int resolutionWidth = GamePropsHolder.props.getResolutionWidth();
        final float leftEdgeX = 0;
        final float rightEdgeX = resolutionWidth - ship.getShipTextureSize().x;
        final Supplier<Float> edgesSupplier;
        if (ship.getX() < resolutionWidth / 2f) {
            edgesSupplier = new StreamUtils.RollingValuesSupplier<>(leftEdgeX, rightEdgeX);
        } else {
            edgesSupplier = new StreamUtils.RollingValuesSupplier<>(rightEdgeX, leftEdgeX);
        }
        return breakShipHeightIntoDescentSteps(ship.getY(), -ship.getShipTextureSize().y, edgesSupplier);
    }

    private List<Vector2> breakShipHeightIntoDescentSteps(float startHeight, float endHeight, Supplier<Float> stepXCoordSource) {

        var stepSizeMin = 10f;
        var stepSizeMax = 25f;
        float fullDescentHeight = startHeight - endHeight;
        int maxDescentSteps = (int) (fullDescentHeight / stepSizeMin);
        var descentSteps = new ArrayList<Vector2>(maxDescentSteps);
        var remainingHeight = startHeight;
        while (remainingHeight > endHeight) {
            var stepX = stepXCoordSource.get();
            var nextStepHeight = MathUtils.random(stepSizeMin, stepSizeMax);
            if (remainingHeight - nextStepHeight > endHeight) {
                remainingHeight -= nextStepHeight;
                descentSteps.add(new Vector2(stepX, remainingHeight));
            } else {
                descentSteps.add(new Vector2(stepX, remainingHeight));
                remainingHeight = endHeight;
            }
        }

        return descentSteps;
    }

    private Action buildShipDescentAction(Vector2 moveFrom, Vector2 moveTo) {

        var speed = MathUtils.random(1500f, 2005f);
        var distance = new Vector2(Math.abs(moveFrom.x - moveTo.x), moveTo.y).len();

        return Actions.moveTo(moveTo.x, moveTo.y, distance / speed, Interpolation.sine);
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

    private Pair<Vector2, EnemyShip> placement2ShipWithPosition(SceneConfigureSpec.ShipPlacement placement) {
        return ImmutablePair.of(
                placement.position.toVector2(),
                new EnemyShip("test:" + placement.position.y, ShipRenderSpecs.getRenderSpec(placement.shipSpecId))
        );
    }

    private Stage addEmptyShipsStage() {
        return addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
    }

    private DelayAction setupShipsFlyToStartActions(Map<EnemyShip, Vector2> shipDesiredPositions) {
        return shipDesiredPositions.entrySet().stream()
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
