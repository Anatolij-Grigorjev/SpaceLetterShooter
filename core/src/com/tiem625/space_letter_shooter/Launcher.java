package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Launcher extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ParticleEffect effect;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		effect = new ParticleEffect();
		effect.load(
				Gdx.files.internal("particles/space/space_particles.p"),
				new TextureAtlas(Gdx.files.internal("particles/space/atlas/pack.atlas"))
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
		batch.dispose();
		effect.dispose();
		img.dispose();
	}
}
