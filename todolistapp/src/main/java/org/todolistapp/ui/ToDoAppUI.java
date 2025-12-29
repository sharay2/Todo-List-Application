package org.todolistapp.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.todolistapp.models.Task;
import org.todolistapp.models.TaskCategory;
import org.todolistapp.service.TaskHandler;
import java.util.List;
/**
 * ToDoAppUI.java

 * This class sets up and manages the main user interface for the To-Do List application.
 * It displays the list of tasks, category filters, and supports adding and editing tasks.

 * Features:
 *  - Top-level layout and scene configuration for JavaFX.
 *  - A scrollable category bar that filters tasks by category.
 *  - A scrollable list of TaskRow components representing each task.
 *  - Pop-up windows for adding and editing tasks.

 * Responsibilities:
 *  - Initialize UI components and apply CSS styling.
 *  - Load and refresh tasks from the TaskHandler service.
 *  - Handle interactions such as adding, editing, completing, or filtering tasks.

 * This class serves as the main visual layer and interacts with the underlying
 * task management logic through TaskHandler.
 */


public class ToDoAppUI {

    private final TaskHandler taskHandler;
    private VBox taskListContainer;
    private HBox categoryBar;
    private TaskCategory selectedCategory = null; // null = ALL

    public ToDoAppUI(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("To Do List");

        // Root layout
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Title
        Label titleLabel = new Label("To Do List");
        titleLabel.getStyleClass().add("title-label");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        // Category bar
        categoryBar = new HBox(10);
        categoryBar.setPadding(new Insets(10));
        categoryBar.setAlignment(Pos.CENTER_LEFT);
        ScrollPane categoryScroll = new ScrollPane(categoryBar);
        categoryScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        categoryScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        categoryScroll.setFitToHeight(true);

        // Task list
        taskListContainer = new VBox(5);
        taskListContainer.setPadding(new Insets(10));
        ScrollPane taskScroll = new ScrollPane(taskListContainer);
        taskScroll.setFitToWidth(true);

        // Center container combining category bar and task list
        VBox centerContainer = new VBox(10);
        centerContainer.getChildren().addAll(categoryScroll, taskScroll);
        root.setCenter(centerContainer);

        // Add Task button
        Button addTaskButton = new Button("Add Task");
        addTaskButton.getStyleClass().add("add-task-button");
        addTaskButton.setOnAction(e -> openAddTaskPopup(primaryStage));
        StackPane addButtonPane = new StackPane(addTaskButton);
        addButtonPane.setPadding(new Insets(10));
        StackPane.setAlignment(addTaskButton, Pos.BOTTOM_RIGHT);
        root.setBottom(addButtonPane);

        // Scene
        Scene scene = new Scene(root, 600, 800);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);

        // Load categories and tasks
        loadCategoryButtons();
        refreshTaskList();

        primaryStage.show();
    }

    // Category buttons
    private void loadCategoryButtons() {
        categoryBar.getChildren().clear();

        // ALL button
        Button allButton = new Button("ALL");
        allButton.getStyleClass().add("category-button");
        allButton.setOnAction(e -> {
            selectedCategory = null;
            refreshTaskList();
        });
        categoryBar.getChildren().add(allButton);

        // Buttons for each enum
        for (TaskCategory cat : TaskCategory.values()) {
            Button catButton = new Button(cat.name());
            catButton.getStyleClass().add("category-button");

            // Apply category-specific style except UNCATEGORIZED
            if (cat != TaskCategory.UNCATEGORIZED) {
                catButton.getStyleClass().add("category-" + cat.name().toLowerCase());
            }

            catButton.setOnAction(e -> {
                selectedCategory = cat;
                refreshTaskList();
            });
            categoryBar.getChildren().add(catButton);
        }
    }

    // Task list
    private void refreshTaskList() {
        taskListContainer.getChildren().clear();
        List<Task> tasks = (selectedCategory == null) ?
                taskHandler.getAllTasks() :
                taskHandler.getTasksByCategory(selectedCategory);

        for (Task task : tasks) {
            TaskRow row = new TaskRow(task, new TaskRow.TaskRowListener() {
                @Override
                public void onTaskClicked(Task task) {
                    openEditTaskPopup(task);
                }

                @Override
                public void onTaskCompleted(Task task) {
                    taskHandler.removeTask(task.getId());
                    refreshTaskList();
                }
            });
            taskListContainer.getChildren().add(row);
        }
    }

    // Add Task Popup
    private void openAddTaskPopup(Stage owner) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Add Task");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Category selection
        ComboBox<TaskCategory> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(TaskCategory.values());
        categoryBox.setValue(TaskCategory.UNCATEGORIZED);

        // Description
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Task description");

        // Add button
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            taskHandler.addTask(categoryBox.getValue(), descriptionField.getText());
            popup.close();
            refreshTaskList();
        });

        layout.getChildren().addAll(new Label("Category:"), categoryBox,
                new Label("Description:"), descriptionField, addButton);

        Scene scene = new Scene(layout, 300, 200);
        popup.setScene(scene);
        popup.showAndWait();
    }

    // Edit Task Popup (similar to Add)
    private void openEditTaskPopup(Task task) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Edit Task");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Category
        ComboBox<TaskCategory> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(TaskCategory.values());
        categoryBox.setValue(task.getCategory());

        // Description
        TextField descriptionField = new TextField(task.getDescription());

        // Save button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            taskHandler.updateTask(task.getId(), categoryBox.getValue(), descriptionField.getText());
            popup.close();
            refreshTaskList();
        });

        layout.getChildren().addAll(new Label("Category:"), categoryBox,
                new Label("Description:"), descriptionField, saveButton);

        Scene scene = new Scene(layout, 300, 200);
        popup.setScene(scene);
        popup.showAndWait();
    }
}
