package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AfterAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.space.dto.SceneConfigureSpec;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SpaceScene extends Scene {

    private final Stage enemyShipsStage;

    public SpaceScene() {
        super();
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
                    Actions.run(() -> addShipStepsDescentActions(ship))
            ));
        });
    }

    private void addShipStepsDescentActions(EnemyShip ship) {
        int resolutionWidth = GamePropsHolder.props.getResolutionWidth();
        float borderXEvenSteps, borderXOddSteps;
        if (ship.getX() < resolutionWidth / 2f) {
            borderXEvenSteps = 0f;
            borderXOddSteps = resolutionWidth - ship.getShipTextureSize().x;
        } else {
            borderXEvenSteps = resolutionWidth - ship.getShipTextureSize().x;
            borderXOddSteps = 0f;
        }

        List<Vector2> descentSteps = breakToDescentSteps(ship.getY(), borderXEvenSteps, borderXOddSteps);
        descentSteps.stream()
                .reduce(new ArrayList<Action>(), (actions, nextStep) -> {
                    var prevStepX = actions.isEmpty() ? ship.getX() : ((MoveToAction)(actions.get(actions.size() - 1))).getX();
                    actions.add(buildShipDescentAction(prevStepX, nextStep.x, nextStep.y));
                    return actions;
                }, ((actions1, actions2) -> {
                    var result = new ArrayList<Action>(actions1);
                    result.addAll(actions2);
                    return result;
                }))
                .forEach(action -> ship.addAction(Actions.after(action)));
        printShipMovements(ship);
    }

    private void printShipMovements(EnemyShip ship) {
        System.out.println("===========SHIP: " + ship.getName() + "===========");
        StreamSupport.stream(ship.getActions().spliterator(), false)
                .filter(action -> action instanceof AfterAction)
                .map(afterAction -> ((AfterAction) afterAction).getAction())
                .filter(action -> action instanceof MoveToAction)
                .map(moveToAction -> ((MoveToAction) moveToAction))
                .forEachOrdered(moveToAction -> {
                    System.out.println("move to: [" + moveToAction.getX() + ";" + moveToAction.getY() + "] duration: " + moveToAction.getDuration() + "s");
                });
    }

    private List<Vector2> breakToDescentSteps(float fullDescentHeight, float evenStepX, float oddStepX) {

        var stepSizeMin = 10f;
        var stepSizeMax = 25f;
        int maxDescentSteps = (int) (fullDescentHeight / stepSizeMin);
        var descentSteps = new ArrayList<Vector2>(maxDescentSteps);
        var remainingHeight = fullDescentHeight;
        var iterationNum = 0;
        while (remainingHeight > 0) {
            var stepX = iterationNum % 2 == 0 ? evenStepX : oddStepX;
            var nextStepHeight = MathUtils.random(stepSizeMin, stepSizeMax);
            if (remainingHeight >= nextStepHeight) {
                remainingHeight -= nextStepHeight;
                descentSteps.add(new Vector2(stepX, remainingHeight));
            } else {
                descentSteps.add(new Vector2(stepX, remainingHeight));
                remainingHeight = 0;
            }
            iterationNum++;
        }

        return descentSteps;
    }

    private Action buildShipDescentAction(float moveFromX, float moveToX, float moveToY) {

        var speed = MathUtils.random(150f, 205f);
        var distance = new Vector2(Math.abs(moveFromX - moveToX), moveToY).len();

        return Actions.moveTo(moveToX, moveToY, distance / speed, Interpolation.fastSlow);
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
