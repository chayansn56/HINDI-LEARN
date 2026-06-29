package com.example.hindilearn.ui.gamified

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleplayScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val progress = UserManager.progress
    val isVi = progress.selectedLanguage == "VI"
    
    var selectedScenario by remember { mutableStateOf<String?>(null) }

    PremiumBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (isVi) "Giao tiếp AI" else "AI Roleplay", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
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
            Box(modifier = modifier.fillMaxSize().padding(padding)) {
                if (selectedScenario == null) {
                    ScenarioSelection(isVi = isVi) { scenario ->
                        selectedScenario = scenario
                    }
                } else {
                    ChatInterface(isVi = isVi, scenario = selectedScenario!!)
                }
            }
        }
    }
}

@Composable
fun ScenarioSelection(isVi: Boolean, onSelect: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isVi) "Chọn một tình huống" else "Choose a Scenario",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(24.dp))

        val scenarios = listOf(
            if (isVi) "Tại nhà hàng" else "At the Restaurant",
            if (isVi) "Gọi Taxi" else "Calling a Taxi",
            if (isVi) "Mặc cả ngoài chợ" else "Bargaining at the Market",
            if (isVi) "Phỏng vấn xin việc" else "Job Interview",
            if (isVi) "Nhận phòng khách sạn" else "Hotel Check-in",
            if (isVi) "Hỏi đường đi" else "Asking for Directions"
        )

        scenarios.forEach { scenario ->
            Card(
                onClick = { onSelect(scenario) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🤖", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(scenario, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
fun ChatInterface(isVi: Boolean, scenario: String) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    var tts by remember { mutableStateOf<android.speech.tts.TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission(),
        onResult = { hasPermission = it }
    )
    
    LaunchedEffect(Unit) {
        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }
    
    DisposableEffect(context) {
        var newTts: android.speech.tts.TextToSpeech? = null
        newTts = android.speech.tts.TextToSpeech(context) { status ->
            if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                val locale = if (UserManager.progress.selectedCourse == "ENGLISH") {
                    java.util.Locale.US
                } else {
                    java.util.Locale("hi", "IN")
                }
                newTts?.language = locale
                isTtsReady = true
            }
        }
        tts = newTts
        
        onDispose {
            newTts?.stop()
            newTts?.shutdown()
        }
    }

    var messages by remember { 
        mutableStateOf(listOf(
            ChatMessage(
                run {
                    val isEnglish = UserManager.progress.selectedCourse == "ENGLISH"
                    if (scenario.contains("Restaurant") || scenario.contains("nhà hàng")) {
                        if (isEnglish) "Welcome! What would you like to order?" else "नमस्ते! आप क्या ऑर्डर करना चाहेंगे?"
                    } else if (scenario.contains("Taxi")) {
                        if (isEnglish) "Where do you want to go?" else "नमस्ते! आप कहाँ जाना चाहते हैं?"
                    } else if (scenario.contains("Interview") || scenario.contains("việc")) {
                        if (isEnglish) "Hello! Welcome to the interview. Tell me about yourself." else "नमस्ते! साक्षात्कार में आपका स्वागत है। अपने बारे में कुछ बताइए।"
                    } else if (scenario.contains("Hotel") || scenario.contains("sạn")) {
                        if (isEnglish) "Welcome to our hotel. Can I see your ID, please?" else "हमारे होटल में आपका स्वागत है। क्या मैं आपकी पहचान आईडी देख सकता हूँ?"
                    } else if (scenario.contains("Directions") || scenario.contains("đường")) {
                        if (isEnglish) "Excuse me, are you lost? How can I help you?" else "क्षमा करें, क्या आप रास्ता भटक गए हैं? मैं आपकी क्या मदद कर सकता हूँ?"
                    } else {
                        if (isEnglish) "This is 500 rupees. Very cheap!" else "यह पाँच सौ रुपये का है। बहुत सस्ता है!"
                    }
                },
                false
            )
        )) 
    }
    
    // Speak initial message when TTS is ready
    LaunchedEffect(isTtsReady) {
        if (isTtsReady && messages.isNotEmpty()) {
            tts?.speak(messages.first().text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var isListening by remember { mutableStateOf(false) }

    val speechRecognizer = remember { android.speech.SpeechRecognizer.createSpeechRecognizer(context) }
    val intent = remember {
        android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, if (UserManager.progress.selectedCourse == "ENGLISH") java.util.Locale.US.toString() else "hi-IN")
        }
    }

    DisposableEffect(Unit) {
        val listener = object : android.speech.RecognitionListener {
            override fun onReadyForSpeech(params: android.os.Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { isListening = false }
            override fun onError(error: Int) { isListening = false }
            override fun onResults(results: android.os.Bundle?) {
                val matches = results?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    inputText = matches[0]
                }
                isListening = false
            }
            override fun onPartialResults(partialResults: android.os.Bundle?) {}
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(listener)
        onDispose { speechRecognizer.destroy() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat History
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            reverseLayout = true
        ) {
            if (isTyping) {
                item {
                    ChatBubble(message = ChatMessage("...", false))
                }
            }
            items(messages.reversed()) { msg ->
                ChatBubble(message = msg)
            }
        }

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(24.dp)),
                placeholder = { Text(if (isVi) "Nói gì đó..." else "Say something...") },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Send
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onSend = {
                        if (inputText.isNotBlank() && !isTyping) {
                            val userText = inputText
                            inputText = ""
                            messages = messages + ChatMessage(userText, true)
                            
                            isTyping = true
                            coroutineScope.launch {
                                val targetLanguage = if (UserManager.progress.selectedCourse == "ENGLISH") "English" else "Hindi"
                                val nativeLanguage = if (isVi) "Vietnamese" else "English"
                                
                                val systemPrompt = """
                                    You are a helpful AI language tutor. 
                                    The user is practicing speaking $targetLanguage in this scenario: $scenario. 
                                    Respond in $targetLanguage, but keep it simple. If the user makes a mistake, gently correct them in $nativeLanguage.
                                    Keep responses very short (1-2 sentences).
                                """.trimIndent()
                                
                                val chatHistory = messages.map { Pair(it.text, it.isUser) }
                                
                                val reply = com.example.hindilearn.data.OpenAiService.generateChatResponse(systemPrompt, chatHistory)
                                    ?: (if (isVi) "Xin lỗi, tôi không thể kết nối. Vui lòng thử lại." else "Sorry, I couldn't connect. Please try again.")
                                    
                                isTyping = false
                                messages = messages + ChatMessage(reply, false)
                                tts?.speak(reply, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                        }
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (inputText.isNotBlank() && !isTyping) {
                        val userText = inputText
                        inputText = ""
                        messages = messages + ChatMessage(userText, true)
                        
                        isTyping = true
                        coroutineScope.launch {
                            val targetLanguage = if (UserManager.progress.selectedCourse == "ENGLISH") "English" else "Hindi"
                            val nativeLanguage = if (isVi) "Vietnamese" else "English"
                            
                            val systemPrompt = """
                                You are a helpful AI language tutor. 
                                The user is practicing speaking ${'$'}targetLanguage in this scenario: ${'$'}scenario. 
                                Respond in ${'$'}targetLanguage, but keep it simple. If the user makes a mistake, gently correct them in ${'$'}nativeLanguage.
                                Keep responses very short (1-2 sentences).
                            """.trimIndent()
                            
                            val chatHistory = messages.map { Pair(it.text, it.isUser) }
                            
                            val reply = com.example.hindilearn.data.OpenAiService.generateChatResponse(systemPrompt, chatHistory)
                                ?: (if (isVi) "Xin lỗi, tôi không thể kết nối. Vui lòng thử lại." else "Sorry, I couldn't connect. Please try again.")
                                
                            isTyping = false
                            messages = messages + ChatMessage(reply, false)
                            
                            // Voice-to-Voice: speak the reply!
                            tts?.speak(reply, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    }
                },
                modifier = Modifier.background(DeepSaffron, CircleShape).size(48.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (hasPermission && !isTyping) {
                        if (isListening) {
                            speechRecognizer.stopListening()
                        } else {
                            inputText = ""
                            speechRecognizer.startListening(intent)
                            isListening = true
                        }
                    }
                },
                modifier = Modifier.background(if (isListening) Color.Red else SoftGreen, CircleShape).size(48.dp)
            ) {
                Icon(if (isListening) Icons.Default.Stop else Icons.Default.Mic, contentDescription = "Mic", tint = if (isListening) Color.White else RoyalBlue)
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(
                    topStart = 16.dp, 
                    topEnd = 16.dp, 
                    bottomStart = if (message.isUser) 16.dp else 0.dp, 
                    bottomEnd = if (message.isUser) 0.dp else 16.dp
                ))
                .background(if (message.isUser) DeepSaffron else MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
