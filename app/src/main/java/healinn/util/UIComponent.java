package healinn.util;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class UIComponent {
    //Label & Text
    public static Label hotelTitle() {
        Label lbl = new Label("HEALINN HOTEL");
        lbl.setFont(Font.font("Georgia", FontWeight.BOLD, 72));
        lbl.setTextFill(Color.web(UIStyle.TEXT_DARK));
        return lbl;
    }

    public static Label hotelSubtitle() {
        Label lbl = new Label("HOTEL & CONVENTION CENTER");
        lbl.setFont(Font.font("Georgia", FontWeight.NORMAL, 16));
        lbl.setTextFill(Color.web(UIStyle.TEXT_MUTED));
        lbl.setStyle("-fx-letter-spacing: 3px;");
        return lbl;
    }

    public static Label pageTitle(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        lbl.setTextFill(Color.web(UIStyle.TEXT_DARK));
        lbl.setStyle("-fx-text-fill: #3d2510 !important;");
        return lbl;
    }

    public static Label sectionTitle(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Georgia", FontWeight.NORMAL, 25));
        lbl.setTextFill(Color.web(UIStyle.TEXT_DARK));
        return lbl;
    }

    public static Label formTitle(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        lbl.setTextFill(Color.web(UIStyle.TEXT_DARK));
        lbl.setPadding(new Insets(0, 0, 8, 0));
        return lbl;
    }

    public static Label lightLabel(String text, double size) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Georgia", size));
        lbl.setTextFill(Color.web(UIStyle.TEXT_LIGHT));
        return lbl;
    }

    public static Label goldLabel(String text, double size) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Georgia", FontWeight.BOLD, size));
        lbl.setTextFill(Color.web(UIStyle.GOLD));
        return lbl;
    }

    public static Label mutedLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Georgia", FontPosture.ITALIC, 18));
        lbl.setTextFill(Color.web(UIStyle.TEXT_MUTED));
        return lbl;
    }

    public static Line dividerGold(double width) {
        Line line = new Line(0, 0, width, 0);
        line.setStroke(Color.web(UIStyle.GOLD));
        line.setStrokeWidth(1.2);
        return line;
    }

    public static Line dividerDark(double width) {
        Line line = new Line(0, 0, width, 0);
        line.setStroke(Color.web(UIStyle.TEXT_DARK));
        line.setStrokeWidth(0.8);
        return line;
    }

    //Tombol
    public static Button outlineButton(String text, double width) {
        Button btn = new Button(text);
        btn.setPrefWidth(width);
        btn.setPrefHeight(52);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        btn.setTextFill(Color.web(UIStyle.TEXT_DARK));
        btn.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + UIStyle.BTN_OUTLINE + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-cursor: hand;"
        );
        UIStyle.applyHoverEffect(btn,
            "-fx-background-color: #f0ebe1; -fx-border-color:" + UIStyle.BTN_OUTLINE + "; -fx-border-width:2; -fx-border-radius:30; -fx-background-radius:30; -fx-cursor:hand;",
            "-fx-background-color: white; -fx-border-color:" + UIStyle.BTN_OUTLINE + "; -fx-border-width:2; -fx-border-radius:30; -fx-background-radius:30; -fx-cursor:hand;"
        );
        return btn;
    }

    public static Button darkButton(String text, double width) {
        Button btn = new Button(text);
        btn.setPrefWidth(width);
        btn.setPrefHeight(52);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        btn.setTextFill(Color.web(UIStyle.TEXT_LIGHT));
        btn.setStyle(
            "-fx-background-color: " + UIStyle.CARD_MED + ";" +
            "-fx-border-color: white;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-cursor: hand;"
        );
        UIStyle.applyHoverEffect(btn,
            "-fx-background-color: #3d2510; -fx-border-color:white; -fx-border-width:1.5; -fx-border-radius:30; -fx-background-radius:30; -fx-cursor:hand;",
            "-fx-background-color: " + UIStyle.CARD_MED + "; -fx-border-color:white; -fx-border-width:1.5; -fx-border-radius:30; -fx-background-radius:30; -fx-cursor:hand;"
        );
        return btn;
    }

    public static Button goldButton(String text, double width) {
        Button btn = new Button(text);
        btn.setPrefWidth(width);
        btn.setPrefHeight(56);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        btn.setTextFill(Color.web(UIStyle.TEXT_DARK));
        btn.setStyle(
            "-fx-background-color: " + UIStyle.GOLD + ";" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-cursor: hand;"
        );
        UIStyle.applyHoverEffect(btn,
            "-fx-background-color: #b8943c; -fx-border-radius:30; -fx-background-radius:30; -fx-cursor:hand;",
            "-fx-background-color: " + UIStyle.GOLD + "; -fx-border-radius:30; -fx-background-radius:30; -fx-cursor:hand;"
        );
        return btn;
    }

    public static Button sidebarButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setPrefHeight(44);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        if (active) {
            btn.setTextFill(Color.web(UIStyle.TEXT_LIGHT));
            btn.setStyle(
                "-fx-background-color: " + UIStyle.CARD_DARK + ";" +
                "-fx-border-radius: 22; -fx-background-radius: 22; -fx-cursor: hand;"
            );
        } else {
            btn.setTextFill(Color.web(UIStyle.TEXT_DARK));
            btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: " + UIStyle.BTN_OUTLINE + ";" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 22; -fx-background-radius: 22; -fx-cursor: hand;"
            );
        }
        return btn;
    }

    public static Button reservasiButton() {
        Button btn = new Button("Reservasi");
        btn.setPrefWidth(200);
        btn.setPrefHeight(38);
        btn.setFont(Font.font("Georgia", FontWeight.NORMAL, 14));
        btn.setTextFill(Color.web(UIStyle.TEXT_DARK));
        btn.setStyle(
            "-fx-background-color: " + UIStyle.GOLD + ";" +
            "-fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand;"
        );
        return btn;
    }

    //Inputan
    public static TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(400);
        tf.setPrefHeight(50);
        tf.setFont(Font.font("Georgia", 16));
        tf.setStyle(
            "-fx-background-color: " + UIStyle.CARD_MED + ";" +
            "-fx-text-fill: " + UIStyle.TEXT_LIGHT + ";" +
            "-fx-prompt-text-fill: #8a7060;" +
            "-fx-border-color: white;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 8 12 8 12;"
        );
        return tf;
    }

    public static TextField lightTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(760);
        tf.setPrefHeight(46);
        tf.setFont(Font.font("Georgia", 15));
        tf.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: " + UIStyle.TEXT_DARK + ";" +
            "-fx-prompt-text-fill: #9e9e9e;" +
            "-fx-border-color: #bbb;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 12 8 12;"
        );
        return tf;
    }

    public static PasswordField styledPasswordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setPrefWidth(400);
        pf.setPrefHeight(50);
        pf.setFont(Font.font("Georgia", 16));
        pf.setStyle(
            "-fx-background-color: " + UIStyle.CARD_MED + ";" +
            "-fx-text-fill: " + UIStyle.TEXT_LIGHT + ";" +
            "-fx-prompt-text-fill: #8a7060;" +
            "-fx-border-color: white;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 8 12 8 12;"
        );
        return pf;
    }
}