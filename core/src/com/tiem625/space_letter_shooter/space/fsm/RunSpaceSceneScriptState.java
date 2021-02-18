package com.tiem625.space_letter_shooter.space.fsm;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.SceneScripts;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.enemy.EnemyShip;
import com.tiem625.space_letter_shooter.space.spec.SceneScript;
import com.tiem625.space_letter_shooter.space.spec.SceneScript.ShipDescentSpec;
import com.tiem625.space_letter_shooter.util.StreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class RunSpaceSceneScriptState extends SceneState<SpaceScene> {

    public static final String KEY = "RUN_SPACE_SCENE";

    private final ShipTextCharsCaptureListener currentCharsCaptureListener;
    private SceneScript sceneScript;
    private int sceneNumShips;
    private int disposedShips;


    public RunSpaceSceneScriptState() {
        super(KEY);
        currentCharsCaptureListener = new ShipTextCharsCaptureListener();
        this.sceneNumShips = 0;
        this.disposedShips = 0;
        EventsHandling.addEventHandler(GameEventType.SHIP_GONE, event -> {
            disposedShips += 1;
        });
    }

    public void setSceneScript(String scriptId) {
        sceneScript = SceneScripts.api.getSceneScript(scriptId);
        this.sceneNumShips = sceneScript.getShipPlacements().size();
        this.disposedShips = 0;
    }

    @Override
    public void enterState(String prevStateKey) {
        entity.getEnemyShipsStage().addListener(currentCharsCaptureListener);

        var shipDescentSpecs = sceneScript.getShipDescentSpecsMap();

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
            EventsHandling.postEvent(GameEventType.SCRIPT_CLEAR.makeEvent(Map.of("script", sceneScript)));
        }
    }

    private void addShipStepsDescentActions(EnemyShip ship, ShipDescentSpec shipDescentSpec) {

        List<Vector2> descentSteps = buildShipDescentPositions(ship, shipDescentSpec);
        List<Action> actionsList = new ArrayList<>();
        descentSteps.stream()
                .reduce(actionsList, (actions, nextStepPosition) -> {
                    final Vector2 prevStepEndPosition = findPrevStepEndPosition(actions)
                            .orElse(new Vector2(ship.getX(), ship.getY()));
                    actions.add(buildShipDescentAction(
                            prevStepEndPosition,
                            nextStepPosition,
                            ship.nextVelocity()
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

    private List<Vector2> buildShipDescentPositions(EnemyShip ship, ShipDescentSpec shipDescentSpec) {
        final Supplier<Float> edgesSupplier = new StreamUtils.RollingValuesSupplier<>(
                shipDescentSpec.getDescentStepsX().toArray(Float[]::new)
        );
        return breakShipHeightIntoDescentSteps(ship, edgesSupplier);
    }

    private List<Vector2> breakShipHeightIntoDescentSteps(EnemyShip ship, Supplier<Float> stepXCoordSource) {
        var startHeight = ship.getY();
        var endHeight = -ship.getHeight();
        var descentSteps = new ArrayList<Vector2>();
        var remainingHeight = startHeight;
        while (remainingHeight > endHeight) {
            var stepX = stepXCoordSource.get();
            var nextStepHeight = ship.nextDescentStep();
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

    private Action buildShipDescentAction(Vector2 moveFrom, Vector2 moveTo, float speed) {

        var distance = new Vector2(Math.abs(moveFrom.x - moveTo.x), moveTo.y).len();
        return Actions.moveTo(moveTo.x, moveTo.y, distance / speed, Interpolation.sine);
    }

    private class ShipTextCharsCaptureListener extends InputListener {

        @Override
        public boolean keyTyped(InputEvent event, char character) {
            var shootingShip = entity.getShootingShip();
            entity.enemyShips()
                    .filter(ship -> ship.canHitCharacter(character))
                    .findAny()
                    .ifPresent(enemyShip -> {
                        shootingShip.shootAt(enemyShip);
                        enemyShip.hitCharacter(character);
                    });

            return true;
        }
    }
}
