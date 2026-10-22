package dev.abiekaputra.jiwoo.data

import dev.abiekaputra.jiwoo.domain.CheckIn
import dev.abiekaputra.jiwoo.domain.JournalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class OfflineJournalRepository(
    private val storage: JournalStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : JournalRepository {
    private val mutableEntries = MutableStateFlow<List<CheckIn>>(emptyList())
    override val entries: StateFlow<List<CheckIn>> = mutableEntries.asStateFlow()

    override suspend fun initialize() = withContext(dispatcher) { refresh() }

    override suspend fun save(entry: CheckIn) =
        withContext(dispatcher) {
            storage.upsert(entry)
            refresh()
        }

    override suspend fun delete(id: String) =
        withContext(dispatcher) {
            storage.delete(id)
            refresh()
        }

    private fun refresh() {
        mutableEntries.value = storage.readAll()
    }
}
