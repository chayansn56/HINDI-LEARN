package com.example.hindilearn.ui.gamified

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*

data class AchievementItem(
    val id: String,
    val titleEn: String,
    val titleVi: String,
    val descEn: String,
    val descVi: String,
    val icon: String,
    val color: Color,
    val rewardXp: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = UserManager.progress
    val isVi = progress.selectedLanguage == "VI"
    
    val achievements = listOf(
        AchievementItem(
            "ach_first_step",
            "First Step", "Bước đầu tiên",
            "Complete the very first lesson.", "Hoàn thành bài học đầu tiên.",
            "🌱", Color(0xFF4CAF50), 20
        ),
        AchievementItem(
            "ach_alphabet_master",
            "Alphabet Master", "Bậc thầy bảng chữ cái",
            "Clear the Alphabet Academy.", "Hoàn thành Học viện chữ cái.",
            "📖", Color(0xFF2196F3), 50
        ),
        AchievementItem(
            "ach_daily_warrior",
            "Daily Warrior", "Chiến binh hàng ngày",
            "Finish your first Daily Challenge.", "Hoàn thành Thử thách hàng ngày đầu tiên.",
            "🗡️", Color(0xFF9C27B0), 30
        ),
        AchievementItem(
            "ach_xp_hundred",
            "Century Club", "Câu lạc bộ thế kỷ",
            "Reach a total of 100 XP.", "Đạt tổng số 100 điểm XP.",
            "💯", Color(0xFFFF9800), 40
        ),
        AchievementItem(
            "ach_explorer",
            "Cultural Explorer", "Nhà thám hiểm văn hóa",
            "Learn about Indian festivals.", "Tìm hiểu về các lễ hội Ấn Độ.",
            "🇮🇳", Color(0xFFE91E63), 25
        )
    )

    PremiumBackground {
        Column(modifier = modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isVi) "Thành tích & Huy hiệu" else "Achievements & Badges",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // List of achievements
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(achievements) { ach ->
                    val isUnlocked = progress.unlockedAchievements.contains(ach.id)
                    
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (isUnlocked) 1f else 0.5f),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Badge Icon Sphere
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        if (isUnlocked) ach.color.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(20.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isUnlocked) ach.icon else "🔒",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Title & Description
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isVi) ach.titleVi else ach.titleEn,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isVi) ach.descVi else ach.descEn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                if (isUnlocked) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (isVi) "Đã hoàn thành! +${ach.rewardXp} XP" else "Completed! +${ach.rewardXp} XP",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RoyalBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
