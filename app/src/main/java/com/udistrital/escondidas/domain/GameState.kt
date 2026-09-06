package com.udistrital.escondidas.domain

data class GameState(
    val targetAzimuth: Float = 0.0f,
    val currentAzimuth: Float = 0.0f,
    val deltaAngle: Float = 180.0f,
    val remainingTimeSeconds: Int = 60,
    val timeElapsedSeconds: Int = 0,
    val score: Int = 0,
    val precisionPercentage: Float = 0.0f,
    val temperatureState: TemperatureState = TemperatureState.VERY_COLD,
    val isGameActive: Boolean = false,
    val isWon: Boolean = false,
    val isGameOver: Boolean = false
)
