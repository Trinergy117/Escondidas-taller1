package com.udistrital.escondidas.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udistrital.escondidas.R

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
        BlurredBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.menu_title),
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 4.sp
                    )
                    Text(
                        text = stringResource(id = R.string.menu_welcome),
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(64.dp))

                    MenuButton(
                        text = stringResource(id = R.string.menu_new_game),
                        icon = Icons.Default.PlayArrow,
                        onClick = onNavigateToGame
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MenuButton(
                        text = stringResource(id = R.string.menu_settings),
                        icon = Icons.Default.Settings,
                        onClick = onNavigateToSettings
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MenuButton(
                        text = stringResource(id = R.string.menu_how_to_play),
                        icon = Icons.Default.Info,
                        onClick = onNavigateToHowToPlay
                    )
                }

                // Pie de página con nombres
                Text(
                    text = stringResource(id = R.string.developed_by),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
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
