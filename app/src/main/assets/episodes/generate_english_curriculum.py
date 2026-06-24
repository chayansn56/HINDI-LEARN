import json
import os
import random

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/english_episodes"
master_vocab_path = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes/master_vocab.json"

if not os.path.exists(output_dir):
    os.makedirs(output_dir)

with open(master_vocab_path, "r", encoding="utf-8") as f:
    master_vocab = json.load(f)

def find_words(keywords=None, categories=None, limit=None, exact_match=False):
    matches = []
    for word in master_vocab:
        if categories and word.get("category") not in categories:
            continue
        if keywords:
            found = False
            for kw in keywords:
                if exact_match:
                    if kw.lower() == word.get("english", "").lower():
                        found = True
                        break
                else:
                    if kw.lower() in word.get("english", "").lower() or kw.lower() in word.get("hindi", ""):
                        found = True
                        break
            if not found:
                continue
        matches.append(word)
    
    if limit is not None:
        if len(matches) > limit:
            return random.sample(matches, limit)
        if len(matches) < limit:
            needed = limit - len(matches)
            pool = [w for w in master_vocab if w not in matches]
            if len(pool) >= needed:
                matches.extend(random.sample(pool, needed))
            else:
                matches.extend(pool)
            return matches[:limit]
    
    return matches

def format_word(w):
    # For English course: hindi = Target English word, english = Known Hindi translation, vietnamese = Known Vietnamese
    return (w.get("english", ""), w.get("pronunciation", ""), w.get("hindi", ""), w.get("vietnamese", ""))

def generate_super_rich_episode(title, key_points, vocab_list, sentences_2):
    nodes = [
        {
            "type": "Introduction",
            "title": title,
            "description": f"Master the vocabulary and grammar for: {title}. Read the key points carefully.",
            "keyPoints": key_points
        }
    ]

    chunks = [vocab_list[i:i+4] for i in range(0, len(vocab_list), 4)]
    
    for idx, chunk in enumerate(chunks):
        nodes.append({
            "type": "TeachRule",
            "title": f"Grammar & Vocabulary - Set {chr(65+idx)}",
            "explanation": f"Understand the usage and roots of: {chunk[0][0]}.",
            "simpleRule": f"Remember: {chunk[0][0]} means {chunk[0][2]} ({chunk[0][3]})."
        })
        
        for v in chunk:
            nodes.append({
                "type": "Flashcard",
                "hindi": v[0],
                "transliteration": v[1],
                "english": v[2],
                "vietnamese": v[3],
                "audio": v[0]
            })
            
        if len(chunk) >= 4:
            nodes.append({
                "type": "MultipleChoice",
                "prompt": f"Translate '{chunk[0][0]}'",
                "prompt_en": f"Choose correct translation for '{chunk[0][0]}'",
                "prompt_vi": f"Chọn bản dịch đúng cho '{chunk[0][0]}'",
                "text": chunk[0][0],
                "subtext": chunk[0][1],
                "answer": chunk[0][2],
                "answer_vi": chunk[0][3],
                "options": [chunk[0][2], chunk[1][2], chunk[2][2], chunk[3][2]],
                "options_vi": [chunk[0][3], chunk[1][3], chunk[2][3], chunk[3][3]]
            })
            nodes.append({
                "type": "MatchPairs",
                "instruction": "Match all the words with their meanings",
                "pairs": [
                    {"hindi": v[0], "english": f"{v[2]} ({v[3]})"} for v in chunk
                ]
            })

    if len(vocab_list) >= 4:
        v5 = vocab_list[0]
        nodes.append({
            "type": "Listening",
            "audio": v5[0],
            "translation_en": v5[2],
            "translation_vi": v5[3],
            "options_en": [v[2] for v in vocab_list[:4]],
            "options_vi": [v[3] for v in vocab_list[:4]]
        })

    for s in sentences_2:
        nodes.append({
            "type": "SentenceBuilder",
            "englishSentence": f"{s['english']} / {s['vietnamese']}",
            "hindiWords": s["words"],
            "correctHindiSentence": s["correct"]
        })

    nodes.append({
        "type": "RevisionSummary",
        "title": "Subgroup Mastered!",
        "takeaways": [
            f"You learned: {vocab_list[0][0]} and {vocab_list[-1][0]}",
            f"Practiced constructing sentences."
        ]
    })
    
    return nodes

