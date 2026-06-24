package com.example.hindilearn.ui.gamified

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hindilearn.data.AudioHelper
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*
import org.json.JSONArray
import java.io.InputStream
import java.util.Locale

data class RevisionWord(
    val hindi: String,
    val transliteration: String,
    val english: String,
    val vietnamese: String,
    val category: String,
    val usageNote: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val progress = UserManager.progress
    val lang = progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"

    var isReviewing by remember { mutableStateOf(false) }
    var reviewQueue = remember { mutableStateListOf<RevisionWord>() }
    var currentReviewWordIdx by remember { mutableIntStateOf(0) }
    var isCardFlipped by remember { mutableStateOf(false) }
    var reviewFinished by remember { mutableStateOf(false) }
    
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var weakWordsList = remember { mutableStateListOf<RevisionWord>() }

    val nextReviewWord = {
        isCardFlipped = false
        if (currentReviewWordIdx + 1 < reviewQueue.size) {
            currentReviewWordIdx++
        } else {
            reviewFinished = true
        }
    }

    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.forLanguageTag("hi-IN")
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    // Load words from assets/episodes/master_vocab.json
    LaunchedEffect(Unit) {
        try {
            val inputStream: InputStream = context.assets.open("episodes/master_vocab.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            val jsonArray = JSONArray(jsonString)
            
            val allWords = mutableListOf<RevisionWord>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                allWords.add(
                    RevisionWord(
                        hindi = obj.getString("hindi"),
                        transliteration = obj.getString("transliteration"),
                        english = obj.getString("english"),
                        vietnamese = obj.getString("vietnamese"),
                        category = obj.getString("category"),
                        usageNote = obj.optString("usage_note")
                    )
                )
            }
            
            val mistakeMap = progress.mistakeMap
            // Sort by mistake count descending
            val sortedWords = allWords.sortedByDescending { word ->
                mistakeMap[word.hindi] ?: 0
            }
            
            // Sample top 5 weak words
            weakWordsList.clear()
            weakWordsList.addAll(sortedWords.take(5))
            
            // Populate review queue with top 6 words
            reviewQueue.clear()
            reviewQueue.addAll(sortedWords.take(6))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    PremiumBackground {
        Crossfade(targetState = isReviewing, label = "ReviewModeTransition") { reviewing ->
            if (!reviewing) {
                // Dashboard View
                Scaffold(
                    modifier = modifier,
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            title = { Text(if (isVi) "Trung tâm ôn tập" else "Revision Center", color = TextDark, fontWeight = FontWeight.Bold) },
                            navigationIcon = {
                                IconButton(onClick = onBack) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextDark)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                        )
                    }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Quick Stats
                        item {
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = if (isVi) "Tiến độ học tập của bạn" else "Your Learning Progress",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepSaffron
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        StatBlock(
                                            value = "${progress.xp} XP",
                                            label = if (isVi) "Tổng XP" else "Total XP",
                                            icon = "🔥"
                                        )
                                        StatBlock(
                                            value = "${progress.coins}",
                                            label = if (isVi) "Đồng xu" else "Coins",
                                            icon = "🪙"
                                        )
                                        StatBlock(
                                            value = "5/5",
                                            label = if (isVi) "Trái tim" else "Hearts",
                                            icon = "❤️"
                                        )
                                    }
                                }
                            }
                        }

                        // Weak Words section
                        item {
                            Text(
                                text = if (isVi) "Từ vựng cần củng cố" else "Words Needing Practice",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    weakWordsList.forEach { word ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(DeepSaffron.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(word.hindi.take(1), fontWeight = FontWeight.Bold, color = DeepSaffron)
                                            }
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(word.hindi, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextDark)
                                                Text(
                                                    text = "/${word.transliteration}/ \u2022 ${if (isVi) word.vietnamese else word.english}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = TextDark.copy(alpha = 0.6f)
                                                )
                                            }
                                            IconButton(onClick = {
                                                AudioHelper.playAudio(context, "", tts, word.hindi)
                                            }) {
                                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = RoyalBlue)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Start Review button
                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            PremiumButton(
                                text = if (isVi) "Bắt đầu ôn tập nhanh (+15 XP)" else "Start Quick Review (+15 XP)",
                                onClick = {
                                    if (reviewQueue.isNotEmpty()) {
                                        currentReviewWordIdx = 0
                                        isCardFlipped = false
                                        reviewFinished = false
                                        isReviewing = true
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                // Flashcards Review Mode
                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            title = { 
                                Text(
                                    text = if (reviewFinished) "Review Complete" else "Flashcard ${currentReviewWordIdx + 1}/${reviewQueue.size}", 
                                    color = TextDark, 
                                    fontWeight = FontWeight.Bold
                                ) 
                            },
                            navigationIcon = {
                                IconButton(onClick = { isReviewing = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextDark)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!reviewFinished) {
                            val activeWord = reviewQueue[currentReviewWordIdx]
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Info banner
                                Text(
                                    text = if (isVi) "Chạm vào thẻ để lật xem nghĩa!" else "Tap the card to reveal the translation!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextDark.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )

                                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    Card(
                                        modifier = Modifier
                                            .size(300.dp)
                                            .clickable {
                                                isCardFlipped = !isCardFlipped
                                                if (isCardFlipped) {
                                                    AudioHelper.playAudio(context, "", tts, activeWord.hindi)
                                                }
                                            },
                                        shape = RoundedCornerShape(32.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                if (!isCardFlipped) {
                                                    Text(activeWord.hindi, fontSize = 56.sp, fontWeight = FontWeight.Black, color = TextDark, textAlign = TextAlign.Center)
                                                    Spacer(modifier = Modifier.height(12.dp))
                                                    Text("/${activeWord.transliteration}/", style = MaterialTheme.typography.titleMedium, color = DeepSaffron)
                                                } else {
                                                    Text(
                                                        text = if (isVi) activeWord.vietnamese else activeWord.english,
                                                        fontSize = 32.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = TextDark,
                                                        textAlign = TextAlign.Center
                                                    )
                                                    if (activeWord.usageNote.isNotEmpty()) {
                                                        Spacer(modifier = Modifier.height(12.dp))
                                                        Text(
                                                            text = activeWord.usageNote,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = TextDark.copy(alpha = 0.6f),
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // Spaced Repetition Buttons
                                if (isCardFlipped) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Button(
                                            onClick = { nextReviewWord() },
                                            colors = ButtonDefaults.buttonColors(containerColor = SoftRed),
                                            modifier = Modifier.weight(1f).height(56.dp),
                                            shape = RoundedCornerShape(16.dp)
                                        ) {
                                            Text("Hard", fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                        Button(
                                            onClick = { nextReviewWord() },
                                            colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron),
                                            modifier = Modifier.weight(1f).height(56.dp),
                                            shape = RoundedCornerShape(16.dp)
                                        ) {
                                            Text("Good", fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                        Button(
                                            onClick = { nextReviewWord() },
                                            colors = ButtonDefaults.buttonColors(containerColor = SoftGreen),
                                            modifier = Modifier.weight(1f).height(56.dp),
                                            shape = RoundedCornerShape(16.dp)
                                        ) {
                                            Text("Easy", fontWeight = FontWeight.Bold, color = TextDark)
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.height(72.dp))
                                }
                            }
                        } else {
                            // Celebrate Completion
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = "XP",
                                    tint = PremiumGold,
                                    modifier = Modifier.size(100.dp)
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = if (isVi) "Ôn tập hoàn tất!" else "Review Complete!",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextDark
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (isVi) "Bạn đã hoàn thành củng cố từ vựng hôm nay." 
                                           else "You have completed your daily vocabulary revision.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextDark.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                                GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = "Star", tint = PremiumGold)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("+15 XP", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PremiumGold)
                                    }
                                }
                                Spacer(modifier = Modifier.height(48.dp))
                                PremiumButton(
                                    text = if (isVi) "Quay lại" else "Finish",
                                    onClick = {
                                        UserManager.addXp(15)
                                        isReviewing = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatBlock(
    value: String,
    label: String,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = TextDark)
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextDark.copy(alpha = 0.5f))
    }
}
