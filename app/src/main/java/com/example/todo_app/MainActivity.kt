package com.example.todo_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo_app.ui.theme.TodoAppTheme

data class TodoItem(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    var todoItems by rememberSaveable { mutableStateOf(listOf<TodoItem>()) }
    var text by rememberSaveable { mutableStateOf("") }
    var nextId by rememberSaveable { mutableStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("TODO List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Add a new task") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                // Correctly set keyboardOptions and keyboardActions
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (text.isNotBlank()) {
                        todoItems = todoItems + TodoItem(nextId++, text.trim())
                        text = ""
                    }
                })
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (text.isNotBlank()) {
                    todoItems = todoItems + TodoItem(nextId++, text.trim())
                    text = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        TodoSection(
            title = "Items (Active)",
            items = todoItems.filter { !it.isCompleted },
            onCheckedChange = { id, checked ->
                todoItems = todoItems.map { if (it.id == id) it.copy(isCompleted = checked) else it }
            },
            onDelete = { id -> todoItems = todoItems.filterNot { it.id == id } }
        )

        Spacer(modifier = Modifier.height(24.dp))
        TodoSection(
            title = "Completed Items",
            items = todoItems.filter { it.isCompleted },
            onCheckedChange = { id, checked ->
                todoItems = todoItems.map { if (it.id == id) it.copy(isCompleted = checked) else it }
            },
            onDelete = { id -> todoItems = todoItems.filterNot { it.id == id } }
        )
    }
}

@Composable
fun TodoSection(
    title: String,
    items: List<TodoItem>,
    onCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit
) {
    Text(title, style = MaterialTheme.typography.titleMedium)
    if (items.isEmpty()) {
        Text("Empty...", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp))
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            for (item in items) {
                TodoRow(item = item, onCheckedChange = onCheckedChange, onDelete = onDelete)
            }
        }
    }
}

@Composable
fun TodoRow(
    item: TodoItem,
    onCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { checked -> onCheckedChange(item.id, checked) }
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        IconButton(onClick = { onDelete(item.id) }) {
            Icon(Icons.Default.Close, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoApp() {
    TodoAppTheme {
        TodoApp()
    }
}
