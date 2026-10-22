package dev.abiekaputra.jiwoo.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dev.abiekaputra.jiwoo.domain.CheckIn
import dev.abiekaputra.jiwoo.domain.Mood
import dev.abiekaputra.jiwoo.domain.ReflectionTag
import java.time.Instant
import org.json.JSONArray
import org.json.JSONObject

class SQLiteJournalStorage(context: Context, private val cipher: JournalCipher) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), JournalStorage {
    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE journal_entries (
                id TEXT PRIMARY KEY NOT NULL,
                encrypted_payload TEXT NOT NULL,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """
                .trimIndent()
        )
        database.execSQL("CREATE INDEX idx_journal_created ON journal_entries(created_at DESC)")
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit

    override fun readAll(): List<CheckIn> {
        val entries = mutableListOf<CheckIn>()
        readableDatabase
            .query(
                "journal_entries",
                arrayOf("id", "encrypted_payload", "created_at", "updated_at"),
                null,
                null,
                null,
                null,
                "created_at DESC",
            )
            .use { cursor ->
                while (cursor.moveToNext()) {
                    entries +=
                        decode(
                            id = cursor.getString(0),
                            payload = cursor.getString(1),
                            createdAt = cursor.getLong(2),
                            updatedAt = cursor.getLong(3),
                        )
                }
            }
        return entries
    }

    override fun upsert(entry: CheckIn) {
        val values =
            ContentValues().apply {
                put("id", entry.id)
                put("encrypted_payload", cipher.encrypt(encode(entry)))
                put("created_at", entry.createdAt.toEpochMilli())
                put("updated_at", entry.updatedAt.toEpochMilli())
            }
        writableDatabase.insertWithOnConflict(
            "journal_entries",
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE,
        )
    }

    override fun delete(id: String) {
        writableDatabase.delete("journal_entries", "id = ?", arrayOf(id))
    }

    private fun encode(entry: CheckIn): String =
        JSONObject()
            .put("mood", entry.mood.name)
            .put("energy", entry.energy)
            .put("note", entry.note)
            .put("tags", JSONArray(entry.tags.map(ReflectionTag::name)))
            .toString()

    private fun decode(id: String, payload: String, createdAt: Long, updatedAt: Long): CheckIn {
        val json = JSONObject(cipher.decrypt(payload))
        val tags = json.getJSONArray("tags")
        return CheckIn(
            id = id,
            mood = Mood.valueOf(json.getString("mood")),
            energy = json.getInt("energy"),
            note = json.getString("note"),
            tags =
                (0 until tags.length()).map { ReflectionTag.valueOf(tags.getString(it)) }.toSet(),
            createdAt = Instant.ofEpochMilli(createdAt),
            updatedAt = Instant.ofEpochMilli(updatedAt),
        )
    }

    private companion object {
        const val DATABASE_NAME = "jiwoo.db"
        const val DATABASE_VERSION = 1
    }
}
