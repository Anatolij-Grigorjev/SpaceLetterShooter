package com.tiem625.space_letter_shooter.space.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.resource.Textures;
import com.tiem625.space_letter_shooter.space.spec.ShipRenderSpec;

import java.util.Map;

import static com.tiem625.space_letter_shooter.util.CommonActionsBuilders.buildShakeActionSequence;


public class EnemyShip extends Actor {

    //DESCRIPTOR
    private final String id;
    private final String text;
    private final TextureRegion texture;
    private final ShipRenderSpec shipRenderSpec;


    //STATE
    private String spelledCharacters;
    private boolean shipDisposing;
    private float minSpeed;
    private float maxSpeed;
    private float minDescentY;
    private float maxDescentY;

    public EnemyShip(String shipId, String text, ShipRenderSpec shipRenderSpec) {
        super();
        this.id = shipId;
        this.text = text;
        this.setName(text);
        this.spelledCharacters = "";
        this.shipDisposing = false;

        this.shipRenderSpec = shipRenderSpec;
        this.texture = Textures.buildAndGetAtlasRegionSprite(shipRenderSpec.spriteKey);
        this.setWidth(texture.getRegionWidth());
        this.setHeight(texture.getRegionHeight());
        this.setOrigin(Align.center);
    }

    public EnemyShip cloneShip(String withText) {
        return new EnemyShip(id, withText, shipRenderSpec);
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

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public String getId() {
        return id;
    }

    public void setSamePosition(Actor other) {
        setPosition(other.getX(), other.getY());
    }

    public boolean canHitCharacter(char input) {
        if (shipTextIsSpelled()) {
            return false;
        }
        var nextShipTextChar = text.substring(spelledCharacters.length()).charAt(0);
        return Character.toUpperCase(nextShipTextChar) == Character.toUpperCase(input);
    }

    public void hitCharacter(char input) {
        spelledCharacters += input;
        beginDisposeActionsIfSpelled();
    }

    public boolean isShipDisposing() {
        return shipDisposing;
    }

    private boolean shipTextIsSpelled() {
        return spelledCharacters.length() >= text.length();
    }

    private void beginDisposeActionsIfSpelled() {
        if (shipTextIsSpelled()) {
            shipDisposing = true;
            beginDisposeActions();
            postShipEvent(GameEventType.SHIP_SPELLED);
        }
    }

    private void beginDisposeActions() {
        var disappearActions = Actions.sequence(
                buildShakeActionSequence(25, new Vector2(25f, 25f), 2.0f),
                Actions.run(() -> postShipEvent(GameEventType.SHIP_GONE)),
                Actions.removeActor()
        );
        addAction(disappearActions);
    }

    private void postShipEvent(GameEventType eventType) {
        EventsHandling.postEvent(eventType.makeEvent(Map.of("ship", this)));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //draw sprite with actor origin at sprite center
        drawTexture(batch, parentAlpha);
        var font = Fonts.ENEMY_TEXT_NORMAL_FONT;
        Fonts.useFontWithColor(font, Color.YELLOW, colorFont -> {
            var drawString = text.substring(spelledCharacters.length());
            colorFont.draw(
                    batch,
                    //text
                    drawString,
                    //position
                    getX() + shipRenderSpec.textShipOffsetX, getY() + shipRenderSpec.textShipOffsetY,
                    //substring indexes
                    0, drawString.length(),
                    //width
                    shipRenderSpec.textTargetWidth,
                    //align, wrap, truncate
                    Align.center, true, "..."
            );
        });
    }

    @Override
    public String toString() {
        return "EnemyShip{" +
                "text='" + text + '\'' +
                ", shipSprite=" + texture +
                '}';
    }

    private void drawTexture(Batch batch, float parentAlpha) {
        batch.draw(
                texture,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );
    }
}
