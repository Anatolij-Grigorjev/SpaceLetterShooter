package com.tiem625.space_letter_shooter.space.fsm;

import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.space.SceneConfigureSpecs;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;
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
        setState(LoadSpaceSceneState.KEY);
        setupEventHandlers();
    }

    private void setLoadSceneSpec(String sceneId) {
        var loadSceneState = (LoadSpaceSceneState) getState(LoadSpaceSceneState.KEY);
        loadSceneState.setSceneSpec(sceneId);
    }

    private void setRunLoadedSceneSpec() {
        var loadSceneState = (LoadSpaceSceneState) getCurrentState();
        var nextState = (RunSpaceSceneState) getState(RunSpaceSceneState.KEY);
        nextState.setSceneSpec(loadSceneState.getLoadedSpecId());
    }

    @Override
    protected Set<? extends State<SpaceScene>> loadStates() {
        return Set.of(
                new LoadSpaceSceneState(),
                new RunSpaceSceneState(),
                new GameOverSpaceSceneState()
        );
    }

    @Override
    protected String computeNextStateKey(float delta) {

        switch (currentStateKey) {
            case LoadSpaceSceneState.KEY:
                if (shipsReadyDescentVal.get()) {
                    setRunLoadedSceneSpec();
                    return RunSpaceSceneState.KEY;
                }
                return null;
            case RunSpaceSceneState.KEY:
                if (shipReachedScreenBottomVal.get()) {
                    return GameOverSpaceSceneState.KEY;
                }
                var nextSceneId = nextSceneIdVal.get();
                if (nextSceneId != null) {
                    setLoadSceneSpec(nextSceneId);
                    return LoadSpaceSceneState.KEY;
                }
                return null;
            case GameOverSpaceSceneState.KEY:
                return null;
            default:
                return super.computeNextStateKey(delta);
        }
    }

    private void setupEventHandlers() {
        EventsHandling.addEventHandler(GameEventType.SCENE_CLEAR, gameEvent -> {
            var clearedScene = (Scene) gameEvent.payload.get("scene");
            SceneConfigureSpec nextSceneSpec = SceneConfigureSpecs.api.getNextSceneSpecAfter(clearedScene.getSceneId());
            nextSceneIdVal.set(nextSceneSpec.getSceneId());
        });
        EventsHandling.addEventHandler(GameEventType.SHIP_REACH_BOTTOM_SCREEN, gameEvent -> {
            shipReachedScreenBottomVal.set(true);
        });
        EventsHandling.addEventHandler(GameEventType.SHIPS_AT_START, gameEvent -> {
            shipsReadyDescentVal.set(true);
        });
    }
}
