package com.tiem625.space_letter_shooter.events;

import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventsHandling {

    private EventsHandling() {
        throw new ClassIsStaticException(getClass());
    }

    private static final Map<GameEventType, Consumer<GameEvent>> handlers = new ConcurrentHashMap<>();

    public static void addEventHandler(GameEventType forEventType, Consumer<GameEvent> handler) {
        handlers.put(forEventType, handler);
    }

    public static void postEvent(GameEvent event) {
        System.out.println("Posted event '" + event.type + "' with " + event.payload.size() + " parameter(-s)...");
        if (handlers.containsKey(event.type)) {
            handlers.get(event.type).accept(event);
        }
    }
}
