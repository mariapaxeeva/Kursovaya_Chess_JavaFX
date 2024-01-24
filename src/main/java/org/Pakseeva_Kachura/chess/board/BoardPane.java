// Панель, на которой размещены шахматная доска с фигурами

package org.Pakseeva_Kachura.chess.board;

import java.util.Random;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.board.pieces.ChessPiece;
import org.Pakseeva_Kachura.chess.board.pieces.ChessFactory;
import org.Pakseeva_Kachura.chess.board.ui.PromotionMenu;

import org.Pakseeva_Kachura.chess.menu.MenuPane;

import org.Pakseeva_Kachura.chess.media.Texture;
import org.Pakseeva_Kachura.chess.media.TextureAtlas;


import org.Pakseeva_Kachura.chess.entity.EntityChangeListener;

public class BoardPane extends Pane implements EntityChangeListener<ChessPiece> {

    private ChessList pieces;
    private ChessFactory factory;

    private MenuPane menu;

    private ChessPiece selectedPiece = null;
    private ChessPiece promotedPiece = null;

    private PromotionMenu promotionMenu = null;

    private ChessType turn = ChessType.WHITE;

    private boolean hasMoved = false;

    private int mouseX = 0;
    private int mouseY = 0;
    private int simpleX = 0;
    private int simpleY = 0;
    private int selX = 0;
    private int selY = 0;

    private Random rand;

    private boolean isDisabled = false;

    public BoardPane(final MenuPane menu) {

        relocate(33, 49);
        setPrefSize(256, 256);
        setOnMouseDragged(dragHandler);
        setOnMousePressed(pressHandler);
        setOnMouseReleased(releaseHandler);

        this.menu = menu;
        this.menu.addBoard(this);

        rand = new Random();
        pieces = new ChessList(this);
        factory = new ChessFactory(pieces, this);

        getChildren().add(
            TextureAtlas.getTexture(
                Texture.BORDER,
                0, 0, 268, 268, -6, -6
            )
        );

        generateCheckerBoard();
        generateChessPieces();

    }

    // УПРАВЛЕНИЕ МЫШЬЮ и ПЕРЕМЕЩЕНИЕ

    // Начало перетаскивания
    private EventHandler<MouseEvent> pressHandler = event -> {

        if (event.getButton() == MouseButton.PRIMARY && !isDisabled) {

            normalizePointer(event);

            if (validateXY()) {

                updateSimpleXY();

                selectedPiece = pieces.findChessPiece(turn, simpleX, simpleY);

                if (selectedPiece != null) {

                    selectedPiece.setSelected(true);

                    /*
                    Сохранение положения мыши относительно квадрата, в котором она находится,
                    для перемещения фигуры в зависимости от положения мыши, а не верхнего левого угла шахматной фигуры.
                    */
                    selX = mouseX % 32;
                    selY = mouseY % 32;

                }

            }

        }

    };

    // Перемещение фигуры
    private EventHandler<MouseEvent> dragHandler = event -> {

        // Игнорирование, если фигура не выбрана
        if (selectedPiece != null) {

            normalizePointer(event);

            /*
            Перемещение выбранный фигуры в новое местоположение указателя, пока оно остается доступным.
            По одной оси все еще можно перемещаться, если другая недоступна.
            */
            if (validateDragX()) {

                selectedPiece.setLayoutX(mouseX - selX);

            } else {

                /*
                Если желаемая клетка недоступна, то проверка, полностью ли указатель мыши находится в пределах доски,
                и по умолчанию установка местоположения, в котором он должен находиться,
                */
                if (mouseX < 0) {

                    selectedPiece.setLayoutX(0);

                } else if (mouseX > 256) {

                    selectedPiece.setLayoutX(224);

                }

            }

            if (validateDragY()) {

                selectedPiece.setLayoutY(mouseY - selY);

            } else {

                if (mouseY < 0) {

                    selectedPiece.setLayoutY(0);

                } else if (mouseY > 256) {

                    selectedPiece.setLayoutY(224);

                }

            }

        }

    };

    // Перемещение
    private EventHandler<MouseEvent> releaseHandler = event -> {

        if (selectedPiece != null) {

            normalizePointer(event);

            /*
            Осуществление перемещения, если координаты X/Y доступны и
            само перемещение соответсвует правилам.
            */
            if (validateXY() || validateDrag()) {

                updateSimpleXY();

                if (pieces.findChessPiece(turn, simpleX, simpleY) == null
                &&  selectedPiece.validateMove(simpleX, simpleY)
                && !selectedPiece.isAt(simpleX, simpleY)) {

                    doMove();

                    return;

                }

            }

            // В случае невозможности перемещения - возврат фигуры на исходную клетку
            selectedPiece.recall();

            selectedPiece = null;

        }

    };

    // Расположение указателя мыши так, чтобы координаты находились в пределах BoardPane
    private void normalizePointer(final MouseEvent event) {

        mouseX = (int) (event.getSceneX() - getLayoutX());
        mouseY = (int) (event.getSceneY() - getLayoutY());

    }

