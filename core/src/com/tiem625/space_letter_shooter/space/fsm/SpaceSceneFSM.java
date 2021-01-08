package com.tiem625.space_letter_shooter.space.fsm;

import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.util.fsm.State;
import com.tiem625.space_letter_shooter.util.fsm.StateMachine;

import java.util.Set;

public class SpaceSceneFSM extends StateMachine<SpaceScene> {

    public SpaceSceneFSM(SpaceScene entity) {
        super(entity);
        setState(LoadSpaceSceneState.KEY);
    }

    @Override
    protected Set<? extends State<SpaceScene>> loadStates() {
        return Set.of(
                new LoadSpaceSceneState("SS1"),
                new RunSpaceSceneState(),
                new GameOverSpaceSceneState()
        );
    }
}
