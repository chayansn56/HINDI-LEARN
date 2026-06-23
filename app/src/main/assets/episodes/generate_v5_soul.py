import json
import os

# VIETANA HINDI ACADEMY - SOUL PHASE (V5) GENERATOR
# DIRECTIVE: DO NOT BUILD A DATABASE. BUILD A WORLD.
# Protagonist: John (EN) / Lan (VI)

episodes = []

# Season 1: Arrival in India
# John/Lan arrives, meets Rahul, Priya, Arjun, Grandma, Ramesh.

story_arc = [
    {
        "id": "episode_1",
        "title": "Welcome to Delhi",
        "desc": "You land in Delhi. It's loud. You don't speak Hindi.",
        "achievements": ["Say Hello", "Acknowledge someone", "Survive the airport"],
        "confidence": "You survived your first 10 minutes in India. You are braver than you think.",
        "nodes": [
            {"type": "STORY", "text": "The heat hits you the moment you step out of the airport. Delhi. It's loud, chaotic, and beautiful. You (John/Lan) take a deep breath. You are terrified."},
            {"type": "DIALOGUE", "speaker": "Ramesh (Taxi Driver)", "text": "Namaste! Taxi?", "hindi": "नमस्ते! टैक्सी?"},
            {"type": "LESSON", "title": "Namaste", "content": "'Namaste' is the universal greeting in India. It means hello, goodbye, and I respect you."},
            {"type": "QUIZ", "question": "Ramesh smiles at you and says 'Namaste'. What do you say?", "options": ["Namaste", "Goodbye", "No"], "answer": "Namaste"},
            {"type": "STORY", "text": "You nervously reply 'Namaste'. Ramesh beams with pride. He grabs your bag and throws it into his cab. The journey begins."}
        ]
    },
    {
        "id": "episode_2",
        "title": "Surviving Ramesh's Driving",
        "desc": "Ramesh drives like he has 9 lives. You only have one.",
        "achievements": ["Ask to stop", "Say Yes", "Say No"],
        "confidence": "You didn't die! And you learned how to say no. Huge progress.",
        "nodes": [
            {"type": "STORY", "text": "Ramesh weaves through traffic like a fighter pilot. A cow blocks the road. He slams the brakes. You grip the seat."},
            {"type": "DIALOGUE", "speaker": "Ramesh", "text": "You okay?", "hindi": "ठीक है? (Theek hai?)"},
            {"type": "LESSON", "title": "Theek hai", "content": "'Theek hai' is the most useful phrase in Hindi. It means 'Okay', 'Fine', 'Alright'."},
            {"type": "QUIZ", "question": "Ramesh asks 'Theek hai?' Are you okay?", "options": ["Haan (Yes)", "Nahi (No)"], "answer": "Haan (Yes)"},
            {"type": "STORY", "text": "You nod, 'Haan'. Ramesh laughs and turns the music up. You realize you actually like this chaos."}
        ]
    },
    {
        "id": "episode_3",
        "title": "Meeting Rahul",
        "desc": "You arrive at your apartment and meet your new neighbor.",
        "achievements": ["Introduce yourself", "Drink Chai", "Make a friend"],
        "confidence": "You made your first Indian friend. You belong here.",
        "nodes": [
            {"type": "STORY", "text": "You arrive at your new apartment. A guy is standing outside, holding a tiny cup of tea. He looks at you and smiles."},
            {"type": "DIALOGUE", "speaker": "Rahul", "text": "Hi! I'm Rahul. Chai piyoge? (Will you drink tea?)", "hindi": "चाय पियोगे?"},
            {"type": "LESSON", "title": "Chai", "content": "In India, you never say no to Chai. It's the language of friendship."},
            {"type": "QUIZ", "question": "Rahul offers you Chai. What is the only acceptable answer?", "options": ["Haan (Yes)", "Nahi (No)"], "answer": "Haan (Yes)"},
            {"type": "STORY", "text": "You say 'Haan'. Rahul's eyes light up. 'Excellent! I make the best chai in Delhi,' he declares. You already know he's going to be a good friend."}
        ]
    },
    {
        "id": "episode_4",
        "title": "Grandma's Kitchen",
        "desc": "Rahul introduces you to his Dadi (Grandma). You will be fed.",
        "achievements": ["Call someone Dadi", "Eat too much", "Say thank you"],
        "confidence": "You survived a Punjabi grandma's love. Your stomach is full, and so is your heart.",
        "nodes": [
            {"type": "STORY", "text": "Rahul drags you into his house. The smell of spices hits you. An old woman is cooking."},
            {"type": "DIALOGUE", "speaker": "Dadi", "text": "Aao beta! Khao! (Come child! Eat!)", "hindi": "आओ बेटा! खाओ!"},
            {"type": "LESSON", "title": "Beta & Shukriya", "content": "'Beta' means child. Elders will call you this. 'Shukriya' means thank you."},
            {"type": "QUIZ", "question": "Dadi gives you a massive plate of food. What do you say?", "options": ["Shukriya (Thank you)", "Namaste", "Nahi (No)"], "answer": "Shukriya (Thank you)"},
            {"type": "STORY", "text": "You say 'Shukriya'. Dadi pinches your cheek. 'Very good, beta,' she says, and puts another roti on your plate. You are too scared to say no."}
        ]
    },
    {
        "id": "episode_5",
        "title": "Enter Priya",
        "desc": "Rahul introduces you to the smartest person he knows.",
        "achievements": ["Use respectful pronouns", "Meet Priya", "Survive"],
        "confidence": "You are no longer scared. You are surviving. Welcome to Phase 2 of your life in India.",
        "nodes": [
            {"type": "STORY", "text": "You and Rahul are walking. A girl with books bumps into Rahul. He drops his chai. It's a tragedy."},
            {"type": "DIALOGUE", "speaker": "Priya", "text": "Sorry Rahul! Tum hamesha raaste mein khade hote ho. (You always stand in the way.)", "hindi": "सॉरी राहुल!"},
            {"type": "LESSON", "title": "Aap vs Tum", "content": "Rahul and Priya use 'Tum' (informal 'you') because they are friends. You should use 'Aap' (formal 'you') for now."},
            {"type": "QUIZ", "question": "Priya turns to you. How should you address her?", "options": ["Aap", "Tum", "Tu"], "answer": "Aap"},
            {"type": "STORY", "text": "You use 'Aap'. Priya smiles warmly. 'You're polite. Don't learn Hindi from Rahul, his grammar is terrible,' she laughs."}
        ]
    }
]

