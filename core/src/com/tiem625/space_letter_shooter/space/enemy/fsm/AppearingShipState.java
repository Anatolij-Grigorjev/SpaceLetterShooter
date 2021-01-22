package com.tiem625.space_letter_shooter.space.enemy.fsm;

import com.tiem625.space_letter_shooter.space.enemy.EnemyShip;
import com.tiem625.space_letter_shooter.util.fsm.State;

public class AppearingShipState extends State<EnemyShip> {

    public static final String KEY = "APPEAR_SHIP";

    public AppearingShipState() {
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
