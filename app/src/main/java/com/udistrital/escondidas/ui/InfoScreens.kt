package com.udistrital.escondidas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
            R.string.step_1,
            R.string.step_2,
            R.string.step_3,
            R.string.step_3_a,
            R.string.step_3_b,
            R.string.step_3_c,
            R.string.step_4
        )
        
        steps.forEach { stepRes ->
            Text(
                text = stringResource(id = stepRes),
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
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
