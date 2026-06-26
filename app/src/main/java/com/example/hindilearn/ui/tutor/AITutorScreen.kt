package com.example.hindilearn.ui.tutor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.util.Locale
import com.example.hindilearn.ui.gamified.PremiumBackground
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import com.example.hindilearn.data.UserManager

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AITutorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var messages by remember { mutableStateOf(listOf(
        ChatMessage("Namaste! I am your AI Hindi Tutor. You can type or speak to me in Hindi or English, and I will help you practice!", false)
    )) }
    var currentInput by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startListening(speechRecognizer, { isListening = it }, { text -> currentInput = text })
        }
    }

    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) tts?.language = Locale.forLanguageTag("hi-IN")
        }
        tts = textToSpeech
        onDispose {
            speechRecognizer.destroy()
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    var isTyping by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun handleSend() {
        if (currentInput.isNotBlank() && !isTyping) {
            val userMsg = currentInput
            messages = messages + ChatMessage(userMsg, true)
            currentInput = ""
            
            isTyping = true
            coroutineScope.launch {
                val targetLanguage = if (UserManager.progress.selectedCourse == "ENGLISH") "English" else "Hindi"
                val systemPrompt = """
                    You are a friendly and helpful AI language tutor.
                    The user is practicing speaking and writing $targetLanguage.
                    Respond in $targetLanguage to help them practice. Keep responses concise and simple.
                """.trimIndent()
                
                val chatHistory = messages.map { Pair(it.text, it.isUser) }
                val aiResponse = com.example.hindilearn.data.OpenAiService.generateChatResponse(systemPrompt, chatHistory)
                    ?: "Sorry, I am having trouble connecting right now. Please try again."
                
                isTyping = false
                messages = messages + ChatMessage(aiResponse, false)
                tts?.speak(aiResponse, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    PremiumBackground {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("AI Hindi Tutor") },
                    navigationIcon = {
                        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(msg)
                }
            }

            Surface(color = MaterialTheme.colorScheme.surfaceVariant, tonalElevation = 2.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (isListening) {
                                speechRecognizer.stopListening()
                                isListening = false
                            } else {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                    startListening(speechRecognizer, { isListening = it }, { text -> currentInput = text })
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        }
                    ) {
                        Icon(
                            if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Microphone",
                            tint = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }

                    OutlinedTextField(
                        value = currentInput,
                        onValueChange = { currentInput = it },
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        placeholder = { Text("Type or speak...") },
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            imeAction = androidx.compose.ui.text.input.ImeAction.Send
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onSend = { handleSend() }
                        )
                    )

                    IconButton(onClick = { handleSend() }, enabled = currentInput.isNotBlank()) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val alignment = if (msg.isUser) Alignment.End else Alignment.Start
    
    val userGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF6750A4), Color(0xFF9C27B0))
    )
    val botGradient = Brush.linearGradient(
        colors = listOf(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.secondaryContainer)
    )
    
    val bubbleShape = if (msg.isUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }
    
    val textColor = if (msg.isUser) Color.White else MaterialTheme.colorScheme.onSecondaryContainer

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(if (msg.isUser) userGradient else botGradient, bubbleShape)
                .padding(16.dp)
        ) {
            Text(msg.text, color = textColor, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

fun startListening(
    speechRecognizer: SpeechRecognizer,
    setListeningState: (Boolean) -> Unit,
    onResult: (String) -> Unit
) {
    val isEnglish = UserManager.progress.selectedCourse == "ENGLISH"
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (isEnglish) Locale.US.toString() else "hi-IN")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) { setListeningState(true) }
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() { setListeningState(false) }
        override fun onError(error: Int) { setListeningState(false) }
        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) onResult(matches[0])
            setListeningState(false)
        }
        override fun onPartialResults(partialResults: Bundle?) {
            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) onResult(matches[0])
        }
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })
    
    speechRecognizer.startListening(intent)
}

fun mockAiResponse(input: String): String {
    val lowerInput = input.lowercase()
    return when {
        lowerInput.contains("namaste") || lowerInput.contains("hello") -> "Namaste! (नमस्ते!) How can I help you practice your Hindi today?"
        lowerInput.contains("rice") || lowerInput.contains("eat") -> "Great! 'I eat rice' translates to 'मैं चावल खाता हूँ' (Main chawal khata hoon). Good job!"
        lowerInput.contains("thank") -> "You're welcome! In Hindi, we say 'धन्यवाद' (Dhanyavaad)."
        else -> "That's wonderful! Keep practicing. You can ask me to translate sentences or test your grammar."
    }
}
