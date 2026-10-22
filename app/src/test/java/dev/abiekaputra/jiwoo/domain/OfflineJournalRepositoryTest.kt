package dev.abiekaputra.jiwoo.domain

import dev.abiekaputra.jiwoo.data.JournalStorage
import dev.abiekaputra.jiwoo.data.OfflineJournalRepository
import java.time.Instant
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class OfflineJournalRepositoryTest {
    @Test
    fun `refreshes observable entries after save and delete`() = runTest {
        val storage = MemoryStorage()
        val repository = OfflineJournalRepository(storage, StandardTestDispatcher(testScheduler))
        val entry =
            CheckIn(
                id = "entry-1",
                mood = Mood.LIGHT,
                energy = 4,
                note = "A useful pause.",
                tags = setOf(ReflectionTag.REST),
                createdAt = Instant.parse("2026-10-22T08:00:00Z"),
            )

        repository.initialize()
        repository.save(entry)
        assertEquals(listOf(entry), repository.entries.value)

        repository.delete(entry.id)
        assertEquals(emptyList<CheckIn>(), repository.entries.value)
    }
}

private class MemoryStorage : JournalStorage {
    private val entries = linkedMapOf<String, CheckIn>()

    override fun readAll(): List<CheckIn> = entries.values.toList()

    override fun upsert(entry: CheckIn) {
        entries[entry.id] = entry
    }

    override fun delete(id: String) {
        entries.remove(id)
    }
}
