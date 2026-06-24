import json
import os

episodes_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(episodes_dir, exist_ok=True)

emerg_1 = [
  {
    "type": "Introduction",
    "title": "Medical Help",
    "description": "Learn essential vocabulary for medical emergencies. Read carefully in English and Vietnamese.",
    "keyPoints": [
      "अस्पताल (aspatāl) = Hospital / Bệnh viện",
      "डॉक्टर (ḍôkṭar) = Doctor / Bác sĩ",
      "मुझे मदद चाहिए। (mujhe madad chāhie.) = I need help. / Tôi cần sự giúp đỡ.",
      "मुझे दर्द है। (mujhe dard hai.) = I have pain. / Tôi bị đau."
    ]
  },
  {
    "type": "TeachRule",
    "title": "Medical Assistance",
    "explanation": "Learn how to ask for a doctor or say you are in pain.",
    "simpleRule": "Remember: अस्पताल means 'Hospital' and डॉक्टर means 'Doctor'."
  },
  {
    "type": "Flashcard",
    "hindi": "अस्पताल",
    "transliteration": "aspatāl",
    "english": "Hospital",
    "vietnamese": "Bệnh viện",
    "audio": "अस्पताल"
  },
  {
    "type": "Flashcard",
    "hindi": "डॉक्टर",
    "transliteration": "ḍôkṭar",
    "english": "Doctor",
    "vietnamese": "Bác sĩ",
    "audio": "डॉक्टर"
  },
  {
    "type": "MultipleChoice",
    "prompt": "Translate 'अस्पताल'",
    "prompt_en": "Choose the correct translation for 'अस्पताल'",
    "prompt_vi": "Chọn bản dịch đúng cho 'अस्पताल'",
    "text": "अस्पताल",
    "subtext": "aspatāl",
    "answer": "Hospital",
    "answer_vi": "Bệnh viện",
    "options": ["Doctor", "Hospital", "Police", "Pharmacy"],
    "options_vi": ["Bác sĩ", "Bệnh viện", "Cảnh sát", "Tiệm thuốc"]
  },
  {
    "type": "Flashcard",
    "hindi": "मुझे मदद चाहिए।",
    "transliteration": "mujhe madad chāhie.",
    "english": "I need help.",
    "vietnamese": "Tôi cần sự giúp đỡ.",
    "audio": "मुझे मदद चाहिए।"
  },
  {
    "type": "Flashcard",
    "hindi": "मुझे दर्द है।",
    "transliteration": "mujhe dard hai.",
    "english": "I have pain.",
    "vietnamese": "Tôi bị đau.",
    "audio": "मुझे दर्द है।"
  },
  {
    "type": "MatchPairs",
    "instruction": "Match the Hindi words with their meanings",
    "pairs": [
      {"hindi": "अस्पताल", "english": "Hospital (Bệnh viện)"},
      {"hindi": "डॉक्टर", "english": "Doctor (Bác sĩ)"},
      {"hindi": "मुझे मदद चाहिए।", "english": "I need help. (Tôi cần sự giúp đỡ.)"},
      {"hindi": "मुझे दर्द है।", "english": "I have pain. (Tôi bị đau.)"}
    ]
  },
  {
    "type": "Listening",
    "audio": "मुझे मदद चाहिए।",
    "translation_en": "I need help.",
    "translation_vi": "Tôi cần sự giúp đỡ.",
    "options_en": ["I have pain.", "I need help.", "Where is the hospital?", "Call a doctor."],
    "options_vi": ["Tôi bị đau.", "Tôi cần sự giúp đỡ.", "Bệnh viện ở đâu?", "Gọi bác sĩ."]
  },
  {
    "type": "SentenceBuilder",
    "englishSentence": "Where is the hospital? / Bệnh viện ở đâu?",
    "hindiWords": ["अस्पताल", "कहाँ", "है", "पुलिस", "दुकान"],
    "correctHindiSentence": "अस्पताल कहाँ है"
  },
  {
    "type": "RevisionSummary",
    "title": "Lesson Summary",
    "takeaways": [
      "You learned critical medical terms: अस्पताल, डॉक्टर.",
      "You learned how to say: 'मुझे मदद चाहिए।' (I need help) and 'मुझे दर्द है।' (I have pain).",
      "You built the sentence: 'अस्पताल कहाँ है' (Where is the hospital?)."
    ]
  }
]

