package com.udistrital.escondidas

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import com.udistrital.escondidas.data.SensorRepository
import com.udistrital.escondidas.domain.Difficulty
import com.udistrital.escondidas.ui.GameScreen
import com.udistrital.escondidas.ui.HowToPlayScreen
import com.udistrital.escondidas.ui.MenuScreen
import com.udistrital.escondidas.ui.SettingsScreen
import com.udistrital.escondidas.ui.theme.EscondidasTheme

enum class Screen {
    MENU, GAME, SETTINGS, HOW_TO
}

class MainActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(SensorRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf(Screen.MENU) }
            val currentLanguage = AppCompatDelegate.getApplicationLocales().get(0)?.language ?: "es"
            var isVibrationEnabled by remember { mutableStateOf(true) }

            EscondidasTheme {
                when (currentScreen) {
                    Screen.MENU -> MenuScreen(
                        onNavigateToGame = { currentScreen = Screen.GAME },
                        onNavigateToSettings = { currentScreen = Screen.SETTINGS },
                        onNavigateToHowToPlay = { currentScreen = Screen.HOW_TO }
                    )
                    Screen.GAME -> GameScreen(
                        viewModel = viewModel,
                        isVibrationEnabled = isVibrationEnabled,
                        onBackToMenu = { currentScreen = Screen.MENU }
                    )
                    Screen.SETTINGS -> SettingsScreen(
                        currentLanguage = currentLanguage,
                        onLanguageChange = { lang ->
                            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lang)
                            AppCompatDelegate.setApplicationLocales(appLocale)
                        },
                        isVibrationEnabled = isVibrationEnabled,
                        onVibrationChange = { isVibrationEnabled = it },
                        onBack = { currentScreen = Screen.MENU }
                    )
                    Screen.HOW_TO -> HowToPlayScreen(
                        onBack = { currentScreen = Screen.MENU }
                    )
                }
            }
        }
    }
}
