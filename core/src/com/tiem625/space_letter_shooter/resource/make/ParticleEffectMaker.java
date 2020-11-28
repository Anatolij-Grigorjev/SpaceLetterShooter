package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.tiem625.space_letter_shooter.resource.ResourceMaker;

public class ParticleEffectMaker extends ResourceMaker {

    public static ParticleEffect buildDefault() {
        return makeResource(ParticleEffect::new);
    }
}
