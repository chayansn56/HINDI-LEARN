import json
import os
import random

# Base output directory
OUTPUT_DIR = "app/src/main/assets/nodes"
if not os.path.exists(OUTPUT_DIR):
    os.makedirs(OUTPUT_DIR)

# Content dictionary containing words and phrases
CONTENT = {
    "node_3": { # PRONOUNS
        "prefix": "grammar",
        "items": [
            ("I", "मैं", "main", "Tôi"),
            ("You (formal)", "आप", "aap", "Bạn (Trịnh trọng)"),
            ("You (informal)", "तुम", "tum", "Bạn (Thân mật)"),
            ("He/She/It (close)", "यह", "yah", "Anh ấy/Cô ấy/Nó (gần)"),
            ("He/She/It (far)", "वह", "vah", "Anh ấy/Cô ấy/Nó (xa)"),
            ("We", "हम", "ham", "Chúng tôi"),
            ("They", "वे", "ve", "Họ"),
            ("My", "मेरा", "mera", "Của tôi"),
            ("Your (formal)", "आपका", "aapka", "Của bạn"),
            ("His/Her", "उसका", "uska", "Của anh ấy/cô ấy"),
            ("Our", "हमारा", "hamara", "Của chúng tôi"),
            ("Their", "उनका", "unka", "Của họ")
        ]
    },
    "node_4": { # FAMILY
        "prefix": "vocab",
        "items": [
            ("Family", "परिवार", "parivaar", "Gia đình"),
            ("Mother", "माँ", "maa", "Mẹ"),
            ("Father", "पिता", "pita", "Cha"),
            ("Brother", "भाई", "bhaai", "Anh/Em trai"),
            ("Sister", "बहन", "bahan", "Chị/Em gái"),
            ("Grandfather", "दादा", "daada", "Ông nội"),
            ("Grandmother", "दादी", "daadi", "Bà nội"),
            ("Son", "बेटा", "beta", "Con trai"),
            ("Daughter", "बेटी", "beti", "Con gái"),
            ("Uncle", "चाचा", "chaacha", "Chú"),
            ("Aunt", "चाची", "chaachi", "Dì")
        ]
    },
    "node_5": { # FOOD
        "prefix": "vocab",
        "items": [
            ("Food", "खाना", "khaana", "Thức ăn"),
            ("Water", "पानी", "paani", "Nước"),
            ("Milk", "दूध", "doodh", "Sữa"),
            ("Tea", "चाय", "chaay", "Trà"),
            ("Rice", "चावल", "chaaval", "Gạo/Cơm"),
            ("Bread", "रोटी", "roti", "Bánh mì"),
            ("Apple", "सेब", "seb", "Quả táo"),
            ("Banana", "केला", "kela", "Quả chuối"),
            ("Meat", "मांस", "maans", "Thịt"),
            ("Chicken", "मुर्गी", "murgi", "Gà"),
            ("Fish", "मछली", "machhli", "Cá"),
            ("Vegetable", "सब्जी", "sabzi", "Rau"),
            ("Fruit", "फल", "phal", "Trái cây"),
            ("Sugar", "चीनी", "cheeni", "Đường"),
            ("Salt", "नमक", "namak", "Muối")
        ]
    },
    "node_6": { # GREETINGS
        "prefix": "vocab",
        "items": [
            ("Hello / Greetings", "नमस्ते", "namaste", "Xin chào"),
            ("Good Morning", "सुप्रभात", "suprabhaat", "Chào buổi sáng"),
            ("Good Night", "शुभ रात्रि", "shubh raatri", "Chúc ngủ ngon"),
            ("How are you?", "आप कैसे हैं?", "aap kaise hain?", "Bạn có khỏe không?"),
            ("I am fine.", "मैं ठीक हूँ।", "main theek hoon.", "Tôi ổn."),
            ("Thank you.", "धन्यवाद।", "dhanyavaad.", "Cảm ơn bạn."),
            ("You're welcome.", "आपका स्वागत है।", "aapka svaagat hai.", "Không có chi."),
            ("See you later.", "बाद में मिलते हैं।", "baad mein milte hain.", "Hẹn gặp lại sau."),
            ("Nice to meet you.", "आपसे मिलकर अच्छा लगा।", "aapse milkar achchha laga.", "Rất vui được gặp bạn."),
            ("Have a good day.", "आपका दिन शुभ हो।", "aapka din shubh ho.", "Chúc một ngày tốt lành."),
            ("Goodbye.", "अलविदा।", "alvida.", "Tạm biệt."),
            ("Take care.", "अपना ख्याल रखना।", "apna khyaal rakhna.", "Bảo trọng."),
            ("What's your name?", "आपका नाम क्या है?", "aapka naam kya hai?", "Tên bạn là gì?"),
            ("My name is Rahul.", "मेरा नाम राहुल है।", "mera naam rahul hai.", "Tên tôi là Rahul."),
            ("Where are you from?", "आप कहाँ से हैं?", "aap kahaan se hain?", "Bạn từ đâu đến?"),
            ("I am from India.", "मैं भारत से हूँ।", "main bhaarat se hoon.", "Tôi đến từ Ấn Độ."),
            ("Please.", "कृपया।", "kripaya.", "Làm ơn."),
            ("Excuse me.", "क्षमा करें।", "kshama karen.", "Xin lỗi."),
            ("Sorry.", "माफ़ कीजिए।", "maaf keejie.", "Xin lỗi.")
        ]
    },
    "node_7": { # NUMBERS
        "prefix": "vocab",
        "items": [
            ("One", "एक", "ek", "Một"),
            ("Two", "दो", "do", "Hai"),
            ("Three", "तीन", "teen", "Ba"),
            ("Four", "चार", "chaar", "Bốn"),
            ("Five", "पाँच", "paanch", "Năm"),
            ("Six", "छह", "chhah", "Sáu"),
            ("Seven", "सात", "saat", "Bảy"),
            ("Eight", "आठ", "aath", "Tám"),
            ("Nine", "नौ", "nau", "Chín"),
            ("Ten", "दस", "das", "Mười"),
            ("Twenty", "बीस", "bees", "Hai mươi"),
            ("Hundred", "सौ", "sau", "Một trăm")
        ]
    },
    "node_8": { # COLORS
        "prefix": "vocab",
        "items": [
            ("Color", "रंग", "rang", "Màu sắc"),
            ("Red", "लाल", "laal", "Màu đỏ"),
            ("Blue", "नीला", "neela", "Màu xanh dương"),
            ("Green", "हरा", "hara", "Màu xanh lá"),
            ("Yellow", "पीला", "peela", "Màu vàng"),
            ("Black", "काला", "kaala", "Màu đen"),
            ("White", "सफेद", "safed", "Màu trắng"),
            ("Pink", "गुलाबी", "gulaabi", "Màu hồng"),
            ("Orange", "नारंगी", "naarangi", "Màu cam"),
            ("Brown", "भूरा", "bhoora", "Màu nâu")
        ]
    },
    "node_9": { # BODY_PARTS
        "prefix": "vocab",
        "items": [
            ("Head", "सिर", "sir", "Đầu"),
            ("Eye", "आँख", "aankh", "Mắt"),
            ("Ear", "कान", "kaan", "Tai"),
            ("Nose", "नाक", "naak", "Mũi"),
            ("Mouth", "मुँह", "munh", "Miệng"),
            ("Hand", "हाथ", "haath", "Bàn tay"),
            ("Leg", "पैर", "pair", "Chân"),
            ("Foot", "पाँव", "paanv", "Bàn chân"),
            ("Hair", "बाल", "baal", "Tóc"),
            ("Face", "चेहरा", "chehra", "Khuôn mặt")
        ]
    },
    "node_10": { # ANIMALS
        "prefix": "vocab",
        "items": [
            ("Animal", "जानवर", "jaanvar", "Động vật"),
            ("Dog", "कुत्ता", "kutta", "Chó"),
            ("Cat", "बिल्ली", "billi", "Mèo"),
            ("Cow", "गाय", "gaay", "Bò"),
            ("Horse", "घोड़ा", "ghoda", "Ngựa"),
            ("Bird", "पक्षी", "pakshi", "Chim"),
            ("Tiger", "बाघ", "baagh", "Hổ"),
            ("Lion", "शेर", "sher", "Sư tử"),
            ("Elephant", "हाथी", "haathi", "Voi"),
            ("Monkey", "बंदर", "bandar", "Khỉ")
        ]
    },
    "node_11": { # NATURE
        "prefix": "vocab",
        "items": [
            ("Sun", "सूरज", "sooraj", "Mặt trời"),
            ("Moon", "चाँद", "chaand", "Mặt trăng"),
            ("Star", "तारा", "taara", "Ngôi sao"),
            ("Sky", "आसमान", "aasman", "Bầu trời"),
            ("Rain", "बारिश", "baarish", "Mưa"),
            ("Wind", "हवा", "havaa", "Gió"),
            ("Tree", "पेड़", "ped", "Cây"),
            ("Flower", "फूल", "phool", "Hoa"),
            ("River", "नदी", "nadi", "Sông"),
            ("Mountain", "पहाड़", "pahaad", "Núi")
        ]
    },
    "node_12": { # VERBS_1
        "prefix": "grammar",
        "items": [
            ("To go", "जाना", "jaana", "Đi"),
            ("To come", "आना", "aana", "Đến"),
            ("To eat", "खाना", "khaana", "Ăn"),
            ("To drink", "पीना", "peena", "Uống"),
            ("To sleep", "सोना", "sona", "Ngủ"),
            ("To wake up", "उठना", "uthna", "Thức dậy"),
            ("To sit", "बैठना", "baithna", "Ngồi"),
            ("To stand", "खड़े होना", "khade hona", "Đứng")
        ]
    },
    "node_13": { # VERBS_2
        "prefix": "grammar",
        "items": [
            ("To read", "पढ़ना", "padhna", "Đọc"),
            ("To write", "लिखना", "likhna", "Viết"),
            ("To speak", "बोलना", "bolna", "Nói"),
            ("To listen", "सुनना", "sunna", "Nghe"),
            ("To run", "दौड़ना", "daudna", "Chạy"),
            ("To walk", "चलना", "chalna", "Đi bộ"),
            ("To play", "खेलना", "khelna", "Chơi"),
            ("To work", "काम करना", "kaam karna", "Làm việc")
        ]
    },
    "node_14": { # ADJECTIVES
        "prefix": "grammar",
        "items": [
            ("Big", "बड़ा", "bada", "Lớn"),
            ("Small", "छोटा", "chhota", "Nhỏ"),
            ("Good", "अच्छा", "achchha", "Tốt"),
            ("Bad", "बुरा", "bura", "Xấu"),
            ("Hot", "गर्म", "garm", "Nóng"),
            ("Cold", "ठंडा", "thanda", "Lạnh"),
            ("Happy", "खुश", "khush", "Vui vẻ"),
            ("Sad", "उदास", "udaas", "Buồn"),
            ("Beautiful", "सुंदर", "sundar", "Đẹp"),
            ("Ugly", "बदसूरत", "badsurat", "Xấu xí")
        ]
    },
    "node_15": { # BASIC_SENTENCES
        "prefix": "grammar",
        "items": [
            ("I am a boy.", "मैं एक लड़का हूँ।", "main ek ladka hoon.", "Tôi là một cậu bé."),
            ("She is a girl.", "वह एक लड़की है।", "vah ek ladki hai.", "Cô ấy là một cô bé."),
            ("This is a book.", "यह एक किताब है।", "yah ek kitaab hai.", "Đây là một cuốn sách."),
            ("I eat an apple.", "मैं सेब खाता हूँ।", "main seb khaata hoon.", "Tôi ăn một quả táo."),
            ("You drink water.", "तुम पानी पीते हो।", "tum paani peete ho.", "Bạn uống nước."),
            ("He is tall.", "वह लंबा है।", "vah lamba hai.", "Anh ấy cao."),
            ("They are playing.", "वे खेल रहे हैं।", "ve khel rahe hain.", "Họ đang chơi.")
        ]
    },
    "node_16": { # ADVANCED_SENTENCES
        "prefix": "grammar",
        "items": [
            ("If it rains, I will stay home.", "अगर बारिश हुई, तो मैं घर पर रहूँगा।", "agar baarish hui, to main ghar par rahoonga.", "Nếu trời mưa, tôi sẽ ở nhà."),
            ("I want to learn Hindi because it is beautiful.", "मैं हिंदी सीखना चाहता हूँ क्योंकि यह सुंदर है।", "main hindi seekhna chaahta hoon kyonki yah sundar hai.", "Tôi muốn học tiếng Hindi vì nó rất đẹp."),
            ("He said that he will come tomorrow.", "उसने कहा कि वह कल आएगा।", "usne kaha ki vah kal aaega.", "Anh ấy nói rằng anh ấy sẽ đến vào ngày mai."),
            ("She was reading a book when I arrived.", "जब मैं पहुँचा, वह किताब पढ़ रही थी।", "jab main pahuncha, vah kitaab padh rahi thi.", "Cô ấy đang đọc sách khi tôi đến."),
            ("We have been living here for five years.", "हम यहाँ पाँच साल से रह रहे हैं।", "ham yahaan paanch saal se rah rahe hain.", "Chúng tôi đã sống ở đây được năm năm.")
        ]
    },
    "node_17": { # LISTENING
        "prefix": "vocab",
        "items": [
            ("Hello", "नमस्ते", "namaste", "Xin chào"),
            ("Where are you?", "आप कहाँ हैं?", "aap kahaan hain?", "Bạn đang ở đâu?"),
            ("I am eating.", "मैं खा रहा हूँ।", "main kha raha hoon.", "Tôi đang ăn."),
            ("The weather is nice.", "मौसम अच्छा है।", "mausam achchha hai.", "Thời tiết đẹp."),
            ("Look there.", "वहाँ देखो।", "vahaan dekho.", "Nhìn kìa.")
        ]
    },
    "node_18": { # LISTENING ADVANCED
        "prefix": "vocab",
        "items": [
            ("I don't know.", "मुझे नहीं पता।", "mujhe nahin pata.", "Tôi không biết."),
            ("What is happening?", "क्या हो रहा है?", "kya ho raha hai?", "Có chuyện gì đang xảy ra vậy?"),
            ("Let's go.", "चलो चलते हैं।", "chalo chalte hain.", "Đi thôi."),
            ("It is very expensive.", "यह बहुत महँगा है।", "yah bahut mahanga hai.", "Nó rất đắt."),
            ("I need help.", "मुझे मदद चाहिए।", "mujhe madad chaahie.", "Tôi cần giúp đỡ.")
        ]
    }
}

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
    
    # Pick 3 wrong answers
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
        return None # Only for sentences
        
    return {
        "type": "SENTENCE_BUILDER",
        "englishSentence": en,
        "hindiWords": hindi_words,
        "correctOrder": hindi_words
    }

for node_id, data in CONTENT.items():
    prefix = data["prefix"]
    items = data["items"]
    
    filename = f"{OUTPUT_DIR}/{prefix}_{node_id}.json"
    
    exercises = []
    
    # 1. Flashcards for all items (Introduction)
    for item in items:
        exercises.append(generate_flashcard(item))
        
    # 2. Multiple Choice for all items (Recognition)
    for item in items:
        exercises.append(generate_multiple_choice(item, items))
        
    # 3. Listening for half the items (Audio comprehension)
    for item in random.sample(items, min(len(items), max(1, len(items)//2))):
        exercises.append(generate_listening(item, items))
        
    # 4. Sentence Builder if applicable
    for item in items:
        sb = generate_sentence_builder(item)
        if sb:
            exercises.append(sb)
            
    # Shuffle from multiple choice onwards to mix it up
    rest = exercises[len(items):]
    random.shuffle(rest)
    
    final_exercises = exercises[:len(items)] + rest
    
    with open(filename, 'w', encoding='utf-8') as f:
        json.dump(final_exercises, f, ensure_ascii=False, indent=2)
        
print("Content successfully generated!")
