#!/usr/bin/env bash
set -euo pipefail

python3 scripts/check_file_lengths.py
./gradlew ktfmtCheck testDebugUnitTest lintDebug assembleDebug
