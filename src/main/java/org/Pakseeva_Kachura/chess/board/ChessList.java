// Список фигур

package org.Pakseeva_Kachura.chess.board;

import java.util.ArrayList;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.board.pieces.King;
import org.Pakseeva_Kachura.chess.board.pieces.ChessPiece;

import org.Pakseeva_Kachura.chess.entity.EntityList;
import org.Pakseeva_Kachura.chess.entity.EntityChangeListener;

public class ChessList extends EntityList<ChessPiece> {

    private ArrayList<ChessPiece> killedPieces;
    private ArrayList<ChessPiece> promotedPieces;

    public ChessList(final EntityChangeListener<ChessPiece> listener) {

        super(listener);

        killedPieces = new ArrayList<ChessPiece>();
        promotedPieces = new ArrayList<ChessPiece>();

    }

    // Добавление захваченных фигур в список взятых фигур
    public void killPiece(final ChessPiece piece) {

        super.removeEntity(piece);

        killedPieces.add(piece);

    }

    // Добавление недавно перемещаемой фигуры
    public void addPromotedPiece(final ChessPiece piece) {

        promotedPieces.add(piece);

        pushChange(piece);

    }

    // Поиск фигуры с данными координатами данного цвета
    public ChessPiece findChessPiece(final ChessType color, final int x, final int y) {

        for (ChessPiece piece : entities) {

            if (piece.getColor() == color
            &&  piece.getX() == x
            &&  piece.getY() == y) {

                return piece;

            }

        }

        return null;

    }

    // Поиск фигуры по координатам
    public ChessPiece findChessPiece(final int x, final int y) {

        for (ChessPiece piece : entities) {

            if (piece.getX() == x
            &&  piece.getY() == y) {

                return piece;

            }

        }

        return null;

    }

    public void pushChange(final ChessPiece changedPiece) {

        for (ChessPiece piece : entities) {

            piece.update(changedPiece);

        }

    }

    // Сброс всех фигур
    public void resetAll() {

        for (ChessPiece piece : promotedPieces) {

            piece.kill();

            super.removeEntity(piece);

        }

        for (ChessPiece piece : entities) {

            piece.reset();

            /*
            | Сброс статуса короля о том, что он под атакой
            */
            if (piece.getType() == ChessType.KING) {

                ((King) piece).resetChecked();

            }

        }

        killedPieces.clear();

    }

    public ArrayList<ChessPiece> getKilledPieces() {

        return killedPieces;

    }

}
