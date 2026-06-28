package com.example.hindilearn.ui.gamified

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Mic
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
import com.example.hindilearn.data.OpenAiService
import com.example.hindilearn.theme.*
import java.util.Locale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch

data class StoryParagraph(
    val hindi: String,
    val english: String,
    val vietnamese: String
)

data class Story(
    val id: String,
    val titleEn: String,
    val titleVi: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionVi: String,
    val characterIcon: String,
    val imageDrawableName: String,
    val paragraphs: List<StoryParagraph>,
    val questionEn: String,
    val questionVi: String,
    val options: List<String>,
    val correctOptionIdx: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lang = UserManager.progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    
    var activeStory by remember { mutableStateOf<Story?>(null) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    
    DisposableEffect(context) {
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLocale = if (UserManager.progress.selectedCourse == "ENGLISH")
                    Locale.forLanguageTag("en-US")
                else
                    Locale.forLanguageTag("hi-IN")
                textToSpeech?.language = ttsLocale
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    var stories = remember { androidx.compose.runtime.mutableStateListOf<Story>() }
    
    androidx.compose.runtime.LaunchedEffect(Unit) {
        try {
            val inputStream = context.assets.open("episodes/stories.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            val jsonArray = org.json.JSONArray(jsonString)
            
            val parsedStories = mutableListOf<Story>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                
                val paragraphsArray = obj.getJSONArray("paragraphs")
                val paragraphs = mutableListOf<StoryParagraph>()
                for (j in 0 until paragraphsArray.length()) {
                    val pObj = paragraphsArray.getJSONObject(j)
                    paragraphs.add(
                        StoryParagraph(
                            hindi = pObj.getString("hindi"),
                            english = pObj.getString("english"),
                            vietnamese = pObj.getString("vietnamese")
                        )
                    )
                }
                
                val optionsArray = obj.getJSONArray("options")
                val options = mutableListOf<String>()
                for (j in 0 until optionsArray.length()) {
                    options.add(optionsArray.getString(j))
                }
                
                parsedStories.add(
                    Story(
                        id = obj.getString("id"),
                        titleHi = obj.getString("titleHi"),
                        titleEn = obj.getString("titleEn"),
                        titleVi = obj.getString("titleVi"),
                        descriptionEn = obj.getString("descriptionEn"),
                        descriptionVi = obj.getString("descriptionVi"),
                        characterIcon = obj.getString("characterIcon"),
                        imageDrawableName = obj.getString("imageDrawableName"),
                        paragraphs = paragraphs,
                        questionEn = obj.getString("questionEn"),
                        questionVi = obj.getString("questionVi"),
                        options = options,
                        correctOptionIdx = obj.getInt("correctOptionIdx")
                    )
                )
            }
            stories.clear()
            stories.addAll(parsedStories)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    PremiumBackground {
        Crossfade(targetState = activeStory, label = "ReaderTransition") { story ->
            if (story == null) {
                // List Mode
                    var showAiDialog by remember { mutableStateOf(false) }
                    var userPrompt by remember { mutableStateOf("") }
                    var isGenerating by remember { mutableStateOf(false) }
                    var aiGenError by remember { mutableStateOf<String?>(null) }
                    val coroutineScope = rememberCoroutineScope()

                    Scaffold(
                        modifier = modifier,
                        containerColor = Color.Transparent,
                        topBar = {
                            TopAppBar(
                                title = { Text(if (isVi) "Góc kể chuyện" else "Stories Center", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                                navigationIcon = {
                                    IconButton(onClick = onBack) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                            )
                        }
                    ) { padding ->
                        if (showAiDialog) {
                            AlertDialog(
                                onDismissRequest = { if (!isGenerating) showAiDialog = false },
                                title = { Text(if (isVi) "Tạo truyện AI tùy chỉnh" else "Create Custom AI Story", fontWeight = FontWeight.Bold) },
                                text = {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            if (isVi) "Nhập chủ đề hoặc bối cảnh truyện (ví dụ: 'Đi chợ', 'Trong văn phòng'):"
                                            else "Enter a theme or scenario for the story (e.g., 'At the market', 'In the office'):",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        OutlinedTextField(
                                            value = userPrompt,
                                            onValueChange = { userPrompt = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            placeholder = { Text("Scenario prompt...") },
                                            enabled = !isGenerating
                                        )
                                        if (isGenerating) {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                                                CircularProgressIndicator(color = RoyalBlue, modifier = Modifier.size(24.dp))
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(if (isVi) "AI đang sáng tác..." else "AI is writing story...")
                                            }
                                        }
                                        aiGenError?.let { err ->
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(err, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                },
                                confirmButton = {
                                    TextButton(
                                        enabled = userPrompt.isNotBlank() && !isGenerating,
                                        onClick = {
                                            isGenerating = true
                                            aiGenError = null
                                            coroutineScope.launch {
                                                val course = UserManager.progress.selectedCourse
                                                val systemPrompt = """
                                                    You are a creative writer and language teacher.
                                                    Create a short story of about 100 words based on this theme: "$userPrompt".
                                                    The learning course is $course.
                                                    If selected course is HINDI: write the story in Hindi, translate it to English and Vietnamese.
                                                    If selected course is ENGLISH: write the story in English, translate it to Vietnamese (keep English field same as original).
                                                    
                                                    Also write 1 comprehension multiple choice question based on the story.
                                                    
                                                    Return ONLY a JSON object formatted exactly as follows:
                                                    {
                                                      "titleHi": "Title in Target Language (Hindi/English)",
                                                      "titleEn": "Title in English",
                                                      "titleVi": "Title in Vietnamese",
                                                      "descriptionEn": "Brief summary in English",
                                                      "descriptionVi": "Brief summary in Vietnamese",
                                                      "paragraphs": [
                                                        {
                                                          "hindi": "target language paragraph sentence",
                                                          "english": "english translation",
                                                          "vietnamese": "vietnamese translation"
                                                        }
                                                      ],
                                                      "questionEn": "Comprehension question in English",
                                                      "questionVi": "Comprehension question in Vietnamese",
                                                      "options": ["Correct option", "Distractor 1", "Distractor 2", "Distractor 3"],
                                                      "correctOptionIdx": 0
                                                    }
                                                    
                                                    Do NOT wrap in markdown.
                                                """.trimIndent()

                                                val response = OpenAiService.generateChatResponse(systemPrompt, listOf("Create story" to true))
                                                if (response != null && !response.startsWith("Error")) {
                                                    try {
                                                        val cleanResponse = response.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                                                        val obj = org.json.JSONObject(cleanResponse)
                                                        val paragraphsArray = obj.getJSONArray("paragraphs")
                                                        val paragraphs = mutableListOf<StoryParagraph>()
                                                        for (j in 0 until paragraphsArray.length()) {
                                                            val pObj = paragraphsArray.getJSONObject(j)
                                                            paragraphs.add(
                                                                StoryParagraph(
                                                                    hindi = pObj.getString("hindi"),
                                                                    english = pObj.getString("english"),
                                                                    vietnamese = pObj.getString("vietnamese")
                                                                )
                                                            )
                                                        }
                                                        val optionsArray = obj.getJSONArray("options")
                                                        val options = mutableListOf<String>()
                                                        for (j in 0 until optionsArray.length()) {
                                                            options.add(optionsArray.getString(j))
                                                        }
                                                        
                                                        val generatedStory = Story(
                                                            id = "ai_story_${System.currentTimeMillis()}",
                                                            titleHi = obj.getString("titleHi"),
                                                            titleEn = obj.getString("titleEn"),
                                                            titleVi = obj.getString("titleVi"),
                                                            descriptionEn = obj.getString("descriptionEn"),
                                                            descriptionVi = obj.getString("descriptionVi"),
                                                            characterIcon = "🤖",
                                                            imageDrawableName = "",
                                                            paragraphs = paragraphs,
                                                            questionEn = obj.getString("questionEn"),
                                                            questionVi = obj.getString("questionVi"),
                                                            options = options,
                                                            correctOptionIdx = obj.getInt("correctOptionIdx")
                                                        )
                                                        activeStory = generatedStory
                                                        showAiDialog = false
                                                    } catch (e: Exception) {
                                                        aiGenError = "Parsing error: ${e.message}"
                                                    }
                                                } else {
                                                    aiGenError = "Connection/AI response error"
                                                }
                                                isGenerating = false
                                            }
                                        }
                                    ) {
                                        Text(if (isVi) "Tạo truyện" else "Create")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        enabled = !isGenerating,
                                        onClick = { showAiDialog = false }
                                    ) {
                                        Text(if (isVi) "Hủy" else "Cancel")
                                    }
                                }
                            )
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(padding),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showAiDialog = true },
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.cardColors(containerColor = RoyalBlue)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(20.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("🤖", fontSize = 28.sp)
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                if (isVi) "TRUYỆN AI TỰ CHỌN" else "CUSTOM AI STORY",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = PremiumGold
                                            )
                                            Text(
                                                if (isVi) "Sáng tạo truyện theo chủ đề bạn muốn!" else "Compose stories for any theme on demand!",
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            itemsIndexed(
                                items = stories,
                                key = { _, item -> item.id }
                            ) { _, item ->
                                StoryListCard(
                                    story = item,
                                    isVi = isVi,
                                    onClick = { activeStory = item }
                                )
                            }
                        }
                    }
            } else {
                // Reader Mode
                ReaderView(
                    story = story,
                    isVi = isVi,
                    tts = tts,
                    onClose = { activeStory = null }
                )
            }
        }
    }
}

@Composable
fun StoryListCard(
    story: Story,
    isVi: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            val imageRes = remember(story.imageDrawableName) {
                context.resources.getIdentifier(story.imageDrawableName, "drawable", context.packageName)
            }
            if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(DeepSaffron.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(story.characterIcon, fontSize = 28.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    story.titleHi,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepSaffron
                )
                Text(
                    if (isVi) story.titleVi else story.titleEn,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    if (isVi) story.descriptionVi else story.descriptionEn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RoyalBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Read", tint = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderView(
    story: Story,
    isVi: Boolean,
    tts: TextToSpeech?,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedAnswerIdx by remember { mutableStateOf<Int?>(null) }
    var answerChecked by remember { mutableStateOf(false) }
    var showQuizFeedback by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(if (isVi) story.titleVi else story.titleEn, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        story.titleHi,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = DeepSaffron,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (isVi) story.titleVi else story.titleEn,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    val context = LocalContext.current
                    val imageRes = remember(story.imageDrawableName) {
                        context.resources.getIdentifier(story.imageDrawableName, "drawable", context.packageName)
                    }
                    if (imageRes != 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Paragraph List
            itemsIndexed(
                items = story.paragraphs,
                key = { idx, _ -> "${story.id}_p_${idx}" }
            ) { _, p ->
                ParagraphCard(
                    para = p,
                    isVi = isVi,
                    onClickAudio = {
                        AudioHelper.playAudio(
                            context = context,
                            audioKey = "",
                            tts = tts,
                            textFallback = p.hindi
                        )
                    }
                )
            }

            // Comprehension Quiz
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (isVi) "Kiểm tra đọc hiểu" else "Reading Comprehension",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepSaffron
                )
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = if (isVi) story.questionVi else story.questionEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        story.options.forEachIndexed { idx, option ->
                            val isSelected = selectedAnswerIdx == idx
                            val cardColor = when {
                                answerChecked && idx == story.correctOptionIdx -> SoftGreen
                                answerChecked && isSelected && idx != story.correctOptionIdx -> SoftRed
                                isSelected -> DeepSaffron.copy(alpha = 0.2f)
                                else -> Color.White.copy(alpha = 0.5f)
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable(enabled = !answerChecked) { selectedAnswerIdx = idx },
                                colors = CardDefaults.cardColors(containerColor = cardColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { if (!answerChecked) selectedAnswerIdx = idx },
                                        enabled = !answerChecked,
                                        colors = RadioButtonDefaults.colors(selectedColor = DeepSaffron)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!answerChecked) {
                            PremiumButton(
                                text = "Check Answer",
                                onClick = {
                                    if (selectedAnswerIdx != null) {
                                        answerChecked = true
                                        showQuizFeedback = true
                                    }
                                },
                                enabled = selectedAnswerIdx != null
                            )
                        } else {
                            val isCorrect = selectedAnswerIdx == story.correctOptionIdx
                            val feedbackMsg = if (isCorrect) {
                                if (isVi) "Chính xác! +10 XP đã được cộng." else "Correct! +10 XP has been registered."
                            } else {
                                if (isVi) "Không chính xác. Hãy tiếp tục học tập!" else "Incorrect. Keep practicing!"
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = feedbackMsg,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCorrect) SoftGreen else SoftRed,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                PremiumButton(
                                    text = "Finish Story",
                                    onClick = {
                                        if (isCorrect) {
                                            UserManager.addXp(10)
                                        }
                                        onClose()
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
fun ParagraphCard(
    para: StoryParagraph,
    isVi: Boolean,
    onClickAudio: () -> Unit
) {
    var selectedWordForExplain by remember { mutableStateOf<String?>(null) }
    var isExplainingWord by remember { mutableStateOf(false) }
    var wordExplainText by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Speech recognition variables for coaching
    val context = LocalContext.current
    var spokenFeedbackText by remember { mutableStateOf<String?>(null) }
    var isListeningSpeech by remember { mutableStateOf(false) }
    var hasRecordPermission by remember { mutableStateOf(false) }
    val speechRecognizer = remember { android.speech.SpeechRecognizer.createSpeechRecognizer(context) }
    
    val requestPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission(),
        onResult = { hasRecordPermission = it }
    )

    if (selectedWordForExplain != null) {
        AlertDialog(
            onDismissRequest = { selectedWordForExplain = null },
            title = { Text(selectedWordForExplain ?: "") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (isExplainingWord) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = RoyalBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isVi) "AI đang phân tích..." else "AI is analyzing word...")
                        }
                    } else {
                        Text(wordExplainText ?: "")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedWordForExplain = null }) {
                    Text("OK")
                }
            }
        )
    }

    if (spokenFeedbackText != null) {
        AlertDialog(
            onDismissRequest = { spokenFeedbackText = null },
            title = { Text(if (isVi) "Kết quả luyện nói" else "Speaking Practice Review") },
            text = { Text(spokenFeedbackText ?: "") },
            confirmButton = {
                TextButton(onClick = { spokenFeedbackText = null }) {
                    Text("Close")
                }
            }
        )
    }

    DisposableEffect(Unit) {
        val listener = object : android.speech.RecognitionListener {
            override fun onReadyForSpeech(params: android.os.Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { isListeningSpeech = false }
            override fun onError(error: Int) {
                isListeningSpeech = false
                spokenFeedbackText = if (isVi) "Không thể ghi nhận âm thanh. Hãy thử lại!" else "Speech unrecognized. Please try again!"
            }
            override fun onResults(results: android.os.Bundle?) {
                val matches = results?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val userText = matches[0]
                    spokenFeedbackText = if (isVi) "Đang chấm điểm..." else "Grading pronunciation..."
                    coroutineScope.launch {
                        val review = OpenAiService.gradePronunciation(para.hindi, userText)
                        if (review != null) {
                            val grade = review.first
                            val rating = review.second
                            val feedback = review.third
                            val stars = "★".repeat(rating) + "☆".repeat(5 - rating)
                            spokenFeedbackText = """
                                Target: ${para.hindi}
                                You said: $userText
                                
                                Grade: $grade ($stars)
                                Feedback: $feedback
                            """.trimIndent()
                        } else {
                            spokenFeedbackText = if (isVi) "Lỗi kết nối AI" else "AI connection error"
                        }
                    }
                }
                isListeningSpeech = false
            }
            override fun onPartialResults(partialResults: android.os.Bundle?) {}
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(listener)
        onDispose { speechRecognizer.destroy() }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Interactive Word Span Row
            val words = remember(para.hindi) { para.hindi.split("\\s+".toRegex()).filter { it.isNotBlank() } }
            
            Text(
                text = if (isVi) "💡 Nhấp vào từ để xem giải nghĩa:" else "💡 Tap any word for live translation & grammar:",
                style = MaterialTheme.typography.labelSmall,
                color = DeepSaffron,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Flow Row of Tappable Words
            androidx.compose.foundation.layout.FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                words.forEach { word ->
                    val cleanWord = word.replace("[.,!?:;।]".toRegex(), "")
                    Text(
                        text = word,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        modifier = Modifier
                            .clickable {
                                selectedWordForExplain = cleanWord
                                isExplainingWord = true
                                coroutineScope.launch {
                                    val course = UserManager.progress.selectedCourse
                                    val systemPrompt = """
                                        Explain the word "$cleanWord" in the context of the story sentence "${para.hindi}".
                                        The user's course is $course.
                                        Provide the dictionary form, parts of speech, English/Vietnamese translations, and 1 short example.
                                        Be extremely concise (under 50 words).
                                    """.trimIndent()
                                    wordExplainText = OpenAiService.generateChatResponse(systemPrompt, listOf("Explain word" to true))
                                    isExplainingWord = false
                                }
                            }
                            .background(RoyalBlue.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isVi) para.vietnamese else para.english,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic Practice button
                IconButton(
                    onClick = {
                        if (!hasRecordPermission) {
                            requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                        } else {
                            if (isListeningSpeech) {
                                speechRecognizer.stopListening()
                                isListeningSpeech = false
                            } else {
                                val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, if (UserManager.progress.selectedCourse == "ENGLISH") "en-US" else "hi-IN")
                                }
                                speechRecognizer.startListening(intent)
                                isListeningSpeech = true
                            }
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background((if (isListeningSpeech) Color.Red else RoyalBlue).copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isListeningSpeech) Icons.Default.Close else Icons.Default.Mic,
                        contentDescription = "Practice speaking",
                        tint = if (isListeningSpeech) Color.Red else RoyalBlue
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Play Audio button
                IconButton(
                    onClick = onClickAudio,
                    modifier = Modifier
                        .size(48.dp)
                        .background(DeepSaffron.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Speak line",
                        tint = DeepSaffron
                    )
                }
            }
        }
    }
}
