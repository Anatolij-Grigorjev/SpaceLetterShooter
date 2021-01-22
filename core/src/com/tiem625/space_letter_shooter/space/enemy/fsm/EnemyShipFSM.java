package com.tiem625.space_letter_shooter.space.enemy.fsm;

import com.tiem625.space_letter_shooter.space.enemy.EnemyShip;
import com.tiem625.space_letter_shooter.util.fsm.State;
import com.tiem625.space_letter_shooter.util.fsm.StateMachine;

import java.util.Set;

public class EnemyShipFSM extends StateMachine<EnemyShip> {

    public EnemyShipFSM(EnemyShip entity) {
        super(entity);
        setState(AppearingShipState.KEY);
    }

    @Override
    protected Set<? extends State<EnemyShip>> loadStates() {
        return Set.of(
                new AppearingShipState(),
                new DescendShipState(),
                new ExplodingShipState()
        );
    }

    @Override
    protected String computeNextStateKey(float delta) {

        switch (currentStateKey) {

            default:
                return super.computeNextStateKey(delta);
        }
    }
}
