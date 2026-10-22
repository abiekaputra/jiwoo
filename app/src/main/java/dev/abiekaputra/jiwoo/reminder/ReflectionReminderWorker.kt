package dev.abiekaputra.jiwoo.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import dev.abiekaputra.jiwoo.R

class ReflectionReminderWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
) : Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        createChannel()
        if (!NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()) {
            return Result.success()
        }
        val notification =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_jiwoo_foreground)
                .setContentTitle(applicationContext.getString(R.string.reminder_title))
                .setContentText(applicationContext.getString(R.string.reminder_body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
        try {
            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
        } catch (_: SecurityException) {
            return Result.success()
        }
        return Result.success()
    }

    private fun createChannel() {
        val channel =
            NotificationChannel(
                    CHANNEL_ID,
                    applicationContext.getString(R.string.reminder_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT,
                )
                .apply {
                    description =
                        applicationContext.getString(R.string.reminder_channel_description)
                }
        applicationContext
            .getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private companion object {
        const val CHANNEL_ID = "jiwoo-reflection-reminders"
        const val NOTIFICATION_ID = 2107
    }
}
