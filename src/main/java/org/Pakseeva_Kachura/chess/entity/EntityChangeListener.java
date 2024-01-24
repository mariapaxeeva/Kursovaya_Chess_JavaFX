// Интерфейс для работы с изменениями в списке фигур

package org.Pakseeva_Kachura.chess.entity;

public interface EntityChangeListener<T> {

    void onAdded(T added);
    void onRemoved(T removed);

}
