package com.tiem625.space_letter_shooter.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import java.util.*;

public class InputProcessorManager {

    private static final Set<InputProcessor> alwaysOnInputProcessors = new HashSet<>();

    private InputProcessorManager() {
        throw new RuntimeException("InputProcessorManager is static!");
    }

    /**
     * Add an always-on {@link InputProcessor} for capturing Gdx input events regardless of what other
     * input processors change.
     *
     * This method only registers the always-on candidate, to enable processing using this {@link InputProcessor},
     * {@link #setCurrentInputProcessors(InputProcessor...)} must be called.
     * 
     * @param alwaysOnProcessor a processor to be added into the set of always-on processors that will always
     *                          multiplex all Gdx input
     */
    public static void addAlwaysOnInputProcessor(InputProcessor alwaysOnProcessor) {
        Objects.requireNonNull(alwaysOnProcessor, "Always-on processors must be present!");
        alwaysOnInputProcessors.add(alwaysOnProcessor);
    }

    /**
     * Use provided {@link InputProcessor} array in an {@link InputMultiplexer} to consume GDX received input.
     * <br/>
     * The method also combines always-on processors (added via {@link #addAlwaysOnInputProcessor(InputProcessor)})
     * into the multiplexed, so invoking this without arguments is equivalent to hooking them up.
     * <br/>
     * If No processors are supplied here and no always-on have been registered, the Gdx-presented processor
     * is <code>null</code>.
     * @param inputProcessors array of current input processors. Gets combines with always-on processors.
     */
    public static void setCurrentInputProcessors(InputProcessor... inputProcessors) {

        var allProcessors = combineNewAndAlwaysOn(inputProcessors);
        System.out.println("Setting Gdx to use "+ allProcessors.length + " input processor(-s)!");
        InputProcessor currentInputProcessor;
        if (allProcessors.length > 0) {
            currentInputProcessor = new InputMultiplexer(allProcessors);
        } else {
            currentInputProcessor = null;
        }

        Gdx.input.setInputProcessor(currentInputProcessor);
    }

    private static InputProcessor[] combineNewAndAlwaysOn(InputProcessor... newProcessors) {
        var allProcessorsList = new ArrayList<InputProcessor>(alwaysOnInputProcessors);
        allProcessorsList.addAll(List.of(newProcessors));

        return allProcessorsList.toArray(new InputProcessor[0]);
    }
}
