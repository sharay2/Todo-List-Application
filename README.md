# Todo-List-Application
A simple to-do list app that stores tasks in a file and supports adding, removing, and listing tasks. The application has the capability to classify tasks into different categories.

Potential Issues/Known Limitations:
Use of an incrementing long ID for uniqueness of each task as well as allowing it to be sorted by creation order. Since a long has a maximum value, its limit is extremely high and unlikely to be reached in practical use. I have considered this to be a potential issue since if this limit is somehow reached, then the program wouldn't behave correctly, as the long ID's would overflow and become negative, thus reversing the ordering logic of the tasks. Deleted task ID's are not used so that we can maintain data integrity and predictable ordering, so this value will continuously increment. If this project were needed in a larger-scale setting, then we should switch from CSV storage to a database to improve scalability and remove practical limitations.

This application assumes that the CSV file is only modified by the program itself. In the case where the user tampers with the CSV file or the file is corrupted, it may cause unexpected behavior or errors. Additional error handling and validation would be needed to handle these issues if needed in a production setting.

