package dev.abiekaputra.jiwoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.abiekaputra.jiwoo.domain.CheckIn
import dev.abiekaputra.jiwoo.ui.components.EntryCard
import dev.abiekaputra.jiwoo.ui.components.PageHeading
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun JournalScreen(entries: List<CheckIn>, onDelete: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<CheckIn?>(null) }
    val visible = entries.filter { entry ->
        query.isBlank() ||
            entry.note.contains(query, ignoreCase = true) ||
            entry.tags.any { it.label.contains(query, ignoreCase = true) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).testTag("journal-screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        PageHeading(
            eyebrow = "Your own record",
            title = "Journal",
            description = "Search the words and themes you chose to keep.",
        )
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().testTag("journal-search"),
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            placeholder = { Text("Search reflections or tags") },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
        )
        if (visible.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 42.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    if (entries.isEmpty()) "Your journal is still quiet."
                    else "No reflection matches that search.",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    if (entries.isEmpty()) "Create a reflection from Today when you are ready."
                    else "Try a different word or one of your tags.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 110.dp),
            ) {
                items(visible, key = CheckIn::id) { entry ->
                    EntryCard(entry = entry, onClick = { selected = entry })
                }
            }
        }
    }

    selected?.let { entry ->
        AlertDialog(
            onDismissRequest = { selected = null },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("${entry.mood.symbol} ${entry.mood.label}")
                    IconButton(
                        onClick = {
                            onDelete(entry.id)
                            selected = null
                        }
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete reflection")
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(DATE_FORMAT.format(entry.createdAt.atZone(ZoneId.systemDefault())))
                    Text(entry.note, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Energy ${entry.energy}/5" +
                            entry.tags
                                .takeIf { it.isNotEmpty() }
                                ?.joinToString(
                                    prefix = "  ·  ",
                                    separator = "  ·  ",
                                ) {
                                    it.label
                                }
                                .orEmpty(),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            },
            confirmButton = { TextButton(onClick = { selected = null }) { Text("Close") } },
        )
    }
}

private val DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, d MMMM · HH:mm")
