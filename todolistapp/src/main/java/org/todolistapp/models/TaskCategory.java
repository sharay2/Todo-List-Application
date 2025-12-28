package org.todolistapp.models;

/**
 * TaskCategory.java

 * Defines all valid categories a task can belong to.
 * This provides type safety and avoids relying on raw strings.

 * A task without a specific category will default to UNCATEGORIZED
 */

public enum TaskCategory {
    WORK,
    PERSONAL,
    SCHOOL,
    ERRAND,
    HEALTH,
    OTHER,
    UNCATEGORIZED,;
}
