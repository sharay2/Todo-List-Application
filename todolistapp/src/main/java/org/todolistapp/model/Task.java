package org.todolistapp.model;
/**
 * Task.java

 * This class represents a single task in the To-Do List application.

 * Each task has:
 *   - a unique, immutable ID (long) for identification and ordering,
 *   - a category (String) to classify the task,
 *   - a description (String) which describes the task itself.

 * The class provides:
 *   - Getters and setters for category and description,
 *   - Conversion to and from CSV format for persistent storage,
 *   - A clean, simple structure suitable for a desktop or command-line To-Do application.
 */

public class Task {
    /* Task values:
        ID = unique name for this task
        category = category that this task falls under
        description = what the task actually is
     */
    private final long id;
    private String category;
    private String description;

    public Task(long id, String category, String description) {
        this.id = id;
        this.category = category;
        this.description = description;
    }

    // GET METHODS
    // ID get method
    public long getId() {
        return id;
    }
    // Category get method
    public String getCategory() {
        return category;
    }
    // Description get method
    public String getDescription() {
        return description;
    }

    // SET METHODS
    // Sets this task to a specific category
    public void setCategory(String category) {
        this.category = category;
    }
    // Sets this task's description
    public void setDescription(String description) {
        this.description = description;
    }

    // Convert task to CSV string for reading/writing to storage
    @Override
    public String toString() {
        return id + "," + category + "," + description;
    }

    // Factory method: create a Task object from a CSV line
    public static Task fromCSV(String csv) {
        String[] fields = csv.split(",",3);

        long id = Long.parseLong(fields[0]);
        String category = fields[1];
        String description = fields[2];

        return new Task(id, category, description);
    }
}
