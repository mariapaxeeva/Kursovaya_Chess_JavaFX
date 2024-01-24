// Абстрактный класс для всех шахматных фигур

package org.Pakseeva_Kachura.chess.board.pieces;

import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;

import org.Pakseeva_Kachura.chess.model.ChessType;

import org.Pakseeva_Kachura.chess.board.ChessList;
import org.Pakseeva_Kachura.chess.board.ui.SelectionRect;
import org.Pakseeva_Kachura.chess.board.animation.FadeAnimation;
import org.Pakseeva_Kachura.chess.board.animation.RecallAnimation;

import org.Pakseeva_Kachura.chess.media.Texture;
import org.Pakseeva_Kachura.chess.media.TextureAtlas;


public abstract class ChessPiece extends Pane {

    protected final ChessType type;
    protected final ChessType color;
    protected final ChessList list;

    private final int originX;
    private final int originY;

    protected int x = 0;
    protected int y = 0;

    protected int xDist = 0;
    protected int yDist = 0;

    protected int iterX = 0;
    protected int iterY = 0;

    protected boolean hasMoved = false;
    protected boolean isSelected = false;

    private ImageView chessView;
    private SelectionRect selectRect;

    private FadeAnimation fadeAnim;
    private RecallAnimation recallAnim;


    public ChessPiece(final ChessList list,
                      final ChessType type,
                      final ChessType color,
                      final int x, final int y) {

        setPrefSize(32, 32);
        relocate(x * 32, y * 32);

        this.type = type;
        this.color = color;
        this.originX = x;
        this.originY = y;
        this.x = x;
        this.y = y;

        this.list = list;
        this.list.addEntity(this);

        ChessType.validateColor(color);

        // Добавление изображение фигуры в TextureAtlas
        chessView = TextureAtlas.getTexture(
            Texture.CHESS_PIECES,
            type.getCoordinate(),
            color.getCoordinate()
        );

        getChildren().add(chessView);

    }

    // Расчет расстояния от текущей позиции до новой позиции.
    protected void calculateDistance(final int targetX, final int targetY) {

        xDist = targetX - x;
        yDist = targetY - y;

    }

    // Метод для поиска препятствий (фигур), которые могут блокировать путь на желаемую клетку
    // Возвращает значение true, если препятствие найдено, значение false, если препятсвий нет
    protected boolean findBlockingPieces(final int targetX, final int targetY) {

        iterX = x;
        iterY = y;

        while (iterX != targetX || iterY != targetY) {

            if (iterX > targetX) {

                iterX--;

            } else if (iterX < targetX) {

                iterX++;

            }

            if (iterY > targetY) {

                iterY--;

            } else if (iterY < targetY) {

                iterY++;

            }

            // Проверка, что на данной клетке не стоит фигура
            if (iterX != targetX || iterY != targetY) {

                if (list.findChessPiece(iterX, iterY) != null) {

                    return true;

                }

            }

        }

        return false;

    }

    // Метод, реализующий выполнение хода
    protected void doMove(final int targetX, final int targetY, final ChessPiece toKill) {

        hasMoved = true;
        isSelected = false;

        relocate(targetX * 32, targetY * 32);

        if (toKill != null) {

            list.killPiece(toKill);

        }

        x = targetX;
        y = targetY;

        list.pushChange(this);

        if (selectRect != null) {

            selectRect.hideNoAnim();

        }

    }

    /*
      Проверка хода, совершение хода и обновление фигуры
      (реализуется для каждой фигуры по-разному)
    */
    public abstract boolean validateMove(int targetX, int targetY);
    public abstract void confirmMove(int targetX, int targetY);
    public abstract void update(ChessPiece changedPiece);

    // Выбор и отмена выбора фигуры
    public void setSelected(final boolean selected) {

        if (isSelected != selected) {

            if (selected) {

                toFront();

                /*
                Сброс координат и проверка текущего положения фигур для корректной работы
                в случаях, если фигура выбрана после возврата в исходное положение
                */
                setTranslateX(0);
                setTranslateY(0);
                relocate(x * 32, y * 32);

                // Создание прямоугольника - выделения, если он еще не создан
                if (selectRect == null) {

                    selectRect = new SelectionRect(color, 0, 0);

                    getChildren().add(selectRect);

                } else {

                    selectRect.show(color);

                }

            } else {

                selectRect.hide();

            }

            isSelected = selected;

        }

    }

    // Сброс хода фигуры
    public void reset() {

        if (hasMoved) {

            x = originX;
            y = originY;

            hasMoved = false;

            recall();

        }

        setSelected(false);

    }

    // Метод используется для удаления фигуры при её взятии
    public void kill() {

        if (fadeAnim == null) {

            fadeAnim = new FadeAnimation(this);

        }

        fadeAnim.fadeOut();
        list.removeEntity(this);

    }

    // Повторное добавление фигуры в основной список шахматных фигур
    public void unkill() {

        reset();

        if (fadeAnim == null) {

            fadeAnim = new FadeAnimation(this);

        }

        fadeAnim.fadeIn();
        list.addEntity(this);

    }

    // Возврат шахматной фигуры на прежнее место
    public void recall() {

        if (recallAnim == null) {

            recallAnim = new RecallAnimation(this);

        }

        recallAnim.start((int) getLayoutX(), (int) getLayoutY(), x * 32, y * 32);

        setSelected(false);

    }

    // Определить, находится ли шахматная фигура в определенной позиции
    // Возвращает значение true, если да, false, если нет
    public boolean isAt(final int atX, final int atY) {

        return atX == x && atY == y;

    }

    public void offsetView(final int offset) {

        chessView.setLayoutX(chessView.getLayoutX() - offset);

    }

    public ChessType getType() {

        return type;

    }

    public ChessType getColor() {

        return color;

    }

    public int getX() {

        return x;

    }

    public int getY() {

        return y;

    }

    public boolean getMoved() {

        return hasMoved;

    }

}
