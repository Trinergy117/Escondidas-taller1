package com.udistrital.escondidas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    onNavigateToGame: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHowToPlay: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ESCONDIDAS",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 4.sp
            )
            Text(
                text = "Bienvenido, encuentra al personaje antes de que acabe el tiempo!",
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(64.dp))

            MenuButton(
                text = "Nueva Partida",
                icon = Icons.Default.PlayArrow,
                onClick = onNavigateToGame
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MenuButton(
                text = "Ajustes",
                icon = Icons.Default.Settings,
                onClick = onNavigateToSettings
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MenuButton(
                text = "Cómo se juega",
                icon = Icons.Default.Info,
                onClick = onNavigateToHowToPlay
            )
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}
