// Абстрактный класс списка шахматных фигур

package org.Pakseeva_Kachura.chess.entity;

import java.util.ArrayList;

public abstract class EntityList<T> {

    protected ArrayList<T> entities;

    protected EntityChangeListener<T> listener = null;

    public EntityList(final EntityChangeListener<T> listener) {

        this.entities = new ArrayList<T>();
        this.listener = listener;

    }

    // Управление фигурами
    public void addEntity(final T entity) {

        entities.add(entity);

        listener.onAdded(entity);

    }

    public void removeEntity(final T entity) {

        // Уведомление listener об удалении фигуры до тех пор, пока фигура удалена
        if (entities.remove(entity) && listener != null) {

            listener.onRemoved(entity);

        }

    }

    public boolean hasEntity(final T entity) {

        return entities.contains(entity);

    }

    public ArrayList<T> getEntities() {

        return entities;

    }

}
