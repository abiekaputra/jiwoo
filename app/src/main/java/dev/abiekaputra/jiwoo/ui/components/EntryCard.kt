package dev.abiekaputra.jiwoo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abiekaputra.jiwoo.domain.CheckIn
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EntryCard(entry: CheckIn, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Text(entry.mood.symbol, fontWeight = FontWeight.Bold)
                    Text(entry.mood.label, fontWeight = FontWeight.SemiBold)
                }
                Text(
                    DATE_FORMAT.format(entry.createdAt.atZone(ZoneId.systemDefault())),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            Text(entry.preview, style = MaterialTheme.typography.bodyLarge)
            if (entry.tags.isNotEmpty()) {
                Text(
                    entry.tags.joinToString("  ·  ") { it.label },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

private val DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM · HH:mm")
