package com.example.hindilearn.data

enum class LearningLevel {
    BASIC, MEDIUM, ADVANCED
}

data class VocabularyCategory(
    val id: String,
    val titleEn: String,
    val titleVi: String,
    val level: LearningLevel
)

data class VocabularyWord(
    val english: String,
    val vietnamese: String,
    val hindi: String,
    val phonetic: String,
    val categoryId: String
)

object HindiVocabularyProvider {
    val categories = listOf(
        // BASIC
        VocabularyCategory("greetings", "Greetings", "Chào hỏi", LearningLevel.BASIC),
        VocabularyCategory("numbers", "Numbers", "Số đếm", LearningLevel.BASIC),
        VocabularyCategory("family", "Family", "Gia đình", LearningLevel.BASIC),
        
        // MEDIUM
        VocabularyCategory("travel", "Travel & Directions", "Du lịch & Phương hướng", LearningLevel.MEDIUM),
        VocabularyCategory("food", "Food & Dining", "Đồ ăn & Ẩm thực", LearningLevel.MEDIUM),
        VocabularyCategory("time", "Time & Days", "Thời gian & Ngày tháng", LearningLevel.MEDIUM),

        // ADVANCED
        VocabularyCategory("emotions", "Emotions & Feelings", "Cảm xúc", LearningLevel.ADVANCED),
        VocabularyCategory("politics", "Society & Politics", "Xã hội & Chính trị", LearningLevel.ADVANCED),
        VocabularyCategory("idioms", "Common Idioms", "Thành ngữ phổ biến", LearningLevel.ADVANCED)
    )

    val words = listOf(
        // --- BASIC: Greetings ---
        VocabularyWord("Hello", "Xin chào", "नमस्ते", "Namaste", "greetings"),
        VocabularyWord("Goodbye", "Tạm biệt", "अलविदा", "Alvida", "greetings"),
        VocabularyWord("How are you?", "Bạn có khỏe không?", "आप कैसे हैं?", "Aap kaise hain?", "greetings"),
        VocabularyWord("Thank you", "Cảm ơn", "धन्यवाद", "Dhanyavaad", "greetings"),

        // --- BASIC: Numbers ---
        VocabularyWord("One", "Một", "एक", "Ek", "numbers"),
        VocabularyWord("Two", "Hai", "दो", "Do", "numbers"),
        VocabularyWord("Three", "Ba", "तीन", "Teen", "numbers"),
        VocabularyWord("Four", "Bốn", "चार", "Chaar", "numbers"),

        // --- BASIC: Family ---
        VocabularyWord("Mother", "Mẹ", "माता", "Mata", "family"),
        VocabularyWord("Father", "Cha", "पिता", "Pita", "family"),
        VocabularyWord("Brother", "Anh/Em trai", "भाई", "Bhai", "family"),
        VocabularyWord("Sister", "Chị/Em gái", "बहन", "Behan", "family"),

        // --- MEDIUM: Travel ---
        VocabularyWord("Where is the hotel?", "Khách sạn ở đâu?", "होटल कहाँ है?", "Hotel kahan hai?", "travel"),
        VocabularyWord("Ticket", "Vé", "टिकट", "Ticket", "travel"),
        VocabularyWord("Right", "Phải", "दाएँ", "Dayein", "travel"),
        VocabularyWord("Left", "Trái", "बाएँ", "Bayein", "travel"),

        // --- MEDIUM: Food ---
        VocabularyWord("Water", "Nước", "पानी", "Paani", "food"),
        VocabularyWord("Food", "Thức ăn", "खाना", "Khaana", "food"),
        VocabularyWord("Delicious", "Ngon", "स्वादिष्ट", "Swaadisht", "food"),
        VocabularyWord("Spicy", "Cay", "तीखा", "Teekha", "food"),

        // --- MEDIUM: Time ---
        VocabularyWord("Today", "Hôm nay", "आज", "Aaj", "time"),
        VocabularyWord("Tomorrow", "Ngày mai", "कल", "Kal", "time"),
        VocabularyWord("Morning", "Buổi sáng", "सुबह", "Subah", "time"),
        VocabularyWord("Night", "Buổi tối", "रात", "Raat", "time"),

        // --- ADVANCED: Emotions ---
        VocabularyWord("Happiness", "Hạnh phúc", "खुशी", "Khushi", "emotions"),
        VocabularyWord("Sadness", "Nỗi buồn", "उदासी", "Udaasi", "emotions"),
        VocabularyWord("Surprise", "Ngạc nhiên", "आश्चर्य", "Aashcharya", "emotions"),
        VocabularyWord("Anger", "Tức giận", "क्रोध", "Krodh", "emotions"),

        // --- ADVANCED: Politics ---
        VocabularyWord("Government", "Chính phủ", "सरकार", "Sarkaar", "politics"),
        VocabularyWord("Election", "Bầu cử", "चुनाव", "Chunaav", "politics"),
        VocabularyWord("Democracy", "Dân chủ", "लोकतंत्र", "Loktantra", "politics"),
        VocabularyWord("Law", "Luật pháp", "कानून", "Kanoon", "politics"),

        // --- ADVANCED: Idioms ---
        VocabularyWord("Piece of cake", "Dễ như ăn kẹo", "बाएं हाथ का खेल", "Baayein haath ka khel", "idioms"),
        VocabularyWord("To add fuel to fire", "Thêm dầu vào lửa", "आग में घी डालना", "Aag mein ghee daalna", "idioms"),
        VocabularyWord("As you sow, so shall you reap", "Gieo gió gặt bão", "जैसी करनी वैसी भरनी", "Jaisi karni waisi bharni", "idioms"),
        VocabularyWord("Empty vessels make the most noise", "Thùng rỗng kêu to", "थोथा चना बाजे घना", "Thotha chana baaje ghana", "idioms")
    )

    fun getCategoriesForLevel(level: LearningLevel): List<VocabularyCategory> {
        return categories.filter { it.level == level }
    }

    fun getWordsForCategory(categoryId: String): List<VocabularyWord> {
        return words.filter { it.categoryId == categoryId }
    }
}
