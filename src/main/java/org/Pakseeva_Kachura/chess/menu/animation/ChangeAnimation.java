// Анимация при изменении TurnIndicator

package org.Pakseeva_Kachura.chess.menu.animation;

import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.animation.FillTransition;

import org.Pakseeva_Kachura.chess.menu.ui.TurnIndicator;

public class ChangeAnimation {

    private FillTransition internalAnim;

    private Color color;

    public ChangeAnimation(final TurnIndicator indicator) {

        internalAnim = new FillTransition(Duration.seconds(0.2), indicator);

    }

    public void change(final Color newColor) {

        internalAnim.stop();

        internalAnim.setFromValue(color);
        internalAnim.setToValue(newColor);

        internalAnim.play();

        color = newColor;

    }

}
