package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.tiem625.space_letter_shooter.resource.ResourcesManager;
import com.tiem625.space_letter_shooter.resource.make.ParticleEffectMaker;
import com.tiem625.space_letter_shooter.resource.make.SpriteBatchMaker;
import com.tiem625.space_letter_shooter.resource.make.TextureMaker;

public class GameLoop extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ParticleEffect effect;
	
	@Override
	public void create () {
		batch = SpriteBatchMaker.buildDefault();
		img = TextureMaker.buildForInternalPath("badlogic.jpg");
		effect = ParticleEffectMaker.buildDefault();

		effect.load(
				Gdx.files.internal("particles/space/space_particles.p"),
				new TextureAtlas(Gdx.files.internal("atlas/pack.atlas"))
		);
		effect.start();
		effect.setPosition(0, 480);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		effect.draw(batch, Gdx.graphics.getDeltaTime());
		batch.end();
	}
	
	@Override
	public void dispose () {
		ResourcesManager.INSTANCE.disposeAll();
	}
}
