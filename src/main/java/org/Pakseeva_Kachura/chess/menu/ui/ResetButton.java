// Кнопка перезагрузки игры

package org.Pakseeva_Kachura.chess.menu.ui;

import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.media.Texture;
import org.Pakseeva_Kachura.chess.media.TextureAtlas;

import org.Pakseeva_Kachura.chess.board.ui.SelectionRect;
import org.Pakseeva_Kachura.chess.menu.MenuPane;

public class ResetButton extends Pane {

    private MenuPane parent;
    private SelectionRect selectRect = null;

    public ResetButton(final MenuPane parent) {

        relocate(3, 3);
        setPrefSize(32, 32);
        setOnMouseClicked(confirmHandler);
        setOnMouseEntered(enterHandler);
        setOnMouseExited(exitHandler);

        this.parent = parent;

        getChildren().add(TextureAtlas.getTexture(Texture.RESET, 0, 0));

    }

    // Обработчики действий пользователя

    private EventHandler<MouseEvent> enterHandler = event -> {

        if (selectRect == null) {

            selectRect = new SelectionRect(ChessType.WHITE, 0, 0);

            getChildren().add(selectRect);

        } else {

            selectRect.show(ChessType.WHITE);

        }

    };

    private EventHandler<MouseEvent> exitHandler = event -> {

        if (selectRect != null) {

            selectRect.hide();

        }

    };

    private EventHandler<MouseEvent> confirmHandler = event -> {

        if (event.getButton() == MouseButton.PRIMARY) {

            // Call the reset function on BoardPane when clicked
            parent.onReset();

        }

    };

}
