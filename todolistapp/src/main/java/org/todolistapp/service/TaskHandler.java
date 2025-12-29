package org.todolistapp.service;
import java.util.*;

import org.todolistapp.models.Task;
import org.todolistapp.models.TaskCategory;
import org.todolistapp.storage.CsvHandler;
/**
 * taskHandler.java

 * This class acts as the business logic layer of the To-Do List application.

 * Responsibilities:
 *  - Maintain an in-memory list of tasks
 *  - Handle ID generation for new tasks
 *  - Provide high-level task operations:
 *      • Add a task
 *      • Delete a task
 *      • Update a task
 *      • Retrieve all tasks or filtered tasks

 * Notes:
 *  - Uses csvHandler for persistent storage
 *  - Sorting ("most recent first") is handled here so the UI always receives
 *    tasks consistently ordered
 */

public class TaskHandler {
    private final CsvHandler storage;
    private final List<Task> tasks;
    private long nextId = 1;

    // Constructor which loads tasks from CSV file
    public TaskHandler(String filePath) {
        this.storage = new CsvHandler(filePath);
        this.tasks = storage.loadTasks();
        initializeIDCounter();
    }

    // Initialize task ID counter by counting to the last line in the CSV file + 1
    private void initializeIDCounter() {
        if(!tasks.isEmpty()) {
            nextId = tasks.stream().mapToLong(Task::getId).max().orElse(0) + 1;
        }
    }

    // Add a new task and sort the tasks by most recently added. Defaults category to UNCATEGORIZED if left empty
    public Task addTask(TaskCategory category, String description) {
        if (category == null) category = TaskCategory.UNCATEGORIZED;
        Task newTask = new Task(nextId++, category, description);
        tasks.add(newTask);
        sortTasksbyNewest();
        storage.saveTasks(tasks);
        return newTask;
    }

    // Removes a task by ID. Returns true if deleted, false if not found
    public boolean removeTask(long id) {
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (removed) storage.saveTasks(tasks);
        return removed;
    }

    // Updates the category or description of an existing task. Returns true if changes successfully, false if not
    public boolean updateTask(long id, TaskCategory category, String description) {
        for(Task task : tasks) {
            if(task.getId() == id) {
                if(category != null) task.setCategory(category);
                if(description != null) task.setDescription(description);

                storage.saveTasks(tasks);
                return true;
            }
        }
        return false;
    }

    // Returns all tasks sorted from newest to oldest
    public List<Task> getAllTasks() {
        sortTasksbyNewest();
        return new ArrayList<>(tasks);
    }

    // Returns list of tasks based on a given category
    public List<Task> getTasksByCategory(TaskCategory category) {
        return tasks.stream().filter(task -> task.getCategory() == category)
                .sorted(Comparator.comparingLong(Task::getId).reversed()).toList();
    }

    // Reorder list of tasks from newest to oldest based on its ID
    private void sortTasksbyNewest() {
        tasks.sort(Comparator.comparingLong(Task::getId).reversed());
    }

}
