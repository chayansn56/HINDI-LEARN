import json
import os

episodes_data = {
    "2": {
        "title": "First Greetings",
        "desc": "John meets Rahul at breakfast.",
        "word": "धन्यवाद",
        "translation_en": "Thank you",
        "translation_vi": "Cảm ơn",
        "hindi_phrase": "धन्यवाद।",
        "story_paras": [
            {"hindi": "राहुल आपको नाश्ता देता है।", "translation": "Rahul gives you breakfast."},
            {"hindi": "धन्यवाद। (Dhanyavaad)", "translation": "Thank you."}
        ],
        "story_q_en": "What did you say to Rahul?",
        "story_q_vi": "Bạn đã nói gì với Rahul?",
        "options_en": ["Thank you", "Sorry"],
        "options_vi": ["Cảm ơn", "Xin lỗi"]
    },
    "3": {
        "title": "Basic Needs",
        "desc": "Asking for water and tea.",
        "word": "पानी",
        "translation_en": "Water",
        "translation_vi": "Nước",
        "hindi_phrase": "पानी चाहिए।",
        "story_paras": [
            {"hindi": "बाहर बहुत गर्मी है।", "translation": "It is very hot outside."},
            {"hindi": "मुझे पानी चाहिए।", "translation": "I want water."}
        ],
        "story_q_en": "What do you want?",
        "story_q_vi": "Bạn muốn gì?",
        "options_en": ["Water", "Food"],
        "options_vi": ["Nước", "Thức ăn"]
    },
    "4": {
        "title": "Navigation",
        "desc": "Getting lost in Connaught Place.",
        "word": "कहाँ",
        "translation_en": "Where",
        "translation_vi": "Ở đâu",
        "hindi_phrase": "स्टेशन कहाँ है?",
        "story_paras": [
            {"hindi": "आप रास्ता भूल गए हैं।", "translation": "You have lost your way."},
            {"hindi": "मेट्रो स्टेशन कहाँ है?", "translation": "Where is the metro station?"}
        ],
        "story_q_en": "What are you asking for?",
        "story_q_vi": "Bạn đang hỏi về cái gì?",
        "options_en": ["Location", "Time"],
        "options_vi": ["Vị trí", "Thời gian"]
    },
    "5": {
        "title": "First Conversation",
        "desc": "Putting it all together.",
        "word": "हाँ",
        "translation_en": "Yes",
        "translation_vi": "Vâng / Có",
        "hindi_phrase": "हाँ, मैं ठीक हूँ।",
        "story_paras": [
            {"hindi": "दुकानदार पूछता है: क्या आप ठीक हैं?", "translation": "The shopkeeper asks: Are you okay?"},
            {"hindi": "हाँ, मैं ठीक हूँ।", "translation": "Yes, I am fine."}
        ],
        "story_q_en": "Are you okay?",
        "story_q_vi": "Bạn có ổn không?",
        "options_en": ["Yes", "No"],
        "options_vi": ["Vâng", "Không"]
    },
    "6": {
        "title": "Sounds That Don't Exist in English",
        "desc": "Learn aspirated sounds क/ख, ग/घ.",
        "word": "खाना",
        "translation_en": "Food/To eat",
        "translation_vi": "Thức ăn/Ăn",
        "hindi_phrase": "मुझे खाना चाहिए।",
        "story_paras": [
            {"hindi": "आप एक रेस्तरां में हैं।", "translation": "You are in a restaurant."},
            {"hindi": "मुझे खाना चाहिए।", "translation": "I want food."}
        ],
        "story_q_en": "What do you want?",
        "story_q_vi": "Bạn muốn gì?",
        "options_en": ["Food", "Water"],
        "options_vi": ["Thức ăn", "Nước"]
    },
    "7": {
        "title": "Tongue Position Matters",
        "desc": "Understand retroflex vs dental sounds.",
        "word": "ठंडा",
        "translation_en": "Cold",
        "translation_vi": "Lạnh",
        "hindi_phrase": "पानी ठंडा है।",
        "story_paras": [
            {"hindi": "आप पानी पीते हैं।", "translation": "You drink water."},
            {"hindi": "पानी बहुत ठंडा है।", "translation": "The water is very cold."}
        ],
        "story_q_en": "How is the water?",
        "story_q_vi": "Nước như thế nào?",
        "options_en": ["Cold", "Hot"],
        "options_vi": ["Lạnh", "Nóng"]
    },
    "8": {
        "title": "Vowels Part 1: Short Sounds",
        "desc": "Short vowels: अ, इ, उ, ऋ.",
        "word": "अब",
        "translation_en": "Now",
        "translation_vi": "Bây giờ",
        "hindi_phrase": "अब चलो।",
        "story_paras": [
            {"hindi": "देर हो रही है।", "translation": "It is getting late."},
            {"hindi": "अब चलो।", "translation": "Let's go now."}
        ],
        "story_q_en": "When should we go?",
        "story_q_vi": "Khi nào chúng ta nên đi?",
        "options_en": ["Now", "Later"],
        "options_vi": ["Bây giờ", "Sau đó"]
    },
    "9": {
        "title": "Vowels Part 2: Long Sounds",
        "desc": "Long vowels: आ, ई, ऊ, ए, ऐ, ओ, औ.",
        "word": "आज",
        "translation_en": "Today",
        "translation_vi": "Hôm nay",
        "hindi_phrase": "आज बहुत गर्मी है।",
        "story_paras": [
            {"hindi": "सूरज चमक रहा है।", "translation": "The sun is shining."},
            {"hindi": "आज बहुत गर्मी है।", "translation": "It is very hot today."}
        ],
        "story_q_en": "What is the weather like today?",
        "story_q_vi": "Thời tiết hôm nay như thế nào?",
        "options_en": ["Hot", "Cold"],
        "options_vi": ["Nóng", "Lạnh"]
    },
    "10": {
        "title": "Vowels Part 3: Special Sounds",
        "desc": "Special vowels: अं, अः.",
        "word": "सुंदर",
        "translation_en": "Beautiful",
        "translation_vi": "Đẹp",
        "hindi_phrase": "यह सुंदर है।",
        "story_paras": [
            {"hindi": "आप एक महल देखते हैं।", "translation": "You see a palace."},
            {"hindi": "यह बहुत सुंदर है।", "translation": "It is very beautiful."}
        ],
        "story_q_en": "How is the palace?",
        "story_q_vi": "Cung điện như thế nào?",
        "options_en": ["Beautiful", "Ugly"],
        "options_vi": ["Đẹp", "Xấu"]
    }
}

