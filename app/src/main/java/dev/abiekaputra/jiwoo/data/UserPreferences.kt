package dev.abiekaputra.jiwoo.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.jiwooPreferences by preferencesDataStore(name = "jiwoo_preferences")

data class UserSettings(
    val onboardingComplete: Boolean = false,
    val reminderEnabled: Boolean = false,
)

class UserPreferences(private val context: Context) {
    val settings: Flow<UserSettings> =
        context.jiwooPreferences.data.map { preferences ->
            UserSettings(
                onboardingComplete = preferences[ONBOARDING_COMPLETE] ?: false,
                reminderEnabled = preferences[REMINDER_ENABLED] ?: false,
            )
        }

    suspend fun completeOnboarding() {
        context.jiwooPreferences.edit { it[ONBOARDING_COMPLETE] = true }
    }

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.jiwooPreferences.edit { it[REMINDER_ENABLED] = enabled }
    }

    private companion object {
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
    }
}
