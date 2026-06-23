package com.example.hindilearn.data

import android.content.Context
import com.example.hindilearn.ui.gamified.Exercise
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

object CurriculumManager {

    private val exercisesCache = mutableMapOf<String, List<Exercise>>()

    suspend fun getExercises(context: Context, nodeId: String, type: String): List<Exercise> = withContext(Dispatchers.IO) {
        val cacheKey = "${nodeId}_${UserManager.progress.selectedLanguage ?: "EN"}"
        if (exercisesCache.containsKey(cacheKey)) {
            return@withContext exercisesCache[cacheKey]!!
        }

        val exercises = mutableListOf<Exercise>()
        val lang = UserManager.progress.selectedLanguage ?: "EN"
        val isVi = lang == "VI"

        try {
            val fileName = "episodes/${nodeId}.json"
            val inputStream: InputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            
            val array = try {
                val obj = org.json.JSONObject(jsonString)
                obj.getJSONArray("nodes")
            } catch (e: Exception) {
                org.json.JSONArray(jsonString)
            }

            if (array != null) {
                for (i in 0 until array.length()) {
                    val exerciseObj = array.getJSONObject(i)
                    val typeStr = exerciseObj.getString("type")
                    when (typeStr) {
                        "STORY" -> {
                            exercises.add(
                                Exercise.Introduction(
                                    title = "Story Time",
                                    description = exerciseObj.optString("text"),
                                    keyPoints = emptyList()
                                )
                            )
                        }
                        "DIALOGUE" -> {
                            exercises.add(
                                Exercise.DialogueMode(
                                    title = "Conversation",
                                    lines = listOf(
                                        Exercise.DialogueLine(
                                            speaker = exerciseObj.optString("speaker"),
                                            hindi = exerciseObj.optString("hindi", exerciseObj.optString("text")),
                                            translation = exerciseObj.optString("text")
                                        )
                                    )
                                )
                            )
                        }
                        "LESSON" -> {
                            exercises.add(
                                Exercise.TeachRule(
                                    title = exerciseObj.optString("title"),
                                    explanation = exerciseObj.optString("content"),
                                    simpleRule = ""
                                )
                            )
                        }
                        "QUIZ" -> {
                            exercises.add(
                                Exercise.MultipleChoice(
                                    prompt = exerciseObj.optString("question"),
                                    text = "",
                                    subtext = "",
                                    answer = exerciseObj.optString("answer"),
                                    options = exerciseObj.optJSONArray("options")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: emptyList()
                                )
                            )
                        }
                        "MultipleChoice", "MULTIPLE_CHOICE" -> {
                            val prompt = if (isVi) exerciseObj.optString("prompt_vi", exerciseObj.optString("prompt", "Chọn bản dịch đúng:")) else exerciseObj.optString("prompt_en", exerciseObj.optString("prompt", "Choose the correct translation:"))
                            val text = exerciseObj.optString("text")
                            val subtext = exerciseObj.optString("subtext")
                            
                            val optArray = (if (isVi) exerciseObj.optJSONArray("options_vi") ?: exerciseObj.optJSONArray("options") else exerciseObj.optJSONArray("options_en") ?: exerciseObj.optJSONArray("options"))
                            val options = mutableListOf<String>()
                            var answer = if (isVi) exerciseObj.optString("answer_vi", exerciseObj.optString("answer")) else exerciseObj.optString("answer_en", exerciseObj.optString("answer"))
                            
                            if (optArray != null) {
                                for (idx in 0 until optArray.length()) {
                                    val item = optArray.opt(idx)
                                    if (item is org.json.JSONObject) {
                                        val optText = item.optString("text")
                                        options.add(optText)
                                        if (item.optBoolean("isCorrect") && answer.isEmpty()) {
                                            answer = optText
                                        }
                                    } else if (item != null) {
                                        options.add(item.toString())
                                    }
                                }
                            }
                            
                            exercises.add(
                                Exercise.MultipleChoice(
                                    prompt = prompt,
                                    text = text,
                                    subtext = subtext,
                                    answer = answer,
                                    options = options
                                )
                            )
                        }
                        "Listening", "LISTENING" -> {
                            exercises.add(
                                Exercise.Listening(
                                    audioText = exerciseObj.optString("audioText", exerciseObj.optString("audio")),
                                    englishTranslation = if (isVi) exerciseObj.optString("translation_vi", exerciseObj.optString("englishTranslation", exerciseObj.optString("translation_en"))) else exerciseObj.optString("translation_en", exerciseObj.optString("englishTranslation")),
                                    options = (if (isVi) exerciseObj.optJSONArray("options_vi") ?: exerciseObj.optJSONArray("options") else exerciseObj.optJSONArray("options_en") ?: exerciseObj.optJSONArray("options"))?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: emptyList()
                                )
                            )
                        }
                        "SentenceBuilder", "SENTENCE_BUILDER" -> {
                            exercises.add(
                                Exercise.SentenceBuilder(
                                    englishSentence = exerciseObj.optString("englishSentence"),
                                    hindiWords = exerciseObj.optJSONArray("hindiWords")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: emptyList(),
                                    correctHindiSentence = exerciseObj.optString("correctHindiSentence").takeIf { it.isNotEmpty() } ?: exerciseObj.optJSONArray("correctOrder")?.let { arr -> List(arr.length()) { arr.getString(it) }.joinToString(" ") } ?: ""
                                )
                            )
                        }
                        "Drawing", "DRAWING" -> {
                            exercises.add(
                                Exercise.Drawing(
                                    letterToDraw = exerciseObj.optString("letterToDraw", exerciseObj.optString("letter")),
                                    hint = if (isVi) exerciseObj.optString("hint_vi", exerciseObj.optString("hint")) else exerciseObj.optString("hint_en", exerciseObj.optString("hint"))
                                )
                            )
                        }
                        "GrammarRule", "GRAMMAR_RULE" -> {
                            exercises.add(
                                Exercise.GrammarRule(
                                    title_en = exerciseObj.optString("title_en", exerciseObj.optString("title")),
                                    title_vi = exerciseObj.optString("title_vi", exerciseObj.optString("title")),
                                    content_en = exerciseObj.optString("content_en", exerciseObj.optString("rule", exerciseObj.optString("content"))),
                                    content_vi = exerciseObj.optString("content_vi", exerciseObj.optString("rule_vi", exerciseObj.optString("content")))
                                )
                            )
                        }
                        "Flashcard", "FLASHCARD" -> {
                            exercises.add(
                                Exercise.Flashcard(
                                    hindi = exerciseObj.optString("hindi"),
                                    transliteration = exerciseObj.optString("transliteration", exerciseObj.optString("pronunciation")),
                                    english = exerciseObj.optString("english", exerciseObj.optString("en")),
                                    vietnamese = exerciseObj.optString("vietnamese", exerciseObj.optString("vi")),
                                    audio = exerciseObj.optString("audio")
                                )
                            )
                        }
                        "Introduction", "INTRODUCTION" -> {
                            exercises.add(
                                Exercise.Introduction(
                                    title = exerciseObj.optString("title"),
                                    description = exerciseObj.optString("description"),
                                    keyPoints = exerciseObj.optJSONArray("keyPoints")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: emptyList()
                                )
                            )
                        }
                        "TeachRule", "TEACH_RULE" -> {
                            exercises.add(
                                Exercise.TeachRule(
                                    title = exerciseObj.optString("title"),
                                    explanation = exerciseObj.optString("explanation"),
                                    simpleRule = exerciseObj.optString("simpleRule")
                                )
                            )
                        }
                        "RevisionSummary", "REVISION_SUMMARY" -> {
                            exercises.add(
                                Exercise.RevisionSummary(
                                    title = exerciseObj.optString("title"),
                                    takeaways = exerciseObj.optJSONArray("takeaways")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: emptyList()
                                )
                            )
                        }
                        "Speaking", "SPEAKING" -> {
                            exercises.add(
                                Exercise.Speaking(
                                    hindiPhrase = exerciseObj.optString("hindiPhrase"),
                                    translation = exerciseObj.optString("translation")
                                )
                            )
                        }
                        "MatchPairs", "MATCH_PAIRS" -> {
                            val pairsArray = exerciseObj.getJSONArray("pairs")
                            val pairs = mutableListOf<Pair<String, String>>()
                            for (j in 0 until pairsArray.length()) {
                                val p = pairsArray.getJSONObject(j)
                                pairs.add(Pair(p.getString("hindi"), p.getString("english")))
                            }
                            exercises.add(
                                Exercise.MatchPairs(
                                    instruction = exerciseObj.optString("instruction", "Match the pairs"),
                                    pairs = pairs
                                )
                            )
                        }
                        "CulturalTip", "CULTURAL_TIP" -> {
                            exercises.add(
                                Exercise.CulturalTip(
                                    title_en = exerciseObj.optString("title_en", exerciseObj.optString("title")),
                                    title_vi = exerciseObj.optString("title_vi", exerciseObj.optString("title")),
                                    content_en = exerciseObj.optString("content_en", exerciseObj.optString("content")),
                                    content_vi = exerciseObj.optString("content_vi", exerciseObj.optString("content"))
                                )
                            )
                        }
                        "StoryMode", "STORY_MODE" -> {
                            val parasArray = exerciseObj.optJSONArray("paragraphs")
                            val paragraphs = mutableListOf<Exercise.StoryParagraph>()
                            if (parasArray != null) {
                                for (j in 0 until parasArray.length()) {
                                    val p = parasArray.getJSONObject(j)
                                    paragraphs.add(Exercise.StoryParagraph(p.optString("hindi"), p.optString("translation")))
                                }
                            }
                            val optsArray = exerciseObj.optJSONArray("options")
                            val optionsList = mutableListOf<Exercise.StoryOption>()
                            if (optsArray != null) {
                                for (j in 0 until optsArray.length()) {
                                    val o = optsArray.getJSONObject(j)
                                    optionsList.add(Exercise.StoryOption(o.optString("text"), o.optBoolean("isCorrect")))
                                }
                            }
                            exercises.add(
                                Exercise.StoryMode(
                                    title_en = exerciseObj.optString("title_en"),
                                    title_vi = exerciseObj.optString("title_vi"),
                                    paragraphs = paragraphs,
                                    question_en = exerciseObj.optString("question_en"),
                                    question_vi = exerciseObj.optString("question_vi"),
                                    options = optionsList
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback if empty
        if (exercises.isEmpty()) {
            exercises.add(Exercise.MultipleChoice("Error", "Content not found for ${nodeId}", "x", "X", listOf("X")))
        }

        exercisesCache[cacheKey] = exercises
        exercises
    }
}
