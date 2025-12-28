package org.todolistapp.storage;
import org.todolistapp.models.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * csvHandler.java

 * This class is responsible for handling persistent storage of Task objects
 * in the To-Do List application. It provides methods to:
 *   - Load all tasks from a CSV file
 *   - Save a list of tasks back to the CSV file
 *   - Append new tasks to storage
 *   - Remove tasks from storage by ID

 * Responsibilities:
 *   - Perform all file I/O operations related to tasks
 *   - Convert between Task objects and CSV string representation

 * Notes:
 *   - Does not implement business logic such as task ID generation,
 *     sorting, or filtering by category. Those responsibilities are
 *     handled by the taskHandler class.
 */

public class csvHandler {
    private final String filePath;

    // Constructor
    public csvHandler(String filePath) {
        this.filePath = filePath;
        checkFile();
    }

    // Checks if the file exists, and if not, create a new CSV file
    private void checkFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if(file.createNewFile())
                    System.out.println("New CSV file created");
            } catch (IOException e) {
                System.err.println("Error creating CSV file: " + e.getMessage());
            }
        }
    }

    // Loads list of tasks from the CSV file
    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null && !line.isBlank()){
                tasks.add(Task.fromCSV(line));
            }
        }
        catch(IOException e){
            System.err.println("Error fetching tasks: " + e.getMessage());
        }
        return tasks;
    }

    // Saves the new list of tasks to the CSV file
    public void saveTasks(List<Task> tasks) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))){
            for(Task task : tasks){
                writer.write(task.toString());
                writer.newLine();
            }
        }
        catch(IOException e){
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    // Add a new task to the CSV file
    public void addTasks(Task task) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            writer.write(task.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error adding task to CSV file: " + e.getMessage());
        }
    }

    // Remove a task by its ID on the CSV file
    public void removeTasks(long id) {
        List<Task> tasks = loadTasks();
        List<Task> updatedTasks = tasks.stream()
                .filter(task -> task.getId() != id)
                .toList();
        saveTasks(updatedTasks);
    }
}
