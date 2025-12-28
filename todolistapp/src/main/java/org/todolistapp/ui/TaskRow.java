package org.todolistapp.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.todolistapp.models.Task;

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

        // Task Button
        taskButton = new Button(task.getDescription());
        taskButton.getStyleClass().add("task-button");
        taskButton.setMaxWidth(Double.MAX_VALUE);
        setHgrow(taskButton, javafx.scene.layout.Priority.ALWAYS);
        taskButton.setOnAction(e -> listener.onTaskClicked(task));

        // Done Button
        doneButton = new Button("✔");
        doneButton.getStyleClass().add("done-button");
        doneButton.setTooltip(new Tooltip("Mark as complete"));
        doneButton.setOnAction(e -> listener.onTaskCompleted(task));

        // Add components
        getChildren().addAll(taskButton, doneButton);
    }

    public Task getTask() {
        return task;
    }
}
