package dev.abiekaputra.jiwoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abiekaputra.jiwoo.domain.CheckIn
import dev.abiekaputra.jiwoo.ui.components.EntryCard
import dev.abiekaputra.jiwoo.ui.components.JiwooTopBar
import dev.abiekaputra.jiwoo.ui.components.PageHeading
import dev.abiekaputra.jiwoo.ui.theme.JiwooIndigo

@Composable
fun TodayScreen(entries: List<CheckIn>, onNewReflection: () -> Unit, onOpenJournal: () -> Unit) {
    Column(
        modifier =
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .testTag("today-screen"),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        JiwooTopBar()
        PageHeading(
            eyebrow = "A moment for yourself",
            title = "How are you arriving today?",
            description = "Name the moment as it is. You do not need to make it sound better.",
        )
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = JiwooIndigo,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Text("ONE QUIET QUESTION", style = MaterialTheme.typography.labelSmall)
                Text(
                    "What deserves your attention before the day moves on?",
                    style = MaterialTheme.typography.titleLarge,
                )
                Button(
                    onClick = onNewReflection,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                    modifier = Modifier.testTag("new-reflection-button"),
                ) {
                    Icon(Icons.Outlined.Edit, contentDescription = null)
                    Text("  New reflection")
                }
            }
        }
        if (entries.isEmpty()) {
            EmptyToday(onNewReflection)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Latest reflection", style = MaterialTheme.typography.titleLarge)
                    OutlinedButton(
                        onClick = onOpenJournal,
                        modifier = Modifier.testTag("open-journal-button"),
                    ) {
                        Text("Journal")
                        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
                    }
                }
                EntryCard(entry = entries.first(), onClick = onOpenJournal)
            }
        }
    }
}

@Composable
private fun EmptyToday(onNewReflection: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Nothing recorded yet", fontWeight = FontWeight.SemiBold)
            Text(
                "Your first check-in takes about a minute and stays on this device.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = onNewReflection) { Text("Start with today") }
        }
    }
}
