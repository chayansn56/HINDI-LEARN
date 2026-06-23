import json
import os

# VIETANA HINDI ACADEMY - PHASE 0 GENERATOR
# DIRECTIVE: THE FOUNDATION ACADEMY
# Protagonist: John (EN) / Lan (VI)

episodes = [
    {
        "id": "episode_0_1",
        "title": "Sounds Academy",
        "desc": "Learn what Hindi sounds are.",
        "achievements": ["Understand sounds", "Mouth positioning"],
        "confidence": "You can now understand Hindi sounds. You have ears for Hindi.",
        "nodes": [
            {"type": "LESSON", "title": "Teacher Introduction", "content": "Welcome to Phase 0. I am your teacher. Today we learn sounds."},
            {"type": "STORY", "text": "Hindi has sounds that don't exist in English or Vietnamese. You must train your ears first."},
            {"type": "DIALOGUE", "speaker": "Teacher", "text": "Listen to the vibration. Ba vs Bha.", "hindi": "ब vs भ"},
            {"type": "LESSON", "title": "Teacher Explanation", "content": "The difference is breathing. Say 'Ba' normally. Now say 'Bha' like you are blowing out a candle."},
            {"type": "LESSON", "title": "Examples", "content": "Ba = Goat (Bakra). Bha = Brother (Bhai)."},
            {"type": "LESSON", "title": "Visual Explanation", "content": "Imagine your mouth. Tongue touches the roof for retroflex sounds (Ta, Da)."},
            {"type": "LESSON", "title": "Vocabulary", "content": "No words to memorize yet. Just focus on sounds."},
            {"type": "DIALOGUE", "speaker": "Audio", "text": "Listen slowly: ta... tha... da... dha... na.", "hindi": "ट ठ ड ढ ण"},
            {"type": "LESSON", "title": "Speaking Practice", "content": "Your turn. Repeat out loud. Feel the vibration in your throat."},
            {"type": "LESSON", "title": "Writing Practice", "content": "No writing yet. Your ear is your pen today."},
            {"type": "STORY", "text": "Let's play a game. I will say a sound, you guess if it has extra air (aspirated)."},
            {"type": "QUIZ", "question": "Does 'Bhai' (Brother) have extra air (aspiration)?", "options": ["Yes", "No"], "answer": "Yes"},
            {"type": "LESSON", "title": "Revision", "content": "Breathing makes a new letter. Tongue position makes a new letter."},
            {"type": "LESSON", "title": "Summary", "content": "You learned about aspiration and retroflex sounds."},
            {"type": "STORY", "text": "🎉 Great! Today you can: ✓ Distinguish Hindi sounds. ✓ Understand mouth position. ✓ Hear the difference."}
        ]
    },
    {
        "id": "episode_0_2",
        "title": "Vowel Academy",
        "desc": "Master all Hindi vowels.",
        "achievements": ["Read vowels", "Write vowels"],
        "confidence": "You can now read 13 vowels.",
        "nodes": [
            {"type": "LESSON", "title": "Teacher Introduction", "content": "Welcome to the Vowel Academy. Let's learn the 13 vowels of Hindi."},
            {"type": "STORY", "text": "Vowels are the soul of the alphabet. Without them, consonants cannot speak."},
            {"type": "DIALOGUE", "speaker": "Teacher", "text": "A, Aa, I, Ii...", "hindi": "अ आ इ ई"},
            {"type": "LESSON", "title": "Teacher Explanation", "content": "Hindi vowels come in pairs. Short and long. 'अ' (A) is short. 'आ' (Aa) is long."},
            {"type": "LESSON", "title": "Examples", "content": "Anar (Pomegranate) starts with अ. Aam (Mango) starts with आ."},
            {"type": "LESSON", "title": "Visual Explanation", "content": "Notice how 'आ' is just 'अ' with an extra stick on the right."},
            {"type": "LESSON", "title": "Vocabulary", "content": "अ (A), आ (Aa), इ (I), ई (Ee), उ (U), ऊ (Oo)"},
            {"type": "DIALOGUE", "speaker": "Audio", "text": "Listen and repeat.", "hindi": "अ आ इ ई उ ऊ"},
            {"type": "LESSON", "title": "Speaking Practice", "content": "Open your mouth wide for 'Aa'. Keep it relaxed for 'A'."},
            {"type": "LESSON", "title": "Writing Practice", "content": "Trace the letter 'अ'. Top curve, bottom curve, middle line, standing line, top roof."},
            {"type": "STORY", "text": "Let's play a matching game in your mind."},
            {"type": "QUIZ", "question": "Which one is the LONG 'A'?", "options": ["अ", "आ"], "answer": "आ"},
            {"type": "LESSON", "title": "Revision", "content": "Short vs Long. The extra line makes it long."},
            {"type": "LESSON", "title": "Summary", "content": "You learned the first 6 vowels."},
            {"type": "STORY", "text": "🎉 Great! Today you can: ✓ Read Hindi vowels. ✓ Distinguish short and long sounds."}
        ]
    }
]

