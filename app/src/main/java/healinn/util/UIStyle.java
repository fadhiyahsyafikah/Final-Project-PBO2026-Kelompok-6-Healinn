package healinn.util;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class UIStyle {
    //Palet warna
    public static final String BG_LIGHT = "#c4cdb8";
    public static final String BG_DARK = "#8fa887";
    public static final String CARD_DARK = "#1e0f05";
    public static final String CARD_MED = "#2b1a0e";
    public static final String GOLD = "#c9a84c";
    public static final String TEXT_LIGHT = "#f0ebe1";
    public static final String TEXT_DARK = "#2b1a0e";
    public static final String TEXT_MUTED = "#6b5a4e";
    public static final String AVAILABLE = "#4caf50";
    public static final String OCCUPIED = "#e53935";
    public static final String BTN_OUTLINE = "#2b1a0e";

    //Background
    public static Background gradientBackground() {
        LinearGradient lg = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web(BG_DARK)),
            new Stop(1.0, Color.web(BG_LIGHT))
        );
        return new Background(new BackgroundFill(lg, CornerRadii.EMPTY, Insets.EMPTY));
    }

    public static Background darkBackground() {
        return new Background(new BackgroundFill(
            Color.web(CARD_DARK), CornerRadii.EMPTY, Insets.EMPTY));
    }

    //Helper Hover
    public static void applyHoverEffect(Button btn, String hoverStyle, String normalStyle) {
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
    }
}