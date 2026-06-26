package com.example.hindilearn.ui.gamified

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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

sealed class ChallengeQuestion {
    data class MultipleChoice(
        val hindi: String,
        val correctTranslation: String,
        val options: List<String>,
        val isListening: Boolean = false
    ) : ChallengeQuestion()

    data class FillInBlank(
        val prompt: String,
        val translation: String,
        val options: List<String>,
        val answer: String
    ) : ChallengeQuestion()

    data class WordOrder(
        val english: String,
        val correctOrder: String,
        val wordsBank: List<String>
    ) : ChallengeQuestion()

    data class MatchPairs(
        val pairs: List<Pair<String, String>>
    ) : ChallengeQuestion()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyChallengeScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val progress = UserManager.progress
    val lang = progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    val haptic = LocalHapticFeedback.current

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    val questions = remember { mutableStateListOf<ChallengeQuestion>() }
    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedOptionIdx by remember { mutableStateOf<Int?>(null) }
    var isAnswerChecked by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var challengeFinished by remember { mutableStateOf(false) }
    var alreadyCompletedToday by remember { mutableStateOf(false) }
    
    // Word ordering states
    var selectedOrderWords by remember { mutableStateOf(listOf<String>()) }
    var availableOrderWords by remember { mutableStateOf(listOf<String>()) }

    // Matching pairs states
    var selectedLeftWord by remember { mutableStateOf<String?>(null) }
    var selectedRightWord by remember { mutableStateOf<String?>(null) }
    var completedMatches by remember { mutableStateOf(setOf<String>()) }

