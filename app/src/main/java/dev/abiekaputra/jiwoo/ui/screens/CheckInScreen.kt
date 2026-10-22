package dev.abiekaputra.jiwoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.abiekaputra.jiwoo.domain.Mood
import dev.abiekaputra.jiwoo.domain.ReflectionTag
import dev.abiekaputra.jiwoo.ui.components.EnergySelector
import dev.abiekaputra.jiwoo.ui.components.MoodSelector
import dev.abiekaputra.jiwoo.ui.components.TagSelector

@Composable
fun CheckInScreen(
    onBack: () -> Unit,
    onSave: (Mood, Int, String, Set<ReflectionTag>) -> Unit,
) {
    var mood by remember { mutableStateOf(Mood.STEADY) }
    var energy by remember { mutableIntStateOf(3) }
    var note by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf<Set<ReflectionTag>>(emptySet()) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier =
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(20.dp)
                .testTag("checkin-screen"),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Go back")
            }
            Column {
                Text("Private check-in", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "Pause. Notice. Put it into your own words.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        MoodSelector(selected = mood, onSelect = { mood = it })
        EnergySelector(selected = energy, onSelect = { energy = it })
        TagSelector(
            selected = tags,
            onToggle = { tag ->
                tags = if (tag in tags) tags - tag else if (tags.size < 3) tags + tag else tags
            },
        )
        OutlinedTextField(
            value = note,
            onValueChange = {
                if (it.length <= 1200) note = it
                error = null
            },
            modifier = Modifier.fillMaxWidth().testTag("reflection-field"),
            label = { Text("What is on your mind?") },
            supportingText = { Text("${note.length}/1200 · write at least 3 characters") },
            minLines = 5,
            shape = RoundedCornerShape(20.dp),
            isError = error != null,
        )
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = {
                if (note.trim().length < 3) error = "Add a little more before saving."
                else {
                    onSave(mood, energy, note, tags)
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("save-reflection-button"),
            shape = RoundedCornerShape(18.dp),
        ) {
            Text("Save privately", modifier = Modifier.padding(vertical = 6.dp))
        }
    }
}
