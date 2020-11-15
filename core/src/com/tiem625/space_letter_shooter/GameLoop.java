package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.config.RuntimeConfig;
import com.tiem625.space_letter_shooter.resource.ResourcesManager;
import com.tiem625.space_letter_shooter.resource.make.ParticleEffectMaker;
import com.tiem625.space_letter_shooter.resource.make.SpriteBatchMaker;
import com.tiem625.space_letter_shooter.resource.make.StageMaker;
import com.tiem625.space_letter_shooter.resource.make.TextureAtlasMaker;

public class GameLoop extends ApplicationAdapter {
    SpriteBatch batch;
    ParticleEffect effect;
    TextureAtlas textures;
    Stage spaceStage;


    @Override
    public void create() {
        spaceStage = StageMaker.buildDefault();
        batch = SpriteBatchMaker.buildDefault();
        textures = TextureAtlasMaker.buildForInternalPackFile("atlas/pack.atlas");
        effect = ParticleEffectMaker.buildDefault();
        effect.load(
                Gdx.files.internal("particles/space/space_particles.p"),
                textures
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
        if (RuntimeConfig.isDynamicBackgroundEnabled()) {
            effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
        batch.end();
    }

    @Override
    public void dispose() {
        ResourcesManager.INSTANCE.disposeAll();
        RuntimeConfig.writeOutConfig();
    }
}
