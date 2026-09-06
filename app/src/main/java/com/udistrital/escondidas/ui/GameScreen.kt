package com.udistrital.escondidas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udistrital.escondidas.GameViewModel
import com.udistrital.escondidas.domain.GameState
import com.udistrital.escondidas.domain.TemperatureState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.gameState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.stopGameSession()
                        onBackToMenu()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver al menú")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameHeader(state)

                GameIndicator(state)

                GameControls(state, onStart = { viewModel.startNewGame() })
            }

            if (state.isGameOver) {
                GameOverDialog(state, onRestart = { viewModel.startNewGame() })
            }
        }
    }
}

@Composable
fun GameHeader(state: GameState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Encuentra el objetivo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatusItem("Tiempo", "${state.remainingTimeSeconds}s")
            StatusItem("Puntaje", "${state.score}")
        }
    }
}

@Composable
fun StatusItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
fun GameIndicator(state: GameState) {
    val color = Color(android.graphics.Color.parseColor(state.temperatureState.colorHex))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(color.copy(alpha = 0.2f), CircleShape)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.temperatureState.labelEs,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Precisión: ${state.precisionPercentage}%",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun GameControls(state: GameState, onStart: () -> Unit) {
    Button(
        onClick = onStart,
        enabled = !state.isGameActive,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text = if (state.isGameOver) "Reiniciar Juego" else "Comenzar Búsqueda")
    }
}

@Composable
fun GameOverDialog(state: GameState, onRestart: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = if (state.isWon) "¡Objetivo Encontrado!" else "Tiempo Agotado",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = if (state.isWon) "¡Excelente trabajo!" else "Casi lo logras...")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Puntaje Final: ${state.score}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "Precisión: ${state.precisionPercentage}%")
            }
        },
        confirmButton = {
            TextButton(onClick = onRestart) {
                Text("Jugar de nuevo")
            }
        }
    )
}