# Generate remaining 15 episodes generically for now, but keeping the format
for i in range(6, 21):
    episodes.append({
        "id": f"episode_{i}",
        "title": f"Life with Rahul and Priya - Part {i}",
        "desc": "You are navigating daily life in Delhi. It's chaotic but fun.",
        "achievements": ["Learn something new", "Laugh", "Belong"],
        "confidence": "You are doing great. Keep living.",
        "nodes": [
            {"type": "STORY", "text": f"Another day in Delhi. You are hanging out with Rahul. He is drinking his 10th cup of chai."},
            {"type": "DIALOGUE", "speaker": "Rahul", "text": "Chalo! (Let's go!)", "hindi": "चलो!"},
            {"type": "LESSON", "title": "Chalo", "content": "'Chalo' means let's go. It's used all the time."},
            {"type": "QUIZ", "question": "Rahul says 'Chalo'. What does it mean?", "options": ["Let's go", "Stop", "Eat"], "answer": "Let's go"},
            {"type": "STORY", "text": "You say 'Chalo' and follow him. You don't know where you're going, but you know it'll be an adventure."}
        ]
    })

all_eps = story_arc + episodes

out_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(out_dir, exist_ok=True)

for ep in all_eps:
    file_path = os.path.join(out_dir, f"{ep['id']}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(ep, f, indent=2, ensure_ascii=False)

print(f"Generated {len(all_eps)} SOUL PHASE episodes.")
