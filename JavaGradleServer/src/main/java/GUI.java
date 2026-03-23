
import controller.LoginController;
import database.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.RaceDAO;
import repository.ReservationDAO;
import repository.UserDAO;
import service.RaceService;
import service.ReservationService;
import service.UserService;

public class GUI extends Application {

    @Override
    public void start(Stage stage) {
        // Initialize DAOs
        UserDAO userDAO = new UserDAO();
        RaceDAO raceDAO = new RaceDAO();
        ReservationDAO reservationDAO = new ReservationDAO();

        // Initialize Services
        UserService userService = new UserService(userDAO);
        RaceService raceService = new RaceService(raceDAO);
        ReservationService reservationService = new ReservationService(reservationDAO);

        // Create LoginController
        LoginController loginController = new LoginController(userService, raceService, reservationService, stage);

        // Set scene
        Scene scene = new Scene(loginController.getView(), 400, 450);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

//        // Initialize database (optional, depending on your setup)
//        Database.initialize();
    }

    public static void main(String[] args) {
        launch();
    }
}