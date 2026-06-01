package healinn;

import healinn.controller.*;
import healinn.service.Database;
import healinn.service.DatabaseSeeder;
import healinn.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Database.getInstance();

        new DatabaseSeeder().seed();

        SceneManager sm = SceneManager.getInstance();
        sm.init(primaryStage);

        LoginSelectController loginSelect = new LoginSelectController();
        CustomerLoginController customerLogin = new CustomerLoginController();
        AdminLoginController adminLogin = new AdminLoginController();

        CustomerDashboardController customerDash = new CustomerDashboardController();
        CustomerBallroomController customerBall = new CustomerBallroomController();
        CustomerRiwayatController customerRiwayat = new CustomerRiwayatController();

        AdminDashboardController adminDash = new AdminDashboardController();

        sm.register(SceneManager.SCENE_WELCOME, loginSelect::createScene);
        sm.register(SceneManager.SCENE_CUSTOMER_AUTH, customerLogin::createScene);
        sm.register(SceneManager.SCENE_ADMIN_LOGIN, adminLogin::createScene);

        sm.register(SceneManager.SCENE_DASHBOARD_PILIH, customerDash::createScene);
        sm.register(SceneManager.SCENE_DASHBOARD_BALL, customerBall::createScene);
        sm.register(SceneManager.SCENE_DASHBOARD_HIST, customerRiwayat::createScene);

        sm.register(SceneManager.SCENE_ADMIN_STATUS, adminDash::createStatusScene);
        sm.register(SceneManager.SCENE_ADMIN_RESERVASI, adminDash::createReservasiScene);
        sm.register(SceneManager.SCENE_ADMIN_STATISTIK, adminDash::createStatistikScene);

        primaryStage.setMinWidth(1250);
        primaryStage.setMinHeight(750);  
        primaryStage.setResizable(true); 

        sm.navigateRoot(SceneManager.SCENE_WELCOME);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
