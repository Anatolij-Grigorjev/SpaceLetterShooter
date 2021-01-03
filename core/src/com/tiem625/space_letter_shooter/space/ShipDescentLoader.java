package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEvent;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;
import com.tiem625.space_letter_shooter.util.StreamUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ShipDescentLoader {

    private static final Vector2 OFFSCREEN_POS = new Vector2(-500, -500);

    private float descentStepMin;
    private float descentStepMax;
    private float descentSpeedMin;
    private float descentSpeedMax;

    public Set<EnemyShip> loadDescendingShipsFromSpec(SceneConfigureSpec spec) {
        descentStepMin = spec.getShipDescentSpec().getStepMin();
        descentStepMax = spec.getShipDescentSpec().getStepMax();
        descentSpeedMin = spec.getShipDescentSpec().getSpeedMin();
        descentSpeedMax = spec.getShipDescentSpec().getSpeedMax();

        var shipDesiredPositions = spec.getShipPlacements().stream()
                .map(this::placement2ShipWithPosition)
                .peek(posShipPair -> posShipPair.getValue().setPosition(OFFSCREEN_POS.x, OFFSCREEN_POS.y))
                .collect(Collectors.toMap(Pair::getValue, Pair::getKey));

        var totalSetupDelayAction = setupShipsFlyToStartActions(shipDesiredPositions);

        var enemyShips = shipDesiredPositions.keySet();

        enemyShips.forEach(ship -> {
            //wait for all ships to get in position
            ship.addAction(Actions.sequence(
                    Actions.delay(totalSetupDelayAction.getDuration() + 0.5f),
                    Actions.run(() -> {
                        addShipStepsDescentActions(ship);
                        postShipReachedBottomEvent(ship);
                    })
            ));
        });

        return enemyShips;
    }

    private void postShipReachedBottomEvent(EnemyShip ship) {
        ship.addAction(Actions.after(Actions.run(() -> {
            if (ship.isShipDisposing()) {
                return;
            }
            EventsHandling.postEvent(new GameEvent(
                    GameEventType.SHIP_REACH_BOTTOM_SCREEN,
                    Map.of("ship", ship)
            ));
        })));
    }

    private void addShipStepsDescentActions(EnemyShip ship) {

        List<Vector2> descentSteps = buildShipDescentPositions(ship);
        List<Action> actionsList = new ArrayList<>();
        descentSteps.stream()
                .reduce(actionsList, (actions, nextStepPosition) -> {
                    final Vector2 prevStepEndPosition = findPrevStepEndPosition(actions)
                            .orElse(new Vector2(ship.getX(), ship.getY()));
                    actions.add(buildShipDescentAction(prevStepEndPosition, nextStepPosition));
                    return actions;
                }, StreamUtils.concatLists())
                .forEach(action -> ship.addAction(Actions.after(action)));
    }

    private Optional<Vector2> findPrevStepEndPosition(List<Action> actions) {
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
//            edgesSupplier = new StreamUtils.RollingValuesSupplier<>(leftEdgeX, rightEdgeX);
            edgesSupplier = new StreamUtils.RollingValuesSupplier<>(ship.getX());
        } else {
//            edgesSupplier = new StreamUtils.RollingValuesSupplier<>(rightEdgeX, leftEdgeX);
            edgesSupplier = new StreamUtils.RollingValuesSupplier<>(ship.getX());
        }
        return breakShipHeightIntoDescentSteps(ship.getY(), -ship.getShipTextureSize().y, edgesSupplier);
    }

    private List<Vector2> breakShipHeightIntoDescentSteps(float startHeight, float endHeight, Supplier<Float> stepXCoordSource) {

        float fullDescentHeight = startHeight - endHeight;
        int maxDescentSteps = (int) (fullDescentHeight / descentStepMin);
        var descentSteps = new ArrayList<Vector2>(maxDescentSteps);
        var remainingHeight = startHeight;
        while (remainingHeight > endHeight) {
            var stepX = stepXCoordSource.get();
            var nextStepHeight = MathUtils.random(descentStepMin, descentStepMax);
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

        var speed = MathUtils.random(descentSpeedMin, descentSpeedMax);
        var distance = new Vector2(Math.abs(moveFrom.x - moveTo.x), moveTo.y).len();

        return Actions.moveTo(moveTo.x, moveTo.y, distance / speed, Interpolation.sine);
    }

    private Pair<Vector2, EnemyShip> placement2ShipWithPosition(SceneConfigureSpec.ShipPlacement placement) {
        return ImmutablePair.of(
                placement.getPosition().toVector2(),
                new EnemyShip("test:" + placement.getPosition().getY(), ShipRenderSpecs.api.getRenderSpec(placement.getShipSpecId()))
        );
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


}
