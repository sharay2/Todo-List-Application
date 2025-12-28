package org.todolistapp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.todolistapp.service.TaskHandler;
import org.todolistapp.ui.ToDoAppUI;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize TaskHandler with path to CSV file
        String csvFilePath = "tasks.csv"; // You can adjust the path
        TaskHandler taskHandler = new TaskHandler(csvFilePath);

        // Start the UI
        ToDoAppUI appUI = new ToDoAppUI(taskHandler);
        appUI.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}