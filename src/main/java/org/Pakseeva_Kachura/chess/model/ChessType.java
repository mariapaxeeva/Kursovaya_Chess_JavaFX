// Перечисление всех типов шахматных фигур, цветов шахмат и ходов игроков

package org.Pakseeva_Kachura.chess.model;

public enum ChessType {

    // Каждое перечисление хранит свои координаты в TextureAtlas
    PAWN(0), ROOK(1), KNIGHT(2), BISHOP(3), QUEEN(4), KING(5),

    BLACK(0), WHITE(1);

    public static final ChessType[] HOME_ORDER = {
        ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK
    };

    public static final ChessType[] PROMOTION_ORDER = {
        ROOK, KNIGHT, BISHOP, QUEEN
    };

    private final int coordinate;

    ChessType(final int coordinate) {

        this.coordinate = coordinate;

    }

    public int getCoordinate() {

        return coordinate;

    }

    // Метод возвращает цвет, противоположный заданному
    public static ChessType inverseOf(final ChessType color) {

        validateColor(color);

        return color == WHITE ? BLACK : WHITE;
    }

    // Метод возвращает цвет фигуры в шестнадцатеричной системе
    public static String toHex(final ChessType color) {

        validateColor(color);

        return color == WHITE ? "eaeaea" : "252525";

    }

    // Функция для проверки того, что данный цвет черный или белый
    public static void validateColor(final ChessType color) {

        if (color != ChessType.BLACK && color != ChessType.WHITE) {

            throw new IllegalArgumentException("Expected BLACK or WHITE, got " + color.toString());

        }

    }

}
