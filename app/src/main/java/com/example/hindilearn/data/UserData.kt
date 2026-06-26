package com.example.hindilearn.data

import android.content.Context
import androidx.room.Room
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

data class UserProgress(
    var xp: Int = 0,
    var streak: Int = 0,
    var hearts: Int = 5,
    var coins: Int = 0,
    var unlockedNodeId: String = "episode_0_1",
    var selectedLanguage: String? = null, // "EN" or "VI"
    var selectedCourse: String = "HINDI", // "HINDI" or "ENGLISH"
    var protagonistState: String = "Scared",
    var unlockedMemories: List<String> = emptyList(),
    var unlockedAchievements: List<String> = emptyList(),
    var mistakeMap: Map<String, Int> = emptyMap(),
    var lastPlayDate: Long = 0L,
    var lastChallengeDate: String = "",
    var userName: String = "",
    var themeMode: String = "SYSTEM" // "SYSTEM", "LIGHT", "DARK"
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
            val mistakes = mutableMapOf<String, Int>()
            if (entity.mistakeMap.isNotEmpty() && entity.mistakeMap.startsWith("{")) {
                try {
                    val jsonObj = org.json.JSONObject(entity.mistakeMap)
                    val keys = jsonObj.keys()
                    while (keys.hasNext()) {
                        val k = keys.next()
                        mistakes[k] = jsonObj.getInt(k)
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
            
            val mems = mutableListOf<String>()
            if (entity.unlockedMemories.isNotEmpty() && entity.unlockedMemories.startsWith("[")) {
                try {
                    val arr = org.json.JSONArray(entity.unlockedMemories)
                    for (i in 0 until arr.length()) mems.add(arr.getString(i))
                } catch (e: Exception) { e.printStackTrace() }
            }
            
            val achs = mutableListOf<String>()
            if (entity.unlockedAchievements.isNotEmpty() && entity.unlockedAchievements.startsWith("[")) {
                try {
                    val arr = org.json.JSONArray(entity.unlockedAchievements)
                    for (i in 0 until arr.length()) achs.add(arr.getString(i))
                } catch (e: Exception) { e.printStackTrace() }
            }
            
            progress = UserProgress(
                xp = entity.xp,
                streak = entity.streak,
                hearts = entity.hearts,
                coins = entity.coins,
                unlockedNodeId = entity.unlockedNodeId,
                selectedLanguage = entity.selectedLanguage,
                selectedCourse = entity.selectedCourse,
                protagonistState = entity.protagonistState,
                unlockedMemories = mems,
                unlockedAchievements = achs,
                mistakeMap = mistakes,
                lastPlayDate = entity.lastPlayDate,
                lastChallengeDate = entity.lastChallengeDate,
                userName = entity.userName,
                themeMode = entity.themeMode
            )
        } else {
            save()
        }
    }

    fun save() {
        val memsJson = org.json.JSONArray(progress.unlockedMemories).toString()
        val achsJson = org.json.JSONArray(progress.unlockedAchievements).toString()
        val mistakesObj = org.json.JSONObject()
        progress.mistakeMap.forEach { (k, v) -> mistakesObj.put(k, v) }
        
        db?.userProgressDao()?.saveProgress(UserProgressEntity(
            id = 1,
            xp = progress.xp,
            streak = progress.streak,
            hearts = progress.hearts,
            coins = progress.coins,
            unlockedNodeId = progress.unlockedNodeId,
            selectedLanguage = progress.selectedLanguage ?: "EN",
            selectedCourse = progress.selectedCourse,
            protagonistState = progress.protagonistState,
            unlockedMemories = memsJson,
            unlockedAchievements = achsJson,
            mistakeMap = mistakesObj.toString(),
            lastPlayDate = progress.lastPlayDate,
            lastChallengeDate = progress.lastChallengeDate,
            userName = progress.userName,
            themeMode = progress.themeMode
        ))
        
        // Trigger Cloud Sync Backup (runs on IO thread internally or via GlobalScope to not block UI)
        appContext?.let { ctx ->
            kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                CloudSyncManager.backupProgressToCloud(ctx, progress)
            }
        }
    }

    fun addXp(amount: Int) {
        val today = System.currentTimeMillis() / (1000 * 60 * 60 * 24)
        val lastPlay = progress.lastPlayDate / (1000 * 60 * 60 * 24)
        
        var newStreak = progress.streak
        if (today - lastPlay == 1L) {
            newStreak += 1 // Next consecutive day
        } else if (today - lastPlay > 1L) {
            newStreak = 1 // Lost streak
        } else if (progress.streak == 0) {
            newStreak = 1 // Started streak today
        }

        progress = progress.copy(
            xp = progress.xp + amount,
            coins = progress.coins + (amount / 2),
            streak = newStreak,
            lastPlayDate = System.currentTimeMillis()
        )
        save()
        // Check for XP achievements
        if (progress.xp >= 100) {
            unlockAchievement("ach_xp_hundred")
        }
    }

    fun incrementMistake(word: String) {
        if (word.isBlank()) return
        val count = progress.mistakeMap[word] ?: 0
        progress = progress.copy(
            mistakeMap = progress.mistakeMap + (word to (count + 1))
        )
        save()
    }

    fun markChallengeCompleted() {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val today = sdf.format(java.util.Date())
        progress = progress.copy(lastChallengeDate = today)
        save()
    }

    fun updateUserName(name: String) {
        progress = progress.copy(userName = name.trim())
        save()
    }

    fun updateThemeMode(mode: String) {
        progress = progress.copy(themeMode = mode)
        save()
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
                "ach_xp_thousand" to Triple("Grandmaster", 200, 100),
                "ach_explorer" to Triple("Cultural Explorer", 25, 12),
                "ach_srs_novice" to Triple("Memory Novice", 30, 15),
                "ach_srs_expert" to Triple("Memory Expert", 100, 50),
                "ach_story_reader" to Triple("Storybook", 40, 20),
                "ach_perfect_score" to Triple("Flawless", 50, 25),
                "ach_night_owl" to Triple("Night Owl", 20, 10),
                "ach_early_bird" to Triple("Early Bird", 20, 10),
                "ach_weekend_warrior" to Triple("Weekend Warrior", 30, 15),
                "ach_polyglot" to Triple("Polyglot in Training", 50, 25),
                "ach_streak_7" to Triple("1-Week Streak", 70, 35),
                "ach_streak_30" to Triple("1-Month Streak", 300, 150),
                "ach_voice_actor" to Triple("Voice Actor", 40, 20),
                "ach_calligrapher" to Triple("Calligrapher", 40, 20),
                "ach_grammar_nerd" to Triple("Grammar Nerd", 40, 20),
                "ach_speed_reader" to Triple("Speed Reader", 30, 15),
                "ach_film_buff" to Triple("Bollywood Buff", 50, 25),
                "ach_foodie" to Triple("Street Foodie", 50, 25)
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

    fun getSeasons(course: String = UserManager.progress.selectedCourse): List<Season> {
        if (course == "ENGLISH") {
            val p0TitlesEn = listOf(
                "The English Alphabet", "Vowels & Consonants", "Tricky Sounds (th, sh, ch)", "Final Sounds (s, t, d)", "Word Stress Basics",
                "Basic Greetings", "Simple Pronouns", "To Be: Present", "Numbers 1-100", "Telling Time",
                "Family Members", "Food & Drink", "Body Parts", "Colors & Shapes", "Clothing",
                "Simple Present Tense", "Action Verbs", "Daily Routine", "Days & Months", "Weather & Seasons",
                "Adjectives: Opposites", "Prepositions of Place", "Asking Questions", "Present Continuous", "Simple Past Tense",
                "Past Continuous", "Future with Will", "Future with Going To", "Modal Verbs", "Foundation Final Exam"
            )
            val p0DescsEn = p0TitlesEn.map { "Learn $it in English." }
            
            val p0EpisodesEn = (1..30).map { i ->
                Episode("episode_0_$i", p0TitlesEn[i - 1], p0DescsEn[i - 1], "FOUNDATION", "You completed ${p0TitlesEn[i - 1]}!", listOf("Learn ${p0TitlesEn[i - 1]}"), false)
            }
            
            val specialBlueprintsEn = listOf(
                Triple("pron", "Pronunciation Mastery", listOf("Word Stress", "Intonation", "Connected Speech", "Final Consonants", "Vowel Pairs")),
                Triple("it_english", "IT & Tech English", listOf("Software Terms", "Agile Meetings", "Code Reviews", "Tech Presentations", "Bug Reports")),
                Triple("hospitality", "Tourism & Hospitality", listOf("Hotel Reception", "Restaurant Service", "Tour Guiding", "Handling Complaints", "Concierge")),
                Triple("business", "Office Business", listOf("Formal Emails", "Negotiations", "Job Interviews", "Sales Pitches", "Networking")),
                Triple("ielts", "IELTS Prep", listOf("Academic Vocabulary", "Essay Structures", "Speaking Part 1", "Speaking Part 2", "Speaking Part 3")),
                Triple("kids", "Kids Vocabulary", listOf("Animals", "Colors", "Toys", "School", "Cartoons"))
            )
            val specialSeasonsEn = specialBlueprintsEn.map { (id, title, list) ->
                val episodes = list.mapIndexed { idx, epTitle ->
                    Episode("episode_${id}_${idx + 1}", epTitle, "Master $epTitle.", "SPECIAL", "You completed $epTitle!", listOf("Learn $epTitle"), false)
                }
                Season(id, title, "Deep dive into $title.", episodes)
            }
            
            val s1EpisodesEn = (1..20).map { i -> Episode("episode_$i", "Episode $i", "Arrival in USA pt $i", "STORY", "Completed Episode $i.", listOf("Survive Episode $i"), true) }
            val s2EpisodesEn = (21..40).map { i -> Episode("episode_$i", "Episode $i", "Living in USA pt $i", "STORY", "Completed Episode $i.", listOf("Survive Episode $i"), true) }
            val s3EpisodesEn = (41..60).map { i -> Episode("episode_$i", "Episode $i", "Friendship pt $i", "STORY", "Completed Episode $i.", listOf("Survive Episode $i"), true) }
            val s4EpisodesEn = (61..80).map { i -> Episode("episode_$i", "Episode $i", "Love and Relationships pt $i", "STORY", "Completed Episode $i.", listOf("Survive Episode $i"), true) }
            val s5EpisodesEn = (81..100).map { i -> Episode("episode_$i", "Episode $i", "Belonging pt $i", "STORY", "Completed Episode $i.", listOf("Survive Episode $i"), true) }

            val coreSeasonsEn = listOf(
                Season("phase_0", "Phase 0: Foundation Academy", "The most important phase. Learn the building blocks.", p0EpisodesEn),
                Season("season_1", "Season 1: Arrival in USA", "Navigating the first days.", s1EpisodesEn),
                Season("season_2", "Season 2: Living in USA", "Daily life, hobbies, and mistakes.", s2EpisodesEn),
                Season("season_3", "Season 3: Friendship", "Deepening bonds and inside jokes.", s3EpisodesEn),
                Season("season_4", "Season 4: Love and Relationships", "Families, marriage, and emotions.", s4EpisodesEn),
                Season("season_5", "Season 5: Belonging", "America becomes home.", s5EpisodesEn)
            )

            return coreSeasonsEn + specialSeasonsEn
        }

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
            Triple("pron", "Pronunciation Lab", listOf("Aspirated vs Unaspirated", "Retroflex vs Dental", "Nasal Sounds", "The Schwa Deletion Rule", "R, L, V, and SH Sounds", "Conjunct Consonants", "Question Intonation & Emotion", "Minimal Pairs Challenge", "Intonation in Statements", "Pronunciation Review & Test", "Geminate Consonants", "Word Stress Patterns", "Perso-Arabic Sounds", "Vowel Lengthening", "Connected Speech Flow")),
            Triple("speak", "Speaking", listOf("Greetings & Farewells", "Introducing Yourself", "Being Polite", "Question Words", "At a Restaurant", "Shopping & Bargaining", "Formal vs Informal (Aap/Tum)", "Expressing Feelings", "Making Plans", "Phone & Emergency", "Ordering Food Online", "Advanced Conversations", "Asking for Opinions", "Talking about Hobbies", "Making Excuses", "Arranging a Meeting", "Giving Directions")),
            Triple("gram", "Grammar", listOf("The Verb 'To Be'", "Sentence Structure (SOV)", "Gender System", "Pronouns & Possessives", "Postpositions", "Present Tense", "Past Tense & 'Ne'", "Future Tense", "Adjectives Agreement", "Negation & Imperatives", "Comparisons", "Honorifics & Compound Verbs", "Subjunctive Mood", "Passive Voice", "Obligation & Necessity", "Ability (Sakkna)", "Desiderative (Chahna)", "Causative Verbs", "Relative Clauses")),
            Triple("listen", "Listening", listOf("Single Words (Slow)", "Two-Word Phrases", "Full Sentences", "Short Dialogues", "Numbers in Context", "Native Speed Challenge", "Weather Report Listening", "Short News Podcasts", "Audio Story Comprehension", "Eavesdropping in Public", "Airport Announcements", "Restaurant Orders Audio", "Directions Dialogue")),
            Triple("write", "Writing", listOf("Tracing Vowels", "Throat & Palate Letters", "Retroflex & Dental Letters", "Lips & Semivowels Letters", "Writing with Matras", "Writing Simple Words", "Writing Short Sentences", "Writing Challenge", "Devanagari Speed Writing", "Advanced Conjuncts Writing", "Formatting Emails", "Digital Keyboards", "Letter Structures", "Creative Writing Prompts", "Official Applications")),
            Triple("culture", "Culture", listOf("Indian Festivals", "Indian Food Etiquette", "Family & Respect", "Religious Diversity", "Clothing & Attire", "Traditional Arts", "Indian Weddings", "Traditional Music & Dance", "Cinema & Cricket", "Tea/Chai Culture", "Traditional Clothing", "Indian Festivals", "Bazaar Shopping Culture")),
            Triple("travel", "Travel Hindi", listOf("At the Airport", "Hailing a Taxi/Auto", "Checking into Hotel", "Asking for Directions", "Ordering Street Food", "Emergency & Health", "Local Sightseeing", "Train Journeys", "Bargaining at a Bazaar", "Renting a Scooter", "Buying Sim Card", "Asking for Recommendations", "Train Station Help", "Luggage Troubles", "Sightseeing Queries")),
            Triple("business", "Business Hindi", listOf("Office Vocabulary", "Meeting Phrases", "Email Templates", "Negotiation", "Formal Register", "Corporate Culture", "Job Interview in Hindi", "Customer Service Dialogue", "Pitching an Idea", "Performance Reviews", "Client Negotiations", "Scheduling Conferences", "Signing Contracts")),
            Triple("bollywood", "Bollywood Hindi", listOf("Common Movie Slang", "Romantic Expressions", "Iconic Dialogues", "Song Lyrics Vocabulary", "Drama & Emotion", "Cinema History", "Classic Melodrama", "Superhit Song Analysis", "Award Show Expressions", "Fan Following Slang", "Romantic Dialogues", "Action Thriller Phrases", "Classic Item Songs")),
            Triple("whatsapp", "WhatsApp Hindi", listOf("Text Abbreviations", "Hinglish Messaging", "Emoji Etiquette", "Group Chat Dynamics", "Dating App Slang", "Social Media Buzzwords", "Meme Vocabulary", "Virtual Birthday Wishes", "Hinglish Slang", "Dating App Slang", "Chat Acronyms")),
            Triple("emerg", "Emergency Hindi", listOf("Medical Help", "Reporting Theft", "Lost Passport", "Pharmacy Terms", "Natural Disasters", "Road Accident Help", "Lost Child or Person", "Fire Emergency", "Police Station Help", "First Aid Help", "Road Accident Help")),
            Triple("numbers_lab", "Numbers Lab", listOf("Numbers 0-100", "Hundreds 100-500", "Hundreds 500-1000", "Large Numbers Practice", "Numbers Challenge")),
            Triple("alphabets_lab", "Alphabet Hub", listOf("Vowels & Vietnamese Sounds", "Velars & Palatals", "Retroflex & Dentals", "Labials & Semivowels", "Conjuncts & Nuqta"))
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

    fun getEpisodeById(id: String, course: String = UserManager.progress.selectedCourse): Episode? {
        return getAllEpisodes(course).find { it.id == id }
    }

    fun getAllEpisodes(course: String = UserManager.progress.selectedCourse): List<Episode> {
        return getSeasons(course).flatMap { it.episodes }
    }

    fun getEpisodesWithLockState(unlockedEpisodeId: String, course: String = UserManager.progress.selectedCourse): List<Episode> {
        val allEpisodes = getAllEpisodes(course)
        val safeId = if (unlockedEpisodeId.startsWith("node")) "episode_1" else unlockedEpisodeId
        val unlockedIndex = allEpisodes.indexOfFirst { it.id == safeId }
        val resolvedIndex = if (unlockedIndex == -1) 0 else unlockedIndex
        return allEpisodes.mapIndexed { index, episode ->
            episode.copy(isLocked = index > resolvedIndex)
        }
    }
}
