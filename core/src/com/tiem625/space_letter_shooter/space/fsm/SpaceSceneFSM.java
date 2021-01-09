package com.tiem625.space_letter_shooter.space.fsm;

import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.util.fsm.State;
import com.tiem625.space_letter_shooter.util.fsm.StateMachine;

import java.util.Set;

public class SpaceSceneFSM extends StateMachine<SpaceScene> {

    private boolean shipReachedScreenBottom = false;
    private boolean shipsReadyDescent = false;

    public SpaceSceneFSM(SpaceScene entity) {
        super(entity);
        setState(LoadSpaceSceneState.KEY);
        setupEventHandlers();
    }

    @Override
    protected Set<? extends State<SpaceScene>> loadStates() {
        return Set.of(
                new LoadSpaceSceneState("SS1"),
                new RunSpaceSceneState("SS1"),
                new GameOverSpaceSceneState()
        );
    }

    @Override
    protected String computeNextStateKey(float delta) {

        switch (currentStateKey) {
            case LoadSpaceSceneState.KEY:
                if (shipsReadyDescent) {
                    return RunSpaceSceneState.KEY;
                }
                return null;
            case RunSpaceSceneState.KEY:
                if (shipReachedScreenBottom) {
                    return GameOverSpaceSceneState.KEY;
                }
                return null;
            case GameOverSpaceSceneState.KEY:
                return null;
            default:
                return super.computeNextStateKey(delta);
        }
    }

    private void setupEventHandlers() {
        EventsHandling.addEventHandler(GameEventType.SHIP_REACH_BOTTOM_SCREEN, gameEvent -> {
            shipReachedScreenBottom = true;
        });
        EventsHandling.addEventHandler(GameEventType.SHIPS_AT_START, gameEvent -> {
            shipsReadyDescent = true;
        });
    }
}
