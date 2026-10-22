package dev.abiekaputra.jiwoo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.abiekaputra.jiwoo.domain.DailySignal
import dev.abiekaputra.jiwoo.domain.WellbeingInsights
import dev.abiekaputra.jiwoo.ui.components.PageHeading
import dev.abiekaputra.jiwoo.ui.theme.JiwooCoral
import dev.abiekaputra.jiwoo.ui.theme.JiwooIndigo
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun InsightsScreen(insights: WellbeingInsights) {
    Column(
        modifier =
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .testTag("insights-screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        PageHeading(
            eyebrow = "Patterns, gently",
            title = "Your signals",
            description = "A private summary of what you recorded. It is context, not a diagnosis.",
        )
        if (insights.totalEntries == 0) EmptyInsights()
        else {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("Entries", insights.totalEntries.toString(), Modifier.weight(1f))
                MetricCard("Streak", "${insights.currentStreak} days", Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("Mood · 7d", decimal(insights.averageMood), Modifier.weight(1f))
                MetricCard("Energy · 7d", decimal(insights.averageEnergy), Modifier.weight(1f))
            }
            SignalChart(insights.dailySignals)
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("A theme worth noticing", fontWeight = FontWeight.Bold)
                    Text(
                        insights.commonTag?.let {
                            "${it.label} appears most often in your recent reflections. Read the entries before deciding what it means."
                        } ?: "Add a few tags to see which context appears most often.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(label: String, value: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun SignalChart(signals: List<DailySignal>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text("Recent rhythm", style = MaterialTheme.typography.titleLarge)
            Row(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                signals.takeLast(7).forEach { signal ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        Row(
                            modifier = Modifier.height(112.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            Bar(signal.mood, JiwooIndigo)
                            Bar(signal.energy, JiwooCoral)
                        }
                        Text(
                            signal.date.format(DAY_FORMAT),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Text("Indigo = mood  ·  Coral = energy", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun Bar(value: Double, color: androidx.compose.ui.graphics.Color) {
    Box(
        Modifier.height((value.coerceIn(1.0, 5.0) * 20).dp)
            .fillMaxWidth(0.34f)
            .background(color, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
    )
}

@Composable
private fun EmptyInsights() {
    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(24.dp)) {
        Text(
            "Your patterns will appear after you save reflections. Jiwoo never fills gaps with guesses.",
            modifier = Modifier.padding(22.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private fun decimal(value: Double): String = "${(value * 10).roundToInt() / 10.0}/5"

private val DAY_FORMAT = DateTimeFormatter.ofPattern("EEE")
