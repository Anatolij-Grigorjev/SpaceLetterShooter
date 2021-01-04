package com.tiem625.space_letter_shooter.util.fsm;

public abstract class State<E> {

    protected E entity;
    protected StateMachine<E> fsm;
    protected final String key;

    public State(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public void setFsm(StateMachine<E> fsm) {
        this.fsm = fsm;
    }

    public abstract void act(float delta);

    public abstract void exitState(String nextStateKey);

    public abstract void enterState(String prevStateKey);
}
