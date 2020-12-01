package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.scene.Scene;

import java.util.Objects;
import java.util.stream.Stream;

public class SpaceScene extends Scene {

    private final Stage enemyShipsStage;

    public SpaceScene() {
        super();
        enemyShipsStage = addEmptyShipsStage();
    }

    private Stage addEmptyShipsStage() {
        return addAndGetStage(new Stage(new FitViewport(
                GamePropsHolder.props.getResolutionWidth(),
                GamePropsHolder.props.getResolutionHeight()
        )));
    }

    public EnemyShip addEnemyShip(EnemyShip ship) {
        Objects.requireNonNull(ship);
        enemyShipsStage.addActor(ship);

        return ship;
    }

    public Stream<EnemyShip> enemyShips() {
        return stages.stream()
                .map(Stage::getActors)
                .flatMap(actors -> Stream.of(actors.items))
                .filter(actor -> actor instanceof EnemyShip)
                .map(actor -> (EnemyShip)actor);
    }

}
