package com.tiem625.space_letter_shooter.space.fsm;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.SceneConfigureSpecs;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.ship.EnemyShip;
import com.tiem625.space_letter_shooter.space.ship.ShipRenderSpecs;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.stream.Collectors;

public class LoadSpaceSceneState extends SceneState<SpaceScene> {

    public static final String KEY = "LOAD_SPACE_SCENE";
    private static final Vector2 OFFSCREEN_POS = new Vector2(-500, -500);

    private final SceneConfigureSpec sceneConfigureSpec;

    public LoadSpaceSceneState(String sceneConfigureSpecKey) {
        super(KEY);
        this.sceneConfigureSpec = SceneConfigureSpecs.api.getSceneConfigureSpec(sceneConfigureSpecKey);
    }

    @Override
    public void enterState(String prevStateKey) {
        super.enterState(prevStateKey);
        load(sceneConfigureSpec);
    }

    private void load(SceneConfigureSpec spec) {

        var shipDesiredPositions = spec.getShipPlacements().stream()
                .map(this::placement2ShipWithPosition)
                .peek(posShipPair -> posShipPair.getValue().setPosition(OFFSCREEN_POS.x, OFFSCREEN_POS.y))
                .collect(Collectors.toMap(Pair::getValue, Pair::getKey));

        var totalSetupDelayAction = setupShipsFlyToStartActions(shipDesiredPositions);
        shipDesiredPositions.keySet().forEach(ship -> entity.addEnemyShipToScene(ship));

        entity.getFirstStage()
                .addAction(
                        Actions.delay(totalSetupDelayAction.getDuration(),
                                Actions.run(this::postShipsReadyDescentEvent)));
    }

    private void postShipsReadyDescentEvent() {
        EventsHandling.postEvent(GameEventType.SHIPS_AT_START.makeEvent());
    }

    private Pair<Vector2, EnemyShip> placement2ShipWithPosition(SceneConfigureSpec.ShipPlacement placement) {
        return ImmutablePair.of(
                placement.getPosition().toVector2(),
                new EnemyShip("test", ShipRenderSpecs.api.getRenderSpec(placement.getShipSpecId()))
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
