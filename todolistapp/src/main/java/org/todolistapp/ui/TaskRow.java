package org.todolistapp.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.todolistapp.models.Task;
import org.todolistapp.models.TaskCategory;
/**
 * TaskRow.java

 * This class represents a single row in the task list UI.
 * Each row displays a task's description and a button to mark it as completed.

 * Responsibilities:
 *  - Render an individual task with its corresponding category styling.
 *  - Provide a clickable area to edit/view the task.
 *  - Provide a "done" button that notifies the parent UI when the task is completed.

 * This class communicates with the main UI through the TaskRowListener interface,
 * allowing the parent component to handle edits and deletions.
 */


public class TaskRow extends HBox {

    private Task task;
    private Button taskButton;
    private Button doneButton;

    // Listener interface to notify parent UI
    public interface TaskRowListener {
        void onTaskClicked(Task task);     // edit/view task
        void onTaskCompleted(Task task);   // delete task
    }

    public TaskRow(Task task, TaskRowListener listener) {
        this.task = task;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("task-row");  // CSS styling

        // --- Add category-based styling ---
        if (task.getCategory() != null && task.getCategory() != TaskCategory.UNCATEGORIZED) {
            getStyleClass().add("category-" + task.getCategory().name().toLowerCase());
        }

        // Task Button
        taskButton = new Button(task.getDescription());
        taskButton.getStyleClass().add("task-button");
        taskButton.setMaxWidth(Double.MAX_VALUE);
        setHgrow(taskButton, javafx.scene.layout.Priority.ALWAYS);
        taskButton.setOnAction(e -> listener.onTaskClicked(task));

        // Done Button
        doneButton = new Button("✔");
        doneButton.getStyleClass().add("done-button");
        doneButton.setTooltip(new Tooltip("✓"));
        doneButton.setOnAction(e -> listener.onTaskCompleted(task));

        // Add components
        getChildren().addAll(taskButton, doneButton);
    }
}
