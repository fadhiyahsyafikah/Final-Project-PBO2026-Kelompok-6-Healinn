package healinn.controller;

import healinn.model.User;
import healinn.service.AccService;
import healinn.util.SceneManager;
import healinn.util.UIComponent;
import healinn.util.UILayout;
import healinn.util.UIStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AdminLoginController {
    private final AccService accService = new AccService();

    public Pane createScene() {
        StackPane root = new StackPane();
        root.setBackground(UIStyle.gradientBackground());

        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(480);
        card.setPadding(new Insets(48));
        card.setStyle(
            "-fx-background-color:" + UIStyle.CARD_DARK + ";" +
            "-fx-background-radius:28;" +
            "-fx-border-color:" + UIStyle.GOLD + ";" +
            "-fx-border-width:1;-fx-border-radius:28;"
        );

        VBox logo = UILayout.buildLogoBox(true, true);

        TextField tfUser = UIComponent.styledTextField("Username Admin");
        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText("Password");
        tfPass.setPrefWidth(400); tfPass.setPrefHeight(50);
        tfPass.setStyle(
            "-fx-background-color:" + UIStyle.CARD_MED + ";" +
            "-fx-text-fill:" + UIStyle.TEXT_LIGHT + ";" +
            "-fx-prompt-text-fill:#8a7060;" +
            "-fx-border-color:white;-fx-border-width:1.5;" +
            "-fx-border-radius:10;-fx-background-radius:10;-fx-padding:8 12 8 12;");

        Label errLbl = new Label();
        errLbl.setStyle("-fx-text-fill:#e53935;-fx-font-size:12px;");
        errLbl.setVisible(false);

        Button btnLogin = UIComponent.goldButton("MASUK SEBAGAI ADMIN", 400);
        btnLogin.setOnAction(e -> {
            User user = accService.login(tfUser.getText().trim(), tfPass.getText());
            if (user == null || !user.getRole().equals("ADMIN")) {
                errLbl.setText("Kredensial admin tidak valid.");
                errLbl.setVisible(true);
            } else {
                errLbl.setVisible(false);
                SceneManager.getInstance().navigateRoot(SceneManager.SCENE_ADMIN_STATUS);
            }
        });
        tfPass.setOnAction(e -> btnLogin.fire());

        Button btnBack = UIComponent.darkButton("KEMBALI", 400);
        btnBack.setOnAction(e -> SceneManager.getInstance().goBack());

        card.getChildren().addAll(logo, tfUser, tfPass, errLbl, btnLogin, btnBack);
        root.getChildren().add(card);
        return root;
    }
}
