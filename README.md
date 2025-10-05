# ToDo_App
- Name: Benjamin Vo
- CWID: 889378170
- Course: CPSC 411A Section 02

The project is a To-Do list app built using Jetpackk Compose.
The app allows the user to add, check off, complete and delete tasks.
Active and completed taks are in separate sections.

## Features
- Add Item: Adds new ToDo items using user text input and button
- Check / Uncheck: Checking box marks item as completed or switches back to incomplete
- Delete: Each item on list has a delete (close) icon
- Two Sections: Items (Active) and Completed Items with headers
- Empty States: Leaves "empty..." message when sections are empty
- Persistence: Uses 'rememberSaveable' to retain state after screen rotation

## Screenshots
<img width="384" height="581" alt="Screenshot 2025-10-04 204224" src="https://github.com/user-attachments/assets/cab6c40e-eda3-4846-bff6-8e4fe1e0e4ac" />
<img width="393" height="649" alt="Screenshot 2025-10-04 204311" src="https://github.com/user-attachments/assets/b380874b-2d8c-492d-ad4c-ac1f132de9bf" />

## Data Class
- TodoItem is the dataclass (models each task)
- Provides copy(), equals(), and toString()

```kotlin
data class TodoItem(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
```
## State
- Uses state variables 'mutableStateOf' and 'rememberSaveable' to store and update the ToDo item list
- Compose auto recomposes parts of the UI when state chenges, interface stays reactive and consistent
- rememberSaveable: keeps ToDo list persistent when screen rotates

```kotlin
var todoItems by rememberSaveable { mutableStateOf(listOf<TodoItem>()) }
```
## State Hoisting
- parent composable (ToDoApp) owns the state (todoItems) and passes data and event handlers to child composables (TodoSection, TodoRow)

```kotlin
TodoSection(
    title = "Items (Active)",
    items = todoItems.filter { !it.isCompleted },
    onCheckedChange = { id, checked ->
        todoItems = todoItems.map { if (it.id == id) it.copy(isCompleted = checked) else it }
    },
    onDelete = { id ->
        todoItems = todoItems.filterNot { it.id == id }
    }
)
```
