package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tiem625.space_letter_shooter.config.GamePropsHolder;
import com.tiem625.space_letter_shooter.scene.Scene;
import com.tiem625.space_letter_shooter.space.enemy.EnemyShip;
import com.tiem625.space_letter_shooter.space.fsm.SpaceSceneFSM;
import com.tiem625.space_letter_shooter.space.shooter.ShootingShip;
import com.tiem625.space_letter_shooter.space.spec.SceneScript;
import com.tiem625.space_letter_shooter.space.vessel.Vessel;
import com.tiem625.space_letter_shooter.space.vessel.VesselRenderSpec;
import com.tiem625.space_letter_shooter.util.StreamUtils;

import java.util.Objects;
import java.util.stream.Stream;

public class SpaceScene extends Scene {

    private final Stage enemyShipsStage;
    private final Stage titleCardStage;
    private final ShootingShip shootingShip;

    public SpaceScene(SceneScript sceneScript) {
        super(sceneScript.getScriptId());
        this.enemyShipsStage = addFullScreenStage();
        this.titleCardStage = addFullScreenStage();
        this.shootingShip = new ShootingShip(
                new Vessel(new VesselRenderSpec("shooting_ship", 150f, 120f))
        );
        setShootingShipAtBottom();
        this.fsm = new SpaceSceneFSM(this);
    }

    public ShootingShip getShootingShip() {
        return shootingShip;
    }

    public EnemyShip addEnemyShipToScene(EnemyShip ship) {
        Objects.requireNonNull(ship);
        enemyShipsStage.addActor(ship);
        return ship;
    }

    public Stage getEnemyShipsStage() {
        return enemyShipsStage;
    }

    public Stage getTitleCardStage() {
        return titleCardStage;
    }

    public Stream<EnemyShip> enemyShips() {
        return stages.stream()
                .map(Stage::getActors)
                .flatMap(StreamUtils::stream)
                .filter(actor -> actor instanceof EnemyShip)
                .map(actor -> (EnemyShip) actor);
    }

    private void setShootingShipAtBottom() {

        var resolution = GamePropsHolder.props.getResolution();
        shootingShip.setPosition(
                (resolution.x - shootingShip.getWidth()) / 2,
                shootingShip.getHeight() / 2
        );
        enemyShipsStage.addActor(shootingShip);
    }
}
