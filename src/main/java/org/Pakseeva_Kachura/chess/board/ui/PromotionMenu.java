// Меню для замены пешки

package org.Pakseeva_Kachura.chess.board.ui;

import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.board.BoardPane;
import org.Pakseeva_Kachura.chess.board.animation.FadeAnimation;

import org.Pakseeva_Kachura.chess.media.Texture;
import org.Pakseeva_Kachura.chess.media.TextureAtlas;

public class PromotionMenu extends Pane {

    private boolean isShown = false;

    private BoardPane parent;
    private ChessType color;

    private SelectionRect selectRect = null;
    private FadeAnimation fadeAnim;
    private ImageView[] choiceViews;

    private int mouseX = 0;
    private int mouseY = 0;
    private int simpleX = 0;
    private int cacheSimpleX = -1;

    public PromotionMenu(final BoardPane parent,
                         final ChessType color,
                         final int x, final int y) {

        setPrefSize(128, 32);
        setOnMouseClicked(confirmHandler);
        setOnMouseMoved(hoverHandler);
        setOnMouseDragged(hoverHandler);

        this.parent = parent;
        this.color = color;

        fadeAnim = new FadeAnimation(this);

        show(color, x, y);

    }

    EventHandler<MouseEvent> confirmHandler = event -> {

        if (event.getButton() == MouseButton.PRIMARY && isShown) {

            normalizePointer(event);

            if (validateXY()) {

                updateSimpleX();

                parent.onPromotionEnd(ChessType.PROMOTION_ORDER[simpleX]);

                hide();

            }

        }

    };

    EventHandler<MouseEvent> hoverHandler = event -> {

        if (isShown) {

            normalizePointer(event);

            if (validateXY()) {

                updateSimpleX();

                // Проверка, что координата мыши изменилась
                if (simpleX != cacheSimpleX) {

                    if (selectRect == null) {

                        selectRect = new SelectionRect(color, simpleX, 0);

                        getChildren().add(selectRect);

                    } else {

                        selectRect.relocate(simpleX * 32, 0);

                        selectRect.show(color);

                    }

                    cacheSimpleX = simpleX;

                }

            }

        }

    };

    // Метод сохраняет указатель мыши в пределах меню
    private void normalizePointer(final MouseEvent event) {

        /*
        | Меню расположено относительно панели управления, поэтому к фактическому
          положению указателя мыши необходимо добавить 33 и 49
        */
        mouseX = (int) (event.getSceneX() - (getLayoutX() + 33));
        mouseY = (int) (event.getSceneY() - (getLayoutY() + 49));

    }

    // Проверка, что координата X не выходит за пределы допустимого значения
    // Возвращает true, если значение допустимо, false, если нет
    private boolean validateXY() {

        return mouseX > 0 && mouseX < getPrefWidth()
            && mouseY > 0 && mouseY < getPrefHeight();

    }

    // Пересчет координаты Х
    private void updateSimpleX() {

        simpleX = mouseX / 32;

    }

    // Показать меню
    public void show(final ChessType newColor, final int x, final int y) {

        ChessType.validateColor(newColor);

        // Меню выравнивается по-разному в зависимости от того, на какой половине доски находится пешка
        if (x < 4) {
            setLayoutX((x * 32));
        } else {
            setLayoutX((x * 32) - 96);
        }

        if (newColor == ChessType.WHITE) {
            setLayoutY((y * 32) - 38);
        } else {
            setLayoutY((y * 32) + 38);
        }

        createViews(newColor);
        fadeAnim.fadeIn();

        color = newColor;
        cacheSimpleX = -1;
        isShown = true;

    }

    // Создание меню из фигур
    private void createViews(final ChessType newColor) {

        if (newColor != color || choiceViews == null) {

            if (choiceViews != null) {

                getChildren().removeAll(choiceViews);

            }

            choiceViews = new ImageView[4];

            for (int i = 0; i < 4; i++) {

                // Добавление изображения для каждой доступной фигуры
                choiceViews[i] = TextureAtlas.getTexture(
                    Texture.CHESS_PIECES,
                    ChessType.PROMOTION_ORDER[i].getCoordinate(),
                    newColor.getCoordinate()
                );

                choiceViews[i].setX(i * 32);

            }

            getChildren().addAll(choiceViews);

        }

    }

    // Скрыть меню
    public void hide() {

        if (isShown) {

            if (selectRect != null) {

                selectRect.hide();

            }

            toBack();
            fadeAnim.fadeOut();
            isShown = false;

        }

    }

}
