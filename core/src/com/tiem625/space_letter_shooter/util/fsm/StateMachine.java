package com.tiem625.space_letter_shooter.util.fsm;

import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEvent;
import com.tiem625.space_letter_shooter.events.GameEventType;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public abstract class StateMachine<E> {

    protected static final String KEY_NO_STATE = "<NO_STATE>";

    protected final E entity;
    protected final Map<String, ? extends State<E>> statesIndex;

    protected String currentStateKey;

    public StateMachine(E entity) {
        this.entity = entity;
        this.currentStateKey = KEY_NO_STATE;
        this.statesIndex = Optional.ofNullable(loadStates()).orElse(Set.of())
                .stream()
                .peek(state -> {
                    state.setFsm(this);
                    state.setEntity(this.entity);
                })
                .collect(Collectors.toMap(State::getKey, identity()));
    }

    public State<E> getCurrentState() {
        return getState(currentStateKey);
    }

    public State<E> getState(String stateKey) {
        return Optional.ofNullable(statesIndex.get(stateKey))
                .orElseThrow(() -> new RuntimeException("No state found for key " + stateKey));
    }

    public final void setState(String nextStateKey) {
        var prevStateKey = currentStateKey;
        logStateChange(prevStateKey, nextStateKey);
        EventsHandling.postEvent(buildStateChangedEvent(prevStateKey, nextStateKey));
        currentStateKey = nextStateKey;

        if (!KEY_NO_STATE.equals(prevStateKey)) {
            exitState(prevStateKey, nextStateKey);
        }

        if (!KEY_NO_STATE.equals(nextStateKey)) {
            enterState(nextStateKey, prevStateKey);
        }
    }

    public final void act(float delta) {
        if (KEY_NO_STATE.equals(currentStateKey)) return;

        actState(delta);

        Optional.ofNullable(computeNextStateKey(delta))
                .ifPresent(this::setState);
    }

    protected abstract Set<? extends State<E>> loadStates();

    protected String computeNextStateKey(float delta) {
        return KEY_NO_STATE;
    };

    private void actState(float delta) {
        getState(currentStateKey).act(delta);
    }

    private void enterState(String nextStateKey, String prevStateKey) {
        getState(nextStateKey).enterState(prevStateKey);
    }

    private void exitState(String prevStateKey, String nextStateKey) {
        getState(prevStateKey).exitState(nextStateKey);
    }

    private GameEvent buildStateChangedEvent(String from, String to) {
        return new GameEvent(
                GameEventType.FSM_STATE_CHANGE,
                Map.of(
                        "fsm", this,
                        "from_state", from,
                        "to_state", to
                )
        );
    }

    private void logStateChange(String prevStateKey, String nextStateKey) {
        System.out.println(getClass().getSimpleName() + ": " + prevStateKey + " -> " + nextStateKey);
    }
}
