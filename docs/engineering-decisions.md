# Engineering decisions

## Keep the product local

**Decision:** ship without an account, API, or sync service.

**Reason:** the central value is a low-friction private pause. Remote storage would add identity, consent, breach, retention, and key-recovery concerns before it added value to the core flow.

## Encrypt one payload per entry

**Decision:** keep only identifiers and timestamps outside the encrypted JSON payload.

**Reason:** a normalized plaintext schema would expose mood and themes. A separate searchable ciphertext index would increase complexity. Local decryption keeps this release understandable and appropriate for personal-scale data.

## Use SQLiteOpenHelper directly

**Decision:** implement the compact version-one schema without an ORM.

**Reason:** direct SQLite keeps encryption placement and stored columns visible. The storage interface allows Room or another implementation later if migrations and larger query needs justify it.

## Derive insight instead of scoring wellbeing

**Decision:** show averages, streak, daily rhythm, and the most common chosen tag.

**Reason:** these values are explainable from a person's own entries. Jiwoo avoids opaque scores, diagnosis language, and recommendations that the data cannot support.

## Schedule reminders with WorkManager

**Decision:** use unique periodic work with a preferred initial delay.

**Reason:** reflection reminders are deferrable. WorkManager respects device health and process restarts; exact-alarm permission would be disproportionate for this use case.
