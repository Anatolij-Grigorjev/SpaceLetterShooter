package com.tiem625.space_letter_shooter.space.ship.fsm;

import com.tiem625.space_letter_shooter.space.ship.EnemyShip;
import com.tiem625.space_letter_shooter.util.fsm.State;

public class ExplodingShipState extends State<EnemyShip> {

    public static final String KEY = "EXPLODE_SHIP";

    public ExplodingShipState() {
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
