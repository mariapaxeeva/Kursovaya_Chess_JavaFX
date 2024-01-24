// Установка фигур на шахматную доску

package org.Pakseeva_Kachura.chess.board.pieces;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.board.ChessList;
import org.Pakseeva_Kachura.chess.board.BoardPane;

public class ChessFactory {

    private final ChessList pieces;
    private final BoardPane board;

    private ChessType color = ChessType.WHITE;

    private King lastGenKing = null;

    public ChessFactory(final ChessList pieces, final BoardPane board) {

        this.pieces = pieces;
        this.board = board;

    }

    // Сформировать ряд пешек
    public void genPawnRow(final int y) {

        for (int i = 0; i < 8; i++) {

            addAt(i, y, ChessType.PAWN);

        }

    }

    // Сформировать задний ряд из разных фигур
    public void genHomeRow(final int y) {

        for (int i = 0; i < 8; i++) {

            addAt(i, y, ChessType.HOME_ORDER[i]);

        }

        lastGenKing.rookSetup();
    }
    // Создание фигуры данного типа в координатах x/y
    public void addAt(final int x, final int y, final ChessType type) {

        switch (type) {

            case PAWN: new Pawn(pieces, color, x, y); break;
            case ROOK: new Rook(pieces, color, x, y); break;
            case KNIGHT: new Knight(pieces, color, x, y); break;
            case BISHOP: new Bishop(pieces, color, x, y); break;
            case QUEEN: new Queen(pieces, color, x, y); break;
            case KING: lastGenKing = new King(pieces, color, x, y, board); break;

        }

    }

    // Перемещение фигуры
    public void promote(final int x, final int y, final ChessType type) {

        pieces.killPiece(pieces.findChessPiece(x, y));

        addAt(x, y, type);

    }

    // Восстановить "взятые" фигуры
    public void replaceKilled() {

        for (ChessPiece piece : pieces.getKilledPieces()) {

            piece.unkill();

        }

    }

    // Установить цвет созданной фигуры
    public void setColor(final ChessType newColor) {

        ChessType.validateColor(color);

        if (newColor != color) {

            color = newColor;

        }

    }

}