    // Проверка, что координаты X/Y не выходят за пределы допустимых значений
    // Возвращает true, если допустимо, false, если нет
    private boolean validateXY() {

        return mouseX > 0 && mouseX < getPrefWidth()
            && mouseY > 0 && mouseY < getPrefHeight();

    }

    // Проверка, что перетаскиваемая в данный момент фигура все еще находится в пределах BoardPane
    // Возвращает true, если допустимо, false, если нет
    private boolean validateDrag() {

        return validateDragX() && validateDragY();

    }

    private boolean validateDragX() {

        return (32 - selX) + mouseX >= 32 && (32 - selX) + mouseX <= 256;

    }

    private boolean validateDragY() {

        return (32 - selY) + mouseY >= 32 && (32 - selY) + mouseY <= 256;

    }

    // Расчет координат из координат мыши
    private void updateSimpleXY() {

        simpleX = mouseX / 32;
        simpleY = mouseY / 32;

    }

    // Совершение хода
    private void doMove() {

        selectedPiece.confirmMove(simpleX, simpleY);

        /*
        | Если пешка только что продвинулась вперед, дойдя до конца доски,
         доска отключается и появляются фигуры за пределами доски, чтобы заменить пешку.
        */
        if (selectedPiece.getType() == ChessType.PAWN) {

            if (selectedPiece.getY() == 0 || selectedPiece.getY() == 7) {

                promotedPiece = selectedPiece;
                isDisabled = true;

                if (promotionMenu == null) {

                    promotionMenu = new PromotionMenu(
                        this,
                        promotedPiece.getColor(),
                        promotedPiece.getX(), promotedPiece.getY()
                    );

                    getChildren().add(promotionMenu);

                } else {

                    promotionMenu.show(
                        promotedPiece.getColor(),
                        promotedPiece.getX(), promotedPiece.getY()
                    );

                }

            }

        }

        if (promotedPiece == null) {

            changeTurn();

        }

        hasMoved = true;
        selectedPiece = null;

    }


    // Замена пешки, когда она доходит до конца доски
    public void onPromotionEnd(final ChessType newType) {

        /*
        Удаление исходной пешки и ее замена фигурой, которая была выбрана в меню.
        Завершение хода игрока, т.к. этого не произошло, когда пешка была первоначально перемещена.
        */
        factory.setColor(turn);
        factory.promote(promotedPiece.getX(), promotedPiece.getY(), newType);

        pieces.addPromotedPiece(pieces.findChessPiece(promotedPiece.getX(), promotedPiece.getY()));

        promotedPiece = null;
        isDisabled = false;

        changeTurn();

    };

    // Сброс
    public void onReset() {

        // Не выполняется, если не было совершено никаких действий
        if (hasMoved) {

            //Сброс шахматных фигур/ссылок на нмх, очистка всех значений и скрытие меню
            factory.replaceKilled();
            pieces.resetAll();

            if (promotionMenu != null) {

                promotionMenu.hide();

            }

            hasMoved = false;
            isDisabled = false;

            selectedPiece = null;
            promotedPiece = null;

            randomizeTurn();

        }

    };

    // Конец игры
    public void onEnd(final ChessType winColor) {

        menu.onEnd(winColor);

        isDisabled = true;
        promotedPiece = null;

        if (promotionMenu != null) {

            promotionMenu.hide();

        }

    }

    // УПРАВЛЕНИЕ ОБЪЕКТАМИ

    // Создание шахматной доски
    private void generateCheckerBoard() {

        Rectangle checkerRect;

        for (int x = 0; x <= 224; x += 32) {

            for (int y = 0; y <= 224; y += 32) {

                checkerRect = new Rectangle(x, y, 32, 32);

                /*
                | Если координаты [например, 4X + 7Y] делятся на два, то клетка должна быть
                 светлой, в противном случае темной.
                */
                if (((x / 32) + (y / 32)) % 2 == 0) {

                     checkerRect.setFill(Color.web("D2B48C"));

                } else {

                    checkerRect.setFill(Color.web("B87333"));

                }

                getChildren().add(checkerRect);

            }

        }

    }

    // Создание шахматных фигур
    private void generateChessPieces() {

        //  Определение очередности хода случайным образом.
        randomizeTurn();

        // Создание фигур каждого цвета
        factory.setColor(ChessType.BLACK);
        factory.genHomeRow(0);
        factory.genPawnRow(1);

        factory.setColor(ChessType.WHITE);
        factory.genPawnRow(6);
        factory.genHomeRow(7);

    }

    public void randomizeTurn() {

        if (rand.nextBoolean()) {

            turn = ChessType.WHITE;

        } else {

            turn = ChessType.BLACK;

        }

        menu.changeTurn(turn);

    }

    // Смена хода
    public void changeTurn() {

        turn = ChessType.inverseOf(turn);

        menu.changeTurn(turn);

    }

    // Добавление объекта
    public void onAdded(final ChessPiece added) {

        getChildren().add(added);

    }

    // Удаление объекта
    public void onRemoved(final ChessPiece removed) {

        getChildren().remove(removed);

    }

}
