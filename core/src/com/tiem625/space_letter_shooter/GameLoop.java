package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.input.InputProcessorManager;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.resource.ResourcesDisposer;
import com.tiem625.space_letter_shooter.resource.Textures;
import com.tiem625.space_letter_shooter.resource.make.BitmapFontMaker;
import com.tiem625.space_letter_shooter.resource.make.ParticleEffectMaker;
import com.tiem625.space_letter_shooter.resource.make.SceneMaker;
import com.tiem625.space_letter_shooter.resource.make.SpriteBatchMaker;
import com.tiem625.space_letter_shooter.scene.ScenesManager;
import com.tiem625.space_letter_shooter.space.EnemyShip;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.util.Point;

public class GameLoop extends ApplicationAdapter {
    SpriteBatch batch;
    ParticleEffect effect;
    SpaceScene spaceScene;


    @Override
    public void create() {
        Fonts.ENEMY_TEXT_NORMAL_FONT = BitmapFontMaker.buildEnemyShipNormalFont();
        Fonts.MAIN_UI_FONT = BitmapFontMaker.buildMain();

        spaceScene = SceneMaker.buildSpaceScene();
        ScenesManager.INSTANCE.setCurrentScene(spaceScene);
        batch = SpriteBatchMaker.buildDefault();
        effect = ParticleEffectMaker.buildDefault();
        effect.load(Gdx.files.internal("particles/space/space_particles.p"), Textures.getAtlas());
        effect.start();
        effect.setPosition(0, GamePropsHolder.props.getResolutionHeight());
        GamePropsHolder.applyCurrentGameConfig();
        var enemyShip = spaceScene.addEnemyShip(new EnemyShip(
                "nestle",
                "enemy_ship_1w",
                new Point(-20, -30)
                ));
        enemyShip.setPosition(100, GamePropsHolder.props.getResolutionHeight() - 100);
        InputProcessorManager.setCurrentInputProcessors(ScenesManager.INSTANCE.getCurrentScene().getFirstStage());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (GamePropsHolder.props.isEnabledDynamicBg()) {
            effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
        ScenesManager.INSTANCE.renderActiveScenes();
        batch.end();
    }

    @Override
    public void dispose() {
        ResourcesDisposer.INSTANCE.disposeAll();
        GamePropsHolder.writeOutConfig();
    }
}
