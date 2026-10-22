# Testing strategy

## Test pyramid

### Unit tests

- `CheckInTest` covers normalization and invalid energy, note, and tag counts.
- `CalculateInsightsTest` covers empty state, averages, common tags, and consecutive-day streaks.
- `OfflineJournalRepositoryTest` covers initialization, save, and delete with an in-memory storage boundary.
- `ReminderTimeTest` covers scheduling before and after the preferred hour.

### Android integration journey

`JiwooJourneyTest` drives the installed product through:

1. Read privacy-oriented onboarding.
2. Enter the empty Today state.
3. Create a reflection with mood, energy, tag, and text.
4. Confirm the saved state.
5. Read journal history and open one entry.
6. Inspect derived insights.
7. Inspect privacy and reminder settings.
8. Query the real database to verify the known note is not stored as plaintext.

The test also captures the eight screenshots committed in `docs/images`.

## Commands

```bash
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew connectedDebugAndroidTest
```

## Manual acceptance checklist

- The application opens without a server or account.
- The first-run promise is readable and does not make medical claims.
- A valid reflection survives process restart.
- Search matches local note text and tags.
- Entry detail and deletion are reachable through the UI.
- Insights respond to recorded entries and remain descriptive.
- Reminder denial does not block any journal feature.
- Navigation and controls remain usable on a phone-sized viewport.
