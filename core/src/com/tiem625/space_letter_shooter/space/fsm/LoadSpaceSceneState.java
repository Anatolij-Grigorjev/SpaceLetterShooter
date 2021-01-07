package com.tiem625.space_letter_shooter.space.fsm;

import com.tiem625.space_letter_shooter.scene.SceneState;
import com.tiem625.space_letter_shooter.space.SceneConfigureSpecs;
import com.tiem625.space_letter_shooter.space.SpaceScene;
import com.tiem625.space_letter_shooter.space.ship.ShipDescentLoader;
import com.tiem625.space_letter_shooter.space.spec.SceneConfigureSpec;

public class LoadSpaceSceneState extends SceneState<SpaceScene> {

    public static final String KEY = "LOAD_SPACE_SCENE";

    private final SceneConfigureSpec sceneConfigureSpec;

    public LoadSpaceSceneState(SpaceScene scene, String sceneConfigureSpecKey) {
        super(KEY, scene);
        this.sceneConfigureSpec = SceneConfigureSpecs.api.getSceneConfigureSpec(sceneConfigureSpecKey);
    }

    @Override
    public void enterState(String prevStateKey) {
        super.enterState(prevStateKey);
        load(sceneConfigureSpec);
    }

    private void load(SceneConfigureSpec spec) {
        var descentLoader = new ShipDescentLoader();
        var descendingShips = descentLoader.loadDescendingShipsFromSpec(spec);
        descendingShips.forEach(entity::addEnemyShipToScene);
    }
}
