package com.example.hindilearn.ui.gamified

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*

data class LeaderboardUser(val name: String, val xp: Int, val isMe: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val progress = UserManager.progress
    val isVi = progress.selectedLanguage == "VI"

    val userName = progress.userName.takeIf { it.isNotBlank() } ?: if (isVi) "Bạn" else "You"
    
    // Dynamically scale competitors based on user XP
    val baseXP = maxOf(progress.xp, 100)
    val competitors = remember(progress.xp) {
        listOf(
            LeaderboardUser("Priya", (baseXP * 1.5).toInt()),
            LeaderboardUser("Rahul", (baseXP * 1.2).toInt()),
            LeaderboardUser("Emma", (baseXP * 1.05).toInt()),
            LeaderboardUser("Arjun", (baseXP * 0.95).toInt()),
            LeaderboardUser("Sophia", (baseXP * 0.8).toInt()),
            LeaderboardUser("Vikram", (baseXP * 0.6).toInt()),
            LeaderboardUser("Aisha", (baseXP * 0.4).toInt()),
            LeaderboardUser("Leo", (baseXP * 0.2).toInt())
        )
    }

    // Insert current user and sort
    val allUsers = (competitors + LeaderboardUser(userName, progress.xp, true))
        .sortedByDescending { it.xp }

    PremiumBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (isVi) "Bảng Xếp Hạng" else "Leaderboard", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // League Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(DeepSaffron)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = "Trophy", tint = Color.White, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isVi) "Giải Vàng" else "Gold League",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = if (isVi) "Top 3 sẽ thăng hạng!" else "Top 3 advance to the next league!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // List of Users
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(allUsers) { index, user ->
                        LeaderboardRow(rank = index + 1, user = user)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(rank: Int, user: LeaderboardUser) {
    val backgroundColor = if (user.isMe) DeepSaffron.copy(alpha = 0.15f) else Color.White
    val borderColor = if (user.isMe) DeepSaffron else Color.Transparent

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (user.isMe) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Text(
                text = "$rank",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = when (rank) {
                    1 -> PremiumGold
                    2 -> Color(0xFFC0C0C0) // Silver
                    3 -> Color(0xFFCD7F32) // Bronze
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                },
                modifier = Modifier.width(32.dp)
            )

            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SoftGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(user.name.take(1), fontWeight = FontWeight.Bold, color = RoyalBlue)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Name
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (user.isMe) FontWeight.Bold else FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // XP
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${user.xp}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Star, contentDescription = "XP", tint = PremiumGold, modifier = Modifier.size(16.dp))
            }
        }
    }
}
