package com.example.hindilearn.ui.gamified

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hindilearn.data.OpenAiService
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoatTalkScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val progress = UserManager.progress
    val isVi = progress.selectedLanguage == "VI"

    // Supported target languages
    var activeLanguage by remember { mutableStateOf("EN") } // "EN", "HI", "VI"
    var isListening by remember { mutableStateOf(false) }
    var chatMessages by remember { mutableStateOf(listOf<Pair<String, Boolean>>(
        (if (isVi) "Xin chào! Mình là thuyền buồm Vietana. Nhấn giữ hoặc chạm vào micro để trò chuyện cùng mình nhé! ⛵" 
         else "Hello! I am the Vietana sailing boat. Tap the microphone to talk with me! ⛵") to false
    )) }

    // TTS Setup
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Initialize defaults
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    fun speak(text: String, languageCode: String) {
        tts?.let { engine ->
            val locale = when (languageCode) {
                "HI" -> Locale("hi", "IN")
                "VI" -> Locale("vi", "VN")
                else -> Locale.US
            }
            engine.language = locale
            engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "boat_talk_response")
        }
    }

    // Animation values for the speaking boat
    val infiniteTransition = rememberInfiniteTransition(label = "BoatSailingCabin")
    val boatTranslationY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TranslationY"
    )
    val boatRotationZ by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "RotationZ"
    )

    // Glowing animation for active speech recording
    val pulseScale by animateFloatAsState(
        targetValue = if (isListening) 1.25f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "MicPulse"
    )

    // STT SpeechRecognizer Setup
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val speechRecognizerIntent = remember(activeLanguage) {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            val langTag = when (activeLanguage) {
                "HI" -> "hi-IN"
                "VI" -> "vi-VN"
                else -> "en-US"
            }
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, langTag)
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, langTag)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }

    fun startListening() {
        isListening = true
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                isListening = false
            }
            override fun onError(error: Int) {
                isListening = false
                val errorMsg = when (error) {
                    SpeechRecognizer.ERROR_NO_MATCH -> if (isVi) "Không nghe rõ, thử lại nhé!" else "Didn't hear that, try again!"
                    SpeechRecognizer.ERROR_NETWORK -> if (isVi) "Lỗi kết nối mạng!" else "Network connection error!"
                    else -> "STT error code: $error"
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val spokenText = matches?.firstOrNull()
                if (!spokenText.isNullOrBlank()) {
                    // 1. Add User query to chat logs
                    chatMessages = chatMessages + (spokenText to true)
                    coroutineScope.launch {
                        listState.animateScrollToItem(chatMessages.size - 1)
                        
                        // 2. Query Gemini API
                        val prompt = "You are the Vietana mascot boat character (speaking blue boat, gold/saffron sails). The user speaks in $activeLanguage. Reply to: \"$spokenText\" directly in $activeLanguage in under 18 words, keeping it motivating and natural."
                        val response = OpenAiService.generateChatResponse(prompt, emptyList())
                        val replyText = if (!response.isNullOrBlank() && !response.startsWith("Error")) {
                            response
                        } else {
                            if (activeLanguage == "HI") "बहुत बढ़िया! सीखते रहें। ⛵"
                            else if (activeLanguage == "VI") "Tuyệt vời lắm! Tiếp tục cố gắng nhé! ⛵"
                            else "Wonderful! Keep practicing! ⛵"
                        }
                        
                        // 3. Add Boat response to chat logs
                        chatMessages = chatMessages + (replyText to false)
                        listState.animateScrollToItem(chatMessages.size - 1)
                        
                        // 4. Speak response aloud
                        speak(replyText, activeLanguage)
                    }
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    PremiumBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = DeepSaffron)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isVi) "Cabin Trò Chuyện" else "Mascot Chat Cabin",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DeepSaffron
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Floating Mascot Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer {
                            translationY = boatTranslationY
                            rotationZ = boatRotationZ
                        },
                    contentAlignment = Alignment.Center
                ) {
                    VietanaMascot(modifier = Modifier.size(120.dp))
                }
            }

            // Language Selector Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val languages = listOf("EN" to "English 🇬🇧", "HI" to "Hindi 🇮🇳", "VI" to "Tiếng Việt 🇻🇳")
                languages.forEach { (code, label) ->
                    val isSelected = activeLanguage == code
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) DeepSaffron else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable { activeLanguage = code }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chat Messages Log
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(chatMessages) { (text, isUser) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(horizontal = 4.dp),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isUser) 16.dp else 4.dp,
                                bottomEnd = if (isUser) 4.dp else 16.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = text,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (!isUser) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = { speak(text, activeLanguage) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Speak Aloud",
                                            tint = DeepSaffron,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Glowing Floating Microphone Action Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = {
                        if (isListening) {
                            speechRecognizer.stopListening()
                            isListening = false
                        } else {
                            startListening()
                        }
                    },
                    modifier = Modifier
                        .size(72.dp)
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                        },
                    shape = CircleShape,
                    containerColor = if (isListening) Color(0xFFE57373) else DeepSaffron,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Talk to Boat",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}