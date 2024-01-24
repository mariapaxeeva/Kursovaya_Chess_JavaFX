// Индикатор текущего хода

package org.Pakseeva_Kachura.chess.menu.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import org.Pakseeva_Kachura.chess.model.ChessType;
import org.Pakseeva_Kachura.chess.menu.animation.ChangeAnimation;

public class TurnIndicator extends Rectangle {

    private ChessType color;

    private ChangeAnimation changeAnim = null;

    public TurnIndicator(final ChessType color) {

        super(22, 22);
        relocate(292, 8);

        setStrokeWidth(3);
        setStrokeType(StrokeType.INSIDE);
        setStroke(Color.web("eaeaea"));

        setFill(Color.web(ChessType.toHex(color)));

        this.color = color;

    }

    // Обновление цвета индикатора
    public void update(final ChessType newColor) {

        if (color != newColor) {

            if (changeAnim == null) {

                changeAnim = new ChangeAnimation(this);

            }

            changeAnim.change(Color.web(ChessType.toHex(newColor)));

            color = newColor;

        }

    }

}
