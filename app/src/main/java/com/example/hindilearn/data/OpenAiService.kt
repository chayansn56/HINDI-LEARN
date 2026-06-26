package com.example.hindilearn.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object OpenAiService {
    // We keep the name OpenAiService for compatibility with existing UI components,
    // but we are using the Gemini API now as requested.
    private val API_KEY get() = com.example.hindilearn.BuildConfig.GROQ_API_KEY
    private val API_URL get() = "https://api.groq.com/openai/v1/chat/completions"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val JSON = "application/json; charset=utf-8".toMediaType()

    suspend fun generateChatResponse(
        systemPrompt: String,
        chatHistory: List<Pair<String, Boolean>> // Pair<Text, IsUser>
    ): String? = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject()
            requestBody.put("model", "llama-3.1-8b-instant")
            requestBody.put("temperature", 0.7)

            val messagesArray = JSONArray()
            messagesArray.put(JSONObject().apply {
                put("role", "system")
                put("content", systemPrompt)
            })

            for (msg in chatHistory) {
                messagesArray.put(JSONObject().apply {
                    put("role", if (msg.second) "user" else "assistant")
                    put("content", msg.first)
                })
            }
            requestBody.put("messages", messagesArray)

            val body = requestBody.toString().toRequestBody(JSON)
            
            val request = Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer $API_KEY")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBodyString = response.body?.string()

            if (response.isSuccessful && responseBodyString != null) {
                val jsonResponse = JSONObject(responseBodyString)
                val choices = jsonResponse.optJSONArray("choices")
                if (choices != null && choices.length() > 0) {
                    val messageObj = choices.getJSONObject(0).optJSONObject("message")
                    return@withContext messageObj?.optString("content")
                }
            } else {
                return@withContext "Error: API returned code ${response.code} - $responseBodyString"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Error: ${e.message}"
        }
        return@withContext null
    }

    suspend fun gradePronunciation(targetSentence: String, userSpokenText: String): Triple<String, Int, String>? = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject()
            requestBody.put("model", "llama-3.1-8b-instant")
            requestBody.put("temperature", 0.3)
            requestBody.put("response_format", JSONObject().put("type", "json_object"))
            
            val systemPrompt = """
                You are a strict but encouraging language teacher who specializes in correcting pronunciation for native Vietnamese speakers.
                The user was supposed to say: "$targetSentence"
                The user actually said: "$userSpokenText"
                
                Evaluate how close they were, specifically identifying typical Vietnamese pronunciation mistakes (e.g., omitting ending consonants like -s, -t, -d, or failing to make long/short vowel distinctions).
                Highlight the exact parts they got right or wrong, and offer concrete suggestions.
                Rate their attempt with a rating from 1 to 5 (integer value).
                
                You MUST return your response as a valid JSON object exactly like this:
                {
                    "grade": "A", // Can be A, B, C, D, or F
                    "rating": 5, // Integer from 1 (poor) to 5 (excellent)
                    "feedback": "Your detailed explanation of what they did right or wrong, including tips for native Vietnamese speakers."
                }
            """.trimIndent()
            
            val messagesArray = JSONArray()
            messagesArray.put(JSONObject().apply {
                put("role", "system")
                put("content", systemPrompt)
            })
            messagesArray.put(JSONObject().apply {
                put("role", "user")
                put("content", "Please grade my pronunciation.")
            })
            requestBody.put("messages", messagesArray)

            val body = requestBody.toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer $API_KEY")
                .post(body)
                .build()
                
            val response = client.newCall(request).execute()
            val responseBodyString = response.body?.string()
            
            if (response.isSuccessful && responseBodyString != null) {
                val jsonResponse = JSONObject(responseBodyString)
                val choices = jsonResponse.optJSONArray("choices")
                if (choices != null && choices.length() > 0) {
                    val messageObj = choices.getJSONObject(0).optJSONObject("message")
                    val content = messageObj?.optString("content")
                    if (content != null) {
                        try {
                            val resultJson = JSONObject(content.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim())
                            return@withContext Triple(
                                resultJson.optString("grade", "N/A"),
                                resultJson.optInt("rating", 3),
                                resultJson.optString("feedback", "No feedback provided.")
                            )
                        } catch (e: Exception) {
                            return@withContext Triple("F", 1, "Error parsing response: $content")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }
}
