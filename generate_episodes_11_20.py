import json
import os

episodes_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(episodes_dir, exist_ok=True)

# Define data for episodes 11 to 20
episodes_data = {
    11: {
        "title": "Lesson 11: Travel by Train",
        "description": "Chào mừng đến ga! Hãy học các từ vựng và mẫu câu cơ bản để đi lại bằng tàu hỏa ở Ấn Độ.",
        "keyPoints": [
            "स्टेशन (station): Ga tàu",
            "प्लेटफॉर्म (platform): Sân ga",
            "ट्रेन (train): Tàu hỏa",
            "टिकट (ticket): Vé"
        ],
        "rule_title": "Cách hỏi vị trí sân ga hoặc ga tàu",
        "rule_explanation": "Để hỏi vị trí của ga tàu hoặc sân ga, bạn sử dụng cấu trúc: [Danh từ] + कहाँ है? (kahan hai? - ở đâu?). Ví dụ: प्लेटफॉर्म कहाँ है? (Sân ga ở đâu?)",
        "rule_simple": "[Vật/Nơi chốn] + कहाँ है? = ... ở đâu?",
        "flashcards": [
            {"hindi": "स्टेशन", "transliteration": "station", "english": "Station", "vietnamese": "Ga tàu"},
            {"hindi": "प्लेटफॉर्म", "transliteration": "platform", "english": "Platform", "vietnamese": "Sân ga"},
            {"hindi": "ट्रेन", "transliteration": "train", "english": "Train", "vietnamese": "Tàu hỏa"},
            {"hindi": "टिकट", "transliteration": "ticket", "english": "Ticket", "vietnamese": "Vé"}
        ],
        "vocab_context": {
            "word": "प्लेटफॉर्म",
            "translation": "Platform / Sân ga",
            "contextSentenceHindi": "मेरा प्लेटफॉर्म कहाँ है?",
            "contextSentenceTranslation": "Where is my platform? / Sân ga của tôi ở đâu?",
            "memoryTrick": "Platform phát âm giống tiếng Anh, chỉ cần viết theo ký tự Devanagari!"
        },
        "mc": {
            "text": "स्टेशन",
            "subtext": "station",
            "answer": "Station",
            "answer_vi": "Ga tàu",
            "options": ["Station", "Train", "Ticket", "Platform"],
            "options_vi": ["Ga tàu", "Tàu hỏa", "Vé", "Sân ga"]
        },
        "listening": {
            "audio": "ट्रेन",
            "translation_en": "Train",
            "options_en": ["Train", "Station", "Ticket", "Platform"],
            "translation_vi": "Tàu hỏa",
            "options_vi": ["Tàu hỏa", "Ga tàu", "Vé", "Sân ga"]
        },
        "sb": {
            "englishSentence": "Build: 'Where is the train?' / Dịch: 'Tàu hỏa ở đâu?'",
            "hindiWords": ["कहाँ", "ट्रेन", "है"],
            "correctHindiSentence": "ट्रेन कहाँ है"
        },
        "match": [
            {"hindi": "स्टेशन", "english": "Station (Ga tàu)"},
            {"hindi": "प्लेटफॉर्म", "english": "Platform (Sân ga)"},
            {"hindi": "ट्रेन", "english": "Train (Tàu hỏa)"},
            {"hindi": "टिकट", "english": "Ticket (Vé)"}
        ],
        "takeaways": [
            "Đã học cách hỏi vị trí sân ga và tàu hỏa.",
            "Nắm vững các danh từ: स्टेशन, प्लेटफॉर्म, ट्रेन, टिकट.",
            "Tự tin giao tiếp cơ bản tại ga tàu Ấn Độ."
        ]
    },
    12: {
        "title": "Lesson 12: Pronouns - Advanced",
        "description": "Học cách sử dụng các đại từ sở hữu nâng cao: Của tôi, Của bạn, Của chúng tôi trong tiếng Hindi.",
        "keyPoints": [
            "मेरा / मेरी / मेरे (mera/meri/mere): Của tôi",
            "आपका / आपकी / आपके (aapka/aapki/aapke): Của bạn",
            "हमारा / हमारी / हमारे (hamara/hamari/hamare): Của chúng tôi"
        ],
        "rule_title": "Sự hòa hợp giống của đại từ sở hữu",
        "rule_explanation": "Đại từ sở hữu trong tiếng Hindi thay đổi đuôi dựa trên giống và số của danh từ đi sau nó. Kết thúc bằng '-aa' cho giống đực số ít, '-ee' cho giống cái, và '-e' cho giống đực số nhiều hoặc thể hiện sự kính trọng.",
        "rule_simple": "मेरा (đực) / मेरी (cái) / मेरे (kính trọng/nhiều)",
        "flashcards": [
            {"hindi": "मेरा", "transliteration": "mera", "english": "Mine (masculine)", "vietnamese": "Của tôi (giống đực)"},
            {"hindi": "मेरी", "transliteration": "meri", "english": "Mine (feminine)", "vietnamese": "Của tôi (giống cái)"},
            {"hindi": "आपका", "transliteration": "aapka", "english": "Yours (formal)", "vietnamese": "Của bạn (lịch sự)"},
            {"hindi": "हमारा", "transliteration": "hamara", "english": "Ours", "vietnamese": "Của chúng tôi"}
        ],
        "vocab_context": {
            "word": "हमारा",
            "translation": "Ours / Của chúng tôi",
            "contextSentenceHindi": "यह हमारा घर है।",
            "contextSentenceTranslation": "This is our house. / Đây là nhà của chúng tôi.",
            "memoryTrick": "Hamara bắt đầu bằng 'Ham' giống như 'Chúng tôi/Chúng ta' (हम - hum)!"
        },
        "mc": {
            "text": "आपका",
            "subtext": "aapka",
            "answer": "Yours (formal)",
            "answer_vi": "Của bạn (lịch sự)",
            "options": ["Yours (formal)", "Mine", "Ours", "His"],
            "options_vi": ["Của bạn (lịch sự)", "Của tôi", "Của chúng tôi", "Của anh ấy"]
        },
        "listening": {
            "audio": "मेरी",
            "translation_en": "Mine (feminine)",
            "options_en": ["Mine (feminine)", "Ours", "Yours", "Theirs"],
            "translation_vi": "Của tôi (giống cái)",
            "options_vi": ["Của tôi (giống cái)", "Của chúng tôi", "Của bạn", "Của họ"]
        },
        "sb": {
            "englishSentence": "Build: 'This is my house.' / Dịch: 'Đây là nhà của tôi.'",
            "hindiWords": ["है", "यह", "मेरा", "घर"],
            "correctHindiSentence": "यह मेरा घर है"
        },
        "match": [
            {"hindi": "मेरा", "english": "Mine / Của tôi"},
            {"hindi": "आपका", "english": "Yours / Của bạn"},
            {"hindi": "हमारा", "english": "Ours / Của chúng tôi"},
            {"hindi": "घर", "english": "House / Nhà"}
        ],
        "takeaways": [
            "Hiểu cách biến đổi đại từ sở hữu theo giống của danh từ.",
            "Sử dụng thành thạo: मेरा/मेरी, आपका/आपकी, हमारा/हमारी.",
            "Biết cách ghép câu sở hữu đơn giản."
        ]
    },
    13: {
        "title": "Lesson 13: Family Relationships",
        "description": "Học cách gọi tên các thành viên trong gia đình: Anh trai, Em gái và Họ hàng bằng tiếng Hindi.",
        "keyPoints": [
            "बड़ा भाई (bada bhai): Anh trai",
            "छोटी बहन (chhoti behen): Em gái",
            "रिश्तेदार (rishtedaar): Họ hàng"
        ],
        "rule_title": "Tính từ bổ nghĩa cho thành viên gia đình",
        "rule_explanation": "Sử dụng बड़ा (bada - lớn) hoặc छोटा (chhota - nhỏ) trước từ chỉ anh chị em để biểu thị lớn hơn hoặc nhỏ hơn. Chúng biến đổi thành बड़ी (badi) và छोटी (chhoti) khi đi với danh từ giống cái.",
        "rule_simple": "बड़ा (lớn) / छोटा (nhỏ) + भाई (anh em) / बहन (chị em)",
        "flashcards": [
            {"hindi": "बड़ा भाई", "transliteration": "bada bhai", "english": "Elder brother", "vietnamese": "Anh trai"},
            {"hindi": "छोटी बहन", "transliteration": "chhoti behen", "english": "Younger sister", "vietnamese": "Em gái"},
            {"hindi": "रिश्तेदार", "transliteration": "rishtedaar", "english": "Relatives", "vietnamese": "Họ hàng"},
            {"hindi": "परिवार", "transliteration": "parivaar", "english": "Family", "vietnamese": "Gia đình"}
        ],
        "vocab_context": {
            "word": "रिश्तेदार",
            "translation": "Relatives / Họ hàng",
            "contextSentenceHindi": "वे मेरे रिश्तेदार हैं।",
            "contextSentenceTranslation": "They are my relatives. / Họ là họ hàng của tôi.",
            "memoryTrick": "Rishtedaar có từ 'Rishta' nghĩa là mối quan hệ, thêm 'daar' để chỉ người!"
        },
        "mc": {
            "text": "छोटी बहन",
            "subtext": "chhoti behen",
            "answer": "Younger sister",
            "answer_vi": "Em gái",
            "options": ["Younger sister", "Elder brother", "Mother", "Father"],
            "options_vi": ["Em gái", "Anh trai", "Mẹ", "Bố"]
        },
        "listening": {
            "audio": "बड़ा भाई",
            "translation_en": "Elder brother",
            "options_en": ["Elder brother", "Younger brother", "Elder sister", "Younger sister"],
            "translation_vi": "Anh trai",
            "options_vi": ["Anh trai", "Em trai", "Chị gái", "Em gái"]
        },
        "sb": {
            "englishSentence": "Build: 'He is my elder brother.' / Dịch: 'Anh ấy là anh trai tôi.'",
            "hindiWords": ["बड़ा", "वह", "मेरा", "है", "भाई"],
            "correctHindiSentence": "वह मेरा बड़ा भाई hai"
        },
        "match": [
            {"hindi": "बड़ा भाई", "english": "Elder brother (Anh trai)"},
            {"hindi": "छोटी बहन", "english": "Younger sister (Em gái)"},
            {"hindi": "रिश्तेदार", "english": "Relatives (Họ hàng)"},
            {"hindi": "परिवार", "english": "Family (Gia đình)"}
        ],
        "takeaways": [
            "Biết phân biệt anh/em, chị/em bằng cách dùng bada/chhota.",
            "Học từ vựng về họ hàng (रिश्तेदार) và gia đình (परिवार).",
            "Đặt được câu giới thiệu các thành viên trong gia đình."
        ]
    },
    14: {
        "title": "Lesson 14: Culture - Indian Spices",
        "description": "Khám phá thế giới gia vị Ấn Độ! Học tên gọi các loại gia vị phổ biến: Nghệ, Thì là, Bạch đậu khấu.",
        "keyPoints": [
            "मसाले (masale): Gia vị",
            "हल्दी (haldi): Nghệ",
            "जीरा (jeera): Thì là",
            "इलायची (elaichi): Bạch đậu khấu"
        ],
        "rule_title": "Danh từ số nhiều của gia vị",
        "rule_explanation": "Từ 'masala' (gia vị - số ít) chuyển thành 'masale' (các gia vị - số nhiều). Nhiều loại gia vị Ấn Độ mang giống cái như Haldi và Elaichi.",
        "rule_simple": "मसाला (số ít) → मसाले (số nhiều)",
        "flashcards": [
            {"hindi": "मसाले", "transliteration": "masale", "english": "Spices", "vietnamese": "Gia vị"},
            {"hindi": "हल्दी", "transliteration": "haldi", "english": "Turmeric", "vietnamese": "Nghệ"},
            {"hindi": "जीरा", "transliteration": "jeera", "english": "Cumin", "vietnamese": "Thì là"},
            {"hindi": "इलायची", "transliteration": "elaichi", "english": "Cardamom", "vietnamese": "Bạch đậu khấu"}
        ],
        "vocab_context": {
            "word": "हल्दी",
            "translation": "Turmeric / Nghệ",
            "contextSentenceHindi": "हल्दी पीली होती है।",
            "contextSentenceTranslation": "Turmeric is yellow. / Nghệ có màu vàng.",
            "memoryTrick": "Haldi - hãy nhớ đến lễ hội cưới Haldi nổi tiếng của Ấn Độ, nơi mọi người bôi bột nghệ vàng lên mặt!"
        },
        "mc": {
            "text": "जीरा",
            "subtext": "jeera",
            "answer": "Cumin",
            "answer_vi": "Thì là",
            "options": ["Cumin", "Turmeric", "Cardamom", "Salt"],
            "options_vi": ["Thì là", "Nghệ", "Bạch đậu khấu", "Muối"]
        },
        "listening": {
            "audio": "इलायची",
            "translation_en": "Cardamom",
            "options_en": ["Cardamom", "Cumin", "Turmeric", "Pepper"],
            "translation_vi": "Bạch đậu khấu",
            "options_vi": ["Bạch đậu khấu", "Thì là", "Nghệ", "Tiêu"]
        },
        "sb": {
            "englishSentence": "Build: 'Spices are good.' / Dịch: 'Gia vị rất tốt.'",
            "hindiWords": ["अच्छे", "मसाले", "हैं"],
            "correctHindiSentence": "मसाले अच्छे हैं"
        },
        "match": [
            {"hindi": "हल्दी", "english": "Turmeric (Nghệ)"},
            {"hindi": "जीरा", "english": "Cumin (Thì là)"},
            {"hindi": "इलायची", "english": "Cardamom (Bạch đậu khấu)"},
            {"hindi": "मसाले", "english": "Spices (Gia vị)"}
        ],
        "takeaways": [
            "Nhận biết các loại gia vị cơ bản trong ẩm thực Ấn Độ.",
            "Học cách phát âm chính xác các từ: हल्दी, जीरा, इलायची.",
            "Hiểu thêm về tầm quan trọng của gia vị trong văn hóa Ấn."
        ]
    },
    15: {
        "title": "Lesson 15: Food - Sweet Delights",
        "description": "Trải nghiệm thế giới đồ ngọt Ấn Độ! Học về các món tráng miệng nổi tiếng: Gulab Jamun, Jalebi và tính từ 'Ngọt'.",
        "keyPoints": [
            "मीठा (meetha): Ngọt",
            "मिठाई (mithai): Bánh kẹo/Đồ ngọt",
            "गुलाब जामुन (gulab jamun): Bánh sữa chiên",
            "जलेबी (jalebi): Kẹo vòng"
        ],
        "rule_title": "Tính từ miêu tả hương vị đồ ăn",
        "rule_explanation": "Tính từ मीठा (meetha - ngọt) thay đổi theo giống của món ăn. Ví dụ: गुलाब जामुन मीठा है (Gulab Jamun ngọt - giống đực) nhưng जलेबी मीठी है (Jalebi ngọt - giống cái).",
        "rule_simple": "मीठा (đực) / मीठी (cái) = Ngọt",
        "flashcards": [
            {"hindi": "मीठा", "transliteration": "meetha", "english": "Sweet", "vietnamese": "Ngọt"},
            {"hindi": "मिठाई", "transliteration": "mithai", "english": "Sweet/Dessert", "vietnamese": "Đồ ngọt/Bánh kẹo"},
            {"hindi": "गुलाब जामुन", "transliteration": "gulab jamun", "english": "Gulab Jamun", "vietnamese": "Bánh sữa chiên nước đường"},
            {"hindi": "जलेबी", "transliteration": "jalebi", "english": "Jalebi", "vietnamese": "Kẹo vòng chiên giòn"}
        ],
        "vocab_context": {
            "word": "जलेबी",
            "translation": "Jalebi / Kẹo vòng",
            "contextSentenceHindi": "जलेबी बहुत मीठी है।",
            "contextSentenceTranslation": "Jalebi is very sweet. / Jalebi rất ngọt.",
            "memoryTrick": "Jalebi có hình xoắn ốc tròn, phát âm gần giống như 'Jelly' nhưng giòn và ngọt lịm!"
        },
        "mc": {
            "text": "गुलाब जामुन",
            "subtext": "gulab jamun",
            "answer": "Gulab Jamun",
            "answer_vi": "Bánh sữa chiên",
            "options": ["Gulab Jamun", "Jalebi", "Salty", "Spicy"],
            "options_vi": ["Bánh sữa chiên", "Kẹo vòng", "Mặn", "Cay"]
        },
        "listening": {
            "audio": "मीठा",
            "translation_en": "Sweet",
            "options_en": ["Sweet", "Sour", "Bitter", "Spicy"],
            "translation_vi": "Ngọt",
            "options_vi": ["Ngọt", "Chua", "Đắng", "Cay"]
        },
        "sb": {
            "englishSentence": "Build: 'I like sweets.' / Dịch: 'Tôi thích đồ ngọt.'",
            "hindiWords": ["पसंद", "मिठाई", "मुझे", "है"],
            "correctHindiSentence": "मुझे मिठाई पसंद है"
        },
        "match": [
            {"hindi": "मीठा", "english": "Sweet (Ngọt)"},
            {"hindi": "मिठाई", "english": "Dessert (Đồ ngọt)"},
            {"hindi": "गुलाब जामुन", "english": "Gulab Jamun (Bánh sữa chiên)"},
            {"hindi": "जलेबी", "english": "Jalebi (Kẹo vòng)"}
        ],
        "takeaways": [
            "Biết tên các món ngọt truyền thống Ấn Độ.",
            "Học cách diễn tả vị ngọt bằng meetha/meethi phù hợp với giống danh từ.",
            "Tự tin gọi món tráng miệng khi ăn đồ Ấn."
        ]
    },
    16: {
        "title": "Lesson 16: Grammar - Present Continuous",
        "description": "Học cách diễn đạt hành động đang xảy ra trong hiện tại: Tôi đang đọc, Bạn đang làm gì?",
        "keyPoints": [
            "रहा है / रही है (raha hai / rahi hai): Đang làm gì (số ít)",
            "रहा हूँ / रही हूँ (raha hoon / rahi hoon): Tôi đang làm gì",
            "रहे हैं (rahe hain): Đang làm gì (số nhiều/lịch sự)"
        ],
        "rule_title": "Thì hiện tại tiếp diễn (Present Continuous)",
        "rule_explanation": "Công thức: [Động từ căn bản] + रहा/रही/रहे + [Động từ Aux (huan/hai/ho/hain)]. Chọn रहा (raha) cho nam số ít, रही (rahi) cho nữ, và रहे (rahe) cho nam số nhiều hoặc thể hiện sự lịch sự.",
        "rule_simple": "Verb root + रहा/रही/रहे + aux verb = Đang làm gì...",
        "flashcards": [
            {"hindi": "पढ़ना", "transliteration": "padhna", "english": "To read", "vietnamese": "Đọc"},
            {"hindi": "लिखना", "transliteration": "likhna", "english": "To write", "vietnamese": "Viết"},
            {"hindi": "करना", "transliteration": "karna", "english": "To do", "vietnamese": "Làm"},
            {"hindi": "देखना", "transliteration": "dekhna", "english": "To see/watch", "vietnamese": "Nhìn/Xem"}
        ],
        "vocab_context": {
            "word": "पढ़ रहा",
            "translation": "Reading (masculine) / Đang đọc (nam)",
            "contextSentenceHindi": "मैं किताब पढ़ रहा हूँ।",
            "contextSentenceTranslation": "I am reading a book. / Tôi đang đọc sách.",
            "memoryTrick": "Từ gốc पढ़ना (đọc). Bỏ đuôi 'na' thành 'padh', thêm 'raha hoon' nếu bạn là nam!"
        },
        "mc": {
            "text": "आप क्या कर रहे हैं?",
            "subtext": "aap kya kar rahe hain?",
            "answer": "What are you doing?",
            "answer_vi": "Bạn đang làm gì?",
            "options": ["What are you doing?", "Where are you going?", "Who is reading?", "What is this?"],
            "options_vi": ["Bạn đang làm gì?", "Bạn đang đi đâu?", "Ai đang đọc?", "Đây là cái gì?"]
        },
        "listening": {
            "audio": "लिख रहा",
            "translation_en": "Writing (masculine)",
            "options_en": ["Writing (masculine)", "Reading", "Sleeping", "Doing"],
            "translation_vi": "Đang viết (nam)",
            "options_vi": ["Đang viết (nam)", "Đang đọc", "Đang ngủ", "Đang làm"]
        },
        "sb": {
            "englishSentence": "Build: 'I am reading.' (masc) / Dịch: 'Tôi đang đọc.' (nam)",
            "hindiWords": ["हूँ", "रहा", "मैं", "पढ़"],
            "correctHindiSentence": "मैं पढ़ रहा हूँ"
        },
        "match": [
            {"hindi": "पढ़ना", "english": "To read (Đọc)"},
            {"hindi": "लिखना", "english": "To write (Viết)"},
            {"hindi": "करना", "english": "To do (Làm)"},
            {"hindi": "देखना", "english": "To see (Xem)"}
        ],
        "takeaways": [
            "Hiểu cấu trúc thì hiện tại tiếp diễn trong tiếng Hindi.",
            "Sử dụng đúng रहा/रही/रहे tùy thuộc vào giới tính chủ ngữ.",
            "Hỏi và trả lời được câu hỏi 'Bạn đang làm gì?'."
        ]
    },
    17: {
        "title": "Lesson 17: Family - Uncle and Aunt",
        "description": "Làm quen với hệ thống đại gia đình Ấn Độ! Học cách gọi tên Chú, Thím, Cậu, Mợ (Chacha, Chachi, Mama, Mami).",
        "keyPoints": [
            "चाचा (chacha): Chú (em trai của bố)",
            "चाची (chachi): Thím (vợ của chacha)",
            "मामा (mama): Cậu (anh em trai của mẹ)",
            "मामी (mami): Mợ (vợ của mama)"
        ],
        "rule_title": "Biến đổi đuôi từ chỉ mối quan hệ",
        "rule_explanation": "Trong tiếng Hindi, các danh từ chỉ người thân nam kết thúc bằng '-aa' thường có từ chỉ người thân nữ tương ứng kết thúc bằng '-ee'. Ví dụ: Chacha (chú) → Chachi (thím), Mama (cậu) → Mami (mợ).",
        "rule_simple": "-aa (nam) → -ee (nữ) tương ứng",
        "flashcards": [
            {"hindi": "चाचा", "transliteration": "chacha", "english": "Paternal Uncle", "vietnamese": "Chú (em bố)"},
            {"hindi": "चाची", "transliteration": "chachi", "english": "Paternal Aunt", "vietnamese": "Thím (vợ chú)"},
            {"hindi": "मामा", "transliteration": "mama", "english": "Maternal Uncle", "vietnamese": "Cậu (em mẹ)"},
            {"hindi": "मामी", "transliteration": "mami", "english": "Maternal Aunt", "vietnamese": "Mợ (vợ cậu)"}
        ],
        "vocab_context": {
            "word": "मामा",
            "translation": "Maternal Uncle / Cậu",
            "contextSentenceHindi": "मेरे मामा दिल्ली में रहते हैं।",
            "contextSentenceTranslation": "My maternal uncle lives in Delhi. / Cậu của tôi sống ở Delhi.",
            "memoryTrick": "Mama viết giống như 'Mẹ' trong tiếng Anh nhưng có nghĩa là Cậu (bên ngoại) trong tiếng Hindi!"
        },
        "mc": {
            "text": "चाची",
            "subtext": "chachi",
            "answer": "Paternal Aunt",
            "answer_vi": "Thím (vợ chú)",
            "options": ["Paternal Aunt", "Paternal Uncle", "Maternal Aunt", "Mother"],
            "options_vi": ["Thím (vợ chú)", "Chú (em bố)", "Mợ (vợ cậu)", "Mẹ"]
        },
        "listening": {
            "audio": "चाचा",
            "translation_en": "Paternal Uncle",
            "options_en": ["Paternal Uncle", "Maternal Uncle", "Father", "Brother"],
            "translation_vi": "Chú (em bố)",
            "options_vi": ["Chú (em bố)", "Cậu (em mẹ)", "Bố", "Anh em"]
        },
        "sb": {
            "englishSentence": "Build: 'These are my maternal uncles.' (honorific) / Dịch: 'Đây là cậu của tôi.'",
            "hindiWords": ["मामा", "हैं", "मेरे", "ये"],
            "correctHindiSentence": "ये मेरे मामा हैं"
        },
        "match": [
            {"hindi": "चाचा", "english": "Paternal Uncle (Chú)"},
            {"hindi": "चाची", "english": "Paternal Aunt (Thím)"},
            {"hindi": "मामा", "english": "Maternal Uncle (Cậu)"},
            {"hindi": "मामी", "english": "Maternal Aunt (Mợ)"}
        ],
        "takeaways": [
            "Phân biệt rõ ràng quan hệ bên nội (Chacha) và bên ngoại (Mama).",
            "Nắm vững quy tắc chuyển đuôi từ tính chất nam sang nữ (-aa thành -ee).",
            "Biết sử dụng kính ngữ khi nói về người lớn tuổi trong gia đình."
        ]
    },
    18: {
        "title": "Lesson 18: Verbs - Daily Routine",
        "description": "Học các động từ diễn tả thói quen hàng ngày: Thức dậy, Ngủ, Dọn dẹp.",
        "keyPoints": [
            "उठना (uthna): Thức dậy",
            "सोना (sona): Ngủ",
            "साफ़ करना (saaf karna): Dọn dẹp/Làm sạch",
            "नहाना (nahaana): Tắm"
        ],
        "rule_title": "Cách chia động từ thói quen (Thì hiện tại đơn)",
        "rule_explanation": "Để diễn tả thói quen hàng ngày, động từ nguyên mẫu (kết thúc bằng -na) được bỏ đuôi '-na' và thêm đuôi '-ta' (nam số ít), '-ti' (nữ số ít), hoặc '-te' (số nhiều). Ví dụ: मैं उठता हूँ (Tôi thức dậy - nam) / मैं उठती हूँ (Tôi thức dậy - nữ).",
        "rule_simple": "Verb root + ता हूँ (nam) / ती हूँ (nữ) = Tôi thường làm...",
        "flashcards": [
            {"hindi": "उठना", "transliteration": "uthna", "english": "To wake up", "vietnamese": "Thức dậy"},
            {"hindi": "सोना", "transliteration": "sona", "english": "To sleep", "vietnamese": "Ngủ"},
            {"hindi": "साफ़ करना", "transliteration": "saaf karna", "english": "To clean", "vietnamese": "Dọn dẹp"},
            {"hindi": "नहाना", "transliteration": "nahaana", "english": "To bathe", "vietnamese": "Tắm"}
        ],
        "vocab_context": {
            "word": "साफ़ करना",
            "translation": "To clean / Dọn dẹp",
            "contextSentenceHindi": "मैं कमरा साफ़ करता हूँ।",
            "contextSentenceTranslation": "I clean the room. / Tôi dọn dẹp phòng.",
            "memoryTrick": "Saaf nghĩa là sạch sẽ, karna nghĩa là làm. Saaf karna có nghĩa là làm sạch!"
        },
        "mc": {
            "text": "सोना",
            "subtext": "sona",
            "answer": "To sleep",
            "answer_vi": "Ngủ",
            "options": ["To sleep", "To wake up", "To clean", "To eat"],
            "options_vi": ["Ngủ", "Thức dậy", "Dọn dẹp", "Ăn"]
        },
        "listening": {
            "audio": "उठना",
            "translation_en": "To wake up",
            "options_en": ["To wake up", "To sleep", "To run", "To walk"],
            "translation_vi": "Thức dậy",
            "options_vi": ["Thức dậy", "Ngủ", "Chạy", "Đi bộ"]
        },
        "sb": {
            "englishSentence": "Build: 'Sleep now.' / Dịch: 'Ngủ đi.'",
            "hindiWords": ["सो", "अब", "जाओ"],
            "correctHindiSentence": "अब सो जाओ"
        },
        "match": [
            {"hindi": "उठना", "english": "To wake up (Thức dậy)"},
            {"hindi": "सोना", "english": "To sleep (Ngủ)"},
            {"hindi": "साफ़ करना", "english": "To clean (Dọn dẹp)"},
            {"hindi": "नहाना", "english": "To bathe (Tắm)"}
        ],
        "takeaways": [
            "Học các động từ mô tả các hoạt động sinh hoạt hàng ngày.",
            "Hiểu cách sử dụng cấu trúc hiện tại đơn chia theo giới tính chủ ngữ.",
            "Diễn tả được lịch trình hoạt động cơ bản của bản thân."
        ]
    },
    19: {
        "title": "Lesson 19: Vocabulary - Weather & Seasons",
        "description": "Học từ vựng về thời tiết và các mùa trong năm: Mưa, Nóng, Lạnh, Mùa.",
        "keyPoints": [
            "मौसम (mausam): Thời tiết / Mùa",
            "बारिश (baarish): Mưa",
            "गर्मी (garmi): Nóng / Mùa hè",
            "सर्दी (sardi): Lạnh / Mùa đông"
        ],
        "rule_title": "Diễn tả thời tiết đang xảy ra",
        "rule_explanation": "Để diễn tả trạng thái thời tiết, chúng ta thường dùng cấu trúc danh từ thời tiết + है (hai). Riêng với mưa, chúng ta dùng cụm động từ: बारिश हो रही है (Mưa đang rơi).",
        "rule_simple": "गर्मी है (Nóng) / सर्दी है (Lạnh) / बारिश हो रही है (Mưa)",
        "flashcards": [
            {"hindi": "मौसम", "transliteration": "mausam", "english": "Weather / Season", "vietnamese": "Thời tiết / Mùa"},
            {"hindi": "बारिश", "transliteration": "baarish", "english": "Rain", "vietnamese": "Mưa"},
            {"hindi": "गर्मी", "transliteration": "garmi", "english": "Heat / Summer", "vietnamese": "Nóng / Mùa hè"},
            {"hindi": "सर्दी", "transliteration": "sardi", "english": "Cold / Winter", "vietnamese": "Lạnh / Mùa đông"}
        ],
        "vocab_context": {
            "word": "मौसम",
            "translation": "Weather / Thời tiết",
            "contextSentenceHindi": "आज मौसम अच्छा है।",
            "contextSentenceTranslation": "The weather is good today. / Hôm nay thời tiết đẹp.",
            "memoryTrick": "Mausam nghe giống như 'Awesome' thời tiết hôm nay thật tuyệt!"
        },
        "mc": {
            "text": "बारिश हो रही है",
            "subtext": "baarish ho rahi hai",
            "answer": "It is raining",
            "answer_vi": "Trời đang mưa",
            "options": ["It is raining", "It is hot", "It is cold", "It is windy"],
            "options_vi": ["Trời đang mưa", "Trời nóng", "Trời lạnh", "Trời có gió"]
        },
        "listening": {
            "audio": "गर्मी",
            "translation_en": "Heat / Summer",
            "options_en": ["Summer / Heat", "Winter / Cold", "Rain", "Snow"],
            "translation_vi": "Nóng / Mùa hè",
            "options_vi": ["Nóng / Mùa hè", "Lạnh / Mùa đông", "Mưa", "Tuyết"]
        },
        "sb": {
            "englishSentence": "Build: 'Today is very hot.' / Dịch: 'Hôm nay trời rất nóng.'",
            "hindiWords": ["आज", "गर्मी", "बहुत", "है"],
            "correctHindiSentence": "आज बहुत गर्मी है"
        },
        "match": [
            {"hindi": "मौसम", "english": "Weather (Thời tiết)"},
            {"hindi": "बारिश", "english": "Rain (Mưa)"},
            {"hindi": "गर्मी", "english": "Summer (Mùa hè)"},
            {"hindi": "सर्दी", "english": "Winter (Mùa đông)"}
        ],
        "takeaways": [
            "Học từ vựng cơ bản về các mùa và thời tiết.",
            "Biết cách diễn đạt thời tiết hàng ngày nóng, lạnh hay mưa.",
            "Đặt câu nhận xét đơn giản về thời tiết hiện tại."
        ]
    },
    20: {
        "title": "Lesson 20: AI Tutor Graduation",
        "description": "Chúc mừng bạn đã hoàn thành khóa học! Hãy ôn tập lại các kiến thức cốt lõi và hướng tới sự trôi chảy.",
        "keyPoints": [
            "बधाई हो (badhaai ho): Chúc mừng",
            "प्रवाह (pravaah): Trôi chảy",
            "सफलता (safalta): Thành công",
            "धन्यवाद (dhanyavaad): Cảm ơn"
        ],
        "rule_title": "Lời chúc mừng và cảm ơn trong tiếng Hindi",
        "rule_explanation": "Khi chúc mừng ai đó, bạn có thể nói 'बधाई हो' (badhaai ho). Để bày tỏ sự trân trọng sau một hành trình học tập, hãy nói 'बहुत-बहुत धन्यवाद' (Xin cảm ơn rất nhiều).",
        "rule_simple": "बधाई हो! = Chúc mừng! / धन्यवाद = Cảm ơn",
        "flashcards": [
            {"hindi": "बधाई हो", "transliteration": "badhaai ho", "english": "Congratulations", "vietnamese": "Chúc mừng"},
            {"hindi": "प्रवाह", "transliteration": "pravaah", "english": "Fluency", "vietnamese": "Trôi chảy"},
            {"hindi": "सफलता", "transliteration": "safalta", "english": "Success", "vietnamese": "Thành công"},
            {"hindi": "धन्यवाद", "transliteration": "dhanyavaad", "english": "Thank you", "vietnamese": "Cảm ơn"}
        ],
        "vocab_context": {
            "word": "प्रवाह",
            "translation": "Fluency / Trôi chảy",
            "contextSentenceHindi": "आप धाराप्रवाह हिंदी बोलते हैं।",
            "contextSentenceTranslation": "You speak Hindi fluently. / Bạn nói tiếng Hindi trôi chảy.",
            "memoryTrick": "Pravaah gần giống như 'pravaah' hay 'flow' - dòng chảy trôi chảy của ngôn ngữ!"
        },
        "mc": {
            "text": "बधाई हो",
            "subtext": "badhaai ho",
            "answer": "Congratulations",
            "answer_vi": "Chúc mừng",
            "options": ["Congratulations", "Thank you", "Goodbye", "Please"],
            "options_vi": ["Chúc mừng", "Cảm ơn", "Tạm biệt", "Làm ơn"]
        },
        "listening": {
            "audio": "सफलता",
            "translation_en": "Success",
            "options_en": ["Success", "Failure", "Start", "End"],
            "translation_vi": "Thành công",
            "options_vi": ["Thành công", "Thất bại", "Bắt đầu", "Kết thúc"]
        },
        "sb": {
            "englishSentence": "Build: 'Thank you teacher.' / Dịch: 'Cảm ơn thầy/cô.'",
            "hindiWords": ["शिक्षक", "धन्यवाद", "बहुत"],
            "correctHindiSentence": "शिक्षक बहुत धन्यवाद"
        },
        "match": [
            {"hindi": "बधाई हो", "english": "Congratulations (Chúc mừng)"},
            {"hindi": "प्रवाह", "english": "Fluency (Trôi chảy)"},
            {"hindi": "सफलता", "english": "Success (Thành công)"},
            {"hindi": "धन्यवाद", "english": "Thank you (Cảm ơn)"}
        ],
        "takeaways": [
            "Chúc mừng bạn đã hoàn thành chặng đường nâng cấp phản xạ!",
            "Hãy tự hào vì đã mở khóa sự tự tin lưu loát tiếng Hindi.",
            "Tiếp tục luyện tập và sử dụng tiếng Hindi trong cuộc sống hàng ngày."
        ]
    }
}

