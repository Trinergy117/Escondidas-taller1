package com.udistrital.escondidas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.udistrital.escondidas.data.SensorRepository
import com.udistrital.escondidas.ui.GameScreen
import com.udistrital.escondidas.ui.HowToPlayScreen
import com.udistrital.escondidas.ui.MenuScreen
import com.udistrital.escondidas.ui.SettingsScreen
import com.udistrital.escondidas.ui.theme.EscondidasTheme

enum class Screen {
    MENU, GAME, SETTINGS, HOW_TO
}

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(SensorRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf(Screen.MENU) }

            EscondidasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        Screen.MENU -> MenuScreen(
                            onNavigateToGame = { currentScreen = Screen.GAME },
                            onNavigateToSettings = { currentScreen = Screen.SETTINGS },
                            onNavigateToHowToPlay = { currentScreen = Screen.HOW_TO }
                        )
                        Screen.GAME -> GameScreen(
                            viewModel = viewModel,
                            onBackToMenu = { currentScreen = Screen.MENU },
                            modifier = Modifier.padding(innerPadding)
                        )
                        Screen.SETTINGS -> SettingsScreen(
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
}
