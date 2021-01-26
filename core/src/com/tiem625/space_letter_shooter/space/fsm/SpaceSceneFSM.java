package com.tiem625.space_letter_shooter.space.fsm;

import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.space.SceneScripts;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.enemy.EnemyShip;
import com.tiem625.space_letter_shooter.space.spec.SceneScript;
import com.tiem625.space_letter_shooter.util.OneUseVal;
import com.tiem625.space_letter_shooter.util.fsm.State;
import com.tiem625.space_letter_shooter.util.fsm.StateMachine;

import java.util.Set;

public class SpaceSceneFSM extends StateMachine<SpaceScene> {

    private final OneUseVal<Boolean> shipReachedScreenBottomVal = OneUseVal.withDefault(false);
    private final OneUseVal<Boolean> shipsReadyDescentVal = OneUseVal.withDefault(false);
    private final OneUseVal<String> nextSceneIdVal = OneUseVal.withDefault(null);

    public SpaceSceneFSM(SpaceScene entity) {
        super(entity);
        setLoadSceneSpec("SS1");
        setState(LoadSpaceSceneScriptState.KEY);
        setupEventHandlers();
    }

    private void setLoadSceneSpec(String sceneId) {
        var loadSceneState = (LoadSpaceSceneScriptState) getState(LoadSpaceSceneScriptState.KEY);
        loadSceneState.setSceneScript(sceneId);
    }

    private void setRunLoadedSceneSpec() {
        var loadSceneState = (LoadSpaceSceneScriptState) getCurrentState();
        var nextState = (RunSpaceSceneScriptState) getState(RunSpaceSceneScriptState.KEY);
        nextState.setSceneScript(loadSceneState.getLoadedSceneScriptId());
    }

    @Override
    protected Set<? extends State<SpaceScene>> loadStates() {
        return Set.of(
                new LoadSpaceSceneScriptState(),
                new RunSpaceSceneScriptState(),
                new GameOverSpaceSceneState()
        );
    }

    @Override
    protected String computeNextStateKey(float delta) {

        switch (currentStateKey) {
            case LoadSpaceSceneScriptState.KEY:
                if (shipsReadyDescentVal.get()) {
                    setRunLoadedSceneSpec();
                    return RunSpaceSceneScriptState.KEY;
                }
                return null;
            case RunSpaceSceneScriptState.KEY:
                if (shipReachedScreenBottomVal.get()) {
                    return GameOverSpaceSceneState.KEY;
                }
                var nextSceneId = nextSceneIdVal.get();
                if (nextSceneId != null) {
                    setLoadSceneSpec(nextSceneId);
                    return LoadSpaceSceneScriptState.KEY;
                }
                return null;
            case GameOverSpaceSceneState.KEY:
                return null;
            default:
                return super.computeNextStateKey(delta);
        }
    }

    private void setupEventHandlers() {
        EventsHandling.addEventHandler(GameEventType.SCRIPT_CLEAR, gameEvent -> {
            var clearedScript = (SceneScript) gameEvent.payload.get("script");
            SceneScript nextScript = SceneScripts.api.getNextSceneSpecAfter(clearedScript.getScriptId());
            nextSceneIdVal.set(nextScript.getScriptId());
        });
        EventsHandling.addEventHandler(GameEventType.SHIP_REACH_BOTTOM_SCREEN, gameEvent -> {
            var ship = (EnemyShip) gameEvent.payload.get("ship");

            //only process events from non-disposing ships
            if (!ship.isShipDisposing()) {
                shipReachedScreenBottomVal.set(true);
            }
        });
        EventsHandling.addEventHandler(GameEventType.SHIPS_AT_START, gameEvent -> {
            shipsReadyDescentVal.set(true);
        });
    }
}
