// Анимация при перезагрузке игры

package org.Pakseeva_Kachura.chess.board.animation;

import javafx.util.Duration;
import javafx.scene.shape.Path;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.animation.PathTransition;

import org.Pakseeva_Kachura.chess.board.pieces.ChessPiece;

public class RecallAnimation {

    PathTransition internalAnim;

    public RecallAnimation(final ChessPiece piece) {

        internalAnim = new PathTransition();

        internalAnim.setNode(piece);
        internalAnim.setDuration(Duration.seconds(0.3));

    }

    // Запуск анимации с текущими позициями фигур
    // и координатами, на которых фигуры должны оказаться
    public void start(final int nowX, final int nowY, final int x, final int y) {

        // Не перемещать фигуру, если ее положение не было изменено
        if (nowX != x || nowY != y) {

            /*
            | Расчет пути, который необходимо пройти, по расстоянию от исходного положения
              элемента до требуемого.
            */
            internalAnim.setPath(
                new Path(
                    new MoveTo(16, 16),
                    new LineTo(
                        ((x + 16) - nowX),
                        ((y + 16) - nowY)
                    )
                )
            );

            internalAnim.play();

        }

    }

}
