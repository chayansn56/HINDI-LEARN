package com.example.hindilearn.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object CurriculumSyncManager {

    private const val BASE_REMOTE_URL = "https://raw.githubusercontent.com/Vietana/HindiLearn-Data/main/episodes/"

    /**
     * Attempts to fetch the latest episode JSON from the remote CDN.
     * If successful, saves it to the local app storage to override the bundled asset.
     */
    suspend fun syncEpisodeData(context: Context, episodeId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val fileName = "$episodeId.json"
            val url = URL("$BASE_REMOTE_URL$fileName")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                
                // Save to local storage
                saveToLocalStorage(context, fileName, jsonString)
                return@withContext true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext false
    }

    private fun saveToLocalStorage(context: Context, fileName: String, content: String) {
        val file = File(context.filesDir, fileName)
        file.writeText(content)
    }

    /**
     * Checks if a locally downloaded overriding version of the file exists.
     * Returns the File if it exists, otherwise null (meaning fallback to assets).
     */
    fun getLocalOverrideFile(context: Context, fileName: String): File? {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) file else null
    }
}
