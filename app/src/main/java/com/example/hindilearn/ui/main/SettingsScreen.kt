package com.example.hindilearn.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.ui.gamified.GlassCard
import com.example.hindilearn.ui.gamified.PremiumBackground
import com.example.hindilearn.ui.gamified.PremiumButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumBackground {
        Scaffold(
            modifier = modifier,
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Settings", color = androidx.compose.ui.graphics.Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = androidx.compose.ui.graphics.Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Language Preferences",
                    style = MaterialTheme.typography.titleLarge,
                    color = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PremiumButton(
                            text = "English",
                            onClick = { 
                                UserManager.updateLanguage("EN")
                                onBack()
                            },
                            color = if (UserManager.progress.selectedLanguage == "EN") MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.White.copy(alpha = 0.2f)
                        )
                        PremiumButton(
                            text = "Vietnamese (Tiếng Việt)",
                            onClick = { 
                                UserManager.updateLanguage("VI")
                                onBack()
                            },
                            color = if (UserManager.progress.selectedLanguage == "VI") MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.White.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}
