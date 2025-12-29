# Todo-List-Application
A simple to-do list app that stores tasks in a file and supports adding, removing, and listing tasks. The application has the capability to classify tasks into different categories.
## **Features:**
- Add new tasks with a category and description
- Edit already existing tasks
- Remove completed tasks
- Filter tasks by category
- Tasks are sorted by most recently added to last

## **Main Files**
`# Main.java`
The entry point of the application.
Initializes the TaskHandler and launches the JavaFX UI (ToDoAppUI).
`# Task.java`
Model class representing a single task.
Stores a unique long ID, a TaskCategory value, and a string description.
`# TaskCategory.java`
Enum defining the task categories: `WORK`, `PERSONAL`, `SCHOOL`, `ERRAND`, `HEALTH`, `OTHER`, `UNCATEGORIZED`
`# TaskHandler.java`
Core logic of the application, which handles:
- Adding tasks
- Removing tasks
- Updating tasks
- Flitering by category
- Sorting tasks (from newest to oldest)
- Saving/loading to the CSV file
`# CsvHandler.java`
Responsible for CSV file operations, which loads tasks at startup and saves updates when the task list changes.
`# ToDoAppUI.java`
JavaFX-based user interface, which includess:
- Task list display
- Category filter bar
- Add/Edit pop-up windows
- Buttons for user interactions
` #TaskRow.java`
Custom UI component representing a single task in the list, which includes:
- A clickable description label for editing a task
- A completion button to remove completed tasks

## **Test Files**
`# TaskTest.java`
Tests the `Task` model, specifically:
- ID handling
- Category storage
- Description updates
` #TaskHandlerTest.java`
Validates task management behavior, specifically:
- Adding tasks
- Updating tasks
- Deleting tasks
- Filtering by category
- Sorting logic
` #CsvHandlerTest.java`
Tests CSV storage logic: specifically:
- Saving tasks to file
- Loading tasks from file
- Ensuring correct formatting and data integrity

## **Running the Program**
To run the program, you will need **Maven** installed on your device.
1. Open a terminal or command prompt.
2. Navigate to the `todolistapp` directory.
3. Run the following command:
```bash
mvn clean javafx:run
```
## **Potential Issues/Known Limitations:**
- Use of an incrementing long ID for uniqueness of each task as well as allowing it to be sorted by creation order. Since a long has a maximum value, its limit is extremely high and unlikely to be reached in practical use. I have considered this to be a potential issue since if this limit is somehow reached, then the program wouldn't behave correctly, as the long ID's would overflow and become negative, thus reversing the ordering logic of the tasks. Deleted task ID's are not used so that we can maintain data integrity and predictable ordering, so this value will continuously increment. If this project were needed in a larger-scale setting, then we should switch from CSV storage to a database to improve scalability and remove practical limitations.
- This application assumes that the CSV file is only modified by the program itself. In the case where the user tampers with the CSV file or the file is corrupted, it may cause unexpected behavior or errors. Additional error handling and validation would be needed to handle these issues if needed in a production setting.
- Due to the use of a CSV file storing the data, the user cannot use commas within the descriptions of each task.
