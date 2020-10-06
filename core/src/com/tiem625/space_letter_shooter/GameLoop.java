package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.tiem625.space_letter_shooter.config.RuntimeConfig;
import com.tiem625.space_letter_shooter.resource.ResourcesManager;
import com.tiem625.space_letter_shooter.resource.make.ParticleEffectMaker;
import com.tiem625.space_letter_shooter.resource.make.SpriteBatchMaker;

public class GameLoop extends ApplicationAdapter {
    SpriteBatch batch;
    ParticleEffect effect;

    @Override
    public void create() {
        batch = SpriteBatchMaker.buildDefault();
        effect = ParticleEffectMaker.buildDefault();
        effect.load(
                Gdx.files.internal("particles/space/space_particles.p"),
                new TextureAtlas(Gdx.files.internal("atlas/pack.atlas"))
        );
        effect.start();
        effect.setPosition(0, RuntimeConfig.getScreenY());
        Gdx.graphics.setTitle(RuntimeConfig.getGameTitle());
        Gdx.graphics.setWindowedMode(RuntimeConfig.getScreenX(), RuntimeConfig.getScreenY());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        effect.draw(batch, Gdx.graphics.getDeltaTime());
        batch.end();
    }

    @Override
    public void dispose() {
        ResourcesManager.INSTANCE.disposeAll();
    }
}
