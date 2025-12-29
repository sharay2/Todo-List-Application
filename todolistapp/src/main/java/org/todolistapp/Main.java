package org.todolistapp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.todolistapp.service.TaskHandler;
import org.todolistapp.ui.ToDoAppUI;
/**
 * Main.java

 * Entry point of the To-Do List application.

 * This class initializes the JavaFX application by creating the TaskHandler
 * and launching the main UI (ToDoAppUI).

 * Responsibilities:
 *  - Instantiate core application services.
 *  - Launch the JavaFX runtime.
 *  - Serve as the starting point for the entire program.

 * This file contains only the main method and should remain lightweight,
 * delegating all UI logic to ToDoAppUI.
 */


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize TaskHandler with path to CSV file
        String csvFilePath = "src\\main\\resources\\tasks.csv";
        TaskHandler taskHandler = new TaskHandler(csvFilePath);

        // Start the UI
        ToDoAppUI appUI = new ToDoAppUI(taskHandler);
        appUI.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}