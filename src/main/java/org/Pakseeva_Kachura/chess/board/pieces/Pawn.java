// Пешка

package org.Pakseeva_Kachura.chess.board.pieces;

import org.Pakseeva_Kachura.chess.model.ChessType;
import org.Pakseeva_Kachura.chess.board.ChessList;

public class Pawn extends ChessPiece {

    private ChessPiece passPiece = null;

    private final int relOne;
    private final int relTwo;

    private boolean did2Step = false;
    private boolean vmwpReturn = false;
    private boolean assumePieceExists = false;

    public Pawn(final ChessList list,
                final ChessType color,
                final int x, final int y) {

        super(list, ChessType.PAWN, color, x, y);

        /*
        Определение параметров в validateMove(), для того, чтобы пешка
        всегда двигалась вперед. Вверх для белых фигур, вниз для черных фигур.
        */
        if (color == ChessType.WHITE) {

            relOne = -1;
            relTwo = -2;

        } else {

            relOne = 1;
            relTwo = 2;

        }

    }

    @Override
    public boolean validateMove(final int targetX, final int targetY) {

        /*
            Пешка может переместиться вперед на незанятую клетку непосредственно перед ней или переместиться на две
            клетки вперед в свой первый ход. Пешки могут перемещаться по диагонали на одно место за раз,
            чтобы взять фигуру противника.
        */
        calculateDistance(targetX, targetY);

        if (Math.abs(xDist) == 1 && yDist == relOne) {

            if (passPiece != null) {

                if (passPiece.getX() == targetX
                 && passPiece.getY() != targetY) {

                    return true;

                }

            }

            if (list.findChessPiece(ChessType.inverseOf(color), targetX, targetY) != null
                || assumePieceExists) {

                return true;

            }

            return false;

        } else if (xDist != 0) {

            return false;

        }

        if (yDist == relOne) {

            if (list.findChessPiece(targetX, targetY) == null
             ^ assumePieceExists) {

                return true;

            }

        } else if (yDist == relTwo && !hasMoved) {

            if ((list.findChessPiece(targetX, targetY - relOne) == null
            &&  list.findChessPiece(targetX, targetY) == null)
             ^  assumePieceExists) {

                return true;

            }

        }

        return false;

    }

    public boolean validateMoveWithPiece(final int targetX, final int targetY) {

        assumePieceExists = true;

        vmwpReturn = validateMove(targetX, targetY);

        assumePieceExists = false;

        return vmwpReturn;

    }

    @Override
    public void confirmMove(final int targetX, final int targetY) {

        /*
        Если фигура только что продвинулась на 2 шага вперед, установка did2Step в true, чтобы другие
        фигуры могли выполнять проход должным образом.
        При следующем ходе этой переменной возвращается значение false
        */
        did2Step = false;

        if (Math.abs(yDist) == 2) {

            did2Step = true;

        }

        if (passPiece != null) {

            if (passPiece.getX() == targetX
            && passPiece.getY() != targetY) {

                doMove(targetX, targetY, passPiece);

                return;

            }

        }

        doMove(targetX, targetY, list.findChessPiece(ChessType.inverseOf(color), targetX, targetY));

    }

    // Если пешка совершила только движение на 2 клетки, то можно сделать специальный ход
    @Override
    public void update(final ChessPiece changedPiece) {

        passPiece = null;

        if (changedPiece.getType() == ChessType.PAWN
        && changedPiece.getColor() != color) {

            if (Math.abs(changedPiece.getX() - x) == 1
            && ((Pawn) changedPiece).get2Step()
            && changedPiece.getY() == y) {

                passPiece = changedPiece;

            }

        }

    }

    public boolean get2Step() {

        return did2Step;

    }

}
