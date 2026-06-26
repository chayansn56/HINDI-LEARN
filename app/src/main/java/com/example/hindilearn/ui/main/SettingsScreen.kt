package com.example.hindilearn.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.ui.gamified.GlassCard
import com.example.hindilearn.ui.gamified.PremiumBackground
import com.example.hindilearn.ui.gamified.PremiumButton
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onCourseSwitched: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val contentColor = if (isDark) Color.White else Color(0xFF1E1E1E)

    PremiumBackground {
        Scaffold(
            modifier = modifier,
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        val isVi = UserManager.progress.selectedLanguage == "VI"
                        Text(if (isVi) "Cài đặt" else "Settings", color = contentColor)
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = contentColor)
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val isVi = UserManager.progress.selectedLanguage == "VI"
                var nameInput by remember { mutableStateOf(UserManager.progress.userName) }

                Spacer(modifier = Modifier.height(8.dp))

                // --- Name Input Section ---
                Text(
                    if (isVi) "Tên của bạn" else "Your Name",
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text(if (isVi) "Nhập tên" else "Enter name") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = contentColor.copy(alpha = 0.4f),
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = contentColor.copy(alpha = 0.6f),
                                focusedTextColor = contentColor,
                                unfocusedTextColor = contentColor
                            )
                        )
                        PremiumButton(
                            text = if (isVi) "Lưu" else "Save",
                            modifier = Modifier.width(100.dp),
                            onClick = {
                                UserManager.updateUserName(nameInput)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- Course Preferences Section ---
                Text(
                    if (isVi) "Khóa học" else "Course Preferences",
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PremiumButton(
                            text = if (isVi) "Học tiếng Hindi (Hindi)" else "Learn Hindi",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                UserManager.progress.selectedCourse = "HINDI"
                                UserManager.save()
                                onCourseSwitched()
                            },
                            color = if (UserManager.progress.selectedCourse == "HINDI") MaterialTheme.colorScheme.primary else Color(0xFFF1F1F1),
                            textColor = if (UserManager.progress.selectedCourse == "HINDI") Color.White else Color(0xFF1E1E1E)
                        )
                        PremiumButton(
                            text = if (isVi) "Học tiếng Anh (English)" else "Learn English",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                UserManager.progress.selectedCourse = "ENGLISH"
                                UserManager.save()
                                onCourseSwitched()
                            },
                            color = if (UserManager.progress.selectedCourse == "ENGLISH") MaterialTheme.colorScheme.primary else Color(0xFFF1F1F1),
                            textColor = if (UserManager.progress.selectedCourse == "ENGLISH") Color.White else Color(0xFF1E1E1E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- Language Toggle Section ---
                Text(
                    if (isVi) "Ngôn ngữ" else "Interface Language",
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            if (isVi) "Tiếng Việt (VI)" else "English (EN)",
                            style = MaterialTheme.typography.bodyLarge,
                            color = contentColor
                        )
                        PremiumButton(
                            text = if (isVi) "Chuyển sang EN" else "Switch to VI",
                            modifier = Modifier.width(160.dp),
                            onClick = {
                                val newLang = if (isVi) "EN" else "VI"
                                UserManager.updateLanguage(newLang)
                            }
                        )
                    }
                }
            }
        }
    }
}
