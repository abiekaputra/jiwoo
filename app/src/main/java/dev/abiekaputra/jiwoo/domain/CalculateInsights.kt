package dev.abiekaputra.jiwoo.domain

import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

data class DailySignal(val date: LocalDate, val mood: Double, val energy: Double)

data class WellbeingInsights(
    val totalEntries: Int,
    val averageMood: Double,
    val averageEnergy: Double,
    val currentStreak: Int,
    val commonTag: ReflectionTag?,
    val dailySignals: List<DailySignal>,
)

class CalculateInsights(
    private val clock: Clock = Clock.systemDefaultZone(),
    private val zoneId: ZoneId = ZoneId.systemDefault(),
) {
    operator fun invoke(entries: List<CheckIn>): WellbeingInsights {
        if (entries.isEmpty()) return emptyInsights()
        val recent = entries.filter {
            it.createdAt.isAfter(clock.instant().minusSeconds(7 * 86_400L))
        }
        val source = recent.ifEmpty { entries }
        val grouped =
            source
                .groupBy { ZonedDateTime.ofInstant(it.createdAt, zoneId).toLocalDate() }
                .map { (date, dayEntries) ->
                    DailySignal(
                        date = date,
                        mood = dayEntries.map { it.mood.score }.average(),
                        energy = dayEntries.map { it.energy }.average(),
                    )
                }
                .sortedBy { it.date }
        return WellbeingInsights(
            totalEntries = entries.size,
            averageMood = source.map { it.mood.score }.average(),
            averageEnergy = source.map { it.energy }.average(),
            currentStreak = calculateStreak(entries),
            commonTag =
                source
                    .flatMap { it.tags }
                    .groupingBy { it }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key,
            dailySignals = grouped,
        )
    }

    private fun calculateStreak(entries: List<CheckIn>): Int {
        val dates =
            entries.map { ZonedDateTime.ofInstant(it.createdAt, zoneId).toLocalDate() }.toSet()
        var cursor = LocalDate.now(clock)
        if (cursor !in dates) cursor = cursor.minusDays(1)
        var streak = 0
        while (cursor in dates) {
            streak += 1
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    private fun emptyInsights() = WellbeingInsights(0, 0.0, 0.0, 0, null, emptyList())
}
