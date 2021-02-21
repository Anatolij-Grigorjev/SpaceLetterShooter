package com.tiem625.space_letter_shooter.space.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
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

import static com.tiem625.space_letter_shooter.util.CommonActionsBuilders.*;


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

    private final Vector2 extents;

    public EnemyShip(String shipId, String text, EnemyShipRenderSpec enemyShipRenderSpec) {
        super();
        this.id = shipId;
        this.shipText = new SpellableText(text, enemyShipRenderSpec.getTextRenderSpec());
        this.shipModel = new Vessel(enemyShipRenderSpec.getVesselRenderSpec());
        addActor(shipModel);
        addActor(shipText);
        setWidth(this.shipModel.getWidth());
        setHeight(this.shipModel.getHeight());
        setOrigin(shipModel.getOriginX(), shipModel.getOriginY());
        this.shipDisposing = false;
        this.extents = new Vector2(getWidth(), getHeight()).scl(0.5f);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public Vector2 getExtents() { return extents; }

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
        shipModel.addAction(buildColorFlashAction(shipModel.getColor(), Color.RED, 0.16f));
        this.addAction(buildShakeActionSequence(2, new Vector2(15f, 15f), 0.16f));
        if (shipText.isSpelled()) {
            disposeShip();
        }
    }

    public void beginAppearActions(Vector2 appearPosition, float actionDelay, float duration) {
        setPosition(appearPosition.x, appearPosition.y);
        setScale(0.0f);
        var moveToStartAction = Actions.parallel(
                Actions.scaleTo(1.0f, 1.0f, duration, Interpolation.slowFast),
                Actions.rotateBy(360f, duration, Interpolation.circle)
        );
        var shipActions = Actions.sequence(
                Actions.delay(actionDelay),
                moveToStartAction
        );
        addAction(shipActions);
    }

    private void disposeShip() {

        shipDisposing = true;
        beginDisposeActions();
        postShipEvent(GameEventType.SHIP_SPELLED);
    }

    private void beginDisposeActions() {
        var numPulses = 10;
        var pulsesDuration = 1.5f;
        var disappearActions = Actions.sequence(
                Actions.parallel(
                        Actions.run(() -> shipModel.addAction(Actions.color(Color.RED, pulsesDuration, Interpolation.fastSlow))),
                        buildShakeActionSequence(15, new Vector2(20f, 20f), 1.0f),
                        buildPulseActionSequence(numPulses, 0.2f, pulsesDuration)
                ),
                Actions.scaleTo(0.0f, 0.0f, 0.25f, Interpolation.slowFast),
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
