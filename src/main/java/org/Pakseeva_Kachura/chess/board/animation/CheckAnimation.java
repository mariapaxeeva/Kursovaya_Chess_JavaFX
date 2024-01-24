// Анимация для короля при шахе

package org.Pakseeva_Kachura.chess.board.animation;

import javafx.animation.AnimationTimer;

import org.Pakseeva_Kachura.chess.board.pieces.King;

public class CheckAnimation extends AnimationTimer {

    private King king;

    private int offset = -1;
    private int ticks = 0;

    public CheckAnimation(final King king) {

        this.king = king;

    }

    // Цикл движений короля по AnimationTimer
    public void handle(final long now) {

        ticks++;

        /*
        | Каждые пять тактов король перемещается вперед и назад,
         чтобы создать эффект встряхивания.
        */
        if (ticks == 5) {

            offset = offset == -1 ? 1 : -1;

            king.offsetView(offset);

            ticks = 0;

        }

    }

}
