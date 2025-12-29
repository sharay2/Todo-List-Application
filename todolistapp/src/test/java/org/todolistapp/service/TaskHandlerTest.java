package org.todolistapp.service;

import org.junit.jupiter.api.*;
import org.todolistapp.models.Task;
import org.todolistapp.models.TaskCategory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TaskHandlerTest.java

 * Unit tests for the TaskHandler class.

 * Responsibilities:
 *  - Verify correct task addition, deletion, and updates.
 *  - Ensure IDs are assigned correctly and incremented.
 *  - Test category filtering and sorting behavior.
 *  - Confirm persistence with temporary CSV storage.

 * Notes:
 *  - Uses JUnit 5.
 *  - Each test runs with a fresh temporary CSV file to isolate side effects.
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskHandlerTest {

    private TaskHandler handler;
    private Path tempCsv;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary CSV file and initialize TaskHandler
        tempCsv = Files.createTempFile("tasks", ".csv");
        handler = new TaskHandler(tempCsv.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempCsv);
    }

    /**
     * Test that adding tasks generates incrementing IDs starting from 1.
     */
    @Test
    void testAddTaskGeneratesIncrementingIds() {
        Task t1 = handler.addTask(TaskCategory.WORK, "Email boss");
        Task t2 = handler.addTask(TaskCategory.SCHOOL, "Finish assignment");

        assertEquals(1L, t1.getId());
        assertEquals(2L, t2.getId());
    }

    /**
     * Test that adding a task with null category defaults to UNCATEGORIZED.
     */
    @Test
    void testAddTaskDefaultsToUncategorized() {
        Task t = handler.addTask(null, "Some task");

        assertEquals(TaskCategory.UNCATEGORIZED, t.getCategory());
    }

    /**
     * Test that removing an existing task returns true and deletes it.
     */
    @Test
    void testRemoveTask() {
        Task t1 = handler.addTask(TaskCategory.WORK, "Clean office");

        boolean removed = handler.removeTask(t1.getId());
        assertTrue(removed);

        assertTrue(handler.getAllTasks().isEmpty());
    }

    /**
     * Test that attempting to remove a non-existent task returns false.
     */
    @Test
    void testRemoveTaskNotFound() {
        assertFalse(handler.removeTask(999));
    }

    /**
     * Test updating an existing task's category and description works correctly.
     */
    @Test
    void testUpdateTask() {
        Task t = handler.addTask(TaskCategory.PERSONAL, "Go for a run");

        boolean updated = handler.updateTask(t.getId(), TaskCategory.HEALTH, "Run 3 miles");
        assertTrue(updated);

        Task updatedTask = handler.getAllTasks().get(0);

        assertEquals(TaskCategory.HEALTH, updatedTask.getCategory());
        assertEquals("Run 3 miles", updatedTask.getDescription());
    }

    /**
     * Test that updating a non-existent task returns false.
     */
    @Test
    void testUpdateTaskNotFound() {
        boolean updated = handler.updateTask(999, TaskCategory.WORK, "Test");
        assertFalse(updated);
    }

    /**
     * Test that tasks are returned sorted newest first (descending by ID).
     */
    @Test
    void testTasksSortedNewestFirst() {
        Task t1 = handler.addTask(TaskCategory.WORK, "Oldest");
        Task t2 = handler.addTask(TaskCategory.WORK, "Middle");
        Task t3 = handler.addTask(TaskCategory.WORK, "Newest");

        List<Task> tasks = handler.getAllTasks();

        assertEquals(t3.getId(), tasks.get(0).getId());
        assertEquals(t2.getId(), tasks.get(1).getId());
        assertEquals(t1.getId(), tasks.get(2).getId());
    }

    /**
     * Test that getTasksByCategory returns only tasks of the specified category.
     */
    @Test
    void testGetTasksByCategory() {
        handler.addTask(TaskCategory.WORK, "Task A");
        handler.addTask(TaskCategory.PERSONAL, "Task B");
        handler.addTask(TaskCategory.WORK, "Task C");

        List<Task> workTasks = handler.getTasksByCategory(TaskCategory.WORK);

        assertEquals(2, workTasks.size());
        assertEquals(TaskCategory.WORK, workTasks.get(0).getCategory());
        assertEquals(TaskCategory.WORK, workTasks.get(1).getCategory());
    }

    /**
     * Test that tasks are properly saved and reloaded from the CSV file.
     */
    @Test
    void testPersistenceSaveAndReload() {
        handler.addTask(TaskCategory.WORK, "One");
        handler.addTask(TaskCategory.OTHER, "Two");

        // Reload from the same CSV
        TaskHandler reloaded = new TaskHandler(tempCsv.toString());

        List<Task> tasks = reloaded.getAllTasks();

        assertEquals(2, tasks.size());
        assertEquals("Two", tasks.get(0).getDescription()); // newest first
        assertEquals("One", tasks.get(1).getDescription());
    }
}
