import json
import os

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(output_dir, exist_ok=True)

# Helper function to save JSON
def save_episode(id, nodes):
    with open(os.path.join(output_dir, f"{id}.json"), "w", encoding="utf-8") as f:
        json.dump(nodes, f, ensure_ascii=False, indent=2)

print("Generating rich content for special modules...")

# --- PRONUNCIATION LAB ---
pron_1 = [
  {
    "type": "Introduction",
    "title": "Aspirated vs Unaspirated",
    "description": "Learn the difference between sounds with and without a burst of air.",
    "keyPoints": ["क (ka) vs ख (kha)", "ग (ga) vs घ (gha)"]
  },
  {
    "type": "TeachRule",
    "title": "The Hand Test",
    "explanation": "Hold your hand in front of your mouth. Say 'ka' (no air). Say 'kha' (air burst).",
    "simpleRule": "Air burst = Aspirated."
  },
  {
    "type": "Flashcard",
    "hindi": "कबूतर",
    "transliteration": "kabutar",
    "english": "Pigeon (No air)",
    "vietnamese": "Chim bồ câu (Không bật hơi)"
  },
  {
    "type": "Flashcard",
    "hindi": "खरगोश",
    "transliteration": "khargosh",
    "english": "Rabbit (Air burst)",
    "vietnamese": "Con thỏ (Có bật hơi)"
  },
  {
    "type": "MultipleChoice",
    "prompt_en": "Which word has an aspirated sound (air burst)?",
    "prompt_vi": "Từ nào có âm bật hơi?",
    "text": "Aspirated Sound",
    "subtext": "Listen to the air",
    "answer": "खरगोश",
    "options": ["कबूतर", "खरगोश", "गाय", "पानी"]
  }
]
save_episode("episode_pron_1", pron_1)

pron_2 = [
  {
    "type": "Introduction",
    "title": "Retroflex vs Dental",
    "description": "The difference between rolling your tongue back and touching your teeth.",
    "keyPoints": ["Retroflex: ट (ṭa), ठ (ṭha)", "Dental: त (ta), थ (tha)"]
  },
  {
    "type": "Flashcard",
    "hindi": "टमाटर",
    "transliteration": "ṭamāṭar",
    "english": "Tomato (Retroflex)",
    "vietnamese": "Cà chua (Âm quặt lưỡi)"
  },
  {
    "type": "Flashcard",
    "hindi": "तरबूज",
    "transliteration": "tarbūj",
    "english": "Watermelon (Dental)",
    "vietnamese": "Dưa hấu (Âm răng)"
  }
]
save_episode("episode_pron_2", pron_2)
for i in range(3, 9): save_episode(f"episode_pron_{i}", pron_2)

# --- SPEAKING ---
speak_1 = [
  {
    "type": "Introduction",
    "title": "Greetings & Farewells",
    "description": "Learn to say hello and goodbye properly in Hindi.",
    "keyPoints": ["Namaste is universal.", "Phir milenge means see you later."]
  },
  {
    "type": "Flashcard",
    "hindi": "नमस्ते",
    "transliteration": "Namaste",
    "english": "Hello / Goodbye",
    "vietnamese": "Xin chào / Tạm biệt"
  },
  {
    "type": "Flashcard",
    "hindi": "फिर मिलेंगे",
    "transliteration": "Phir milenge",
    "english": "See you later",
    "vietnamese": "Hẹn gặp lại"
  },
  {
    "type": "DialogueMode",
    "title": "Meeting a Friend",
    "lines": [
      {"speaker": "Rahul", "hindi": "नमस्ते!", "translation": "Hello!"},
      {"speaker": "Priya", "hindi": "नमस्ते, क्या हाल है?", "translation": "Hello, how are you?"}
    ]
  }
]
save_episode("episode_speak_1", speak_1)

speak_2 = [
  {
    "type": "Introduction",
    "title": "Introducing Yourself",
    "description": "Tell people your name and where you are from.",
    "keyPoints": ["Mera naam... hai (My name is...)", "Main... se hoon (I am from...)"]
  },
  {
    "type": "Flashcard",
    "hindi": "मेरा नाम जॉन है।",
    "transliteration": "Mera naam John hai.",
    "english": "My name is John.",
    "vietnamese": "Tên tôi là John."
  }
]
save_episode("episode_speak_2", speak_2)
for i in range(3, 11): save_episode(f"episode_speak_{i}", speak_2)

# --- GRAMMAR ---
gram_1 = [
  {
    "type": "Introduction",
    "title": "The Verb 'To Be'",
    "description": "Learn how to say am, is, and are in Hindi (होना).",
    "keyPoints": ["I am = मैं हूँ", "You are = तुम हो", "He/She/It is = वह है"]
  },
  {
    "type": "GrammarRule",
    "title_en": "Present Tense 'To Be'",
    "title_vi": "Động từ 'To Be' ở thì hiện tại",
    "content_en": "मैं हूँ (I am)\nतुम हो (You are)\nवह है (He/She is)\nहम हैं (We are)",
    "content_vi": "मैं हूँ (Tôi là)\nतुम हो (Bạn là)\nवह है (Anh ấy/Cô ấy là)\nहम हैं (Chúng tôi là)"
  },
  {
    "type": "MultipleChoice",
    "prompt_en": "Translate: 'I am'",
    "prompt_vi": "Dịch: 'Tôi là'",
    "text": "I am",
    "subtext": "",
    "answer": "मैं हूँ",
    "options": ["मैं हूँ", "तुम हो", "वह है", "हम हैं"]
  }
]
save_episode("episode_gram_1", gram_1)

