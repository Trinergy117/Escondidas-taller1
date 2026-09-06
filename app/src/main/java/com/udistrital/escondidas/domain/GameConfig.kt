package com.udistrital.escondidas.domain

data class GameConfig(
    val timeLimitSeconds: Int = 60,
    val winToleranceDegrees: Float = 6.0f,
    val hotThresholdDegrees: Float = 20.0f,
    val warmThresholdDegrees: Float = 50.0f,
    val coldThresholdDegrees: Float = 90.0f
)
