package com.tiem625.space_letter_shooter.resource.make;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleEffectMaker extends ResourceMaker {

    public static ParticleEffect buildDefault() {
        return makeResource(ParticleEffect::new);
    }
}
