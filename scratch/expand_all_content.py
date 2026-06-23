import json
import os
import glob
import random

# Base output directory
OUTPUT_DIR = "app/src/main/assets/nodes"

# A massive pool of generic vocabulary to draw from if a node is empty
MASTER_VOCAB = [
    ("I", "मैं", "main", "Tôi"),
    ("You", "आप", "aap", "Bạn"),
    ("He/She", "वह", "vah", "Anh ấy/Cô ấy"),
    ("We", "हम", "ham", "Chúng tôi"),
    ("They", "वे", "ve", "Họ"),
    ("Mother", "माँ", "maa", "Mẹ"),
    ("Father", "पिता", "pita", "Cha"),
    ("Brother", "भाई", "bhaai", "Anh trai"),
    ("Sister", "बहन", "bahan", "Chị gái"),
    ("Water", "पानी", "paani", "Nước"),
    ("Food", "खाना", "khaana", "Thức ăn"),
    ("Tea", "चाय", "chaay", "Trà"),
    ("House", "घर", "ghar", "Nhà"),
    ("Car", "गाड़ी", "gaadi", "Xe hơi"),
    ("Book", "किताब", "kitaab", "Sách"),
    ("Dog", "कुत्ता", "kutta", "Chó"),
    ("Cat", "बिल्ली", "billi", "Mèo"),
    ("Sun", "सूरज", "sooraj", "Mặt trời"),
    ("Moon", "चाँद", "chaand", "Mặt trăng"),
    ("Day", "दिन", "din", "Ngày"),
    ("Night", "रात", "raat", "Đêm"),
    ("Today", "आज", "aaj", "Hôm nay"),
    ("Tomorrow", "कल", "kal", "Ngày mai"),
    ("Good", "अच्छा", "achchha", "Tốt"),
    ("Bad", "बुरा", "bura", "Xấu"),
    ("Big", "बड़ा", "bada", "Lớn"),
    ("Small", "छोटा", "chhota", "Nhỏ"),
    ("Hot", "गर्म", "garm", "Nóng"),
    ("Cold", "ठंडा", "thanda", "Lạnh"),
    ("Happy", "खुश", "khush", "Vui vẻ"),
    ("Sad", "उदास", "udaas", "Buồn"),
    ("Beautiful", "सुंदर", "sundar", "Đẹp"),
    ("To go", "जाना", "jaana", "Đi"),
    ("To come", "आना", "aana", "Đến"),
    ("To eat", "खाना", "khaana", "Ăn"),
    ("To drink", "पीना", "peena", "Uống"),
    ("To sleep", "सोना", "sona", "Ngủ"),
    ("To read", "पढ़ना", "padhna", "Đọc"),
    ("To write", "लिखना", "likhna", "Viết"),
    ("Hello", "नमस्ते", "namaste", "Xin chào"),
    ("Thank you", "धन्यवाद", "dhanyavaad", "Cảm ơn"),
    ("Please", "कृपया", "kripaya", "Làm ơn"),
    ("Sorry", "माफ़ कीजिए", "maaf keejie", "Xin lỗi"),
    ("Yes", "हाँ", "haan", "Vâng"),
    ("No", "नहीं", "nahin", "Không"),
    ("One", "एक", "ek", "Một"),
    ("Two", "दो", "do", "Hai"),
    ("Three", "तीन", "teen", "Ba"),
    ("Red", "लाल", "laal", "Đỏ"),
    ("Blue", "नीला", "neela", "Xanh dương"),
    ("Green", "हरा", "hara", "Xanh lá")
]

