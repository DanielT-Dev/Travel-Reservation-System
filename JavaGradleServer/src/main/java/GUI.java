import controller.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.NetworkClient;

public class GUI extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Connect to server
            NetworkClient networkClient = new NetworkClient("localhost", 5000);

            // Create LoginController with NetworkClient
            LoginController loginController = new LoginController(networkClient, stage);

            // Set scene
            Scene scene = new Scene(loginController.getView(), 400, 450);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}