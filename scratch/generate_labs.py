# -*- coding: utf-8 -*-
import json
import os

hindi_path = '/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes'
english_path = '/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/english_episodes'

os.makedirs(hindi_path, exist_ok=True)
os.makedirs(english_path, exist_ok=True)

# 1. ENGLISH LABS DATA
english_alphabets_lab = [
    {
        "id": "episode_alphabets_lab_1", "title": "The English Alphabet & Vowels",
        "vocab": [
            {"eng": "A", "vi": "Chữ A"},
            {"eng": "E", "vi": "Chữ E"},
            {"eng": "I", "vi": "Chữ I"},
            {"eng": "O", "vi": "Chữ O"},
            {"eng": "U", "vi": "Chữ U"},
            {"eng": "Vowel", "vi": "Nguyên âm"}
        ]
    },
    {
        "id": "episode_alphabets_lab_2", "title": "Consonants & Basic Phonics",
        "vocab": [
            {"eng": "B", "vi": "Chữ B"},
            {"eng": "C", "vi": "Chữ C"},
            {"eng": "D", "vi": "Chữ D"},
            {"eng": "F", "vi": "Chữ F"},
            {"eng": "G", "vi": "Chữ G"},
            {"eng": "Consonant", "vi": "Phụ âm"}
        ]
    },
    {
        "id": "episode_alphabets_lab_3", "title": "Digraphs & Blends (ch, sh, th, wh)",
        "vocab": [
            {"eng": "Chat", "vi": "Trò chuyện (âm ch)"},
            {"eng": "Ship", "vi": "Tàu thủy (âm sh)"},
            {"eng": "Thin", "vi": "Mỏng / Gầy (âm th)"},
            {"eng": "White", "vi": "Màu trắng (âm wh)"},
            {"eng": "Digraph", "vi": "Chữ ghép (hai chữ cái ghép lại thành một âm)"}
        ]
    },
    {
        "id": "episode_alphabets_lab_4", "title": "Silent Letters & Magic E",
        "vocab": [
            {"eng": "Write", "vi": "Viết (âm w câm)"},
            {"eng": "Know", "vi": "Biết / Hiểu (âm k câm)"},
            {"eng": "Knight", "vi": "Hiệp sĩ (âm k và gh câm)"},
            {"eng": "Climb", "vi": "Leo trèo (âm b câm)"},
            {"eng": "Magic E", "vi": "Âm E câm (làm kéo dài nguyên âm trước nó)"}
        ]
    },
    {
        "id": "episode_alphabets_lab_5", "title": "Pronunciation Masterclass",
        "vocab": [
            {"eng": "Phonics", "vi": "Ngữ âm"},
            {"eng": "Accent", "vi": "Giọng điệu / Trọng âm"},
            {"eng": "Stress", "vi": "Trọng âm từ"},
            {"eng": "Fluent", "vi": "Trôi chảy"},
            {"eng": "Pronounce", "vi": "Phát âm"}
        ]
    }
]

english_numbers_lab = [
    {
        "id": "episode_numbers_lab_1", "title": "Numbers 1-10",
        "vocab": [
            {"eng": "One", "vi": "Số một (1)"},
            {"eng": "Two", "vi": "Số hai (2)"},
            {"eng": "Three", "vi": "Số ba (3)"},
            {"eng": "Four", "vi": "Số bốn (4)"},
            {"eng": "Five", "vi": "Số năm (5)"},
            {"eng": "Ten", "vi": "Số mười (10)"}
        ]
    },
    {
        "id": "episode_numbers_lab_2", "title": "Numbers 11-100",
        "vocab": [
            {"eng": "Eleven", "vi": "Mười một (11)"},
            {"eng": "Twenty", "vi": "Hai mươi (20)"},
            {"eng": "Thirty", "vi": "Ba mươi (30)"},
            {"eng": "Fifty", "vi": "Năm mươi (50)"},
            {"eng": "Hundred", "vi": "Một trăm (100)"}
        ]
    },
    {
        "id": "episode_numbers_lab_3", "title": "Hundreds 100-1000",
        "vocab": [
            {"eng": "Two hundred", "vi": "Hai trăm (200)"},
            {"eng": "Five hundred", "vi": "Năm trăm (500)"},
            {"eng": "Thousand", "vi": "Một nghìn (1000)"},
            {"eng": "Nine hundred", "vi": "Chín trăm (900)"}
        ]
    },
    {
        "id": "episode_numbers_lab_4", "title": "Large Numbers",
        "vocab": [
            {"eng": "Ten thousand", "vi": "Mười nghìn (10.000)"},
            {"eng": "Million", "vi": "Triệu (1.000.000)"},
            {"eng": "Billion", "vi": "Tỷ (1.000.000.000)"},
            {"eng": "Trillion", "vi": "Nghìn tỷ"}
        ]
    },
    {
        "id": "episode_numbers_lab_5", "title": "Numbers Challenge",
        "vocab": [
            {"eng": "Plus", "vi": "Cộng (+)"},
            {"eng": "Minus", "vi": "Trừ (-)"},
            {"eng": "Equal", "vi": "Bằng (=)"},
            {"eng": "Total", "vi": "Tổng số"},
            {"eng": "Math", "vi": "Toán học"}
        ]
    }
]

