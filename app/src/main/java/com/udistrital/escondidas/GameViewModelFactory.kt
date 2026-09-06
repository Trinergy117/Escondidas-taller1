package com.udistrital.escondidas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udistrital.escondidas.data.SensorRepository

class GameViewModelFactory(
    private val sensorRepository: SensorRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(sensorRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
