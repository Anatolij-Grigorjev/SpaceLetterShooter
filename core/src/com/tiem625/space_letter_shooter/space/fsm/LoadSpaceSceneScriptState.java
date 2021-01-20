package com.tiem625.space_letter_shooter.space.fsm;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.resource.Colors;
import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.ColorOverlay;
import com.tiem625.space_letter_shooter.space.FlyInCenterText;
import com.tiem625.space_letter_shooter.space.SceneScripts;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.ship.EnemyShip;
import com.tiem625.space_letter_shooter.space.ship.ShipRenderSpecs;
import com.tiem625.space_letter_shooter.space.spec.SceneScript;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.stream.Collectors;

public class LoadSpaceSceneScriptState extends SceneState<SpaceScene> {

    public static final String KEY = "LOAD_SPACE_SCENE";
    private static final Vector2 OFFSCREEN_POS = new Vector2(-500, -500);

    private SceneScript sceneScript;

    public LoadSpaceSceneScriptState() {
        super(KEY);
    }

    public void setSceneScript(String scriptId) {
        this.sceneScript = SceneScripts.api.getSceneScript(scriptId);
    }

    public String getLoadedSceneScriptId() {
        return this.sceneScript.getScriptId();
    }

    @Override
    public void enterState(String prevStateKey) {
        super.enterState(prevStateKey);

        load(sceneScript);
    }

    private void load(SceneScript spec) {

        var shipDesiredPositions = spec.getShipPlacements().stream()
                .map(this::placement2ShipWithPosition)
                .peek(posShipPair -> {
                    var ship = posShipPair.getValue();
                    var descentSpec = spec.getShipDescentSpecsMap().get(ship.getId());
                    setShipDescentAttributes(ship, descentSpec);
                })
                .collect(Collectors.toMap(Pair::getValue, Pair::getKey));

        var totalSetupDelayAction = setupShipsFlyToStartActions(shipDesiredPositions);
        shipDesiredPositions.keySet().forEach(ship -> entity.addEnemyShipToScene(ship));
        float shipsFlyInDuration = totalSetupDelayAction.getDuration() + 0.5f;


        var whenShipsReadyActions = Actions.sequence(Actions.delay(shipsFlyInDuration));
        addStageLoadingOverlay(whenShipsReadyActions);
        addStageTitleSequence(whenShipsReadyActions);
        whenShipsReadyActions.addAction(Actions.run(this::postShipsReadyDescentEvent));
        entity.getEnemyShipsStage().addAction(whenShipsReadyActions);
    }

    private void setShipDescentAttributes(EnemyShip ship, SceneScript.ShipDescentSpec descentSpec) {
        //set initial position
        ship.setPosition(OFFSCREEN_POS.x, OFFSCREEN_POS.y);
        ship.setSpeedRange(descentSpec.getSpeedMin(), descentSpec.getSpeedMax());
        ship.setDescentYRange(descentSpec.getStepMin(), descentSpec.getStepMax());
    }

    private void addStageLoadingOverlay(SequenceAction postLoadSequence) {

        var enemyShipsStage = entity.getEnemyShipsStage();
        var loadingOverlay = ColorOverlay.fullScreen(Colors.BLACK_ALPHA75);
        enemyShipsStage.addActor(loadingOverlay);
        postLoadSequence.addAction(Actions.removeActor(loadingOverlay));
    }

    private void addStageTitleSequence(SequenceAction postLoadSequence) {

        var titleCardStage = entity.addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
        var sceneNameText = new FlyInCenterText(new String[]{entity.getClass().getSimpleName(), sceneScript.getSceneName()});
        sceneNameText.setAnimateOnStage(titleCardStage);
        postLoadSequence.addAction(Actions.run(() -> entity.removeAndGetStage(titleCardStage)));
    }

    private void postShipsReadyDescentEvent() {
        EventsHandling.postEvent(GameEventType.SHIPS_AT_START.makeEvent());
    }

    private Pair<Vector2, EnemyShip> placement2ShipWithPosition(SceneScript.ShipPlacement placement) {
        return ImmutablePair.of(
                placement.getPosition().toVector2(),
                new EnemyShip(
                        placement.getShipId(),
                        placement.getText(),
                        ShipRenderSpecs.api.getRenderSpec(placement.getShipSpecId())
                )
        );
    }

    private DelayAction setupShipsFlyToStartActions(Map<EnemyShip, Vector2> shipDesiredPositions) {
        return shipDesiredPositions.entrySet().stream()
                .reduce(Actions.delay(0.5f), (prevDelay, shipPositionPair) -> {
                    var ship = shipPositionPair.getKey();
                    var startPosition = shipPositionPair.getValue();
                    var moveDuration = 1f;
                    var moveToStartAction = Actions.moveTo(
                            startPosition.x, startPosition.y,
                            moveDuration,
                            Interpolation.sine
                    );
                    var shipActions = Actions.sequence(
                            Actions.delay(prevDelay.getDuration()),
                            moveToStartAction
                    );
                    ship.addAction(shipActions);

                    return Actions.delay(prevDelay.getDuration() + moveDuration);
                }, (delay1, delay2) -> Actions.delay(delay1.getDuration() + delay2.getDuration()));
    }
}
