package com.udistrital.escondidas.domain

enum class Difficulty {
    EASY, NORMAL, HARD
}

data class GameConfig(
    val timeLimitSeconds: Int = 30,
    val winToleranceDegrees: Float = 2.0f,
    val hotThresholdDegrees: Float = 50.0f,
    val warmThresholdDegrees: Float = 80.0f,
    val coldThresholdDegrees: Float = 130.0f
)