# Generate remaining 8 episodes for Phase 0 based on the Master Script
topics = [
    ("Consonant Academy", "Master all consonants.", ["Read consonants", "Write consonants"]),
    ("Matra Academy", "Learn vowel marks.", ["Use Matras", "Visual transformations"]),
    ("Reading Academy", "Read your first words.", ["Read words", "Read sentences"]),
    ("Number Academy", "0 to 100.", ["Say numbers", "Say prices"]),
    ("Survival Sentences", "The 10 sentences you need to live.", ["Say hello", "Ask for help"]),
    ("Listening Academy", "Train your ear.", ["Understand native speed", "Identify voices"]),
    ("Writing Academy", "Type and write.", ["Build sentences", "Fill blanks"]),
    ("Confidence Academy", "You are ready.", ["Read 50 words", "Survive Phase 0"])
]

for i in range(3, 11):
    topic = topics[i-3]
    episodes.append({
        "id": f"episode_0_{i}",
        "title": topic[0],
        "desc": topic[1],
        "achievements": topic[2],
        "confidence": "You are getting stronger every day.",
        "nodes": [
            {"type": "LESSON", "title": "Teacher Introduction", "content": f"Welcome to {topic[0]}."},
            {"type": "STORY", "text": "Every step you take builds your foundation."},
            {"type": "DIALOGUE", "speaker": "Teacher", "text": "Let's begin.", "hindi": "शुरू करें।"},
            {"type": "LESSON", "title": "Teacher Explanation", "content": "Here is the core concept of today's lesson."},
            {"type": "LESSON", "title": "Examples", "content": "Observe how this is used in daily life."},
            {"type": "LESSON", "title": "Visual Explanation", "content": "Look at the pattern."},
            {"type": "LESSON", "title": "Vocabulary", "content": "New words to empower you."},
            {"type": "DIALOGUE", "speaker": "Audio", "text": "Listen carefully.", "hindi": "ध्यान से सुनो।"},
            {"type": "LESSON", "title": "Speaking Practice", "content": "Say it out loud."},
            {"type": "LESSON", "title": "Writing Practice", "content": "Write it down."},
            {"type": "STORY", "text": "Time for a game."},
            {"type": "QUIZ", "question": "Are you ready to move forward?", "options": ["Yes", "Always"], "answer": "Yes"},
            {"type": "LESSON", "title": "Revision", "content": "Let's review what we learned."},
            {"type": "LESSON", "title": "Summary", "content": "You mastered today's topic."},
            {"type": "STORY", "text": "🎉 Great! Today you leveled up your foundation."}
        ]
    })

def create_files():
    output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
    os.makedirs(output_dir, exist_ok=True)
    
    for episode in episodes:
        file_path = os.path.join(output_dir, f"{episode['id']}.json")
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(episode, f, indent=4, ensure_ascii=False)
        print(f"Generated {file_path}")

if __name__ == "__main__":
    create_files()
