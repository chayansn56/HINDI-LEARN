package com.example.hindilearn.ui.gamified

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.GamifiedCurriculum
import com.example.hindilearn.data.UserManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryScreen() {
    val unlockedMemories = UserManager.progress.unlockedMemories
    val allMemories = GamifiedCurriculum.getMemories()
    val protagonist = if (UserManager.progress.selectedLanguage == "EN") "John" else "Lan"
    val state = UserManager.progress.protagonistState

    PremiumBackground {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
            title = { Text("Memories", color = Color.White, fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
        
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "The Story of $protagonist",
                style = MaterialTheme.typography.headlineMedium,
                color = com.example.hindilearn.theme.PremiumGold,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Current State: $state",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(allMemories) { memory ->
                    val isUnlocked = unlockedMemories.contains(memory.id)
                    MemoryCard(memory = memory, isUnlocked = isUnlocked)
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
    }
}

@Composable
fun MemoryCard(memory: com.example.hindilearn.data.MemoryItem, isUnlocked: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color(0xFF2C2C2C).copy(alpha = 0.9f) else Color(0xFF1E1E1E).copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isUnlocked) com.example.hindilearn.theme.PremiumGold else Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Icon(Icons.Default.Star, contentDescription = "Unlocked", tint = Color.Black)
                } else {
                    Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.LightGray)
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isUnlocked) memory.title else "???",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) Color.White else Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isUnlocked) memory.description else "Keep playing to unlock this memory.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUnlocked) Color.White.copy(alpha = 0.8f) else Color.DarkGray
                )
            }
        }
    }
}
