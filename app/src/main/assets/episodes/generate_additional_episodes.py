import json
import os
import random

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(output_dir, exist_ok=True)

def generate_10_node_episode(topic, english_title, key_points, vocab_list):
    # Ensure exactly 4-6 items
    v1, v2, v3, v4 = vocab_list[:4]
    nodes = [
        {
            "type": "Introduction",
            "title": english_title,
            "description": f"Learn critical vocabulary for: {english_title}.",
            "keyPoints": key_points
        },
        {
            "type": "TeachRule",
            "title": "Grammar & Usage Note",
            "explanation": "Pay close attention to pronunciation and context.",
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
            "prompt_en": f"Choose correct translation for '{v1[0]}'",
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
            "instruction": "Match the words with their translations",
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

# Define the new episodes data
additional_data = {
    # Pronunciation Lab
    "pron_9": {
        "title": "Intonation in Statements",
        "keyPoints": ["क्या? (kyā?) = What? / Cái gì?", "हाँ! (hā̃!) = Yes! / Vâng!", "नहीं। (nahī̃.) = No. / Không."],
        "vocab": [
            ("क्या?", "kyā?", "What?", "Cái gì?"),
            ("हाँ!", "hā̃!", "Yes!", "Vâng!"),
            ("नहीं।", "nahī̃.", "No.", "Không."),
            ("ठीक है।", "ṭhīk hai.", "Okay.", "Được rồi.")
        ]
    },
    "pron_10": {
        "title": "Pronunciation Review & Test",
        "keyPoints": ["क (ka) vs ख (kha)", "ट (ṭa) vs त (ta)"],
        "vocab": [
            ("कमल", "kamal", "Lotus", "Hoa sen"),
            ("खरगोश", "khargosh", "Rabbit", "Con thỏ"),
            ("टमाटर", "ṭamāṭar", "Tomato", "Cà chua"),
            ("तारा", "tārā", "Star", "Ngôi sao")
        ]
    },
    # Speaking
    "speak_11": {
        "title": "Ordering Food Online",
        "keyPoints": ["खाना (khānā) = Food / Thức ăn", "ऑर्डर (ôrḍar) = Order / Đặt hàng", "डिलीवरी (ḍilīvarī) = Delivery / Giao hàng"],
        "vocab": [
            ("खाना", "khānā", "Food", "Thức ăn"),
            ("ऑर्डर", "ôrḍar", "Order", "Đặt hàng"),
            ("डिलीवरी", "ḍilīvarī", "Delivery", "Giao hàng"),
            ("पैसे", "paise", "Money / Coins", "Tiền")
        ]
    },
    "speak_12": {
        "title": "Advanced Conversations",
        "keyPoints": ["बातचीत (bātchīt) = Conversation / Cuộc trò chuyện", "मदद (madad) = Help / Giúp đỡ"],
        "vocab": [
            ("बातचीत", "bātchīt", "Conversation", "Cuộc trò chuyện"),
            ("मदद", "madad", "Help", "Giúp đỡ"),
            ("शुक्रिया", "shukriyā", "Thank you (Urdu origin)", "Cảm ơn"),
            ("दोस्त", "dost", "Friend", "Bạn bè")
        ]
    },
    # Grammar
    "gram_13": {
        "title": "Subjunctive Mood",
        "keyPoints": ["शायद (shāyad) = Maybe / Có lẽ", "अगर (agar) = If / Nếu"],
        "vocab": [
            ("शायद", "shāyad", "Maybe", "Có lẽ"),
            ("अगर", "agar", "If", "Nếu"),
            ("तो", "to", "Then", "Thì"),
            ("होगा", "hogā", "Will be (M)", "Sẽ là (Nam)")
        ]
    },
    "gram_14": {
        "title": "Passive Voice",
        "keyPoints": ["किया गया (kiyā gayā) = Was done / Đã được làm"],
        "vocab": [
            ("किया गया", "kiyā gayā", "Was done", "Đã được làm"),
            ("कहा गया", "kahā gayā", "Was said", "Đã được nói"),
            ("लिखा गया", "likhā gayā", "Was written", "Đã được viết"),
            ("बनाया गया", "banāyā gayā", "Was made", "Đã được tạo ra")
        ]
    },
    # Listening
    "listen_7": {
        "title": "Weather Report Listening",
        "keyPoints": ["मौसम (mausam) = Weather / Thời tiết", "बारिश (bārish) = Rain / Mưa"],
        "vocab": [
            ("मौसम", "mausam", "Weather", "Thời tiết"),
            ("बारिश", "bārish", "Rain", "Mưa"),
            ("धूप", "dhūp", "Sunshine", "Ánh nắng"),
            ("गर्मी", "garmī", "Heat / Summer", "Nóng / Mùa hè")
        ]
    },
    "listen_8": {
        "title": "Short News Podcasts",
        "keyPoints": ["समाचार (samāchār) = News / Tin tức", "रेडियो (reḍiyo) = Radio / Đài phát thanh"],
        "vocab": [
            ("समाचार", "samāchār", "News", "Tin tức"),
            ("रेडियो", "reḍiyo", "Radio", "Đài phát thanh"),
            ("आज", "āj", "Today", "Hôm nay"),
            ("कल", "kal", "Tomorrow / Yesterday", "Ngày mai / Hôm qua")
        ]
    },
    # Writing
    "write_9": {
        "title": "Devanagari Speed Writing",
        "keyPoints": ["लिखना (likhnā) = To write / Viết", "तेज़ (tez) = Fast / Nhanh"],
        "vocab": [
            ("लिखना", "likhnā", "To write", "Viết"),
            ("तेज़", "tez", "Fast", "Nhanh"),
            ("कलम", "kalam", "Pen", "Bút"),
            ("काग़ज़", "kāġaz", "Paper", "Giấy")
        ]
    },
    "write_10": {
        "title": "Advanced Conjuncts Writing",
        "keyPoints": ["संयुक्त (sanyukt) = Combined / Kết hợp"],
        "vocab": [
            ("नमस्ते", "namaste", "Hello / Respectful Bow", "Xin chào"),
            ("सच्चा", "sachchā", "True / Honest", "Đích thực"),
            ("दिल्ली", "dillī", "Delhi", "Delhi"),
            ("बिल्ली", "billī", "Cat", "Con mèo")
        ]
    },
    # Culture
    "culture_7": {
        "title": "Indian Weddings",
        "keyPoints": ["शादी (shādī) = Wedding / Đám cưới", "दुल्हन (dulhan) = Bride / Cô dâu", "दूल्हा (dūlhā) = Groom / Chú rể"],
        "vocab": [
            ("शादी", "shādī", "Wedding", "Đám cưới"),
            ("दुल्हन", "dulhan", "Bride", "Cô dâu"),
            ("दूल्हा", "dūlhā", "Groom", "Chú rể"),
            ("मेहंदी", "mehandī", "Henna", "Hình xăm Henna")
        ]
    },
    "culture_8": {
        "title": "Traditional Music & Dance",
        "keyPoints": ["संगीत (sangeet) = Music / Âm nhạc", "नृत्य (nritya) = Dance / Nhảy múa"],
        "vocab": [
            ("संगीत", "sangeet", "Music", "Âm nhạc"),
            ("नृत्य", "nritya", "Dance", "Nhảy múa"),
            ("सितार", "sitār", "Sitar (instrument)", "Đàn Sitar"),
            ("ढोलक", "ḍholak", "Dholak (drum)", "Trống Dholak")
        ]
    },
    # Travel
    "travel_9": {
        "title": "Bargaining at a Bazaar",
        "keyPoints": ["दाम (dām) = Price / Giá cả", "कम करो (kam karo) = Reduce it / Giảm giá đi"],
        "vocab": [
            ("दाम", "dām", "Price", "Giá cả"),
            ("कम करो", "kam karo", "Reduce it", "Giảm giá đi"),
            ("सस्ता", "sastā", "Cheap", "Rẻ"),
            ("महँगा", "mahaṅgā", "Expensive", "Đắt")
        ]
    },
    "travel_10": {
        "title": "Renting a Scooter",
        "keyPoints": ["किराया (kirāyā) = Rent / Thuê", "गाड़ी (gāṛī) = Vehicle / Xe cộ"],
        "vocab": [
            ("किराया", "kirāyā", "Rent", "Thuê"),
            ("गाड़ी", "gāṛī", "Vehicle / Car / Scooter", "Xe cộ"),
            ("पेट्रोल", "peṭrol", "Petrol / Gasoline", "Xăng dầu"),
            ("चाबी", "chābī", "Key", "Chìa khóa")
        ]
    },
    # Business
    "business_7": {
        "title": "Job Interview in Hindi",
        "keyPoints": ["नौकरी (naukrī) = Job / Công việc", "साक्षात्कार (sākshātkār) = Interview / Phỏng vấn"],
        "vocab": [
            ("नौकरी", "naukrī", "Job", "Công việc"),
            ("साक्षात्कार", "sākshātkār", "Interview", "Phỏng vấn"),
            ("योग्यता", "yogyatā", "Qualification", "Trình độ chuyên môn"),
            ("अनुभव", "anubhav", "Experience", "Kinh nghiệm")
        ]
    },
    "business_8": {
        "title": "Customer Service Dialogue",
        "keyPoints": ["ग्राहक (grāhak) = Customer / Khách hàng", "सेवा (sevā) = Service / Dịch vụ"],
        "vocab": [
            ("ग्राहक", "grāhak", "Customer", "Khách hàng"),
            ("सेवा", "sevā", "Service", "Dịch vụ"),
            ("समस्या", "samasyā", "Problem", "Vấn đề"),
            ("समाधान", "samādhān", "Solution", "Giải pháp")
        ]
    },
    # Bollywood
    "bollywood_7": {
        "title": "Classic Melodrama",
        "keyPoints": ["संवाद (samvād) = Dialogue / Lời thoại", "प्यार (pyār) = Love / Tình yêu"],
        "vocab": [
            ("संवाद", "samvād", "Dialogue", "Lời thoại"),
            ("प्यार", "pyār", "Love", "Tình yêu"),
            ("ज़िंदगी", "zindagī", "Life", "Cuộc sống"),
            ("दिल", "dil", "Heart", "Trái tim")
        ]
    },
    "bollywood_8": {
        "title": "Superhit Song Analysis",
        "keyPoints": ["गीत (geet) = Song / Bài hát", "गायक (gāyak) = Singer / Ca sĩ"],
        "vocab": [
            ("गीत", "geet", "Song", "Bài hát"),
            ("गायक", "gāyak", "Singer", "Ca sĩ"),
            ("धुन", "dhun", "Tune / Melody", "Giai điệu"),
            ("याद", "yād", "Memory / Remembrance", "Kỷ niệm / Nỗi nhớ")
        ]
    },
    # WhatsApp
    "whatsapp_5": {
        "title": "Dating App Slang",
        "keyPoints": ["बंदी (bandī) = Girl / Bạn gái", "बंदा (bandā) = Guy / Bạn trai"],
        "vocab": [
            ("बंदी", "bandī", "Girl / Girlfriend", "Bạn gái"),
            ("बंदा", "bandā", "Guy / Boyfriend", "Bạn trai"),
            ("क्रश", "krash", "Crush", "Người thầm thương"),
            ("पटोला", "paṭolā", "Beautiful girl (Punjabi slang)", "Cô gái xinh đẹp")
        ]
    },
    "whatsapp_6": {
        "title": "Social Media Buzzwords",
        "keyPoints": ["वायरल (vāiral) = Viral / Lan truyền", "फॉलो (fôlo) = Follow / Theo dõi"],
        "vocab": [
            ("वायरल", "vāiral", "Viral", "Lan truyền"),
            ("फॉलो", "fôlo", "Follow", "Theo dõi"),
            ("लाइक", "lāik", "Like", "Thích"),
            ("कमेंट", "kameṇṭ", "Comment", "Bình luận")
        ]
    },
    # Emergency
    "emerg_5": {
        "title": "Natural Disasters",
        "keyPoints": ["बाढ़ (bāṛh) = Flood / Lũ lụt", "भूकंप (bhūkamp) = Earthquake / Động đất"],
        "vocab": [
            ("बाढ़", "bāṛh", "Flood", "Lũ lụt"),
            ("भूकंप", "bhūkamp", "Earthquake", "Động đất"),
            ("तूफ़ान", "tūfān", "Storm", "Bão"),
            ("बचाओ!", "bachāo!", "Save me! / Help!", "Cứu tôi với!")
        ]
    },
    "emerg_6": {
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

for ep_id, ep_info in additional_data.items():
    nodes = generate_10_node_episode(
        topic=ep_info["title"],
        english_title=ep_info["title"],
        key_points=ep_info["keyPoints"],
        vocab_list=ep_info["vocab"]
    )
    filename = f"episode_{ep_id}.json"
    with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
        json.dump(nodes, f, ensure_ascii=False, indent=2)

print("Additional episodes generated successfully!")
