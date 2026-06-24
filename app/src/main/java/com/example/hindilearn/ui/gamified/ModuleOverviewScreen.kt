package com.example.hindilearn.ui.gamified

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.Episode
import com.example.hindilearn.data.GamifiedCurriculum
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*

fun translateTitle(title: String, isVi: Boolean): String {
    if (!isVi) return title
    return when (title) {
        // Main categories/Seasons
        "Phase 0: Foundation Academy" -> "Giai đoạn 0: Học viện nền tảng"
        "Season 1: Arrival in India" -> "Phần 1: Đến Ấn Độ"
        "Season 2: Living in India" -> "Phần 2: Sống tại Ấn Độ"
        "Season 3: Friendship" -> "Phần 3: Tình bạn"
        "Season 4: Love and Relationships" -> "Phần 4: Tình yêu & Quan hệ"
        "Season 5: Belonging" -> "Phần 5: Sự gắn kết"
        "Pronunciation Lab" -> "Luyện phát âm"
        "Speaking" -> "Giao tiếp"
        "Grammar" -> "Ngữ pháp"
        "Listening" -> "Luyện nghe"
        "Writing" -> "Luyện viết"
        "Culture" -> "Văn hóa"
        "Travel Hindi" -> "Hindi du lịch"
        "Business Hindi" -> "Hindi thương mại"
        "Bollywood Hindi" -> "Hindi Bollywood"
        "WhatsApp Hindi" -> "Hindi WhatsApp"
        "Emergency Hindi" -> "Hindi khẩn cấp"

        // Pronunciation Lab sub groups
        "Aspirated vs Unaspirated" -> "Bật hơi vs Không bật hơi"
        "Retroflex vs Dental" -> "Âm quặt lưỡi vs Âm răng"
        "Nasal Sounds" -> "Âm mũi"
        "The Schwa Deletion Rule" -> "Quy tắc nuốt âm Schwa"
        "R, L, V, and SH Sounds" -> "Các âm R, L, V, và SH"
        "Conjunct Consonants" -> "Phụ âm ghép"
        "Question Intonation & Emotion" -> "Ngữ điệu câu hỏi & Cảm xúc"
        "Minimal Pairs Challenge" -> "Thử thách cặp từ tối giản"
        "Intonation in Statements" -> "Ngữ điệu trong câu trần thuật"
        "Pronunciation Review & Test" -> "Ôn tập & Kiểm tra phát âm"
        "Geminate Consonants" -> "Phụ âm đôi"
        "Word Stress Patterns" -> "Trọng âm từ"
        "Perso-Arabic Sounds" -> "Âm gốc Ba Tư-Ả Rập"
        "Vowel Lengthening" -> "Kéo dài nguyên âm"
        "Connected Speech Flow" -> "Luồng nói liên kết"

        // Speaking sub groups
        "Greetings & Farewells" -> "Chào hỏi & Tạm biệt"
        "Introducing Yourself" -> "Giới thiệu bản thân"
        "Being Polite" -> "Lịch sự"
        "Question Words" -> "Từ để hỏi"
        "At a Restaurant" -> "Tại nhà hàng"
        "Shopping & Bargaining" -> "Mua sắm & Trả giá"
        "Formal vs Informal (Aap/Tum)" -> "Trang trọng vs Thân mật"
        "Expressing Feelings" -> "Bày tỏ cảm xúc"
        "Making Plans" -> "Lập kế hoạch"
        "Phone & Emergency" -> "Điện thoại & Khẩn cấp"
        "Ordering Food Online" -> "Đặt đồ ăn trực tuyến"
        "Advanced Conversations" -> "Hội thoại nâng cao"
        "Asking for Opinions" -> "Hỏi ý kiến"
        "Talking about Hobbies" -> "Nói về sở thích"
        "Making Excuses" -> "Viện cớ"
        "Arranging a Meeting" -> "Sắp xếp cuộc họp"
        "Giving Directions" -> "Chỉ đường"

        // Grammar sub groups
        "The Verb 'To Be'" -> "Động từ 'To Be'"
        "Sentence Structure (SOV)" -> "Cấu trúc câu (SOV)"
        "Gender System" -> "Hệ thống giống (Nam/Nữ)"
        "Pronouns & Possessives" -> "Đại từ & Sở hữu"
        "Postpositions" -> "Hậu giới từ"
        "Present Tense" -> "Thì hiện tại"
        "Past Tense & 'Ne'" -> "Thì quá khứ & 'Ne'"
        "Future Tense" -> "Thì tương lai"
        "Adjectives Agreement" -> "Sự tương hợp của tính từ"
        "Negation & Imperatives" -> "Phủ định & Mệnh lệnh"
        "Comparisons" -> "So sánh"
        "Honorifics & Compound Verbs" -> "Kính ngữ & Động từ ghép"
        "Subjunctive Mood" -> "Thức giả định"
        "Passive Voice" -> "Thể bị động"
        "Obligation & Necessity" -> "Nghĩa vụ & Sự cần thiết"
        "Ability (Sakkna)" -> "Khả năng (Sakkna)"
        "Desiderative (Chahna)" -> "Ý muốn (Chahna)"
        "Causative Verbs" -> "Động từ sai khiến"
        "Relative Clauses" -> "Mệnh đề quan hệ"

        // Listening sub groups
        "Single Words (Slow)" -> "Từ đơn (Chậm)"
        "Two-Word Phrases" -> "Cụm từ hai chữ"
        "Full Sentences" -> "Câu đầy đủ"
        "Short Dialogues" -> "Hội thoại ngắn"
        "Numbers in Context" -> "Số trong ngữ cảnh"
        "Native Speed Challenge" -> "Thử thách tốc độ bản xứ"
        "Weather Report Listening" -> "Nghe dự báo thời tiết"
        "Short News Podcasts" -> "Podcast tin tức ngắn"
        "Audio Story Comprehension" -> "Hiểu truyện qua âm thanh"
        "Eavesdropping in Public" -> "Nghe lén nơi công cộng"
        "Airport Announcements" -> "Thông báo tại sân bay"
        "Restaurant Orders Audio" -> "Nghe đặt món nhà hàng"
        "Directions Dialogue" -> "Hội thoại chỉ đường"

        // Writing sub groups
        "Tracing Vowels" -> "Tập viết nguyên âm"
        "Throat & Palate Letters" -> "Chữ phát âm họng & vòm họng"
        "Retroflex & Dental Letters" -> "Chữ quặt lưỡi & răng"
        "Lips & Semivowels Letters" -> "Chữ môi & bán nguyên âm"
        "Writing with Matras" -> "Viết với dấu nguyên âm (Matras)"
        "Writing Simple Words" -> "Viết từ đơn giản"
        "Writing Short Sentences" -> "Viết câu ngắn"
        "Writing Challenge" -> "Thử thách viết"
        "Devanagari Speed Writing" -> "Viết Devanagari nhanh"
        "Advanced Conjuncts Writing" -> "Viết từ ghép nâng cao"
        "Formatting Emails" -> "Định dạng Email"
        "Digital Keyboards" -> "Bàn phím kỹ thuật số"
        "Letter Structures" -> "Cấu trúc thư"
        "Creative Writing Prompts" -> "Gợi ý viết sáng tạo"
        "Official Applications" -> "Đơn từ chính thức"

        // Culture sub groups
        "Indian Festivals" -> "Các lễ hội Ấn Độ"
        "Indian Food Etiquette" -> "Phép lịch sự ăn uống Ấn Độ"
        "Family & Respect" -> "Gia đình & Sự tôn kính"
        "Religious Diversity" -> "Sự đa dạng tôn giáo"
        "Clothing & Attire" -> "Trang phục truyền thống"
        "Traditional Arts" -> "Nghệ thuật truyền thống"
        "Indian Weddings" -> "Đám cưới Ấn Độ"
        "Traditional Music & Dance" -> "Âm nhạc & Điệu nhảy truyền thống"
        "Cinema & Cricket" -> "Điện ảnh & Bóng chày (Cricket)"
        "Tea/Chai Culture" -> "Văn hóa trà Chai"
        "Traditional Clothing" -> "Quần áo truyền thống"
        "Bazaar Shopping Culture" -> "Văn hóa mua sắm ở chợ"

        // Travel sub groups
        "At the Airport" -> "Tại sân bay"
        "Hailing a Taxi/Auto" -> "Đón Taxi/Xe ba bánh"
        "Checking into Hotel" -> "Nhận phòng khách sạn"
        "Asking for Directions" -> "Hỏi đường đi"
        "Ordering Street Food" -> "Đặt món ăn đường phố"
        "Emergency & Health" -> "Khẩn cấp & Sức khỏe"
        "Local Sightseeing" -> "Tham quan địa phương"
        "Train Journeys" -> "Hành trình tàu hỏa"
        "Bargaining at a Bazaar" -> "Trả giá ở chợ"
        "Renting a Scooter" -> "Thuê xe máy"
        "Buying Sim Card" -> "Mua thẻ Sim"
        "Asking for Recommendations" -> "Hỏi các đề xuất"
        "Train Station Help" -> "Trợ giúp tại ga tàu"
        "Luggage Troubles" -> "Sự cố hành lý"
        "Sightseeing Queries" -> "Thắc mắc tham quan"

        // Business sub groups
        "Office Vocabulary" -> "Từ vựng văn phòng"
        "Meeting Phrases" -> "Cụm từ họp hành"
        "Email Templates" -> "Mẫu Email công việc"
        "Negotiation" -> "Đàm phán"
        "Formal Register" -> "Giao tiếp trang trọng"
        "Corporate Culture" -> "Văn hóa doanh nghiệp"
        "Job Interview in Hindi" -> "Phỏng vấn xin việc bằng tiếng Hindi"
        "Customer Service Dialogue" -> "Hội thoại dịch vụ khách hàng"
        "Pitching an Idea" -> "Thuyết trình ý tưởng"
        "Performance Reviews" -> "Đánh giá hiệu suất"
        "Client Negotiations" -> "Đàm phán với khách hàng"
        "Scheduling Conferences" -> "Lên lịch hội nghị"
        "Signing Contracts" -> "Ký kết hợp đồng"

        // Bollywood sub groups
        "Common Movie Slang" -> "Tiếng lóng phim ảnh"
        "Romantic Expressions" -> "Biểu cảm lãng mạn"
        "Iconic Dialogues" -> "Lời thoại kinh điển"
        "Song Lyrics Vocabulary" -> "Từ vựng lời bài hát"
        "Drama & Emotion" -> "Kịch tính & Cảm xúc"
        "Cinema History" -> "Lịch sử điện ảnh"
        "Classic Melodrama" -> "Kịch tâm lý kinh điển"
        "Superhit Song Analysis" -> "Phân tích bài hát nổi tiếng"
        "Award Show Expressions" -> "Biểu cảm lễ trao giải"
        "Fan Following Slang" -> "Tiếng lóng của người hâm mộ"
        "Romantic Dialogues" -> "Hội thoại lãng mạn"
        "Action Thriller Phrases" -> "Cụm từ phim hành động kịch tính"
        "Classic Item Songs" -> "Bài hát vũ đạo kinh điển"

        // WhatsApp sub groups
        "Text Abbreviations" -> "Từ viết tắt tin nhắn"
        "Hinglish Messaging" -> "Nhắn tin bằng tiếng Hinglish"
        "Emoji Etiquette" -> "Phép lịch sự dùng biểu tượng"
        "Group Chat Dynamics" -> "Tương tác trong nhóm chat"
        "Dating App Slang" -> "Tiếng lóng ứng dụng hẹn hò"
        "Social Media Buzzwords" -> "Từ ngữ phổ biến mạng xã hội"
        "Meme Vocabulary" -> "Từ vựng về ảnh chế (Meme)"
        "Virtual Birthday Wishes" -> "Chúc mừng sinh nhật online"
        "Hinglish Slang" -> "Tiếng lóng Hinglish"
        "Chat Acronyms" -> "Từ viết tắt trò chuyện"

        // Emergency sub groups
        "Medical Help" -> "Trợ giúp y tế"
        "Reporting Theft" -> "Trình báo mất trộm"
        "Lost Passport" -> "Mất hộ chiếu"
        "Pharmacy Terms" -> "Từ vựng tiệm thuốc"
        "Natural Disasters" -> "Thiên tai tự nhiên"
        "Road Accident Help" -> "Trợ giúp tai nạn đường bộ"
        "Lost Child or Person" -> "Trẻ em hoặc người bị lạc"
        "Fire Emergency" -> "Khẩn cấp hỏa hoạn"
        "Police Station Help" -> "Trợ giúp tại đồn cảnh sát"
        "First Aid Help" -> "Trợ giúp sơ cứu"

        "Numbers Lab" -> "Phòng số học"
        "Alphabet Hub" -> "Góc bảng chữ cái"
        "Numbers 0-100" -> "Số từ 0-100"
        "Hundreds 100-500" -> "Hàng trăm 100-500"
        "Hundreds 500-1000" -> "Hàng trăm 500-1000"
        "Large Numbers Practice" -> "Luyện tập số lớn"
        "Numbers Challenge" -> "Thử thách số học"
        "Vowels & Vietnamese Sounds" -> "Nguyên âm & Âm tương đương Tiếng Việt"
        "Velars & Palatals" -> "Âm họng & Âm vòm họng"
        "Retroflex & Dentals" -> "Âm quặt lưỡi & Âm răng"
        "Labials & Semivowels" -> "Âm môi & Bán nguyên âm"
        "Conjuncts & Nuqta" -> "Phụ âm ghép & Dấu phụ"

        else -> title
    }
}

