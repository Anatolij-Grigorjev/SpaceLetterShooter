package com.tiem625.space_letter_shooter.events;

import com.tiem625.space_letter_shooter.scene.SceneId;
import com.tiem625.space_letter_shooter.util.ClassIsStaticException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventsHandling {

    private EventsHandling() {
        throw new ClassIsStaticException(getClass());
    }

    private static final Map<EventHandlerCoordinate, Consumer<GameEvent>> handlers = new ConcurrentHashMap<>();

    public static void addEventHandler(SceneId handlingScene, GameEventType handlesEventType, Consumer<GameEvent> handler) {
        var coordinate = new EventHandlerCoordinate(handlingScene, handlesEventType);
        handlers.put(coordinate, handler);
    }

    public static void postEvent(GameEvent event) {
        System.out.println("Posted event '" + event.type + "' for destination " + event.destination);
        var handlerCoordinate = new EventHandlerCoordinate(event.destination, event.type);
        if (handlers.containsKey(handlerCoordinate)) {
            handlers.get(handlerCoordinate).accept(event);
        }
    }

    private static class EventHandlerCoordinate {
        public final SceneId sceneId;
        public final GameEventType eventType;

        public EventHandlerCoordinate(SceneId sceneId, GameEventType eventType) {
            this.sceneId = sceneId;
            this.eventType = eventType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EventHandlerCoordinate that = (EventHandlerCoordinate) o;
            return sceneId == that.sceneId &&
                    eventType == that.eventType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sceneId, eventType);
        }
    }
}
