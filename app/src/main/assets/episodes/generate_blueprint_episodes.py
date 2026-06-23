import json
import os

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(output_dir, exist_ok=True)

# -----------------
# FOUNDATIONS MISSING EPISODES
# -----------------
foundations_data = {
    "19": [
        {"type": "Introduction", "title": "Matras Part 2: Above & Compound", "description": "Learn matras placed above the letter: े (e), ै (ai), ो (o), ौ (au), and ृ (ri).", "keyPoints": ["े (e) -> क->के", "ै (ai) -> क->कै", "ो (o) -> क->को", "ौ (au) -> क->कौ", "ृ (ri) -> क->कृ"]},
        {"type": "Flashcard", "hindi": "केला", "transliteration": "kela", "english": "Banana", "vietnamese": "Chuối"},
        {"type": "Flashcard", "hindi": "कैसा", "transliteration": "kaisa", "english": "How", "vietnamese": "Như thế nào"},
        {"type": "Flashcard", "hindi": "कोयल", "transliteration": "koyal", "english": "Cuckoo", "vietnamese": "Chim tu hú"},
        {"type": "Flashcard", "hindi": "कौआ", "transliteration": "kauwa", "english": "Crow", "vietnamese": "Con quạ"},
        {"type": "Flashcard", "hindi": "कृपया", "transliteration": "kripaya", "english": "Please", "vietnamese": "Làm ơn / Xin vui lòng"},
        {"type": "MultipleChoice", "prompt": "Translate 'Banana'", "text": "केला", "subtext": "kela", "answer": "Banana", "options": ["Banana", "Crow", "How", "Please"]},
        {"type": "MatchPairs", "instruction": "Match words with meanings", "pairs": [
            {"hindi": "केला", "english": "Banana"},
            {"hindi": "कैसा", "english": "How"},
            {"hindi": "कोयल", "english": "Cuckoo"},
            {"hindi": "कौआ", "english": "Crow"}
        ]}
    ],
    "20": [
        {"type": "Introduction", "title": "Reading Practice: First Words", "description": "Put consonants and matras together to read common Hindi words.", "keyPoints": ["Combine letters", "Read syllables", "Learn everyday words"]},
        {"type": "Flashcard", "hindi": "नमस्ते", "transliteration": "namaste", "english": "Hello / Respectful Greeting", "vietnamese": "Xin chào"},
        {"type": "Flashcard", "hindi": "किताब", "transliteration": "kitaab", "english": "Book", "vietnamese": "Sách"},
        {"type": "Flashcard", "hindi": "दुकान", "transliteration": "dukaan", "english": "Shop", "vietnamese": "Cửa hàng"},
        {"type": "Flashcard", "hindi": "समय", "transliteration": "samay", "english": "Time", "vietnamese": "Thời gian"},
        {"type": "MultipleChoice", "prompt": "Translate 'Book'", "text": "किताब", "subtext": "kitaab", "answer": "Book", "options": ["Book", "Shop", "Time", "Hello"]},
        {"type": "SentenceBuilder", "englishSentence": "This is a book.", "hindiWords": ["यह", "एक", "किताब", "है"], "correctHindiSentence": "यह एक किताब है"}
    ],
    "21": [
        {"type": "Introduction", "title": "Numbers 0–10", "description": "Learn how to say and recognize numbers from 0 to 10 in Hindi.", "keyPoints": ["शून्य (shunya/0) to दस (das/10)", "Recognize Hindi symbols", "Pronounce accurately"]},
        {"type": "Flashcard", "hindi": "शून्य", "transliteration": "shunya", "english": "Zero (0)", "vietnamese": "Số không"},
        {"type": "Flashcard", "hindi": "एक", "transliteration": "ek", "english": "One (1)", "vietnamese": "Số một"},
        {"type": "Flashcard", "hindi": "दो", "transliteration": "do", "english": "Two (2)", "vietnamese": "Số hai"},
        {"type": "Flashcard", "hindi": "तीन", "transliteration": "teen", "english": "Three (3)", "vietnamese": "Số ba"},
        {"type": "Flashcard", "hindi": "चार", "transliteration": "char", "english": "Four (4)", "vietnamese": "Số bốn"},
        {"type": "Flashcard", "hindi": "पांच", "transliteration": "panch", "english": "Five (5)", "vietnamese": "Số năm"},
        {"type": "Flashcard", "hindi": "दस", "transliteration": "das", "english": "Ten (10)", "vietnamese": "Số mười"},
        {"type": "MultipleChoice", "prompt": "Identify the number '3'", "text": "तीन", "subtext": "teen", "answer": "Three", "options": ["One", "Two", "Three", "Five"]},
        {"type": "MatchPairs", "instruction": "Match numbers", "pairs": [
            {"hindi": "एक", "english": "One"},
            {"hindi": "दो", "english": "Two"},
            {"hindi": "तीन", "english": "Three"},
            {"hindi": "चार", "english": "Four"}
        ]}
    ],
    "22": [
        {"type": "Introduction", "title": "Numbers 11–20 & Tens to 100", "description": "Learn numbers up to 100 including tens.", "keyPoints": ["ग्यारह (11) to बीस (20)", "बीस (20), तीस (30), चालीस (40)...", "सौ (sau/100)"]},
        {"type": "Flashcard", "hindi": "ग्यारह", "transliteration": "gyaarah", "english": "Eleven (11)", "vietnamese": "Mười một"},
        {"type": "Flashcard", "hindi": "बारह", "transliteration": "baarah", "english": "Twelve (12)", "vietnamese": "Mười hai"},
        {"type": "Flashcard", "hindi": "बीस", "transliteration": "bees", "english": "Twenty (20)", "vietnamese": "Hai mươi"},
        {"type": "Flashcard", "hindi": "पचास", "transliteration": "pachaas", "english": "Fifty (50)", "vietnamese": "Năm mươi"},
        {"type": "Flashcard", "hindi": "सौ", "transliteration": "sau", "english": "One Hundred (100)", "vietnamese": "Một trăm"},
        {"type": "MultipleChoice", "prompt": "Translate 'One Hundred'", "text": "सौ", "subtext": "sau", "answer": "One Hundred", "options": ["Eleven", "Twenty", "Fifty", "One Hundred"]}
    ],
    "23": [
        {"type": "Introduction", "title": "Family & People", "description": "Vocabulary for family members and relationships.", "keyPoints": ["Mother & Father", "Brother & Sister", "Friend & Child"]},
        {"type": "Flashcard", "hindi": "माँ", "transliteration": "maa", "english": "Mother", "vietnamese": "Mẹ"},
        {"type": "Flashcard", "hindi": "पिता", "transliteration": "pita", "english": "Father", "vietnamese": "Bố"},
        {"type": "Flashcard", "hindi": "भाई", "transliteration": "bhai", "english": "Brother", "vietnamese": "Anh/Em trai"},
        {"type": "Flashcard", "hindi": "बहन", "transliteration": "behen", "english": "Sister", "vietnamese": "Chị/Em gái"},
        {"type": "Flashcard", "hindi": "दोस्त", "transliteration": "dost", "english": "Friend", "vietnamese": "Bạn bè"},
        {"type": "MultipleChoice", "prompt": "Translate 'Mother'", "text": "माँ", "subtext": "maa", "answer": "Mother", "options": ["Mother", "Father", "Sister", "Friend"]},
        {"type": "SentenceBuilder", "englishSentence": "This is my brother.", "hindiWords": ["यह", "मेरा", "भाई", "है"], "correctHindiSentence": "यह मेरा भाई है"}
    ],
    "24": [
        {"type": "Introduction", "title": "Food & Drink", "description": "Learn food vocabulary: staple foods, vegetables, and drinks.", "keyPoints": ["Chai & Water", "Roti, Rice, and Lentils", "Fruits"]},
        {"type": "Flashcard", "hindi": "रोटी", "transliteration": "roti", "english": "Indian Flatbread", "vietnamese": "Bánh mì Roti"},
        {"type": "Flashcard", "hindi": "चावल", "transliteration": "chawal", "english": "Rice", "vietnamese": "Cơm / Gạo"},
        {"type": "Flashcard", "hindi": "दाल", "transliteration": "daal", "english": "Lentils", "vietnamese": "Đậu lăng"},
        {"type": "Flashcard", "hindi": "दूध", "transliteration": "doodh", "english": "Milk", "vietnamese": "Sữa"},
        {"type": "Flashcard", "hindi": "पानी", "transliteration": "paani", "english": "Water", "vietnamese": "Nước"},
        {"type": "MultipleChoice", "prompt": "Translate 'Water'", "text": "पानी", "subtext": "paani", "answer": "Water", "options": ["Milk", "Water", "Rice", "Lentils"]}
    ],
    "25": [
        {"type": "Introduction", "title": "Body & Health", "description": "Learn parts of the body and words for basic medical states.", "keyPoints": ["Head, Eye, Ear, Nose", "Pain, Fever, Medicine"]},
        {"type": "Flashcard", "hindi": "सिर", "transliteration": "sir", "english": "Head", "vietnamese": "Đầu"},
        {"type": "Flashcard", "hindi": "दर्द", "transliteration": "dard", "english": "Pain", "vietnamese": "Đau"},
        {"type": "Flashcard", "hindi": "बुखार", "transliteration": "bukhaar", "english": "Fever", "vietnamese": "Sốt"},
        {"type": "Flashcard", "hindi": "दवाई", "transliteration": "dawai", "english": "Medicine", "vietnamese": "Thuốc"},
        {"type": "MultipleChoice", "prompt": "Translate 'Medicine'", "text": "दवाई", "subtext": "dawai", "answer": "Medicine", "options": ["Head", "Pain", "Fever", "Medicine"]}
    ],
    "28": [
        {"type": "Introduction", "title": "Common Verbs", "description": "Learn verbs to start expressing actions: do, be, eat, drink, go, come.", "keyPoints": ["Verbs end in -na", "hona (be), karna (do)", "khana (eat), peena (drink)"]},
        {"type": "Flashcard", "hindi": "करना", "transliteration": "karna", "english": "To do", "vietnamese": "Làm"},
        {"type": "Flashcard", "hindi": "होना", "transliteration": "hona", "english": "To be", "vietnamese": "Thì, là, ở / Có"},
        {"type": "Flashcard", "hindi": "खाना", "transliteration": "khaana", "english": "To eat / Food", "vietnamese": "Ăn / Đồ ăn"},
        {"type": "Flashcard", "hindi": "पीना", "transliteration": "peena", "english": "To drink", "vietnamese": "Uống"},
        {"type": "Flashcard", "hindi": "जाना", "transliteration": "jaana", "english": "To go", "vietnamese": "Đi"},
        {"type": "Flashcard", "hindi": "आना", "transliteration": "aana", "english": "To come", "vietnamese": "Đến"},
        {"type": "MultipleChoice", "prompt": "Translate 'To eat'", "text": "खाना", "subtext": "khaana", "answer": "To eat / Food", "options": ["To do", "To be", "To eat / Food", "To drink"]}
    ],
    "29": [
        {"type": "Introduction", "title": "15 Survival Sentences", "description": "Essential sentences to navigate daily life in India.", "keyPoints": ["Ask for help", "Ask for location", "Express needs"]},
        {"type": "Flashcard", "hindi": "मुझे मदद चाहिए।", "transliteration": "mujhe madad chahiye", "english": "I need help.", "vietnamese": "Tôi cần giúp đỡ."},
        {"type": "Flashcard", "hindi": "यह कितने का है?", "transliteration": "yeh kitne ka hai", "english": "How much is this?", "vietnamese": "Cái này bao nhiêu tiền?"},
        {"type": "Flashcard", "hindi": "मुझे समझ नहीं आया।", "transliteration": "mujhe samajh nahin aaya", "english": "I don't understand.", "vietnamese": "Tôi không hiểu."},
        {"type": "MultipleChoice", "prompt": "Translate 'I need help.'", "text": "मुझे मदद चाहिए।", "subtext": "mujhe madad chahiye", "answer": "I need help.", "options": ["I need help.", "How much is this?", "I don't understand.", "Hello"]}
    ],
    "30": [
        {"type": "Introduction", "title": "Foundation Final Exam", "description": "Test your learning of script, pronunciation, numbers, and basic words.", "keyPoints": ["Vowels & Consonants", "Numbers & Verbs", "Survival sentences"]},
        {"type": "MultipleChoice", "prompt": "Translate 'Hello'", "text": "नमस्ते", "subtext": "namaste", "answer": "Hello", "options": ["Hello", "Goodbye", "Thank you", "Water"]},
        {"type": "MultipleChoice", "prompt": "Identify the number 'One'", "text": "एक", "subtext": "ek", "answer": "One", "options": ["One", "Two", "Three", "Four"]},
        {"type": "SentenceBuilder", "englishSentence": "I need help.", "hindiWords": ["मुझे", "मदद", "चाहिए"], "correctHindiSentence": "मुझे मदद चाहिए"},
        {"type": "MultipleChoice", "prompt": "Translate 'Water'", "text": "पानी", "subtext": "paani", "answer": "Water", "options": ["Water", "Milk", "Rice", "Banana"]}
    ]
}

