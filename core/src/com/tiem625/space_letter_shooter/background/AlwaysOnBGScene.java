package com.tiem625.space_letter_shooter.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.resource.Textures;
import com.tiem625.space_letter_shooter.resource.make.ParticleEffectMaker;
import com.tiem625.space_letter_shooter.scene.Scene;

public class AlwaysOnBGScene extends Scene {

    private final ParticleEffect bgStarsParticles;
    private final Stage particlesStage;

    public AlwaysOnBGScene() {
        super("space_bg");
        bgStarsParticles = buildBgStarsParticles();
        particlesStage = addAndGetStage(new Stage());
        particlesStage.addActor(new ParticlesDrawActor(bgStarsParticles));
    }

    private ParticleEffect buildBgStarsParticles() {
        final ParticleEffect bgStarsParticles;
        bgStarsParticles = ParticleEffectMaker.buildDefault();
        bgStarsParticles.load(Gdx.files.internal("particles/space/space_particles.p"), Textures.getAtlas());
        bgStarsParticles.start();
        bgStarsParticles.setPosition(0, GamePropsHolder.props.getResolutionHeight());
        return bgStarsParticles;
    }

    private static class ParticlesDrawActor extends Actor {

        private final ParticleEffect particles;

        public ParticlesDrawActor(ParticleEffect particles) {
            this.particles = particles;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (GamePropsHolder.props.isEnabledDynamicBg()) {
                particles.draw(batch, Gdx.graphics.getDeltaTime());
            }
        }
    }
}
