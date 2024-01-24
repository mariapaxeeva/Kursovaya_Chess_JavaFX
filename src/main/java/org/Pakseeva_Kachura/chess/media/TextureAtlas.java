// Работа с изображениями

package org.Pakseeva_Kachura.chess.media;

import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

public final class TextureAtlas {

    private TextureAtlas() {    }

    // Размеры изображений по умолчанию
    private static final int DEF_WIDTH = 32;
    private static final int DEF_HEIGHT = 32;

    private static HashMap<Texture, Image> loadedImages = new HashMap<Texture, Image>();

    // Метод возвращает текстуру с учетом координат X/Y и размера по умолчанию
    public static ImageView getTexture(final Texture tex, final int x, final int y) {

        ImageView view = new ImageView(loadFullTexture(tex));

        view.setViewport(new Rectangle2D(
            x * DEF_WIDTH, y * DEF_HEIGHT, DEF_WIDTH, DEF_HEIGHT
        ));

        return view;

    }

    // Метод возвращает текстуру с учетом координат X/Y и пользовательского размера
    public static ImageView getTexture(final Texture tex,
                                       final int x, final int y,
                                       final int w, final int h) {

        ImageView view = new ImageView(loadFullTexture(tex));

        view.setViewport(new Rectangle2D(
            x * w, y * h, w, h
        ));

        return view;

    }

    // Метод возвращает текстуру с учетом координат X/Y, пользовательского размера и перемещения
    public static ImageView getTexture(final Texture tex,
                                       final int x, final int y,
                                       final int w, final int h,
                                       final int rx, final int ry) {

        ImageView view = new ImageView(loadFullTexture(tex));

        view.setViewport(new Rectangle2D(
            x * w, y * h, w, h
        ));

        view.relocate(rx, ry);

        return view;

    }

    // Метод возвращает полное изображение
    public static Image loadFullTexture(final Texture tex) {

        // Check if image is loaded, load it if not
        if (!loadedImages.containsKey(tex)) {

            loadedImages.put(
                tex,
                new Image(TextureAtlas.class.getResource(tex.getPath()).toString())
            );

        }

        return loadedImages.get(tex);

    }

}