def build_p0_episodes():
    p0_defs = {
        1: {"title": "The English Alphabet", "kws": ["Apple", "Boy", "Cat", "Dog", "Elephant", "Fish", "Girl", "House", "Ice", "Juice", "Kite", "Lion"], "exact": False},
        2: {"title": "Vowels & Consonants", "kws": ["Ant", "Egg", "Igloo", "Octopus", "Umbrella"], "exact": False},
        3: {"title": "Basic Greetings", "kws": ["Hello", "Goodbye", "Morning", "Night", "Thank you", "Sorry", "Please", "Welcome"], "exact": False},
        4: {"title": "Simple Pronouns", "kws": ["I", "You", "He", "She", "It", "We", "They", "Me", "Him", "Her", "Us", "Them"], "exact": False},
        5: {"title": "To Be: Present", "kws": ["Am", "Is", "Are"], "exact": False},
        8: {"title": "Numbers 0-20", "kws": ["Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve"], "exact": True},
        9: {"title": "Numbers 20-100", "kws": ["Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety", "Hundred"], "exact": True},
        11: {"title": "Colors & Shapes", "kws": ["Red", "Blue", "Green", "Yellow", "Circle", "Square", "Triangle", "Black", "White"], "exact": False},
        12: {"title": "Family Members", "kws": ["Mother", "Father", "Brother", "Sister", "Uncle", "Aunt", "Son", "Daughter", "Grandmother", "Grandfather"], "exact": False},
        13: {"title": "Food & Drink", "kws": ["Water", "Food", "Milk", "Tea", "Rice", "Bread", "Fruit", "Meat", "Chicken", "Fish"], "exact": False},
        14: {"title": "Body Parts", "kws": ["Head", "Eye", "Ear", "Nose", "Mouth", "Hand", "Leg", "Foot", "Arm", "Finger"], "exact": False},
        16: {"title": "Simple Present Tense", "kws": ["Eat", "Drink", "Sleep", "Walk", "Run", "Read", "Write", "Speak", "Listen"], "exact": False},
        20: {"title": "Weather & Seasons", "kws": ["Sun", "Rain", "Wind", "Snow", "Hot", "Cold", "Summer", "Winter", "Spring"], "exact": False},
        21: {"title": "Adjectives: Opposites", "kws": ["Big", "Small", "Tall", "Short", "Fast", "Slow", "Good", "Bad", "Happy", "Sad"], "exact": False},
    }
    
    for ep_num in range(1, 31):
        if ep_num in p0_defs:
            d = p0_defs[ep_num]
            limit = None if d["exact"] else 12
            words = find_words(d["kws"], limit=limit, exact_match=d["exact"])
            if not words: words = find_words(limit=12)
            
            vocab_list = [format_word(w) for w in words]
            s1 = {"english": f"I see the {vocab_list[0][2]}", "vietnamese": f"Tôi thấy {vocab_list[0][3]}", "words": ["I", "see", "the", vocab_list[0][0]], "correct": f"I see the {vocab_list[0][0]}"}
            
            v2 = vocab_list[1] if len(vocab_list) > 1 else vocab_list[0]
            s2 = {"english": f"Where is {v2[2]}?", "vietnamese": f"{v2[3]} ở đâu?", "words": ["Where", "is", "the", v2[0]], "correct": f"Where is the {v2[0]}"}
            
            nodes = generate_super_rich_episode(d["title"], [f"Focus: {d['title']}"], vocab_list, [s1, s2])
            
            filename = f"episode_0_{ep_num}.json"
            with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
                json.dump(nodes, f, ensure_ascii=False, indent=2)
        else:
            words = find_words(limit=12)
            vocab_list = [format_word(w) for w in words]
            s1 = {"english": f"I have a {vocab_list[0][2]}", "vietnamese": f"Tôi có một {vocab_list[0][3]}", "words": ["I", "have", "a", vocab_list[0][0]], "correct": f"I have a {vocab_list[0][0]}"}
            s2 = {"english": f"This is a {vocab_list[1][2]}", "vietnamese": f"Đây là một {vocab_list[1][3]}", "words": ["This", "is", "a", vocab_list[1][0]], "correct": f"This is a {vocab_list[1][0]}"}
            
            nodes = generate_super_rich_episode(f"Episode {ep_num}", ["Foundation Practice"], vocab_list, [s1, s2])
            filename = f"episode_0_{ep_num}.json"
            with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
                json.dump(nodes, f, ensure_ascii=False, indent=2)

