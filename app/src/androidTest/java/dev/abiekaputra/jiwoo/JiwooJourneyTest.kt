package dev.abiekaputra.jiwoo

import android.database.sqlite.SQLiteDatabase
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import dev.abiekaputra.jiwoo.domain.CheckIn
import dev.abiekaputra.jiwoo.domain.Mood
import dev.abiekaputra.jiwoo.domain.ReflectionTag
import java.io.File
import java.time.Instant
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JiwooJourneyTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun completesPrivateReflectionJourneyAndCapturesEvidence() {
        waitForTag("onboarding-screen")
        capture("01-onboarding")
        composeRule.onNodeWithText("Begin privately").performClick()

        waitForTag("today-screen")
        composeRule.onNodeWithText("Nothing recorded yet").assertIsDisplayed()
        capture("02-today-empty")

        composeRule.onNodeWithTag("new-reflection-button").performClick()
        waitForTag("checkin-screen")
        composeRule.onNodeWithText("Light").performClick()
        composeRule.onNodeWithText("4").performClick()
        composeRule.onNodeWithText("Work").performClick()
        val reflection = "I made room to finish one important task without rushing the rest."
        composeRule.onNodeWithTag("reflection-field").performTextReplacement(reflection)
        composeRule.onNodeWithTag("reflection-field").assertTextContains(reflection)
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack()
        composeRule.waitForIdle()
        capture("03-check-in")
        composeRule.onNodeWithTag("save-reflection-button").performClick()

        waitForTag("today-screen")
        composeRule.onNodeWithText("Latest reflection").assertIsDisplayed()
        capture("04-today-saved")
        seedHistory()

        composeRule.onNodeWithTag("nav-journal").performClick()
        waitForTag("journal-screen")
        composeRule.waitForIdle()
        capture("05-journal")
        composeRule
            .onNodeWithText("I made room to finish one important task without rushing the rest.")
            .performClick()
        composeRule.onNodeWithText("Energy 4/5  ·  Work").assertIsDisplayed()
        capture("06-reflection-detail")
        composeRule.onNodeWithText("Close").performClick()

        composeRule.onNodeWithTag("nav-insights").performClick()
        waitForTag("insights-screen")
        composeRule.onNodeWithText("Recent rhythm").assertIsDisplayed()
        capture("07-insights")

        composeRule.onNodeWithTag("nav-settings").performClick()
        waitForTag("settings-screen")
        capture("08-settings")

        assertJournalPayloadIsEncrypted()
    }

    private fun seedHistory() {
        val application =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                as JiwooApplication
        runBlocking {
            listOf(
                    CheckIn(
                        id = "seed-rest",
                        mood = Mood.STEADY,
                        energy = 3,
                        note = "A slower evening helped me hear what I had been carrying.",
                        tags = setOf(ReflectionTag.REST),
                        createdAt = Instant.parse("2026-10-21T12:00:00Z"),
                    ),
                    CheckIn(
                        id = "seed-family",
                        mood = Mood.BRIGHT,
                        energy = 5,
                        note =
                            "Dinner felt easy and present. I want to remember that ordinary warmth.",
                        tags = setOf(ReflectionTag.FAMILY),
                        createdAt = Instant.parse("2026-10-20T12:00:00Z"),
                    ),
                    CheckIn(
                        id = "seed-growth",
                        mood = Mood.HEAVY,
                        energy = 2,
                        note =
                            "The difficult part was asking for help before the pressure became noise.",
                        tags = setOf(ReflectionTag.WORK, ReflectionTag.GROWTH),
                        createdAt = Instant.parse("2026-10-19T12:00:00Z"),
                    ),
                )
                .forEach { application.container.repository.save(it) }
        }
        composeRule.waitForIdle()
    }

    private fun assertJournalPayloadIsEncrypted() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database =
            SQLiteDatabase.openDatabase(
                context.getDatabasePath("jiwoo.db").path,
                null,
                SQLiteDatabase.OPEN_READONLY,
            )
        database.rawQuery("SELECT encrypted_payload FROM journal_entries", null).use { cursor ->
            while (cursor.moveToNext()) {
                assertFalse(cursor.getString(0).contains("important task"))
            }
        }
        database.close()
    }

    private fun capture(name: String) {
        composeRule.waitForIdle()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val directory = File(context.getExternalFilesDir(null), "screenshots").apply(File::mkdirs)
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .takeScreenshot(File(directory, "$name.png"))
    }

    private fun waitForTag(tag: String) {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