emerg_2 = [
  {
    "type": "Introduction",
    "title": "Reporting Theft",
    "description": "Learn phrases to report theft or loss of items. Read in English and Vietnamese.",
    "keyPoints": [
      "पुलिस (pulis) = Police / Cảnh sát",
      "चोर (chor) = Thief / Kẻ trộm",
      "चोरी (chorī) = Theft / Vụ trộm",
      "मेरा फ़ोन खो गया है। (merā fon kho gayā hai.) = My phone is lost. / Điện thoại của tôi bị mất."
    ]
  },
  {
    "type": "TeachRule",
    "title": "Safety & Security",
    "explanation": "If something is stolen, you need to call the police or report the theft.",
    "simpleRule": "Remember: पुलिस means 'Police' and चोर means 'Thief'."
  },
  {
    "type": "Flashcard",
    "hindi": "पुलिस",
    "transliteration": "pulis",
    "english": "Police",
    "vietnamese": "Cảnh sát",
    "audio": "पुलिस"
  },
  {
    "type": "Flashcard",
    "hindi": "चोर",
    "transliteration": "chor",
    "english": "Thief",
    "vietnamese": "Kẻ trộm",
    "audio": "चोर"
  },
  {
    "type": "MultipleChoice",
    "prompt": "Translate 'पुलिस'",
    "prompt_en": "Choose the correct translation for 'पुलिस'",
    "prompt_vi": "Chọn bản dịch đúng cho 'पुलिस'",
    "text": "पुलिस",
    "subtext": "pulis",
    "answer": "Police",
    "answer_vi": "Cảnh sát",
    "options": ["Thief", "Police", "Hospital", "Embassy"],
    "options_vi": ["Kẻ trộm", "Cảnh sát", "Bệnh viện", "Đại sứ quán"]
  },
  {
    "type": "Flashcard",
    "hindi": "मेरा फ़ोन खो गया है।",
    "transliteration": "merā fon kho gayā hai.",
    "english": "My phone is lost.",
    "vietnamese": "Điện thoại của tôi bị mất.",
    "audio": "मेरा फ़ोन खो गया है।"
  },
  {
    "type": "MatchPairs",
    "instruction": "Match the Hindi words with their meanings",
    "pairs": [
      {"hindi": "पुलिस", "english": "Police (Cảnh sát)"},
      {"hindi": "चोर", "english": "Thief (Kẻ trộm)"},
      {"hindi": "chori", "english": "Theft (Vụ trộm)"},
      {"hindi": "मेरा फ़ोन खो गया है।", "english": "My phone is lost. (Điện thoại của tôi bị mất.)"}
    ]
  },
  {
    "type": "Listening",
    "audio": "पुलिस",
    "translation_en": "Police",
    "translation_vi": "Cảnh sát",
    "options_en": ["Thief", "Police", "Hospital", "Embassy"],
    "options_vi": ["Kẻ trộm", "Cảnh sát", "Bệnh viện", "Đại sứ quán"]
  },
  {
    "type": "SentenceBuilder",
    "englishSentence": "Call the police / Gọi cảnh sát",
    "hindiWords": ["पुलिस", "को", "बुलाओ", "चोर", "यहाँ"],
    "correctHindiSentence": "पुलिस को बुलाओ"
  },
  {
    "type": "RevisionSummary",
    "title": "Lesson Summary",
    "takeaways": [
      "You learned safety terms: पुलिस, चोर.",
      "You learned how to report a lost phone: 'मेरा फ़ोन खो गया है।' (My phone is lost).",
      "You built the sentence: 'पुलिस को बुलाओ' (Call the police)."
    ]
  }
]

