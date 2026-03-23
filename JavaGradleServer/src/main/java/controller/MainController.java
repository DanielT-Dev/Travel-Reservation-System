package controller;

import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.User;
import model.Race;
import model.Reservation;
import service.UserService;
import service.RaceService;
import service.ReservationService;

public class MainController {

    private final UserService userService;
    private final RaceService raceService;
    private final ReservationService reservationService;

    private final TableView<User> userTable = new TableView<>();
    private final TableView<Race> raceTable = new TableView<>();
    private final TableView<Reservation> reservationTable = new TableView<>();

    private final double TABLE_HEIGHT = 200; // fixed height
    private final double TABLE_WIDTH = 950;  // fixed width

    public MainController(UserService userService, RaceService raceService, ReservationService reservationService) {
        this.userService = userService;
        this.raceService = raceService;
        this.reservationService = reservationService;
    }

    public VBox getView() {
        buildUserTable();
        buildRaceTable();
        buildReservationTable();

        VBox root = new VBox(15, userTable, raceTable, reservationTable);
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
        userTable.getItems().addAll(userService.findAll());
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
        raceTable.getItems().addAll(raceService.findAll());
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
        reservationTable.getItems().addAll(reservationService.findAll());
    }
}