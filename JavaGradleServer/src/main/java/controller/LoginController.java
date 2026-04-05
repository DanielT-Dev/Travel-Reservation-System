package controller;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.NetworkClient;

public class LoginController {

    private final NetworkClient networkClient;
    private final Stage stage;

    public LoginController(NetworkClient networkClient, Stage stage) {
        this.networkClient = networkClient;
        this.stage = stage;
    }

    public VBox getView() {
        Label titleLabel = new Label("Welcome back");
        titleLabel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #6b4f3b;"
        );

        Label subtitleLabel = new Label("Sign in to continue");
        subtitleLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #8a6d5a;"
        );

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(fieldStyle());

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(fieldStyle());

        Button loginButton = new Button("Login");
        loginButton.setStyle(buttonStyle());

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 13px;");

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);
        loadingIndicator.setPrefSize(28, 28);

        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            messageLabel.setText("");
            messageLabel.setStyle("");
            loadingIndicator.setVisible(true);
            loginButton.setDisable(true);

            // Run login in background thread to avoid freezing GUI
            new Thread(() -> {
                boolean success = networkClient.login(email, password);

                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    loginButton.setDisable(false);

                    if (success) {
                        messageLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 13px;");
                        messageLabel.setText("Login successful!");

                        MainController mainController = new MainController(networkClient);
                        Scene mainScene = new Scene(mainController.getView(), 1000, 850);
                        stage.setScene(mainScene);
                        stage.setTitle("Main Dashboard");
                    } else {
                        messageLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 13px;");
                        messageLabel.setText("Invalid email or password");
                    }
                });
            }).start();
        });

        VBox root = new VBox(12,
                titleLabel,
                subtitleLabel,
                emailField,
                passwordField,
                loginButton,
                loadingIndicator,
                messageLabel
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(28));
        root.setPrefWidth(340);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #fff8f0, #fcefe3);" +
                        "-fx-border-color: #ead7c3;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;"
        );

        return root;
    }

    private String fieldStyle() {
        return "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #e2c9b6;" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 10 12 10 12;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #4a3b31;";
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