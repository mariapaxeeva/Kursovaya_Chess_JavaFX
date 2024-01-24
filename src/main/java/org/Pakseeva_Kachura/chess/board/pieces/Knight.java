// Конь

package org.Pakseeva_Kachura.chess.board.pieces;

import org.Pakseeva_Kachura.chess.model.ChessType;
import org.Pakseeva_Kachura.chess.board.ChessList;

public class Knight extends ChessPiece {

    public Knight(final ChessList list,
                  final ChessType color,
                  final int x, final int y) {

        super(list, ChessType.KNIGHT, color, x, y);

    }

    @Override
    public boolean validateMove(final int targetX, final int targetY) {

        /*
         Кони могут двигаться в форме буквы L во всех направлениях, независимо от того,
         находятся ли на их пути какие-либо фигуры
        */

        calculateDistance(targetX, targetY);

        xDist = Math.abs(xDist);
        yDist = Math.abs(yDist);

        if ((xDist == 1 && yDist == 2) || (xDist == 2 && yDist == 1)) {

            return true;

        }

        return false;

    }

    @Override
    public void confirmMove(final int targetX, final int targetY) {

        doMove(targetX, targetY, list.findChessPiece(ChessType.inverseOf(color), targetX, targetY));

    }

    @Override
    public void update(final ChessPiece changedPiece) {

        // Для коня реализация не требуется

    }

}
