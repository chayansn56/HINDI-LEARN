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
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.hindilearn.data.OpenAiService
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

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
    onMascotTalkSelected: () -> Unit,
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

    var isCelebrating by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    PremiumBackground {
        Box(modifier = Modifier.fillMaxSize()) {
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
                                text = if (isEnglishCourse) "A" else "अ",
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

                    var hasEntered by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        hasEntered = true
                    }

                    val entranceOffsetX by androidx.compose.animation.core.animateDpAsState(
                        targetValue = if (hasEntered) 0.dp else (-350).dp,
                        animationSpec = androidx.compose.animation.core.spring(
                            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                        ),
                        label = "EntranceOffset"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var mascotBubbleText by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(if (isVi) "Chào mừng bạn đến với Vietana! Mình là chiếc thuyền xanh buồm vàng, người bạn đồng hành khám phá ngôn ngữ của bạn! ⛵" else "Welcome to Vietana! I'm the blue boat with gold sails, your companion in language discovery! ⛵") }
                    val mascotCoroutines = androidx.compose.runtime.rememberCoroutineScope()

                    // Pulse/Wobble Animation States (Wave Sailing)
                    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "MascotWobble")
                    val mascotTranslationY by infiniteTransition.animateFloat(
                        initialValue = -5f,
                        targetValue = 5f,
                        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                            animation = androidx.compose.animation.core.tween(1300, easing = androidx.compose.animation.core.LinearEasing),
                            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                        ),
                        label = "TranslationY"
                    )
                    val mascotTranslationX by infiniteTransition.animateFloat(
                        initialValue = -4f,
                        targetValue = 4f,
                        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                            animation = androidx.compose.animation.core.tween(1700, easing = androidx.compose.animation.core.LinearEasing),
                            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                        ),
                        label = "TranslationX"
                    )
                    val mascotRotationZ by infiniteTransition.animateFloat(
                        initialValue = -3f,
                        targetValue = 3f,
                        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                            animation = androidx.compose.animation.core.tween(1500, easing = androidx.compose.animation.core.LinearEasing),
                            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                        ),
                        label = "RotationZ"
                    )

                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = entranceOffsetX),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onMascotTalkSelected()
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Animated Vector Vietana Mascot (Smart Girl with Glasses & Conical Hat in a Blue Boat)
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .graphicsLayer {
                                        translationX = mascotTranslationX
                                        translationY = mascotTranslationY
                                        rotationZ = mascotRotationZ
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                VietanaMascot(modifier = Modifier.size(56.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isVi) "VIETANA MASCOT" else "VIETANA MASCOT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DeepSaffron,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = mascotBubbleText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
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
                CategorySectionTitle(if (isVi) "Học viện chính" else "Core Academy")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        title = if (isVi) "Giai đoạn 0: Nền tảng" else "Phase 0: Foundations",
                        subtitle = if (isVi) { if (isEnglishCourse) "Học tiếng Anh từ con số 0." else "Học tiếng Hindi từ con số 0." } else { if (isEnglishCourse) "Learn English from zero." else "Learn Hindi from zero." },
                        emoji = "🌱",
                        onClick = { onModuleSelected("phase_0") },
                        tintColor = Color(0xFFE2F4E3)
                    )
                    CategoryCard(
                        title = if (isVi) "Giai đoạn 1: Tiểu học" else "Phase 1: Elementary",
                        subtitle = if (isVi) "Từ vựng đời sống & Điều hướng." else "Life vocab & Navigation.",
                        emoji = "🌿",
                        onClick = { onModuleSelected("phase_1") },
                        tintColor = Color(0xFFD5F0D7)
                    )
                    CategoryCard(
                        title = if (isVi) "Giai đoạn 2: Trôi chảy" else "Phase 2: Fluency",
                        subtitle = if (isVi) "Ngữ pháp trung cấp." else "Intermediate grammar.",
                        emoji = "🌳",
                        onClick = { onModuleSelected("phase_2") },
                        tintColor = Color(0xFFC7ECCB)
                    )
                    CategoryCard(
                        title = if (isVi) "Giai đoạn 3: Thành thạo" else "Phase 3: Mastery",
                        subtitle = if (isVi) "Giao tiếp nâng cao." else "Advanced conversation.",
                        emoji = "👑",
                        onClick = { onModuleSelected("phase_3") },
                        tintColor = Color(0xFFB9E8BF)
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
                        emoji = if (isEnglishCourse) "🇺🇸" else "🇮🇳",
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
                        subtitle = if (isVi) "Các câu chuyện ngắn." else if (isEnglishCourse) "6 immersive stories about American life." else "6 immersive stories about Indian life.",
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
            
            if (isCelebrating) {
                CelebrationFlightOverlay(onAnimationFinished = { isCelebrating = false })
            }
        }
    }
}

