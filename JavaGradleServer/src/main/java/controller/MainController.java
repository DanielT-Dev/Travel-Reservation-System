package controller;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Race;
import model.Reservation;
import model.SeatDTO;
import model.User;
import network.NetworkClient;

import java.util.List;

public class MainController {

    private final NetworkClient networkClient;

    private final TableView<User> userTable = new TableView<>();
    private final TableView<Race> raceTable = new TableView<>();
    private final TableView<Reservation> reservationTable = new TableView<>();

    private final double TABLE_HEIGHT = 200;
    private final double TABLE_WIDTH = 950;

    private void refreshTables() {
        Task<Void> refreshTask = new Task<>() {
            @Override
            protected Void call() {
                var users = networkClient.getAllUsers();
                var races = networkClient.getAllRaces();
                var reservations = networkClient.getAllReservations();

                javafx.application.Platform.runLater(() -> {
                    userTable.getItems().setAll(users);
                    raceTable.getItems().setAll(races);
                    reservationTable.getItems().setAll(reservations);
                });
                return null;
            }
        };
        new Thread(refreshTask).start();
    }

    public MainController(NetworkClient networkClient) {
        this.networkClient = networkClient;
        this.networkClient.onDataChanged(this::refreshTables);
    }

    private void openSearchWindow() {
        TextField destinationField = new TextField();
        destinationField.setPromptText("Destination");

        TextField dateField = new TextField();
        dateField.setPromptText("Date");

        TextField timeField = new TextField();
        timeField.setPromptText("Time");

        Button searchBtn = new Button("Search");
        Button reserveBtn = new Button("Reserve Seats");

        TableView<SeatDTO> seatTable = new TableView<>();
        seatTable.setPrefHeight(300);
        seatTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SeatDTO, Integer> seatCol = new TableColumn<>("Seat");
        seatCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));

        TableColumn<SeatDTO, String> nameCol = new TableColumn<>("Client");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));

        seatTable.getColumns().addAll(seatCol, nameCol);

        final Race[] currentRace = new Race[1];

        searchBtn.setOnAction(e -> {
            String dest = destinationField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();

            currentRace[0] = networkClient.findRace(dest, date, time);

            if (currentRace[0] == null) {
                seatTable.getItems().clear();
                return;
            }

            List<SeatDTO> seats = networkClient.getSeatsForRace(currentRace[0].getId());
            seatTable.getItems().setAll(seats);
        });

        reserveBtn.setOnAction(ev -> {
            if (currentRace[0] == null) return;

            ReservationController rc = new ReservationController(networkClient, currentRace[0]);
            rc.openWindow();
        });

        HBox searchBox = new HBox(10, destinationField, dateField, timeField);
        HBox buttonBox = new HBox(10, searchBtn, reserveBtn);
        VBox root = new VBox(15, searchBox, buttonBox, seatTable);
        root.setPadding(new Insets(15));

        Stage stage = new Stage();
        stage.setTitle("Search Race & Reserve");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }

    public VBox getView() {
        Button logoutButton = new Button("Log Out");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setPrefWidth(120);

        logoutButton.setOnAction(e -> {
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            LoginController loginController = new LoginController(networkClient, stage);
            Scene loginScene = new Scene(loginController.getView(), 400, 300);

            stage.setScene(loginScene);
        });

        Button openTaskWindowButton = new Button("Open Task Window");
        openTaskWindowButton.setStyle(
                "-fx-background-color: #d99b73;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10 14 10 14;" +
                        "-fx-cursor: hand;"
        );
        openTaskWindowButton.setOnAction(e -> openSearchWindow());

        Button cancelReservationBtn = new Button("Cancel Reservation");
        cancelReservationBtn.setStyle(
                "-fx-background-color: #c0392b;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        cancelReservationBtn.setOnAction(e -> {
            Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            networkClient.cancelReservation(selected.getId());
            refreshTables();
        });

        buildUserTable();
        buildRaceTable();
        buildReservationTable();
        refreshTables();

        VBox root = new VBox(15, logoutButton, openTaskWindowButton, cancelReservationBtn, userTable, raceTable, reservationTable);
        root.setPadding(new Insets(20));
        return root;
    }

    private void buildUserTable() {
        userTable.setPrefHeight(TABLE_HEIGHT);
        userTable.setPrefWidth(TABLE_WIDTH);

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        userTable.getColumns().addAll(idCol, nameCol, emailCol);
    }

    private void buildRaceTable() {
        raceTable.setPrefHeight(TABLE_HEIGHT);
        raceTable.setPrefWidth(TABLE_WIDTH);

        TableColumn<Race, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Race, String> destCol = new TableColumn<>("Destination");
        destCol.setCellValueFactory(new PropertyValueFactory<>("destination"));

        TableColumn<Race, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Race, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        raceTable.getColumns().addAll(idCol, destCol, dateCol, timeCol);
    }

    private void buildReservationTable() {
        reservationTable.setPrefHeight(TABLE_HEIGHT);
        reservationTable.setPrefWidth(TABLE_WIDTH);

        TableColumn<Reservation, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Reservation, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Reservation, Integer> userCol = new TableColumn<>("User ID");
        userCol.setCellValueFactory(new PropertyValueFactory<>("id_user"));

        TableColumn<Reservation, Integer> raceCol = new TableColumn<>("Race ID");
        raceCol.setCellValueFactory(new PropertyValueFactory<>("id_race"));

        reservationTable.getColumns().addAll(idCol, nameCol, userCol, raceCol);
    }
}