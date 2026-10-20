package com.minesweeper_app.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class Render extends Application{

    @Override 
    public void start(Stage stage) {
        stage.setTitle("Test");
        stage.setScene(new LoginScreen().createScreen(stage));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /*public Scene createStartScene() {
        Label label = new Label("Welcome enter your name!");
        
        TextInputDialog input = new TextInputDialog();
        input.setHeaderText("Name");
        
        Optional<String> answer = input.showAndWait();

        return new Scene(new StackPane(label, new Text(answer.get())), 400, 300);
    }*/
}
