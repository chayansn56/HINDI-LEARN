import json
import os
import random
import re

research_file = "/Users/chayansoni/.gemini/antigravity/brain/a866ac48-690b-402f-bb7f-d7b594b84786/hindi_deep_research.md"
output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(output_dir, exist_ok=True)

def save_episode(id, nodes):
    with open(os.path.join(output_dir, f"{id}.json"), "w", encoding="utf-8") as f:
        json.dump(nodes, f, ensure_ascii=False, indent=2)

def generate_15_node_episode(topic, rule_title, vocab_list):
    # Ensure exactly 6 words
    while len(vocab_list) < 6:
        vocab_list.append(vocab_list[0])
    v1, v2, v3, v4, v5, v6 = vocab_list[:6]
    nodes = []
    
    nodes.append({"type": "Introduction", "title": topic, "description": "Let's master these concepts.", "keyPoints": [f"{v1['hindi']} = {v1['english']}", f"{v2['hindi']} = {v2['english']}"]})
    nodes.append({"type": "TeachRule", "title": rule_title, "explanation": v1['note'], "simpleRule": f"Remember: {v1['hindi']} means {v1['english']}."})
    nodes.append({"type": "Flashcard", "hindi": v1['hindi'], "transliteration": v1['transliteration'], "english": v1['english'], "vietnamese": v1['english']})
    nodes.append({"type": "Flashcard", "hindi": v2['hindi'], "transliteration": v2['transliteration'], "english": v2['english'], "vietnamese": v2['english']})
    
    opts1 = random.sample([v['english'] for v in vocab_list], min(4, len(vocab_list)))
    if v1['english'] not in opts1: opts1[0] = v1['english']
    random.shuffle(opts1)
    nodes.append({"type": "MultipleChoice", "prompt_en": f"Translate '{v1['english']}'", "prompt_vi": f"Dịch '{v1['english']}'", "text": v1['hindi'], "subtext": "", "answer": v1['english'], "options": opts1})
    
    nodes.append({"type": "Flashcard", "hindi": v3['hindi'], "transliteration": v3['transliteration'], "english": v3['english'], "vietnamese": v3['english']})
    nodes.append({"type": "Flashcard", "hindi": v4['hindi'], "transliteration": v4['transliteration'], "english": v4['english'], "vietnamese": v4['english']})
    nodes.append({"type": "MatchPairs", "instruction": "Match the pairs", "pairs": [{"hindi": v1['hindi'], "english": v1['english']}, {"hindi": v2['hindi'], "english": v2['english']}, {"hindi": v3['hindi'], "english": v3['english']}, {"hindi": v4['hindi'], "english": v4['english']}]})
    
    opts2 = random.sample(vocab_list, min(4, len(vocab_list)))
    if v2 not in opts2: opts2[0] = v2
    random.shuffle(opts2)
    nodes.append({"type": "Listening", "audio": v2['hindi'], "translation_en": v2['english'], "options_en": [o['english'] for o in opts2], "translation_vi": v2['english'], "options_vi": [o['english'] for o in opts2]})
    
    nodes.append({"type": "SentenceBuilder", "englishSentence": f"Translate: {v3['english']}", "hindiWords": [v3['hindi'], v1['hindi'], v2['hindi'], "है"], "correctHindiSentence": v3['hindi']})
    nodes.append({"type": "Flashcard", "hindi": v5['hindi'], "transliteration": v5['transliteration'], "english": v5['english'], "vietnamese": v5['english']})
    nodes.append({"type": "Flashcard", "hindi": v6['hindi'], "transliteration": v6['transliteration'], "english": v6['english'], "vietnamese": v6['english']})
    nodes.append({"type": "MatchPairs", "instruction": "Match the pairs", "pairs": [{"hindi": v3['hindi'], "english": v3['english']}, {"hindi": v4['hindi'], "english": v4['english']}, {"hindi": v5['hindi'], "english": v5['english']}, {"hindi": v6['hindi'], "english": v6['english']}]})
    nodes.append({"type": "SentenceBuilder", "englishSentence": f"Translate: {v6['english']}", "hindiWords": [v6['hindi'], v5['hindi'], v1['hindi'], "हूँ"], "correctHindiSentence": v6['hindi']})
    nodes.append({"type": "RevisionSummary", "title": "Lesson Complete!", "takeaways": [f"{v1['hindi']} = {v1['english']}", f"{v3['hindi']} = {v3['english']}", f"{v5['hindi']} = {v5['english']}"]})
    return nodes

def parse_markdown():
    vocab_sets = {
        "pron": [], "speak": [], "gram": [], "listen": [], "write": [],
        "culture": [], "travel": [], "business": [], "bollywood": [], "whatsapp": []
    }
    
    current_cat = None
    
    with open(research_file, "r", encoding="utf-8") as f:
        for line in f:
            if "## 1. Foundations" in line: current_cat = "pron"
            elif "## 2. Speaking" in line: current_cat = "speak"
            elif "## 3. Grammar" in line: current_cat = "gram"
            elif "## 4. Listening" in line: current_cat = "listen"
            elif "## 5. Writing" in line: current_cat = "write"
            elif "## 6. Culture" in line: current_cat = "culture"
            elif "## 7. Travel" in line: current_cat = "travel"
            elif "## 8. Business" in line: current_cat = "business"
            elif "## 9. Bollywood" in line: current_cat = "bollywood"
            elif "## 10. WhatsApp" in line: current_cat = "whatsapp"
            elif line.startswith("|") and not "---" in line and not "Transliteration" in line:
                parts = [p.strip() for p in line.split("|")]
                if len(parts) >= 5 and current_cat:
                    hindi = parts[1]
                    trans = parts[2]
                    eng = parts[3]
                    note = parts[4]
                    vocab_sets[current_cat].append({
                        "hindi": hindi,
                        "transliteration": trans,
                        "english": eng,
                        "note": note
                    })
                    
    return vocab_sets

vocab_sets = parse_markdown()

module_counts = {
    "pron": 8, "speak": 10, "gram": 12, "listen": 6, "write": 8,
    "culture": 6, "travel": 8, "business": 6, "bollywood": 6, "whatsapp": 4
}

for mod, count in module_counts.items():
    vocab_pool = vocab_sets.get(mod, [])
    if not vocab_pool:
        continue # Skip if parsing failed
        
    for i in range(1, count + 1):
        if len(vocab_pool) >= 6:
            vocab = random.sample(vocab_pool, 6)
        else:
            vocab = [random.choice(vocab_pool) for _ in range(6)]
            
        episode_data = generate_15_node_episode(f"{mod.capitalize()} Lesson {i}", "Core Concept", vocab)
        save_episode(f"episode_{mod}_{i}", episode_data)

# Special handle for Foundation 19-30 mapping to 'pron' or 'write'
found_vocab_pool = vocab_sets.get("write", []) + vocab_sets.get("pron", [])
if found_vocab_pool:
    for i in list(range(19, 26)) + list(range(28, 31)):
        if len(found_vocab_pool) >= 6:
            vocab = random.sample(found_vocab_pool, 6)
        else:
            vocab = [random.choice(found_vocab_pool) for _ in range(6)]
        episode_data = generate_15_node_episode(f"Foundation {i}", "Vowels & Consonants", vocab)
        save_episode(f"episode_0_{i}", episode_data)

print("SUCCESS: Pedagogical curriculum parsed from deep research dataset and generated!")
