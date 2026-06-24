import json
import os
import random

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/english_episodes"
master_vocab_path = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes/master_vocab.json"

with open(master_vocab_path, "r", encoding="utf-8") as f:
    master_vocab = json.load(f)

def find_words(limit=8):
    return random.sample(master_vocab, limit)

def format_word(w):
    return (w.get("english", ""), "", w.get("hindi", ""), w.get("vietnamese", ""))

def generate_english_episode(title, key_points, vocab_8):
    v1, v2, v3, v4, v5, v6, v7, v8 = vocab_8
    return [
        {
            "type": "StoryMode",
            "title_en": title,
            "title_vi": title,
            "paragraphs": [
                {"hindi": f"This is the story for {title}. You see a {v1[0]}.", "translation": f"Câu chuyện cho {title}. Bạn thấy {v1[3]}."},
                {"hindi": f"Then a {v2[0]} appears.", "translation": f"Sau đó {v2[3]} xuất hiện."}
            ],
            "question_en": f"What did you see first?",
            "question_vi": "Bạn thấy gì đầu tiên?",
            "options": [
                {"text": v1[0], "isCorrect": True},
                {"text": v2[0], "isCorrect": False}
            ]
        }
    ]

for i in range(1, 101):
    words = find_words(limit=8)
    vocab_8 = [format_word(w) for w in words]
    nodes = generate_english_episode(f"Episode {i}", ["Story Practice"], vocab_8)
    filename = f"episode_{i}.json"
    with open(os.path.join(output_dir, filename), "w", encoding="utf-8") as f:
        json.dump(nodes, f, ensure_ascii=False, indent=2)

print("Generated missing story episodes.")
