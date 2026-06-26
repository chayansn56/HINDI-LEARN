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
import java.util.Locale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale

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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = para.hindi,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isVi) para.vietnamese else para.english,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
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
