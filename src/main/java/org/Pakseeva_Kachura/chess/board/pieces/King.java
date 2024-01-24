// Король

package org.Pakseeva_Kachura.chess.board.pieces;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.board.ChessList;
import org.Pakseeva_Kachura.chess.board.BoardPane;
import org.Pakseeva_Kachura.chess.board.animation.CheckAnimation;

public class King extends ChessPiece {

    private ChessPiece leftRook;
    private ChessPiece rightRook;

    private BoardPane board;

    private ChessPiece checkingPiece = null;
    private CheckAnimation checkAnim = null;
    private boolean isChecked = false;

    private int checkX = 0;
    private int checkY = 0;

    public King(final ChessList list,
                final ChessType color,
                final int x, final int y,
                final BoardPane board) {

        super(list, ChessType.KING, color, x, y);

        this.board = board;

    }

    @Override
    public boolean validateMove(final int targetX, final int targetY) {

        /*
         Короли могут перемещаться во всех направлениях, при условии,
         что пройденное расстояние равно единице, а сделанный ход не приведет к мату.
         Короли могут также выполнить ход, называемый рокировкой ладьей,
         при котором король перемещается на два пробела, а ладья перемещается на то место,
         которое прошел король.
        */

        if (doDistanceLogic(targetX, targetY)) {

            if (isChecked) {

                return true;

            } else {

                return validateSafe(targetX, targetY);

            }

        }

        /*
            Условия для рокировки:
      - Король не должен был двигаться или находиться под атакой
      - Ладьи не должны были двигаться
      - На пути между королем и ладьей не должно быть фигур
      - Король не должен проходить ни через какие фигуры, находящиеся под атакой
        */
        if (yDist == 0 && !hasMoved && checkingPiece == null) {

            if (targetX == 2 && !leftRook.getMoved()) {

                if (!findBlockingPieces(targetX - 2, targetY)) {

                    if (validateSafePath(targetX)) {

                        return true;

                    }

                }

            } else if (targetX == 6 && !rightRook.getMoved()) {

                if (!findBlockingPieces(targetX + 1, targetY)) {

                    if (validateSafePath(targetX)) {

                        return true;

                    }

                }

            }

        }

        return false;

    }

    // Проверка расстояния (для validateSafe()).
    // Возвращает true, если допустимо, false, если нет
    public boolean doDistanceLogic(final int targetX, final int targetY) {

        calculateDistance(targetX, targetY);

        xDist = Math.abs(xDist);
        yDist = Math.abs(yDist);

        if (xDist + yDist < 2) {

            return true;

        } else if (xDist + yDist == 2) {

            if (xDist == yDist) {

                return true;

            }

        }

        return false;

    }

    @Override
    public void confirmMove(final int targetX, final int targetY) {

        calculateDistance(targetX, targetY);

        // Перемещение ладьи при рокировке
        if (Math.abs(xDist) == 2 && yDist == 0) {

            if (xDist < 0) {

                leftRook.confirmMove(targetX + 1, targetY);

            } else {

                rightRook.confirmMove(targetX - 1, targetY);

            }

            doMove(targetX, targetY, null);

            return;

        }

        doMove(targetX, targetY, list.findChessPiece(ChessType.inverseOf(color), targetX, targetY));

    }

    @Override
    public void update(final ChessPiece changedPiece) {

        /*
        Проверка безопасности текущего положения короля. Если под атакой, то проверка,
        есть ли какой-либо выход из шаха. Если нет выхода, это автоматический мат.
        Если есть выход, игрок должен в течение следующего хода уйти из-под атаки,
        в противном случае это все равно автоматический мат. Если будет сделан ход
        другой фигурой, это также автоматический мат.
        */
        checkingPiece = findCheckingPieces();

        if (checkAnim != null) {

            checkAnim.stop();

        }

        if (checkingPiece != null) {

            if (!isChecked && !validateCheckmate() && changedPiece.getColor() != color) {

                if (checkAnim == null) {

                    checkAnim = new CheckAnimation(this);

                }

                checkAnim.start();

                isChecked = true;

            } else {

                board.onEnd(ChessType.inverseOf(color));

            }

            return;

        } else {

            isChecked = false;

        }

    }

