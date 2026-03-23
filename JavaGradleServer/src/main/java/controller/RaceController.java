package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Race;
import service.RaceService;

public class RaceController {

    private final RaceService raceService;
    private final TableView<Race> table = new TableView<>();
    private final Label statusLabel = new Label();

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    public VBox getView() {
        Label title = new Label("Races");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #6b4f3b;"
        );

        statusLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #8a6d5a;"
        );

        TableColumn<Race, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Race, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));

        TableColumn<Race, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Race, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Race, String> seatsCol = new TableColumn<>("Seats");
        seatsCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAvaiableSeats()))
        );

        TableColumn<Race, String> seatCountCol = new TableColumn<>("Seat Count");
        seatCountCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAvaiableSeats().size()))
        );

        table.getColumns().addAll(idCol, destinationCol, dateCol, timeCol, seatsCol, seatCountCol);
        table.setPrefHeight(320);
        table.setMinWidth(800);

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(buttonStyle());
        refreshButton.setOnAction(e -> refreshTable());

        HBox actions = new HBox(10, refreshButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(14, title, statusLabel, actions, table);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #fff8f0, #fcefe3);" +
                        "-fx-border-color: #ead7c3;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;"
        );

        refreshTable();
        return root;
    }

    public void refreshTable() {
        table.setItems(FXCollections.observableArrayList(raceService.findAll()));
        statusLabel.setText("Total races: " + raceService.getTotalRaces());
    }

    private String buttonStyle() {
        return "-fx-background-color: #d99b73;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10 14 10 14;" +
                "-fx-cursor: hand;";
    }
}