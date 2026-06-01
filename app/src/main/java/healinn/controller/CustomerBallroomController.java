package healinn.controller;

import healinn.model.BallroomPackage;
import healinn.service.BallroomService;
import healinn.util.UIComponent;
import healinn.util.UILayout;
import healinn.util.UIStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CustomerBallroomController {

    private final BallroomService ballroomSvc = new BallroomService();
    private String loggedInUsername = CustomerDashboardController.loggedInUsername;

    public void setUsername(String username) { this.loggedInUsername = username; }

    public Pane createScene() {
        loggedInUsername = CustomerDashboardController.loggedInUsername;
        BorderPane root = new BorderPane();
        root.setBackground(UIStyle.gradientBackground());
        root.setLeft(UILayout.customerSidebar("ballroom", loggedInUsername));

        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(0, 40, 40, 40));
        mainContent.getChildren().add(
            UILayout.contentHeader("RESERVASI BALLROOM", loggedInUsername, 1000));

        // Status ballroom
        boolean available = ballroomSvc.getBallroom().isAvailable();
        Label statusLbl = new Label(available ? "● Tersedia untuk dipesan" : "● Sedang Dipesan");
        statusLbl.setStyle("-fx-font-family:'Georgia';-fx-font-size:14px;-fx-text-fill:" +
            (available ? UIStyle.AVAILABLE : UIStyle.OCCUPIED) + ";");
        mainContent.getChildren().add(statusLbl);

        Label sectionTitle = UIComponent.sectionTitle("Grand Ballroom HealInn");
        mainContent.getChildren().add(sectionTitle);

        FlowPane flow = new FlowPane(20, 20);
        flow.setAlignment(Pos.TOP_LEFT);

        for (BallroomPackage pkg : BallroomPackage.values()) {
            VBox card = UILayout.ballroomCard(pkg.getDisplayName(), pkg.getFormatedPrice());
            Button resBtn = (Button) card.getChildren().stream()
                .filter(n -> n instanceof Button).findFirst().orElse(null);

            if (resBtn != null) {
                resBtn.setDisable(!available);
                resBtn.setOnAction(e ->
                    new RoomBookingController(loggedInUsername, pkg).showDialog());
            }
            flow.getChildren().add(card);
        }

        mainContent.getChildren().add(flow);

        ScrollPane scroll = new ScrollPane(mainContent);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        root.setCenter(scroll);
        return root;
    }
}
