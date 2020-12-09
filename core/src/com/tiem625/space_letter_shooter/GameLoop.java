package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.tiem625.space_letter_shooter.space.dto.ShipRenderSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameLoop extends ApplicationAdapter {
    Random RNG = new Random();
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
        var shipsXMargin = 100.0f;
        var shipsXPadding = 50.0f;
        var finalXOffset = loadShipSpecs().stream()
                .map(spec -> new EnemyShip("test" + System.currentTimeMillis(), spec))
                .map(spaceScene::addEnemyShip)
                .reduce(shipsXMargin, (prevOffset, nextShip) -> {
                    var shipSize = nextShip.getShipSize();
                    var randomY = shipSize.y + RNG.nextFloat() * (GamePropsHolder.props.getResolutionHeight() / 2 - shipSize.y);
                    nextShip.setPosition(prevOffset, randomY);

                    return shipSize.x + shipsXPadding;
                }, Float::sum);
        InputProcessorManager.setCurrentInputProcessors(ScenesManager.INSTANCE.getCurrentScene().getFirstStage());
    }

    private List<ShipRenderSpec> loadShipSpecs() {

        try {
            return new ObjectMapper().readValue(Gdx.files.internal("enemy_ships.json").read(), new TypeReference<List<ShipRenderSpec>>() {});
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
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
