// Панель элементов пользовательского интерфейса

package org.Pakseeva_Kachura.chess.menu;

import javafx.scene.layout.Pane;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.board.BoardPane;

import org.Pakseeva_Kachura.chess.menu.ui.EndScreen;
import org.Pakseeva_Kachura.chess.menu.ui.ResetButton;
import org.Pakseeva_Kachura.chess.menu.ui.TurnIndicator;

public class MenuPane extends Pane {

    private BoardPane board;

    private TurnIndicator turn = null;
    private EndScreen endScreen = null;

    public MenuPane() {

        setPrefSize(276, 35);

        getChildren().addAll(
            new ResetButton(this)
        );

    }

    // Изменение TurnIndicator в меню.
    public void changeTurn(final ChessType newColor) {

        if (turn == null) {

            turn = new TurnIndicator(newColor);

            getChildren().add(turn);

        } else {

            turn.update(newColor);

        }

    }

    // Сброс панели управления и меню
    public void onReset() {

        board.onReset();

        // Скрытие экрана
        if (endScreen != null) {

            endScreen.hide();

        }

    }

    // Меню в конце игры
    public void onEnd(final ChessType winColor) {

        if (endScreen == null) {

            endScreen = new EndScreen(winColor);

            getChildren().add(endScreen);

        } else {

            endScreen.show(winColor);

        }

    }

    // Добавление ссылки на панель управления
    public void addBoard(final BoardPane newBoard) {

        board = newBoard;

    }

}
