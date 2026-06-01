package healinn.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UILayout {
    //Kartu Kamar
    public static VBox roomCard(String roomNumber, String bedType, boolean available, boolean isAdmin) {
        VBox card = new VBox(6);
        card.setPrefSize(120, 110);
        card.setPadding(new Insets(12, 14, 12, 14));
        card.setAlignment(Pos.TOP_LEFT);

        String bgColor = isAdmin && !available ? UIStyle.OCCUPIED : UIStyle.CARD_DARK;
        card.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: " + (available && !isAdmin ? "hand" : "default") + ";"
        );

        Label numLbl = new Label("No. " + roomNumber);
        numLbl.setFont(Font.font("Georgia", FontWeight.NORMAL, 13));
        numLbl.setTextFill(Color.web(UIStyle.TEXT_LIGHT));

        Label bedLbl = new Label(bedType);
        bedLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
        bedLbl.setTextFill(Color.web(UIStyle.TEXT_LIGHT));

        card.getChildren().addAll(numLbl, bedLbl);

        if (!isAdmin) {
            HBox statusRow = new HBox(5);
            statusRow.setAlignment(Pos.CENTER_LEFT);
            Text dot = new Text("●");
            dot.setFill(Color.web(available ? UIStyle.AVAILABLE : UIStyle.OCCUPIED));
            dot.setFont(Font.font(10));
            Label statusLbl = new Label(available ? "Tersedia" : "Terisi");
            statusLbl.setFont(Font.font("Georgia", 11));
            statusLbl.setTextFill(Color.web(available ? UIStyle.AVAILABLE : UIStyle.OCCUPIED));
            statusRow.getChildren().addAll(dot, statusLbl);
            card.getChildren().add(statusRow);
        }

        if (available && !isAdmin) {
            card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #3d2510; -fx-background-radius:12; -fx-cursor:hand;"));
            card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: " + UIStyle.CARD_DARK + "; -fx-background-radius:12; -fx-cursor:hand;"));
        }
        return card;
    }

    //Kartu Paket Ballroom
    public static VBox ballroomCard(String duration, String price) {
        VBox card = new VBox(10);
        card.setPrefSize(340, 180);
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: " + UIStyle.CARD_DARK + ";" +
            "-fx-background-radius: 20;"
        );

        Label icon = new Label("🏛");
        icon.setFont(Font.font(36));

        Label durLbl = new Label(duration);
        durLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        durLbl.setTextFill(Color.web(UIStyle.TEXT_LIGHT));

        Label priceLbl = new Label(price);
        priceLbl.setFont(Font.font("Georgia", FontWeight.NORMAL, 15));
        priceLbl.setTextFill(Color.web(UIStyle.TEXT_LIGHT));

        Button resBtn = UIComponent.reservasiButton();

        card.getChildren().addAll(icon, durLbl, priceLbl, resBtn);
        return card;
    }

    //Sidebar Customer
    public static VBox customerSidebar(String activeMenu, String username) {
        VBox sidebar = new VBox(16);
        sidebar.setPrefWidth(260);
        sidebar.setPrefHeight(720);
        sidebar.setPadding(new Insets(24, 20, 24, 20));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setBackground(new Background(new BackgroundFill(Color.web("#f5f0e8"), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox logoBox = buildLogoBox(false, false);
        logoBox.setPadding(new Insets(0, 0, 12, 0));

        Line sep = UIComponent.dividerDark(200);

        Label dashLbl = new Label("DASHBOARD");
        dashLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        dashLbl.setTextFill(Color.web(UIStyle.TEXT_DARK));
        dashLbl.setPadding(new Insets(10, 0, 6, 0));

        Button btnKamar = UIComponent.sidebarButton("  🛏  PILIH KAMAR", "kamar".equals(activeMenu));
        Button btnBall = UIComponent.sidebarButton("  🏛  BALLROOM", "ballroom".equals(activeMenu));
        Button btnRiwayat = UIComponent.sidebarButton("  🕐  RIWAYAT", "riwayat".equals(activeMenu));

        SceneManager sm = SceneManager.getInstance();
        btnKamar.setOnAction(e -> sm.navigateTo(SceneManager.SCENE_DASHBOARD_PILIH));
        btnBall.setOnAction(e -> sm.navigateTo(SceneManager.SCENE_DASHBOARD_BALL));
        btnRiwayat.setOnAction(e -> {
            healinn.controller.CustomerRiwayatController hc = new healinn.controller.CustomerRiwayatController();
            sm.getStage().getScene().setRoot(hc.createScene());
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox userBox = new HBox(8);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPadding(new Insets(0, 0, 10, 0)); // Jarak ke tombol log out
        
        Label userLbl = new Label(username);
        userLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        userLbl.setTextFill(Color.web(UIStyle.TEXT_DARK));
        
        Label userIcon = new Label("👤");
        userIcon.setFont(Font.font(18));
        userBox.getChildren().addAll(userIcon, userLbl);

        Button btnLogout = UIComponent.darkButton("LOG OUT", 180);
        btnLogout.setOnAction(e -> sm.navigateRoot(SceneManager.SCENE_WELCOME));

        sidebar.getChildren().addAll(logoBox, sep, dashLbl, btnKamar, btnBall, btnRiwayat, spacer, userBox, btnLogout);
        return sidebar;
    }

    //Sidebar Admin
    public static VBox adminSidebar(String activeMenu, String username) {
        VBox sidebar = new VBox(16);
        sidebar.setPrefWidth(260);
        sidebar.setPrefHeight(720);
        sidebar.setPadding(new Insets(24, 20, 24, 20));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setBackground(new Background(new BackgroundFill(
        Color.web("#f5f0e8"), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox logoBox = buildLogoBox(false, true); 
        logoBox.setPadding(new Insets(0, 0, 12, 0));

        Line sep = UIComponent.dividerDark(200);

        Label dashLbl = new Label("DASHBOARD");
        dashLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        dashLbl.setTextFill(Color.web(UIStyle.TEXT_DARK));
        dashLbl.setPadding(new Insets(10, 0, 6, 0));

        Button btnStatus = UIComponent.sidebarButton("  🛏  STATUS KAMAR", "status".equals(activeMenu));
        Button btnReservasi = UIComponent.sidebarButton("  📋  RESERVASI", "reservasi".equals(activeMenu));
        Button btnStatistik = UIComponent.sidebarButton("  📊  STATISTIK", "statistik".equals(activeMenu));

        SceneManager sm = SceneManager.getInstance();
        btnStatus.setOnAction(e -> sm.navigateTo(SceneManager.SCENE_ADMIN_STATUS));
        btnReservasi.setOnAction(e -> sm.navigateTo(SceneManager.SCENE_ADMIN_RESERVASI));
        btnStatistik.setOnAction(e -> sm.navigateTo(SceneManager.SCENE_ADMIN_STATISTIK));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox userBox = new HBox(8);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPadding(new Insets(0, 0, 10, 0));
        
        Label userLbl = new Label(username);
        userLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        userLbl.setTextFill(Color.web(UIStyle.TEXT_DARK));
        
        Label userIcon = new Label("👤");
        userIcon.setFont(Font.font(18));
        userBox.getChildren().addAll(userIcon, userLbl);

        Button btnLogout = UIComponent.darkButton("LOG OUT", 180);
        btnLogout.setOnAction(e -> sm.navigateRoot(SceneManager.SCENE_WELCOME));

        sidebar.getChildren().addAll(logoBox, sep, dashLbl, btnStatus, btnReservasi, btnStatistik, spacer, userBox, btnLogout);
        return sidebar;
    }

    //Content Header 
    public static VBox contentHeader(String titleText, String username, double divWidth) {
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setPadding(new Insets(24, 32, 0, 32));

        Label title = UIComponent.pageTitle(titleText);
        HBox.setHgrow(title, Priority.ALWAYS);

        topRow.getChildren().addAll(title);

        Line div = UIComponent.dividerDark(divWidth);
        VBox header = new VBox(8, topRow, div);
        header.setPadding(new Insets(0, 0, 8, 0));
        return header;
    }

    //Logo
    public static VBox buildLogoBox(boolean isDarkBackground, boolean showAdminPortal) {
        VBox box = new VBox(9);
        box.setAlignment(Pos.CENTER);

        String logoColor = isDarkBackground ? UIStyle.TEXT_LIGHT : UIStyle.TEXT_DARK;

        Label h1 = new Label("HEALINN");
        h1.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        h1.setTextFill(Color.web(logoColor));

        Label h2 = new Label("HOTEL & CONVENTION CENTER");
        h2.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        h2.setTextFill(Color.web(logoColor));

        box.getChildren().addAll(h1, h2);

        if (showAdminPortal) {
            Label adminPortal = new Label("ADMIN PORTAL");
            adminPortal.setFont(Font.font("Georgia", FontWeight.BOLD, 10));
            adminPortal.setTextFill(Color.web(isDarkBackground ? UIStyle.TEXT_LIGHT : UIStyle.TEXT_MUTED));
            box.getChildren().add(adminPortal);
        }
        return box;
    }

    //Form Panel 
    public static VBox darkFormPanel() {
        VBox panel = new VBox(18);
        panel.setPrefWidth(760);
        panel.setPadding(new Insets(36, 48, 36, 48));
        panel.setBackground(UIStyle.darkBackground());
        panel.setStyle("-fx-background-radius: 0;");
        return panel;
    }
}