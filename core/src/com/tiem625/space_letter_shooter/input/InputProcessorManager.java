package com.tiem625.space_letter_shooter.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.util.*;
import java.util.stream.Stream;

public class InputProcessorManager {

    private static final Set<InputProcessor> alwaysOnInputProcessors = new HashSet<>();

    private InputProcessorManager() {
        throw new ClassIsStaticException(getClass());
    }

    /**
     * Add an always-on {@link InputProcessor} for capturing Gdx input events regardless of what other
     * input processors change.
     * <br/>
     * This method appends the set processors to the current {@link InputMultiplexer} registered with Gdx or
     * makes a new one if none was found.
     * 
     * @param alwaysOnProcessor a processor to be added into the set of always-on processors that will always
     *                          multiplex all Gdx input
     */
    public static void addAlwaysOnInputProcessor(InputProcessor alwaysOnProcessor) {
        Objects.requireNonNull(alwaysOnProcessor, "Always-on processors must be present!");
        alwaysOnInputProcessors.add(alwaysOnProcessor);
        getCurrentOrSetNewMultiplexer().addProcessor(alwaysOnProcessor);
    }

    /**
     * Use provided {@link InputProcessor} array in an {@link InputMultiplexer} to consume GDX received input.
     * <br/>
     * The method also combines always-on processors (added via {@link #addAlwaysOnInputProcessor(InputProcessor)})
     * into the multiplexer, so invoking this without arguments is equivalent to hooking them up.
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

    public static void appendNewInputProcessors(InputProcessor... inputProcessors) {
        var currentProcessor = getCurrentOrSetNewMultiplexer();
        Stream.of(inputProcessors).forEach(currentProcessor::addProcessor);
    }

    private static InputProcessor[] flatMapInputProcessors(Collection<InputProcessor>... inputProcessorsSets) {
        return Stream.of(inputProcessorsSets)
                .flatMap(Collection::stream)
                .toArray(InputProcessor[]::new);
    }

    private static InputProcessor[] combineNewAndAlwaysOn(InputProcessor... newProcessors) {
        return flatMapInputProcessors(alwaysOnInputProcessors, List.of(newProcessors));
    }

    private static InputMultiplexer getCurrentOrSetNewMultiplexer() {
        return (InputMultiplexer) Optional.ofNullable(Gdx.input.getInputProcessor())
                .orElseGet(() -> {
                    var multiplexer = new InputMultiplexer();
                    Gdx.input.setInputProcessor(multiplexer);
                    return multiplexer;
                });
    }
}
