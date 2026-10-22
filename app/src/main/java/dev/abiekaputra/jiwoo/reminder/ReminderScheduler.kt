package dev.abiekaputra.jiwoo.reminder

interface ReminderScheduler {
    fun scheduleDaily()

    fun cancel()
}
