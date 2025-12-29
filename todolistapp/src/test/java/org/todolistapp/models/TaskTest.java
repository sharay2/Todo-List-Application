package org.todolistapp.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TaskTest.java

 * Unit tests for the Task class.

 * Responsibilities:
 *  - Verify that Task objects correctly store and return their ID, category, and description.
 *  - Ensure the fromCSV and toString methods correctly serialize and deserialize tasks.
 *  - Confirm that setters update Task properties correctly.

 * Notes:
 *  - Uses JUnit 5.
 */

class TaskTest {

    /**
     * Test that the constructor correctly initializes ID, category, and description.
     */
    @Test
    void testTaskConstructor() {
        Task task = new Task(1L, TaskCategory.WORK, "Finish report");

        assertEquals(1L, task.getId());
        assertEquals(TaskCategory.WORK, task.getCategory());
        assertEquals("Finish report", task.getDescription());
    }

    /**
     * Test that the setCategory method correctly updates the category.
     */
    @Test
    void testSetCategory() {
        Task task = new Task(1L, TaskCategory.WORK, "Finish report");
        task.setCategory(TaskCategory.PERSONAL);

        assertEquals(TaskCategory.PERSONAL, task.getCategory());
    }

    /**
     * Test that the setDescription method correctly updates the description.
     */
    @Test
    void testSetDescription() {
        Task task = new Task(1L, TaskCategory.WORK, "Finish report");
        task.setDescription("Go to gym");

        assertEquals("Go to gym", task.getDescription());
    }

    /**
     * Test that toString correctly serializes the task to CSV format.
     */
    @Test
    void testToString() {
        Task task = new Task(1L, TaskCategory.ERRAND, "Buy milk");
        String csv = task.toString();

        assertEquals("1,ERRAND,Buy milk", csv);
    }

    /**
     * Test that fromCSV correctly deserializes a CSV string into a Task object.
     */
    @Test
    void testFromCSV() {
        String csv = "5,HEALTH,Go jogging";
        Task task = Task.fromCSV(csv);

        assertEquals(5L, task.getId());
        assertEquals(TaskCategory.HEALTH, task.getCategory());
        assertEquals("Go jogging", task.getDescription());
    }

    /**
     * Test that fromCSV throws an exception if the input is invalid.
     */
    @Test
    void testFromCSVInvalid() {
        String invalidCsv = "invalid,input";
        assertThrows(IllegalArgumentException.class, () -> Task.fromCSV(invalidCsv));
    }
}
