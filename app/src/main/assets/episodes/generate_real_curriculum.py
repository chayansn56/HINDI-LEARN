import json
import os
import random

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
master_vocab_path = os.path.join(output_dir, "master_vocab.json")

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
    
    # if limit is None, return all matches!
    return matches

def format_word(w):
    return (w.get("hindi", ""), w.get("transliteration", ""), w.get("english", ""), w.get("vietnamese", ""))

def generate_super_rich_episode(title, key_points, vocab_list, sentences_2):
    nodes = [
        {
            "type": "Introduction",
            "title": title,
            "description": f"Master the vocabulary and grammar for: {title}. Read the key points carefully.",
            "keyPoints": key_points
        }
    ]

    # chunk vocab into sets of 4
    chunks = [vocab_list[i:i+4] for i in range(0, len(vocab_list), 4)]
    
    for idx, chunk in enumerate(chunks):
        nodes.append({
            "type": "TeachRule",
            "title": f"Grammar & Phonetics - Set {chr(65+idx)}",
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
        v6 = vocab_list[1] if len(vocab_list) > 1 else v5
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
            f"You learned: {vocab_list[0][0]}, {vocab_list[-1][0]}",
            f"Practiced constructing sentences."
        ]
    })
    
    return nodes

def build_p0_episodes():
    p0_defs = {
        21: {"title": "Numbers 0–10", "kws": ["Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"], "exact": True},
        22: {"title": "Numbers 11–20 & Tens to 100", "kws": ["Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety", "One Hundred"], "exact": True},
        23: {"title": "Essential Words: Family & People", "kws": ["Mother", "Father", "Brother", "Sister", "Grandfather", "Grandmother", "Uncle", "Aunt"], "exact": False},
        24: {"title": "Essential Words: Food & Drink", "kws": ["Food", "Water", "Milk", "Tea", "Rice", "Bread", "Fruit", "Apple"], "exact": False},
        25: {"title": "Essential Words: Body & Health", "kws": ["Head", "Eye", "Hand", "Leg", "Heart", "Blood", "Doctor", "Medicine"], "exact": False},
        26: {"title": "Colors & Nature", "kws": ["Red", "Blue", "Green", "Yellow", "Sun", "Moon", "Tree", "Flower"], "exact": False},
        27: {"title": "Time & Calendar", "kws": ["Today", "Tomorrow", "Yesterday", "Day", "Night", "Month", "Year", "Week"], "exact": False},
        28: {"title": "Common Verbs", "kws": ["To eat", "To drink", "To go", "To come", "To sleep", "To see", "To hear", "To speak"], "exact": False},
    }
    
    for ep_num in range(1, 31):
        if ep_num in p0_defs:
            d = p0_defs[ep_num]
            # Use None for exact matches so we get ALL words!
            limit = None if d["exact"] else 8
            words = find_words(d["kws"], limit=limit, exact_match=d["exact"])
            
            # fallback if we couldn't find anything
            if not words:
                words = find_words(limit=8)
                
            vocab_list = [format_word(w) for w in words]
            s1 = {"english": f"I know {vocab_list[0][2]}", "vietnamese": f"Tôi biết {vocab_list[0][3]}", "words": ["मैं", vocab_list[0][0], "जानता", "हूँ"], "correct": f"मैं {vocab_list[0][0]} जानता हूँ"}
            
            v2 = vocab_list[1] if len(vocab_list) > 1 else vocab_list[0]
            s2 = {"english": f"Where is {v2[2]}?", "vietnamese": f"{v2[3]} ở đâu?", "words": [v2[0], "कहाँ", "है", "था"], "correct": f"{v2[0]} कहाँ है"}
            
            nodes = generate_super_rich_episode(d["title"], [f"Focus: {d['title']}"], vocab_list, [s1, s2])
            
            filename = f"episode_0_{ep_num}.json"
            with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
                json.dump(nodes, f, ensure_ascii=False, indent=2)

