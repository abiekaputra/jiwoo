package dev.abiekaputra.jiwoo.domain

import kotlinx.coroutines.flow.StateFlow

interface JournalRepository {
    val entries: StateFlow<List<CheckIn>>

    suspend fun initialize()

    suspend fun save(entry: CheckIn)

    suspend fun delete(id: String)
}
