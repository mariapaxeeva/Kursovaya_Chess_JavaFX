// Ферзь

package org.Pakseeva_Kachura.chess.board.pieces;

import org.Pakseeva_Kachura.chess.model.ChessType;
import org.Pakseeva_Kachura.chess.board.ChessList;

public class Queen extends ChessPiece {

    public Queen(final ChessList list,
                 final ChessType color,
                 final int x, final int y) {

        super(list, ChessType.QUEEN, color, x, y);

    }

    @Override
    public boolean validateMove(final int targetX, final int targetY) {

        /*
        Ферзи могут передвигаться без ограничений как по прямой, так и по диагонали,
        но не могут перепрыгивать через другие шахматные фигуры.
        */

        calculateDistance(targetX, targetY);

        xDist = Math.abs(xDist);
        yDist = Math.abs(yDist);

        if ((xDist == 0 ^ yDist == 0) || (xDist == yDist)) {

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

        // Для ферзя реализация не требуется

    }

}