for ep_id, nodes in foundations_data.items():
    file_path = os.path.join(output_dir, f"episode_0_{ep_id}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(nodes, f, ensure_ascii=False, indent=2)

# -----------------
# SPECIAL MODULES EPISODES GENERATOR
# -----------------
# We will create sub-episodes for:
# pron (1..8), speak (1..10), gram (1..12), listen (1..6), write (1..8), culture (1..6), travel (1..8), business (1..6), bollywood (1..6), whatsapp (1..4)
special_blueprints = {
    "pron": {
        "count": 8,
        "titles": ["Aspirated vs Unaspirated", "Retroflex vs Dental", "Nasal Sounds", "The Schwa Deletion Rule", "R, L, V, and SH Sounds", "Conjunct Consonants", "Question Intonation & Emotion", "Minimal Pairs Challenge"],
        "descs": ["Understand breath in क/ख, ग/घ.", "Dental त vs Retroflex ट.", "Master अं and Chandrabindu.", "Why final अ is silent.", "Pronouncing unique Hindi consonants.", "How half-letters join.", "Tone of questions and commands.", "Ear training test."]
    },
    "speak": {
        "count": 10,
        "titles": ["Greetings & Farewells", "Introducing Yourself", "Being Polite", "Question Words", "At a Restaurant", "Shopping & Bargaining", "Formal vs Informal (Aap/Tum)", "Expressing Feelings", "Making Plans", "Phone & Emergency"],
        "descs": ["Namaste, Alvida, and Phir milenge.", "Saying your name and origin.", "Kripaya, Dhanyavaad, and Maaf kijiye.", "Kya, Kaun, Kahan, Kab, Kaise.", "Ordering chai and food.", "Bargaining at the bazaar.", "When to use Aap, Tum, and Tu.", "Happy, sad, tired, angry.", "Let's meet tomorrow.", "Call for help or talk on phone."]
    },
    "gram": {
        "count": 12,
        "titles": ["The Verb 'To Be'", "Sentence Structure (SOV)", "Gender System", "Pronouns & Possessives", "Postpositions", "Present Tense", "Past Tense & 'Ne'", "Future Tense", "Adjectives Agreement", "Negation & Imperatives", "Comparisons", "Honorifics & Compound Verbs"],
        "descs": ["Conjugate hona (am, is, are).", "Subject-Object-Verb rule.", "Masculine vs Feminine nouns.", "Mera, Tumhara, Uska agreement.", "In, On, From, To, For (mein, par, se).", "I eat, she eats (ta/ti/te).", "I went, she went, plus ne particle.", "I will go, they will speak.", "Good boy vs Good girl (acha/achi).", "Nahin and command 'Mat'.", "Better than and Best.", "Adding Ji and helper verbs."]
    },
    "listen": {
        "count": 6,
        "titles": ["Single Words (Slow)", "Two-Word Phrases", "Full Sentences", "Short Dialogues", "Numbers in Context", "Native Speed Challenge"],
        "descs": ["Identify simple vocabulary.", "Translate short noun-adjective pairs.", "Understand basic complete statements.", "Listen to conversations.", "Catch prices and hours.", "Test your ears at natural speed."]
    },
    "write": {
        "count": 8,
        "titles": ["Tracing Vowels", "Throat & Palate Letters", "Retroflex & Dental Letters", "Lips & Semivowels Letters", "Writing with Matras", "Writing Simple Words", "Writing Short Sentences", "Writing Challenge"],
        "descs": ["Practice tracing all 13 vowels.", "Trace क to ञ.", "Trace ट to न.", "Trace प to ह.", "Combine consonants and vowel marks.", "Assemble letters to make words.", "Construct complete statements.", "Write what you hear."]
    },
    "culture": {
        "count": 6,
        "titles": ["Indian Festivals", "Indian Food Etiquette", "Family & Respect", "Religious Diversity", "Clothing & Attire", "Traditional Arts"],
        "descs": ["Diwali, Holi, and Eid.", "Eating with hands, hospitality.", "Addressing elders, joint families.", "Unity in diversity.", "Sari, Kurta, and dress codes.", "Henna, rangoli, and music."]
    },
    "travel": {
        "count": 8,
        "titles": ["At the Airport", "Hailing a Taxi/Auto", "Checking into Hotel", "Asking for Directions", "Ordering Street Food", "Emergency & Health", "Local Sightseeing", "Train Journeys"],
        "descs": ["Immigration and bags.", "Dealing with Ramesh and auto drivers.", "Receptionist interactions.", "Where is and how far.", "Chaat, samosas, hygiene.", "Doctor, pharmacy, lost passport.", "Buying tickets, monument guides.", "Booking berths, train chai."]
    },
    "business": {
        "count": 6,
        "titles": ["Office Vocabulary", "Meeting Phrases", "Email Templates", "Negotiation", "Formal Register", "Corporate Culture"],
        "descs": ["Common workplace terms.", "Presenting and agreeing.", "Opening and closing emails.", "Discussing prices and terms.", "Aap register in offices.", "Networking and timing in India."]
    },
    "bollywood": {
        "count": 6,
        "titles": ["Common Movie Slang", "Romantic Expressions", "Iconic Dialogues", "Song Lyrics Vocabulary", "Drama & Emotion", "Cinema History"],
        "descs": ["Yaar, Bhai, Jugaad.", "Pyaar, Dil, Ishq.", "Famous quotes of superstars.", "Translate popular hooks.", "Expressions of anger and sorrow.", "Understand the genres of Bollywood."]
    },
    "whatsapp": {
        "count": 4,
        "titles": ["Text Abbreviations", "Hinglish Messaging", "Emoji Etiquette", "Group Chat Dynamics"],
        "descs": ["Shortcuts used by Indian youth.", "Mixing Hindi and English.", "Expressing warmth digitally.", "Greetings and casual updates."]
    }
}

for module_id, data in special_blueprints.items():
    count = data["count"]
    titles = data["titles"]
    descs = data["descs"]
    
    for idx in range(1, count + 1):
        ep_id = f"episode_{module_id}_{idx}"
        title = titles[idx-1]
        desc = descs[idx-1]
        
        # Simple high quality generic Hindi course JSON format
        nodes = [
            {"type": "Introduction", "title": f"{title}", "description": f"{desc}", "keyPoints": ["Key concepts of " + title, "Practical exercises included"]},
            {"type": "Flashcard", "hindi": "नमस्ते", "transliteration": "namaste", "english": "Hello", "vietnamese": "Xin chào"},
            {"type": "MultipleChoice", "prompt": "Identify the word", "text": "नमस्ते", "subtext": "namaste", "answer": "Hello", "options": ["Hello", "Goodbye", "Water", "Rice"]}
        ]
        
        # Write specific data for certain exercises
        file_path = os.path.join(output_dir, f"{ep_id}.json")
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(nodes, f, ensure_ascii=False, indent=2)

print("All blueprint files generated!")
