package healinn.util;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Supplier;

public class SceneManager {
    private static SceneManager instance;
    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }
    
    private SceneManager() {}

    private Stage primaryStage;

    private final Map<String, Supplier<Pane>> registry = new HashMap<>();

    private final Stack<String> history = new Stack<>();

    private String currentScene;

    public static final String SCENE_WELCOME = "welcome";
    public static final String SCENE_CUSTOMER_AUTH = "customer_auth";
    public static final String SCENE_SIGNUP = "signup";
    public static final String SCENE_LOGIN = "login";
    public static final String SCENE_ADMIN_LOGIN = "admin_login";
    public static final String SCENE_DASHBOARD_PILIH = "dashboard_pilih";
    public static final String SCENE_DASHBOARD_BALL = "dashboard_ballroom";
    public static final String SCENE_DASHBOARD_HIST = "dashboard_riwayat";
    public static final String SCENE_PESAN_KAMAR = "pesan_kamar";
    public static final String SCENE_RESERVASI_BALL = "reservasi_ballroom";
    public static final String SCENE_ADMIN_STATUS = "admin_status";
    public static final String SCENE_ADMIN_RESERVASI = "admin_reservasi";
    public static final String SCENE_ADMIN_STATISTIK = "admin_statistik";

    public void init(Stage stage) {
        this.primaryStage = stage;
        configureStage();
    }

    private void configureStage() {
        primaryStage.setTitle("Healinn Hotel & Convention Center");
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.setResizable(false);
    }

    public void register(String name, Supplier<Pane> supplier) {
        registry.put(name, supplier);
    }

    public void navigateTo(String sceneName) {
        if (currentScene != null) history.push(currentScene);
        showScene(sceneName);
    }

    public void navigateRoot(String sceneName) {
        history.clear();
        showScene(sceneName);
    }

    public void goBack() {
        if (!history.isEmpty()) {
            showScene(history.pop());
        }
    }

    public boolean canGoBack() {
        return !history.isEmpty();
    }

    private void showScene(String sceneName) {
        Supplier<Pane> supplier = registry.get(sceneName);
        if (supplier == null) {
            throw new IllegalArgumentException(
                "Scene tidak terdaftar: '" + sceneName + "'. " +
                "Pastikan register() sudah dipanggil di App.java."
            );
        }

        Pane root = supplier.get();
        Scene scene = buildScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        currentScene = sceneName;
    }

    private Scene buildScene(Pane root) {
        Scene scene = new Scene(root, 1280, 720);
        try {
            String css = getClass().getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (NullPointerException ignored) {
            //CSS
        }
        return scene;
    }

    public Stage getStage() {return primaryStage;}

    public String getCurrentScene() {return currentScene;}
}
