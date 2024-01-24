// Главная сцена

package org.Pakseeva_Kachura.chess;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

import org.Pakseeva_Kachura.chess.board.BoardPane;
import org.Pakseeva_Kachura.chess.menu.MenuPane;

public class ChessScene extends Scene {

    public ChessScene(final Group root) {

        super(root, 322, 354);

        // Фон
        setFill(new LinearGradient(
            20, 140, 20, 355, false,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("C19A6B")),
            new Stop(1, Color.web("eaeaee"))
        ));

        MenuPane menu = new MenuPane();
        BoardPane board = new BoardPane(menu);

        root.getChildren().addAll(menu, board);

    }

}