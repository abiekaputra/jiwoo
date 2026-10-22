package dev.abiekaputra.jiwoo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abiekaputra.jiwoo.ui.components.JiwooMark
import dev.abiekaputra.jiwoo.ui.theme.JiwooIndigo

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    Column(
        modifier =
            Modifier.fillMaxSize()
                .background(JiwooIndigo)
                .padding(horizontal = 24.dp, vertical = 40.dp)
                .testTag("onboarding-screen"),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            JiwooMark(size = 76)
            Text(
                text = "A quieter place\nfor what is real.",
                color = Color.White,
                style = MaterialTheme.typography.displaySmall,
            )
            Text(
                text =
                    "Jiwoo helps you notice patterns without turning a personal moment into a public profile.",
                color = Color.White.copy(alpha = 0.76f),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Surface(
            color = Color.White.copy(alpha = 0.10f),
            shape = RoundedCornerShape(28.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                OnboardingPoint(
                    Icons.Outlined.Lock,
                    "Encrypted reflections",
                    "Journal text is protected with a device-bound key.",
                )
                OnboardingPoint(
                    Icons.Outlined.CloudOff,
                    "Offline by design",
                    "No account, cloud sync, tracker, or advertising SDK.",
                )
                OnboardingPoint(
                    Icons.Outlined.SelfImprovement,
                    "Reflection, not diagnosis",
                    "Jiwoo shows your own patterns and makes no medical claim.",
                )
            }
        }

        Column {
            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().height(56.dp).testTag("begin-button"),
                shape = RoundedCornerShape(18.dp),
            ) {
                Text("Begin privately", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "You stay in control. Entries can be deleted at any time.",
                color = Color.White.copy(alpha = 0.66f),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
private fun OnboardingPoint(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    body: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Icon(icon, contentDescription = null, tint = Color(0xFFFF8B82))
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(
                body,
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