    // Проверка безопасности хода
    // Возвращает значение true, если безопасен, и значение false, если нет
    private boolean validateSafe(final int targetX, final int targetY) {

        for (ChessPiece entity : list.getEntities()) {

            if (entity.getColor() != color) {

                if (entity.getType() == ChessType.PAWN) {

                    if (((Pawn) entity).validateMoveWithPiece(targetX, targetY)) {

                        return false;

                    }

                    // Если данная фигура - король, то выполняется doDistanceLogic для
                    // предотвращения бесконечных рекурсивных вызовов validateSafe()
                } else if (entity.getType() == ChessType.KING) {

                    if (((King) entity).doDistanceLogic(targetX, targetY)) {

                        return false;

                    }

                } else {

                    if (entity.validateMove(targetX, targetY)) {

                        return false;

                    }

                }

            }

        }

        return true;

    }

    // Проверка безопасности пути
    // Возвращает true, если путь безопасен, false, если нет
    public boolean validateSafePath(final int targetX) {

        iterX = x;

        while (iterX != targetX) {

            if (iterX > targetX) {

                iterX--;

            } else if (iterX < targetX) {

                iterX++;

            }

            if (!validateSafe(iterX, y)) {

                return false;

            }

        }

        return true;

    }

    // Вариант validateSafe(), который возвращает шахматные фигуры вместо логического значения
    public ChessPiece findCheckingPieces() {

        for (ChessPiece entity : list.getEntities()) {

            if (entity.getColor() != color) {

                if (entity.getType() == ChessType.KING) {

                    if (((King) entity).doDistanceLogic(x, y)) {

                        return entity;

                    }

                } else {

                    if (entity.validateMove(x, y)) {

                        return entity;

                    }

                }

            }

        }

        return null;

    }

    // Проверка возможных ходов, если король находится под атакой
    // Возвращает true, если ходы есть, false, если нет
    public boolean validateCheckmate() {

        for (int nearX = Math.max(x - 1, 0); nearX < Math.min(x + 2, 8); nearX++) {

            for (int nearY = Math.max(y - 1, 0); nearY < Math.min(y + 2, 8); nearY++) {

                if (list.findChessPiece(color, nearX, nearY) == null) {

                    if (validateSafe(nearX, nearY)) {

                        return false;

                    }

                }

            }

        }

        /*
        Если не удается проверка выше, то возможно какая-либо из других фигур
        блокирует движение нужной фигуры (ладьи, слона или ферзя)
        */
        if (checkingPiece.getType() == ChessType.ROOK
        ||  checkingPiece.getType() == ChessType.BISHOP
        ||  checkingPiece.getType() == ChessType.QUEEN) {

            calculateDistance(checkingPiece.getX(), checkingPiece.getY());

            if (xDist + yDist != 0) {

                for (ChessPiece piece : list.getEntities()) {

                    iterX = x;
                    iterY = y;

                    checkX = checkingPiece.getX();
                    checkY = checkingPiece.getY();

                    while (iterX != checkX || iterY != checkY) {

                        if (iterX > checkX) {

                            iterX--;

                        } else if (iterX < checkX) {

                            iterX++;

                        }

                        if (iterY > checkY) {

                            iterY--;

                        } else if (iterY < checkY) {

                            iterY++;

                        }

                        if (iterX != checkX || iterY != checkY) {

                            if (piece.validateMove(iterX, iterY)) {

                                return false;

                            }

                        }

                    }

                }

            }

        }

        // Если обе проверки не удаются, допустимых ходов нет и должен быть объявлен мат.
        return true;

    }

    // Установка ссылок на ладьи для рокировки
    public void rookSetup() {

        leftRook = list.findChessPiece(color, 0, y);
        rightRook = list.findChessPiece(color, 7, y);

    }

    public void resetChecked() {

        isChecked = false;
        checkingPiece = null;

        if (checkAnim != null) {

            checkAnim.stop();

        }

    }

    public boolean getChecked() {

        return isChecked;

    }

}