MASTER_SENTENCES = [
    ("I am a boy.", "मैं एक लड़का हूँ।", "main ek ladka hoon.", "Tôi là một cậu bé."),
    ("She is a girl.", "वह एक लड़की है।", "vah ek ladki hai.", "Cô ấy là một cô bé."),
    ("This is a book.", "यह एक किताब है।", "yah ek kitaab hai.", "Đây là một cuốn sách."),
    ("I eat an apple.", "मैं सेब खाता हूँ।", "main seb khaata hoon.", "Tôi ăn một quả táo."),
    ("You drink water.", "तुम पानी पीते हो।", "tum paani peete ho.", "Bạn uống nước."),
    ("He is tall.", "वह लंबा है।", "vah lamba hai.", "Anh ấy cao."),
    ("They are playing.", "वे खेल रहे हैं।", "ve khel rahe hain.", "Họ đang chơi."),
    ("The weather is nice.", "मौसम अच्छा है।", "mausam achchha hai.", "Thời tiết đẹp."),
    ("I am fine.", "मैं ठीक हूँ।", "main theek hoon.", "Tôi ổn."),
    ("See you later.", "बाद में मिलते हैं।", "baad mein milte hain.", "Hẹn gặp lại sau."),
    ("What is your name?", "आपका नाम क्या है?", "aapka naam kya hai?", "Tên bạn là gì?"),
    ("Where are you from?", "आप कहाँ से हैं?", "aap kahaan se hain?", "Bạn từ đâu đến?"),
    ("I am from India.", "मैं भारत से हूँ।", "main bhaarat se hoon.", "Tôi đến từ Ấn Độ.")
]

def generate_flashcard(item):
    en, hi, trans, vi = item
    return {
        "type": "FLASHCARD",
        "hindi": hi,
        "transliteration": trans,
        "english": en,
        "vietnamese": vi,
        "audio": hi
    }

def generate_multiple_choice(item, all_items):
    en, hi, trans, vi = item
    wrong_items = random.sample([x for x in all_items if x != item], min(3, len(all_items)-1))
    options_en = [en] + [x[0] for x in wrong_items]
    options_vi = [vi] + [x[3] for x in wrong_items]
    random.shuffle(options_en)
    random.shuffle(options_vi)
    return {
        "type": "MULTIPLE_CHOICE",
        "text": hi,
        "subtext": trans,
        "answer_en": en,
        "options_en": options_en,
        "answer_vi": vi,
        "options_vi": options_vi
    }

def generate_listening(item, all_items):
    en, hi, trans, vi = item
    wrong_items = random.sample([x for x in all_items if x != item], min(3, len(all_items)-1))
    options_en = [en] + [x[0] for x in wrong_items]
    options_vi = [vi] + [x[3] for x in wrong_items]
    random.shuffle(options_en)
    random.shuffle(options_vi)
    return {
        "type": "LISTENING",
        "audio": hi,
        "translation_en": en,
        "options_en": options_en,
        "translation_vi": vi,
        "options_vi": options_vi
    }

def generate_sentence_builder(item):
    en, hi, trans, vi = item
    hindi_words = hi.split(" ")
    if len(hindi_words) < 2:
        return None
    return {
        "type": "SENTENCE_BUILDER",
        "englishSentence": en,
        "hindiWords": hindi_words,
        "correctOrder": hindi_words
    }

all_files = glob.glob(f"{OUTPUT_DIR}/*.json")
for file_path in all_files:
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = json.load(f)
    except:
        content = []
        
    # If the file already has >= 20 items, skip it to avoid messing up well-populated files (like VOWELS or Greetings)
    if len(content) >= 20:
        continue
        
    # Pick 8-12 random vocab words and 3-5 sentences to enrich the node
    num_vocab = random.randint(8, 12)
    num_sentences = random.randint(3, 5)
    
    selected_vocab = random.sample(MASTER_VOCAB, num_vocab)
    selected_sentences = random.sample(MASTER_SENTENCES, num_sentences)
    
    new_exercises = []
    
    # Generate Flashcards
    for item in selected_vocab:
        new_exercises.append(generate_flashcard(item))
        
    # Generate Multiple Choice
    for item in selected_vocab:
        new_exercises.append(generate_multiple_choice(item, MASTER_VOCAB))
        
    # Generate Listening
    for item in random.sample(selected_vocab, num_vocab // 2):
        new_exercises.append(generate_listening(item, MASTER_VOCAB))
        
    # Generate Sentence Builders
    for item in selected_sentences:
        sb = generate_sentence_builder(item)
        if sb:
            new_exercises.append(sb)
            
    # Shuffle the new exercises
    random.shuffle(new_exercises)
    
    # Keep the original content at the beginning (it might be a GrammarRule or CultureTip)
    final_content = content + new_exercises
    
    with open(file_path, 'w', encoding='utf-8') as f:
        json.dump(final_content, f, ensure_ascii=False, indent=2)

print(f"Successfully expanded content in {len(all_files)} node files!")
