package com.udistrital.escondidas.domain

enum class TemperatureState(val labelEs: String, val colorHex: String) {
    VERY_COLD("Muy Frío", "#0284C7"),
    COLD("Frío", "#06B6D4"),
    WARM("Tibio", "#F59E0B"),
    HOT("Caliente", "#EF4444"),
    FOUND("¡Encontrado!", "#10B981")
}
