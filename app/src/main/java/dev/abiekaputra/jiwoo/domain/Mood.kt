package dev.abiekaputra.jiwoo.domain

enum class Mood(val score: Int, val label: String, val symbol: String) {
    LOW(1, "Low", "●"),
    HEAVY(2, "Heavy", "◒"),
    STEADY(3, "Steady", "◐"),
    LIGHT(4, "Light", "◕"),
    BRIGHT(5, "Bright", "○");

    companion object {
        fun fromScore(score: Int): Mood = entries.firstOrNull { it.score == score } ?: STEADY
    }
}
