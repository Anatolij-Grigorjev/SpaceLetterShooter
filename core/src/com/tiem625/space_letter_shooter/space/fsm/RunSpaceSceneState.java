package com.tiem625.space_letter_shooter.space.fsm;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.SceneConfigureSpecs;
import com.tiem625.space_letter_shooter.space.ShipTextCharsCaptureListener;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.ship.EnemyShip;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;
import com.tiem625.space_letter_shooter.util.StreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class RunSpaceSceneState extends SceneState<SpaceScene> {

    public static final String KEY = "RUN_SPACE_SCENE";
    private float descentStepMin;
    private float descentStepMax;
    private float descentSpeedMin;
    private float descentSpeedMax;

    private ShipTextCharsCaptureListener currentCharsCaptureListener;


    public RunSpaceSceneState(String sceneConfigSpecId) {
        super(KEY);
        currentCharsCaptureListener = null;
        var sceneConfigureSpec = SceneConfigureSpecs.api.getSceneConfigureSpec(sceneConfigSpecId);
        readDescentConfig(sceneConfigureSpec);
    }

    private void readDescentConfig(SceneConfigureSpec spec) {
        descentStepMin = spec.getShipDescentSpec().getStepMin();
        descentStepMax = spec.getShipDescentSpec().getStepMax();
        descentSpeedMin = spec.getShipDescentSpec().getSpeedMin();
        descentSpeedMax = spec.getShipDescentSpec().getSpeedMax();
    }

    @Override
    public void enterState(String prevStateKey) {
        currentCharsCaptureListener = new ShipTextCharsCaptureListener(entity);
        entity.getEnemyShipsStage().addListener(currentCharsCaptureListener);
        entity.enemyShips().forEach(ship -> {
            ship.addAction(Actions.sequence(
                    Actions.delay(0.5f),
                    Actions.run(() -> {
                        addShipStepsDescentActions(ship);
                        postShipReachedBottomEvent(ship);
                    })
            ));
        });
    }

    @Override
    public void exitState(String nextStateKey) {
        Optional.ofNullable(currentCharsCaptureListener)
                .ifPresent(listener -> entity.getEnemyShipsStage().removeListener(listener));
        currentCharsCaptureListener = null;
    }

    private void postShipReachedBottomEvent(EnemyShip ship) {
        ship.addAction(Actions.after(Actions.run(() -> {
            EventsHandling.postEvent(GameEventType.SHIP_REACH_BOTTOM_SCREEN.makeEvent(
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
        return actions.stream()
                .reduce(StreamUtils.findLast())
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
}
