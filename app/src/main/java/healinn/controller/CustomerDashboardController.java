package healinn.controller;

import healinn.model.*;
import healinn.service.RoomService;
import healinn.util.SceneManager;
import healinn.util.UIComponent;
import healinn.util.UILayout;
import healinn.util.UIStyle;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CustomerDashboardController {
    private final RoomService roomSvc = new RoomService();
    private String loggedInUsername = "User";

    public void setUsername(String username) {this.loggedInUsername = username;}

    public Pane createScene() {
        BorderPane root = new BorderPane();
        root.setBackground(UIStyle.gradientBackground());
        root.setLeft(UILayout.customerSidebar("kamar"));

        VBox containerLayout = new VBox(10);
        containerLayout.setPadding(new Insets(0, 40, 40, 40));
        
        containerLayout.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(containerLayout, Priority.ALWAYS);

        containerLayout.getChildren().add(
            UILayout.contentHeader("PILIH KAMAR", loggedInUsername, 200));

        VBox scrollableContent = new VBox(10);
        scrollableContent.setPadding(new Insets(0, 10, 0, 0));
        
        scrollableContent.setMaxHeight(Double.MAX_VALUE);

        for (RoomType type : RoomType.values()) {
            for (BedType bed : BedType.values()) {
                String label = type.getDisplayName() + " - " + bed.getDisplayName() + " (" + bed.getFormattedPrice(type) + ")";
                scrollableContent.getChildren().add(createCategorySection(label, type, bed));
            }
        }

        ScrollPane scroll = new ScrollPane(scrollableContent);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;-fx-viewport-background:transparent;");

        containerLayout.getChildren().add(scroll);
        root.setCenter(containerLayout);
        return root;
    }

    private VBox createCategorySection(String title, RoomType type, BedType bed) {
        VBox section = new VBox(12);
        section.setPadding(new Insets(10, 0, 15, 0));

        Label lblTitle = UIComponent.sectionTitle(title);
        FlowPane flow = new FlowPane(15, 15);

        for (Room r : roomSvc.getRoomsByTypeAndBed(type, bed)) {
            VBox card = UILayout.roomCard(
                String.valueOf(r.getRoomNumber()),
                bed.getDisplayName(),
                r.isAvailable(), false);

            if (r.isAvailable()) {
                card.setOnMouseClicked(e -> new RoomBookingController(null, r, this).showDialog());
            }
            flow.getChildren().add(card);
        }

        section.getChildren().addAll(lblTitle, flow);
        return section;
    }

    public void refresh() {
        SceneManager.getInstance().navigateTo(SceneManager.SCENE_DASHBOARD_PILIH);
    }
}