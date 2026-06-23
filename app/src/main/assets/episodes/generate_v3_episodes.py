import json
import os
import random

episodes_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(episodes_dir, exist_ok=True)

# Character pool and running jokes
characters = ["Rahul", "Priya", "Arjun", "Grandma", "Taxi Driver Ramesh"]
running_jokes = [
    "Rahul drinks too much chai.",
    "Arjun gets lost easily.",
    "Grandma offers too much food.",
    "Ramesh talks nonstop."
]

def generate_episode(ep_num):
    season = (ep_num - 1) // 20 + 1
    
    # Story details based on season
    if season == 1:
        story_title = f"Arrival in India - Part {ep_num}"
        story_text = f"John is navigating his early days. He meets {random.choice(characters)}."
    elif season == 2:
        story_title = f"Living in India - Part {ep_num}"
        story_text = f"John is getting used to daily life. {random.choice(running_jokes)}"
    elif season == 3:
        story_title = f"Friendship - Part {ep_num}"
        story_text = f"Inside jokes and movie nights with friends. {random.choice(running_jokes)}"
    elif season == 4:
        story_title = f"Love & Relationships - Part {ep_num}"
        story_text = f"John learns about Indian families and relationships."
    else:
        story_title = f"Belonging - Part {ep_num}"
        story_text = f"India feels like home. John speaks Hindi confidently."

    # Generate exercises
    exercises = []

    # 1. Intro / Story
    exercises.append({
        "type": "StoryMode",
        "title_en": story_title,
        "title_vi": "Câu chuyện " + str(ep_num),
        "paragraphs": [
            {"hindi": "यह एक नई कहानी है।", "translation": story_text}
        ],
        "question_en": "What is John doing?",
        "question_vi": "John đang làm gì?",
        "options": [
            {"text": "Experiencing India", "isCorrect": True},
            {"text": "Sleeping", "isCorrect": False}
        ]
    })

    # 2. Dialogue
    exercises.append({
        "type": "DialogueMode",
        "title": "Conversation Practice",
        "lines": [
            {"speaker": "John", "hindi": "नमस्ते।", "translation": "Hello."},
            {"speaker": random.choice(characters), "hindi": "नमस्ते जॉन! कैसे हो?", "translation": "Hello John! How are you?"}
        ]
    })

    # 3. Teacher Explanation
    exercises.append({
        "type": "TeachRule",
        "title": "Teacher Priya's Tip",
        "explanation": "Remember, in Hindi, verbs change based on gender and number.",
        "simpleRule": "Masculine: -a, Feminine: -i"
    })

    # 4. Vocabulary / Flashcard
    exercises.append({
        "type": "Flashcard",
        "hindi": "दोस्त",
        "transliteration": "Dost",
        "english": "Friend",
        "vietnamese": "Bạn bè",
        "audio": "दोस्त"
    })

    # 5. Listening
    exercises.append({
        "type": "Listening",
        "audioText": "मेरा एक दोस्त है।",
        "englishTranslation": "I have a friend.",
        "translation_vi": "Tôi có một người bạn.",
        "options": ["I have a friend.", "Where is my friend?", "He is my friend."]
    })

    # 6. Speaking
    exercises.append({
        "type": "Speaking",
        "hindiPhrase": "तुम मेरे दोस्त हो।",
        "translation": "You are my friend."
    })

    # 7. SentenceBuilder
    exercises.append({
        "type": "SentenceBuilder",
        "englishSentence": "You are my friend.",
        "hindiWords": ["तुम", "मेरे", "दोस्त", "हो"],
        "correctHindiSentence": "तुम मेरे दोस्त हो"
    })

    # 8. Quiz (MultipleChoice)
    exercises.append({
        "type": "MultipleChoice",
        "prompt": "Choose the correct translation:",
        "prompt_vi": "Chọn bản dịch đúng:",
        "text": "दोस्त",
        "subtext": "Dost",
        "answer": "Friend",
        "answer_vi": "Bạn bè",
        "options": ["Friend", "Enemy", "Family", "Teacher"],
        "options_vi": ["Bạn bè", "Kẻ thù", "Gia đình", "Giáo viên"]
    })

    # 9. Summary
    exercises.append({
        "type": "RevisionSummary",
        "title": "Episode Summary",
        "takeaways": [
            "Learned new vocabulary.",
            "Practiced conversation.",
            "Understood grammar rules."
        ]
    })

    return exercises

# Generate 100 episodes
for i in range(1, 101):
    ep_data = generate_episode(i)
    file_path = os.path.join(episodes_dir, f"episode_{i}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(ep_data, f, ensure_ascii=False, indent=2)

print("Generated 100 episodes!")
