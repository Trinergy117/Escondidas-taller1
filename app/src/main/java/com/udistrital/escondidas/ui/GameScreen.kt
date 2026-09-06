package com.udistrital.escondidas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
){
    val state by viewModel.gameState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Encuentra al personaje", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.stopGameSession()
                        onBackToMenu()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver al menú")
                    }
                },
                actions = {
                    // Tiempo y Puntaje en el TopBar
                    StatusItemTopBar("⏳ ${state.remainingTimeSeconds}s")
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusItemTopBar("🏆 ${state.score}")
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(onClick = { viewModel.resetGame() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reiniciar partida")
                    }
                }
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Muévete y gira tu teléfono para encontrar el objetivo",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                GameIndicator(state)

                GameControls(state, onStart = { viewModel.startNewGame() })
            }

            if (state.isGameOver) {
                GameOverDialog(
                    state = state, 
                    onRestart = { viewModel.startNewGame() },
                    onBackToMenu = onBackToMenu
                )
            }
        }
    }
}

@Composable
fun StatusItemTopBar(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun GameIndicator(state: GameState) {
    val color = Color(android.graphics.Color.parseColor(state.temperatureState.colorHex))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(220.dp)
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
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Precisión: ${state.precisionPercentage}%",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
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
            .height(64.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = if (state.isGameOver) "Jugar de nuevo" else "Comenzar Búsqueda",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GameOverDialog(
    state: GameState, 
    onRestart: () -> Unit,
    onBackToMenu: () -> Unit
) {
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
                Text(text = "Tiempo total: ${state.remainingTimeSeconds}")
            }
        },
        confirmButton = {
            Button(onClick = onRestart) {
                Text("Jugar de nuevo")
            }
        },
        dismissButton = {
            TextButton(onClick = onBackToMenu) {
                Text("Volver al menú")
            }
        }
    )
}
