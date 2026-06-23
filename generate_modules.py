import os
import json

modules = [
    "pronunciation", "speaking", "grammar", "listening", "writing",
    "culture", "travel", "business", "bollywood", "whatsapp"
]

content_template = [
    {
        "type": "Introduction",
        "title": "Welcome to {name}",
        "description": "This module is currently under construction. Check back soon for high-quality content!",
        "keyPoints": [
            "Premium lessons coming soon.",
            "Stay tuned!"
        ]
    }
]

for m in modules:
    name = m.capitalize()
    if m == "whatsapp": name = "WhatsApp Hindi"
    if m == "bollywood": name = "Bollywood Hindi"
    if m == "travel": name = "Travel Hindi"
    if m == "business": name = "Business Hindi"
    if m == "pronunciation": name = "Pronunciation Lab"

    content = [
        {
            "type": "Introduction",
            "title": f"Welcome to {name}",
            "description": "This module is currently under construction. Check back soon for high-quality content!",
            "keyPoints": [
                "Premium lessons coming soon.",
                "Stay tuned!"
            ]
        }
    ]

    path = f"/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes/episode_{m}.json"
    with open(path, "w") as f:
        json.dump(content, f, indent=4)
