package com.example.hindilearn.ui.gamified

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hindilearn.data.AudioHelper
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*
import java.util.Locale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale

data class StoryParagraph(
    val hindi: String,
    val english: String,
    val vietnamese: String
)

data class Story(
    val id: String,
    val titleEn: String,
    val titleVi: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionVi: String,
    val characterIcon: String,
    val imageDrawableName: String,
    val paragraphs: List<StoryParagraph>,
    val questionEn: String,
    val questionVi: String,
    val options: List<String>,
    val correctOptionIdx: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lang = UserManager.progress.selectedLanguage ?: "EN"
    val isVi = lang == "VI"
    
    var activeStory by remember { mutableStateOf<Story?>(null) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.forLanguageTag("hi-IN")
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    val stories = listOf(
        Story(
            id = "story_1",
            titleHi = "दीदी की चाय",
            titleEn = "ELDER SISTER Di's Tea Time",
            titleVi = "Giờ uống trà của chị Di",
            descriptionEn = "Learn kitchen vocabulary with ELDER SISTER Di and Jiju.",
            descriptionVi = "Học từ vựng nhà bếp cùng chị Di và anh rể Jiju.",
            characterIcon = "☕",
            imageDrawableName = "story_tea_time",
            paragraphs = listOf(
                StoryParagraph(
                    "दीदी रसोई में विशेष मसाला चाय बना रही हैं।",
                    "ELDER SISTER Di is making special masala chai in the kitchen.",
                    "Chị Di đang pha trà sữa masala đặc biệt trong bếp."
                ),
                StoryParagraph(
                    "वह अदरक, इलायची और थोड़ी चीनी डालती हैं।",
                    "She adds ginger, cardamom, and a little sugar.",
                    "Chị ấy thêm gừng, bạch đậu khấu và một ít đường."
                ),
                StoryParagraph(
                    "जीजू आते हैं और कहते हैं: मुझे भी एक cup गर्म चाय चाहिए!",
                    "Jiju comes and says: I want a cup of hot tea too!",
                    "Jiju đến và nói: Tôi cũng muốn một tách trà nóng!"
                ),
                StoryParagraph(
                    "माँ मुस्कुराती हैं और कहती हैं: चाय तैयार है, चलो साथ में पीते हैं!",
                    "Mom smiles and says: Tea is ready, let's drink together!",
                    "Mẹ mỉm cười và nói: Trà đã sẵn sàng, chúng ta cùng uống nhé!"
                )
            ),
            questionEn = "What is ELDER SISTER Di making?",
            questionVi = "Chị Di đang pha đồ uống gì?",
            options = listOf("चाय (Tea / Trà)", "कॉफ़ी (Coffee / Cà phê)", "पानी (Water / Nước)"),
            correctOptionIdx = 0
        ),
        Story(
            id = "story_2",
            titleHi = "माँ का खाना",
            titleEn = "Mom's Special Recipe",
            titleVi = "Công thức đặc biệt của Mẹ",
            descriptionEn = "Join Mom in cooking delicious Indian dishes.",
            descriptionVi = "Cùng mẹ nấu những món ăn Ấn Độ thơm ngon.",
            characterIcon = "🥘",
            imageDrawableName = "story_mom_cooking",
            paragraphs = listOf(
                StoryParagraph(
                    "माँ परिवार के लिए स्वादिष्ट खाना बना रही हैं।",
                    "Mom is cooking delicious food for the family.",
                    "Mẹ đang nấu những món ăn ngon cho cả gia đình."
                ),
                StoryParagraph(
                    "वह गर्म रोटी, पीली दाल और पनीर की सब्ज़ी बनाती हैं।",
                    "She makes hot rotis, yellow dal, and paneer sabzi.",
                    "Mẹ làm bánh roti nóng, súp đậu lăng vàng và món phô mai paneer."
                ),
                StoryParagraph(
                    "दीदी ताज़ी सब्ज़ियाँ काटने में माँ की मदद करती हैं।",
                    "ELDER SISTER Di helps mom cut the fresh vegetables.",
                    "Chị Di giúp mẹ thái rau củ tươi."
                ),
                StoryParagraph(
                    "जीजू अंदर आते हैं और कहते हैं: खुशबू बहुत अच्छी है! मुझे बहुत भूख लगी है।",
                    "Jiju walks in and says: The smell is amazing! I am very hungry.",
                    "Jiju đi vào và nói: Mùi thơm quá! Tôi đang rất đói."
                )
            ),
            questionEn = "Who is cooking the food?",
            questionVi = "Ai đang nấu ăn?",
            options = listOf("जीजू (Jiju / Anh rể)", "माँ (Mom / Mẹ)", "दीदी (Didi / Chị gái)"),
            correctOptionIdx = 1
        ),
        Story(
            id = "story_3",
            titleHi = "जीजू की यात्रा",
            titleEn = "The Greatman JIJU's Adventure",
            titleVi = "Cuộc phiêu lưu của Jiju tuyệt vời",
            descriptionEn = "Jiju travels to Agra to visit the Taj Mahal.",
            descriptionVi = "Jiju đi du lịch đến Agra để ngắm đền Taj Mahal.",
            characterIcon = "🚄",
            imageDrawableName = "story_jiju_taj",
            paragraphs = listOf(
                StoryParagraph(
                    "महान जीजू आज ताजमहल देखने के लिए आगरा जा रहे हैं।",
                    "The greatman Jiju is traveling to Agra today to see the Taj Mahal.",
                    "Jiju tuyệt vời hôm nay đang đi du lịch đến Agra để ngắm Taj Mahal."
                ),
                StoryParagraph(
                    "वह दिल्ली स्टेशन से सुबह की ट्रेन में चढ़ते हैं।",
                    "He boards the early morning train from Delhi station.",
                    "Anh ấy lên chuyến tàu sớm vào buổi sáng từ ga Delhi."
                ),
                StoryParagraph(
                    "ट्रेन में, जीजू एक स्थानीय यात्री से बात करते हैं और अपनी हिंदी का अभ्यास करते हैं।",
                    "On the train, Jiju talks to a local traveler and practices his Hindi.",
                    "Trên tàu, Jiju nói chuyện với một du khách địa phương và thực hành tiếng Hindi."
                ),
                StoryParagraph(
                    "दीदी उन्हें फोन करके पूछती हैं: जीजू, क्या आप आगरा पहुँच गए?",
                    "ELDER SISTER Di calls him to check: Jiju, did you reach Agra?",
                    "Chị Di gọi điện kiểm tra: Jiju, anh đã đến Agra chưa?"
                )
            ),
            questionEn = "Where is Jiju going?",
            questionVi = "Jiju đang đi du lịch ở đâu?",
            options = listOf("दिल्ली (Delhi / Delhi)", "आगरा (Agra / Agra)", "मुंबई (Mumbai / Mumbai)"),
            correctOptionIdx = 1
        ),
        Story(
            id = "story_4",
            titleHi = "पारिवारिक मिलन",
            titleEn = "Family Gathering",
            titleVi = "Quây quanh gia đình",
            descriptionEn = "A happy Sunday in the family living room.",
            descriptionVi = "Một ngày Chủ nhật hạnh phúc trong phòng khách gia đình.",
            characterIcon = "🏡",
            imageDrawableName = "story_family_sunday",
            paragraphs = listOf(
                StoryParagraph(
                    "आज एक सुंदर रविवार है और परिवार बैठक में इकट्ठा हुआ है।",
                    "Today is a beautiful Sunday and the family is gathered in the living room.",
                    "Hôm nay là một ngày Chủ nhật đẹp trời và cả gia đình đang quây quần trong phòng khách."
                ),
                StoryParagraph(
                    "माँ सोफे पर बैठी हैं और हिंदी की किताब पढ़ रही हैं।",
                    "Mom is sitting on the sofa and reading a Hindi book.",
                    "Mẹ đang ngồi trên ghế sofa và đọc một cuốn sách tiếng Hindi."
                ),
                StoryParagraph(
                    "दीदी और जीजू साथ में एक बोर्ड गेम खेल रहे हैं।",
                    "ELDER SISTER Di and Jiju are playing a board game together.",
                    "Chị Di và Jiju đang chơi cờ cùng nhau."
                ),
                StoryParagraph(
                    "सब लोग हँस रहे हैं और खुशी की कहानियाँ साझा कर रहे हैं।",
                    "Everyone is laughing and sharing happy stories.",
                    "Mọi người đang cười nói vui vẻ và chia sẻ những câu chuyện hạnh phúc."
                )
            ),
            questionEn = "What day is it today?",
            questionVi = "Hôm nay là ngày thứ mấy?",
            options = listOf("रविवार (Sunday / Chủ nhật)", "सोमवार (Monday / Thứ hai)", "शनिवार (Saturday / Thứ bảy)"),
            correctOptionIdx = 0
        ),
        Story(
            id = "story_5",
            titleHi = "दीदी का जन्मदिन",
            titleEn = "Didi's Surprise Birthday",
            titleVi = "Sinh nhật bất ngờ của chị Di",
            descriptionEn = "Help Jiju and Mom plan a surprise birthday party for Didi.",
            descriptionVi = "Giúp Jiju và Mẹ lên kế hoạch cho bữa tiệc sinh nhật bất ngờ của chị Di.",
            characterIcon = "🎂",
            imageDrawableName = "story_didi_birthday",
            paragraphs = listOf(
                StoryParagraph(
                    "दीदी का जन्मदिन है और जीजू कुछ विशेष करना चाहते हैं।",
                    "It is Didi's birthday and Jiju wants to do something special.",
                    "Hôm nay là sinh nhật của chị Di và Jiju muốn làm điều gì đó đặc biệt."
                ),
                StoryParagraph(
                    "वह माँ के साथ मिलकर एक सुंदर केक बनाते हैं।",
                    "He together with Mom bakes a beautiful cake.",
                    "Anh ấy cùng với Mẹ nướng một chiếc bánh kem thật đẹp."
                ),
                StoryParagraph(
                    "कमरे को गुब्बारों और रंग-बिरंगे बैनरों से सजाया जाता है।",
                    "The room is decorated with balloons and colorful banners.",
                    "Căn phòng được trang trí bằng bóng bay và các biểu ngữ đầy màu sắc."
                ),
                StoryParagraph(
                    "जब दीदी घर आती हैं, तो सब चिल्लाते हैं: जन्मदिन मुबारक हो!",
                    "When Didi comes home, everyone shouts: Happy Birthday!",
                    "Khi chị Di về đến nhà, mọi người reo hò: Chúc mừng sinh nhật!"
                )
            ),
            questionEn = "What do Jiju and Mom bake for Didi?",
            questionVi = "Jiju và Mẹ nướng cái gì cho chị Di?",
            options = listOf("केक (Cake / Bánh kem)", "समोसा (Samosa / Bánh gối Samosa)", "चाय (Tea / Trà)"),
            correctOptionIdx = 0
        ),
        Story(
            id = "story_6",
            titleHi = "बाज़ार की सैर",
            titleEn = "A Trip to the Indian Market",
            titleVi = "Chuyến tham quan chợ Ấn Độ",
            descriptionEn = "Jiju and Didi go shopping for spices and mangoes.",
            descriptionVi = "Jiju và chị Di đi mua sắm gia vị và xoài ở chợ.",
            characterIcon = "🛍️",
            imageDrawableName = "story_indian_market",
            paragraphs = listOf(
                StoryParagraph(
                    "जीजू और दीदी ताज़े फल खरीदने के लिए भारतीय बाज़ार जाते हैं।",
                    "Jiju and Didi go to the Indian market to buy fresh fruits.",
                    "Jiju và chị Di đến chợ Ấn Độ để mua trái cây tươi."
                ),
                StoryParagraph(
                    "दुकानदार बहुत सारे मीठे और ताज़े आम दिखाता है।",
                    "The shopkeeper shows many sweet and fresh mangoes.",
                    "Người bán hàng giới thiệu rất nhiều xoài ngọt và tươi."
                ),
                StoryParagraph(
                    "जीजू पूछते हैं: भाई साहब, ये आम कैसे दिए?",
                    "Jiju asks: Brother, how much are these mangoes?",
                    "Jiju hỏi: Anh ơi, những quả xoài này giá bao nhiêu?"
                ),
                StoryParagraph(
                    "दीदी कुछ ताजे मसाले भी खरीदती हैं जैसे हल्दी और लाल मिर्च।",
                    "Didi also buys some fresh spices like turmeric and red chili.",
                    "Chị Di cũng mua một số gia vị tươi như nghệ và ớt đỏ."
                )
            ),
            questionEn = "What fruit does the shopkeeper show to Jiju and Didi?",
            questionVi = "Người bán hàng giới thiệu loại trái cây nào cho Jiju và chị Di?",
            options = listOf("आम (Mango / Xoài)", "सेब (Apple / Táo)", "केला (Banana / Chuối)"),
            correctOptionIdx = 0
        )
    )

    PremiumBackground {
        Crossfade(targetState = activeStory, label = "ReaderTransition") { story ->
            if (story == null) {
                // List Mode
                Scaffold(
                    modifier = modifier,
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            title = { Text(if (isVi) "Góc kể chuyện" else "Stories Center", color = TextDark, fontWeight = FontWeight.Bold) },
                            navigationIcon = {
                                IconButton(onClick = onBack) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextDark)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                        )
                    }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(
                            items = stories,
                            key = { _, item -> item.id }
                        ) { _, item ->
                            StoryListCard(
                                story = item,
                                isVi = isVi,
                                onClick = { activeStory = item }
                            )
                        }
                    }
                }
            } else {
                // Reader Mode
                ReaderView(
                    story = story,
                    isVi = isVi,
                    tts = tts,
                    onClose = { activeStory = null }
                )
            }
        }
    }
}

