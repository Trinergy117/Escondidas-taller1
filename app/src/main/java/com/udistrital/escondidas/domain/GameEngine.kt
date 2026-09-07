package com.udistrital.escondidas.domain

import kotlin.math.abs
import kotlin.math.roundToInt

class GameEngine(val config: GameConfig = GameConfig()) {

    fun calculateAngularDelta(currentAzimuth: Float, targetAzimuth: Float): Float {
        val normCurrent = (currentAzimuth % 360 + 360) % 360
        val normTarget = (targetAzimuth % 360 + 360) % 360
        val diff = abs(normCurrent - normTarget)
        return if (diff > 180.0f) 360.0f - diff else diff
    }

    fun evaluateTemperature(deltaAngle: Float): TemperatureState {
        return when {
            deltaAngle <= config.winToleranceDegrees -> TemperatureState.FOUND
            deltaAngle <= config.hotThresholdDegrees -> TemperatureState.HOT
            deltaAngle <= config.warmThresholdDegrees -> TemperatureState.WARM
            deltaAngle <= config.coldThresholdDegrees -> TemperatureState.COLD
            else -> TemperatureState.VERY_COLD
        }
    }

    fun calculatePrecision(deltaAngle: Float): Float {
        val clampedDelta = deltaAngle.coerceIn(0.0f, 180.0f)
        val precision = ((180.0f - clampedDelta) / 180.0f) * 100.0f
        return (precision * 10.0f).roundToInt() / 10.0f
    }

    fun calculateScore(timeElapsedSeconds: Int, precisionPercentage: Float): Int {
        val timeRemaining = (config.timeLimitSeconds - timeElapsedSeconds).coerceAtLeast(0)
        val timeRatio = timeRemaining.toFloat() / config.timeLimitSeconds.toFloat()
        
        val baseScore = 300
        val timeBonus = (timeRatio * 500).roundToInt()
        val precisionBonus = ((precisionPercentage / 100.0f) * 200).roundToInt()
        
        return baseScore + timeBonus + precisionBonus
    }

    fun generateRandomTarget(currentAzimuth: Float = 0.0f): Float {
        val minDistance = 100.0f // Asegurar al menos 100 grados de distancia inicial
        var newTarget: Float
        var attempts = 0
        do {
            newTarget = (0..359).random().toFloat()
            attempts++
        } while (calculateAngularDelta(currentAzimuth, newTarget) < minDistance && attempts < 100)
        return newTarget
    }
}
