package com.udistrital.escondidas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    InfoScreenTemplate(title = "Ajustes", onBack = onBack) {
        Text("Aquí irán los ajustes del juego (sensibilidad, tiempo, etc).")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Por ahora, el juego usa la configuración por defecto.")
    }
}

@Composable
fun HowToPlayScreen(onBack: () -> Unit) {
    InfoScreenTemplate(title = "¿Cómo se juega?", onBack = onBack) {
        Text("1. Presiona 'Nueva Partida'.", fontWeight = FontWeight.Bold)
        Text("2. Mueve tu dispositivo para encontrar el ángulo correcto.")
        Text("3. El color cambiará según qué tan cerca estés:")
        Text("   - Azul: Muy Frío", color = MaterialTheme.colorScheme.primary)
        Text("   - Amarillo: Tibio")
        Text("   - Rojo: Caliente", color = MaterialTheme.colorScheme.error)
        Text("4. Encuentra el punto exacto antes de que el tiempo se agote.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreenTemplate(
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        BlurredBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                content = content
            )
        }
    }
}
