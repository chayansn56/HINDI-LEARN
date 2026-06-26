# -*- coding: utf-8 -*-
import json
import os

base_path = '/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes'

# Fix episode_2_11.json: Replace doctor with patient
file_11 = os.path.join(base_path, 'episode_2_11.json')
if os.path.exists(file_11):
    with open(file_11, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Do replacements
    content = content.replace('"डॉक्टर"', '"मरीज़"')
    content = content.replace('"Doctor"', '"Patient"')
    content = content.replace('"Bác sĩ"', '"Bệnh nhân"')
    content = content.replace('"doctor"', '"mareez"')
    
    with open(file_11, 'w', encoding='utf-8') as f:
        f.write(content)
    print("Fixed duplicates in episode_2_11.json")

# Fix episode_2_15.json: Replace success with progress
file_15 = os.path.join(base_path, 'episode_2_15.json')
if os.path.exists(file_15):
    with open(file_15, 'r', encoding='utf-8') as f:
        content = f.read()
        
    content = content.replace('"सफलता"', '"प्रगति"')
    content = content.replace('"Success"', '"Progress"')
    content = content.replace('"Sự thành công"', '"Tiến trình"')
    content = content.replace('"safalta"', '"pragati"')
    # Also update sentence builder text in episode_2_15
    content = content.replace('"सफलता पर"', '"प्रगति पर"')
    content = content.replace('"correctHindiSentence": "आपकी सफलता पर बधाई"', '"correctHindiSentence": "आपकी प्रगति पर बधाई"')
    content = content.replace('"correctOrder": "आपकी सफलता पर बधाई"', '"correctOrder": "आपकी प्रगति पर बधाई"')
    content = content.replace('"hindiWords": ["बधाई", "सफलता", "पर", "आपकी"]', '"hindiWords": ["बधाई", "प्रगति", "पर", "आपकी"]')
    content = content.replace('"eng": "Congratulations on your success."', '"eng": "Congratulations on your progress."')
    content = content.replace('"englishSentence": "Congratulations on your success."', '"englishSentence": "Congratulations on your progress."')
    
    with open(file_15, 'w', encoding='utf-8') as f:
        f.write(content)
    print("Fixed duplicates in episode_2_15.json")
