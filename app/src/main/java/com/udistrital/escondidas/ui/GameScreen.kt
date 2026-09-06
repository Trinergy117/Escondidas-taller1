package com.udistrital.escondidas.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udistrital.escondidas.GameViewModel
import com.udistrital.escondidas.R
import com.udistrital.escondidas.domain.GameState
import com.udistrital.escondidas.domain.TemperatureState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    isVibrationEnabled: Boolean,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
){
    val state by viewModel.gameState.collectAsState()
    val context = LocalContext.current
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    // Lógica de vibración
    LaunchedEffect(state.temperatureState, isVibrationEnabled) {
        // Detener cualquier vibración previa (debido a bucles visto en el desarrollo) antes de iniciar una nueva
        vibrator.cancel()
        
        if (isVibrationEnabled) {
            when (state.temperatureState) {
                TemperatureState.HOT -> {
                    // Vibración constante suave (bucle)
                    val pattern = longArrayOf(0, 200, 200) 
                    val amplitudes = intArrayOf(0, 40, 0) 
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, 1))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(pattern, 1)
                    }
                }
                TemperatureState.FOUND -> {
                    // Vibración fuerte y única para la victoria
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(600, 255))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(600)
                    }
                }
                else -> {
                    // Para otros estados (FRIO, TIBIO), ya cancelamos al inicio del bloque
                }
            }
        }
    }

    // Detener vibración al salir por generacion de bucle visto antes
    DisposableEffect(Unit) {
        onDispose {
            vibrator.cancel()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.game_title), fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.stopGameSession()
                        onBackToMenu()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.game_back_to_menu))
                    }
                },
                actions = {
                    // Tiempo y Puntaje en el TopBar
                    StatusItemTopBar("⏳ ${state.remainingTimeSeconds}s")
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusItemTopBar("🏆 ${state.score}")
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(onClick = { viewModel.resetGame() }) {
                        Icon(Icons.Default.Refresh, contentDescription = stringResource(id = R.string.menu_new_game))
                    }
                }
            )
        }
    ){ innerPadding ->
        BlurredBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = stringResource(id = R.string.game_instruction),
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
                    text = state.temperatureState.getLabel(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.game_precision, state.precisionPercentage),
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
            text = if (state.isGameOver) stringResource(id = R.string.game_play_again) else stringResource(id = R.string.game_start_search),
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
                text = if (state.isWon) stringResource(id = R.string.game_won_title) else stringResource(id = R.string.game_lost_title),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = if (state.isWon) stringResource(id = R.string.game_won_message) else stringResource(id = R.string.game_lost_message))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.game_score_final, state.score), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = stringResource(id = R.string.game_precision, state.precisionPercentage))
                Text(text = stringResource(id = R.string.game_time, state.timeElapsedSeconds))
                Text(text = stringResource(id = R.string.game_rumbo, state.currentAzimuth.toInt()))
                Text(text = stringResource(id = R.string.game_error_margin, state.deltaAngle))
            }
        },
        confirmButton = {
            Button(onClick = onRestart) {
                Text(stringResource(id = R.string.game_play_again))
            }
        },
        dismissButton = {
            TextButton(onClick = onBackToMenu) {
                Text(stringResource(id = R.string.game_back_to_menu))
            }
        }
    )
}
