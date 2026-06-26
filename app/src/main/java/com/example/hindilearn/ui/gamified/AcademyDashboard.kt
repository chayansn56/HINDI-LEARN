package com.example.hindilearn.ui.gamified

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.Episode
import com.example.hindilearn.data.GamifiedCurriculum
import com.example.hindilearn.data.UserManager
import androidx.compose.ui.graphics.Color
import com.example.hindilearn.theme.*

@Composable
fun AcademyDashboard(
    onNodeSelected: (Episode) -> Unit,
    onModuleSelected: (String) -> Unit,
    onSettingsSelected: () -> Unit,
    onAlphabetSelected: () -> Unit,
    onStoriesSelected: () -> Unit,
    onRevisionSelected: () -> Unit,
    onDailyChallengeSelected: () -> Unit,
    onAchievementsSelected: () -> Unit,
    onLeaderboardSelected: () -> Unit,
    onRoleplaySelected: () -> Unit,
    onPronunciationLabSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val progress = UserManager.progress
    val allEpisodesWithLock = GamifiedCurriculum.getEpisodesWithLockState(progress.unlockedNodeId)
    val lang = progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    val isHindiCourse = progress.selectedCourse == "HINDI"
    val isEnglishCourse = progress.selectedCourse == "ENGLISH"
    
    val userName = progress.userName.ifBlank { if (isVi) "Bạn" else "Friend" }
    val greetingPrefix = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
        in 5..11 -> if (isVi) "Chào buổi sáng," else "Good Morning,"
        in 12..17 -> if (isVi) "Chào buổi chiều," else "Good Afternoon,"
        in 18..21 -> if (isVi) "Chào buổi tối," else "Good Evening,"
        else -> if (isVi) "Chúc ngủ ngon," else "Good Night,"
    }
    
    val showToast = { msg: String ->
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    PremiumBackground {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            // Dashboard Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    // Row 1: Brand & Top Level Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            val courseLabel = if (progress.selectedCourse == "ENGLISH") {
                                if (isVi) "HỌC TIẾNG ANH" else "LEARN ENGLISH"
                            } else {
                                if (isVi) "HỌC TIẾNG HINDI" else "LEARN HINDI"
                            }
                            Text(
                                text = courseLabel,
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepSaffron,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = if (isVi) "ứng dụng bởi VIETANA" else "app by VIETANA",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Quick language switch toggle on top bar if active course is Hindi
                            if (isHindiCourse) {
                                TextButton(
                                    onClick = {
                                        val nextLang = if (isVi) "EN" else "VI"
                                        UserManager.updateLanguage(nextLang)
                                    },
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = if (isVi) "🇻🇳 VI" else "🇺🇸 EN",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepSaffron
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalFireDepartment, contentDescription = "Streak", tint = DeepSaffron, modifier = Modifier.size(18.dp))
                                Text("${progress.streak}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 2.dp, end = 6.dp))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = "XP", tint = PremiumGold, modifier = Modifier.size(18.dp))
                                Text("${progress.xp}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 2.dp, end = 6.dp))
                            }

                            IconButton(
                                onClick = onSettingsSelected,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 2: Greeting & Quick Alphabet Hub Shortcut
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = greetingPrefix,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = userName,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Prominent Alphabet Hub Round Button
                        Button(
                            onClick = onAlphabetSelected,
                            colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "अ",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepSaffron
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isVi) "Bảng chữ" else "Letters",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepSaffron
                            )
                        }
                    }
                }
            }

            // Continue Journey Banner
            item {
                val journeyEpisodes = allEpisodesWithLock.filter { it.id.startsWith("episode_") }
                val nextEpisode = journeyEpisodes.firstOrNull { !it.isLocked } ?: journeyEpisodes.lastOrNull()
                
                if (nextEpisode != null) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            val moduleLabel = if (nextEpisode.id.startsWith("episode_0_")) {
                                if (isVi) "GIAI ĐOẠN 0" else "PHASE 0"
                            } else {
                                if (isVi) "PHẦN 1" else "SEASON 1"
                            }
                            Text((if (isVi) "BÀI TIẾP THEO • " else "UP NEXT • ") + moduleLabel, style = MaterialTheme.typography.labelSmall, color = RoyalBlue, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(nextEpisode.title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Text(nextEpisode.synopsis, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.7f))
                            Spacer(modifier = Modifier.height(16.dp))
                            PremiumButton(
                                text = if (isVi) "Tiếp tục \u2192" else "Continue \u2192",
                                onClick = { onNodeSelected(nextEpisode) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            
            // Choose What To Learn Title
            item {
                Text(
                    text = if (isVi) "Chọn nội dung học" else "Choose What To Learn",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Foundations
            item {
                CategorySectionTitle(if (isVi) "Giai đoạn 0" else "Phase 0")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        title = if (isVi) "Nền tảng" else "Foundations",
                        subtitle = if (isVi) { if (isEnglishCourse) "Học tiếng Anh từ con số 0." else "Học tiếng Hindi từ con số 0." } else { if (isEnglishCourse) "Learn English from zero." else "Learn Hindi from zero." },
                        emoji = "🌱",
                        onClick = { onModuleSelected("phase_0") },
                        tintColor = Color(0xFFE2F4E3)
                    )
                    CategoryCard(
                        title = if (isVi) "Luyện phát âm" else "Pronunciation Lab",
                        subtitle = if (isVi) "Âm thanh, Giọng điệu." else "Sounds, Accents.",
                        emoji = "🧠",
                        onClick = onPronunciationLabSelected,
                        tintColor = Color(0xFFE2F4E3)
                    )
                    CategoryCard(
                        title = if (isVi) "Phòng số học" else "Numbers Lab",
                        subtitle = if (isVi) "Học số lên tới 1000." else "Learn numbers up to 1000.",
                        emoji = "🔢",
                        onClick = { onModuleSelected("numbers_lab") },
                        tintColor = Color(0xFFE2F4E3)
                    )
                    CategoryCard(
                        title = if (isVi) "Góc bảng chữ cái" else "Alphabet Hub",
                        subtitle = if (isVi) { if (isEnglishCourse) "Bảng chữ cái & Âm tiếng Anh." else "Hindi & Âm tương đồng." } else { if (isEnglishCourse) "English Alphabets & Sounds." else "Hindi Alphabets vs VI Sounds." },
                        emoji = "🔤",
                        onClick = { onModuleSelected("alphabets_lab") },
                        tintColor = Color(0xFFE2F4E3)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Core Skills
            item {
                CategorySectionTitle(if (isVi) "Kỹ năng chính" else "Core Skills")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        title = if (isVi) "Giao tiếp & Vai trò" else "AI Roleplay",
                        subtitle = if (isVi) "Thực hành hội thoại." else "Practice real conversations.",
                        emoji = "🤖",
                        onClick = onRoleplaySelected,
                        tintColor = Color(0xFFE2EDF8)
                    )
                    CategoryCard(
                        title = if (isVi) "Giao tiếp" else "Speaking",
                        subtitle = if (isVi) "Chào hỏi, Giới thiệu." else "Greetings, Introductions.",
                        emoji = "🗣️",
                        onClick = { onModuleSelected("speak") },
                        tintColor = Color(0xFFE2EDF8)
                    )
                    CategoryCard(
                        title = if (isVi) "Ngữ pháp" else "Grammar",
                        subtitle = if (isVi) "Thì, Câu hỏi." else "Tenses, Questions.",
                        emoji = "📚",
                        onClick = { onModuleSelected("gram") },
                        tintColor = Color(0xFFE2EDF8)
                    )
                    CategoryCard(
                        title = if (isVi) "Luyện nghe" else "Listening",
                        subtitle = if (isVi) "Câu chuyện, Tốc độ bản xứ." else "Stories, Native speed.",
                        emoji = "👂",
                        onClick = { onModuleSelected("listen") },
                        tintColor = Color(0xFFE2EDF8)
                    )
                    CategoryCard(
                        title = if (isVi) "Luyện viết" else "Writing",
                        subtitle = if (isVi) "Tập viết, Gõ phím." else "Tracing, Typing.",
                        emoji = "✍️",
                        onClick = { onModuleSelected("write") },
                        tintColor = Color(0xFFE2EDF8)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // The World
            item {
                CategorySectionTitle(if (isVi) "Thế giới" else "The World")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        title = if (isVi) "Văn hóa" else "Culture",
                        subtitle = if (isVi) "Lễ hội, Ẩm thực." else "Festivals, Food.",
                        emoji = "🇮🇳",
                        onClick = { onModuleSelected("culture") },
                        tintColor = Color(0xFFF9F0DF)
                    )
                    CategoryCard(
                        title = if (isVi) { if (isEnglishCourse) "Tiếng Anh Du Lịch" else "Hindi du lịch" } else { if (isEnglishCourse) "Travel English" else "Travel Hindi" },
                        subtitle = if (isVi) "Sân bay, Taxi." else "Airport, Taxi.",
                        emoji = "✈️",
                        onClick = { onModuleSelected("travel") },
                        tintColor = Color(0xFFF9F0DF)
                    )
                    CategoryCard(
                        title = if (isVi) { if (isEnglishCourse) "Tiếng Anh Thương Mại" else "Hindi thương mại" } else { if (isEnglishCourse) "Business English" else "Business Hindi" },
                        subtitle = if (isVi) "Cuộc họp, Email." else "Meetings, Emails.",
                        emoji = "💼",
                        onClick = { onModuleSelected("business") },
                        tintColor = Color(0xFFF9F0DF)
                    )
                    CategoryCard(
                        title = if (isVi) { if (isEnglishCourse) "Văn Hóa Pop Mỹ" else "Hindi Bollywood" } else { if (isEnglishCourse) "American Pop Culture" else "Bollywood Hindi" },
                        subtitle = if (isVi) "Bài hát, Biểu cảm." else "Songs, Expressions.",
                        emoji = "🎵",
                        onClick = { onModuleSelected("bollywood") },
                        tintColor = Color(0xFFF9F0DF)
                    )
                    CategoryCard(
                        title = if (isVi) { if (isEnglishCourse) "Tiếng Anh Mạng Xã Hội" else "Hindi WhatsApp" } else { if (isEnglishCourse) "Social Media English" else "WhatsApp Hindi" },
                        subtitle = if (isVi) "Trò chuyện hiện đại." else "Modern conversations.",
                        emoji = "💬",
                        onClick = { onModuleSelected("whatsapp") },
                        tintColor = Color(0xFFF9F0DF)
                    )
                    CategoryCard(
                        title = if (isVi) { if (isEnglishCourse) "Tiếng Anh Khẩn Cấp" else "Hindi khẩn cấp" } else { if (isEnglishCourse) "Emergency English" else "Emergency Hindi" },
                        subtitle = if (isVi) "Y tế & Khẩn cấp." else "Medical & Urgent help.",
                        emoji = "🚨",
                        onClick = { onModuleSelected("emerg") },
                        tintColor = Color(0xFFF9F0DF)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            // Utilities
            item {
                CategorySectionTitle(if (isVi) "Học viện của bạn" else "Your Academy")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        title = if (isVi) "Truyện" else "Stories",
                        subtitle = if (isVi) "Các câu chuyện ngắn." else "6 immersive stories about Indian life.",
                        emoji = "🎭",
                        onClick = onStoriesSelected,
                        tintColor = Color(0xFFEDE4F8)
                    )
                    CategoryCard(
                        title = if (isVi) "Trung tâm ôn tập" else "Revision Center",
                        subtitle = if (isVi) "Từ vựng còn yếu." else "Mistakes, Weak words.",
                        emoji = "⭐",
                        onClick = onRevisionSelected,
                        tintColor = Color(0xFFEDE4F8)
                    )
                    CategoryCard(
                        title = if (isVi) "Thử thách" else "Daily Challenge",
                        subtitle = if (isVi) "Kiểm tra kỹ năng." else "Test your skills.",
                        emoji = "🔥",
                        onClick = onDailyChallengeSelected,
                        tintColor = Color(0xFFEDE4F8)
                    )
                    CategoryCard(
                        title = if (isVi) "Thành tựu" else "Achievements",
                        subtitle = if (isVi) "Huy hiệu & Phần thưởng." else "Badges & Rewards.",
                        emoji = "🏆",
                        onClick = onAchievementsSelected,
                        tintColor = Color(0xFFEDE4F8)
                    )
                    CategoryCard(
                        title = if (isVi) "Bảng Xếp Hạng" else "Leaderboard",
                        subtitle = if (isVi) "Cạnh tranh toàn cầu." else "Compete globally.",
                        emoji = "🌍",
                        onClick = onLeaderboardSelected,
                        tintColor = Color(0xFFEDE4F8)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun CategorySectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium, color = DeepSaffron, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
}

