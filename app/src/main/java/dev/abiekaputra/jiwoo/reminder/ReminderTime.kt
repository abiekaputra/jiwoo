package dev.abiekaputra.jiwoo.reminder

import java.time.Duration
import java.time.ZonedDateTime

object ReminderTime {
    fun delayUntilHour(now: ZonedDateTime, hour: Int): Duration {
        require(hour in 0..23) { "Reminder hour must be valid." }
        var target = now.toLocalDate().atTime(hour, 0).atZone(now.zone)
        if (!target.isAfter(now)) target = target.plusDays(1)
        return Duration.between(now, target)
    }
}
