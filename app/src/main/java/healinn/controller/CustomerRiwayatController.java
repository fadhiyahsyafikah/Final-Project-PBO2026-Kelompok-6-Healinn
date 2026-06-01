package healinn.controller;

import healinn.model.Reservation;
import healinn.service.ReservationService;
import healinn.util.SceneManager;
import healinn.util.UIComponent;
import healinn.util.UILayout;
import healinn.util.UIStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class CustomerRiwayatController {
    private final ReservationService resSvc = new ReservationService();
    private String loggedInUsername = "";

    public void setUsername(String username) {this.loggedInUsername = username;}

    public Pane createScene() {
        BorderPane root = new BorderPane();
        root.setBackground(UIStyle.gradientBackground());
        root.setLeft(UILayout.customerSidebar("riwayat"));

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(0, 40, 40, 40));
        mainContent.getChildren().add(UILayout.contentHeader("RIWAYAT RESERVASI", loggedInUsername, 1000));

        VBox historyList = new VBox(15);
        historyList.setPadding(new Insets(10, 10, 10, 0));

        List<Reservation> reservations = resSvc.getByCustomer(loggedInUsername);

        if (reservations.isEmpty()) {
            Label empty = UIComponent.lightLabel("Belum ada riwayat reservasi.", 15);
            historyList.getChildren().add(empty);
        } else {
            for (Reservation r : reservations) {
                boolean allowCancel = r.getStatus() == Reservation.Status.ACTIVE;

                String details = r.getFormattedCheckIn() + " → " + r.getFormattedCheckOut();
                if (r.getType() == Reservation.Type.ROOM) {
                    details += "  (" + r.getNights() + " malam)";
                }
                if (r.getPurpose() != null && !r.getPurpose().isBlank()) {
                    details += "\nTujuan: " + r.getPurpose();
                }

                String statusText = switch (r.getStatus()) {
                    case ACTIVE -> "AKTIF";
                    case COMPLETED -> "SELESAI";
                    case CANCELLED -> "DIBATALKAN";
                };

                final String resId = r.getReservation();

                VBox card = createHistoryCard(
                    r.getBookableName(),
                    details,
                    r.getFormattedPrice(),
                    statusText,
                    allowCancel,
                    () -> resSvc.cancelReservation(resId)
                );
                historyList.getChildren().add(card);
            }
        }

        ScrollPane scroll = new ScrollPane(historyList);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        mainContent.getChildren().add(scroll);
        root.setCenter(mainContent);
        return root;
    }

    public VBox createHistoryCard(String titleText, String detailsText, String priceText,
        String statusText, boolean allowCancel, Runnable onCancelAction) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color:" + UIStyle.CARD_DARK + ";-fx-background-radius:15;");

        HBox rowTop = new HBox();
        rowTop.setAlignment(Pos.CENTER_LEFT);

        Label title = UIComponent.goldLabel(titleText, 16);
        Label status = new Label(statusText);
        String statusColor = switch (statusText) {
            case "AKTIF" -> "#2196f3";
            case "DIBATALKAN" -> "#e53935";
            default -> "#4caf50";
        };
        status.setStyle("-fx-background-color:" + statusColor +
                        ";-fx-text-fill:white;-fx-padding:4 10 4 10;" +
                        "-fx-background-radius:20;-fx-font-size:11px;-fx-font-weight:bold;");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        rowTop.getChildren().addAll(title, spacer1, status);

        HBox rowBottom = new HBox();
        rowBottom.setAlignment(Pos.BOTTOM_LEFT);

        Label details = UIComponent.lightLabel(detailsText, 13);
        details.setWrapText(true);
        Label price = UIComponent.goldLabel(priceText, 18);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        rowBottom.getChildren().addAll(details, spacer2, price);

        card.getChildren().addAll(rowTop, rowBottom);

        if (allowCancel) {
            HBox rowAction = new HBox();
            rowAction.setAlignment(Pos.CENTER_RIGHT);
            rowAction.setPadding(new Insets(5, 0, 0, 0));

            Button btnCancel = new Button("Batalkan Pesanan");
            btnCancel.setStyle(
                "-fx-background-color:#e53935;-fx-text-fill:white;" +
                "-fx-font-family:'Georgia';-fx-font-size:12px;-fx-font-weight:bold;" +
                "-fx-background-radius:8;-fx-cursor:hand;-fx-padding:6 14 6 14;");

            btnCancel.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Yakin ingin membatalkan pesanan ini?", ButtonType.YES, ButtonType.NO);
                alert.setTitle("Konfirmasi Pembatalan");
                alert.setHeaderText(null);
                alert.showAndWait().ifPresent(resp -> {
                    if (resp == ButtonType.YES) {
                        if (onCancelAction != null) onCancelAction.run();
                        SceneManager.getInstance().navigateTo(SceneManager.SCENE_DASHBOARD_HIST);
                    }
                });
            });
            rowAction.getChildren().add(btnCancel);
            card.getChildren().add(rowAction);
        }
        return card;
    }
}
