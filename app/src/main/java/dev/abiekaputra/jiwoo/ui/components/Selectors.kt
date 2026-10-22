package dev.abiekaputra.jiwoo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abiekaputra.jiwoo.domain.Mood
import dev.abiekaputra.jiwoo.domain.ReflectionTag

@Composable
fun MoodSelector(selected: Mood, onSelect: (Mood) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("How do you feel?", style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Mood.entries.forEach { mood ->
                MoodOption(mood = mood, selected = selected == mood, onSelect = onSelect)
            }
        }
    }
}

@Composable
private fun MoodOption(mood: Mood, selected: Boolean, onSelect: (Mood) -> Unit) {
    OutlinedButton(
        onClick = { onSelect(mood) },
        modifier = Modifier.padding(horizontal = 1.dp),
        shape = RoundedCornerShape(18.dp),
        border =
            BorderStroke(
                1.dp,
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
            ),
        contentPadding =
            androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 10.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(mood.symbol, fontWeight = FontWeight.Bold)
            Text(mood.label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun EnergySelector(selected: Int, onSelect: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Energy right now", style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            (1..5).forEach { energy ->
                Surface(
                    onClick = { onSelect(energy) },
                    modifier = Modifier.weight(1f),
                    color =
                        if (selected == energy) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor =
                        if (selected == energy) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text(
                        text = energy.toString(),
                        modifier = Modifier.padding(vertical = 13.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun TagSelector(selected: Set<ReflectionTag>, onToggle: (ReflectionTag) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("What shaped today? · up to 3", style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ReflectionTag.entries.forEach { tag ->
                Surface(
                    onClick = { onToggle(tag) },
                    color =
                        if (tag in selected) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(999.dp),
                ) {
                    Text(
                        tag.label,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    )
                }
            }
        }
    }
}
