package org.todolistapp.storage;

import org.junit.jupiter.api.*;
import org.todolistapp.models.Task;
import org.todolistapp.models.TaskCategory;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * CsvHandlerTest.java

 * This class contains unit tests for the csvHandler class, which is responsible
 * for persistent storage of Task objects in a CSV file. The tests verify that
 * tasks can be correctly loaded, saved, added, and removed, ensuring the
 * integrity of the storage mechanism.

 * Responsibilities:
 *  - Verify that tasks are loaded correctly from the CSV file.
 *  - Verify that tasks are saved and appended properly.
 *  - Verify that tasks can be removed by ID.
 *  - Validate behavior with special cases, such as commas in descriptions.

 * Notes:
 *  - Uses JUnit 5 for testing.
 *  - Each test operates on a temporary CSV file to prevent data corruption.
 */

class CsvHandlerTest {

    private static final String TEST_CSV = "test_tasks.csv";
    private CsvHandler csvHandler;

    @BeforeEach
    void setUp() throws IOException {
        // Create an empty test CSV file before each test
        Files.deleteIfExists(Paths.get(TEST_CSV));
        Files.createFile(Paths.get(TEST_CSV));
        csvHandler = new CsvHandler(TEST_CSV);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Remove the test CSV file after each test
        Files.deleteIfExists(Paths.get(TEST_CSV));
    }

    /**
     * Test that loading tasks from an empty CSV file returns an empty list.
     */
    @Test
    @DisplayName("Load tasks from empty CSV should return empty list")
    void testLoadTasksEmpty() {
        List<Task> tasks = csvHandler.loadTasks();
        assertTrue(tasks.isEmpty(), "Tasks list should be empty for an empty CSV");
    }

    /**
     * Test that adding a task appends it to the CSV file
     * and can be correctly loaded back.
     */
    @Test
    @DisplayName("Add task should append to CSV")
    void testAddTask() {
        Task task = new Task(1, TaskCategory.WORK, "Finish report");
        csvHandler.addTasks(task);

        List<Task> tasks = csvHandler.loadTasks();
        assertEquals(1, tasks.size(), "CSV should contain 1 task after adding");
        assertEquals("Finish report", tasks.get(0).getDescription());
        assertEquals(TaskCategory.WORK, tasks.get(0).getCategory());
    }

    /**
     * Test that saving a list of tasks overwrites the CSV file
     * and all tasks are persisted correctly.
     */
    @Test
    @DisplayName("Save tasks should overwrite CSV")
    void testSaveTasks() {
        Task task1 = new Task(1, TaskCategory.PERSONAL, "Buy groceries");
        Task task2 = new Task(2, TaskCategory.SCHOOL, "Submit homework");
        csvHandler.saveTasks(List.of(task1, task2));

        List<Task> tasks = csvHandler.loadTasks();
        assertEquals(2, tasks.size(), "CSV should contain 2 tasks after saving");
        assertEquals("Buy groceries", tasks.get(0).getDescription());
        assertEquals("Submit homework", tasks.get(1).getDescription());
    }

    /**
     * Test that removing a task by ID correctly deletes it from the CSV
     * and leaves other tasks intact.
     */
    @Test
    @DisplayName("Remove task should delete task by ID")
    void testRemoveTask() {
        Task task1 = new Task(1, TaskCategory.WORK, "Finish report");
        Task task2 = new Task(2, TaskCategory.HEALTH, "Go for a run");
        csvHandler.saveTasks(List.of(task1, task2));

        csvHandler.removeTasks(1);
        List<Task> tasks = csvHandler.loadTasks();
        assertEquals(1, tasks.size(), "CSV should contain 1 task after removing");
        assertEquals(TaskCategory.HEALTH, tasks.get(0).getCategory());
        assertEquals("Go for a run", tasks.get(0).getDescription());
    }

    /**
     * Test that a task with commas in its description
     * is correctly saved and loaded from the CSV.
     */
    @Test
    @DisplayName("Loading tasks with commas in description works correctly")
    void testLoadTaskWithComma() {
        Task task = new Task(1, TaskCategory.OTHER, "Buy milk, eggs, and bread");
        csvHandler.addTasks(task);

        List<Task> tasks = csvHandler.loadTasks();
        assertEquals(1, tasks.size());
        assertEquals("Buy milk, eggs, and bread", tasks.get(0).getDescription());
    }
}
