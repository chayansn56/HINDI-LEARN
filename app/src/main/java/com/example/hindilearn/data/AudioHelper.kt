package com.example.hindilearn.data

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech

object AudioHelper {
    fun playAudio(
        context: Context,
        audioKey: String,
        tts: TextToSpeech?,
        textFallback: String,
        speechRate: Float = 1.0f
    ) {
        if (audioKey.isEmpty()) {
            tts?.setSpeechRate(speechRate)
            tts?.speak(textFallback, TextToSpeech.QUEUE_FLUSH, null, null)
            tts?.setSpeechRate(1.0f) // reset
            return
        }

        // Clean the key for a safe file name
        val cleanKey = audioKey.replace(Regex("[?\\\\/:*?\\\"<>|।!]"), "").trim()
        val assetFileName = "audio/$cleanKey.mp3"

        var played = false
        try {
            // Check if file exists in assets before trying to open it to prevent log clutter
            val list = context.assets.list("audio")
            if (list != null && list.contains("$cleanKey.mp3")) {
                val afd = context.assets.openFd(assetFileName)
                val mediaPlayer = MediaPlayer().apply {
                    setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    afd.close()
                    prepare()
                    // Set speed (API 23+) if slow playback is requested
                    if (speechRate != 1.0f && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        playbackParams = playbackParams.setSpeed(speechRate)
                    }
                    start()
                    setOnCompletionListener {
                        release()
                    }
                }
                played = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!played) {
            // Fallback to TTS
            tts?.setSpeechRate(speechRate)
            tts?.speak(textFallback, TextToSpeech.QUEUE_FLUSH, null, null)
            tts?.setSpeechRate(1.0f) // reset
        }
    }
}
