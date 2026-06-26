import json
import glob
import os

episodes = glob.glob("app/src/main/assets/english_episodes/*.json")
errors = []

for ep in episodes:
    try:
        with open(ep, 'r', encoding='utf-8') as f:
            data = json.load(f)
            
            # Basic validation
            if "id" not in data: errors.append(f"{ep}: missing 'id'")
            if "title" not in data: errors.append(f"{ep}: missing 'title'")
            if "exercises" not in data or not isinstance(data["exercises"], list): 
                errors.append(f"{ep}: missing or invalid 'exercises'")
                
            for i, ex in enumerate(data.get("exercises", [])):
                if "type" not in ex:
                    errors.append(f"{ep}: exercise {i} missing 'type'")
                elif ex["type"] == "Flashcard" and "pairs" not in ex:
                    errors.append(f"{ep}: Flashcard exercise {i} missing 'pairs'")
    except json.JSONDecodeError as e:
        errors.append(f"{ep}: JSON error: {e}")
    except Exception as e:
        errors.append(f"{ep}: Error: {e}")

if errors:
    print(f"Found {len(errors)} errors:")
    for err in errors[:50]:
        print(err)
else:
    print(f"Checked {len(episodes)} episodes, no obvious JSON structure errors found.")
