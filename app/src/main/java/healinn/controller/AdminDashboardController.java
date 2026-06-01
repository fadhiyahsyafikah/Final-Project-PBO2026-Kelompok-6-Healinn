package healinn.controller;

import healinn.model.*;
import healinn.service.ReservationService;
import healinn.service.RoomService;
import healinn.util.SceneManager;
import healinn.util.UIComponent;
import healinn.util.UILayout;
import healinn.util.UIStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class AdminDashboardController {
    private final RoomService roomSvc = new RoomService();
    private final ReservationService resSvc = new ReservationService();

    public Pane createStatusScene() {
        BorderPane root = new BorderPane();
        root.setBackground(UIStyle.gradientBackground());
        root.setLeft(UILayout.adminSidebar("status", "Admin"));

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(0, 40, 40, 40));
        mainContainer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(mainContainer, Priority.ALWAYS);
        
        mainContainer.getChildren().add(UILayout.contentHeader("STATUS KAMAR", "Admin", 1000));

        VBox scrollableContent = new VBox(20);
        scrollableContent.setMaxHeight(Double.MAX_VALUE);

        for (RoomType type : RoomType.values()) {
            Label typeLabel = UIComponent.sectionTitle(type.getDisplayName());
            scrollableContent.getChildren().add(typeLabel);

            for (BedType bedType : BedType.values()) {
                Label bedLabel = UIComponent.lightLabel(
                    bedType.getDisplayName() + " — " + bedType.getFormattedPrice(type), 13);
                FlowPane flow = new FlowPane(8, 8);
                flow.setPadding(new Insets(0, 0, 8, 16));

                for (Room r : roomSvc.getRoomsByTypeAndBed(type, bedType)) {
                    VBox card = UILayout.roomCard(
                        String.valueOf(r.getRoomNumber()),
                        bedType.getDisplayName(),
                        r.isAvailable(), true);
                    flow.getChildren().add(card);
                }
                scrollableContent.getChildren().addAll(bedLabel, flow);
            }
        }

        ScrollPane scroll = new ScrollPane(scrollableContent);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;-fx-viewport-background:transparent;");

        mainContainer.getChildren().add(scroll);
        root.setCenter(mainContainer);
        return root;
    }

    public Pane createStatistikScene() {
        BorderPane root = new BorderPane();
        root.setBackground(UIStyle.gradientBackground());
        root.setLeft(UILayout.adminSidebar("statistik", "Admin"));

        VBox content = new VBox(24);
        content.setPadding(new Insets(0, 40, 40, 40));
        content.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(content, Priority.ALWAYS);

        content.getChildren().add(UILayout.contentHeader("STATISTIK", "Admin", 1000));

        VBox tile1 = createStatTile("Kamar Terisi",
            String.valueOf(roomSvc.countOccupied()), "🛌");
        VBox tile2 = createStatTile("Tamu Aktif",
            String.valueOf(resSvc.countActiveGuests()), "👤");
        VBox tile3 = createStatTile("Total Pendapatan",
            RoomType.formatRupiah(resSvc.getTotalRevenue()), "📈");

        HBox tiles = new HBox(24, tile1, tile2, tile3);
        tiles.setAlignment(Pos.CENTER);
        VBox.setMargin(tiles, new Insets(16, 0, 0, 0));
        content.getChildren().add(tiles);
        root.setCenter(content);
        return root;
    }

    public Pane createReservasiScene() {
        BorderPane root = new BorderPane();
        root.setBackground(UIStyle.gradientBackground());
        root.setLeft(UILayout.adminSidebar("reservasi", "Admin"));

        VBox mainContainer = new VBox(16);
        mainContainer.setPadding(new Insets(0, 40, 40, 40));
        mainContainer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(mainContainer, Priority.ALWAYS);
        
        mainContainer.getChildren().add(UILayout.contentHeader("SEMUA RESERVASI", "Admin", 1000));

        VBox scrollableContent = new VBox(16);
        scrollableContent.setMaxHeight(Double.MAX_VALUE);

        List<Reservation> all = resSvc.getAll();

        if (all.isEmpty()) {
            Label empty = UIComponent.lightLabel("Belum ada reservasi.", 15);
            scrollableContent.getChildren().add(empty);
        } else {
            for (Reservation r : all) {
                scrollableContent.getChildren().add(buildReservasiCard(r));
            }
        }

        ScrollPane scroll = new ScrollPane(scrollableContent);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;-fx-viewport-background:transparent;");
        
        mainContainer.getChildren().add(scroll);
        root.setCenter(mainContainer);
        return root;
    }

    private VBox buildReservasiCard(Reservation r) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setStyle("-fx-background-color:" + UIStyle.CARD_DARK +
                    ";-fx-background-radius:14;");

        String statusColor = switch (r.getStatus()) {
            case ACTIVE    -> "#4caf50";
            case COMPLETED -> "#9e9e9e";
            case CANCELLED -> "#e53935";
        };

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        Label idLbl = UIComponent.goldLabel(r.getReservation(), 14);
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label statusLbl = new Label(r.getStatus().name());
        statusLbl.setStyle(
            "-fx-background-color:" + statusColor +
            ";-fx-text-fill:white;-fx-padding:3 10 3 10;" +
            "-fx-background-radius:20;-fx-font-size:11px;");
        top.getChildren().addAll(idLbl, sp, statusLbl);

        Label nameLbl  = UIComponent.lightLabel(r.getBookableName(), 14);
        Label custLbl  = UIComponent.lightLabel("Customer : " + r.getCustomerUsername(), 12);
        Label dateLbl  = UIComponent.lightLabel(
            r.getFormattedCheckIn() + " → " + r.getFormattedCheckOut(), 12);
        Label guestLbl = UIComponent.lightLabel(
            "Tamu: " + (r.getGuestCount() > 0
                ? r.getGuestCount() + " orang" : "-"), 12);
        Label priceLbl = UIComponent.goldLabel(r.getFormattedPrice(), 16);

        card.getChildren().addAll(top, nameLbl, custLbl, dateLbl, guestLbl);

        if (r.getPurpose() != null && !r.getPurpose().isBlank()) {
            card.getChildren().add(
                UIComponent.lightLabel("Tujuan   : " + r.getPurpose(), 12));
        }

        card.getChildren().add(priceLbl);

        if (r.getStatus() == Reservation.Status.ACTIVE) {
            HBox actionRow = new HBox();
            actionRow.setAlignment(Pos.CENTER_RIGHT);
            actionRow.setPadding(new Insets(6, 0, 0, 0));

            Button btnCancel = new Button("Batalkan Reservasi");
            btnCancel.setStyle(
                "-fx-background-color:#e53935;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Georgia';" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;" +
                "-fx-padding:6 16 6 16;"
            );

            btnCancel.setOnAction(e -> {
                // Konfirmasi sebelum batalkan
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Konfirmasi Pembatalan");
                confirm.setHeaderText(null);
                confirm.setContentText(
                    "Yakin ingin membatalkan reservasi " +
                    r.getReservation() + " atas nama " +
                    r.getCustomerUsername() + "?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean success = resSvc.cancelReservation(r.getReservation());
                        if (success) {
                            // Refresh scene reservasi admin
                            SceneManager.getInstance()
                                .navigateTo(SceneManager.SCENE_ADMIN_RESERVASI);
                        } else {
                            Alert err = new Alert(Alert.AlertType.ERROR);
                            err.setTitle("Gagal");
                            err.setHeaderText(null);
                            err.setContentText("Gagal membatalkan reservasi.");
                            err.showAndWait();
                        }
                    }
                });
            });

            actionRow.getChildren().add(btnCancel);
            card.getChildren().add(actionRow);
        }

        return card;
    }

    private VBox createStatTile(String title, String value, String icon) {
        VBox tile = new VBox(10);
        tile.setPrefSize(260, 150);
        tile.setAlignment(Pos.CENTER);
        tile.setStyle("-fx-background-color:" + UIStyle.CARD_DARK +
                      ";-fx-background-radius:20;");
        Label ico  = new Label(icon); ico.setStyle("-fx-font-size:36;");
        Label ttl  = UIComponent.lightLabel(title, 14);
        Label val  = UIComponent.goldLabel(value, 22);
        tile.getChildren().addAll(ico, ttl, val);
        return tile;
    }
}