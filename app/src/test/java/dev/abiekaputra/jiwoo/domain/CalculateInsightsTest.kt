package dev.abiekaputra.jiwoo.domain

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CalculateInsightsTest {
    private val clock = Clock.fixed(Instant.parse("2026-10-22T12:00:00Z"), ZoneId.of("UTC"))
    private val subject = CalculateInsights(clock, ZoneId.of("UTC"))

    @Test
    fun `returns an honest empty result`() {
        val result = subject(emptyList())

        assertEquals(0, result.totalEntries)
        assertEquals(0.0, result.averageMood, 0.0)
        assertNull(result.commonTag)
    }

    @Test
    fun `calculates recent averages and the common context`() {
        val result =
            subject(
                listOf(
                    entry("2026-10-22T08:00:00Z", Mood.LIGHT, 4, ReflectionTag.WORK),
                    entry("2026-10-21T08:00:00Z", Mood.STEADY, 2, ReflectionTag.WORK),
                    entry("2026-10-20T08:00:00Z", Mood.BRIGHT, 5, ReflectionTag.REST),
                )
            )

        assertEquals(4.0, result.averageMood, 0.01)
        assertEquals(3.67, result.averageEnergy, 0.01)
        assertEquals(ReflectionTag.WORK, result.commonTag)
        assertEquals(3, result.dailySignals.size)
    }

    @Test
    fun `counts a consecutive streak ending today`() {
        val result =
            subject(
                listOf(
                    entry("2026-10-22T08:00:00Z", Mood.STEADY, 3),
                    entry("2026-10-21T08:00:00Z", Mood.STEADY, 3),
                    entry("2026-10-19T08:00:00Z", Mood.STEADY, 3),
                )
            )

        assertEquals(2, result.currentStreak)
    }

    private fun entry(
        at: String,
        mood: Mood,
        energy: Int,
        tag: ReflectionTag? = null,
    ) =
        CheckIn(
            mood = mood,
            energy = energy,
            note = "A valid reflection.",
            tags = tag?.let(::setOf).orEmpty(),
            createdAt = Instant.parse(at),
        )
}