fun translateDesc(desc: String, isVi: Boolean): String {
    if (!isVi) return desc
    if (desc.startsWith("Master the concepts of")) {
        val topic = desc.removePrefix("Master the concepts of ").removeSuffix(".")
        return "Luyện tập các kiến thức về ${translateTitle(topic, true)}."
    }
    if (desc.contains("Episodes available")) {
        return desc.replace("Episodes available", "Bài học có sẵn")
    }
    return when (desc) {
        "Deep dive into Pronunciation Lab." -> "Tìm hiểu chuyên sâu về Luyện phát âm."
        "Deep dive into Speaking." -> "Tìm hiểu chuyên sâu về Giao tiếp."
        "Deep dive into Grammar." -> "Tìm hiểu chuyên sâu về Ngữ pháp."
        "Deep dive into Listening." -> "Tìm hiểu chuyên sâu về Luyện nghe."
        "Deep dive into Writing." -> "Tìm hiểu chuyên sâu về Luyện viết."
        "Deep dive into Culture." -> "Tìm hiểu chuyên sâu về Văn hóa."
        "Deep dive into Travel Hindi." -> "Tìm hiểu chuyên sâu về Hindi du lịch."
        "Deep dive into Business Hindi." -> "Tìm hiểu chuyên sâu về Hindi thương mại."
        "Deep dive into Bollywood Hindi." -> "Tìm hiểu chuyên sâu về Hindi Bollywood."
        "Deep dive into WhatsApp Hindi." -> "Tìm hiểu chuyên sâu về Hindi WhatsApp."
        "Deep dive into Emergency Hindi." -> "Tìm hiểu chuyên sâu về Hindi khẩn cấp."
        "Deep dive into Numbers Lab." -> "Tìm hiểu chuyên sâu về Phòng số học."
        "Deep dive into Alphabet Hub." -> "Tìm hiểu chuyên sâu về Góc bảng chữ cái."
        "The most important phase. Learn the building blocks." -> "Giai đoạn quan trọng nhất. Học các viên gạch xây dựng nền tảng."
        else -> desc
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleOverviewScreen(
    seasonId: String,
    onEpisodeSelected: (Episode) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val seasons = GamifiedCurriculum.getSeasons()
    val season = seasons.find { it.id == seasonId } ?: seasons.first()
    val episodes = season.episodes
    val isVi = UserManager.progress.selectedLanguage == "VI"

    PremiumBackground {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text(translateTitle(season.title, isVi), color = TextDark, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header / Intro
                item {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(
                            text = translateDesc(season.description, isVi),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextDark.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isVi) "${episodes.size} Bài học có sẵn" else "${episodes.size} Episodes available",
                            style = MaterialTheme.typography.labelMedium,
                            color = DeepSaffron,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // List of episodes
                itemsIndexed(episodes) { index, episode ->
                    EpisodeListCard(
                        index = index + 1,
                        episode = episode,
                        isVi = isVi,
                        onClick = { onEpisodeSelected(episode) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
fun EpisodeListCard(
    index: Int,
    episode: Episode,
    isVi: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Index circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DeepSaffron.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = index.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepSaffron
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = translateTitle(episode.title, isVi),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = translateDesc(episode.synopsis, isVi),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDark.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Play Icon button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RoyalBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start Lesson",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

