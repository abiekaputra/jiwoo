package dev.abiekaputra.jiwoo.domain

import java.time.Instant
import java.util.UUID

data class CheckIn(
    val id: String = UUID.randomUUID().toString(),
    val mood: Mood,
    val energy: Int,
    val note: String,
    val tags: Set<ReflectionTag>,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = createdAt,
) {
    init {
        require(energy in 1..5) { "Energy must be between 1 and 5." }
        require(note.trim().length in 3..1200) { "Reflection must contain 3 to 1,200 characters." }
        require(tags.size <= 3) { "Choose no more than three tags." }
        require(updatedAt >= createdAt) { "Updated time cannot precede creation time." }
    }

    val preview: String
        get() = note.trim().replace(Regex("\\s+"), " ").take(100)
}
