package dev.abiekaputra.jiwoo.domain

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CheckInTest {
    @Test
    fun `creates a trimmed readable preview`() {
        val entry = entry(note = "  A day   with enough space to notice what mattered.  ")

        assertEquals("A day with enough space to notice what mattered.", entry.preview)
    }

    @Test
    fun `rejects invalid energy`() {
        assertThrows(IllegalArgumentException::class.java) { entry(energy = 0) }
        assertThrows(IllegalArgumentException::class.java) { entry(energy = 6) }
    }

    @Test
    fun `rejects an empty reflection and more than three tags`() {
        assertThrows(IllegalArgumentException::class.java) { entry(note = " ") }
        assertThrows(IllegalArgumentException::class.java) {
            entry(tags = ReflectionTag.entries.take(4).toSet())
        }
    }

    private fun entry(
        note: String = "A valid reflection.",
        energy: Int = 3,
        tags: Set<ReflectionTag> = emptySet(),
    ) =
        CheckIn(
            mood = Mood.STEADY,
            energy = energy,
            note = note,
            tags = tags,
            createdAt = Instant.parse("2026-10-22T08:00:00Z"),
        )
}
