package com.udistrital.escondidas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udistrital.escondidas.R
import com.udistrital.escondidas.domain.Difficulty

@Composable
fun SettingsScreen(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    isVibrationEnabled: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    InfoScreenTemplate(title = stringResource(id = R.string.settings_title), onBack = onBack) {
        Text(
            text = stringResource(id = R.string.settings_language),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LanguageOption("Español", "es", currentLanguage == "es") { onLanguageChange("es") }
            LanguageOption("English", "en", currentLanguage == "en") { onLanguageChange("en") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.settings_vibration),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Switch(
                checked = isVibrationEnabled,
                onCheckedChange = onVibrationChange
            )
        }
    }
}

@Composable
fun DifficultyOption(label: String, difficulty: Difficulty, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) }
    )
}

@Composable
fun LanguageOption(label: String, code: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) }
    )
}

@Composable
fun HowToPlayScreen(onBack: () -> Unit) {
    InfoScreenTemplate(title = stringResource(id = R.string.how_to_play_title), onBack = onBack) {
        val steps = listOf(
            StepData(R.string.step_1, R.raw.step_1),
            StepData(R.string.step_2, R.raw.step_2),
            StepData(R.string.step_2_a, R.raw.step_2_1),
            StepData(R.string.step_2_b, R.raw.step_2_2),
            StepData(R.string.step_2_c, R.raw.step_2_3),
            StepData(R.string.step_2_d, R.raw.step_2_4),
            StepData(R.string.step_3, R.raw.step_3),
            StepData(R.string.step_4, R.raw.step_4)
        )

        steps.forEach { step ->
            Text(
                text = stringResource(id = step.textRes),
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            step.imageRes?.let { imageRes ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

private data class StepData(val textRes: Int, val imageRes: Int?)

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
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
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
