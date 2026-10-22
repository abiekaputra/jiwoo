package dev.abiekaputra.jiwoo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.abiekaputra.jiwoo.data.UserSettings
import dev.abiekaputra.jiwoo.domain.CheckIn
import dev.abiekaputra.jiwoo.domain.Mood
import dev.abiekaputra.jiwoo.domain.ReflectionTag
import dev.abiekaputra.jiwoo.domain.WellbeingInsights
import java.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class JiwooUiState(
    val loading: Boolean = true,
    val entries: List<CheckIn> = emptyList(),
    val settings: UserSettings = UserSettings(),
    val insights: WellbeingInsights = WellbeingInsights(0, 0.0, 0.0, 0, null, emptyList()),
    val message: String? = null,
)

class JiwooViewModel(private val container: AppContainer) : ViewModel() {
    private val loading = MutableStateFlow(true)
    private val message = MutableStateFlow<String?>(null)
    val uiState: StateFlow<JiwooUiState> =
        combine(
                loading,
                container.repository.entries,
                container.preferences.settings,
                message,
            ) { isLoading, entries, settings, feedback ->
                JiwooUiState(
                    loading = isLoading,
                    entries = entries,
                    settings = settings,
                    insights = container.calculateInsights(entries),
                    message = feedback,
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), JiwooUiState())

    init {
        viewModelScope.launch {
            runCatching { container.repository.initialize() }
                .onFailure { message.value = "Your journal could not be opened." }
            loading.value = false
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch { container.preferences.completeOnboarding() }
    }

    fun save(mood: Mood, energy: Int, note: String, tags: Set<ReflectionTag>, at: Instant? = null) {
        viewModelScope.launch {
            runCatching {
                val timestamp = at ?: Instant.now()
                container.repository.save(
                    CheckIn(
                        mood = mood,
                        energy = energy,
                        note = note.trim(),
                        tags = tags,
                        createdAt = timestamp,
                        updatedAt = timestamp,
                    )
                )
            }
                .onSuccess { message.value = "Reflection saved privately." }
                .onFailure { message.value = it.message ?: "Reflection could not be saved." }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            runCatching { container.repository.delete(id) }
                .onSuccess { message.value = "Reflection deleted." }
                .onFailure { message.value = "Reflection could not be deleted." }
        }
    }

    fun setReminder(enabled: Boolean) {
        viewModelScope.launch {
            container.preferences.setReminderEnabled(enabled)
            if (enabled) container.reminderScheduler.scheduleDaily()
            else container.reminderScheduler.cancel()
            message.value = if (enabled) "Daily reminder set for 8:00 PM." else "Reminder disabled."
        }
    }

    fun consumeMessage() {
        message.value = null
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    JiwooViewModel(container) as T
            }
    }
}
