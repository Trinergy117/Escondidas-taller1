package com.udistrital.escondidas.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset

@Composable
fun BlurredBackground(content: @Composable () -> Unit) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo base
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Gradiente radial en la esquina superior derecha
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(primaryColor.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(size.width, 0f),
                    radius = size.width * 0.8f
                )
            )
            
            // Gradiente radial en la esquina inferior izquierda
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(primaryColor.copy(alpha = 0.1f), Color.Transparent),
                    center = Offset(0f, size.height),
                    radius = size.height * 0.7f
                )
            )
        }
        
        content()
    }
}
