#!/usr/bin/env python3
"""Enforce the repository's source-file size limits."""

from pathlib import Path
import sys


ROOT = Path(__file__).resolve().parents[1]
SOURCE_ROOTS = (ROOT / "app" / "src" / "main",)
TEST_ROOTS = (ROOT / "app" / "src" / "test", ROOT / "app" / "src" / "androidTest")
SOURCE_SUFFIXES = {".kt", ".java"}


def violations(roots: tuple[Path, ...], limit: int) -> list[str]:
    failures = []
    for root in roots:
        for path in sorted(root.rglob("*")):
            if not path.is_file() or path.suffix not in SOURCE_SUFFIXES:
                continue
            count = len(path.read_text(encoding="utf-8").splitlines())
            if count > limit:
                failures.append(f"{path.relative_to(ROOT)}: {count} lines (limit {limit})")
    return failures


def main() -> int:
    failures = violations(SOURCE_ROOTS, 300) + violations(TEST_ROOTS, 1000)
    if failures:
        print("File length violations:")
        print("\n".join(f"- {failure}" for failure in failures))
        return 1
    print("File length limits passed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
