import json
import os

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(output_dir, exist_ok=True)

def generate_10_node_episode(title, key_points, vocab_list):
    v1, v2, v3, v4 = vocab_list[:4]
    nodes = [
        {
            "type": "Introduction",
            "title": title,
            "description": f"Learn key vocabulary and concepts for {title}.",
            "keyPoints": key_points
        },
        {
            "type": "TeachRule",
            "title": "Grammar & Pronunciation rule",
            "explanation": "Pay attention to correct word alignment and phonetic features.",
            "simpleRule": f"Remember: {v1[0]} = {v1[2]} ({v1[3]})"
        },
        {
            "type": "Flashcard",
            "hindi": v1[0],
            "transliteration": v1[1],
            "english": v1[2],
            "vietnamese": v1[3],
            "audio": v1[0]
        },
        {
            "type": "Flashcard",
            "hindi": v2[0],
            "transliteration": v2[1],
            "english": v2[2],
            "vietnamese": v2[3],
            "audio": v2[0]
        },
        {
            "type": "MultipleChoice",
            "prompt": f"Translate '{v1[0]}'",
            "prompt_en": f"Choose the correct translation for '{v1[0]}'",
            "prompt_vi": f"Chọn bản dịch đúng cho '{v1[0]}'",
            "text": v1[0],
            "subtext": v1[1],
            "answer": v1[2],
            "answer_vi": v1[3],
            "options": [v1[2], v2[2], v3[2], v4[2]],
            "options_vi": [v1[3], v2[3], v3[3], v4[3]]
        },
        {
            "type": "Flashcard",
            "hindi": v3[0],
            "transliteration": v3[1],
            "english": v3[2],
            "vietnamese": v3[3],
            "audio": v3[0]
        },
        {
            "type": "Flashcard",
            "hindi": v4[0],
            "transliteration": v4[1],
            "english": v4[2],
            "vietnamese": v4[3],
            "audio": v4[0]
        },
        {
            "type": "MatchPairs",
            "instruction": "Match the Hindi words with their translations",
            "pairs": [
                {"hindi": v1[0], "english": f"{v1[2]} ({v1[3]})"},
                {"hindi": v2[0], "english": f"{v2[2]} ({v2[3]})"},
                {"hindi": v3[0], "english": f"{v3[2]} ({v3[3]})"},
                {"hindi": v4[0], "english": f"{v4[2]} ({v4[3]})"}
            ]
        },
        {
            "type": "Listening",
            "audio": v3[0],
            "translation_en": v3[2],
            "translation_vi": v3[3],
            "options_en": [v1[2], v2[2], v3[2], v4[2]],
            "options_vi": [v1[3], v2[3], v3[3], v4[3]]
        },
        {
            "type": "RevisionSummary",
            "title": "Module Finished",
            "takeaways": [
                f"You mastered: {v1[0]} and {v2[0]}",
                f"Completed listening drills for: {v3[0]}"
            ]
        }
    ]
    return nodes

