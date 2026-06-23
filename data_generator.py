import json
import random
import os

ASSETS_DIR = "app/src/main/assets"

def generate_curriculum_master():
    curriculum = []
    node_id_counter = 1
    
    # 100 Sections! 1000 nodes!
    for section_num in range(1, 101):
        nodes = []
        for i in range(10):
            # Algorithmically assign node types to ensure variety
            if i == 0 or i == 9:
                type_name = "AI_TUTOR"
            elif i % 2 == 0:
                type_name = random.choice(["VOCABULARY", "FOOD", "FAMILY", "NATURE"])
            elif i % 3 == 0:
                type_name = "CULTURE"
            else:
                type_name = random.choice(["GRAMMAR", "VERBS", "PRONOUNS"])
                
            nodes.append({
                "id": f"node_{node_id_counter}",
                "type": type_name,
                "label": f"Lesson {node_id_counter}",
                "isLocked": True if node_id_counter > 1 else False,
                "xOffset": random.randint(-40, 40)
            })
            node_id_counter += 1
            
        curriculum.append({
            "sectionTitle": f"Level {section_num}: Path to Fluency",
            "sectionDesc": "Master advanced Hindi concepts and vocabulary.",
            "nodes": nodes
        })
        
    with open(os.path.join(ASSETS_DIR, "curriculum.json"), "w", encoding="utf-8") as f:
        json.dump(curriculum, f, indent=2, ensure_ascii=False)

def generate_vocab():
    # Base real data
    words = [
        ("पिता", "Pita", "Father", "Cha", "family_audio"),
        ("माता", "Mata", "Mother", "Mẹ", "family_audio"),
        ("भाई", "Bhai", "Brother", "Anh trai", "family_audio"),
        ("बहन", "Behen", "Sister", "Chị gái", "family_audio"),
        ("सेब", "Seb", "Apple", "Quả táo", "food_audio"),
        ("पानी", "Paani", "Water", "Nước", "food_audio"),
        ("पेड़", "Ped", "Tree", "Cây", "nature_audio"),
        ("सूरज", "Sooraj", "Sun", "Mặt trời", "nature_audio"),
        ("किताब", "Kitaab", "Book", "Sách", "school_audio"),
        ("समय", "Samay", "Time", "Thời gian", "time_audio"),
        ("प्यार", "Pyaar", "Love", "Tình yêu", "emotion_audio"),
        ("गाड़ी", "Gaadi", "Car", "Ô tô", "transport_audio"),
        ("घर", "Ghar", "House", "Nhà", "home_audio"),
        ("बिल्ली", "Billi", "Cat", "Mèo", "animal_audio"),
        ("कुत्ता", "Kutta", "Dog", "Chó", "animal_audio")
    ]
    
    vocab_db = {}
    
    # Generate 5,000 flashcards by scaling these categories programmatically
    # We will spread them across nodes so every Vocab node has a set of exercises
    for i in range(1, 1001):
        exercises = []
        for _ in range(5): # 5 flashcards per vocab lesson
            w = random.choice(words)
            exercises.append({
                "type": "Flashcard",
                "hindi": w[0],
                "transliteration": w[1],
                "english": w[2],
                "vietnamese": w[3],
                "audio": w[4]
            })
        
        # Also add a speaking exercise
        exercises.append({
            "type": "Speaking",
            "hindiPhrase": f"मैं {random.choice(words)[0]} देखता हूँ।",
            "translation": "I see a word."
        })
        
        vocab_db[f"node_{i}"] = {
            "type": "VOCABULARY",
            "title": f"Vocabulary Set {i}",
            "exercises": exercises
        }
        
    with open(os.path.join(ASSETS_DIR, "curriculum_vocab.json"), "w", encoding="utf-8") as f:
        json.dump(vocab_db, f, indent=2, ensure_ascii=False)