out_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"

for ep_num, data in episodes_data.items():
    content = [
        {
            "type": "Introduction",
            "title": data["title"],
            "description": data["desc"],
            "keyPoints": ["Pay attention to the vocabulary.", "Practice speaking aloud."]
        },
        {
            "type": "StoryMode",
            "title_en": data["title"],
            "title_vi": data["title"],
            "paragraphs": data["story_paras"],
            "question_en": data["story_q_en"],
            "question_vi": data["story_q_vi"],
            "options": [
                {"text": data["options_en"][0], "isCorrect": True},
                {"text": data["options_en"][1], "isCorrect": False}
            ]
        },
        {
            "type": "TeachRule",
            "title": "Important Word",
            "explanation": f"The word {data['word']} means {data['translation_en']} ({data['translation_vi']}).",
            "simpleRule": f"{data['word']} = {data['translation_en']}"
        },
        {
            "type": "VocabularyContext",
            "word": data["word"],
            "translation": data["translation_en"],
            "contextSentenceHindi": data["hindi_phrase"],
            "contextSentenceTranslation": data["translation_en"],
            "memoryTrick": f"Remember {data['word']} as {data['translation_en']}."
        },
        {
            "type": "Listening",
            "audio": data["word"],
            "translation_en": data["translation_en"],
            "options_en": data["options_en"] + ["Something else", "Nothing"],
            "translation_vi": data["translation_vi"],
            "options_vi": data["options_vi"] + ["Khác", "Không có gì"]
        },
        {
            "type": "Speaking",
            "hindiPhrase": data["hindi_phrase"],
            "translation": data["translation_en"]
        }
    ]
    
    file_path = os.path.join(out_dir, f"episode_0_{ep_num}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(content, f, indent=2, ensure_ascii=False)

print("Generated episodes 2 to 10 successfully.")