# Function to map episode data to app exercises schema
def build_exercises(ep_num, ep):
    return [
        {
            "type": "Introduction",
            "title": ep["title"],
            "description": ep["description"],
            "keyPoints": ep["keyPoints"]
        },
        {
            "type": "TeachRule",
            "title": ep["rule_title"],
            "explanation": ep["rule_explanation"],
            "simpleRule": ep["rule_simple"]
        },
        {
            "type": "Flashcard",
            "hindi": ep["flashcards"][0]["hindi"],
            "transliteration": ep["flashcards"][0]["transliteration"],
            "english": ep["flashcards"][0]["english"],
            "vietnamese": ep["flashcards"][0]["vietnamese"],
            "audio": ep["flashcards"][0]["hindi"]
        },
        {
            "type": "Flashcard",
            "hindi": ep["flashcards"][1]["hindi"],
            "transliteration": ep["flashcards"][1]["transliteration"],
            "english": ep["flashcards"][1]["english"],
            "vietnamese": ep["flashcards"][1]["vietnamese"],
            "audio": ep["flashcards"][1]["hindi"]
        },
        {
            "type": "Flashcard",
            "hindi": ep["flashcards"][2]["hindi"],
            "transliteration": ep["flashcards"][2]["transliteration"],
            "english": ep["flashcards"][2]["english"],
            "vietnamese": ep["flashcards"][2]["vietnamese"],
            "audio": ep["flashcards"][2]["hindi"]
        },
        {
            "type": "Flashcard",
            "hindi": ep["flashcards"][3]["hindi"],
            "transliteration": ep["flashcards"][3]["transliteration"],
            "english": ep["flashcards"][3]["english"],
            "vietnamese": ep["flashcards"][3]["vietnamese"],
            "audio": ep["flashcards"][3]["hindi"]
        },
        {
            "type": "VocabularyContext",
            "word": ep["vocab_context"]["word"],
            "translation": ep["vocab_context"]["translation"],
            "contextSentenceHindi": ep["vocab_context"]["contextSentenceHindi"],
            "contextSentenceTranslation": ep["vocab_context"]["contextSentenceTranslation"],
            "memoryTrick": ep["vocab_context"]["memoryTrick"]
        },
        {
            "type": "MultipleChoice",
            "prompt": "Choose the correct translation:",
            "prompt_vi": "Chọn bản dịch đúng:",
            "text": ep["mc"]["text"],
            "subtext": ep["mc"]["subtext"],
            "answer": ep["mc"]["answer"],
            "answer_vi": ep["mc"]["answer_vi"],
            "options": ep["mc"]["options"],
            "options_vi": ep["mc"]["options_vi"]
        },
        {
            "type": "Listening",
            "audio": ep["listening"]["audio"],
            "translation_en": ep["listening"]["translation_en"],
            "options_en": ep["listening"]["options_en"],
            "translation_vi": ep["listening"]["translation_vi"],
            "options_vi": ep["listening"]["options_vi"]
        },
        {
            "type": "SentenceBuilder",
            "englishSentence": ep["sb"]["englishSentence"],
            "hindiWords": ep["sb"]["hindiWords"],
            "correctHindiSentence": ep["sb"]["correctHindiSentence"]
        },
        {
            "type": "MatchPairs",
            "instruction": "Match the pairs / Ghép các cặp từ",
            "pairs": ep["match"]
        },
        {
            "type": "RevisionSummary",
            "title": "Tổng kết bài học",
            "takeaways": ep["takeaways"]
        }
    ]

# Output files
for ep_num, ep in episodes_data.items():
    exercises = build_exercises(ep_num, ep)
    
    # 1. Save as episode_11.json to episode_20.json
    filename_ep = f"episode_{ep_num}.json"
    filepath_ep = os.path.join(episodes_dir, filename_ep)
    with open(filepath_ep, "w", encoding="utf-8") as f:
        json.dump(exercises, f, ensure_ascii=False, indent=2)
        
    # 2. Save as node_11.json to node_20.json
    filename_node = f"node_{ep_num}.json"
    filepath_node = os.path.join(episodes_dir, filename_node)
    with open(filepath_node, "w", encoding="utf-8") as f:
        json.dump(exercises, f, ensure_ascii=False, indent=2)

print("Successfully generated all episode and node files!")
