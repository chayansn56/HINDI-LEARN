package com.example.hindilearn.data

import android.content.Context
import androidx.room.Room
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

data class UserProgress(
    var xp: Int = 0,
    var streak: Int = 0,
    var hearts: Int = 5,
    var coins: Int = 0,
    var unlockedNodeId: String = "episode_0_1",
    var selectedLanguage: String? = null, // "EN" or "VI"
    var protagonistState: String = "Scared",
    var unlockedMemories: List<String> = emptyList(),
    var unlockedAchievements: List<String> = emptyList()
)

object UserManager {
    var progress by mutableStateOf(UserProgress())
    private var db: AppDatabase? = null
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "hindi-learn-db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        
        val entity = db?.userProgressDao()?.getProgress()
        if (entity != null) {
            progress = UserProgress(
                xp = entity.xp,
                streak = entity.streak,
                hearts = entity.hearts,
                coins = entity.coins,
                unlockedNodeId = entity.unlockedNodeId,
                selectedLanguage = entity.selectedLanguage,
                protagonistState = entity.protagonistState,
                unlockedMemories = entity.unlockedMemories.split(",").filter { it.isNotEmpty() },
                unlockedAchievements = entity.unlockedAchievements.split(",").filter { it.isNotEmpty() }
            )
        } else {
            save()
        }
    }

    private fun save() {
        db?.userProgressDao()?.saveProgress(UserProgressEntity(
            id = 1,
            xp = progress.xp,
            streak = progress.streak,
            hearts = progress.hearts,
            coins = progress.coins,
            unlockedNodeId = progress.unlockedNodeId,
            selectedLanguage = progress.selectedLanguage ?: "EN",
            protagonistState = progress.protagonistState,
            unlockedMemories = progress.unlockedMemories.joinToString(","),
            unlockedAchievements = progress.unlockedAchievements.joinToString(",")
        ))
    }

    fun addXp(amount: Int) {
        progress = progress.copy(
            xp = progress.xp + amount,
            coins = progress.coins + (amount / 2)
        )
        save()
        // Check for XP achievements
        if (progress.xp >= 100) {
            unlockAchievement("ach_xp_hundred")
        }
    }

    fun loseHeart() {
        if (progress.hearts > 0) {
            progress = progress.copy(hearts = progress.hearts - 1)
            save()
        }
    }

    fun unlockNextNode(currentNodeId: String) {
        val nodes = GamifiedCurriculum.getAllEpisodes()
        val currentIndex = nodes.indexOfFirst { it.id == currentNodeId }
        
        // Check for memories to unlock based on completing this node
        val memoriesToUnlock = GamifiedCurriculum.getMemories().filter { it.episodeUnlockId == currentNodeId }
        memoriesToUnlock.forEach { unlockMemory(it.id) }
        
        // Check achievements on first lesson or alphabet master milestones
        if (currentNodeId == "episode_0_1") {
            unlockAchievement("ach_first_step")
        }
        if (currentNodeId == "episode_0_16") {
            unlockAchievement("ach_alphabet_master")
        }
        if (currentNodeId.startsWith("episode_culture_")) {
            unlockAchievement("ach_explorer")
        }
        
        if (currentIndex != -1 && currentIndex + 1 < nodes.size) {
            val nextNodeId = nodes[currentIndex + 1].id
            val currentUnlockedIndex = nodes.indexOfFirst { it.id == progress.unlockedNodeId }
            if (currentIndex >= currentUnlockedIndex) {
                var newState = progress.protagonistState
                if (nextNodeId == "episode_5") newState = "Surviving"
                if (nextNodeId == "episode_10") newState = "Traveling"
                if (nextNodeId == "episode_15") newState = "Speaking"
                if (nextNodeId == "episode_20") newState = "Belonging"
                
                progress = progress.copy(
                    unlockedNodeId = nextNodeId,
                    protagonistState = newState
                )
                save()
            }
        }
    }

    fun unlockMemory(memoryId: String) {
        if (!progress.unlockedMemories.contains(memoryId)) {
            progress = progress.copy(unlockedMemories = progress.unlockedMemories + memoryId)
            save()
        }
    }

    fun unlockAchievement(achievementId: String) {
        if (!progress.unlockedAchievements.contains(achievementId)) {
            val achievementsMap = mapOf(
                "ach_first_step" to Triple("First Step", 20, 10),
                "ach_alphabet_master" to Triple("Alphabet Master", 50, 25),
                "ach_daily_warrior" to Triple("Daily Warrior", 30, 15),
                "ach_xp_hundred" to Triple("Century Club", 40, 20),
                "ach_explorer" to Triple("Cultural Explorer", 25, 12)
            )
            val info = achievementsMap[achievementId] ?: return
            
            progress = progress.copy(
                unlockedAchievements = progress.unlockedAchievements + achievementId,
                xp = progress.xp + info.second,
                coins = progress.coins + info.third
            )
            save()
            
            appContext?.let { ctx ->
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    android.widget.Toast.makeText(
                        ctx,
                        "🏆 Achievement Unlocked: ${info.first}! +${info.second} XP",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun updateLanguage(language: String) {
        progress = progress.copy(selectedLanguage = language)
        save()
    }
}

data class Episode(
    val id: String,
    val title: String,
    val synopsis: String,
    val type: String,
    val confidenceMessage: String,
    val achievements: List<String> = emptyList(),
    val isLocked: Boolean
)

data class Season(
    val id: String,
    val title: String,
    val description: String,
    val episodes: List<Episode>
)

data class MemoryItem(
    val id: String,
    val title: String,
    val description: String,
    val episodeUnlockId: String
)

object GamifiedCurriculum {
    fun getMemories(): List<MemoryItem> {
        return listOf(
            MemoryItem("mem_1", "Surviving Ramesh's Taxi", "I was terrified, but I didn't die.", "episode_2"),
            MemoryItem("mem_2", "Grandma's Kitchen", "I have never been so full in my life.", "episode_4"),
            MemoryItem("mem_3", "Getting Lost", "I walked the wrong way for an hour.", "episode_7"),
            MemoryItem("mem_4", "Spicy Street Food", "My tongue is still burning.", "episode_12"),
            MemoryItem("mem_5", "Diwali", "The day India felt like home.", "episode_20")
        )
    }

    fun getSeasons(): List<Season> {
        val p0Titles = listOf(
            "The Arrival", "First Greetings", "Basic Needs", "Navigation", "First Conversation",
            "Sounds That Don't Exist in English", "Tongue Position Matters", "Vowels Part 1: Short Sounds", "Vowels Part 2: Long Sounds", "Vowels Part 3: Special Sounds",
            "Consonants: Throat (कवर्ग)", "Consonants: Palate (चवर्ग)", "Consonants: Retroflex (टवर्ग)", "Consonants: Dental (तवर्ग)", "Consonants: Lips (पवर्ग)",
            "Consonants: Semivowels & Sibilants", "Alphabet Review & Test", "Matras Part 1: After & Below", "Matras Part 2: Above & Compound", "Reading Practice: First Words",
            "Numbers 0–10", "Numbers 11–20 & Tens to 100", "Essential Words: Family & People", "Essential Words: Food & Drink", "Essential Words: Body & Health",
            "Colors & Nature", "Time & Calendar", "Common Verbs", "15 Survival Sentences", "Foundation Final Exam"
        )
        val p0Descs = listOf(
            "John lands in Delhi and learns his first word.", "John meets Rahul at breakfast.", "Asking for water and tea.", "Getting lost in Connaught Place.", "Putting it all together.",
            "Learn aspirated sounds क/ख, ग/घ.", "Understand retroflex vs dental sounds.", "Short vowels: अ, इ, उ, ऋ.", "Long vowels: आ, ई, ऊ, ए, ऐ, ओ, औ.", "Special vowels: अं, अः.",
            "Sounds made in the throat.", "Sounds made on the palate.", "Retroflex sounds: tongue curled back.", "Dental sounds: tongue touching teeth.", "Lip sounds: प, फ, ब, bh, m.",
            "Semivowels and sibilants.", "Test your letters knowledge.", "Vowel attachment marks.", "Above-line and compound matras.", "Combining letters and matras to read.",
            "Learn to count 0 to 10.", "Learn to count to 100.", "Words for family members.", "Words for Indian food and fruits.", "Words for body parts and health.",
            "Colors, nature, and celestial bodies.", "Days of week, months, seasons.", "Most common verbs: do, eat, go.", "Crucial sentences for daily survival.", "Comprehensive final exam."
        )

        val p0Episodes = (1..30).map { i ->
            Episode(
                id = "episode_0_$i",
                title = p0Titles[i - 1],
                synopsis = p0Descs[i - 1],
                type = "FOUNDATION",
                confidenceMessage = "You completed ${p0Titles[i - 1]}!",
                achievements = listOf("Learn ${p0Titles[i - 1]}"),
                isLocked = false
            )
        }

        val specialBlueprints = listOf(
            Triple("pron", "Pronunciation Lab", listOf("Aspirated vs Unaspirated", "Retroflex vs Dental", "Nasal Sounds", "The Schwa Deletion Rule", "R, L, V, and SH Sounds", "Conjunct Consonants", "Question Intonation & Emotion", "Minimal Pairs Challenge")),
            Triple("speak", "Speaking", listOf("Greetings & Farewells", "Introducing Yourself", "Being Polite", "Question Words", "At a Restaurant", "Shopping & Bargaining", "Formal vs Informal (Aap/Tum)", "Expressing Feelings", "Making Plans", "Phone & Emergency")),
            Triple("gram", "Grammar", listOf("The Verb 'To Be'", "Sentence Structure (SOV)", "Gender System", "Pronouns & Possessives", "Postpositions", "Present Tense", "Past Tense & 'Ne'", "Future Tense", "Adjectives Agreement", "Negation & Imperatives", "Comparisons", "Honorifics & Compound Verbs")),
            Triple("listen", "Listening", listOf("Single Words (Slow)", "Two-Word Phrases", "Full Sentences", "Short Dialogues", "Numbers in Context", "Native Speed Challenge")),
            Triple("write", "Writing", listOf("Tracing Vowels", "Throat & Palate Letters", "Retroflex & Dental Letters", "Lips & Semivowels Letters", "Writing with Matras", "Writing Simple Words", "Writing Short Sentences", "Writing Challenge")),
            Triple("culture", "Culture", listOf("Indian Festivals", "Indian Food Etiquette", "Family & Respect", "Religious Diversity", "Clothing & Attire", "Traditional Arts")),
            Triple("travel", "Travel Hindi", listOf("At the Airport", "Hailing a Taxi/Auto", "Checking into Hotel", "Asking for Directions", "Ordering Street Food", "Emergency & Health", "Local Sightseeing", "Train Journeys")),
            Triple("business", "Business Hindi", listOf("Office Vocabulary", "Meeting Phrases", "Email Templates", "Negotiation", "Formal Register", "Corporate Culture")),
            Triple("bollywood", "Bollywood Hindi", listOf("Common Movie Slang", "Romantic Expressions", "Iconic Dialogues", "Song Lyrics Vocabulary", "Drama & Emotion", "Cinema History")),
            Triple("whatsapp", "WhatsApp Hindi", listOf("Text Abbreviations", "Hinglish Messaging", "Emoji Etiquette", "Group Chat Dynamics"))
        )

        val specialSeasons = specialBlueprints.map { (id, title, list) ->
            val episodes = list.mapIndexed { idx, epTitle ->
                Episode(
                    id = "episode_${id}_${idx + 1}",
                    title = epTitle,
                    synopsis = "Master the concepts of $epTitle.",
                    type = "SPECIAL",
                    confidenceMessage = "You completed $epTitle!",
                    achievements = listOf("Learn $epTitle"),
                    isLocked = false
                )
            }
            Season(id, title, "Deep dive into $title.", episodes)
        }

        val s1Episodes = (1..20).map { i ->
            Episode("episode_$i", "Episode $i", "Arrival in India pt $i", "STORY", "Today you completed Episode $i.", listOf("Survive Episode $i"), true)
        }
        val s2Episodes = (21..40).map { i ->
            Episode("episode_$i", "Episode $i", "Living in India pt $i", "STORY", "Today you completed Episode $i.", listOf("Survive Episode $i"), true)
        }
        val s3Episodes = (41..60).map { i ->
            Episode("episode_$i", "Episode $i", "Friendship pt $i", "STORY", "Today you completed Episode $i.", listOf("Survive Episode $i"), true)
        }
        val s4Episodes = (61..80).map { i ->
            Episode("episode_$i", "Episode $i", "Love and Relationships pt $i", "STORY", "Today you completed Episode $i.", listOf("Survive Episode $i"), true)
        }
        val s5Episodes = (81..100).map { i ->
            Episode("episode_$i", "Episode $i", "Belonging pt $i", "STORY", "Today you completed Episode $i.", listOf("Survive Episode $i"), true)
        }

        val coreSeasons = listOf(
            Season("phase_0", "Phase 0: Foundation Academy", "The most important phase. Learn the building blocks.", p0Episodes),
            Season("season_1", "Season 1: Arrival in India", "John navigates his first days.", s1Episodes),
            Season("season_2", "Season 2: Living in India", "Daily life, hobbies, and mistakes.", s2Episodes),
            Season("season_3", "Season 3: Friendship", "Deepening bonds and inside jokes.", s3Episodes),
            Season("season_4", "Season 4: Love and Relationships", "Families, marriage, and deep emotions.", s4Episodes),
            Season("season_5", "Season 5: Belonging", "India becomes a second home.", s5Episodes)
        )

        return coreSeasons + specialSeasons
    }

    fun getEpisodeById(id: String): Episode? {
        return getAllEpisodes().find { it.id == id }
    }

    fun getAllEpisodes(): List<Episode> {
        return getSeasons().flatMap { it.episodes }
    }

    fun getEpisodesWithLockState(unlockedEpisodeId: String): List<Episode> {
        val allEpisodes = getAllEpisodes()
        val safeId = if (unlockedEpisodeId.startsWith("node")) "episode_1" else unlockedEpisodeId
        val unlockedIndex = allEpisodes.indexOfFirst { it.id == safeId }
        val resolvedIndex = if (unlockedIndex == -1) 0 else unlockedIndex
        return allEpisodes.mapIndexed { index, episode ->
            episode.copy(isLocked = index > resolvedIndex)
        }
    }
}
