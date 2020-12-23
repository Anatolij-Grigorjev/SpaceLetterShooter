package com.tiem625.space_letter_shooter.events;

import com.tiem625.space_letter_shooter.scene.SceneId;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventsHandling {

    private EventsHandling() {
        throw new ClassIsStaticException(getClass());
    }

    private static final Map<SceneId, Set<Consumer<GameEvent>>> handlers = new ConcurrentHashMap<>();

    public static void addEventHandler(SceneId address, Consumer<GameEvent> handler) {
        handlers.putIfAbsent(address, new HashSet<>());
        handlers.get(address).add(handler);
    }

    public static void postEvent(GameEvent event) {
        if (handlers.containsKey(event.destination)) {
            handlers.get(event.destination).forEach(handler -> handler.accept(event));
        }
    }
}
