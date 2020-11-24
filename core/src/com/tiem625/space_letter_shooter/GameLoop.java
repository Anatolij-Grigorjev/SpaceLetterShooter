package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.config.ConfigHolder;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;
import com.tiem625.space_letter_shooter.resource.ResourcesManager;
import com.tiem625.space_letter_shooter.resource.make.*;
import com.tiem625.space_letter_shooter.stages.StageId;
import com.tiem625.space_letter_shooter.stages.StagesManager;

public class GameLoop extends ApplicationAdapter {
    SpriteBatch batch;
    ParticleEffect effect;
    TextureAtlas textures;
    Stage spaceStage;
    BitmapFont bitmapFont;


    @Override
    public void create() {
        spaceStage = StageMaker.buildWithId(StageId.SPACE);
        batch = SpriteBatchMaker.buildDefault();
        textures = TextureAtlasMaker.buildForInternalPackFile("atlas/pack.atlas");
        effect = ParticleEffectMaker.buildDefault();
        bitmapFont = BitmapFontMaker.buildDefault();
        effect.load(Gdx.files.internal("particles/space/space_particles.p"), textures);
        effect.start();
        effect.setPosition(0, ConfigHolder.config.getResolutionHeight());
        ConfigHolder.applyCurrentGameConfig();
        InputProcessorManager.setCurrentInputProcessors();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (ConfigHolder.config.isEnabledDynamicBg()) {
            effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
        StagesManager.INSTANCE.renderCurrentStages();
        bitmapFont.draw(batch, "nestle", 100, 400);
        batch.end();
    }

    @Override
    public void dispose() {
        ResourcesManager.INSTANCE.disposeAll();
        ConfigHolder.writeOutConfig();
    }
}
