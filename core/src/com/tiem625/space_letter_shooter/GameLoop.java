package com.tiem625.space_letter_shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
import com.tiem625.space_letter_shooter.resource.make.BitmapFontMaker;
import com.tiem625.space_letter_shooter.resource.make.SceneMaker;
import com.tiem625.space_letter_shooter.resource.make.SpriteBatchMaker;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.scene.ScenesManager;
import com.tiem625.space_letter_shooter.space.EnemyShip;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.dto.ShipRenderSpec;
import com.tiem625.space_letter_shooter.util.MathUtils;
import com.tiem625.space_letter_shooter.util.Point;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tiem625.space_letter_shooter.util.MathUtils.RNG;

public class GameLoop extends ApplicationAdapter {
    SpriteBatch batch;
    SpaceScene spaceScene;


    @Override
    public void create() {
        batch = SpriteBatchMaker.buildDefault();
        Fonts.ENEMY_TEXT_NORMAL_FONT = BitmapFontMaker.buildEnemyShipNormalFont();
        Fonts.MAIN_UI_FONT = BitmapFontMaker.buildMain();

        if (isGameDebugProfile()) {
            addAlwaysOnDebugScene();
        }
        spaceScene = SceneMaker.buildSpaceScene();
        var alwaysOnBGScene = SceneMaker.buildAlwaysOnBGScene();
        ScenesManager.INSTANCE.addAlwaysOnScene(alwaysOnBGScene);

        ScenesManager.INSTANCE.setCurrentScene(spaceScene);
        setCurrentSceneAsInput();
        GamePropsHolder.applyCurrentGameConfig();


        var shipsXMargin = 10.0f;
        var shipsSpecs = loadShipSpecs();
        var sceneShips = addSpaceSceneShips(7, shipsSpecs);
        float shipsTotalWidth = (float)sceneShips.stream().mapToDouble(ship -> ship.getShipTextureSize().x).sum();
        var spaceReservedForPadding = GamePropsHolder.props.getResolutionWidth() - shipsTotalWidth - (2 * shipsXMargin);
        var shipsXPadding = spaceReservedForPadding <= 0.0f ? 0.0f : spaceReservedForPadding / sceneShips.size();
        var shipDesiredPositions = calcShipsStartingPositions(sceneShips, shipsXMargin, shipsXPadding);
        setupShipsFlyToStartActions(shipDesiredPositions);
    }

    private boolean isGameDebugProfile() {
        return Objects.equals(System.getProperty("game.profile.active"), "debug");
    }

    private void addAlwaysOnDebugScene() {
        System.out.println("Game in debug profile! Enabling debug config changer...");
        Scene debugScene = SceneMaker.buildDebugScene();
        ScenesManager.INSTANCE.addAlwaysOnScene(debugScene);
        InputProcessorManager.addAlwaysOnInputProcessor(debugScene.getFirstStage());
    }

    private void setupShipsFlyToStartActions(Map<EnemyShip, Point> shipDesiredPositions) {
        shipDesiredPositions.entrySet().stream()
                .reduce(Actions.delay(0.5f), (prevDelay, shipPositionPair) -> {
                    var ship = shipPositionPair.getKey();
                    var startPosition = shipPositionPair.getValue();
                    var moveToStartAction = Actions.moveTo(
                            startPosition.x,
                            startPosition.y,
                            1,
                            Interpolation.sine
                    );
                    var shipActions = Actions.sequence(
                            Actions.delay(prevDelay.getDuration()),
                            moveToStartAction
                    );
                    ship.addAction(shipActions);

                    return Actions.delay(prevDelay.getDuration() + 0.5f);
                }, (delay1, delay2) -> Actions.delay(delay1.getDuration() + delay2.getDuration()));
    }

    private List<EnemyShip> addSpaceSceneShips(int numShips, Set<ShipRenderSpec> shipSpecs) {
        return IntStream.range(0, numShips)
                .mapToObj(idx -> new EnemyShip("test_" + idx, MathUtils.nextRandomElement(shipSpecs)))
                .map(spaceScene::addEnemyShip)
                //put ships safely offscreen
                .peek(ship -> ship.setPosition(-500, -500))
                .collect(Collectors.toList());
    }

    private Map<EnemyShip, Point> calcShipsStartingPositions(List<EnemyShip> enemyShips, float shipsXMargin, float shipsXPadding) {
        var shipStartingPositions = new HashMap<EnemyShip, Point>();
        float nextShipOffsetX = shipsXMargin;
        var resolutionHeight = GamePropsHolder.props.getResolutionHeight();
        for (EnemyShip nextShip: enemyShips) {
            Point shipTextureSize = nextShip.getShipTextureSize();
            spaceScene.addEnemyShip(nextShip);
            //top half of screen, between 0 - 1 ship heights from top border
            var randomYOffset = resolutionHeight - ((1 + RNG.nextFloat()) * shipTextureSize.y);
            shipStartingPositions.put(nextShip, new Point(nextShipOffsetX, randomYOffset));
            nextShipOffsetX = nextShipOffsetX + shipsXPadding + shipTextureSize.x;
        }

        return shipStartingPositions;
    }

    private Set<ShipRenderSpec> loadShipSpecs() {

        try {
            return new ObjectMapper().readValue(Gdx.files.internal("enemy_ships.json").read(), new TypeReference<>() {
            });
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptySet();
        }
    }

    private void setCurrentSceneAsInput() {
        ScenesManager.INSTANCE.currentScene()
                .ifPresent(scene ->
                        InputProcessorManager.setCurrentInputProcessors(scene.stages().toArray(Stage[]::new)));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        ScenesManager.INSTANCE.renderActiveScenes();
        batch.end();
    }

    @Override
    public void dispose() {
        ResourcesDisposer.INSTANCE.disposeAll();
        GamePropsHolder.writeOutConfig();
    }
}