new_episodes = {
    # Pronunciation Lab: 11 to 15
    "pron_11": {
        "title": "Geminate Consonants",
        "keyPoints": ["सच्चा (sachchā) = True / Đích thực", "बिल्ली (billī) = Cat / Con mèo"],
        "vocab": [
            ("सच्चा", "sachchā", "True", "Đích thực"),
            ("बिल्ली", "billī", "Cat", "Con mèo"),
            ("पत्ता", "pattā", "Leaf", "Chiếc lá"),
            ("चम्मच", "chammach", "Spoon", "Cái thìa")
        ]
    },
    "pron_12": {
        "title": "Word Stress Patterns",
        "keyPoints": ["करना (karnā) = To do / Làm", "करवाना (karvānā) = To cause to do / Nhờ làm"],
        "vocab": [
            ("करना", "karnā", "To do", "Làm"),
            ("करवाना", "karvānā", "To cause to do", "Nhờ làm"),
            ("चलना", "chalnā", "To walk", "Đi bộ"),
            ("चलाना", "chalānā", "To drive", "Lái xe")
        ]
    },
    "pron_13": {
        "title": "Perso-Arabic Sounds",
        "keyPoints": ["साफ़ (sāf) = Clean / Sạch sẽ", "ज़मीन (zamīn) = Ground / Mặt đất"],
        "vocab": [
            ("साफ़", "sāf", "Clean", "Sạch sẽ"),
            ("ज़मीन", "zamīn", "Ground", "Mặt đất"),
            ("फ़ायदा", "fāydā", "Benefit", "Lợi ích"),
            ("ख़राब", "kharāb", "Bad / Ruined", "Hỏng / Tệ")
        ]
    },
    "pron_14": {
        "title": "Vowel Lengthening",
        "keyPoints": ["दिन (din) = Day / Ngày", "दीन (dīn) = Poor / Nghèo khổ"],
        "vocab": [
            ("दिन", "din", "Day", "Ngày"),
            ("दीन", "dīn", "Poor", "Nghèo khổ"),
            ("मिलना", "milnā", "To meet", "Gặp gỡ"),
            ("मील", "mīl", "Mile", "Dặm")
        ]
    },
    "pron_15": {
        "title": "Connected Speech Flow",
        "keyPoints": ["क्या हुआ? (kyā huā?) = What happened? / Có chuyện gì vậy?", "कुछ नहीं। (kuchh nahī̃.) = Nothing. / Không có gì."],
        "vocab": [
            ("क्या हुआ?", "kyā huā?", "What happened?", "Có chuyện gì vậy?"),
            ("कुछ नहीं।", "kuchh nahī̃.", "Nothing.", "Không có gì."),
            ("चलो चलें", "chalo chalẽ", "Let's go", "Đi nào"),
            ("कोई बात नहीं", "koī bāt nahī̃", "No problem", "Không sao")
        ]
    },

    # Speaking: 13 to 17
    "speak_13": {
        "title": "Asking for Opinions",
        "keyPoints": ["विचार (vichār) = Opinion / Ý kiến", "सोचना (sochnā) = To think / Suy nghĩ"],
        "vocab": [
            ("विचार", "vichār", "Opinion / Thought", "Ý kiến / Suy nghĩ"),
            ("सोचना", "sochnā", "To think", "Suy nghĩ"),
            ("सलाह", "salāh", "Advice", "Lời khuyên"),
            ("सही", "sahī", "Correct", "Đúng")
        ]
    },
    "speak_14": {
        "title": "Talking about Hobbies",
        "keyPoints": ["शौक (shauk) = Hobby / Sở thích", "पसंद (pasand) = Like / Thích"],
        "vocab": [
            ("शौक", "shauk", "Hobby", "Sở thích"),
            ("पसंद", "pasand", "Like", "Thích"),
            ("खेलना", "khelnā", "To play", "Chơi"),
            ("गाना", "gānā", "To sing / Song", "Hát / Bài hát")
        ]
    },
    "speak_15": {
        "title": "Making Excuses",
        "keyPoints": ["व्यस्त (vyast) = Busy / Bận rộn", "वजह (wajah) = Reason / Lý do"],
        "vocab": [
            ("व्यस्त", "vyast", "Busy", "Bận rộn"),
            ("वजह", "wajah", "Reason", "Lý do"),
            ("देर", "der", "Late", "Trễ"),
            ("ज़रूरी", "zarūrī", "Important / Necessary", "Quan trọng / Cần thiết")
        ]
    },
    "speak_16": {
        "title": "Arranging a Meeting",
        "keyPoints": ["मिलना (milnā) = To meet / Gặp gỡ", "समय (samay) = Time / Thời gian"],
        "vocab": [
            ("मिलना", "milnā", "To meet", "Gặp gỡ"),
            ("समय", "samay", "Time", "Thời gian"),
            ("जगह", "jagah", "Place", "Địa điểm"),
            ("तारीख", "tārīkh", "Date", "Ngày tháng")
        ]
    },
    "speak_17": {
        "title": "Giving Directions",
        "keyPoints": ["दाएँ (dāẽ) = Right / Bên phải", "बाएँ (bāẽ) = Left / Bên trái"],
        "vocab": [
            ("दाएँ", "dāẽ", "Right", "Bên phải"),
            ("बाएँ", "bāẽ", "Left", "Bên trái"),
            ("सीधे", "sīdhe", "Straight", "Đi thẳng"),
            ("मुड़ो", "muṛo", "Turn", "Rẽ")
        ]
    },

    # Grammar: 15 to 19
    "gram_15": {
        "title": "Obligation & Necessity",
        "keyPoints": ["चाहिए (chāhie) = Should / Cần phải", "ज़रूर (zarūr) = Definitely / Chắc chắn"],
        "vocab": [
            ("चाहिए", "chāhie", "Should / Need", "Cần phải"),
            ("ज़रूर", "zarūr", "Definitely", "Chắc chắn"),
            ("पड़ता है", "paṛtā hai", "Has to", "Phải (bắt buộc)"),
            ("काम", "kām", "Work", "Công việc")
        ]
    },
    "gram_16": {
        "title": "Ability (Sakkna)",
        "keyPoints": ["सकता (saktā) = Can (M) / Có thể", "सकती (saktī) = Can (F) / Có thể"],
        "vocab": [
            ("सकता", "saktā", "Can (M)", "Có thể (Nam)"),
            ("सकती", "saktī", "Can (F)", "Có thể (Nữ)"),
            ("सकते", "sakte", "Can (Plural)", "Có thể (Số nhiều)"),
            ("करना", "karnā", "To do", "Làm")
        ]
    },
    "gram_17": {
        "title": "Desiderative (Chahna)",
        "keyPoints": ["चाहता (chāhtā) = Want (M) / Muốn", "चाहती (chāhtī) = Want (F) / Muốn"],
        "vocab": [
            ("चाहता", "chāhtā", "Want (M)", "Muốn (Nam)"),
            ("चाहती", "chāhtī", "Want (F)", "Muốn (Nữ)"),
            ("चाहते", "chāhte", "Want (Plural)", "Muốn (Số nhiều)"),
            ("जाना", "jānā", "To go", "Đi")
        ]
    },
    "gram_18": {
        "title": "Causative Verbs",
        "keyPoints": ["कराना (karānā) = To make someone do / Làm cho", "लिखवाना (likhvānā) = To get written / Nhờ viết"],
        "vocab": [
            ("कराना", "karānā", "To cause to do", "Làm cho"),
            ("लिखवाना", "likhvānā", "To get written", "Nhờ viết"),
            ("बनाना", "banānā", "To make", "Làm / Chế tạo"),
            ("बनवाना", "banvānā", "To get made", "Nhờ làm")
        ]
    },
    "gram_19": {
        "title": "Relative Clauses",
        "keyPoints": ["जो (jo) = Who / Người mà", "वह (vah) = He/She/That / Người đó"],
        "vocab": [
            ("जो", "jo", "Who / Which", "Người mà / Cái mà"),
            ("वह", "vah", "That", "Đó / Kia"),
            ("जहाँ", "jahā̃", "Where (Relative)", "Nơi mà"),
            ("वहाँ", "vahā̃", "There", "Ở đó")
        ]
    },

    # Listening: 9 to 13
    "listen_9": {
        "title": "Audio Story Comprehension",
        "keyPoints": ["कहानी (kahānī) = Story / Câu chuyện", "सुनना (sunnā) = To listen / Nghe"],
        "vocab": [
            ("कहानी", "kahānī", "Story", "Câu chuyện"),
            ("सुनना", "sunnā", "To listen", "Nghe"),
            ("एक बार", "ek bār", "Once upon a time / Once", "Một lần / Ngày xửa ngày xưa"),
            ("राजा", "rājā", "King", "Vua")
        ]
    },
    "listen_10": {
        "title": "Eavesdropping in Public",
        "keyPoints": ["बाज़ार (bāzār) = Market / Chợ", "भीड़ (bhīṛ) = Crowd / Đám đông"],
        "vocab": [
            ("बाज़ार", "bāzār", "Market", "Chợ"),
            ("भीड़", "bhīṛ", "Crowd", "Đám đông"),
            ("आवाज़", "āvāz", "Voice / Sound", "Giọng nói / Âm thanh"),
            ("शोर", "shor", "Noise", "Tiếng ồn")
        ]
    },
    "listen_11": {
        "title": "Airport Announcements",
        "keyPoints": ["उड़ान (uḍān) = Flight / Chuyến bay", "यात्री (yātrī) = Passenger / Hành khách"],
        "vocab": [
            ("उड़ान", "uḍān", "Flight", "Chuyến bay"),
            ("यात्री", "yātrī", "Passenger", "Hành khách"),
            ("घोषणा", "ghoshṇā", "Announcement", "Thông báo"),
            ("देरी", "derī", "Delay", "Sự chậm trễ")
        ]
    },
    "listen_12": {
        "title": "Restaurant Orders Audio",
        "keyPoints": ["ऑर्डर (ôrḍar) = Order / Đặt món", "वेट र (veṭar) = Waiter / Bồi bàn"],
        "vocab": [
            ("ऑर्डर", "ôrḍar", "Order", "Đặt món"),
            ("वेटर", "veṭar", "Waiter", "Bồi bàn"),
            ("बिल", "bil", "Bill", "Hóa đơn"),
            ("स्वादिष्ट", "svādishṭ", "Delicious", "Ngon miệng")
        ]
    },
    "listen_13": {
        "title": "Directions Dialogue",
        "keyPoints": ["रास्ता (rāstā) = Way / Đường đi", "कहाँ (kahā̃) = Where / Ở đâu"],
        "vocab": [
            ("रास्ता", "rāstā", "Way / Path", "Đường đi"),
            ("कहाँ", "kahā̃", "Where", "Ở đâu"),
            ("नक्शा", "nakshā", "Map", "Bản đồ"),
            ("पास", "pās", "Near", "Gần")
        ]
    },

    # Writing: 11 to 15
    "write_11": {
        "title": "Formatting Emails",
        "keyPoints": ["ईमेल (īmel) = Email / Thư điện tử", "विषय (vishay) = Subject / Chủ đề"],
        "vocab": [
            ("ईमेल", "īmel", "Email", "Thư điện tử"),
            ("विषय", "vishay", "Subject", "Chủ đề"),
            ("महोदय", "mahoday", "Dear Sir / Respected Sir", "Kính gửi Ông"),
            ("सादर", "sādar", "With Respect / Regards", "Trân trọng")
        ]
    },
    "write_12": {
        "title": "Digital Keyboards",
        "keyPoints": ["कीबोर्ड (kībōrḍ) = Keyboard / Bàn phím", "टाइप (ṭāip) = Type / Gõ phím"],
        "vocab": [
            ("कीबोर्ड", "kībōrḍ", "Keyboard", "Bàn phím"),
            ("टाइप", "ṭāip", "Type", "Gõ phím"),
            ("भाषा", "bhāshā", "Language", "Ngôn ngữ"),
            ("अक्षर", "akshar", "Letter / Character", "Chữ cái")
        ]
    },
    "write_13": {
        "title": "Letter Structures",
        "keyPoints": ["पत्र (patra) = Letter / Thư", "पता (patā) = Address / Địa chỉ"],
        "vocab": [
            ("पत्र", "patra", "Letter", "Thư"),
            ("पता", "patā", "Address", "Địa chỉ"),
            ("दिनांक", "dināṅk", "Date", "Ngày tháng"),
            ("प्रिय", "priya", "Dear (Informal)", "Thân gửi")
        ]
    },
    "write_14": {
        "title": "Creative Writing Prompts",
        "keyPoints": ["लेख (lekh) = Article / Bài viết", "विचार (vichār) = Idea / Ý tưởng"],
        "vocab": [
            ("लेख", "lekh", "Article / Essay", "Bài viết"),
            ("विचार", "vichār", "Idea / Thought", "Ý tưởng"),
            ("कहानी", "kahānī", "Story", "Câu chuyện"),
            ("लिखना", "likhnā", "To write", "Viết")
        ]
    },
    "write_15": {
        "title": "Official Applications",
        "keyPoints": ["प्रार्थना पत्र (prārthanā patra) = Application Letter / Đơn xin phép"],
        "vocab": [
            ("प्रार्थना पत्र", "prārthanā patra", "Application Letter", "Đơn xin phép"),
            ("छुट्टी", "chhuṭṭī", "Holiday / Leave", "Nghỉ phép"),
            ("स्वीकार", "svīkār", "Acceptance / Accept", "Chấp nhận"),
            ("अनुरोध", "anurodh", "Request", "Yêu cầu")
        ]
    },

    # Culture: 9 to 13
    "culture_9": {
        "title": "Cinema & Cricket",
        "keyPoints": ["क्रिकेट (krikeṭ) = Cricket / Bóng chày", "सिनेमा (sinemā) = Cinema / Điện ảnh"],
        "vocab": [
            ("क्रिकेट", "krikeṭ", "Cricket", "Bóng chày"),
            ("सिनेमा", "sinemā", "Cinema", "Điện ảnh"),
            ("मैच", "maich", "Match", "Trận đấu"),
            ("अभिनेता", "abhinetā", "Actor", "Nam diễn viên")
        ]
    },
    "culture_10": {
        "title": "Tea/Chai Culture",
        "keyPoints": ["चाय (chāy) = Tea / Trà", "दुकान (dukān) = Shop / Cửa hàng"],
        "vocab": [
            ("चाय", "chāy", "Tea", "Trà"),
            ("दुकान", "dukān", "Shop", "Cửa hàng"),
            ("गपशप", "gapshap", "Gossip / Small talk", "Tán gẫu"),
            ("कुल्हड़", "kulhaṛ", "Clay cup", "Cốc đất nung")
        ]
    },
    "culture_11": {
        "title": "Traditional Clothing",
        "keyPoints": ["साड़ी (sāṛī) = Sari / Trang phục Sari", "कुर्ता (kurtā) = Kurta / Áo Kurta"],
        "vocab": [
            ("साड़ी", "sāṛī", "Sari", "Trang phục Sari"),
            ("कुर्ता", "kurtā", "Kurta", "Áo Kurta"),
            ("कपड़े", "kapaṛe", "Clothes", "Quần áo"),
            ("रंग-बिरंगा", "raṅg-biraṅgā", "Colorful", "Rực rỡ sắc màu")
        ]
    },
    "culture_12": {
        "title": "Indian Festivals",
        "keyPoints": ["दिवाली (divālī) = Diwali / Lễ hội ánh sáng", "होली (holī) = Holi / Lễ hội màu sắc"],
        "vocab": [
            ("दिवाली", "divālī", "Diwali", "Lễ hội ánh sáng"),
            ("होली", "holī", "Holi", "Lễ hội màu sắc"),
            ("त्योहार", "tyohār", "Festival", "Lễ hội"),
            ("मिठाई", "miṭhāī", "Sweets", "Bánh kẹo truyền thống")
        ]
    },
    "culture_13": {
        "title": "Bazaar Shopping Culture",
        "keyPoints": ["बाज़ार (bāzār) = Market / Chợ", "दाम (dām) = Price / Giá cả"],
        "vocab": [
            ("बाज़ार", "bāzār", "Market", "Chợ"),
            ("दाम", "dām", "Price", "Giá cả"),
            ("दुकानदार", "dukāndār", "Shopkeeper", "Chủ cửa hàng"),
            ("खरीदना", "kharīdnā", "To buy", "Mua")
        ]
    },

    # Travel: 11 to 15
    "travel_11": {
        "title": "Buying Sim Card",
        "keyPoints": ["सिम कार्ड (sim kārḍ) = Sim Card / Thẻ Sim", "इंटरनेट (iṇṭaranēṭ) = Internet / Mạng Internet"],
        "vocab": [
            ("सिम कार्ड", "sim kārḍ", "Sim Card", "Thẻ Sim"),
            ("इंटरनेट", "iṇṭaranēṭ", "Internet", "Mạng Internet"),
            ("डॉक्यूमेंट", "ḍôkyūmeṇṭ", "Document", "Tài liệu / Giấy tờ"),
            ("नंबर", "nambar", "Number", "Số điện thoại")
        ]
    },
    "travel_12": {
        "title": "Asking for Recommendations",
        "keyPoints": ["सुझाव (sujhāv) = Suggestion / Gợi ý", "अच्छा (achchhā) = Good / Tốt"],
        "vocab": [
            ("सुझाव", "sujhāv", "Suggestion / Recommendation", "Gợi ý / Đề xuất"),
            ("अच्छा", "achchhā", "Good", "Tốt"),
            ("होटल", "hoṭal", "Hotel", "Khách sạn"),
            ("जगह", "jagah", "Place", "Địa điểm")
        ]
    },
    "travel_13": {
        "title": "Train Station Help",
        "keyPoints": ["प्लेटफ़ॉर्म (pleṭfôrm) = Platform / Sân ga", "टिकट (ṭikaṭ) = Ticket / Vé"],
        "vocab": [
            ("प्लेटफ़ॉर्म", "pleṭfôrm", "Platform", "Sân ga"),
            ("टिकट", "ṭikaṭ", "Ticket", "Vé"),
            ("पूछताछ", "pūchhtāchh", "Inquiry", "Quầy thông tin"),
            ("रेलगाड़ी", "relgāṛī", "Train", "Tàu hỏa")
        ]
    },
    "travel_14": {
        "title": "Luggage Troubles",
        "keyPoints": ["सामान (sāmān) = Luggage / Hành lý", "खो गया (kho gayā) = Lost / Bị mất"],
        "vocab": [
            ("सामान", "sāmān", "Luggage / Goods", "Hành lý"),
            ("खो गया", "kho gayā", "Lost", "Bị mất"),
            ("भारी", "bhārī", "Heavy", "Nặng"),
            ("बैग", "baig", "Bag", "Túi xách / Balo")
        ]
    },
    "travel_15": {
        "title": "Sightseeing Queries",
        "keyPoints": ["महल (mahal) = Palace / Cung điện", "किला (kilā) = Fort / Pháo đài"],
        "vocab": [
            ("महल", "mahal", "Palace", "Cung điện"),
            ("किला", "kilā", "Fort", "Pháo đài"),
            ("इतिहास", "itihās", "History", "Lịch sử"),
            ("टिकट", "ṭikaṭ", "Ticket", "Vé")
        ]
    },

    # Business: 9 to 13
    "business_9": {
        "title": "Pitching an Idea",
        "keyPoints": ["प्रस्तुति (prastuti) = Presentation / Thuyết trình", "विचार (vichār) = Idea / Ý tưởng"],
        "vocab": [
            ("प्रस्तुति", "prastuti", "Presentation", "Thuyết trình"),
            ("विचार", "vichār", "Idea", "Ý tưởng"),
            ("योजना", "yojanā", "Plan", "Kế hoạch"),
            ("लाभ", "lābh", "Profit", "Lợi nhuận")
        ]
    },
    "business_10": {
        "title": "Performance Reviews",
        "keyPoints": ["समीक्षा (samīkshā) = Review / Đánh giá", "काम (kām) = Work / Công việc"],
        "vocab": [
            ("समीक्षा", "samīkshā", "Review / Evaluation", "Đánh giá"),
            ("काम", "kām", "Work", "Công việc"),
            ("प्रदर्शन", "pradarshan", "Performance", "Hiệu suất"),
            ("बोनस", "bonas", "Bonus", "Tiền thưởng")
        ]
    },
    "business_11": {
        "title": "Client Negotiations",
        "keyPoints": ["समझौता (samjhautā) = Agreement / Thỏa thuận", "कीमत (kīmat) = Price / Giá cả"],
        "vocab": [
            ("समझौता", "samjhautā", "Agreement", "Thỏa thuận"),
            ("कीमत", "kīmat", "Price", "Giá cả"),
            ("सौदा", "saudā", "Deal", "Giao dịch"),
            ("शर्तें", "shartẽ", "Terms", "Điều khoản")
        ]
    },
    "business_12": {
        "title": "Scheduling Conferences",
        "keyPoints": ["सम्मेलन (sammelan) = Conference / Hội nghị", "बैठक (baiṭhak) = Meeting / Cuộc họp"],
        "vocab": [
            ("सम्मेलन", "sammelan", "Conference", "Hội nghị"),
            ("बैठक", "baiṭhak", "Meeting", "Cuộc họp"),
            ("समय", "samay", "Time", "Thời gian"),
            ("कमरा", "kamarā", "Room", "Phòng")
        ]
    },
    "business_13": {
        "title": "Signing Contracts",
        "keyPoints": ["अनुबंध (anubandh) = Contract / Hợp đồng", "हस्ताक्षर (hastākshar) = Signature / Chữ ký"],
        "vocab": [
            ("अनुबंध", "anubandh", "Contract", "Hợp đồng"),
            ("हस्ताक्षर", "hastākshar", "Signature", "Chữ ký"),
            ("कानूनी", "kānūnī", "Legal", "Hợp pháp"),
            ("स्वीकृति", "svīkriti", "Approval", "Chấp thuận")
        ]
    },

    # Bollywood: 9 to 13
    "bollywood_9": {
        "title": "Award Show Expressions",
        "keyPoints": ["पुरस्कार (puraskār) = Award / Giải thưởng", "बधाई (badhāī) = Congratulations / Chúc mừng"],
        "vocab": [
            ("पुरस्कार", "puraskār", "Award", "Giải thưởng"),
            ("बधाई", "badhāī", "Congratulations", "Chúc mừng"),
            ("भाषण", "bhāshaṇ", "Speech", "Bài phát biểu"),
            ("मंच", "manch", "Stage", "Sân khấu")
        ]
    },
    "bollywood_10": {
        "title": "Fan Following Slang",
        "keyPoints": ["प्रशंसक (prashansak) = Fan / Người hâm mộ", "सुपरस्टार (suparasṭār) = Superstar / Siêu sao"],
        "vocab": [
            ("प्रशंसक", "prashansak", "Fan", "Người hâm mộ"),
            ("सुपरस्टार", "suparasṭār", "Superstar", "Siêu sao"),
            ("ऑटोग्राफ", "ôṭogrāf", "Autograph", "Chữ ký lưu niệm"),
            ("तस्वीर", "tasvīr", "Picture / Photo", "Bức ảnh")
        ]
    },
    "bollywood_11": {
        "title": "Romantic Dialogues",
        "keyPoints": ["मोहब्बत (mohabbaat) = Love / Tình yêu", "इश्क़ (ishq) = Passionate Love / Tình yêu mãnh liệt"],
        "vocab": [
            ("मोहब्बत", "mohabbaat", "Love", "Tình yêu"),
            ("इश्क़", "ishq", "Passionate Love", "Tình yêu mãnh liệt"),
            ("शायरी", "shāyarī", "Poetry", "Thơ ca"),
            ("नफ़रत", "nafrat", "Hate", "Ghét bỏ")
        ]
    },
    "bollywood_12": {
        "title": "Action Thriller Phrases",
        "keyPoints": ["बदला (badlā) = Revenge / Trả thù", "दुश्मन (dushman) = Enemy / Kẻ thù"],
        "vocab": [
            ("बदला", "badlā", "Revenge", "Trả thù"),
            ("दुश्मन", "dushman", "Enemy", "Kẻ thù"),
            ("लड़ाई", "laṛāī", "Fight", "Cuộc chiến"),
            ("जीत", "jīt", "Victory", "Chiến thắng")
        ]
    },
    "bollywood_13": {
        "title": "Classic Item Songs",
        "keyPoints": ["नाच (nāch) = Dance / Nhảy múa", "गाना (gānā) = Song / Bài hát"],
        "vocab": [
            ("नाच", "nāch", "Dance", "Nhảy múa"),
            ("गाना", "gānā", "Song", "Bài hát"),
            ("मस्ती", "mastī", "Fun / Joy", "Vui vẻ"),
            ("धमाल", "dhamāl", "Riot of fun", "Quậy phá / Vui nhộn")
        ]
    },

    # WhatsApp: 7 to 11
    "whatsapp_7": {
        "title": "Meme Vocabulary",
        "keyPoints": ["मज़ाक (mazāk) = Joke / Trò đùa", "व्हाट्सएप (vhāṭsēp) = WhatsApp / WhatsApp"],
        "vocab": [
            ("मज़ाक", "mazāk", "Joke", "Trò đùa"),
            ("व्हाट्सएप", "vhāṭsēp", "WhatsApp", "WhatsApp"),
            ("ग्रुप", "grup", "Group", "Nhóm"),
            ("शेयर", "sheyar", "Share", "Chia sẻ")
        ]
    },
    "whatsapp_8": {
        "title": "Virtual Birthday Wishes",
        "keyPoints": ["जन्मदिन (janmadin) = Birthday / Sinh nhật", "बधाई (badhāī) = Congratulations / Chúc mừng"],
        "vocab": [
            ("जन्मदिन", "janmadin", "Birthday", "Sinh nhật"),
            ("बधाई", "badhāī", "Congratulations / Greetings", "Chúc mừng"),
            ("पार्टी", "pārṭī", "Party", "Bữa tiệc"),
            ("गिफ़्ट", "gifṭ", "Gift", "Món quà")
        ]
    },
    "whatsapp_9": {
        "title": "Hinglish Slang",
        "keyPoints": ["भाई (bhāī) = Brother / Anh trai", "यार (yār) = Friend / Bạn thân"],
        "vocab": [
            ("भाई", "bhāī", "Brother / Bro", "Anh trai"),
            ("यार", "yār", "Friend / Dude", "Bạn thân"),
            ("कूदना", "kūdnā", "To jump / Excite", "Nhảy cẫng lên"),
            ("मस्त", "mast", "Awesome", "Tuyệt vời")
        ]
    },
    "whatsapp_10": {
        "title": "Dating App Slang",
        "keyPoints": ["बंदी (bandī) = Girl / Bạn gái", "बंदा (bandā) = Guy / Bạn trai"],
        "vocab": [
            ("बंदी", "bandī", "Girl / Girlfriend", "Bạn gái"),
            ("बंदा", "bandā", "Guy / Boyfriend", "Bạn trai"),
            ("क्रश", "krash", "Crush", "Người thầm thương"),
            ("पटोला", "paṭolā", "Beautiful girl", "Cô gái xinh đẹp")
        ]
    },
    "whatsapp_11": {
        "title": "Chat Acronyms",
        "keyPoints": ["जीएन (GN) = Good Night / Chúc ngủ ngon", "जीएम (GM) = Good Morning / Chào buổi sáng"],
        "vocab": [
            ("जीएन", "GN", "Good Night", "Chúc ngủ ngon"),
            ("जीएम", "GM", "Good Morning", "Chào buổi sáng"),
            ("टीसी", "TC", "Take Care", "Bảo trọng"),
            ("केके", "KK", "Okay Okay", "Được rồi được rồi")
        ]
    },

    # Emergency: 7 to 11
    "emerg_7": {
        "title": "Lost Child or Person",
        "keyPoints": ["खोया हुआ (khoyā huā) = Lost / Bị lạc", "बच्चा (bachchā) = Child / Đứa trẻ"],
        "vocab": [
            ("खोया हुआ", "khoyā huā", "Lost / Missing", "Bị lạc / Mất tích"),
            ("बच्चा", "bachchā", "Child", "Đứa trẻ"),
            ("मदद", "madad", "Help", "Giúp đỡ"),
            ("रो रहा है", "ro rahā hai", "Crying", "Đang khóc")
        ]
    },
    "emerg_8": {
        "title": "Fire Emergency",
        "keyPoints": ["आग (āg) = Fire / Lửa", "धुआँ (dhuā̃) = Smoke / Khói"],
        "vocab": [
            ("आग", "āg", "Fire", "Lửa"),
            ("धुआँ", "dhuā̃", "Smoke", "Khói"),
            ("भागो!", "bhāgo!", "Run!", "Chạy đi!"),
            ("बचाओ!", "bachāo!", "Save me / Help!", "Cứu tôi với!")
        ]
    },
    "emerg_9": {
        "title": "Police Station Help",
        "keyPoints": ["थाना (thānā) = Police Station / Đồn cảnh sát", "शिकायत (shikāyat) = Complaint / Khiếu nại"],
        "vocab": [
            ("थाना", "thānā", "Police Station", "Đồn cảnh sát"),
            ("शिकायत", "shikāyat", "Complaint", "Đơn khiếu nại"),
            ("अधिकारी", "adhikārī", "Officer", "Cán bộ / Sĩ quan"),
            ("बयान", "bayān", "Statement", "Lời khai")
        ]
    },
    "emerg_10": {
        "title": "First Aid Help",
        "keyPoints": ["पट्टी (paṭṭī) = Bandage / Băng bó", "दर्द (dard) = Pain / Đau"],
        "vocab": [
            ("पट्टी", "paṭṭī", "Bandage", "Băng bó"),
            ("दर्द", "dard", "Pain", "Đau"),
            ("घाव", "ghāv", "Wound", "Vết thương"),
            ("दवाई", "davāī", "Medicine", "Thuốc")
        ]
    },
    "emerg_11": {
        "title": "Road Accident Help",
        "keyPoints": ["दुर्घटना (durghaṭnā) = Accident / Tai nạn", "चोट (choṭ) = Injury / Chấn thương"],
        "vocab": [
            ("दुर्घटना", "durghaṭnā", "Accident", "Tai nạn"),
            ("चोट", "choṭ", "Injury", "Chấn thương"),
            ("एंबुलेंस", "eṇbuleṇs", "Ambulance", "Xe cứu thương"),
            ("खून", "khūn", "Blood", "Máu")
        ]
    }
}

for ep_id, ep_info in new_episodes.items():
    nodes = generate_10_node_episode(
        title=ep_info["title"],
        key_points=ep_info["keyPoints"],
        vocab_list=ep_info["vocab"]
    )
    filename = f"episode_{ep_id}.json"
    with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
        json.dump(nodes, f, ensure_ascii=False, indent=2)

print("55 new episodes generated successfully!")