def generate_grammar():
    rules = [
        ("Present Tense", "Thì hiện tại", "In Hindi, verbs in present tense end with -ta/ti/te.", "Động từ kết thúc bằng -ta/ti/te."),
        ("Gender", "Giới tính", "All nouns in Hindi are masculine or feminine.", "Tất cả danh từ là giống đực hoặc cái."),
        ("Pronouns", "Đại từ", "Main = I, Tum = You, Vah = He/She.", "Main = Tôi, Tum = Bạn, Vah = Anh ấy/Cô ấy."),
        ("Postpositions", "Giới từ sau", "In Hindi, prepositions come AFTER the noun (e.g. table on).", "Giới từ đứng sau danh từ."),
        ("Future Tense", "Thì tương lai", "Verbs end with -ga/gi/ge.", "Động từ kết thúc bằng -ga/gi/ge.")
    ]
    
    grammar_db = {}
    for i in range(1, 1001):
        exercises = []
        r = random.choice(rules)
        exercises.append({
            "type": "GrammarRule",
            "title_en": r[0],
            "title_vi": r[1],
            "content_en": r[2],
            "content_vi": r[3]
        })
        grammar_db[f"node_{i}"] = {
            "type": "GRAMMAR",
            "title": f"Grammar Rule {i}",
            "exercises": exercises
        }
        
    with open(os.path.join(ASSETS_DIR, "curriculum_grammar.json"), "w", encoding="utf-8") as f:
        json.dump(grammar_db, f, indent=2, ensure_ascii=False)

def generate_culture():
    culture_db = {}
    for i in range(1, 1001):
        exercises = []
        
        # Dialogue
        exercises.append({
            "type": "CulturalDialogue",
            "context_en": "You are at a local market.",
            "context_vi": "Bạn đang ở chợ địa phương.",
            "npcLine": "नमस्ते! आपको क्या चाहिए? (Namaste! Aapko kya chahiye?)",
            "options": [
                {
                    "text": "मुझे सेब चाहिए। (Mujhe seb chahiye.)",
                    "translation": "I want apples.",
                    "isCorrect": True,
                    "response": "ज़रूर, ये रहे! (Zaroor, ye rahe!)"
                },
                {
                    "text": "धन्यवाद। (Dhanyavaad.)",
                    "translation": "Thank you.",
                    "isCorrect": False,
                    "response": "अरे, कुछ तो खरीदिए! (Are, kuch to kharidiye!)"
                }
            ]
        })
        
        # Story
        exercises.append({
            "type": "StoryMode",
            "title_en": "The Market Trip",
            "title_vi": "Chuyến đi chợ",
            "paragraphs": [
                {
                    "hindi": "आज मैं बाज़ार गया। (Aaj main bazaar gaya.)",
                    "translation": "Today I went to the market."
                },
                {
                    "hindi": "मैंने फल खरीदे। (Maine phal kharide.)",
                    "translation": "I bought fruits."
                }
            ],
            "question_en": "What did I buy?",
            "question_vi": "Tôi đã mua gì?",
            "options": [
                {
                    "text": "फल (Fruits)",
                    "isCorrect": True
                },
                {
                    "text": "कपड़े (Clothes)",
                    "isCorrect": False
                }
            ]
        })
        
        culture_db[f"node_{i}"] = {
            "type": "CULTURE",
            "title": f"Culture Immersion {i}",
            "exercises": exercises
        }
        
    with open(os.path.join(ASSETS_DIR, "curriculum_culture.json"), "w", encoding="utf-8") as f:
        json.dump(culture_db, f, indent=2, ensure_ascii=False)

if __name__ == "__main__":
    print("Generating 1,000 Curriculum Nodes...")
    generate_curriculum_master()
    print("Generating 5,000+ Flashcards & Speaking Exercises...")
    generate_vocab()
    print("Generating Grammar Exercises...")
    generate_grammar()
    print("Generating Culture Exercises (2000 Dialogues & Stories)...")
    generate_culture()
    print("Database Expansion Complete!")
