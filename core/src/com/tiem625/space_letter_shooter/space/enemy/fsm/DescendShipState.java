package com.tiem625.space_letter_shooter.space.enemy.fsm;

import com.tiem625.space_letter_shooter.space.enemy.EnemyShip;
import com.tiem625.space_letter_shooter.util.fsm.State;

public class DescendShipState extends State<EnemyShip> {

    public static final String KEY = "DESCEND_SHIP";

    public DescendShipState() {
        super(KEY);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void exitState(String nextStateKey) {
        super.exitState(nextStateKey);
    }

    @Override
    public void enterState(String prevStateKey) {
        super.enterState(prevStateKey);
    }

}