@Composable
fun StoryListCard(
    story: Story,
    isVi: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            val imageRes = remember(story.imageDrawableName) {
                context.resources.getIdentifier(story.imageDrawableName, "drawable", context.packageName)
            }
            if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(DeepSaffron.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(story.characterIcon, fontSize = 28.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    story.titleHi,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepSaffron
                )
                Text(
                    if (isVi) story.titleVi else story.titleEn,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    if (isVi) story.descriptionVi else story.descriptionEn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDark.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RoyalBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Read", tint = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderView(
    story: Story,
    isVi: Boolean,
    tts: TextToSpeech?,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedAnswerIdx by remember { mutableStateOf<Int?>(null) }
    var answerChecked by remember { mutableStateOf(false) }
    var showQuizFeedback by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(if (isVi) story.titleVi else story.titleEn, color = TextDark, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        story.titleHi,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = DeepSaffron,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (isVi) story.titleVi else story.titleEn,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        textAlign = TextAlign.Center
                    )

                    val context = LocalContext.current
                    val imageRes = remember(story.imageDrawableName) {
                        context.resources.getIdentifier(story.imageDrawableName, "drawable", context.packageName)
                    }
                    if (imageRes != 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Paragraph List
            itemsIndexed(
                items = story.paragraphs,
                key = { idx, _ -> "${story.id}_p_${idx}" }
            ) { _, p ->
                ParagraphCard(
                    para = p,
                    isVi = isVi,
                    onClickAudio = {
                        AudioHelper.playAudio(
                            context = context,
                            audioKey = "",
                            tts = tts,
                            textFallback = p.hindi
                        )
                    }
                )
            }

            // Comprehension Quiz
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (isVi) "Kiểm tra đọc hiểu" else "Reading Comprehension",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepSaffron
                )
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = if (isVi) story.questionVi else story.questionEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        story.options.forEachIndexed { idx, option ->
                            val isSelected = selectedAnswerIdx == idx
                            val cardColor = when {
                                answerChecked && idx == story.correctOptionIdx -> SoftGreen
                                answerChecked && isSelected && idx != story.correctOptionIdx -> SoftRed
                                isSelected -> DeepSaffron.copy(alpha = 0.2f)
                                else -> Color.White.copy(alpha = 0.5f)
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable(enabled = !answerChecked) { selectedAnswerIdx = idx },
                                colors = CardDefaults.cardColors(containerColor = cardColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { if (!answerChecked) selectedAnswerIdx = idx },
                                        enabled = !answerChecked,
                                        colors = RadioButtonDefaults.colors(selectedColor = DeepSaffron)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = TextDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!answerChecked) {
                            PremiumButton(
                                text = "Check Answer",
                                onClick = {
                                    if (selectedAnswerIdx != null) {
                                        answerChecked = true
                                        showQuizFeedback = true
                                    }
                                },
                                enabled = selectedAnswerIdx != null
                            )
                        } else {
                            val isCorrect = selectedAnswerIdx == story.correctOptionIdx
                            val feedbackMsg = if (isCorrect) {
                                if (isVi) "Chính xác! +10 XP đã được cộng." else "Correct! +10 XP has been registered."
                            } else {
                                if (isVi) "Không chính xác. Hãy tiếp tục học tập!" else "Incorrect. Keep practicing!"
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = feedbackMsg,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCorrect) SoftGreen else SoftRed,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                PremiumButton(
                                    text = "Finish Story",
                                    onClick = {
                                        if (isCorrect) {
                                            UserManager.addXp(10)
                                        }
                                        onClose()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParagraphCard(
    para: StoryParagraph,
    isVi: Boolean,
    onClickAudio: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = para.hindi,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isVi) para.vietnamese else para.english,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextDark.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                onClick = onClickAudio,
                modifier = Modifier
                    .size(48.dp)
                    .background(DeepSaffron.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Speak line",
                    tint = DeepSaffron
                )
            }
        }
    }
}
