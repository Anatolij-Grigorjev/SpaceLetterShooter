package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.resource.Textures;
import com.tiem625.space_letter_shooter.space.spec.ShipRenderSpec;

import static com.tiem625.space_letter_shooter.util.CommonActionsBuilders.buildShakeActionSequence;


public class EnemyShip extends Actor {

    private final String text;
    private String spelledCharacters;
    private final TextureRegion texture;
    private final ShipRenderSpec shipRenderSpec;

    public EnemyShip(String text, ShipRenderSpec shipRenderSpec) {
        super();
        this.text = text;
        this.setName(text);
        this.spelledCharacters = "";
        this.shipRenderSpec = shipRenderSpec;
        this.texture = Textures.buildAndGetAtlasRegionSprite(shipRenderSpec.spriteKey);
    }

    public EnemyShip cloneShip(String withText) {
        return new EnemyShip(withText, shipRenderSpec);
    }

    public void stopActions() {
        clearActions();
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void setSamePosition(Actor other) {
        setPosition(other.getX(), other.getY());
    }

    public Vector2 getShipTextureSize() {
        return new Vector2(texture.getRegionWidth(), texture.getRegionHeight());
    }

    public boolean canHitCharacter(char input) {
        if (shipTextIsSpelled()) {
            return false;
        }
        var nextShipTextChar = text.substring(spelledCharacters.length()).charAt(0);
        return nextShipTextChar == input;
    }

    public void hitCharacter(char input) {
        spelledCharacters += input;
        beginDisposeActionsIfSpelled();
    }

    private boolean shipTextIsSpelled() {
        return spelledCharacters.length() >= text.length();
    }

    private void beginDisposeActionsIfSpelled() {
        if (shipTextIsSpelled()) {
            beginDisposeActions();
        }
    }

    private void beginDisposeActions() {
        var disappearActions = Actions.sequence(
                buildShakeActionSequence(25, new Vector2(25f, 25f), 2.0f),
                Actions.removeActor()
        );
        addAction(disappearActions);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //draw sprite with actor origin at sprite center
        batch.draw(texture, getX(), getY());
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
}
