package com.udistrital.escondidas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.escondidas.data.SensorRepository
import com.udistrital.escondidas.domain.Difficulty
import com.udistrital.escondidas.domain.GameConfig
import com.udistrital.escondidas.domain.GameEngine
import com.udistrital.escondidas.domain.GameState
import com.udistrital.escondidas.domain.TemperatureState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val sensorRepository: SensorRepository,
    initialConfig: GameConfig = GameConfig()
) : ViewModel() {

    private var gameEngine = GameEngine(initialConfig)
    private var gameConfig = initialConfig
    private val _gameState = MutableStateFlow(GameState(remainingTimeSeconds = initialConfig.timeLimitSeconds))
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun setDifficulty(difficulty: Difficulty) {
        val newConfig = when (difficulty) {
            Difficulty.EASY -> GameConfig(timeLimitSeconds = 60, winToleranceDegrees = 5.0f, hotThresholdDegrees = 60.0f, warmThresholdDegrees = 100.0f, coldThresholdDegrees = 150.0f)
            Difficulty.NORMAL -> GameConfig(timeLimitSeconds = 45, winToleranceDegrees = 3.0f, hotThresholdDegrees = 40.0f, warmThresholdDegrees = 70.0f, coldThresholdDegrees = 110.0f)
            Difficulty.HARD -> GameConfig(timeLimitSeconds = 30, winToleranceDegrees = 2.0f, hotThresholdDegrees = 20.0f, warmThresholdDegrees = 45.0f, coldThresholdDegrees = 80.0f)
        }
        gameConfig = newConfig
        gameEngine = GameEngine(newConfig)
        resetGame()
    }

    private var timerJob: Job? = null
    private var sensorJob: Job? = null

    fun startNewGame() {
        val currentAzimuth = _gameState.value.currentAzimuth
        stopGameSession()
        val target = gameEngine.generateRandomTarget(currentAzimuth)
        _gameState.value = GameState(
            targetAzimuth = target,
            currentAzimuth = currentAzimuth,
            remainingTimeSeconds = gameConfig.timeLimitSeconds,
            isGameActive = true
        )
        startTimer()
        subscribeToSensors()
    }

    fun resetGame() {
        val currentAzimuth = _gameState.value.currentAzimuth
        stopGameSession()
        _gameState.value = GameState(
            currentAzimuth = currentAzimuth,
            remainingTimeSeconds = gameConfig.timeLimitSeconds
        )
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var elapsed = 0
            while (elapsed < gameConfig.timeLimitSeconds && _gameState.value.isGameActive) {
                delay(1000L)
                elapsed++
                val current = _gameState.value
                val delta = gameEngine.calculateAngularDelta(current.currentAzimuth, current.targetAzimuth)
                val isWon = delta <= gameConfig.winToleranceDegrees
                val isTimeOut = elapsed >= gameConfig.timeLimitSeconds
                val precision = gameEngine.calculatePrecision(delta)

                _gameState.value = current.copy(
                    timeElapsedSeconds = elapsed,
                    remainingTimeSeconds = (gameConfig.timeLimitSeconds - elapsed).coerceAtLeast(0),
                    isWon = isWon,
                    isGameOver = isWon || isTimeOut,
                    isGameActive = !(isWon || isTimeOut),
                    score = if (isWon) gameEngine.calculateScore(elapsed, precision) else current.score
                )
                if (isWon || isTimeOut) break
            }
        }
    }

    private fun subscribeToSensors() {
        sensorJob?.cancel()
        sensorJob = viewModelScope.launch {
            sensorRepository.getOrientationFlow().collect { azimuth ->
                val current = _gameState.value
                if (current.isGameActive) {
                    val delta = gameEngine.calculateAngularDelta(azimuth, current.targetAzimuth)
                    val temp = gameEngine.evaluateTemperature(delta)
                    val precision = gameEngine.calculatePrecision(delta)
                    val isWon = delta <= gameConfig.winToleranceDegrees

                    _gameState.value = current.copy(
                        currentAzimuth = azimuth,
                        deltaAngle = delta,
                        precisionPercentage = precision,
                        temperatureState = if (isWon) TemperatureState.FOUND else temp,
                        isWon = isWon,
                        isGameOver = isWon,
                        isGameActive = !isWon,
                        score = if (isWon) gameEngine.calculateScore(current.timeElapsedSeconds, precision) else current.score
                    )
                    if (isWon) stopGameSession()
                }
            }
        }
    }

    fun stopGameSession() {
        timerJob?.cancel()
        sensorJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        stopGameSession()
    }
}