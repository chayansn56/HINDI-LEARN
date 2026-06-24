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
            "description": f"Focus on this core specialized module: {title}.",
            "keyPoints": key_points
        },
        {
            "type": "TeachRule",
            "title": "Pronunciation & Resemblance",
            "explanation": "Pay close attention to Vietnamese phonetic overlaps and Hinglish spelling.",
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
            "instruction": "Match the items carefully",
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
                f"You mastered: {v1[0]} ({v1[1]})",
                f"You learned Vietnamese resemblance for: {v3[0]}"
            ]
        }
    ]
    return nodes

popups_data = {
    # Numbers Lab: 1 to 5
    "numbers_lab_1": {
        "title": "Numbers 0-100",
        "keyPoints": ["शून्य (shunya) = Zero (0) / Số không", "पचास (pachaas) = Fifty (50) / Năm mươi", "सौ (sau) = One Hundred (100) / Một trăm"],
        "vocab": [
            ("शून्य", "shunya", "Zero (0)", "Số không"),
            ("पचास", "pachaas", "Fifty (50)", "Năm mươi"),
            ("सौ", "sau", "One Hundred (100)", "Một trăm"),
            ("पच्चीस", "pachchees", "Twenty-Five (25)", "Hai mươi lăm")
        ]
    },
    "numbers_lab_2": {
        "title": "Hundreds 100-500",
        "keyPoints": ["एक सौ (ek sau) = 100 / Một trăm", "दो सौ पचास (do sau pachaas) = 250 / Hai trăm năm mươi", "पाँच सौ (paanch sau) = 500 / Năm trăm"],
        "vocab": [
            ("दो सौ", "do sau", "Two Hundred (200)", "Hai trăm"),
            ("तीन सौ", "teen sau", "Three Hundred (300)", "Ba trăm"),
            ("चार सौ", "chaar sau", "Four Hundred (400)", "Bốn trăm"),
            ("पाँच सौ", "paanch sau", "Five Hundred (500)", "Năm trăm")
        ]
    },
    "numbers_lab_3": {
        "title": "Hundreds 500-1000",
        "keyPoints": ["छह सौ (chheh sau) = 600 / Sáu trăm", "सात सौ पचास (saat sau pachaas) = 750 / Bảy trăm năm mươi", "एक हज़ार (ek hazaar) = 1000 / Một nghìn"],
        "vocab": [
            ("छह सौ", "chheh sau", "Six Hundred (600)", "Sáu trăm"),
            ("आठ सौ", "aath sau", "Eight Hundred (800)", "Tám trăm"),
            ("नौ सौ", "nau sau", "Nine Hundred (900)", "Chín trăm"),
            ("एक हज़ार", "ek hazaar", "One Thousand (1000)", "Một nghìn")
        ]
    },
    "numbers_lab_4": {
        "title": "Large Numbers Practice",
        "keyPoints": ["दो हज़ार (do hazaar) = 2000 / Hai nghìn", "दस हज़ार (das hazaar) = 10,000 / Mười nghìn"],
        "vocab": [
            ("एक हज़ार", "ek hazaar", "One Thousand (1000)", "Một nghìn"),
            ("दो हज़ार", "do hazaar", "Two Thousand (2000)", "Hai nghìn"),
            ("पाँच हज़ार", "paanch hazaar", "Five Thousand (5000)", "Năm nghìn"),
            ("दस हज़ार", "das hazaar", "Ten Thousand (10000)", "Mười nghìn")
        ]
    },
    "numbers_lab_5": {
        "title": "Numbers Challenge",
        "keyPoints": ["संख्या (sankhya) = Number / Con số", "गिनती (ginti) = Counting / Đếm số"],
        "vocab": [
            ("संख्या", "sankhya", "Number", "Con số"),
            ("गिनती", "ginti", "Counting", "Đếm số"),
            ("सौ", "sau", "Hundred", "Trăm"),
            ("हज़ार", "hazaar", "Thousand", "Nghìn")
        ]
    },

    # Alphabet Hub: 1 to 5
    "alphabets_lab_1": {
        "title": "Vowels & Vietnamese Sounds",
        "keyPoints": ["अ (a) = Sounds like Vietnamese 'ă'", "आ (aa) = Sounds like Vietnamese 'a'", "इ (i) = Sounds like Vietnamese 'i'"],
        "vocab": [
            ("अ", "a (short)", "Short 'a' (like ă in Vietnamese)", "Âm 'ă' ngắn"),
            ("आ", "aa (long)", "Long 'aa' (like a in Vietnamese)", "Âm 'a' dài"),
            ("इ", "i (short)", "Short 'i' (like i in Vietnamese)", "Âm 'i' ngắn"),
            ("ई", "ee (long)", "Long 'ee' (like i/y in Vietnamese)", "Âm 'i' dài")
        ]
    },
    "alphabets_lab_2": {
        "title": "Velars & Palatals",
        "keyPoints": ["क (ka) = Sounds like Vietnamese 'c/k'", "च (cha) = Sounds like Vietnamese 'ch'"],
        "vocab": [
            ("क", "ka", "Velar Ka (like c/k in Vietnamese)", "Âm 'c/k'"),
            ("ख", "kha", "Aspirated Kha (like kh in Vietnamese)", "Âm 'kh' bật hơi"),
            ("च", "cha", "Palatal Cha (like ch in Vietnamese)", "Âm 'ch'"),
            ("ज", "ja", "Palatal Ja (like d/gi in Vietnamese)", "Âm 'd/gi'")
        ]
    },
    "alphabets_lab_3": {
        "title": "Retroflex & Dentals",
        "keyPoints": ["त (ta) = Sounds like Vietnamese 't'", "न (na) = Sounds like Vietnamese 'n'"],
        "vocab": [
            ("त", "ta", "Dental Ta (like t in Vietnamese)", "Âm 't' răng"),
            ("द", "da", "Dental Da (like đ in Vietnamese)", "Âm 'đ' răng"),
            ("न", "na", "Dental Na (like n in Vietnamese)", "Âm 'n' răng"),
            ("ट", "ta (retroflex)", "Retroflex Ta (curled tongue)", "Âm 't' quặt lưỡi")
        ]
    },
    "alphabets_lab_4": {
        "title": "Labials & Semivowels",
        "keyPoints": ["प (pa) = Sounds like Vietnamese 'p'", "म (ma) = Sounds like Vietnamese 'm'", "ल (la) = Sounds like Vietnamese 'l'"],
        "vocab": [
            ("प", "pa", "Labial Pa (like p in Vietnamese)", "Âm 'p' môi"),
            ("ब", "ba", "Labial Ba (like b in Vietnamese)", "Âm 'b' môi"),
            ("म", "ma", "Labial Ma (like m in Vietnamese)", "Âm 'm' môi"),
            ("ल", "la", "Semivowel La (like l in Vietnamese)", "Âm 'l' lưỡi")
        ]
    },
    "alphabets_lab_5": {
        "title": "Conjuncts & Nuqta",
        "keyPoints": ["फ़ (fa) = Sounds like Vietnamese 'ph'", "ह (ha) = Sounds like Vietnamese 'h'"],
        "vocab": [
            ("फ़", "fa", "Nuqta Fa (like ph in Vietnamese)", "Âm 'ph' phất"),
            ("ह", "ha", "Glottal Ha (like h in Vietnamese)", "Âm 'h' họng"),
            ("य", "ya", "Palatal Ya (like d/y in Vietnamese)", "Âm 'd/y' nhẹ"),
            ("र", "ra", "Flapped Ra (rolled r in Vietnamese)", "Âm 'r' rung")
        ]
    }
}

for ep_id, ep_info in popups_data.items():
    nodes = generate_10_node_episode(
        title=ep_info["title"],
        key_points=ep_info["keyPoints"],
        vocab_list=ep_info["vocab"]
    )
    filename = f"episode_{ep_id}.json"
    with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
        json.dump(nodes, f, ensure_ascii=False, indent=2)

print("Popup groups created successfully!")
