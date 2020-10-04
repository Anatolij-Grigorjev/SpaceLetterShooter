package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.utils.Disposable;
import com.tiem625.space_letter_shooter.resource.ResourcesManager;

import java.util.Objects;
import java.util.function.Supplier;

abstract class ResourceMaker {

    protected static <T extends Disposable> T makeResource(Supplier<T> instancer) {
        Objects.requireNonNull(instancer);
        var instance = instancer.get();
        ResourcesManager.INSTANCE.addResource(instance);

        return instance;
    }
}
