package com.example.demoproject;

import com.example.demoproject.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

public class LoginController {

    @FXML private TextField loginInput;
    @FXML private PasswordField passwordInput;
    @FXML private TextField passwordVisibleInput;
    @FXML private CheckBox showPasswordCheckBox;
    @FXML private Label infoLabel;

    private List<User> users;

    @FXML
    public void initialize() {
        loadUsersFromJson();

        // Связываем видимое и скрытое поле пароля
        passwordVisibleInput.managedProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordVisibleInput.visibleProperty().bind(showPasswordCheckBox.selectedProperty());

        passwordInput.managedProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordInput.visibleProperty().bind(showPasswordCheckBox.selectedProperty().not());

        passwordVisibleInput.textProperty().bindBidirectional(passwordInput.textProperty());
    }

    private void loadUsersFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            User[] userArray = mapper.readValue(
                    new File("src/main/resources/com/example/demoproject/user.json"),
                    User[].class
            );
            users = Arrays.asList(userArray);
        } catch (IOException e) {
            infoLabel.setText("Cannot read user.json");
        }
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @FXML
    private void handleLogin() {
        String login = loginInput.getText();
        String password = passwordInput.getText();

        if (login.isEmpty() || password.isEmpty()) {
            infoLabel.setText("Fill all fields");
            return;
        }

        // Ищем пользователя в списке
        User matchedUser = users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElse(null);

        if (matchedUser == null) {
            infoLabel.setText("User not found");
            return;
        }

        if (!md5(password).equals(matchedUser.getPasswordHash())) {
            infoLabel.setText("Incorrect password");
            return;
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) loginInput.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("style2.css").toExternalForm());
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
