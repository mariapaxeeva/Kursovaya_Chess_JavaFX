// Ладья

package org.Pakseeva_Kachura.chess.board.pieces;

import org.Pakseeva_Kachura.chess.model.ChessType;
import org.Pakseeva_Kachura.chess.board.ChessList;

public class Rook extends ChessPiece {

    public Rook(final ChessList list,
                final ChessType color,
                final int x, final int y) {

        super(list, ChessType.ROOK, color, x, y);

    }

    @Override
    public boolean validateMove(final int targetX, final int targetY) {

        /*
        Ладьи могут двигаться прямо во всех направлениях, но не по диагонали.
        Они также не могут перепрыгивать через другие фигуры.
        Ладьи участвуют в рокировке с королем.
        */

        calculateDistance(targetX, targetY);

        if (xDist == 0 ^ yDist == 0) {

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

        // Для ладьи реализайия не требуется

    }

}
