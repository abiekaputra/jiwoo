package dev.abiekaputra.jiwoo.data

import dev.abiekaputra.jiwoo.domain.CheckIn

interface JournalStorage {
    fun readAll(): List<CheckIn>

    fun upsert(entry: CheckIn)

    fun delete(id: String)
}
