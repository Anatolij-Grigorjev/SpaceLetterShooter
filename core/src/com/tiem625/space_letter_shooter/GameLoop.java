package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
import java.util.function.BinaryOperator;

import static com.tiem625.space_letter_shooter.util.MathUtils.RNG;

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
        var shipsXMargin = 100.0f;
        var shipsXPadding = 150.0f;
        var shipsSpecs = loadShipSpecs();
        placeRowOfShips(shipsSpecs, shipsXMargin, shipsXPadding);

        spaceScene.enemyShips()
                .sorted((ship1, ship2) -> (int)(ship1.getX() - ship2.getX()))
                .reduce(Actions.delay(500), (prevDelay, nextShip) -> {

                    var moveToStartAction = Actions.moveTo(
                            nextShip.getX(),
                            nextShip.getY(),
                            500,
                            Interpolation.sine
                    );
                    var shipActions = Actions.sequence(
                            prevDelay,
                            moveToStartAction
                    );
                    nextShip.moveBy(-500, -50);
                    nextShip.addAction(shipActions);

                    return Actions.delay(prevDelay.getDuration() + 500);
                }, (delay1, delay2) -> Actions.delay(delay1.getDuration() + delay2.getDuration()));

        setCurrentSceneAsInput();
    }

    private void placeRowOfShips(List<ShipRenderSpec> shipsSpecs, float shipsXMargin, float shipsXPadding) {
        shipsSpecs.stream()
                .map(spec -> new EnemyShip("test " + System.currentTimeMillis(), spec))
                .map(spaceScene::addEnemyShip)
                .reduce(shipsXMargin, (prevOffset, nextShip) -> {
                    var shipSize = nextShip.getShipSize();
                    var randomY = GamePropsHolder.props.getResolutionHeight() - (shipSize.y + RNG.nextFloat() * shipSize.y);
                    nextShip.setPosition(prevOffset, randomY);
                    return shipSize.x + shipsXPadding + prevOffset;
                }, BinaryOperator.maxBy(Float::compare));
    }

    private List<ShipRenderSpec> loadShipSpecs() {

        try {
            return new ObjectMapper().readValue(Gdx.files.internal("enemy_ships.json").read(), new TypeReference<List<ShipRenderSpec>>() {});
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void setCurrentSceneAsInput() {
        InputProcessorManager
                .setCurrentInputProcessors(
                        ScenesManager.INSTANCE.getCurrentScene()
                                .stages()
                                .toArray(Stage[]::new));
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
