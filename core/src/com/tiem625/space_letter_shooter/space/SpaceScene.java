package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.config.Viewports;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.space.fsm.SpaceSceneFSM;
import com.tiem625.space_letter_shooter.space.ship.EnemyShip;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;
import com.tiem625.space_letter_shooter.util.StreamUtils;

import java.util.Objects;
import java.util.stream.Stream;

public class SpaceScene extends Scene {

    private final Stage enemyShipsStage;

    public SpaceScene(SceneConfigureSpec sceneConfigureSpec) {
        super(sceneConfigureSpec.getSceneId());
        this.enemyShipsStage = addEmptyShipsStage();
        this.enemyShipsStage.addListener(new ShipTextCharsCaptureListener());
        this.fsm = new SpaceSceneFSM(this);
    }

    public EnemyShip addEnemyShipToScene(EnemyShip ship) {
        Objects.requireNonNull(ship);
        enemyShipsStage.addActor(ship);
        return ship;
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

    private class ShipTextCharsCaptureListener extends InputListener {

        @Override
        public boolean keyTyped(InputEvent event, char character) {

            enemyShips()
                    .filter(ship -> ship.canHitCharacter(character))
                    .findFirst()
                    .ifPresent(enemyShip -> enemyShip.hitCharacter(character));

            return true;
        }
    }

}
