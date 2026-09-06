package com.udistrital.escondidas.domain

data class GameConfig(
    val timeLimitSeconds: Int = 30,
    val winToleranceDegrees: Float = 2.0f,
    val hotThresholdDegrees: Float = 40.0f,
    val warmThresholdDegrees: Float = 70.0f,
    val coldThresholdDegrees: Float = 110.0f
)
