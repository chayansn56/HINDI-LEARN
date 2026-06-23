package com.example.hindilearn.ui.gamified
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.data.AudioHelper
import com.example.hindilearn.ui.gamified.PremiumBackground
import com.example.hindilearn.ui.gamified.ConfettiOverlay
import com.example.hindilearn.ui.gamified.PremiumButton
import com.example.hindilearn.ui.gamified.GlassCard
import com.example.hindilearn.ui.gamified.AnimatedProgressBar
import java.util.Locale

@Composable
fun MultipleChoiceUI(ex: Exercise.MultipleChoice, tts: TextToSpeech?, isCorrect: Boolean?, onAnswer: (String) -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(ex.prompt, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.size(200.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(ex.text, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
                        Text(ex.subtext, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { AudioHelper.playAudio(context, ex.text, tts, ex.text, speechRate = 0.5f) }) {
                                Text("🐢", style = MaterialTheme.typography.titleLarge)
                            }
                            IconButton(onClick = { AudioHelper.playAudio(context, ex.text, tts, ex.text) }) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
        
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            ex.options.forEach { option ->
                val btnColor = when {
                    isCorrect == true && option == ex.answer -> MaterialTheme.colorScheme.primary
                    isCorrect == false && option == ex.answer -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                Button(
                    onClick = { onAnswer(option) },
                    colors = ButtonDefaults.buttonColors(containerColor = btnColor, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(option, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderUI(ex: Exercise.SentenceBuilder, isCorrect: Boolean?, onAnswer: (String) -> Unit) {
    var selectedWords by remember { mutableStateOf(listOf<String>()) }
    var availableWords by remember { mutableStateOf(ex.hindiWords.shuffled()) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Translate this sentence", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(ex.englishSentence, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Answer Area
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(16.dp)
                ) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        selectedWords.forEach { word ->
                            WordChip(word) {
                                if (isCorrect == null) {
                                    selectedWords = selectedWords - word
                                    availableWords = availableWords + word
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Word Bank
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    availableWords.forEach { word ->
                        WordChip(word) {
                            if (isCorrect == null) {
                                availableWords = availableWords - word
                                selectedWords = selectedWords + word
                            }
                        }
                    }
                }
            }
        }
        
        Box(modifier = Modifier.fillMaxWidth().height(72.dp).padding(bottom = 16.dp), contentAlignment = Alignment.BottomCenter) {
            if (isCorrect == null && selectedWords.isNotEmpty()) {
                Button(onClick = { 
                    if (isCorrect == null) {
                        onAnswer(selectedWords.joinToString(" "))
                    }
                }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                    Text("Check")
                }
            }
        }
    }
}

@Composable
fun WordChip(word: String, onClick: () -> Unit) {
    val cleanWord = word.replace("\u200b", "")
    Surface(
        modifier = Modifier.clickable { onClick() }.clip(RoundedCornerShape(8.dp)),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp
    ) {
        Text(cleanWord, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

@Composable
fun ListeningUI(ex: Exercise.Listening, tts: TextToSpeech?, isCorrect: Boolean?, onAnswer: (String) -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Listen and choose the translation", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { AudioHelper.playAudio(context, ex.audioText, tts, ex.audioText) },
                    modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(40.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Normal Speed", modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(32.dp))
                IconButton(
                    onClick = {
                        AudioHelper.playAudio(context, ex.audioText, tts, ex.audioText, speechRate = 0.5f)
                    },
                    modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(40.dp))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Slow Speed", modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            ex.options.forEach { option ->
                val btnColor = when {
                    isCorrect == true && option == ex.englishTranslation -> MaterialTheme.colorScheme.primary
                    isCorrect == false && option == ex.englishTranslation -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                Button(
                    onClick = { onAnswer(option) },
                    colors = ButtonDefaults.buttonColors(containerColor = btnColor, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(option, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun DrawingUI(ex: Exercise.Drawing, isCorrect: Boolean?, onAnswer: (Boolean) -> Unit) {
    val paths = remember { androidx.compose.runtime.mutableStateListOf<Path>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Draw this character", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(ex.hint, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.size(320.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = ex.letterToDraw,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = androidx.compose.ui.unit.TextUnit(200f, androidx.compose.ui.unit.TextUnitType.Sp),
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    
                    val drawColor = MaterialTheme.colorScheme.primary
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        currentPath = Path().apply { moveTo(offset.x, offset.y) }
                                    },
                                    onDragEnd = {
                                        currentPath?.let { paths.add(it) }
                                        currentPath = null
                                    }
                                ) { change, dragAmount ->
                                    change.consume()
                                    currentPath?.lineTo(change.position.x, change.position.y)
                                }
                            }
                    ) {
                        val stroke = Stroke(width = 30f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        paths.forEach { path ->
                            drawPath(path = path, color = drawColor, style = stroke) // Primary Purple
                        }
                        currentPath?.let { path ->
                            drawPath(path = path, color = drawColor, style = stroke)
                        }
                    }
                    
                    IconButton(
                        onClick = { paths.clear() },
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Clear Canvas", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
        
        Box(modifier = Modifier.fillMaxWidth().height(80.dp).padding(bottom = 16.dp), contentAlignment = Alignment.BottomCenter) {
            if (isCorrect == null) {
                Button(
                    onClick = { onAnswer(true) },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Verify Drawing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FlashcardUI(ex: Exercise.Flashcard, tts: TextToSpeech?, isCorrect: Boolean?, onAnswer: (Boolean) -> Unit) {
    val context = LocalContext.current
    val lang = UserManager.progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    var isFlipped by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (isVi) "Từ vựng mới" else "New Vocabulary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (isVi) "Nhấp vào thẻ để lật" else "Tap card to flip",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier
                    .size(310.dp, 310.dp)
                    .clickable { isFlipped = !isFlipped },
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedContent(
                        targetState = isFlipped,
                        transitionSpec = {
                            slideInVertically { height -> height } togetherWith slideOutVertically { height -> -height }
                        },
                        label = "CardFlip"
                    ) { flipped ->
                        if (!flipped) {
                            // Front
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = ex.hindi,
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        fontSize = androidx.compose.ui.unit.TextUnit(80f, androidx.compose.ui.unit.TextUnitType.Sp)
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = ex.transliteration,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            // Back
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = if (isVi) ex.vietnamese else ex.english,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Text(
                                    text = if (isVi) "English: ${ex.english}" else "Tiếng Việt: ${ex.vietnamese}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Audio Buttons (Top Right)
                    if (ex.audio.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            IconButton(
                                onClick = { AudioHelper.playAudio(context, ex.audio, tts, ex.hindi, speechRate = 0.5f) }
                            ) {
                                Text("🐢", style = MaterialTheme.typography.titleLarge)
                            }
                            IconButton(
                                onClick = { AudioHelper.playAudio(context, ex.audio, tts, ex.hindi) }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Listen",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (!isFlipped) {
                Button(
                    onClick = { isFlipped = true },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = if (isVi) "Xem nghĩa" else "Reveal Translation",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isVi) "Bạn thấy từ này thế nào?" else "How well did you know this?",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(
                                onClick = { onAnswer(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.width(96.dp).height(48.dp)
                            ) {
                                Text(if (isVi) "Khó" else "Hard", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Text(
                                text = if (isVi) "Ôn lại sớm" else "Review soon",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(
                                onClick = { onAnswer(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.width(96.dp).height(48.dp)
                            ) {
                                Text(if (isVi) "Vừa" else "Good", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Text(
                                text = if (isVi) "Bình thường" else "Normal",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(
                                onClick = { onAnswer(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.width(96.dp).height(48.dp)
                            ) {
                                Text(if (isVi) "Dễ" else "Easy", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Text(
                                text = if (isVi) "Đã thuộc" else "Mastered",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GrammarRuleUI(ex: Exercise.GrammarRule, onAnswer: (Boolean) -> Unit) {
    val lang = UserManager.progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(if (isVi) ex.title_vi else ex.title_en, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = if (isVi) ex.content_vi else ex.content_en,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onAnswer(true) },
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("I Understand", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SpeakingUI(ex: Exercise.Speaking, isCorrect: Boolean?, onAnswer: (Boolean) -> Unit) {
    val context = LocalContext.current
    var isListening by remember { mutableStateOf(false) }
    var recognizedText by remember { mutableStateOf("") }
    
    // We mock the permission launcher for now since a true runtime request needs Accompanist or native launcher
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            isListening = true
            // In a real app, instantiate SpeechRecognizer here and startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
            // For cinematic simulation, we'll pretend they spoke the phrase perfectly after 2 seconds
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                recognizedText = ex.hindiPhrase
                isListening = false
                onAnswer(true)
            }, 2000)
        }
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Speak this phrase", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(ex.hindiPhrase, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(ex.translation, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        Spacer(modifier = Modifier.height(64.dp))
        
        // Microphone Button
        val animScale by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (isListening) 1.5f else 1.0f,
            animationSpec = androidx.compose.animation.core.tween(500)
        )
        
        IconButton(
            onClick = {
                if (isCorrect == null && !isListening) {
                    launcher.launch(android.Manifest.permission.RECORD_AUDIO)
                }
            },
            modifier = Modifier
                .size(100.dp)
                .scale(animScale)
                .background(if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50.dp))
        ) {
            Icon(Icons.Default.Mic, contentDescription = "Speak", tint = if (isListening) Color.White else MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        if (isListening) {
            Text("Listening...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
        } else if (recognizedText.isNotEmpty()) {
            Text("You said: $recognizedText", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun CulturalDialogueUI(ex: Exercise.CulturalDialogue, isCorrect: Boolean?, onAnswer: (Boolean) -> Unit) {
    val lang = UserManager.progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    var selectedOption by remember { mutableStateOf<Exercise.DialogueOption?>(null) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Context", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(if (isVi) ex.context_vi else ex.context_en, style = MaterialTheme.typography.bodyLarge)
        
        Spacer(modifier = Modifier.height(24.dp))
        Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp), modifier = Modifier.align(Alignment.Start)) {
            Text(ex.npcLine, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (selectedOption != null) {
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp), modifier = Modifier.align(Alignment.End)) {
                Text(if (isVi) selectedOption!!.text_vi else selectedOption!!.text_en, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp), modifier = Modifier.align(Alignment.Start)) {
                Text(selectedOption!!.response, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
            }
        } else {
            ex.options.forEach { opt ->
                OutlinedButton(
                    onClick = { 
                        selectedOption = opt
                        onAnswer(opt.isCorrect) 
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                        Text(opt.text_en, style = MaterialTheme.typography.titleMedium)
                        Text(opt.text_vi, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun StoryModeUI(ex: Exercise.StoryMode, isCorrect: Boolean?, onAnswer: (Boolean) -> Unit) {
    val lang = UserManager.progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    
    var currentParagraphIndex by remember { androidx.compose.runtime.mutableIntStateOf(0) }
    val isStoryFinished = currentParagraphIndex >= ex.paragraphs.size
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(if (isVi) ex.title_vi else ex.title_en, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = com.example.hindilearn.theme.DeepSaffron)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (!isStoryFinished) {
            val p = ex.paragraphs[currentParagraphIndex]
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { currentParagraphIndex++ },
                contentAlignment = Alignment.Center
            ) {
                com.example.hindilearn.ui.gamified.GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(p.hindi, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = com.example.hindilearn.theme.TextDark, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(p.translation, style = MaterialTheme.typography.titleMedium, color = com.example.hindilearn.theme.TextDark.copy(alpha=0.7f), textAlign = TextAlign.Center)
                        
                        Spacer(modifier = Modifier.height(48.dp))
                        Text("Tap anywhere to continue", style = MaterialTheme.typography.labelMedium, color = com.example.hindilearn.theme.TextDark.copy(alpha=0.4f))
                    }
                }
            }
            
            // Progress indicators
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (i in ex.paragraphs.indices) {
                    Box(modifier = Modifier.weight(1f).height(4.dp).background(if (i <= currentParagraphIndex) com.example.hindilearn.theme.DeepSaffron else Color.LightGray.copy(alpha=0.5f), RoundedCornerShape(2.dp)))
                }
            }
        } else {
            androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(if (isVi) ex.question_vi else ex.question_en, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = com.example.hindilearn.theme.TextDark)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ex.options.forEach { opt ->
                        val color = if (isCorrect != null) {
                            if (opt.isCorrect) com.example.hindilearn.theme.SoftGreen else if (!opt.isCorrect && isCorrect == false) com.example.hindilearn.theme.SoftRed else com.example.hindilearn.theme.WarmIvory
                        } else com.example.hindilearn.theme.WarmIvory
                        
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(enabled = isCorrect == null) { onAnswer(opt.isCorrect) },
                            colors = CardDefaults.cardColors(containerColor = color),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(opt.text, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium, color = com.example.hindilearn.theme.TextDark)
                        }
                    }
                }
            }
        }
    }
}

fun getMockExercises(type: String): List<Exercise> {
    val lang = UserManager.progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    
    return when(type) {
        "VOWELS" -> listOf(
            Exercise.MultipleChoice(
                prompt = if (isVi) "Điều này có nghĩa là gì?" else "What does this mean?", 
                text = "अ", subtext = "a", 
                answer = if (isVi) "Nguyên âm đầu tiên (A)" else "First Vowel (A)", 
                options = if (isVi) listOf("Phụ âm K", "Nguyên âm đầu tiên (A)", "Dấu O") else listOf("Consonant K", "First Vowel (A)", "Matra O")
            ),
            Exercise.MultipleChoice(
                prompt = if (isVi) "Điều này có nghĩa là gì?" else "What does this mean?", 
                text = "आ", subtext = "aa", 
                answer = if (isVi) "A dài" else "Long A", 
                options = if (isVi) listOf("A dài", "I ngắn", "Phụ âm M") else listOf("Long A", "Short I", "Consonant M")
            )
        )
        "CONSONANTS" -> listOf(
            Exercise.MultipleChoice(
                prompt = if (isVi) "Điều này có nghĩa là gì?" else "What does this mean?", 
                text = "क", subtext = "ka", 
                answer = "K", 
                options = listOf("M", "P", "K")
            )
        )
        "VOCABULARY" -> listOf(
            Exercise.MultipleChoice(
                prompt = if (isVi) "Chọn bản dịch đúng" else "Select the correct translation", 
                text = "माता", subtext = "Maata", 
                answer = if (isVi) "Mẹ" else "Mother", 
                options = if (isVi) listOf("Cha", "Anh trai", "Mẹ") else listOf("Father", "Brother", "Mother")
            )
        )
        "SENTENCES" -> listOf(
            Exercise.SentenceBuilder(
                englishSentence = if (isVi) "Tôi ăn một quả táo." else "I eat an apple.", 
                hindiWords = listOf("मैं", "सेब", "खाता", "हूँ।"), 
                correctHindiSentence = "मैं सेब खाता हूँ।"
            )
        )
        "LISTENING" -> listOf(
            Exercise.Listening(
                audioText = "नमस्ते, आप कैसे हैं?", 
                englishTranslation = if (isVi) "Xin chào, bạn khỏe không?" else "Hello, how are you?", 
                options = if (isVi) listOf("Tạm biệt, hẹn gặp lại.", "Xin chào, bạn khỏe không?", "Tên bạn là gì?") else listOf("Goodbye, see you.", "Hello, how are you?", "What is your name?")
            )
        )
        "WRITING" -> listOf(
            Exercise.Drawing(
                letterToDraw = "क", 
                hint = if (isVi) "Vẽ chữ 'K'" else "Draw the letter 'K'"
            )
        )
        else -> listOf(
             Exercise.MultipleChoice("Error", "X", "x", "X", listOf("X"))
        )
    }
}

@Composable
fun TeachRuleUI(ex: Exercise.TeachRule, onAnswer: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(ex.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(ex.explanation, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)).padding(16.dp)) {
                        Text(ex.simpleRule, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onAnswer(true) },
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Got it", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ExamplesListUI(ex: Exercise.ExamplesList, tts: TextToSpeech?, onAnswer: (Boolean) -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text(ex.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(ex.examples) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.hindi, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(item.transliteration, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.translation, style = MaterialTheme.typography.bodyLarge)
                        }
                        IconButton(onClick = { AudioHelper.playAudio(context, item.hindi, tts, item.hindi) }) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
        
        Button(
            onClick = { onAnswer(true) },
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Continue", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VisualExplanationUI(ex: Exercise.VisualExplanation, onAnswer: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(ex.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            Text(ex.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(32.dp))
            
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val parsedColor = try { Color(android.graphics.Color.parseColor(ex.highlightedColorHex)) } catch(e: Exception) { MaterialTheme.colorScheme.error }
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = parsedColor.copy(alpha=0.2f))) {
                    Text(ex.highlightedWord, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = parsedColor, modifier = Modifier.padding(32.dp))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onAnswer(true) },
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("I Understand", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RevisionSummaryUI(ex: Exercise.RevisionSummary, onAnswer: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(ex.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ex.takeaways.forEach { takeaway ->
                        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 8.dp)) {
                            Icon(Icons.Default.Check, contentDescription = "Check", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(takeaway, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { onAnswer(true) },
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Finish Lesson", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun IntroductionUI(ex: Exercise.Introduction, onAnswer: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(ex.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            Text(ex.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ex.keyPoints.forEach { point ->
                        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 8.dp)) {
                            Icon(Icons.Default.Check, contentDescription = "Point", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(point, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onAnswer(true) },
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Start Lesson", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GrammarTableUI(ex: Exercise.GrammarTable, onAnswer: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(ex.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary.copy(alpha=0.1f)).padding(8.dp)) {
                        ex.headers.forEach { header ->
                            Text(header, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    HorizontalDivider()
                    ex.rows.forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            row.forEach { cell ->
                                Text(cell, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.1f))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onAnswer(true) },
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Got it", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VocabularyContextUI(ex: Exercise.VocabularyContext, tts: TextToSpeech?, onAnswer: (Boolean) -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(ex.word, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(ex.translation, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha=0.8f))
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(onClick = { AudioHelper.playAudio(context, ex.word, tts, ex.word) }, modifier = Modifier.background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha=0.1f), RoundedCornerShape(50))) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
            
            Text("In Context:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(ex.contextSentenceHindi, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(ex.contextSentenceTranslation, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(onClick = { AudioHelper.playAudio(context, ex.contextSentenceHindi, tts, ex.contextSentenceHindi) }) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Memory Trick 💡", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = com.example.hindilearn.theme.PremiumGold)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = com.example.hindilearn.theme.PremiumGold.copy(alpha=0.1f))
            ) {
                Text(ex.memoryTrick, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onAnswer(true) },
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Next", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DialogueModeUI(ex: Exercise.DialogueMode, tts: TextToSpeech?, onAnswer: (Boolean) -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text(ex.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = com.example.hindilearn.theme.TextDark)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(vertical = 16.dp)) {
            items(ex.lines) { line ->
                val isFirstSpeaker = line.speaker == ex.lines.firstOrNull()?.speaker
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = if (isFirstSpeaker) Arrangement.Start else Arrangement.End
                ) {
                    com.example.hindilearn.ui.gamified.GlassCard(
                        shape = RoundedCornerShape(
                            topStart = 20.dp, topEnd = 20.dp,
                            bottomStart = if (isFirstSpeaker) 4.dp else 20.dp,
                            bottomEnd = if (isFirstSpeaker) 20.dp else 4.dp
                        ),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp).background(if (isFirstSpeaker) com.example.hindilearn.theme.WarmIvory.copy(alpha=0.5f) else com.example.hindilearn.theme.DeepSaffron.copy(alpha=0.1f))) {
                            Text(line.speaker, style = MaterialTheme.typography.labelSmall, color = com.example.hindilearn.theme.TextDark.copy(alpha=0.6f))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(line.hindi, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = com.example.hindilearn.theme.TextDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(line.translation, style = MaterialTheme.typography.bodyMedium, color = com.example.hindilearn.theme.TextDark.copy(alpha=0.8f))
                            
                            IconButton(onClick = { AudioHelper.playAudio(context, line.hindi, tts, line.hindi) }, modifier = Modifier.align(Alignment.End).size(32.dp)) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = com.example.hindilearn.theme.DeepSaffron, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
        
        com.example.hindilearn.ui.gamified.PremiumButton(
            text = "Continue →",
            onClick = { onAnswer(true) }
        )
    }
}
