// Окно окончания игры

package org.Pakseeva_Kachura.chess.menu.ui;

import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.media.Texture;
import org.Pakseeva_Kachura.chess.media.TextureAtlas;

import org.Pakseeva_Kachura.chess.menu.animation.EndAnimation;

public class EndScreen extends Pane {

    private boolean isShown = false;
    private ChessType color;

    private ImageView endView = null;
    private EndAnimation endAnim;

    public EndScreen(final ChessType color) {

        setPrefSize(124, 12);
        relocate(99, 35);

        this.color = color;

        endAnim = new EndAnimation(this);

        show(color);

    }

    // Вывод изображения об окончании игры
    public void show(final ChessType newColor) {

        if (newColor != color || endView == null) {

            if (endView != null) {

                getChildren().remove(endView);

            }

            // Получение изображения в зависимости от того, кто победил.
            endView = TextureAtlas.getTexture(
                Texture.END_SCREEN, 0, newColor.getCoordinate(), 124, 24
            );

            color = newColor;

            getChildren().add(endView);

        }

        endAnim.in();

        isShown = true;

    }

    // Скрытие изображения
    public void hide() {

        if (isShown) {

            endAnim.out();

            isShown = false;

        }

    }

}
