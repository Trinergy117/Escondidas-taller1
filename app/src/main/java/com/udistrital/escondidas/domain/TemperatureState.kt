package com.udistrital.escondidas.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.udistrital.escondidas.R

enum class TemperatureState(val colorHex: String, val stringRes: Int) {
    VERY_COLD("#0284C7", R.string.temp_very_cold),
    COLD("#06B6D4", R.string.temp_cold),
    WARM("#F59E0B", R.string.temp_warm),
    HOT("#EF4444", R.string.temp_hot),
    FOUND("#10B981", R.string.temp_found);

    @Composable
    fun getLabel(): String = stringResource(id = stringRes)
}
