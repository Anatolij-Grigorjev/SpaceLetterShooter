package com.tiem625.space_letter_shooter.space;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tiem625.space_letter_shooter.events.EventsHandling;
import com.tiem625.space_letter_shooter.events.GameEvent;
import com.tiem625.space_letter_shooter.events.GameEventType;
import com.tiem625.space_letter_shooter.resource.Fonts;
import com.tiem625.space_letter_shooter.scene.SceneId;

import java.util.Map;
import java.util.stream.IntStream;

public class GameOverText extends Actor {

    private final String[] text = {"GAME OVER", "Press 'R' to try again."};
    private final BitmapFont font;
    private final float lineVertSpace;

    public GameOverText() {
        this.font = Fonts.MAIN_UI_FONT;
        this.lineVertSpace = this.font.getCapHeight() + this.font.getLineHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        IntStream.range(0, text.length).forEachOrdered(idx -> {
            font.draw(
                    batch,
                    //text
                    text[idx],
                    //position
                    getX(), getY() - (idx * lineVertSpace)
            );
        });
    }

    public static class RestartSceneInputListener extends InputListener {

        @Override
        public boolean keyTyped(InputEvent event, char character) {
            switch (character) {
                case 'R':
                case 'r':
                    EventsHandling.postEvent(new GameEvent(SceneId.DEBUG, GameEventType.SCENE_RESTART, Map.of()));
                    break;
                default:
                    break;
            }

            return true;
        }
    }
}
