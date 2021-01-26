package com.tiem625.space_letter_shooter.space.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.space.spec.EnemyShipRenderSpec;
import com.tiem625.space_letter_shooter.space.vessel.Vessel;
import com.tiem625.space_letter_shooter.text.SpellableText;

import java.util.Map;

import static com.tiem625.space_letter_shooter.util.CommonActionsBuilders.buildShakeActionSequence;


public class EnemyShip extends Group {

    //DESCRIPTOR
    private final String id;
    private final Vessel shipModel;
    private final SpellableText shipText;

    //STATE
    private boolean shipDisposing;
    private float minSpeed;
    private float maxSpeed;
    private float minDescentY;
    private float maxDescentY;

    public EnemyShip(String shipId, String text, EnemyShipRenderSpec enemyShipRenderSpec) {
        super();
        this.id = shipId;
        this.shipText = new SpellableText(text, enemyShipRenderSpec.getTextRenderSpec());
        this.shipModel = new Vessel(enemyShipRenderSpec.getVesselRenderSpec());
        addActor(shipModel);
        addActor(shipText);
        this.shipDisposing = false;
    }

    public void setShipText(String newText) {
        shipText.setText(newText);
    }

    public void stopActions() {
        clearActions();
    }

    public float nextVelocity() {
        return MathUtils.random(minSpeed, maxSpeed);
    }

    public float nextDescentStep() {
        return MathUtils.random(minDescentY, maxDescentY);
    }

    public void setSpeedRange(float min, float max) {
        minSpeed = min;
        maxSpeed = max;
    }

    public void setDescentYRange(float min, float max) {
        minDescentY = min;
        maxDescentY = max;
    }

    public String getId() {
        return id;
    }

    public boolean isShipDisposing() {
        return shipDisposing;
    }

    public boolean canHitCharacter(char input) {
        return shipText.canSpellCharacter(input);
    }

    public void hitCharacter(char input) {
        shipText.spellCharacter();
        if (shipText.isSpelled()) {
            disposeShip();
        }
    }

    private void disposeShip() {

        shipDisposing = true;
        beginDisposeActions();
        postShipEvent(GameEventType.SHIP_SPELLED);
    }

    private void beginDisposeActions() {
        var disappearActions = Actions.sequence(
                buildShakeActionSequence(15, new Vector2(20f, 20f), 1.0f),
                Actions.run(() -> postShipEvent(GameEventType.SHIP_GONE)),
                Actions.removeActor()
        );
        addAction(disappearActions);
    }

    private void postShipEvent(GameEventType eventType) {
        EventsHandling.postEvent(eventType.makeEvent(Map.of("ship", this)));
    }

    @Override
    public String toString() {
        return "EnemyShip{" +
                "text='" + shipText + '\'' +
                ", ship=" + shipModel +
                '}';
    }
}