def build_other_groups():
    groups = {
        "pron": ["Vowel", "Consonant", "Sound", "Speak", "Loud", "Voice", "Listen", "Word", "Say", "Pronounce", "Tongue", "Mouth"],
        "speak": ["Hello", "Talk", "Say", "Friend", "Name", "How", "What", "When", "Where", "Why", "Who", "Yes", "No"],
        "gram": ["Rule", "Time", "Now", "Then", "Always", "Never", "Word", "Sentence", "Subject", "Verb", "Object"],
        "listen": ["Hear", "Music", "Song", "Radio", "News", "Voice", "Sound", "Loud", "Quiet", "Ear", "Audio"],
        "write": ["Pen", "Paper", "Letter", "Write", "Read", "Book", "Word", "Sign", "Notebook", "Pencil", "Ink"],
        "culture": ["Festival", "Party", "Dance", "Music", "Art", "Food", "Drink", "Tradition", "Culture", "Custom", "Habit"],
        "travel": ["Journey", "Train", "Bus", "Car", "Ticket", "Hotel", "Room", "Station", "Airport", "Flight", "Luggage", "Map"],
        "business": ["Office", "Meeting", "Boss", "Work", "Money", "Price", "Contract", "Job", "Company", "Employee", "Salary"],
        "bollywood": ["Movie", "Song", "Hero", "Dance", "Drama", "Action", "Romance", "Star", "Cinema", "Film", "Actor", "Actress"],
        "whatsapp": ["Message", "Call", "Phone", "Send", "Receive", "Friend", "Chat", "Text", "Emoji", "Online", "Status"],
        "emerg": ["Help", "Doctor", "Police", "Hospital", "Fire", "Emergency", "Medicine", "Pain", "Accident", "Ambulance", "Nurse"],
        "numbers_lab": ["One", "Two", "Three", "Ten", "Hundred", "Thousand", "Fifty", "Twenty", "Zero", "Million", "Billion"],
        "alphabets_lab": ["A", "B", "C", "Letter", "Word", "Sign", "Name", "Write", "Alphabet", "Spell", "Read"]
    }
    
    for g, kws in groups.items():
        for i in range(1, 20):
            words = find_words(kws, limit=16, exact_match=False)
            vocab_list = [format_word(w) for w in words]
            s1 = {"english": f"It is a {vocab_list[0][2]}", "vietnamese": f"Nó là một {vocab_list[0][3]}", "words": ["It", "is", "a", vocab_list[0][0]], "correct": f"It is a {vocab_list[0][0]}"}
            v2 = vocab_list[1] if len(vocab_list) > 1 else vocab_list[0]
            s2 = {"english": f"I like {v2[2]}", "vietnamese": f"Tôi thích {v2[3]}", "words": ["I", "really", "like", v2[0]], "correct": f"I really like {v2[0]}"}
            
            nodes = generate_super_rich_episode(f"{g.capitalize()} - Level {i}", [f"Learn {g} concepts in depth."], vocab_list, [s1, s2])
            filename = f"episode_{g}_{i}.json"
            with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
                json.dump(nodes, f, ensure_ascii=False, indent=2)

def build_story_episodes():
    for i in range(1, 101):
        words = find_words(limit=12)
        vocab_list = [format_word(w) for w in words]
        nodes = generate_super_rich_episode(f"Episode {i}", ["Story Practice & Vocabulary Expansion"], vocab_list, [
            {"english": f"I see the {vocab_list[0][2]}", "vietnamese": f"Tôi thấy {vocab_list[0][3]}", "words": ["I", "see", "the", vocab_list[0][0]], "correct": f"I see the {vocab_list[0][0]}"},
            {"english": f"I want a {vocab_list[1][2]}", "vietnamese": f"Tôi muốn {vocab_list[1][3]}", "words": ["I", "want", "a", vocab_list[1][0]], "correct": f"I want a {vocab_list[1][0]}"}
        ])
        
        # Inject StoryMode into the beginning
        story_node = {
            "type": "StoryMode",
            "title_en": f"Episode {i}",
            "title_vi": f"Tập {i}",
            "paragraphs": [
                {"hindi": f"You arrive at the new city. You see a {vocab_list[0][0]} ({vocab_list[0][2]}).", "translation": f"Bạn đến thành phố mới. Bạn thấy một {vocab_list[0][3]}."},
                {"hindi": f"Later, you encounter a {vocab_list[1][0]} ({vocab_list[1][2]}).", "translation": f"Sau đó, bạn gặp một {vocab_list[1][3]}."}
            ],
            "question_en": f"What did you see first?",
            "question_vi": "Bạn thấy gì đầu tiên?",
            "options": [
                {"text": vocab_list[0][0], "isCorrect": True},
                {"text": vocab_list[1][0], "isCorrect": False}
            ]
        }
        nodes.insert(1, story_node)
        
        filename = f"episode_{i}.json"
        with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
            json.dump(nodes, f, ensure_ascii=False, indent=2)

if __name__ == "__main__":
    print("Building Rich English Phase 0...")
    build_p0_episodes()
    print("Building Rich English Other Groups...")
    build_other_groups()
    print("Building Rich English Stories...")
    build_story_episodes()
    print("Done generating massive English data.")
