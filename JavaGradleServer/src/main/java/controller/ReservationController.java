package controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Race;
import network.NetworkClient;

import java.util.ArrayList;
import java.util.List;

public class ReservationController {

    private final NetworkClient networkClient;
    private final Race race;

    public ReservationController(NetworkClient networkClient, Race race) {
        this.networkClient = networkClient;
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
                String name = nameField.getText().trim();
                int numberOfSeats = Integer.parseInt(seatsField.getText().trim());

                if (name.isEmpty()) {
                    message.setText("Client name is required!");
                    message.setStyle("-fx-text-fill: red;");
                    return;
                }

                List<Integer> freeSeats = networkClient.getFreeSeats(race.getId());

                if (freeSeats.size() < numberOfSeats) {
                    message.setText("Not enough available seats!");
                    message.setStyle("-fx-text-fill: red;");
                    return;
                }

                List<Integer> selectedSeats = new ArrayList<>();
                for (int i = 0; i < numberOfSeats; i++) {
                    selectedSeats.add(freeSeats.get(i));
                }

                networkClient.createReservation(
                        race.getId(),
                        name,
                        selectedSeats
                );

                message.setText("Reservation successful!");
                message.setStyle("-fx-text-fill: green;");

            } catch (NumberFormatException ex) {
                message.setText("Invalid number of seats!");
                message.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                message.setText("Reservation failed!");
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