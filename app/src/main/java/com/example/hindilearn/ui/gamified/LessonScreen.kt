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
import com.example.hindilearn.ui.gamified.PremiumBackground
import com.example.hindilearn.ui.gamified.ConfettiOverlay
import com.example.hindilearn.ui.gamified.PremiumButton
import com.example.hindilearn.ui.gamified.GlassCard
import com.example.hindilearn.ui.gamified.AnimatedProgressBar
import java.util.Locale

sealed class Exercise {
    data class MultipleChoice(val prompt: String, val text: String, val subtext: String, val answer: String, val options: List<String>) : Exercise()
    data class SentenceBuilder(val englishSentence: String, val hindiWords: List<String>, val correctHindiSentence: String) : Exercise()
    data class Listening(val audioText: String, val englishTranslation: String, val options: List<String>) : Exercise()
    data class Drawing(val letterToDraw: String, val hint: String) : Exercise()
    data class Flashcard(val hindi: String, val transliteration: String, val english: String, val vietnamese: String, val audio: String) : Exercise()
    data class GrammarRule(val title_en: String, val title_vi: String, val content_en: String, val content_vi: String) : Exercise()
    data class Introduction(val title: String, val description: String, val keyPoints: List<String>) : Exercise()
    data class GrammarTable(val title: String, val headers: List<String>, val rows: List<List<String>>) : Exercise()
    data class VocabularyContext(val word: String, val translation: String, val contextSentenceHindi: String, val contextSentenceTranslation: String, val memoryTrick: String) : Exercise()
    data class DialogueMode(val title: String, val lines: List<DialogueLine>) : Exercise()
    data class DialogueLine(val speaker: String, val hindi: String, val translation: String)
    data class TeachRule(val title: String, val explanation: String, val simpleRule: String) : Exercise()
    data class ExamplesList(val title: String, val examples: List<ExampleItem>) : Exercise()
    data class ExampleItem(val hindi: String, val transliteration: String, val translation: String)
    data class VisualExplanation(val title: String, val description: String, val highlightedWord: String, val highlightedColorHex: String) : Exercise()
    data class RevisionSummary(val title: String, val takeaways: List<String>) : Exercise()
    data class Speaking(val hindiPhrase: String, val translation: String) : Exercise()
    data class MatchPairs(val instruction: String, val pairs: List<Pair<String, String>>) : Exercise()
    data class CulturalTip(val title_en: String, val title_vi: String, val content_en: String, val content_vi: String) : Exercise()
    data class StoryMode(val title_en: String, val title_vi: String, val paragraphs: List<StoryParagraph>, val question_en: String, val question_vi: String, val options: List<StoryOption>) : Exercise()
    
    data class StoryParagraph(val hindi: String, val translation: String)
    data class StoryOption(val text: String, val isCorrect: Boolean)
    
    data class CulturalDialogue(
        val context_en: String, val context_vi: String, val npcLine: String, 
        val options: List<DialogueOption>
    ) : Exercise()
    data class DialogueOption(val text_en: String, val text_vi: String, val isCorrect: Boolean, val response: String)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    nodeId: String,
    nodeType: String,
    onFinish: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    val exerciseQueue = remember { androidx.compose.runtime.mutableStateListOf<Exercise>() }
    var totalExercisesCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(nodeId) {
        val exercises = com.example.hindilearn.data.CurriculumManager.getExercises(context, nodeId, nodeType)
        exerciseQueue.clear()
        exerciseQueue.addAll(exercises)
        totalExercisesCount = exercises.size
        isLoading = false
    }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var lessonFinished by remember { mutableStateOf(false) }

    val advanceQueue = {
        if (exerciseQueue.isNotEmpty()) {
            exerciseQueue.removeAt(0)
        }
        if (exerciseQueue.isEmpty()) {
            lessonFinished = true
        }
    }

