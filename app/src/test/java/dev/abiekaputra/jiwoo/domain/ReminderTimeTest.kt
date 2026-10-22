package dev.abiekaputra.jiwoo.domain

import dev.abiekaputra.jiwoo.reminder.ReminderTime
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ReminderTimeTest {
    private val zone = ZoneId.of("Asia/Jakarta")

    @Test
    fun `targets the same day before reminder hour`() {
        val now = ZonedDateTime.of(2026, 10, 22, 18, 30, 0, 0, zone)

        assertEquals(Duration.ofMinutes(90), ReminderTime.delayUntilHour(now, 20))
    }

    @Test
    fun `rolls to tomorrow after reminder hour`() {
        val now = ZonedDateTime.of(2026, 10, 22, 21, 0, 0, 0, zone)

        assertEquals(Duration.ofHours(23), ReminderTime.delayUntilHour(now, 20))
    }
}
