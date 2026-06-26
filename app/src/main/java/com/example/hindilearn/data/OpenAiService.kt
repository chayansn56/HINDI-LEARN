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
            requestBody.put("model", "llama3-8b-8192")
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

    suspend fun gradePronunciation(targetSentence: String, userSpokenText: String): Pair<String, String>? = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject()
            requestBody.put("model", "llama3-8b-8192")
            requestBody.put("temperature", 0.3)
            requestBody.put("response_format", JSONObject().put("type", "json_object"))
            
            val systemPrompt = """
                You are a strict but encouraging language teacher.
                The user was supposed to say: "$targetSentence"
                The user actually said: "$userSpokenText"
                
                Evaluate how close they were. Ignore minor punctuation or capitalization differences.
                If they are completely wrong or said something else, give an F.
                If they are perfect, give an A.
                
                You MUST return your response as a valid JSON object exactly like this:
                {
                    "grade": "A", // Can be A, B, C, D, or F
                    "feedback": "Your detailed explanation of what they did right or wrong."
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
                            val resultJson = JSONObject(content)
                            return@withContext Pair(
                                resultJson.optString("grade", "N/A"),
                                resultJson.optString("feedback", "No feedback provided.")
                            )
                        } catch (e: Exception) {
                            return@withContext Pair("F", "Error parsing response: $content")
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
