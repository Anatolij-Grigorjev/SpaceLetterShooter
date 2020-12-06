package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.utils.Disposable;
import com.tiem625.space_letter_shooter.resource.ResourcesDisposer;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class ResourceMaker {

    /**
     * Create a {@link Disposable} resource in the way described by the provided {@link Supplier}.<br/>
     * When a resource is created using this helper, its lifecycle is handled using the game's
     * {@link ResourcesDisposer}, making it disposed of correctly at end of game session
     * @param instancer The instructions on how to construct an instance of the required resource. Not <code>null</code>
     * @param <T> the type of resource, inherently {@link Disposable}
     * @return the newly constructed, resource-managed instance
     */
    protected static <T extends Disposable> T makeResource(Supplier<T> instancer) {
        Objects.requireNonNull(instancer);
        var instance = instancer.get();
        ResourcesDisposer.INSTANCE.addResource(instance);

        return instance;
    }
}
