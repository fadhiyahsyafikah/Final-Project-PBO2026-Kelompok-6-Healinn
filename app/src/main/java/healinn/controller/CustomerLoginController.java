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

public class CustomerLoginController {
    private final AccService accService = new AccService();

    public Pane createScene() {
        StackPane root = new StackPane();
        root.setBackground(UIStyle.gradientBackground());

        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(520);
        card.setPadding(new Insets(40));
        card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:30;" +
            "-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.12),12,0,0,6);"
        );

        VBox logo = UILayout.buildLogoBox(false, false);

        // tombol login / sign up
        HBox tabBar = new HBox(0);
        tabBar.setMaxWidth(440);

        Button tabLogin = tabBtn("MASUK");
        Button tabSignup = tabBtn("DAFTAR");
        tabLogin.setPrefWidth(220);
        tabSignup.setPrefWidth(220);
        tabBar.getChildren().addAll(tabLogin, tabSignup);

        // login
        VBox formLogin = buildLoginForm();
        // signup
        VBox formSignup = buildSignupForm();
        formSignup.setVisible(false);
        formSignup.setManaged(false);

        setTabActive(tabLogin);
        setTabInactive(tabSignup);

        tabLogin.setOnAction(e -> {
            setTabActive(tabLogin); setTabInactive(tabSignup);
            formLogin.setVisible(true);  formLogin.setManaged(true);
            formSignup.setVisible(false); formSignup.setManaged(false);
        });
        tabSignup.setOnAction(e -> {
            setTabActive(tabSignup); setTabInactive(tabLogin);
            formSignup.setVisible(true);  formSignup.setManaged(true);
            formLogin.setVisible(false);  formLogin.setManaged(false);
        });

        Button btnBack = UIComponent.darkButton("KEMBALI", 440);
        btnBack.setOnAction(e -> SceneManager.getInstance().goBack());

        card.getChildren().addAll(logo, tabBar, formLogin, formSignup, btnBack);
        root.getChildren().add(card);
        return root;
    }

    private VBox buildLoginForm() {
        VBox form = new VBox(14);
        form.setMaxWidth(440);

        TextField tfUser = UIComponent.lightTextField("Username");
        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText("Password");
        tfPass.setPrefWidth(440); tfPass.setPrefHeight(46);
        tfPass.setStyle("-fx-background-color:white;-fx-border-color:#bbb;" + "-fx-border-radius:6;-fx-background-radius:6;-fx-padding:8 12 8 12;");

        Label errLbl = new Label();
        errLbl.setStyle("-fx-text-fill:#e53935;-fx-font-size:12px;");
        errLbl.setVisible(false);

        Button btnLogin = UIComponent.goldButton("MASUK", 440);
        btnLogin.setOnAction(e -> {
            String u = tfUser.getText().trim();
            String p = tfPass.getText();
            User user = accService.login(u, p);
            if (user == null || !user.getRole().equals("CUSTOMER")) {
                errLbl.setText(user == null
                    ? "Username atau password salah."
                    : "Akun ini bukan akun customer.");
                errLbl.setVisible(true);
            } else {
                errLbl.setVisible(false);
                SceneManager sm = SceneManager.getInstance();
                sm.navigateRoot(SceneManager.SCENE_DASHBOARD_PILIH);
            }
        });
        tfPass.setOnAction(e -> btnLogin.fire());

        form.getChildren().addAll(tfUser, tfPass, errLbl, btnLogin);
        return form;
    }

    private VBox buildSignupForm() {
        VBox form = new VBox(14);
        form.setMaxWidth(440);

        TextField tfUser = UIComponent.lightTextField("Username");
        TextField tfEmail = UIComponent.lightTextField("Email");
        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText("Password (min. 6 karakter)");
        tfPass.setPrefWidth(440); tfPass.setPrefHeight(46);
        tfPass.setStyle("-fx-background-color:white;-fx-border-color:#bbb;" + "-fx-border-radius:6;-fx-background-radius:6;-fx-padding:8 12 8 12;");

        Label errLbl = new Label();
        errLbl.setStyle("-fx-text-fill:#e53935;-fx-font-size:12px;");
        errLbl.setVisible(false);
        Label succLbl = new Label();
        succLbl.setStyle("-fx-text-fill:#4caf50;-fx-font-size:12px;");
        succLbl.setVisible(false);

        Button btnReg = UIComponent.goldButton("DAFTAR SEKARANG", 440);
        btnReg.setOnAction(e -> {
            String err = accService.register(
                tfUser.getText(), tfEmail.getText(), tfPass.getText());
            if (err != null) {
                errLbl.setText(err); errLbl.setVisible(true); succLbl.setVisible(false);
            } else {
                errLbl.setVisible(false);
                succLbl.setText("Akun berhasil dibuat! Silakan masuk.");
                succLbl.setVisible(true);
                tfUser.clear(); tfEmail.clear(); tfPass.clear();
            }
        });

        form.getChildren().addAll(tfUser, tfEmail, tfPass, errLbl, succLbl, btnReg);
        return form;
    }

    private Button tabBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:transparent;-fx-font-family:'Georgia';" +
                   "-fx-font-size:13px;-fx-padding:10 0 10 0;-fx-cursor:hand;" +
                   "-fx-border-color:#ccc;-fx-border-width:0 0 2 0;");
        return b;
    }
    private void setTabActive(Button b) {
        b.setStyle("-fx-background-color:transparent;-fx-font-family:'Georgia';" +
                   "-fx-font-size:13px;-fx-padding:10 0 10 0;-fx-cursor:hand;" +
                   "-fx-text-fill:" + UIStyle.TEXT_DARK + ";" +
                   "-fx-border-color:" + UIStyle.GOLD + ";-fx-border-width:0 0 2 0;");
    }
    private void setTabInactive(Button b) {
        b.setStyle("-fx-background-color:transparent;-fx-font-family:'Georgia';" +
                   "-fx-font-size:13px;-fx-padding:10 0 10 0;-fx-cursor:hand;" +
                   "-fx-text-fill:#999;" +
                   "-fx-border-color:#ccc;-fx-border-width:0 0 2 0;");
    }
}