def build_numbers_lab():
    defs = {
        1: {"title": "Numbers 0-100", "kws": ["One", "Two", "Fifty", "One Hundred", "Twenty-Five", "Ten", "Twenty", "Thirty"], "exact": True},
        2: {"title": "Hundreds 100-500", "kws": ["One Hundred", "Two Hundred", "Three Hundred", "Four Hundred", "Five Hundred"], "exact": False},
        3: {"title": "Hundreds 500-1000", "kws": ["Six Hundred", "Seven Hundred", "Eight Hundred", "Nine Hundred", "One Thousand"], "exact": False},
        4: {"title": "Large Numbers Practice", "kws": ["One Thousand", "Ten Thousand", "Lakh", "Crore", "Million"], "exact": False},
        5: {"title": "Numbers Challenge", "kws": ["One", "Ten", "Hundred", "Thousand", "Lakh", "Crore", "Fifty", "Twenty"], "exact": False},
    }
    
    for i in range(1, 6):
        d = defs[i]
        words = find_words(d["kws"], limit=None if d["exact"] else 8, exact_match=d["exact"])
        if not words: words = find_words(limit=8)
        vocab_list = [format_word(w) for w in words]
        
        s1 = {"english": f"I have {vocab_list[0][2]}", "vietnamese": f"Tôi có {vocab_list[0][3]}", "words": ["मेरे", "पास", vocab_list[0][0], "है"], "correct": f"मेरे पास {vocab_list[0][0]} है"}
        v2 = vocab_list[1] if len(vocab_list) > 1 else vocab_list[0]
        s2 = {"english": f"Price is {v2[2]}", "vietnamese": f"Giá là {v2[3]}", "words": ["दाम", v2[0], "है", "था"], "correct": f"दाम {v2[0]} है"}
        
        nodes = generate_super_rich_episode(d["title"], [f"Master {d['title']}"], vocab_list, [s1, s2])
        filename = f"episode_numbers_lab_{i}.json"
        with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
            json.dump(nodes, f, ensure_ascii=False, indent=2)

def build_other_groups():
    groups = {
        "speak": ["Speak", "Talk", "Say", "Hello", "How", "What", "Where", "Why", "When"],
        "listen": ["Hear", "Listen", "Sound", "Voice", "Loud", "Soft", "Music", "Song"],
        "write": ["Write", "Read", "Book", "Pen", "Paper", "Letter", "Word", "Sentence"],
        "gram": ["Rule", "Grammar", "Tense", "Past", "Future", "Present", "Noun", "Verb"],
        "culture": ["Festival", "Food", "Dance", "Music", "Art", "Temple", "Religion", "Tradition"],
        "travel": ["Journey", "Train", "Bus", "Car", "Ticket", "Hotel", "Room", "Station"],
        "business": ["Office", "Meeting", "Boss", "Worker", "Money", "Price", "Contract", "Job"],
        "bollywood": ["Movie", "Song", "Hero", "Heroine", "Dance", "Drama", "Action", "Romance"],
        "whatsapp": ["Message", "Call", "Phone", "Send", "Receive", "Friend", "Group", "Chat"],
        "emerg": ["Help", "Doctor", "Police", "Hospital", "Fire", "Emergency", "Medicine", "Pain"],
        "alphabets_lab": ["A", "B", "C", "Letter", "Word", "Sign", "Name", "Write"],
        "pron": ["Vowel", "Consonant", "Sound", "Speak", "Loud", "Voice", "Listen", "Word"]
    }
    
    for g, kws in groups.items():
        for i in range(1, 20):
            words = find_words(kws, limit=8, exact_match=False)
            vocab_list = [format_word(w) for w in words]
            s1 = {"english": f"This is a {vocab_list[0][2]}", "vietnamese": f"Đây là {vocab_list[0][3]}", "words": ["यह", "एक", vocab_list[0][0], "है"], "correct": f"यह एक {vocab_list[0][0]} है"}
            v2 = vocab_list[1] if len(vocab_list) > 1 else vocab_list[0]
            s2 = {"english": f"I see {v2[2]}", "vietnamese": f"Tôi thấy {v2[3]}", "words": ["मैं", v2[0], "देखता", "हूँ"], "correct": f"मैं {v2[0]} देखता हूँ"}
            
            nodes = generate_super_rich_episode(f"{g.capitalize()} - Level {i}", [f"Learn {g} concepts."], vocab_list, [s1, s2])
            filename = f"episode_{g}_{i}.json"
            with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
                json.dump(nodes, f, ensure_ascii=False, indent=2)

if __name__ == "__main__":
    print("Building Phase 0...")
    build_p0_episodes()
    print("Building Numbers Lab...")
    build_numbers_lab()
    print("Building Other Groups...")
    build_other_groups()
    print("Done generating highly relevant data.")

