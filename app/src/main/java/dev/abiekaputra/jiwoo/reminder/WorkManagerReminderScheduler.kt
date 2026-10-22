package dev.abiekaputra.jiwoo.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class WorkManagerReminderScheduler(context: Context) : ReminderScheduler {
    private val workManager = WorkManager.getInstance(context)

    override fun scheduleDaily() {
        val delay = ReminderTime.delayUntilHour(ZonedDateTime.now(), REMINDER_HOUR)
        val work =
            PeriodicWorkRequestBuilder<ReflectionReminderWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delay.toMillis(), TimeUnit.MILLISECONDS)
                .addTag(WORK_NAME)
                .build()
        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            work,
        )
    }

    override fun cancel() {
        workManager.cancelUniqueWork(WORK_NAME)
    }

    private companion object {
        const val WORK_NAME = "jiwoo-daily-reflection"
        const val REMINDER_HOUR = 20
    }
}
