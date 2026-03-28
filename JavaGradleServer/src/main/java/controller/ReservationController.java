package controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Race;
import service.ReservationService;

import java.util.ArrayList;
import java.util.List;

public class ReservationController {

    private final ReservationService reservationService;
    private final Race race;

    public ReservationController(ReservationService reservationService, Race race) {
        this.reservationService = reservationService;
        this.race = race;
    }

    public VBox getView() {
        Label title = new Label("Make Reservation");

        TextField nameField = new TextField();
        nameField.setPromptText("Client Name");

        TextField seatsField = new TextField();
        seatsField.setPromptText("Number of seats");

        Label message = new Label();

        Button reserveBtn = new Button("Reserve");

        reserveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int numberOfSeats = Integer.parseInt(seatsField.getText());

                // get free seats
                List<Integer> freeSeats = reservationService.getFreeSeats(race.getId());

                if (freeSeats.size() < numberOfSeats) {
                    message.setText("Not enough available seats!");
                    message.setStyle("-fx-text-fill: red;");
                    return;
                }

                // pick first N free seats
                List<Integer> selectedSeats = new ArrayList<>();
                for (int i = 0; i < numberOfSeats; i++) {
                    selectedSeats.add(freeSeats.get(i));
                }

                reservationService.createReservation(
                        race.getId(),
                        name,
                        selectedSeats
                );

                message.setText("Reservation successful!");
                message.setStyle("-fx-text-fill: green;");

            } catch (Exception ex) {
                message.setText("Invalid input!");
                message.setStyle("-fx-text-fill: red;");
            }
        });

        VBox root = new VBox(10, title, nameField, seatsField, reserveBtn, message);
        root.setPadding(new Insets(20));
        return root;
    }

    public void openWindow() {
        Stage stage = new Stage();
        stage.setTitle("Reservation");
        stage.setScene(new Scene(getView(), 300, 250));
        stage.show();
    }
}