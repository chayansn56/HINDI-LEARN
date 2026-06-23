import json
import os
import random

episodes_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
vocab_file = os.path.join(episodes_dir, "master_vocab.json")

# Simple bilingual sentence templates for SentenceBuilder exercises
SENTENCE_TEMPLATES = [
    {"en": "I am a boy", "vi": "Tôi là một cậu bé", "hi": "मैं लड़का हूँ", "words": ["मैं", "लड़का", "हूँ", "लड़की", "है"], "category": "gram"},
    {"en": "I am a girl", "vi": "Tôi là một cô gái", "hi": "मैं लड़की हूँ", "words": ["मैं", "लड़की", "हूँ", "लड़का", "है"], "category": "gram"},
    {"en": "He is a boy", "vi": "Cậu ấy là một cậu bé", "hi": "वह लड़का है", "words": ["वह", "लड़का", "है", "हूँ", "वे"], "category": "gram"},
    {"en": "We are friends", "vi": "Chúng tôi là bạn bè", "hi": "हम दोस्त हैं", "words": ["हम", "दोस्त", "हैं", "हो", "तुम"], "category": "gram"},
    {"en": "I eat an apple", "vi": "Tôi ăn một quả táo", "hi": "मैं सेब खाता हूँ", "words": ["मैं", "सेब", "खाता", "हूँ", "रोटी", "है"], "category": "gram"},
    {"en": "She eats bread", "vi": "Cô ấy ăn bánh mì", "hi": "वह रोटी खाती है", "words": ["वह", "रोटी", "खाती", "है", "पीती", "हूँ"], "category": "gram"},
    {"en": "I drink water", "vi": "Tôi uống nước", "hi": "मैं पानी पीता हूँ", "words": ["मैं", "पानी", "पीता", "हूँ", "दूध", "है"], "category": "gram"},
    {"en": "Hello, how are you?", "vi": "Xin chào, bạn khỏe không?", "hi": "नमस्ते आप कैसे हैं", "words": ["नमस्ते", "आप", "कैसे", "हैं", "तुम", "हो"], "category": "speak"},
    {"en": "My name is John", "vi": "Tên tôi là John", "hi": "मेरा नाम जॉन है", "words": ["मेरा", "नाम", "जॉन", "है", "लड़की", "हूँ"], "category": "speak"},
    {"en": "Thank you very much", "vi": "Cảm ơn bạn rất nhiều", "hi": "बहुत बहुत धन्यवाद", "words": ["बहुत", "बहुत", "धन्यवाद", "कृपया", "नमस्ते"], "category": "speak"},
    {"en": "Please speak slowly", "vi": "Làm ơn nói chậm lại", "hi": "कृपया धीरे बोलिए", "words": ["कृपया", "धीरे", "बोलिए", "सुनिए", "नमस्ते"], "category": "speak"},
    {"en": "Where is the hotel?", "vi": "Khách sạn ở đâu?", "hi": "होटल कहाँ है", "words": ["होटल", "कहाँ", "है", "स्टेशन", "में"], "category": "travel"},
    {"en": "Where is the airport?", "vi": "Sân bay ở đâu?", "hi": "हवाई अड्डा कहाँ है", "words": ["हवाई", "अड्डा", "कहाँ", "है", "होटल", "पर"], "category": "travel"},
    {"en": "How much is the ticket?", "vi": "Vé bao nhiêu tiền?", "hi": "टिकट कितने का है", "words": ["टिकट", "कितने", "का", "है", "पैसे", "को"], "category": "travel"},
    {"en": "Please reduce the price", "vi": "Làm ơn giảm giá đi", "hi": "कृपया दाम कम करो", "words": ["कृपया", "दाम", "कम", "करो", "ज्यादा", "पैसे"], "category": "travel"},
    {"en": "I want hot bread", "vi": "Tôi muốn bánh mì nóng", "hi": "मुझे गर्म रोटी चाहिए", "words": ["मुझे", "गर्म", "रोटी", "चाहिए", "दूध", "पीना"], "category": "food"},
    {"en": "This food is spicy", "vi": "Thức ăn này rất cay", "hi": "यह खाना तीखा है", "words": ["यह", "खाना", "तीखा", "है", "स्वादिष्ट", "मीठा"], "category": "food"},
    {"en": "Do you want water?", "vi": "Bạn có muốn nước không?", "hi": "क्या आपको पानी चाहिए", "words": ["क्या", "आपको", "पानी", "चाहिए", "चाय", "दूध"], "category": "food"},
    {"en": "This is my mother", "vi": "Đây là mẹ của tôi", "hi": "यह मेरी माँ है", "words": ["यह", "मेरी", "माँ", "है", "पिता", "भाई"], "category": "family"},
    {"en": "He is my brother", "vi": "Anh ấy là anh trai của tôi", "hi": "वह मेरा भाई है", "words": ["वह", "मेरा", "भाई", "है", "बहन", "माँ"], "category": "family"},
    {"en": "My family is small", "vi": "Gia đình tôi nhỏ", "hi": "मेरा परिवार छोटा है", "words": ["मेरा", "परिवार", "छोटा", "है", "बड़ा", "दोस्त"], "category": "family"},
    {"en": "Where is the office?", "vi": "Văn phòng ở đâu?", "hi": "दफ़्तर कहाँ है", "words": ["दफ़्तर", "कहाँ", "है", "कमरा", "में"], "category": "business"},
    {"en": "I have a meeting today", "vi": "Hôm nay tôi có cuộc họp", "hi": "आज मेरी बैठक है", "words": ["आज", "मेरी", "बैठक", "है", "काम", "दफ़्तर"], "category": "business"},
    {"en": "Please sign here", "vi": "Vui lòng ký vào đây", "hi": "कृपया यहाँ हस्ताक्षर करें", "words": ["कृपया", "यहाँ", "हस्ताक्षर", "करें", "कागज़", "लिखना"], "category": "business"},
    {"en": "Today is Diwali", "vi": "Hôm nay là lễ Diwali", "hi": "आज दिवाली है", "words": ["आज", "दिवाली", "है", "होली", "ईद"], "category": "culture"},
    {"en": "I go to the temple", "vi": "Tôi đi đến đền thờ", "hi": "मैं मंदिर जाता हूँ", "words": ["मैं", "मंदिर", "जाता", "हूँ", "घर", "बाज़ार"], "category": "culture"},
    {"en": "I love you", "vi": "Tôi yêu bạn", "hi": "मैं तुमसे प्यार करता हूँ", "words": ["मैं", "तुमसे", "प्यार", "करता", "हूँ", "दोस्त"], "category": "bollywood"},
    {"en": "This is my dream", "vi": "Đây là giấc mơ của tôi", "hi": "यह मेरा सपना है", "words": ["यह", "मेरा", "सपना", "है", "दिल", "ज़िंदगी"], "category": "bollywood"},
    {"en": "What is the plan, bro?", "vi": "Kế hoạch là gì vậy bro?", "hi": "सीन क्या है भाई", "words": ["सीन", "क्या", "है", "भाई", "यार", "मस्त"], "category": "whatsapp"},
    {"en": "Don't annoy me", "vi": "Đừng làm phiền tôi nữa", "hi": "दिमाग मत खा", "words": ["दिमाग", "मत", "खा", "यार", "बकवास", "ब्रो"], "category": "whatsapp"},
    {"en": "Where is the train station?", "vi": "Ga tàu ở đâu?", "hi": "रेलवे स्टेशन कहाँ है", "words": ["रेलवे", "स्टेशन", "कहाँ", "है", "हवाई", "अड्डा"], "category": "travel"},
    {"en": "What is your name?", "vi": "Tên của bạn là gì?", "hi": "आपका नाम क्या है", "words": ["आपका", "नाम", "क्या", "है", "मेरा", "लड़का"], "category": "speak"},
    {"en": "How are you?", "vi": "Bạn khỏe không?", "hi": "तुम कैसे हो", "words": ["तुम", "कैसे", "हो", "नमस्ते", "हम", "हैं"], "category": "speak"},
    {"en": "This is a big house", "vi": "Đây là một ngôi nhà lớn", "hi": "यह बड़ा घर है", "words": ["यह", "बड़ा", "घर", "है", "छोटा", "सड़क"], "category": "gram"},
    {"en": "The boy runs fast", "vi": "Cậu bé chạy nhanh", "hi": "लड़का तेज़ दौड़ता है", "words": ["लड़का", "तेज़", "दौड़ता", "है", "लड़की", "धीमे"], "category": "gram"},
    {"en": "This water is clean", "vi": "Nước này sạch", "hi": "यह पानी साफ़ है", "words": ["यह", "पानी", "साफ़", "है", "दूध", "गंदा"], "category": "listen"},
    {"en": "Diwali is a festival", "vi": "Diwali là một lễ hội", "hi": "दिवाली त्योहार है", "words": ["दिवाली", "त्योहार", "है", "मंदिर", "पूजा"], "category": "culture"},
    {"en": "Sign this paper", "vi": "Ký vào tờ giấy này", "hi": "कागज़ पर हस्ताक्षर करें", "words": ["कागज़", "पर", "हस्ताक्षर", "करें", "कमरा", "लिखना"], "category": "business"}
]

