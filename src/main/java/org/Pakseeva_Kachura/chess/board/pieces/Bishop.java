// Слон

package org.Pakseeva_Kachura.chess.board.pieces;

import org.Pakseeva_Kachura.chess.model.ChessType;
import org.Pakseeva_Kachura.chess.board.ChessList;

public class Bishop extends ChessPiece {

    public Bishop(final ChessList list,
                  final ChessType color,
                  final int x, final int y) {

        super(list, ChessType.BISHOP, color, x, y);

    }

    @Override
    public boolean validateMove(final int targetX, final int targetY) {

        /*
        | Слоны могут передвигаться только по диагонали,
          но не могут перепрыгивать через другие фигуры.
        */

        calculateDistance(targetX, targetY);

        xDist = Math.abs(xDist);
        yDist = Math.abs(yDist);

        if (xDist == yDist) {

            if (!findBlockingPieces(targetX, targetY)) {

                return true;

            }

        }

        return false;

    }

    @Override
    public void confirmMove(final int targetX, final int targetY) {

        doMove(targetX, targetY, list.findChessPiece(ChessType.inverseOf(color), targetX, targetY));

    }

    @Override
    public void update(final ChessPiece changedPiece) {

        // Для слона реализация не требуется

    }

}
