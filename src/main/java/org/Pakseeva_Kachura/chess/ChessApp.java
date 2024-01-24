// Главное окно игры

package org.Pakseeva_Kachura.chess;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;

import org.Pakseeva_Kachura.chess.media.Texture;
import org.Pakseeva_Kachura.chess.media.TextureAtlas;


public class ChessApp extends Application {

    // Инициализация
    public void start(final Stage primaryStage) {


        primaryStage.setTitle("Шахматы");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(
            TextureAtlas.loadFullTexture(Texture.ICON)
        );

        primaryStage.setScene(new ChessScene(new Group()));
        primaryStage.show();

    }

    public static void run(final String[] args) {

        launch();

    }

}
