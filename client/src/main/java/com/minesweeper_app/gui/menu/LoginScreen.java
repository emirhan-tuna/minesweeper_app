package com.minesweeper_app.gui.menu;

import com.minesweeper_app.gui.Constants;
import com.minesweeper_app.gui.Screens;
import com.minesweeper_app.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Screens {

    public LoginScreen() {
        super("");
    }

    @Override 
    public Parent createRoot(Stage stage) {
        StackPane root = new StackPane();
        VBox layout = new VBox(40);
        VBox fieldBox = new VBox(20);
        VBox buttonBox = new VBox(20);
        Label label = new Label();
        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setPrefSize(200, 20);
        PasswordField passField = new PasswordField();
        passField.setPrefSize(200, 20);
        passField.setPromptText("Password");
        fieldBox.getChildren().addAll(userField, passField);

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        Button registerButton = new Button("Register");
        registerButton.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        buttonBox.getChildren().addAll(loginButton, registerButton);
        fieldBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
        fieldBox.setMaxWidth(550);
        buttonBox.setMaxWidth(550);

        layout.getChildren().addAll(fieldBox, buttonBox, label);

        loginButton.setOnAction(event -> {
            try {
                ApiClient.get().login(userField.getText().trim(), passField.getText());

                name = userField.getText().trim();
                switchTo(s, new MainMenu(name));
            } catch (ApiClient.ApiError e) {
                label.setText(e.getMessage());
            }
        });

        registerButton.setOnAction(event -> {
            try {
                ApiClient.get().register(userField.getText().trim(), passField.getText());
                name = userField.getText().trim();
                switchTo(s, new MainMenu(name));
            } catch (ApiClient.ApiError e) {
                label.setText(e.getMessage());
            }
        });

        layout.setAlignment(Pos.CENTER);
        root.getChildren().addAll(layout);
        root.setAlignment(Pos.CENTER);

        return root;
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
