package com.tiem625.space_letter_shooter.util.fsm;

public abstract class State<E> {

    protected E entity;
    protected StateMachine<E> fsm;
    protected final String key;

    public State(String key) {
        this.key = key;
    }

    public final String getKey() {
        return key;
    }

    public final E getEntity() {
        return entity;
    }

    public final void setEntity(E entity) {
        this.entity = entity;
    }

    public final void setFsm(StateMachine<E> fsm) {
        this.fsm = fsm;
    }

    public abstract void act(float delta);

    public void exitState(String nextStateKey) {};

    public void enterState(String prevStateKey) {};
}