@Composable
fun CategorySectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium, color = DeepSaffron, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
}

@Composable
fun VietanaMascot(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Boat Hull (Crescent Royal Blue boat shape at the bottom)
        val boatPath = Path().apply {
            moveTo(w * 0.1f, h * 0.65f)
            quadraticBezierTo(w * 0.5f, h * 0.95f, w * 0.9f, h * 0.65f)
            quadraticBezierTo(w * 0.5f, h * 0.78f, w * 0.1f, h * 0.65f)
            close()
        }

        // 2. Mast (center vertical wooden line)
        drawLine(
            color = Color(0xFF5D4037),
            start = Offset(w * 0.5f, h * 0.2f),
            end = Offset(w * 0.5f, h * 0.72f),
            strokeWidth = 3f
        )

        // 3. Sails (Vietana Logo crescent shape sails)
        // Left sail
        val leftSailPath = Path().apply {
            moveTo(w * 0.48f, h * 0.22f)
            quadraticBezierTo(w * 0.15f, h * 0.45f, w * 0.48f, h * 0.62f)
            quadraticBezierTo(w * 0.35f, h * 0.45f, w * 0.48f, h * 0.22f)
            close()
        }
        drawPath(leftSailPath, PremiumGold)

        // Right sail
        val rightSailPath = Path().apply {
            moveTo(w * 0.52f, h * 0.25f)
            quadraticBezierTo(w * 0.8f, h * 0.48f, w * 0.52f, h * 0.62f)
            quadraticBezierTo(w * 0.65f, h * 0.48f, w * 0.52f, h * 0.25f)
            close()
        }
        drawPath(rightSailPath, DeepSaffron)

        // 4. Mascot face details drawn directly on the sails to make the boat speak!
        // Left eye
        drawCircle(
            color = Color.Black,
            radius = 3.5f,
            center = Offset(w * 0.38f, h * 0.42f)
        )
        // Right eye
        drawCircle(
            color = Color.Black,
            radius = 3.5f,
            center = Offset(w * 0.62f, h * 0.42f)
        )
        // Highlights
        drawCircle(
            color = Color.White,
            radius = 1f,
            center = Offset(w * 0.37f, h * 0.41f)
        )
        drawCircle(
            color = Color.White,
            radius = 1f,
            center = Offset(w * 0.61f, h * 0.41f)
        )

        // Cute smile centered on sails
        drawArc(
            color = Color.Black,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.46f, h * 0.46f),
            size = androidx.compose.ui.geometry.Size(w * 0.08f, h * 0.06f),
            style = Stroke(width = 2.5f)
        )

        // 5. Render Boat Hull (covers bottom mast edge nicely)
        drawPath(boatPath, RoyalBlue)

        // Gold trim line on the hull
        val boatTrimPath = Path().apply {
            moveTo(w * 0.18f, h * 0.70f)
            quadraticBezierTo(w * 0.5f, h * 0.86f, w * 0.82f, h * 0.70f)
        }
        drawPath(
            path = boatTrimPath,
            color = PremiumGold,
            style = Stroke(width = 2.5f)
        )
    }
}

@Composable
fun CelebrationFlightOverlay(
    onAnimationFinished: () -> Unit
) {
    val animationProgress = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 2200,
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            )
        )
        onAnimationFinished()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val w = maxWidth
        val h = maxHeight
        val progress = animationProgress.value

        // Trajectory curves: starts left, flies up in center, goes right
        val xOffset = w * progress - 80.dp
        val yOffset = h * 0.4f - (h * 0.25f * kotlin.math.sin(progress * kotlin.math.PI).toFloat())

        // Rotation follows trajectory curve
        val tangentAngle = -30f * kotlin.math.cos(progress * kotlin.math.PI).toFloat()

        Box(
            modifier = Modifier
                .absoluteOffset(x = xOffset, y = yOffset)
                .graphicsLayer { rotationZ = tangentAngle }
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background sparkle trail
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val rand = java.util.Random(12)
                    for (i in 0..6) {
                        val px = size.width * (0.2f + 0.6f * rand.nextFloat())
                        val py = size.height * (0.3f + 0.5f * rand.nextFloat())
                        val scale = (0.2f + 0.8f * rand.nextFloat()) * (1f - progress)
                        drawCircle(
                            color = PremiumGold.copy(alpha = scale),
                            radius = 8f * scale,
                            center = Offset(px, py)
                        )
                    }
                }
                VietanaMascot(modifier = Modifier.size(72.dp))
            }
        }
    }
}

