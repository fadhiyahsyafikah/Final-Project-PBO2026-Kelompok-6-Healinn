package healinn.controller;

import healinn.model.*;
import healinn.service.ReservationService;
import healinn.util.SceneManager;
import healinn.util.UIComponent;
import healinn.util.UIStyle;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RoomBookingController {
    private final ReservationService resSvc = new ReservationService();

    // Mode Kamar
    private final Room room;
    private final CustomerDashboardController roomParent;
    private final String usernameKamar;

    // Mode Ballroom
    private final BallroomPackage ballroomPkg;
    private final String usernameBallroom;

    // pesan kamar
    public RoomBookingController(User user, Room r, CustomerDashboardController parent) {
        this.room = r;
        this.roomParent = parent;
        this.usernameKamar = (user != null) ? user.getUsername() : "";
        this.ballroomPkg = null;
        this.usernameBallroom = null;
    }

    // pesan ballroom
    public RoomBookingController(String username, BallroomPackage pkg) {
        this.ballroomPkg = pkg;
        this.usernameBallroom = username;
        this.room = null;
        this.roomParent = null;
        this.usernameKamar = null;
    }

    public void showDialog() {
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(14);
        root.setSpacing(14);
        root.setPadding(new Insets(30));
        root.setPrefWidth(500);
        root.setStyle(
            "-fx-background-color:" + UIStyle.CARD_MED + ";" +
            "-fx-border-color:white;-fx-border-width:2;" +
            "-fx-border-radius:20;-fx-background-radius:20;"
        );

        Label title = UIComponent.goldLabel("Formulir Reservasi", 24);
        Label errLabel = new Label();
        errLabel.setStyle("-fx-text-fill:#e53935;-fx-font-size:12px;");
        errLabel.setVisible(false);
        errLabel.setWrapText(true);

        Button btnConfirm = UIComponent.goldButton("KONFIRMASI & BAYAR", 440);
        Button btnCancel = UIComponent.darkButton("Batal", 440);
        btnCancel.setOnAction(e -> stage.close());

        root.getChildren().add(title);

        // kamar
        if (room != null) {
            Label info = UIComponent.lightLabel(room.getName() + " — " + room.getFormattedPrice(), 15);
            root.getChildren().add(info);

            DatePicker dpIn = new DatePicker(LocalDate.now());
            DatePicker dpOut = new DatePicker(LocalDate.now().plusDays(1));
            dpIn.setPrefWidth(440);
            dpOut.setPrefWidth(440);

            // Estimasi harga (auto-update)
            Label estimasiLbl = UIComponent.lightLabel("Total: ", 14);
            Runnable updateEst = () -> {
                LocalDate ci = dpIn.getValue();
                LocalDate co = dpOut.getValue();
                if (ci != null && co != null && co.isAfter(ci)) {
                    long nights = ChronoUnit.DAYS.between(ci, co);
                    long total = nights * room.getPricePerNight();
                    estimasiLbl.setText("Total yang harus dibayar: " + RoomType.formatRupiah(total));
                }
            };
            dpIn.valueProperty().addListener((o, ov, nv)  -> updateEst.run());
            dpOut.valueProperty().addListener((o, ov, nv) -> updateEst.run());
            updateEst.run();

            //tamu aktif
            Spinner<Integer> spGuest = new Spinner<>(1, 4, 1);
            spGuest.setPrefWidth(440);
            spGuest.setEditable(true);
            spGuest.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
            );
            spGuest.getEditor().setPrefHeight(50);
            spGuest.getEditor().setStyle(
                "-fx-background-color:" + UIStyle.CARD_DARK + ";" +
                "-fx-text-fill:" + UIStyle.TEXT_LIGHT + ";" +
                "-fx-font-family:'Georgia';" +
                "-fx-font-size:14px;" +
                "-fx-border-color:white;" +
                "-fx-border-width:1.5;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;" +
                "-fx-padding:8 12 8 12;"
            );

            // nominal bayar
            TextField tfPay = UIComponent.styledTextField("Masukkan nominal pembayaran (angka)");
            tfPay.setPrefWidth(440);

            addLabel(root, "Tanggal Check-In");
            root.getChildren().add(dpIn);
            addLabel(root, "Tanggal Check-Out");
            root.getChildren().add(dpOut);
            root.getChildren().add(estimasiLbl);
            addLabel(root, "Jumlah Tamu (maks. 4 orang)");
            root.getChildren().add(spGuest);
            addLabel(root, "Nominal Pembayaran");
            root.getChildren().addAll(tfPay, errLabel);

            btnConfirm.setOnAction(e -> {
                LocalDate ci = dpIn.getValue();
                LocalDate co = dpOut.getValue();
                if (ci == null || co == null || !co.isAfter(ci)) {
                    showErr(errLabel, "Tanggal check-out harus setelah check-in."); return;
                }
                String payStr = tfPay.getText().trim().replaceAll("[^0-9]", "");
                if (payStr.isBlank()) {
                    showErr(errLabel, "Masukkan nominal pembayaran."); return;
                }

                int guestCount;
                try {guestCount = Integer.parseInt(spGuest.getEditor().getText().trim());
                    if (guestCount < 1 || guestCount > 4) {
                        showErr(errLabel, "Jumlah tamu harus antara 1 sampai 4.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showErr(errLabel, "Jumlah tamu harus berupa angka.");
                    return;
                }

                long inputAmt = Long.parseLong(payStr);
                long expectedAmt = resSvc.estimateRoomPrice(room.getType(), room.getBedType(), ci, co);
                if (inputAmt != expectedAmt) {
                    showErr(errLabel, "Pembayaran tidak sesuai. Harus tepat " + RoomType.formatRupiah(expectedAmt) + "."); 
                        return;
                }
                Reservation res = resSvc.bookRoom(
                    usernameKamar, room.getRoomId(), ci, co, guestCount);
                if (res == null) {
                    showErr(errLabel, "Gagal melakukan reservasi."); return;
                }
                stage.close();
                showSuccessAlert("Reservasi Berhasil",
                    "ID      : " + res.getReservation() + "\n" +
                    "Kamar   : " + res.getBookableName() + "\n" +
                    "Check-In: " + res.getFormattedCheckIn() + "\n" +
                    "Check-Out: " + res.getFormattedCheckOut() + "\n" +
                    "Tamu      : " + res.getGuestCount() + " orang\n" +
                    "Total   : " + res.getFormattedPrice());
                if (roomParent != null) roomParent.refresh();
            });

        // ballroom
        } else if (ballroomPkg != null) {
            Label info = UIComponent.lightLabel(
                "Grand Ballroom HealInn — Paket: " + ballroomPkg.getDisplayName() +
                " (" + ballroomPkg.getFormatedPrice() + ")", 14);
            root.getChildren().add(info);

            DatePicker dpEvent = new DatePicker(LocalDate.now().plusDays(7));
            dpEvent.setPrefWidth(440);

            // Spinner jumlah hari (hanya untuk paket per-hari)
            Label daysLabel = new Label("Jumlah Hari");
            daysLabel.setStyle(
                "-fx-text-fill:" + UIStyle.TEXT_LIGHT + ";" +
                "-fx-font-family:'Georgia';-fx-font-size:14px;");
            Spinner<Integer> daysSpinner = new Spinner<>(1, 30, 1);
            daysSpinner.setPrefWidth(440);
            daysSpinner.setEditable(true);
            daysSpinner.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
            );
            daysSpinner.getEditor().setPrefHeight(50);
            daysSpinner.getEditor().setStyle(
                "-fx-background-color:" + UIStyle.CARD_DARK + ";" +
                "-fx-text-fill:" + UIStyle.TEXT_LIGHT + ";" +
                "-fx-font-family:'Georgia';" +
                "-fx-font-size:14px;" +
                "-fx-border-color:white;" +
                "-fx-border-width:1.5;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;" +
                "-fx-padding:8 12 8 12;"
            );

            boolean isPerDay = ballroomPkg.isPerDay();
            daysLabel.setVisible(isPerDay); daysLabel.setManaged(isPerDay);
            daysSpinner.setVisible(isPerDay); daysSpinner.setManaged(isPerDay);

            // Estimasi total (auto-update jika paket per-hari)
            Label estimasiLbl = UIComponent.lightLabel(
                "Total yang harus dibayar: " +
                RoomType.formatRupiah(ballroomPkg.calculateTotal(1)), 14);
            daysSpinner.valueProperty().addListener((o, ov, nv) ->
                estimasiLbl.setText("Total yang harus dibayar: " +
                    RoomType.formatRupiah(ballroomPkg.calculateTotal(nv))));

            // Spinner jumlah tamu ballroom (maks. 500 orang)
            Spinner<Integer> spGuest = new Spinner<>(1, 500, 1);
            spGuest.setPrefWidth(440);
            spGuest.setEditable(true);
            spGuest.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
            );
            spGuest.getEditor().setPrefHeight(50);
            spGuest.getEditor().setStyle(
                "-fx-background-color:" + UIStyle.CARD_DARK + ";" +
                "-fx-text-fill:" + UIStyle.TEXT_LIGHT + ";" +
                "-fx-font-family:'Georgia';" +
                "-fx-font-size:14px;" +
                "-fx-border-color:white;" +
                "-fx-border-width:1.5;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;" +
                "-fx-padding:8 12 8 12;"
            );

            TextField tfPurpose = UIComponent.styledTextField(
                "Tujuan Acara (Pernikahan, Seminar, dll)");
            tfPurpose.setPrefWidth(440);

            // Input validasi pembayaran
            TextField tfPay = UIComponent.styledTextField(
                "Masukkan nominal pembayaran (angka)");
            tfPay.setPrefWidth(440);

            addLabel(root, "Tanggal Acara");
            root.getChildren().add(dpEvent);
            root.getChildren().addAll(daysLabel, daysSpinner);
            root.getChildren().add(estimasiLbl);
            addLabel(root, "Jumlah Tamu (maks. 500 orang)");
            root.getChildren().add(spGuest);
            addLabel(root, "Tujuan Penggunaan");
            root.getChildren().add(tfPurpose);
            addLabel(root, "Nominal Pembayaran");
            root.getChildren().addAll(tfPay, errLabel);

            btnConfirm.setOnAction(e -> {
                LocalDate eventDate = dpEvent.getValue();
                if (eventDate == null || eventDate.isBefore(LocalDate.now())) {
                    showErr(errLabel, "Pilih tanggal acara yang valid.");
                    return;
                }

                // validasi jumlah tamu
                int guestCount;
                try {guestCount = Integer.parseInt(spGuest.getEditor().getText().trim());
                    if (guestCount < 1 || guestCount > 500) {
                        showErr(errLabel, "Jumlah tamu harus antara 1 sampai 500.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showErr(errLabel, "Jumlah tamu harus berupa angka.");
                    return;
                }

                if (tfPurpose.getText().isBlank()) {
                    showErr(errLabel, "Tujuan penggunaan tidak boleh kosong.");
                    return;
                }

                // Validasi pembayaran
                String payStr = tfPay.getText().trim().replaceAll("[^0-9]", "");
                if (payStr.isBlank()) {
                    showErr(errLabel, "Masukkan nominal pembayaran.");
                    return;
                }
                    int days = daysSpinner.getValue();
                    long inputAmt = Long.parseLong(payStr);
                    long expectedAmt = ballroomPkg.calculateTotal(days);
                    if (inputAmt != expectedAmt) {
                        showErr(errLabel, "Pembayaran tidak sesuai. Harus tepat " + RoomType.formatRupiah(expectedAmt) + ".");
                        return;
                    }

                Reservation res = resSvc.bookBallroom(
                    usernameBallroom, ballroomPkg,
                    eventDate, days,
                    tfPurpose.getText().trim(),
                    guestCount);
                if (res == null) {
                    showErr(errLabel, "Ballroom tidak tersedia.");
                    return;
                }
                stage.close();
                showSuccessAlert("Reservasi Ballroom Berhasil",
                    "ID      : " + res.getReservation() + "\n" +
                    "Paket   : " + res.getBallroomPackage().getDisplayName() + "\n" +
                    "Tanggal : " + res.getFormattedCheckIn() + "\n" +
                    "Tamu    : " + res.getGuestCount() + " orang\n" +
                    "Tujuan  : " + res.getPurpose() + "\n" +
                    "Total   : " + res.getFormattedPrice());
                SceneManager.getInstance().navigateTo(SceneManager.SCENE_DASHBOARD_BALL);
            });
        }

        root.getChildren().addAll(btnConfirm, btnCancel);

        stage.setScene(new Scene(root));
        stage.show();
    }

    // method tambahan
    private void addLabel(VBox root, String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill:" + UIStyle.TEXT_LIGHT +
                     ";-fx-font-family:'Georgia';-fx-font-size:14px;");
        root.getChildren().add(lbl);
    }

    private void showErr(Label lbl, String msg) {
        lbl.setText(msg); lbl.setVisible(true);
    }

    private void showSuccessAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}