# 2. HINDI LABS DATA
hindi_alphabets_lab = [
    {
        "id": "episode_alphabets_lab_1", "title": "Vowels & Vietnamese Sounds",
        "vocab": [
            {"hi": "अ", "vi": "Nguyên âm ngắn /a/", "en": "Short Vowel A", "trans": "a"},
            {"hi": "आ", "vi": "Nguyên âm dài /aa/", "en": "Long Vowel AA", "trans": "aa"},
            {"hi": "इ", "vi": "Nguyên âm ngắn /i/", "en": "Short Vowel I", "trans": "i"},
            {"hi": "ई", "vi": "Nguyên âm dài /ee/", "en": "Long Vowel EE", "trans": "ee"},
            {"hi": "उ", "vi": "Nguyên âm ngắn /u/", "en": "Short Vowel U", "trans": "u"}
        ]
    },
    {
        "id": "episode_alphabets_lab_2", "title": "Velars & Palatals",
        "vocab": [
            {"hi": "क", "vi": "Âm /k/ nhẹ (cổ họng)", "en": "Velar K", "trans": "ka"},
            {"hi": "ख", "vi": "Âm /kh/ bật hơi", "en": "Aspirated KH", "trans": "kha"},
            {"hi": "ग", "vi": "Âm /g/ (cổ họng)", "en": "Velar G", "trans": "ga"},
            {"hi": "घ", "vi": "Âm /gh/ bật hơi", "en": "Aspirated GH", "trans": "gha"},
            {"hi": "च", "vi": "Âm /ch/ (vòm họng)", "en": "Palatal CH", "trans": "cha"}
        ]
    },
    {
        "id": "episode_alphabets_lab_3", "title": "Retroflex & Dentals",
        "vocab": [
            {"hi": "ट", "vi": "Âm /t/ uốn lưỡi ngược", "en": "Retroflex T", "trans": "ta"},
            {"hi": "ठ", "vi": "Âm /th/ uốn lưỡi bật hơi", "en": "Aspirated Retroflex TH", "trans": "tha"},
            {"hi": "ड", "vi": "Âm /d/ uốn lưỡi", "en": "Retroflex D", "trans": "da"},
            {"hi": "ढ", "vi": "Âm /dh/ uốn lưỡi bật hơi", "en": "Aspirated Retroflex DH", "trans": "dha"},
            {"hi": "त", "vi": "Âm /t/ chạm răng", "en": "Dental T", "trans": "ta dental"}
        ]
    },
    {
        "id": "episode_alphabets_lab_4", "title": "Labials & Semivowels",
        "vocab": [
            {"hi": "प", "vi": "Âm /p/ bằng môi", "en": "Labial P", "trans": "pa"},
            {"hi": "फ", "vi": "Âm /ph/ bật hơi bằng môi", "en": "Aspirated PH", "trans": "pha"},
            {"hi": "ब", "vi": "Âm /b/ bằng môi", "en": "Labial B", "trans": "ba"},
            {"hi": "भ", "vi": "Âm /bh/ bật hơi", "en": "Aspirated BH", "trans": "bha"},
            {"hi": "म", "vi": "Âm /m/ bằng môi", "en": "Labial M", "trans": "ma"}
        ]
    },
    {
        "id": "episode_alphabets_lab_5", "title": "Conjuncts & Nuqta",
        "vocab": [
            {"hi": "क्ष", "vi": "Phụ âm ghép ksha", "en": "Conjunct KSHA", "trans": "ksha"},
            {"hi": "त्र", "vi": "Phụ âm ghép tra", "en": "Conjunct TRA", "trans": "tra"},
            {"hi": "ज्ञ", "vi": "Phụ âm ghép gya", "en": "Conjunct GYA", "trans": "gya"},
            {"hi": "ज़", "vi": "Âm /z/ mượn từ tiếng Ba Tư", "en": "Nuqta ZA", "trans": "za"},
            {"hi": "फ़", "vi": "Âm /f/ mượn từ tiếng Ba Tư", "en": "Nuqta FA", "trans": "fa"}
        ]
    }
]

