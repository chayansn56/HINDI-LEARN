import json
import os

episodes = {
    1: [
        {"type": "StoryMode", "title_en": "Arrival in Delhi", "title_vi": "Đến Delhi", "paragraphs": [
            {"hindi": "जॉन दिल्ली पहुंचता है।", "translation": "John arrives in Delhi."}
        ], "question_en": "Who arrives in Delhi?", "question_vi": "Ai đến Delhi?", "options": [
            {"text": "John", "isCorrect": True}, {"text": "Rahul", "isCorrect": False}
        ]},
        {"type": "Flashcard", "hindi": "नमस्ते", "pronunciation": "Namaste", "en": "Hello", "vi": "Xin chào", "audio": "नमस्ते"},
        {"type": "MultipleChoice", "prompt": "Translate 'Hello'", "text": "नमस्ते", "subtext": "Namaste", "answer": "Hello", "options": ["Hello", "Goodbye", "Thank you"]}
    ],
    2: [
        {"type": "StoryMode", "title_en": "Meeting Rahul", "title_vi": "Gặp Rahul", "paragraphs": [
            {"hindi": "राहुल: नमस्ते जॉन! मेरा नाम राहुल है।", "translation": "Rahul: Hello John! My name is Rahul."}
        ], "question_en": "Who does John meet?", "question_vi": "John gặp ai?", "options": [
            {"text": "Rahul", "isCorrect": True}, {"text": "Priya", "isCorrect": False}
        ]},
        {"type": "Flashcard", "hindi": "मेरा नाम...", "pronunciation": "Mera naam...", "en": "My name is...", "vi": "Tên tôi là...", "audio": "मेरा नाम"},
        {"type": "SentenceBuilder", "englishSentence": "My name is John.", "hindiWords": ["मेरा", "नाम", "जॉन", "है"], "correctHindiSentence": "मेरा नाम जॉन है"}
    ]
}

# Fill the rest with placeholders
for i in range(3, 21):
    episodes[i] = [
        {"type": "StoryMode", "title_en": f"Episode {i}", "title_vi": f"Tập {i}", "paragraphs": [
            {"hindi": "यह एक कहानी है।", "translation": "This is a story."}
        ], "question_en": "Is this a story?", "question_vi": "Đây có phải là một câu chuyện không?", "options": [
            {"text": "Yes", "isCorrect": True}, {"text": "No", "isCorrect": False}
        ]}
    ]

for ep_num, data in episodes.items():
    with open(f"episode_{ep_num}.json", "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

print("Episodes generated!")