def load_master_vocab():
    with open(vocab_file, "r", encoding="utf-8") as f:
        return json.load(f)

def generate_15_node_episode(topic, rule_title, explanation, vocab_list, category_key):
    # Ensure exactly 6 vocabulary words
    while len(vocab_list) < 6:
        vocab_list.append(vocab_list[0])
    v1, v2, v3, v4, v5, v6 = vocab_list[:6]
    
    # Select or fallback to sentence builders
    cat_sentences = [s for s in SENTENCE_TEMPLATES if s["category"] == category_key]
    if not cat_sentences:
        cat_sentences = SENTENCE_TEMPLATES
    
    sb1 = random.choice(cat_sentences)
    sb2 = random.choice([s for s in cat_sentences if s != sb1] or cat_sentences)
    
    nodes = []
    
    # 1. Introduction
    nodes.append({
        "type": "Introduction",
        "title": topic,
        "description": f"Learn key vocabulary and concepts for {topic}. Listen carefully and read along in English and Vietnamese.",
        "keyPoints": [
            f"{v1['hindi']} ({v1['transliteration']}) = {v1['english']} / {v1['vietnamese']}",
            f"{v2['hindi']} ({v2['transliteration']}) = {v2['english']} / {v2['vietnamese']}",
            f"{v3['hindi']} ({v3['transliteration']}) = {v3['english']} / {v3['vietnamese']}"
        ]
    })
    
    # 2. TeachRule
    nodes.append({
        "type": "TeachRule",
        "title": rule_title,
        "explanation": explanation or v1.get("usage_note", "Notice the usage and spelling of this word."),
        "simpleRule": f"Remember: {v1['hindi']} means '{v1['english']}' ({v1['vietnamese']})."
    })
    
    # 3 & 4. Flashcards
    nodes.append({
        "type": "Flashcard",
        "hindi": v1["hindi"],
        "transliteration": v1["transliteration"],
        "english": v1["english"],
        "vietnamese": v1["vietnamese"],
        "audio": v1["hindi"]
    })
    nodes.append({
        "type": "Flashcard",
        "hindi": v2["hindi"],
        "transliteration": v2["transliteration"],
        "english": v2["english"],
        "vietnamese": v2["vietnamese"],
        "audio": v2["hindi"]
    })
    
    # 5. Multiple Choice (semantically related distractors)
    distractors_1 = [v["english"] for v in vocab_list if v["english"] != v1["english"]]
    distractors_1_vi = [v["vietnamese"] for v in vocab_list if v["vietnamese"] != v1["vietnamese"]]
    while len(distractors_1) < 3:
        distractors_1.append("Water")
        distractors_1_vi.append("Nước")
    opts_en = [v1["english"]] + distractors_1[:3]
    opts_vi = [v1["vietnamese"]] + distractors_1_vi[:3]
    # Shuffle synchronously
    combined = list(zip(opts_en, opts_vi))
    random.shuffle(combined)
    shuffled_en, shuffled_vi = zip(*combined)
    
    nodes.append({
        "type": "MultipleChoice",
        "prompt": f"Translate '{v1['hindi']}'",
        "prompt_en": f"Choose the correct translation for '{v1['hindi']}'",
        "prompt_vi": f"Chọn bản dịch đúng cho '{v1['hindi']}'",
        "text": v1["hindi"],
        "subtext": v1["transliteration"],
        "answer": v1["english"],
        "answer_vi": v1["vietnamese"],
        "options": list(shuffled_en),
        "options_vi": list(shuffled_vi)
    })
    
    # 6 & 7. Flashcards
    nodes.append({
        "type": "Flashcard",
        "hindi": v3["hindi"],
        "transliteration": v3["transliteration"],
        "english": v3["english"],
        "vietnamese": v3["vietnamese"],
        "audio": v3["hindi"]
    })
    nodes.append({
        "type": "Flashcard",
        "hindi": v4["hindi"],
        "transliteration": v4["transliteration"],
        "english": v4["english"],
        "vietnamese": v4["vietnamese"],
        "audio": v4["hindi"]
    })
    
    # 8. MatchPairs
    nodes.append({
        "type": "MatchPairs",
        "instruction": "Match the Hindi words with their meanings",
        "pairs": [
            {"hindi": v1["hindi"], "english": f"{v1['english']} ({v1['vietnamese']})"},
            {"hindi": v2["hindi"], "english": f"{v2['english']} ({v2['vietnamese']})"},
            {"hindi": v3["hindi"], "english": f"{v3['english']} ({v3['vietnamese']})"},
            {"hindi": v4["hindi"], "english": f"{v4['english']} ({v4['vietnamese']})"}
        ]
    })
    
    # 9. Listening (fully translated options)
    distractors_2 = [v["english"] for v in vocab_list if v["english"] != v2["english"]]
    distractors_2_vi = [v["vietnamese"] for v in vocab_list if v["vietnamese"] != v2["vietnamese"]]
    while len(distractors_2) < 3:
        distractors_2.append("House")
        distractors_2_vi.append("Nhà")
    l_opts_en = [v2["english"]] + distractors_2[:3]
    l_opts_vi = [v2["vietnamese"]] + distractors_2_vi[:3]
    # Shuffle synchronously
    l_combined = list(zip(l_opts_en, l_opts_vi))
    random.shuffle(l_combined)
    l_shuffled_en, l_shuffled_vi = zip(*l_combined)
    
    nodes.append({
        "type": "Listening",
        "audio": v2["hindi"],
        "translation_en": v2["english"],
        "translation_vi": v2["vietnamese"],
        "options_en": list(l_shuffled_en),
        "options_vi": list(l_shuffled_vi)
    })
    
    # 10. SentenceBuilder (REAL sentence builder)
    nodes.append({
        "type": "SentenceBuilder",
        "englishSentence": f"{sb1['en']} / {sb1['vi']}",
        "hindiWords": sb1["words"],
        "correctHindiSentence": sb1["hi"]
    })
    
    # 11 & 12. Flashcards
    nodes.append({
        "type": "Flashcard",
        "hindi": v5["hindi"],
        "transliteration": v5["transliteration"],
        "english": v5["english"],
        "vietnamese": v5["vietnamese"],
        "audio": v5["hindi"]
    })
    nodes.append({
        "type": "Flashcard",
        "hindi": v6["hindi"],
        "transliteration": v6["transliteration"],
        "english": v6["english"],
        "vietnamese": v6["vietnamese"],
        "audio": v6["hindi"]
    })
    
    # 13. MatchPairs
    nodes.append({
        "type": "MatchPairs",
        "instruction": "Match the words to proceed",
        "pairs": [
            {"hindi": v3["hindi"], "english": f"{v3['english']} ({v3['vietnamese']})"},
            {"hindi": v4["hindi"], "english": f"{v4['english']} ({v4['vietnamese']})"},
            {"hindi": v5["hindi"], "english": f"{v5['english']} ({v5['vietnamese']})"},
            {"hindi": v6["hindi"], "english": f"{v6['english']} ({v6['vietnamese']})"}
        ]
    })
    
    # 14. SentenceBuilder
    nodes.append({
        "type": "SentenceBuilder",
        "englishSentence": f"{sb2['en']} / {sb2['vi']}",
        "hindiWords": sb2["words"],
        "correctHindiSentence": sb2["hi"]
    })
    
    # 15. RevisionSummary
    nodes.append({
        "type": "RevisionSummary",
        "title": "Lesson Summary",
        "takeaways": [
            f"You learned basic vocabulary terms: {v1['hindi']}, {v3['hindi']}, {v5['hindi']}.",
            f"You completed interactive listening and matching drills.",
            f"You practiced word ordering for the sentence: '{sb2['en']}'."
        ]
    })
    
    return nodes