emerg_3 = [
  {
    "type": "Introduction",
    "title": "Lost Passport",
    "description": "Learn words to deal with a lost passport and visiting the embassy. Read along in English and Vietnamese.",
    "keyPoints": [
      "पासपोर्ट (pāsporṭ) = Passport / Hộ chiếu",
      "दूतावास (dūtāvās) = Embassy / Đại sứ quán",
      "खो गया (kho gayā) = Lost / Bị mất",
      "मेरा पासपोर्ट खो गया। (merā pāsporṭ kho gayā.) = I lost my passport. / Tôi bị mất hộ chiếu."
    ]
  },
  {
    "type": "TeachRule",
    "title": "Embassy & Passport Support",
    "explanation": "If you lose your passport, you must locate the embassy.",
    "simpleRule": "Remember: दूतावास means 'Embassy' and पासपोर्ट means 'Passport'."
  },
  {
    "type": "Flashcard",
    "hindi": "पासपोर्ट",
    "transliteration": "pāsporṭ",
    "english": "Passport",
    "vietnamese": "Hộ chiếu",
    "audio": "पासपोर्ट"
  },
  {
    "type": "Flashcard",
    "hindi": "दूतावास",
    "transliteration": "dūtāvās",
    "english": "Embassy",
    "vietnamese": "Đại sứ quán",
    "audio": "दूतावास"
  },
  {
    "type": "MultipleChoice",
    "prompt": "Translate 'दूतावास'",
    "prompt_en": "Choose the correct translation for 'दूतावास'",
    "prompt_vi": "Chọn bản dịch đúng cho 'दूतावास'",
    "text": "दूतावास",
    "subtext": "dūtāvās",
    "answer": "Embassy",
    "answer_vi": "Đại sứ quán",
    "options": ["Embassy", "Hospital", "Police", "Pharmacy"],
    "options_vi": ["Đại sứ quán", "Bệnh viện", "Cảnh sát", "Tiệm thuốc"]
  },
  {
    "type": "Flashcard",
    "hindi": "मेरा पासपोर्ट खो गया।",
    "transliteration": "merā pāsporṭ kho gayā.",
    "english": "I lost my passport.",
    "vietnamese": "Tôi bị mất hộ chiếu.",
    "audio": "मेरा पासपोर्ट खो गया।"
  },
  {
    "type": "MatchPairs",
    "instruction": "Match the Hindi words with their meanings",
    "pairs": [
      {"hindi": "पासपोर्ट", "english": "Passport (Hộ chiếu)"},
      {"hindi": "दूतावास", "english": "Embassy (Đại sứ quán)"},
      {"hindi": "खो गया", "english": "Lost (Bị mất)"},
      {"hindi": "मेरा पासपोर्ट खो गया।", "english": "I lost my passport. (Tôi bị mất hộ chiếu.)"}
    ]
  },
  {
    "type": "Listening",
    "audio": "मेरा पासपोर्ट खो गया।",
    "translation_en": "I lost my passport.",
    "translation_vi": "Tôi bị mất hộ chiếu.",
    "options_en": ["I lost my phone.", "I lost my passport.", "Where is the embassy?", "Call the police."],
    "options_vi": ["Tôi bị mất điện thoại.", "Tôi bị mất hộ chiếu.", "Đại sứ quán ở đâu?", "Gọi cảnh sát."]
  },
  {
    "type": "SentenceBuilder",
    "englishSentence": "Where is the embassy? / Đại sứ quán ở đâu?",
    "hindiWords": ["दूतावास", "कहाँ", "है", "स्टेशन", "यहाँ"],
    "correctHindiSentence": "दूतावास कहाँ hai"
  },
  {
    "type": "RevisionSummary",
    "title": "Lesson Summary",
    "takeaways": [
      "You learned important terms: पासपोर्ट, दूतावास.",
      "You practiced stating that your passport is lost: 'मेरा पासपोर्ट खो गया।'.",
      "You constructed the phrase: 'दूतावास कहाँ है' (Where is the embassy?)."
    ]
  }
]

