# -*- coding: utf-8 -*-
import os
import json
from collections import defaultdict

base_path = '/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/english_episodes'

phases = {
    "Phase 0": [f"episode_0_{i}.json" for i in range(1, 15)],
    "Phase 1": [f"episode_1_{i}.json" for i in range(1, 17)],
    "Phase 2": [f"episode_2_{i}.json" for i in range(1, 16)],
    "Phase 3": [f"episode_3_{i}.json" for i in range(1, 16)]
}

vocab_by_phase = defaultdict(set)
vocab_locations = defaultdict(list)

for phase_name, files in phases.items():
    for filename in files:
        file_path = os.path.join(base_path, filename)
        if not os.path.exists(file_path):
            print(f"Warning: File {filename} does not exist!")
            continue
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = json.load(f)
                for idx, node in enumerate(data):
                    node_type = node.get("type")
                    if node_type == "Flashcard":
                        word = node.get("hindi") or node.get("english")
                        if word:
                            word = word.strip().lower()
                            vocab_by_phase[phase_name].add(word)
                            vocab_locations[word].append(f"{filename} (Node {idx})")
        except Exception as e:
            print(f"Error reading {filename}: {e}")

print("=== English Curriculum Vocabulary Audit ===")
for phase_name, words in vocab_by_phase.items():
    print(f"{phase_name}: {len(words)} unique words/phrases")

print("\n=== Duplicate Vocabulary Check ===")
has_duplicates = False
all_words = list(vocab_locations.keys())
for word in sorted(all_words):
    locs = vocab_locations[word]
    if len(locs) > 1:
        # Check if the word is repeated in different phases
        phases_found = set()
        for loc in locs:
            if "episode_0_" in loc:
                phases_found.add("Phase 0")
            elif "episode_1_" in loc:
                phases_found.add("Phase 1")
            elif "episode_2_" in loc:
                phases_found.add("Phase 2")
            elif "episode_3_" in loc:
                phases_found.add("Phase 3")
        if len(phases_found) > 1:
            print(f"Duplicate word '{word}' found in multiple phases: {list(phases_found)} at {locs}")
            has_duplicates = True

if not has_duplicates:
    print("No duplicate vocabulary found across phases!")
