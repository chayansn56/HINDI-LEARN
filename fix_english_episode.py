import json
import re

def fix_episode(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
        
    hindi_to_vi = {}
    hindi_to_en = {}
    
    # First pass: build translation dictionary from Flashcards
    for ex in data:
        if ex.get("type") == "Flashcard":
            h_word = ex.get("english", "").strip() # This contains the Hindi word e.g. "पैसा"
            v_word = ex.get("vietnamese", "").strip() # This contains "Tiền"
            en_word = ex.get("hindi", "").strip() # This contains "Money"
            
            if h_word:
                hindi_to_vi[h_word] = v_word
                hindi_to_en[h_word] = en_word

    # Second pass: fix exercises
    for ex in data:
        if ex.get("type") == "Flashcard":
            # Fix Flashcard: map 'hindi' to English word, 'english' to Vietnamese word
            # so the UI shows English on front, Vietnamese on back.
            # But wait, LessonExercises expects: front = hindi, back = english, transliteration
            ex["hindi"] = ex["hindi"] # keep English word
            ex["english"] = ex.get("vietnamese", "") # put Vietnamese word in english slot
            ex["transliteration"] = "" 
            ex["vietnamese"] = "" 

        elif ex.get("type") in ["MultipleChoice", "MULTIPLE_CHOICE"]:
            # Fix options: translate Hindi options to Vietnamese options
            options = ex.get("options", [])
            new_options_vi = []
            new_options_en = []
            for opt in options:
                opt_clean = opt.strip()
                # Find the mapping (some might have weird zero width spaces so we check inclusion)
                found_vi = opt_clean
                found_en = opt_clean
                for h, v in hindi_to_vi.items():
                    if h in opt_clean:
                        found_vi = v
                        break
                for h, e in hindi_to_en.items():
                    if h in opt_clean:
                        found_en = e
                        break
                new_options_vi.append(found_vi)
                new_options_en.append(found_en)
            
            ex["options_vi"] = new_options_vi
            ex["options"] = new_options_en # Set base options to English words
            
            # Answer is already fine or we can map it
            ans_clean = ex.get("answer", "").strip()
            for h, v in hindi_to_vi.items():
                if h in ans_clean:
                    ex["answer_vi"] = v
                    ex["answer"] = hindi_to_en.get(h, v)
                    break
                    
        elif ex.get("type") == "SentenceBuilder":
            # "englishSentence": "It is a पैसा​​ / Nó là một Tiền"
            eng_sent = ex.get("englishSentence", "")
            if "/" in eng_sent:
                ex["englishSentence"] = eng_sent.split("/")[-1].strip() # just keep "Nó là một Tiền"
            
            # "correctHindiSentence": "It is a Money"
            corr = ex.get("correctHindiSentence", "")
            corr = corr.replace("a Money", "money").replace("a Water", "water").replace("a Time", "time")
            ex["correctHindiSentence"] = corr
            ex["hindiWords"] = corr.split(" ")

    with open(file_path.replace(".json", "_fixed.json"), 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

fix_episode('app/src/main/assets/english_episodes/episode_numbers_lab_9.json')
