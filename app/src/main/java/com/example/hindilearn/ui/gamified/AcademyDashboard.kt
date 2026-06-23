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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val progress = UserManager.progress
    val allEpisodesWithLock = GamifiedCurriculum.getEpisodesWithLockState(progress.unlockedNodeId)
    val lang = progress.selectedLanguage ?: "EN"
    val userName = if (lang == "EN") "John \uD83C\uDDFA\uD83C\uDDF8" else "Lan \uD83C\uDDFB\uD83C\uDDF3"
    val greeting = if (lang == "EN") "Good Afternoon, $userName" else "Xin chào, $userName"
    
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
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val courseLabel = if (UserManager.progress.selectedCourse == "ENGLISH") "LEARN ENGLISH" else "LEARN HINDI"
                        Text(courseLabel, style = MaterialTheme.typography.headlineSmall, color = DeepSaffron, fontWeight = FontWeight.Black)
                        Text("app by", style = MaterialTheme.typography.labelSmall, color = TextDark.copy(alpha=0.5f))
                        Text("VIETANA GROUP", style = MaterialTheme.typography.titleLarge, color = TextDark, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(greeting, style = MaterialTheme.typography.bodyMedium, color = TextDark.copy(alpha=0.7f))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = "Streak", tint = DeepSaffron, modifier = Modifier.size(20.dp))
                        Text("${progress.streak}", style = MaterialTheme.typography.titleMedium, color = TextDark, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp, end = 16.dp))
                        Icon(Icons.Default.Star, contentDescription = "XP", tint = PremiumGold, modifier = Modifier.size(20.dp))
                        Text("${progress.xp}", style = MaterialTheme.typography.titleMedium, color = TextDark, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp, end = 16.dp))
                        IconButton(onClick = onAlphabetSelected) {
                            Text(
                                text = "अ",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = DeepSaffron
                            )
                        }
                        IconButton(onClick = onSettingsSelected) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextDark)
                        }
                    }
                }
            }

            // Continue Journey Banner
            item {
                // Find first unlocked episode (including phase 0)
                val journeyEpisodes = allEpisodesWithLock.filter { it.id.startsWith("episode_") }
                val nextEpisode = journeyEpisodes.firstOrNull { !it.isLocked } ?: journeyEpisodes.lastOrNull()
                
                if (nextEpisode != null) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            val moduleLabel = if (nextEpisode.id.startsWith("episode_0_")) "PHASE 0" else "SEASON 1"
                            Text("UP NEXT \u2022 $moduleLabel", style = MaterialTheme.typography.labelSmall, color = RoyalBlue, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(nextEpisode.title, style = MaterialTheme.typography.headlineSmall, color = TextDark, fontWeight = FontWeight.Bold)
                            Text(nextEpisode.synopsis, style = MaterialTheme.typography.bodyMedium, color = TextDark.copy(alpha=0.7f))
                            Spacer(modifier = Modifier.height(16.dp))
                            PremiumButton(text = "Continue \u2192", onClick = { onNodeSelected(nextEpisode) })
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            
            // Choose What To Learn Title
            item {
                Text("Choose What To Learn", style = MaterialTheme.typography.titleLarge, color = TextDark, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Foundations
            item {
                CategorySectionTitle("Phase 0")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard("Foundations", "Learn Hindi from zero.", "\uD83C\uDF31", onClick = { onModuleSelected("phase_0") })
                    CategoryCard("Pronunciation Lab", "Sounds, Accents.", "\uD83E\uDDE0", onClick = { onModuleSelected("pron") })
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Core Skills
            item {
                CategorySectionTitle("Core Skills")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard("Speaking", "Greetings, Introductions.", "\uD83D\uDDE3\uFE0F", onClick = { onModuleSelected("speak") })
                    CategoryCard("Grammar", "Tenses, Questions.", "\uD83D\uDCDA", onClick = { onModuleSelected("gram") })
                    CategoryCard("Listening", "Stories, Native speed.", "\uD83D\uDC42", onClick = { onModuleSelected("listen") })
                    CategoryCard("Writing", "Tracing, Typing.", "\u270D\uFE0F", onClick = { onModuleSelected("write") })
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // The World
            item {
                CategorySectionTitle("The World")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard("Culture", "Festivals, Food.", "\uD83C\uDDEE\uD83C\uDDF3", onClick = { onModuleSelected("culture") })
                    CategoryCard("Travel Hindi", "Airport, Taxi.", "\u2708\uFE0F", onClick = { onModuleSelected("travel") })
                    CategoryCard("Business Hindi", "Meetings, Emails.", "\uD83D\uDCBC", onClick = { onModuleSelected("business") })
                    CategoryCard("Bollywood Hindi", "Songs, Expressions.", "\uD83C\uDFB5", onClick = { onModuleSelected("bollywood") })
                    CategoryCard("WhatsApp Hindi", "Modern conversations.", "\uD83D\uDCAC", onClick = { onModuleSelected("whatsapp") })
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            // Utilities
            item {
                CategorySectionTitle("Your Academy")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard("Stories", "ELDER SISTER Di, mom, the greatman JIJU.", "\uD83C\uDFAD", onClick = onStoriesSelected)
                    CategoryCard("Revision Center", "Mistakes, Weak words.", "\u2B50", onClick = onRevisionSelected)
                    CategoryCard("Daily Challenge", "Test your skills.", "\uD83D\uDD25", onClick = onDailyChallengeSelected)
                    CategoryCard("Achievements", "Badges & Rewards.", "🏆", onClick = onAchievementsSelected)
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