    val haptic = LocalHapticFeedback.current

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val isEnglishCourse = com.example.hindilearn.data.UserManager.progress.selectedCourse == "ENGLISH"
    DisposableEffect(context) {
        var ttsInstance: TextToSpeech? = null
        ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance?.language = if (isEnglishCourse) Locale.US else Locale.forLanguageTag("hi-IN")
            }
        }
        tts = ttsInstance
        onDispose {
            ttsInstance?.stop()
            ttsInstance?.shutdown()
        }
    }

    var showWelcome by remember { mutableStateOf(true) }

    PremiumBackground {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        val p = if (totalExercisesCount == 0) 0f else (totalExercisesCount - exerciseQueue.size + (if (isCorrect != null) 1 else 0)).toFloat() / totalExercisesCount.toFloat()
                        AnimatedProgressBar(
                            progress = p,
                            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                            color = com.example.hindilearn.theme.DeepSaffron
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onClose) { Icon(Icons.Default.Close, contentDescription = "Close", tint = com.example.hindilearn.theme.TextDark) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (showWelcome) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                        Surface(shape = CircleShape, color = com.example.hindilearn.theme.DeepSaffron.copy(alpha=0.1f), modifier = Modifier.size(120.dp).padding(8.dp), shadowElevation = 0.dp) {
                            Icon(Icons.Default.Face, contentDescription = "Vietana Guide", modifier = Modifier.padding(24.dp), tint = com.example.hindilearn.theme.DeepSaffron)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        val isVi = UserManager.progress.selectedLanguage == "VI"
                        Text(
                            if (isVi) "Chào mừng trở lại!" else "Welcome back!",
                            style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (isVi) "Hãy cùng chinh phục bài học này!" else "Let's master this unit together.",
                            style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.8f)
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                        PremiumButton(
                            text = if (isVi) "Bắt đầu học" else "Start Learning",
                            onClick = { showWelcome = false }
                        )
                    }
                }
            } else if (lessonFinished) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    ConfettiOverlay()
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        val episode = com.example.hindilearn.data.GamifiedCurriculum.getEpisodeById(nodeId)
                        val titleMsg = if (episode != null) "Great job on ${episode.title}!" else "You mastered this unit."
                        val confMsg = episode?.confidenceMessage ?: "I am so proud of your progress."
                        
                        Text("🎉 Amazing!", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(titleMsg, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text("Today you can:", style = MaterialTheme.typography.bodyLarge, color = com.example.hindilearn.theme.DeepSaffron, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                episode?.achievements?.forEach { achievement ->
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 16.dp)) {
                                        Icon(Icons.Default.Check, contentDescription = "Check", tint = com.example.hindilearn.theme.SoftGreen, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(achievement, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                GlassCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                                        Surface(shape = CircleShape, color = com.example.hindilearn.theme.WarmIvory, modifier = Modifier.size(48.dp)) {
                                            Icon(Icons.Default.Face, contentDescription = "Rahul", modifier = Modifier.padding(12.dp), tint = com.example.hindilearn.theme.DeepSaffron)
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            val isViFinish = UserManager.progress.selectedLanguage == "VI"
                                            Text(
                                                if (isViFinish) "Rahul nói:" else "Rahul says:",
                                                style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.6f)
                                            )
                                            Text("“$confMsg”", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.WorkspacePremium, contentDescription = "XP", tint = com.example.hindilearn.theme.PremiumGold, modifier = Modifier.size(32.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("+15 XP", style = MaterialTheme.typography.headlineMedium, color = com.example.hindilearn.theme.PremiumGold, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(48.dp))
                        PremiumButton(
                            text = "Continue \u2192",
                            onClick = {
                                UserManager.addXp(15)
                                UserManager.unlockNextNode(nodeId)
                                onFinish()
                            }
                        )
                    }
                }
            } else if (exerciseQueue.isNotEmpty()) {
                val currentEx = exerciseQueue.first()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                
                AnimatedContent(
                    targetState = currentEx,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = spring(stiffness = Spring.StiffnessLow)) { width -> width } togetherWith slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessLow)) { width -> -width }
                    },
                    label = "ExerciseTransition",
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) { targetEx ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (targetEx) {
                        is Exercise.MultipleChoice -> {
                            val isWriting = nodeId.contains("write")
                            if (isWriting) {
                                WritingUI(targetEx, isCorrect) { ans ->
                                    if (isCorrect == null) {
                                        val cleanUser = ans.replace(Regex("[।.,?!\\s\\u200b]"), "").lowercase().trim()
                                        val cleanCorrect = targetEx.text.replace(Regex("[।.,?!\\s\\u200b]"), "").lowercase().trim()
                                        val isRight = (cleanUser == cleanCorrect)
                                        isCorrect = isRight
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        com.example.hindilearn.data.SrsManager.addWordIfNotExists(targetEx.text)
                                        com.example.hindilearn.data.SrsManager.processReview(targetEx.text, isRight)
                                        if (!isRight) {
                                            UserManager.loseHeart()
                                            UserManager.incrementMistake(targetEx.text)
                                            exerciseQueue.add(targetEx)
                                        }
                                    }
                                }
                            } else {
                                MultipleChoiceUI(targetEx, tts, isCorrect) { ans ->
                                    if (isCorrect == null) {
                                        val isRight = (ans == targetEx.answer)
                                        isCorrect = isRight
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        com.example.hindilearn.data.SrsManager.addWordIfNotExists(targetEx.text)
                                        com.example.hindilearn.data.SrsManager.processReview(targetEx.text, isRight)
                                        if (!isRight) {
                                            UserManager.loseHeart()
                                            UserManager.incrementMistake(targetEx.text)
                                            exerciseQueue.add(targetEx)
                                        }
                                    }
                                }
                            }
                        }
                        is Exercise.SentenceBuilder -> SentenceBuilderUI(targetEx, isCorrect) { constructed ->
                            if (isCorrect == null) {
                                val cleanUser = constructed.replace(Regex("[।.,?!\\s\\u200b]"), "").lowercase().trim()
                                val cleanCorrect = targetEx.correctHindiSentence.replace(Regex("[।.,?!\\s\\u200b]"), "").lowercase().trim()
                                val isRight = (cleanUser == cleanCorrect)
                                isCorrect = isRight
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                com.example.hindilearn.data.SrsManager.addWordIfNotExists(targetEx.correctHindiSentence)
                                com.example.hindilearn.data.SrsManager.processReview(targetEx.correctHindiSentence, isRight)
                                if (!isRight) {
                                    UserManager.loseHeart()
                                    UserManager.incrementMistake(targetEx.englishSentence)
                                    exerciseQueue.add(targetEx)
                                }
                            }
                        }
                        is Exercise.Listening -> ListeningUI(targetEx, tts, isCorrect) { ans ->
                            if (isCorrect == null) {
                                val isRight = (ans == targetEx.englishTranslation)
                                isCorrect = isRight
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                com.example.hindilearn.data.SrsManager.addWordIfNotExists(targetEx.audioText)
                                com.example.hindilearn.data.SrsManager.processReview(targetEx.audioText, isRight)
                                if (!isRight) {
                                    UserManager.loseHeart()
                                    UserManager.incrementMistake(targetEx.audioText)
                                    exerciseQueue.add(targetEx)
                                }
                            }
                        }
                        is Exercise.Drawing -> DrawingUI(targetEx, isCorrect) { ans ->
                            if (isCorrect == null) {
                                isCorrect = true // Mocking validation for now
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                        is Exercise.Flashcard -> FlashcardUI(targetEx, tts, isCorrect) { ans ->
                            if (isCorrect == null) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                com.example.hindilearn.data.SrsManager.addWordIfNotExists(targetEx.hindi)
                                advanceQueue()
                            }
                        }
                        is Exercise.GrammarRule -> GrammarRuleUI(targetEx) { ans ->
                            if (isCorrect == null) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                advanceQueue()
                            }
                        }
                        is Exercise.Speaking -> SpeakingUI(targetEx, isCorrect) { ans ->
                            if (isCorrect == null) {
                                isCorrect = ans
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                com.example.hindilearn.data.SrsManager.addWordIfNotExists(targetEx.hindiPhrase)
                                com.example.hindilearn.data.SrsManager.processReview(targetEx.hindiPhrase, ans)
                                if (!ans) {
                                    UserManager.loseHeart()
                                    UserManager.incrementMistake(targetEx.hindiPhrase)
                                    exerciseQueue.add(targetEx)
                                }
                            }
                        }
                        is Exercise.CulturalDialogue -> CulturalDialogueUI(targetEx, isCorrect) { ans ->
                            if (isCorrect == null) {
                                isCorrect = ans
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (!ans) {
                                    UserManager.loseHeart()
                                    UserManager.incrementMistake(targetEx.context_en)
                                    exerciseQueue.add(targetEx)
                                }
                            }
                        }
                        is Exercise.TeachRule -> TeachRuleUI(targetEx) {
                            if (isCorrect == null) advanceQueue()
                        }
                        is Exercise.Introduction -> IntroductionUI(targetEx) {
                            if (isCorrect == null) advanceQueue()
                        }
                        is Exercise.GrammarTable -> GrammarTableUI(targetEx) {
                            if (isCorrect == null) advanceQueue()
                        }
                        is Exercise.VocabularyContext -> VocabularyContextUI(targetEx, tts) {
                            if (isCorrect == null) advanceQueue()
                        }
                        is Exercise.DialogueMode -> DialogueModeUI(targetEx, tts) {
                            if (isCorrect == null) advanceQueue()
                        }
                        is Exercise.ExamplesList -> ExamplesListUI(targetEx, tts) {
                            if (isCorrect == null) advanceQueue()
                        }
                        is Exercise.VisualExplanation -> VisualExplanationUI(targetEx) {
                            if (isCorrect == null) advanceQueue()
                        }
                        is Exercise.RevisionSummary -> RevisionSummaryUI(targetEx) {
                            if (isCorrect == null) advanceQueue()
                        }
                        is Exercise.CulturalTip -> {
                            val isVi = UserManager.progress.selectedLanguage == "VI"
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text(if (isVi) targetEx.title_vi else targetEx.title_en, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(if (isVi) targetEx.content_vi else targetEx.content_en, style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(32.dp))
                                PremiumButton(
                                    text = "Continue",
                                    onClick = {
                                        if (isCorrect == null) advanceQueue()
                                    }
                                )
                            }
                        }
                        is Exercise.StoryMode -> StoryModeUI(targetEx, isCorrect) { ans ->
                            if (isCorrect == null) {
                                isCorrect = ans
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (!ans) {
                                    UserManager.loseHeart()
                                    UserManager.incrementMistake(targetEx.title_en)
                                    exerciseQueue.add(targetEx)
                                }
                            }
                        }
                        is Exercise.MatchPairs -> MatchPairsUI(targetEx, isCorrect) { ans ->
                            if (isCorrect == null) {
                                isCorrect = ans
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (!ans) {
                                    UserManager.loseHeart()
                                    UserManager.incrementMistake(targetEx.instruction)
                                    exerciseQueue.add(targetEx)
                                }
                            }
                        }
                        else -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text("New Exercise Available!", color = Color.White, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(16.dp))
                                PremiumButton(
                                    text = "Continue",
                                    onClick = {
                                        if (isCorrect == null) {
                                            isCorrect = true
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    }
                                )
                            }
                        }
                        }
                    }
                }
                
                AnimatedVisibility(
                    visible = isCorrect != null,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    val isViMsg = UserManager.progress.selectedLanguage == "VI"
                    val msg = if (isCorrect == true) {
                        if (isViMsg) "Chính xác! 🎉" else "Correct! 🎉"
                    } else {
                        if (isViMsg) "Thử lại nhé! 💪" else "Oops! Let's try that again. 💪"
                    }
                    val color = if (isCorrect == true) com.example.hindilearn.theme.SoftGreen else com.example.hindilearn.theme.SoftRed
                    val textColor = if (isCorrect == true) com.example.hindilearn.theme.TextDark else Color.White
                    
                    Box(modifier = Modifier.fillMaxWidth()) {
                        com.example.hindilearn.ui.gamified.GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = if (isCorrect == false) 32.dp else 0.dp), // Space for Grandma
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        ) {
                            Column(modifier = Modifier.background(color).padding(24.dp).padding(top = if (isCorrect == false) 16.dp else 0.dp)) {
                                Text(msg, color = textColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                                
                                com.example.hindilearn.ui.gamified.PremiumButton(
                                    text = "Continue \u2192",
                                    onClick = {
                                        isCorrect = null
                                        if (exerciseQueue.isNotEmpty()) {
                                            exerciseQueue.removeAt(0)
                                        }
                                        if (exerciseQueue.isEmpty()) {
                                            lessonFinished = true
                                        }
                                    },
                                    color = if (isCorrect == true) com.example.hindilearn.theme.PremiumGold else com.example.hindilearn.theme.DeepSaffron
                                )
                            }
                        }
                        
                        if (isCorrect == false) {
                            // Grandma overlay
                            Surface(
                                shape = CircleShape,
                                color = com.example.hindilearn.theme.WarmIvory,
                                modifier = Modifier
                                    .size(64.dp)
                                    .align(Alignment.TopStart)
                                    .offset(x = 24.dp, y = (-16).dp),
                                shadowElevation = 4.dp
                            ) {
                                Icon(Icons.Default.Face, contentDescription = "Grandma", modifier = Modifier.padding(12.dp), tint = com.example.hindilearn.theme.DeepSaffron)
                            }
                        }
                    }
                }
            }
        }
    }
}
}
