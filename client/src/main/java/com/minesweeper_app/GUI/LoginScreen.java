package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Screens{

    public LoginScreen() {
        super("");
    }

    @Override 
    public Scene createScreen(Stage stage) {
        VBox layout = new VBox();
        Label label = new Label();
        TextField userField = new TextField();
        userField.setPromptText("Username");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        Button registerButton = new Button("Register");
        registerButton.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        layout.getChildren().addAll(userField, passField, loginButton, registerButton, label);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.setMaxWidth(250);

        loginButton.setOnAction(event -> {
            try {
                ApiClient.get().login(userField.getText().trim(), passField.getText());

                name = userField.getText().trim();
                stage.setScene(new MainMenu(name).createScreen(stage));
            } catch (ApiClient.ApiError e) {
                label.setText(e.getMessage());
            }
        });

        registerButton.setOnAction(event -> {
            try {
                ApiClient.get().register(userField.getText().trim(), passField.getText());

                name = userField.getText().trim();
                stage.setScene(new MainMenu(name).createScreen(stage));
            } catch (ApiClient.ApiError e) {
                label.setText(e.getMessage());
            }
        });

        return new Scene(layout, Constants.width, Constants.height);
    }

    public boolean checkValidUsername(String username) {
        if (username.trim().isEmpty() ||username.length() > 50) {
            return false;
        }

        return true;
    }

    public boolean checkValidPass(String pass) {
        if (pass.trim().isEmpty()) {
            return false;
        }

        return true;
    }
}
