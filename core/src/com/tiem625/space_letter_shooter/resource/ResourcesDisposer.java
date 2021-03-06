package com.tiem625.space_letter_shooter.resource;

import com.badlogic.gdx.utils.Disposable;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum ResourcesDisposer {

    api;

    private final Set<Disposable> resources;

    private final String DISPOSING_OF_RESOURCE_MESSAGE_FORMAT = "Disposing '%s' of type [%s]\n";

    ResourcesDisposer() {
        resources = ConcurrentHashMap.newKeySet();
    }

    public <T extends Disposable> void addResource(T resource) {
        Objects.requireNonNull(resource);
        resources.add(resource);
    }

    public <T extends Disposable> boolean disposeOne(T resource) {
        if (resources.contains(resource)) {
            resources.remove(resource);
            resource.dispose();
            return true;
        } else return false;
    }

    public void disposeAll() {
        resources.forEach(resource -> {
            System.out.printf(DISPOSING_OF_RESOURCE_MESSAGE_FORMAT, resource, resource.getClass());
            resource.dispose();
        });
    }
}