def main():
    print("Loading master vocabulary dictionary...")
    vocab = load_master_vocab()
    
    # Group vocabulary by category field
    grouped = {}
    for word in vocab:
        cat = word["category"]
        if cat not in grouped:
            grouped[cat] = []
        grouped[cat].append(word)
        
    print(f"Loaded categories: {list(grouped.keys())}")
    
    # Category to Season mappings
    module_mapping = {
        "pron": ["pron", "pronouns", "numbers", "colors", "animals"],
        "speak": ["speak", "greetings"],
        "gram": ["gram", "pronouns", "to_be_verb", "postpositions", "verbs_common"],
        "listen": ["listen", "animals", "colors", "food", "family"],
        "write": ["write", "numbers"],
        "culture": ["culture", "food", "family", "bollywood"],
        "travel": ["travel"],
        "business": ["business"],
        "bollywood": ["bollywood"],
        "whatsapp": ["whatsapp"]
    }
    
    module_counts = {
        "pron": 8, "speak": 10, "gram": 12, "listen": 6, "write": 8,
        "culture": 6, "travel": 8, "business": 6, "bollywood": 6, "whatsapp": 4
    }
    
    # Generate episodes for all 10 modules
    for mod_key, count in module_counts.items():
        allowed_cats = module_mapping[mod_key]
        mod_vocab_pool = []
        for cat in allowed_cats:
            mod_vocab_pool.extend(grouped.get(cat, []))
            
        if not mod_vocab_pool:
            print(f"Warning: No vocab found for module {mod_key}")
            continue
            
        print(f"Generating {count} episodes for module '{mod_key}' using pool of {len(mod_vocab_pool)} words...")
        for i in range(1, count + 1):
            vocab_sample = random.sample(mod_vocab_pool, min(6, len(mod_vocab_pool)))
            # Duplicate if pool is too small
            while len(vocab_sample) < 6:
                vocab_sample.append(random.choice(mod_vocab_pool))
                
            episode_data = generate_15_node_episode(
                topic=f"{mod_key.capitalize()} - Lesson {i}",
                rule_title=f"Vocabulary & Grammar - Part {i}",
                explanation=f"Focus on speaking and writing these essential terms carefully. Pay attention to gender endings and postpositions.",
                vocab_list=vocab_sample,
                category_key=mod_key
            )
            
            file_name = f"episode_{mod_key}_{i}.json"
            with open(os.path.join(episodes_dir, file_name), "w", encoding="utf-8") as f:
                json.dump(episode_data, f, ensure_ascii=False, indent=2)
                
    # Generate Foundation episodes 19-30
    foundation_pool = []
    for cat in ["foundations", "pron", "speak", "gram", "listen", "write"]:
        foundation_pool.extend(grouped.get(cat, []))
        
    print(f"Generating Foundation episodes 19 to 30 using pool of {len(foundation_pool)} words...")
    for i in range(19, 31):
        vocab_sample = random.sample(foundation_pool, min(6, len(foundation_pool)))
        while len(vocab_sample) < 6:
            vocab_sample.append(random.choice(foundation_pool))
            
        episode_data = generate_15_node_episode(
            topic=f"Foundation Academy - Level {i}",
            rule_title=f"Alphabet & Building Blocks - Part {i}",
            explanation=f"Consolidate your Devanagari script, basic words, and greeting structures.",
            vocab_list=vocab_sample,
            category_key="gram"
        )
        
        file_name = f"episode_0_{i}.json"
        with open(os.path.join(episodes_dir, file_name), "w", encoding="utf-8") as f:
            json.dump(episode_data, f, ensure_ascii=False, indent=2)

    # Clean up unnumbered helper json files
    stale_files = [
        "episode_bollywood.json",
        "episode_business.json",
        "episode_culture.json",
        "episode_grammar.json",
        "episode_listening.json",
        "episode_pronunciation.json",
        "episode_speaking.json",
        "episode_travel.json",
        "episode_whatsapp.json",
        "episode_writing.json"
    ]
    for filename in stale_files:
        filepath = os.path.join(episodes_dir, filename)
        if os.path.exists(filepath):
            os.remove(filepath)
            print(f"Removed stale file: {filename}")

    print("Success: Generated curriculum files and cleaned assets!")

if __name__ == "__main__":
    main()
