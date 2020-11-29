package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.scene.Scene;

import java.util.stream.Stream;

public class SpaceScene extends Scene {

    public SpaceScene() {
        super();
        addEmptyShipsStage();
    }

    private void addEmptyShipsStage() {
        addStage(new Stage());
    }

    public Stream<EnemyShip> enemyShips() {
        return stages.stream()
                .map(Stage::getActors)
                .flatMap(actors -> Stream.of(actors.items))
                .filter(actor -> actor instanceof EnemyShip)
                .map(actor -> (EnemyShip)actor);
    }

}