gram_2 = [
  {
    "type": "Introduction",
    "title": "Sentence Structure (SOV)",
    "description": "Hindi sentences follow a Subject-Object-Verb order.",
    "keyPoints": ["Subject comes first.", "Verb comes last."]
  },
  {
    "type": "GrammarRule",
    "title_en": "Subject Object Verb",
    "title_vi": "Chủ ngữ Tân ngữ Động từ",
    "content_en": "English: I (S) eat (V) an apple (O).\nHindi: मैं (S) सेब (O) खाता हूँ (V).",
    "content_vi": "Tiếng Anh: I (S) eat (V) an apple (O).\nTiếng Hindi: मैं (S) सेब (O) खाता हूँ (V)."
  }
]
save_episode("episode_gram_2", gram_2)
for i in range(3, 13): save_episode(f"episode_gram_{i}", gram_2)

# --- LISTENING ---
listen_1 = [
  {
    "type": "Introduction",
    "title": "Single Words (Slow)",
    "description": "Practice your listening comprehension with single words spoken slowly.",
    "keyPoints": ["Listen carefully to the pronunciation.", "Pay attention to vowels."]
  },
  {
    "type": "Listening",
    "audio": "किताब",
    "translation_en": "Book",
    "options_en": ["Book", "Pen", "Table", "Chair"],
    "translation_vi": "Sách",
    "options_vi": ["Sách", "Bút", "Bàn", "Ghế"]
  }
]
save_episode("episode_listen_1", listen_1)
for i in range(2, 7): save_episode(f"episode_listen_{i}", listen_1)

# --- WRITING ---
write_1 = [
  {
    "type": "Introduction",
    "title": "Tracing Vowels",
    "description": "Practice writing Hindi vowels (Svar).",
    "keyPoints": ["Follow the stroke order.", "Start with अ."]
  },
  {
    "type": "Drawing",
    "letterToDraw": "अ",
    "hint_en": "Draw the letter 'A' (a in anar)",
    "hint_vi": "Viết chữ 'A' (a trong anar)"
  }
]
save_episode("episode_write_1", write_1)
for i in range(2, 9): save_episode(f"episode_write_{i}", write_1)

# --- CULTURE ---
culture_1 = [
  {
    "type": "Introduction",
    "title": "Indian Festivals",
    "description": "Learn about the vibrant festivals celebrated in India.",
    "keyPoints": ["Diwali: Festival of Lights", "Holi: Festival of Colors"]
  },
  {
    "type": "CulturalTip",
    "title_en": "Diwali (दिवाली)",
    "title_vi": "Lễ hội Diwali (दिवाली)",
    "content_en": "Diwali is the festival of lights, celebrating the triumph of light over darkness.",
    "content_vi": "Diwali là lễ hội ánh sáng, kỷ niệm chiến thắng của ánh sáng trước bóng tối."
  }
]
save_episode("episode_culture_1", culture_1)
for i in range(2, 7): save_episode(f"episode_culture_{i}", culture_1)

# --- TRAVEL ---
travel_1 = [
  {
    "type": "Introduction",
    "title": "At the Airport",
    "description": "Essential phrases for navigating the airport in India.",
    "keyPoints": ["Baggage claim", "Immigration", "Taxis"]
  },
  {
    "type": "Flashcard",
    "hindi": "मेरा सामान कहाँ है?",
    "transliteration": "Mera saamaan kahan hai?",
    "english": "Where is my luggage?",
    "vietnamese": "Hành lý của tôi ở đâu?"
  }
]
save_episode("episode_travel_1", travel_1)
for i in range(2, 9): save_episode(f"episode_travel_{i}", travel_1)

# --- BUSINESS ---
business_1 = [
  {
    "type": "Introduction",
    "title": "Office Vocabulary",
    "description": "Learn words related to the workplace and office environment.",
    "keyPoints": ["Meeting", "Boss", "Colleague"]
  },
  {
    "type": "Flashcard",
    "hindi": "बैठक",
    "transliteration": "Baithak",
    "english": "Meeting",
    "vietnamese": "Cuộc họp"
  }
]
save_episode("episode_business_1", business_1)
for i in range(2, 7): save_episode(f"episode_business_{i}", business_1)

# --- BOLLYWOOD ---
bollywood_1 = [
  {
    "type": "Introduction",
    "title": "Common Movie Slang",
    "description": "Understand the slang words frequently used in Bollywood movies.",
    "keyPoints": ["Yaar", "Jugaad", "Bhai"]
  },
  {
    "type": "Flashcard",
    "hindi": "यार",
    "transliteration": "Yaar",
    "english": "Friend / Buddy",
    "vietnamese": "Bạn bè / Bạn thân"
  }
]
save_episode("episode_bollywood_1", bollywood_1)
for i in range(2, 7): save_episode(f"episode_bollywood_{i}", bollywood_1)

# --- WHATSAPP ---
whatsapp_1 = [
  {
    "type": "Introduction",
    "title": "Text Abbreviations",
    "description": "Learn common abbreviations used in Hindi texting.",
    "keyPoints": ["Kya haal hai -> kyu hal h", "Theek hai -> thk h"]
  },
  {
    "type": "Flashcard",
    "hindi": "सीन क्या है?",
    "transliteration": "Scene kya hai?",
    "english": "What's the plan?",
    "vietnamese": "Kế hoạch là gì?"
  }
]
save_episode("episode_whatsapp_1", whatsapp_1)
for i in range(2, 5): save_episode(f"episode_whatsapp_{i}", whatsapp_1)

print("Finished generating special module episodes.")
