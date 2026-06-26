package com.example.hindilearn.ui.gamified

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import java.util.Locale

data class AlphabetLetter(
    val character: String,
    val transliteration: String,
    val descriptionEn: String,
    val descriptionVi: String,
    val type: String // "Vowel" or "Consonant"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphabetScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lang = UserManager.progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    
    DisposableEffect(context) {
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.forLanguageTag("hi-IN")
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    val vowels = listOf(
        AlphabetLetter("अ", "a", "Short 'a' (kalam)", "Nguyên âm ngắn 'a'", "Vowel"),
        AlphabetLetter("आ", "aa", "Long 'aa' (aam)", "Nguyên âm dài 'aa'", "Vowel"),
        AlphabetLetter("इ", "i", "Short 'i' (kitaab)", "Nguyên âm ngắn 'i'", "Vowel"),
        AlphabetLetter("ई", "ee", "Long 'ee' (paani)", "Nguyên âm dài 'ee'", "Vowel"),
        AlphabetLetter("उ", "u", "Short 'u' (kutta)", "Nguyên âm ngắn 'u'", "Vowel"),
        AlphabetLetter("ऊ", "oo", "Long 'oo' (oon)", "Nguyên âm dài 'oo'", "Vowel"),
        AlphabetLetter("ऋ", "ri", "Short 'ri' (kripaya)", "Nguyên âm ngắn 'ri'", "Vowel"),
        AlphabetLetter("ए", "e", "Short 'e' (kela)", "Nguyên âm ngắn 'e'", "Vowel"),
        AlphabetLetter("ऐ", "ai", "Diphthong 'ai' (kaisa)", "Nguyên âm đôi 'ai'", "Vowel"),
        AlphabetLetter("ओ", "o", "Long 'o' (koyal)", "Nguyên âm dài 'o'", "Vowel"),
        AlphabetLetter("औ", "au", "Diphthong 'au' (kauwa)", "Nguyên âm đôi 'au'", "Vowel"),
        AlphabetLetter("अं", "an", "Nasal 'an' (angoor)", "Âm mũi 'an'", "Vowel"),
        AlphabetLetter("अः", "ah", "Breathy 'ah' (dukh)", "Âm bật hơi 'ah'", "Vowel")
    )

    val consonants = listOf(
        // Velar
        AlphabetLetter("क", "ka", "Velar (lotus)", "Âm họng 'ka'", "Consonant"),
        AlphabetLetter("ख", "kha", "Aspirated (rabbit)", "Âm họng bật hơi", "Consonant"),
        AlphabetLetter("ग", "ga", "Velar (cow)", "Âm họng 'ga'", "Consonant"),
        AlphabetLetter("घ", "gha", "Aspirated (house)", "Âm họng bật hơi", "Consonant"),
        // Palatal
        AlphabetLetter("च", "cha", "Palatal (spoon)", "Âm vòm miệng", "Consonant"),
        AlphabetLetter("छ", "chha", "Aspirated (umbrella)", "Âm vòm bật hơi", "Consonant"),
        AlphabetLetter("ज", "ja", "Palatal (water)", "Âm vòm miệng", "Consonant"),
        AlphabetLetter("झ", "jha", "Aspirated (flag)", "Âm vòm bật hơi", "Consonant"),
        // Retroflex
        AlphabetLetter("ट", "ṭa", "Retroflex (tomato)", "Âm quặt lưỡi", "Consonant"),
        AlphabetLetter("ठ", "ṭha", "Aspirated Retro", "Âm quặt bật hơi", "Consonant"),
        AlphabetLetter("ड", "ḍa", "Retroflex (drum)", "Âm quặt lưỡi", "Consonant"),
        AlphabetLetter("ढ", "ḍha", "Aspirated Retro", "Âm quặt bật hơi", "Consonant"),
        // Dental
        AlphabetLetter("त", "ta", "Dental (star)", "Âm răng", "Consonant"),
        AlphabetLetter("थ", "tha", "Aspirated Dental", "Âm răng bật hơi", "Consonant"),
        AlphabetLetter("द", "da", "Dental (door)", "Âm răng", "Consonant"),
        AlphabetLetter("ध", "dha", "Aspirated Dental", "Âm răng bật hơi", "Consonant"),
        AlphabetLetter("न", "na", "Dental (river)", "Âm răng", "Consonant"),
        // Labial
        AlphabetLetter("प", "pa", "Labial (kite)", "Âm môi", "Consonant"),
        AlphabetLetter("फ", "pha", "Aspirated (flower)", "Âm môi bật hơi", "Consonant"),
        AlphabetLetter("ब", "ba", "Labial (monkey)", "Âm môi", "Consonant"),
        AlphabetLetter("भ", "bha", "Aspirated (bear)", "Âm môi bật hơi", "Consonant"),
        AlphabetLetter("म", "ma", "Labial (fish)", "Âm môi", "Consonant"),
        // Semivowel
        AlphabetLetter("य", "ya", "Semivowel (journey)", "Bán nguyên âm", "Consonant"),
        AlphabetLetter("र", "ra", "Semivowel (king)", "Bán nguyên âm", "Consonant"),
        AlphabetLetter("ल", "la", "Semivowel (boy)", "Bán nguyên âm", "Consonant"),
        AlphabetLetter("व", "va", "Semivowel (forest)", "Bán nguyên âm", "Consonant"),
        // Sibilant
        AlphabetLetter("श", "sha", "Sibilant (lion)", "Âm xuýt", "Consonant"),
        AlphabetLetter("ष", "sha", "Retroflex sibilant", "Âm xuýt quặt lưỡi", "Consonant"),
        AlphabetLetter("स", "sa", "Sibilant (apple)", "Âm xuýt", "Consonant"),
        AlphabetLetter("ह", "ha", "Aspirate (elephant)", "Âm hơi họng", "Consonant"),
        // Joint
        AlphabetLetter("क्ष", "ksha", "Conjunct (bird)", "Âm ghép 'ksha'", "Consonant"),
        AlphabetLetter("त्र", "tra", "Conjunct (trident)", "Âm ghép 'tra'", "Consonant"),
        AlphabetLetter("ज्ञ", "gya", "Conjunct (knowledge)", "Âm ghép 'gya'", "Consonant"),
        AlphabetLetter("श्र", "shra", "Conjunct (Mr.)", "Âm ghép 'shra'", "Consonant")
    )

    PremiumBackground {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            text = if (isVi) "Học bảng chữ cái" else "Alphabet Academy", 
                            color = MaterialTheme.colorScheme.onSurface, 
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                                contentDescription = "Back", 
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                // Info block
                GlassCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (isVi) "Nhấn vào bất kỳ ký tự nào để nghe cách phát âm tiếng Hindi bản xứ!" 
                               else "Tap any letter to hear the native Hindi pronunciation!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text(if (isVi) "Nguyên âm" else "Vowels", fontWeight = FontWeight.Bold) },
                        selectedContentColor = DeepSaffron,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text(if (isVi) "Phụ âm" else "Consonants", fontWeight = FontWeight.Bold) },
                        selectedContentColor = DeepSaffron,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Letter Grid
                val currentPool = if (selectedTab == 0) vowels else consonants
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(currentPool) { letter ->
                        LetterCard(
                            letter = letter,
                            isVi = isVi,
                            onClick = {
                                AudioHelper.playAudio(
                                    context = context,
                                    audioKey = letter.character,
                                    tts = tts,
                                    textFallback = letter.character
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LetterCard(
    letter: AlphabetLetter,
    isVi: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = letter.character,
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "/${letter.transliteration}/",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepSaffron,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isVi) letter.descriptionVi else letter.descriptionEn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(RoyalBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Pronounce",
                    tint = RoyalBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