hindi_numbers_lab = [
    {
        "id": "episode_numbers_lab_1", "title": "Numbers 1-10",
        "vocab": [
            {"hi": "एक", "vi": "Số một (1)", "en": "One", "trans": "ek"},
            {"hi": "दो", "vi": "Số hai (2)", "en": "Two", "trans": "do"},
            {"hi": "तीन", "vi": "Số ba (3)", "en": "Three", "trans": "teen"},
            {"hi": "चार", "vi": "Số bốn (4)", "en": "Four", "trans": "chaar"},
            {"hi": "पाँच", "vi": "Số năm (5)", "en": "Five", "trans": "paanch"},
            {"hi": "दस", "vi": "Số mười (10)", "en": "Ten", "trans": "das"}
        ]
    },
    {
        "id": "episode_numbers_lab_2", "title": "Numbers 11-100",
        "vocab": [
            {"hi": "ग्यारह", "vi": "Mười một (11)", "en": "Eleven", "trans": "gyarah"},
            {"hi": "बीस", "vi": "Hai mươi (20)", "en": "Twenty", "trans": "bees"},
            {"hi": "तीस", "vi": "Ba mươi (30)", "en": "Thirty", "trans": "tees"},
            {"hi": "पचास", "vi": "Năm mươi (50)", "en": "Fifty", "trans": "pachas"},
            {"hi": "सौ", "vi": "Một trăm (100)", "en": "Hundred", "trans": "sau"}
        ]
    },
    {
        "id": "episode_numbers_lab_3", "title": "Hundreds 100-1000",
        "vocab": [
            {"hi": "दो सौ", "vi": "Hai trăm (200)", "en": "Two hundred", "trans": "do sau"},
            {"hi": "पाँच सौ", "vi": "Năm trăm (500)", "en": "Five hundred", "trans": "paanch sau"},
            {"hi": "हज़ार", "vi": "Một nghìn (1000)", "en": "Thousand", "trans": "hazaar"}
        ]
    },
    {
        "id": "episode_numbers_lab_4", "title": "Large Numbers Practice",
        "vocab": [
            {"hi": "दस हज़ार", "vi": "Mười nghìn (10.000)", "en": "Ten thousand", "trans": "das hazaar"},
            {"hi": "लाख", "vi": "Một trăm nghìn (Lakh)", "en": "Lakh (100,000)", "trans": "lakh"},
            {"hi": "करोड़", "vi": "Mười triệu (Crore)", "en": "Crore (10,000,000)", "trans": "karod"}
        ]
    },
    {
        "id": "episode_numbers_lab_5", "title": "Numbers Challenge",
        "vocab": [
            {"hi": "जोड़", "vi": "Phép cộng (+)", "en": "Addition", "trans": "jod"},
            {"hi": "घटाना", "vi": "Phép trừ (-)", "en": "Subtraction", "trans": "ghatana"},
            {"hi": "बराबर", "vi": "Bằng (=)", "en": "Equal", "trans": "barabar"},
            {"hi": "कुल", "vi": "Tổng số", "en": "Total", "trans": "kul"}
        ]
    }
]

