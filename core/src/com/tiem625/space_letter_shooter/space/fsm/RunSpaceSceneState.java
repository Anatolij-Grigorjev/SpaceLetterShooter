package com.tiem625.space_letter_shooter.space.fsm;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.SceneConfigureSpecs;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.ship.EnemyShip;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;
import com.tiem625.space_letter_shooter.util.StreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RunSpaceSceneState extends SceneState<SpaceScene> {

    public static final String KEY = "RUN_SPACE_SCENE";

    private final ShipTextCharsCaptureListener currentCharsCaptureListener;
    private final SceneConfigureSpec sceneConfigureSpec;
    private final int sceneNumShips;
    private int disposedShips = 0;


    public RunSpaceSceneState(String sceneConfigSpecId) {
        super(KEY);
        currentCharsCaptureListener = new ShipTextCharsCaptureListener();
        sceneConfigureSpec = SceneConfigureSpecs.api.getSceneConfigureSpec(sceneConfigSpecId);
        this.sceneNumShips = sceneConfigureSpec.getShipPlacements().size();
        EventsHandling.addEventHandler(GameEventType.SHIP_DISPOSING, event -> {
            disposedShips += 1;
        });
    }

    @Override
    public void enterState(String prevStateKey) {
        entity.getEnemyShipsStage().addListener(currentCharsCaptureListener);

        var shipDescentSpecs = sceneConfigureSpec.getShipDescentSpecs().stream()
                .collect(Collectors.toMap(SceneConfigureSpec.ShipDescentSpec::getShipId, Function.identity()));

        entity.enemyShips().forEach(ship -> {
            ship.addAction(Actions.sequence(
                    Actions.delay(0.5f),
                    Actions.run(() -> {
                        addShipStepsDescentActions(ship, shipDescentSpecs.get(ship.getId()));
                        postShipReachedBottomEvent(ship);
                    })
            ));
        });
    }

    @Override
    public void exitState(String nextStateKey) {
        entity.getEnemyShipsStage().removeListener(currentCharsCaptureListener);
    }

    private void postShipReachedBottomEvent(EnemyShip ship) {
        ship.addAction(Actions.after(Actions.run(() -> {
            EventsHandling.postEvent(GameEventType.SHIP_REACH_BOTTOM_SCREEN.makeEvent(
                    Map.of("ship", ship)
            ));
        })));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (disposedShips >= sceneNumShips) {
            EventsHandling.postEvent(GameEventType.SHIP_REACH_BOTTOM_SCREEN.makeEvent());
        }
    }

    private void addShipStepsDescentActions(
            EnemyShip ship,
            SceneConfigureSpec.ShipDescentSpec shipDescentSpec
    ) {

        List<Vector2> descentSteps = buildShipDescentPositions(ship, shipDescentSpec);
        List<Action> actionsList = new ArrayList<>();
        descentSteps.stream()
                .reduce(actionsList, (actions, nextStepPosition) -> {
                    final Vector2 prevStepEndPosition = findPrevStepEndPosition(actions)
                            .orElse(new Vector2(ship.getX(), ship.getY()));
                    actions.add(buildShipDescentAction(
                            prevStepEndPosition,
                            nextStepPosition,
                            shipDescentSpec.getSpeedMin(),
                            shipDescentSpec.getSpeedMax()
                    ));
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

    private List<Vector2> buildShipDescentPositions(EnemyShip ship, SceneConfigureSpec.ShipDescentSpec shipDescentSpec) {
        final Supplier<Float> edgesSupplier = new StreamUtils.RollingValuesSupplier<>(
                shipDescentSpec.getDescentStepsX().stream().map(Integer::floatValue).toArray(Float[]::new)
        );
        return breakShipHeightIntoDescentSteps(
                ship.getY(),
                -ship.getShipTextureSize().y,
                shipDescentSpec.getStepMin(),
                shipDescentSpec.getStepMax(),
                edgesSupplier
        );
    }

    private List<Vector2> breakShipHeightIntoDescentSteps(
            float startHeight,
            float endHeight,
            float descentStepMin,
            float descentStepMax,
            Supplier<Float> stepXCoordSource
    ) {

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

    private Action buildShipDescentAction(
            Vector2 moveFrom,
            Vector2 moveTo,
            float descentSpeedMin,
            float descentSpeedMax
    ) {

        var speed = MathUtils.random(descentSpeedMin, descentSpeedMax);
        var distance = new Vector2(Math.abs(moveFrom.x - moveTo.x), moveTo.y).len();

        return Actions.moveTo(moveTo.x, moveTo.y, distance / speed, Interpolation.sine);
    }

    private class ShipTextCharsCaptureListener extends InputListener {

        @Override
        public boolean keyTyped(InputEvent event, char character) {

            entity.enemyShips()
                    .filter(ship -> ship.canHitCharacter(character))
                    .findFirst()
                    .ifPresent(enemyShip -> enemyShip.hitCharacter(character));

            return true;
        }
    }
}
