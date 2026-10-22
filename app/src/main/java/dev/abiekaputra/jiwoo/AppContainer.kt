package dev.abiekaputra.jiwoo

import android.content.Context
import dev.abiekaputra.jiwoo.data.KeystoreJournalCipher
import dev.abiekaputra.jiwoo.data.OfflineJournalRepository
import dev.abiekaputra.jiwoo.data.SQLiteJournalStorage
import dev.abiekaputra.jiwoo.data.UserPreferences
import dev.abiekaputra.jiwoo.domain.CalculateInsights
import dev.abiekaputra.jiwoo.domain.JournalRepository
import dev.abiekaputra.jiwoo.reminder.ReminderScheduler
import dev.abiekaputra.jiwoo.reminder.WorkManagerReminderScheduler

class AppContainer(context: Context) {
    val repository: JournalRepository =
        OfflineJournalRepository(SQLiteJournalStorage(context, KeystoreJournalCipher()))
    val preferences = UserPreferences(context)
    val reminderScheduler: ReminderScheduler = WorkManagerReminderScheduler(context)
    val calculateInsights = CalculateInsights()
}