# GENERATOR FUNCTIONS
def build_english_lessons(lessons, path):
    for lesson in lessons:
        nodes = []
        nodes.append({
            "type": "Introduction",
            "title": lesson["title"],
            "description": f"Master vocabulary and expressions for {lesson['title']}.",
            "keyPoints": [f"Learn {lesson['vocab'][0]['eng']} and related terms."]
        })
        for item in lesson["vocab"]:
            nodes.append({
                "type": "Flashcard",
                "hindi": item["eng"],
                "english": item["eng"],
                "vietnamese": item["vi"],
                "audio": item["eng"]
            })
        for idx, item in enumerate(lesson["vocab"]):
            options_vi = [item["vi"]]
            options_en = [item["eng"]]
            for other_idx, other_item in enumerate(lesson["vocab"]):
                if other_idx != idx and len(options_vi) < 4:
                    options_vi.append(other_item["vi"])
                    options_en.append(other_item["eng"])
            while len(options_vi) < 4:
                options_vi.append("N/A")
                options_en.append("N/A")
            nodes.append({
                "type": "MultipleChoice",
                "prompt": "Choose the correct translation:",
                "prompt_en": "Choose the correct translation:",
                "prompt_vi": "Chọn bản dịch đúng:",
                "hindi": item["eng"],
                "text": item["eng"],
                "subtext": "",
                "answer": item["eng"],
                "answer_vi": item["vi"],
                "options": options_en,
                "options_en": options_en,
                "options_vi": options_vi
            })
        pairs = []
        for item in lesson["vocab"][:4]:
            pairs.append({
                "hindi": item["eng"],
                "english": item["vi"]
            })
        nodes.append({
            "type": "MatchPairs",
            "instruction": "Match the English words with their Vietnamese meanings",
            "pairs": pairs
        })
        
        file_name = f"{lesson['id']}.json"
        with open(os.path.join(path, file_name), 'w', encoding='utf-8') as f:
            json.dump(nodes, f, ensure_ascii=False, indent=2)

def build_hindi_lessons(lessons, path):
    for lesson in lessons:
        nodes = []
        nodes.append({
            "type": "Introduction",
            "title": lesson["title"],
            "description": f"Master vocabulary and expressions for {lesson['title']}.",
            "keyPoints": [f"Learn {lesson['vocab'][0]['hi']} and related terms."]
        })
        for item in lesson["vocab"]:
            nodes.append({
                "type": "Flashcard",
                "hindi": item["hi"],
                "transliteration": item["trans"],
                "english": item["en"],
                "vietnamese": item["vi"],
                "audio": item["hi"]
            })
        for idx, item in enumerate(lesson["vocab"]):
            options_vi = [item["vi"]]
            options_en = [item["en"]]
            for other_idx, other_item in enumerate(lesson["vocab"]):
                if other_idx != idx and len(options_vi) < 4:
                    options_vi.append(other_item["vi"])
                    options_en.append(other_item["en"])
            while len(options_vi) < 4:
                options_vi.append("N/A")
                options_en.append("N/A")
            nodes.append({
                "type": "MultipleChoice",
                "prompt": "Choose the correct translation:",
                "prompt_en": "Choose the correct translation:",
                "prompt_vi": "Chọn bản dịch đúng:",
                "hindi": item["hi"],
                "text": item["hi"],
                "subtext": "",
                "answer": item["en"],
                "answer_vi": item["vi"],
                "options": options_en,
                "options_en": options_en,
                "options_vi": options_vi
            })
        pairs = []
        for item in lesson["vocab"][:4]:
            pairs.append({
                "hindi": item["hi"],
                "english": item["en"]
            })
        nodes.append({
            "type": "MatchPairs",
            "instruction": "Match the Hindi words with their English meanings",
            "pairs": pairs
        })
        
        file_name = f"{lesson['id']}.json"
        with open(os.path.join(path, file_name), 'w', encoding='utf-8') as f:
            json.dump(nodes, f, ensure_ascii=False, indent=2)

# Build all
print("Building English alphabet lab...")
build_english_lessons(english_alphabets_lab, english_path)
print("Building English numbers lab...")
build_english_lessons(english_numbers_lab, english_path)
print("Building Hindi alphabet lab...")
build_hindi_lessons(hindi_alphabets_lab, hindi_path)
print("Building Hindi numbers lab...")
build_hindi_lessons(hindi_numbers_lab, hindi_path)
print("Labs generation completed successfully!")