emerg_4 = [
  {
    "type": "Introduction",
    "title": "Pharmacy Terms",
    "description": "Learn words related to pharmacy, medicine, and common sicknesses like a fever.",
    "keyPoints": [
      "दवाई (davāī) = Medicine / Thuốc",
      "दवाखाना (davākhānā) = Pharmacy / Tiệm thuốc",
      "बुखार (bukhār) = Fever / Sốt",
      "मुझे बुखार है। (mujhe bukhār hai.) = I have a fever. / Tôi bị sốt."
    ]
  },
  {
    "type": "TeachRule",
    "title": "Visiting a Pharmacy",
    "explanation": "Use 'dawai' to request medicine at a pharmacy ('davakhana').",
    "simpleRule": "Remember: दवाई means 'Medicine' and दवाखाना means 'Pharmacy'."
  },
  {
    "type": "Flashcard",
    "hindi": "दवाई",
    "transliteration": "davāī",
    "english": "Medicine",
    "vietnamese": "Thuốc",
    "audio": "दवाई"
  },
  {
    "type": "Flashcard",
    "hindi": "दवाखाना",
    "transliteration": "davākhānā",
    "english": "Pharmacy",
    "vietnamese": "Tiệm thuốc",
    "audio": "दवाखाना"
  },
  {
    "type": "MultipleChoice",
    "prompt": "Translate 'दवाई'",
    "prompt_en": "Choose the correct translation for 'दवाई'",
    "prompt_vi": "Chọn bản dịch đúng cho 'दवाई'",
    "text": "दवाई",
    "subtext": "davāī",
    "answer": "Medicine",
    "answer_vi": "Thuốc",
    "options": ["Medicine", "Hospital", "Doctor", "Embassy"],
    "options_vi": ["Thuốc", "Bệnh viện", "Bác sĩ", "Đại sứ quán"]
  },
  {
    "type": "Flashcard",
    "hindi": "मुझे बुखार है।",
    "transliteration": "mujhe bukhār hai.",
    "english": "I have a fever.",
    "vietnamese": "Tôi bị sốt.",
    "audio": "मुझे बुखार है।"
  },
  {
    "type": "MatchPairs",
    "instruction": "Match the Hindi words with their meanings",
    "pairs": [
      {"hindi": "दवाई", "english": "Medicine (Thuốc)"},
      {"hindi": "दवाखाना", "english": "Pharmacy (Tiệm thuốc)"},
      {"hindi": "बुखार", "english": "Fever (Sốt)"},
      {"hindi": "मुझे बुखार है।", "english": "I have a fever. (Tôi bị sốt.)"}
    ]
  },
  {
    "type": "Listening",
    "audio": "दवाई",
    "translation_en": "Medicine",
    "translation_vi": "Thuốc",
    "options_en": ["Medicine", "Hospital", "Doctor", "Embassy"],
    "options_vi": ["Thuốc", "Bệnh viện", "Bác sĩ", "Đại sứ quán"]
  },
  {
    "type": "SentenceBuilder",
    "englishSentence": "I need medicine / Tôi cần thuốc",
    "hindiWords": ["मुझे", "दवाई", "चाहिए", "पानी", "चाय"],
    "correctHindiSentence": "मुझे दवाई चाहिए"
  },
  {
    "type": "RevisionSummary",
    "title": "Lesson Summary",
    "takeaways": [
      "You learned vocabulary for sickness & cure: दवाई, दवाखाना, बुखार.",
      "You learned to express sickness: 'मुझे बुखार है।' (I have a fever).",
      "You built the phrase: 'मुझे दवाई चाहिए' (I need medicine)."
    ]
  }
]

with open(os.path.join(episodes_dir, "episode_emerg_1.json"), "w", encoding="utf-8") as f:
    json.dump(emerg_1, f, ensure_ascii=False, indent=2)

with open(os.path.join(episodes_dir, "episode_emerg_2.json"), "w", encoding="utf-8") as f:
    json.dump(emerg_2, f, ensure_ascii=False, indent=2)

with open(os.path.join(episodes_dir, "episode_emerg_3.json"), "w", encoding="utf-8") as f:
    json.dump(emerg_3, f, ensure_ascii=False, indent=2)

with open(os.path.join(episodes_dir, "episode_emerg_4.json"), "w", encoding="utf-8") as f:
    json.dump(emerg_4, f, ensure_ascii=False, indent=2)

print("Emergency episodes created successfully!")
