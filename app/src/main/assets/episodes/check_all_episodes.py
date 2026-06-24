import json
import os

episodes_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"

errors = []
success_count = 0

for filename in sorted(os.listdir(episodes_dir)):
    if filename.endswith(".json"):
        if filename in ["master_vocab.json", "dictionary.json", "numbers_1_to_1000.json"]:
            # Ignore raw dictionary / vocab list / database
            continue
        filepath = os.path.join(episodes_dir, filename)
        try:
            with open(filepath, "r", encoding="utf-8") as f:
                data = json.load(f)
            
            if not isinstance(data, list):
                # Check if it has a root object with 'nodes' array (another valid structure)
                if isinstance(data, dict) and "nodes" in data:
                    data = data["nodes"]
                else:
                    errors.append(f"{filename}: Root is not a list or dictionary with 'nodes'")
                    continue
                
            for idx, item in enumerate(data):
                if "type" not in item:
                    errors.append(f"{filename} [Index {idx}]: Missing 'type'")
                    continue
                
                type_val = item["type"]
                if type_val == "Flashcard":
                    for key in ["hindi", "transliteration"]:
                        if key not in item:
                            errors.append(f"{filename} [Index {idx} - Flashcard]: Missing key '{key}'")
                elif type_val == "MultipleChoice":
                    # Can have answer or list of option objects with isCorrect
                    has_ans = "answer" in item or "answer_vi" in item or "answer_en" in item
                    has_opt_obj = False
                    if "options" in item and len(item["options"]) > 0:
                        if isinstance(item["options"][0], dict) and "isCorrect" in item["options"][0]:
                            has_opt_obj = True
                    if not (has_ans or has_opt_obj):
                        errors.append(f"{filename} [Index {idx} - MultipleChoice]: Missing answer key or isCorrect object option")
                elif type_val == "MatchPairs":
                    if "pairs" not in item:
                        errors.append(f"{filename} [Index {idx} - MatchPairs]: Missing 'pairs'")
                elif type_val == "SentenceBuilder":
                    if "correctHindiSentence" not in item and "correctOrder" not in item:
                        errors.append(f"{filename} [Index {idx} - SentenceBuilder]: Missing 'correctHindiSentence' or 'correctOrder'")
            
            success_count += 1
        except Exception as e:
            errors.append(f"{filename}: Exception raised while loading: {str(e)}")

print(f"Audit completed: Checked {success_count} JSON files.")
if errors:
    print(f"Found {len(errors)} issues:")
    for err in errors[:10]:
        print(f" - {err}")
else:
    print("All JSON files parsed successfully and adhere to curriculum structures!")
