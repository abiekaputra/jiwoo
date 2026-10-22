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
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.EnhancedEncryption
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abiekaputra.jiwoo.ui.components.PageHeading

@Composable
fun SettingsScreen(reminderEnabled: Boolean, onReminderChange: (Boolean) -> Unit) {
    Column(
        modifier =
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .testTag("settings-screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        PageHeading(
            eyebrow = "Your boundaries",
            title = "Privacy & rhythm",
            description = "Choose a gentle reminder and see exactly what Jiwoo stores.",
        )
        Surface(
            shape = RoundedCornerShape(24.dp),
            border =
                androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null)
                    Column {
                        Text("Daily reflection", fontWeight = FontWeight.SemiBold)
                        Text(
                            "A local notification at 8:00 PM",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Switch(
                    checked = reminderEnabled,
                    onCheckedChange = onReminderChange,
                    modifier = Modifier.testTag("reminder-switch"),
                )
            }
        }
        Text("Privacy model", style = MaterialTheme.typography.titleLarge)
        PrivacyRow(
            icon = Icons.Outlined.EnhancedEncryption,
            title = "Device-bound encryption",
            body =
                "Mood, energy, tags, and journal text are encrypted with an AES-GCM key held by Android Keystore.",
        )
        PrivacyRow(
            icon = Icons.Outlined.CloudOff,
            title = "No cloud account",
            body = "Jiwoo works offline. This release does not send or synchronize journal data.",
        )
        PrivacyRow(
            icon = Icons.Outlined.VisibilityOff,
            title = "No analytics SDK",
            body = "There is no behavioral tracking, advertising identifier, or remote telemetry.",
        )
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text("Wellbeing boundary", fontWeight = FontWeight.Bold)
                Text(
                    "Jiwoo supports reflection. It does not diagnose, provide treatment, or replace professional care.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            "Jiwoo 1.0.0 · local portfolio release",
            modifier = Modifier.padding(bottom = 110.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun PrivacyRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    body: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(14.dp),
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.padding(11.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