    val checkMatchingPair = { pairs: List<Pair<String, String>> ->
        val left = selectedLeftWord
        val right = selectedRightWord
        if (left != null && right != null) {
            val matched = pairs.any { it.first == left && it.second == right }
            if (matched) {
                completedMatches = completedMatches + left + right
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            } else {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            selectedLeftWord = null
            selectedRightWord = null
        }
    }

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

    // Load master vocab and generate 5 dynamic questions
    LaunchedEffect(Unit) {
        // Check if challenge already completed today
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val today = sdf.format(java.util.Date())
        if (UserManager.progress.lastChallengeDate == today) {
            alreadyCompletedToday = true
            return@LaunchedEffect
        }
        try {
            val inputStream: InputStream = context.assets.open("episodes/master_vocab.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            val jsonArray = JSONArray(jsonString)

            val vocabList = mutableListOf<Map<String, String>>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                vocabList.add(
                    mapOf(
                        "hindi" to obj.getString("hindi"),
                        "english" to obj.getString("english"),
                        "vietnamese" to obj.getString("vietnamese")
                    )
                )
            }

            val randomWords = vocabList.shuffled()
            val questionsList = mutableListOf<ChallengeQuestion>()

            // Q1: Vocabulary translation multiple choice
            val w1 = randomWords[0]
            val correct1 = if (isVi) w1["vietnamese"]!! else w1["english"]!!
            val opts1 = mutableListOf(correct1)
            var idx = 1
            while (opts1.size < 4 && idx < randomWords.size) {
                val dist = if (isVi) randomWords[idx]["vietnamese"]!! else randomWords[idx]["english"]!!
                if (dist != correct1 && dist.isNotEmpty()) {
                    opts1.add(dist)
                }
                idx++
            }
            questionsList.add(
                ChallengeQuestion.MultipleChoice(
                    hindi = w1["hindi"]!!,
                    correctTranslation = correct1,
                    options = opts1.shuffled(),
                    isListening = false
                )
            )

            // Q2: Listening test multiple choice
            val w2 = randomWords[1]
            val correct2 = if (isVi) w2["vietnamese"]!! else w2["english"]!!
            val opts2 = mutableListOf(correct2)
            while (opts2.size < 4 && idx < randomWords.size) {
                val dist = if (isVi) randomWords[idx]["vietnamese"]!! else randomWords[idx]["english"]!!
                if (dist != correct2 && dist.isNotEmpty()) {
                    opts2.add(dist)
                }
                idx++
            }
            questionsList.add(
                ChallengeQuestion.MultipleChoice(
                    hindi = w2["hindi"]!!,
                    correctTranslation = correct2,
                    options = opts2.shuffled(),
                    isListening = true
                )
            )

            // Q3: Fill in blank - DYNAMIC from vocab
            val w3 = randomWords.getOrNull(2) ?: randomWords[0]
            val fillPrompts = listOf(
                Triple("${w3["hindi"]} _____ (${w3["english"]} / ${w3["vietnamese"]})", "में", listOf("में", "पर", "से", "को")),
                Triple("मेज़ _____ किताब है (The book is on the table)", "पर", listOf("में", "पर", "से", "को")),
                Triple("वह स्कूल _____ आता है (He comes from school)", "से", listOf("में", "पर", "से", "को")),
                Triple("मुझे पानी _____ दो (Give me water)", "को", listOf("में", "पर", "से", "को"))
            ).random()
            questionsList.add(
                ChallengeQuestion.FillInBlank(
                    prompt = fillPrompts.first,
                    translation = fillPrompts.second,
                    options = fillPrompts.third.shuffled(),
                    answer = fillPrompts.second
                )
            )

            // Q4: Word Ordering - DYNAMIC
            val wordOrderSentences = listOf(
                Triple("I am fine. / Tôi khỏe.", "मैं ठीक हूँ", listOf("हूँ", "मैं", "ठीक")),
                Triple("She is good. / Cô ấy tốt.", "वह अच्छी है", listOf("वह", "अच्छी", "है")),
                Triple("Water is sweet. / Nước ngọt.", "पानी मीठा है", listOf("पानी", "मीठा", "है")),
                Triple("He is a student. / Anh ấy là học sinh.", "वह एक छात्र है", listOf("वह", "एक", "छात्र", "है")),
                Triple("My name is Raj. / Tên tôi là Raj.", "मेरा नाम राज है", listOf("मेरा", "नाम", "राज", "है"))
            ).random()
            questionsList.add(
                ChallengeQuestion.WordOrder(
                    english = wordOrderSentences.first,
                    correctOrder = wordOrderSentences.second,
                    wordsBank = wordOrderSentences.third.shuffled()
                )
            )

            // Q5: Match Pairs (4 pairs)
            val pairsPool = randomWords.take(4).map {
                Pair(it["hindi"]!!, if (isVi) it["vietnamese"]!! else it["english"]!!)
            }
            questionsList.add(ChallengeQuestion.MatchPairs(pairsPool))

            questions.addAll(questionsList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Initialize available words for ordering question
    LaunchedEffect(currentQuestionIdx, questions.size) {
        if (currentQuestionIdx < questions.size) {
            val q = questions[currentQuestionIdx]
            if (q is ChallengeQuestion.WordOrder) {
                availableOrderWords = q.wordsBank
                selectedOrderWords = emptyList()
            } else if (q is ChallengeQuestion.MatchPairs) {
                completedMatches = emptySet()
                selectedLeftWord = null
                selectedRightWord = null
            }
            selectedOptionIdx = null
            isAnswerChecked = false
            isCorrect = null
        }
    }

    PremiumBackground {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        if (alreadyCompletedToday) {
                            Text(if (isVi) "Đã hoàn thành!" else "Challenge Completed!", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        } else if (!challengeFinished && questions.isNotEmpty()) {
                            val progressPercent = (currentQuestionIdx).toFloat() / questions.size.toFloat()
                            AnimatedProgressBar(
                                progress = progressPercent,
                                modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                                color = DeepSaffron
                            )
                        } else {
                            Text(if (isVi) "Hoàn thành!" else "Challenge Completed!", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (alreadyCompletedToday) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Check, contentDescription = "Done", tint = SoftGreen, modifier = Modifier.size(100.dp))
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = if (isVi) "Bạn đã hoàn thành thử thách hôm nay!" else "You've already completed today's challenge!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (isVi) "Hãy quay lại vào ngày mai để nhận thử thách mới." else "Come back tomorrow for a new challenge.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.7f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            PremiumButton(text = if (isVi) "Quay lại" else "Go Back", onClick = onBack)
                        }
                    }
                } else if (!challengeFinished && questions.isNotEmpty()) {
                    val currentQuestion = questions[currentQuestionIdx]

                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                        when (currentQuestion) {
                            is ChallengeQuestion.MultipleChoice -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (currentQuestion.isListening) 
                                                   (if (isVi) "Nghe và chọn nghĩa đúng:" else "Listen and choose correct meaning:")
                                               else 
                                                   (if (isVi) "Chọn nghĩa của từ sau:" else "Choose the meaning of the word:"),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(32.dp))

                                    if (currentQuestion.isListening) {
                                        IconButton(
                                            onClick = { AudioHelper.playAudio(context, "", tts, currentQuestion.hindi) },
                                            modifier = Modifier
                                                .size(100.dp)
                                                .background(DeepSaffron.copy(alpha = 0.15f), CircleShape)
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                contentDescription = "Play Audio",
                                                tint = DeepSaffron,
                                                modifier = Modifier.size(48.dp)
                                            )
                                        }
                                        LaunchedEffect(Unit) {
                                            // Auto-play audio on entry
                                            AudioHelper.playAudio(context, "", tts, currentQuestion.hindi)
                                        }
                                    } else {
                                        Card(
                                            shape = RoundedCornerShape(24.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(32.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(currentQuestion.hindi, fontSize = 52.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                                                Spacer(modifier = Modifier.height(16.dp))
                                                IconButton(
                                                    onClick = { AudioHelper.playAudio(context, "", tts, currentQuestion.hindi) }
                                                ) {
                                                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play", tint = RoyalBlue)
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(48.dp))

                                    currentQuestion.options.forEachIndexed { idx, option ->
                                        val isSelected = selectedOptionIdx == idx
                                        val btnColor = when {
                                            isAnswerChecked && option == currentQuestion.correctTranslation -> SoftGreen
                                            isAnswerChecked && isSelected && option != currentQuestion.correctTranslation -> SoftRed
                                            isSelected -> DeepSaffron.copy(alpha = 0.2f)
                                            else -> Color.White.copy(alpha = 0.5f)
                                        }
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp)
                                                .clickable(enabled = !isAnswerChecked) { selectedOptionIdx = idx },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = btnColor)
                                        ) {
                                            Text(
                                                text = option,
                                                modifier = Modifier.padding(16.dp),
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }

                            is ChallengeQuestion.FillInBlank -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (isVi) "Điền vào chỗ trống:" else "Fill in the blank:",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(32.dp))
                                    Text(
                                        text = currentQuestion.prompt,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        color = DeepSaffron,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(48.dp))

                                    currentQuestion.options.forEachIndexed { idx, option ->
                                        val isSelected = selectedOptionIdx == idx
                                        val btnColor = when {
                                            isAnswerChecked && option == currentQuestion.answer -> SoftGreen
                                            isAnswerChecked && isSelected && option != currentQuestion.answer -> SoftRed
                                            isSelected -> DeepSaffron.copy(alpha = 0.2f)
                                            else -> Color.White.copy(alpha = 0.5f)
                                        }
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp)
                                                .clickable(enabled = !isAnswerChecked) { selectedOptionIdx = idx },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = btnColor)
                                        ) {
                                            Text(
                                                text = option,
                                                modifier = Modifier.padding(16.dp),
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }

                            is ChallengeQuestion.WordOrder -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (isVi) "Sắp xếp thứ tự các từ:" else "Arrange the words in order:",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = currentQuestion.english,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = RoyalBlue,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(32.dp))

                                    // Selection Area
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color.White.copy(alpha = 0.4f))
                                            .padding(12.dp)
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            selectedOrderWords.forEach { word ->
                                                WordChip(word) {
                                                    if (!isAnswerChecked) {
                                                        selectedOrderWords = selectedOrderWords - word
                                                        availableOrderWords = availableOrderWords + word
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(32.dp))

                                    // Available Word Bank
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        availableOrderWords.forEach { word ->
                                            WordChip(word) {
                                                if (!isAnswerChecked) {
                                                    availableOrderWords = availableOrderWords - word
                                                    selectedOrderWords = selectedOrderWords + word
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            is ChallengeQuestion.MatchPairs -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (isVi) "Ghép cặp từ tương ứng:" else "Match the pairs:",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val lefts = currentQuestion.pairs.map { it.first }.shuffled(random = java.util.Random(42))
                                    val rights = currentQuestion.pairs.map { it.second }.shuffled(random = java.util.Random(24))

                                    Row(
                                        modifier = Modifier.fillMaxWidth().weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        // Left Side (Hindi)
                                        Column(modifier = Modifier.weight(1f)) {
                                            lefts.forEach { word ->
                                                val isCompleted = completedMatches.contains(word)
                                                val isSelected = selectedLeftWord == word
                                                val cardColor = when {
                                                    isCompleted -> SoftGreen
                                                    isSelected -> DeepSaffron.copy(alpha = 0.2f)
                                                    else -> Color.White.copy(alpha = 0.5f)
                                                }
                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 6.dp)
                                                        .clickable(enabled = !isCompleted && !isAnswerChecked) {
                                                            selectedLeftWord = word
                                                            checkMatchingPair(currentQuestion.pairs)
                                                        },
                                                    shape = RoundedCornerShape(12.dp),
                                                    colors = CardDefaults.cardColors(containerColor = cardColor)
                                                ) {
                                                    Text(
                                                        text = word,
                                                        modifier = Modifier.padding(16.dp),
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                        }

                                        // Right Side (Meaning)
                                        Column(modifier = Modifier.weight(1f)) {
                                            rights.forEach { meaning ->
                                                val isCompleted = completedMatches.contains(meaning)
                                                val isSelected = selectedRightWord == meaning
                                                val cardColor = when {
                                                    isCompleted -> SoftGreen
                                                    isSelected -> DeepSaffron.copy(alpha = 0.2f)
                                                    else -> Color.White.copy(alpha = 0.5f)
                                                }
                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 6.dp)
                                                        .clickable(enabled = !isCompleted && !isAnswerChecked) {
                                                            selectedRightWord = meaning
                                                            checkMatchingPair(currentQuestion.pairs)
                                                        },
                                                    shape = RoundedCornerShape(12.dp),
                                                    colors = CardDefaults.cardColors(containerColor = cardColor)
                                                ) {
                                                    Text(
                                                        text = meaning,
                                                        modifier = Modifier.padding(16.dp),
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Check / Continue Button Area
                    Spacer(modifier = Modifier.height(16.dp))
                    if (!isAnswerChecked) {
                        val isEnable = when (currentQuestion) {
                            is ChallengeQuestion.MultipleChoice -> selectedOptionIdx != null
                            is ChallengeQuestion.FillInBlank -> selectedOptionIdx != null
                            is ChallengeQuestion.WordOrder -> selectedOrderWords.isNotEmpty()
                            is ChallengeQuestion.MatchPairs -> false // Automatic in checking matches
                        }
                        
                        if (currentQuestion !is ChallengeQuestion.MatchPairs) {
                            PremiumButton(
                                text = "Check Answer",
                                onClick = {
                                    isAnswerChecked = true
                                    when (currentQuestion) {
                                        is ChallengeQuestion.MultipleChoice -> {
                                            val correctAns = currentQuestion.correctTranslation
                                            val picked = currentQuestion.options[selectedOptionIdx!!]
                                            isCorrect = (picked == correctAns)
                                        }
                                        is ChallengeQuestion.FillInBlank -> {
                                            val picked = currentQuestion.options[selectedOptionIdx!!]
                                            isCorrect = (picked == currentQuestion.answer)
                                        }
                                        is ChallengeQuestion.WordOrder -> {
                                            val constructed = selectedOrderWords.joinToString(" ")
                                            isCorrect = (constructed == currentQuestion.correctOrder)
                                        }
                                        else -> {}
                                    }
                                    if (isCorrect == true) {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    } else {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                },
                                enabled = isEnable
                            )
                        } else {
                            // In MatchPairs, check if all 4 matched
                            val allMatched = completedMatches.size == 8
                            if (allMatched) {
                                PremiumButton(
                                    text = "Continue",
                                    onClick = {
                                        isCorrect = true
                                        isAnswerChecked = true
                                    }
                                )
                            } else {
                                PremiumButton(
                                    text = "Match All Pairs",
                                    onClick = {},
                                    enabled = false
                                )
                            }
                        }
                    }

                    // Slide up green/red validation banner
                    AnimatedVisibility(
                        visible = isAnswerChecked,
                        enter = androidx.compose.animation.slideInVertically(initialOffsetY = { it }),
                        exit = androidx.compose.animation.slideOutVertically(targetOffsetY = { it })
                    ) {
                        val feedbackColor = if (isCorrect == true) SoftGreen else SoftRed
                        val feedbackMsg = if (isCorrect == true) "Correct!" else "Oops! Keep learning."
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .background(feedbackColor)
                                .padding(24.dp)
                        ) {
                            Column {
                                Text(
                                    feedbackMsg,
                                    color = if (isCorrect == true) TextDark else Color.White,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                PremiumButton(
                                    text = "Continue \u2192",
                                    onClick = {
                                        if (currentQuestionIdx + 1 < questions.size) {
                                            currentQuestionIdx++
                                        } else {
                                            challengeFinished = true
                                        }
                                    },
                                    color = if (isCorrect == true) PremiumGold else DeepSaffron
                                )
                            }
                        }
                    }
                } else if (challengeFinished) {
                    // Completion celebrate view
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Gold Medal",
                            tint = PremiumGold,
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = if (isVi) "Thử thách hoàn thành!" else "Daily Challenge Cleared!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isVi) "Bạn nhận được các phần thưởng củng cố hôm nay." 
                                   else "You have successfully claimed today's rewards.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(24.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("+20 XP", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = PremiumGold)
                                    Text("Experience", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                                VerticalDivider(modifier = Modifier.width(1.dp).height(40.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("+10 Coins", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = DeepSaffron)
                                    Text("Academy Coins", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(48.dp))

                        PremiumButton(
                            text = if (isVi) "Nhận phần thưởng" else "Claim Rewards",
                            onClick = {
                                UserManager.markChallengeCompleted()
                                UserManager.addXp(20)
                                UserManager.unlockAchievement("ach_daily_warrior")
                                onBack()
                            }
                        )
                    }
                }
            }
        }
    }
}
