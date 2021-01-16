package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.space.fsm.SpaceSceneFSM;
import com.tiem625.space_letter_shooter.space.ship.EnemyShip;
import com.tiem625.space_letter_shooter.space.spec.SceneScript;
import com.tiem625.space_letter_shooter.util.StreamUtils;

import java.util.Objects;
import java.util.stream.Stream;

public class SpaceScene extends Scene {

    private final Stage enemyShipsStage;

    public SpaceScene(SceneScript sceneScript) {
        super(sceneScript.getScriptId());
        this.enemyShipsStage = addEmptyShipsStage();
        this.fsm = new SpaceSceneFSM(this);
    }

    public EnemyShip addEnemyShipToScene(EnemyShip ship) {
        Objects.requireNonNull(ship);
        enemyShipsStage.addActor(ship);
        return ship;
    }

    public Stage getEnemyShipsStage() {
        return enemyShipsStage;
    }

    public Stream<EnemyShip> enemyShips() {
        return stages.stream()
                .map(Stage::getActors)
                .flatMap(StreamUtils::stream)
                .filter(actor -> actor instanceof EnemyShip)
                .map(actor -> (EnemyShip) actor);
    }

    private Stage addEmptyShipsStage() {
        return addAndGetStage(new Stage(Viewports.FIT_FULLSCREEN));
    }
}
