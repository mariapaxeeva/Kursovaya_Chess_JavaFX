// Текстуры

package org.Pakseeva_Kachura.chess.media;

public enum Texture {

    CHESS_PIECES("/org/Pakseeva_Kachura/chess/textures/chesspieces.png"),
    END_SCREEN("/org/Pakseeva_Kachura/chess/textures/endscreens.png"),
    BORDER("/org/Pakseeva_Kachura/chess/textures/border.png"),
    RESET("/org/Pakseeva_Kachura/chess/textures/reset.png"),
    ICON("/org/Pakseeva_Kachura/chess/textures/icon.png");

    private final String path;

    Texture(final String path) {

        this.path = path;

    }

    // Получение пути к файлу с изображениями
    public String getPath() {

        return path;

    }

}
