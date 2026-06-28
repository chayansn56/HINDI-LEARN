package com.example.hindilearn.ui.gamified

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hindilearn.data.OpenAiService
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PronunciationLabScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val progress = UserManager.progress
    val isVi = progress.selectedLanguage == "VI"
    val isEnglish = progress.selectedCourse == "ENGLISH"

    var hasPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasPermission = it }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    val hindiSentences = remember { listOf(
        "नमस्ते, आप कैसे हैं?",       // Hello, how are you?
        "मेरा नाम राज है।",            // My name is Raj.
        "मुझे हिंदी सीखना है।",        // I want to learn Hindi.
        "यह कितने का है?",             // How much is this?
        "धन्यवाद, आपकी मदद के लिए।",  // Thank you for your help.
        "कृपया धीरे बोलिए।",           // Please speak slowly.
        "मुझे समझ नहीं आया।",         // I didn't understand.
        "आप कहाँ से हैं?",             // Where are you from?
        "मैं ठीक हूँ, आप बताइए।",     // I'm fine, how about you?
        "एक कप चाय मिलेगी?",          // Can I get a cup of tea?
        "यह बहुत अच्छा है!",           // This is very good!
        "मुझे भूख लगी है।"             // I'm hungry.
    ) }
    val englishSentences = remember { listOf(
        "Hello, how are you today?",
        "My name is John and I am from America.",
        "Can you speak more slowly, please?",
        "Where is the nearest restaurant?",
        "I would like a cup of tea, please.",
        "How much does this cost?",
        "I don't understand. Can you repeat that?",
        "Thank you very much for your help.",
        "Nice to meet you!",
        "I am learning English every day.",
        "Could you help me find this address?",
        "The weather is beautiful today."
    ) }
    var currentSentenceIndex by remember { mutableIntStateOf(0) }
    val targetSentence = if (isEnglish) {
        englishSentences[currentSentenceIndex % englishSentences.size]
    } else {
        hindiSentences[currentSentenceIndex % hindiSentences.size]
    }
    var userSpokenText by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }
    var aiGrade by remember { mutableStateOf<String?>(null) }
    var aiRating by remember { mutableIntStateOf(3) }
    var aiFeedback by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }

    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val intent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (isEnglish) Locale.US.toString() else "hi-IN")
        }
    }

    DisposableEffect(Unit) {
        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { isListening = false }
            override fun onError(error: Int) {
                isListening = false
                aiFeedback = when (error) {
                    android.speech.SpeechRecognizer.ERROR_NO_MATCH -> if (isVi) "Không nghe thấy gì. Thử lại nhé!" else "Didn't catch that. Please try again!"
                    android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> if (isVi) "Hết thời gian. Hãy nói to hơn!" else "Timed out. Please speak louder!"
                    else -> if (isVi) "Lỗi nhận dạng giọng nói. Thử lại!" else "Recognition error. Please try again!"
                }
                aiGrade = "?"
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    userSpokenText = matches[0]
                    isAnalyzing = true
                    coroutineScope.launch {
                        val result = OpenAiService.gradePronunciation(targetSentence, userSpokenText)
                        if (result != null) {
                            aiGrade = result.first
                            aiRating = result.second
                            aiFeedback = result.third
                        } else {
                            aiFeedback = if (isVi) "Lỗi kết nối AI" else "AI Connection Error"
                        }
                        isAnalyzing = false
                    }
                }
                isListening = false
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(listener)
        onDispose { speechRecognizer.destroy() }
    }

    PremiumBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (isVi) "Phòng thí nghiệm phát âm" else "Pronunciation Lab", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = modifier.fillMaxSize().padding(padding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Target Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (isVi) "Hãy nói to:" else "Say this out loud:", style = MaterialTheme.typography.titleMedium, color = TextDark)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(targetSentence, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = RoyalBlue)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // User Result
                AnimatedVisibility(visible = userSpokenText.isNotEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (isVi) "Bạn đã nói:" else "You said:", color = TextDark)
                        Text(userSpokenText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = DeepSaffron)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // AI Grading Result
                if (isAnalyzing) {
                    CircularProgressIndicator(color = RoyalBlue)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if (isVi) "AI đang phân tích..." else "AI is analyzing...", color = TextDark)
                } else if (aiGrade != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = when (aiGrade) {
                            "A" -> Color(0xFF2E7D32)  // Deep green
                            "B" -> Color(0xFF558B2F)  // Light green
                            "C" -> Color(0xFFF9A825)  // Amber
                            "D" -> Color(0xFFE65100)  // Orange
                            "F" -> Color(0xFFC62828)  // Red
                            else -> Color(0xFF546E7A)
                        }),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            val gradeEmoji = when (aiGrade) { "A" -> "🌟"; "B" -> "👍"; "C" -> "💪"; "D" -> "🎯"; "F" -> "🔄"; else -> "❓" }
                            Text("$gradeEmoji Grade: $aiGrade", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // 1-5 Star Ratings Row
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (i in 1..5) {
                                    val starColor = if (i <= aiRating) PremiumGold else Color.White.copy(alpha = 0.3f)
                                    Text("★", fontSize = 28.sp, color = starColor)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                aiFeedback ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    currentSentenceIndex++
                                    userSpokenText = ""
                                    aiGrade = null
                                    aiFeedback = null
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (isVi) "Câu tiếp theo →" else "Next Sentence →", color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Mic Button
                if (hasPermission) {
                    FloatingActionButton(
                        onClick = {
                            if (isListening) {
                                speechRecognizer.stopListening()
                            } else {
                                userSpokenText = ""
                                aiGrade = null
                                aiFeedback = null
                                speechRecognizer.startListening(intent)
                                isListening = true
                            }
                        },
                        containerColor = if (isListening) Color.Red else RoyalBlue,
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape
                    ) {
                        Icon(
                            if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = "Mic",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                } else {
                    Text(if (isVi) "Cần cấp quyền micro" else "Microphone permission required", color = Color.Red)
                }
            }
        }
    }
}
