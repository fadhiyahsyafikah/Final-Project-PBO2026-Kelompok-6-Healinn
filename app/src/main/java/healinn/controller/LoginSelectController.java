package healinn.controller;

import healinn.util.SceneManager;
import healinn.util.UIComponent;
import healinn.util.UILayout;
import healinn.util.UIStyle;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class LoginSelectController {
    public Pane createScene(){
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setBackground(UIStyle.gradientBackground());

        Label welcome = new Label("Welcome to");
        welcome.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 24px; -fx-text-fill: " + UIStyle.TEXT_DARK + ";");

        VBox logobox = UILayout.buildLogoBox(false,false);
        Label title = (Label) logobox.getChildren().get(0);
        title.setStyle("-fx-font-size: 72px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        Button btnCustomer = UIComponent.outlineButton("CUSTOMER", 300);
        Button btnAdmin = UIComponent.darkButton("ADMIN", 300);

        btnCustomer.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneManager.SCENE_CUSTOMER_AUTH));
        btnAdmin.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneManager.SCENE_ADMIN_LOGIN));

        root.getChildren().addAll(welcome, logobox, btnCustomer, btnAdmin);
        return root;
    }
} 
