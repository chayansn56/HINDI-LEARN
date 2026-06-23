import json
import os
import random

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(output_dir, exist_ok=True)

def save_episode(id, nodes):
    with open(os.path.join(output_dir, f"{id}.json"), "w", encoding="utf-8") as f:
        json.dump(nodes, f, ensure_ascii=False, indent=2)

def generate_15_node_episode(topic, rule_title, rule_content, vocab_list):
    # Ensure exactly 6 words
    while len(vocab_list) < 6:
        vocab_list.append(vocab_list[0])
    v1, v2, v3, v4, v5, v6 = vocab_list[:6]
    nodes = []
    
    nodes.append({"type": "Introduction", "title": topic, "description": "Let's learn some new concepts. Listen carefully and repeat.", "keyPoints": [f"{v1[0]} = {v1[2]}", f"{v2[0]} = {v2[2]}"]})
    nodes.append({"type": "TeachRule", "title": rule_title, "explanation": rule_content, "simpleRule": f"Remember: {v1[0]} means {v1[2]}."})
    nodes.append({"type": "Flashcard", "hindi": v1[0], "transliteration": v1[1], "english": v1[2], "vietnamese": v1[3]})
    nodes.append({"type": "Flashcard", "hindi": v2[0], "transliteration": v2[1], "english": v2[2], "vietnamese": v2[3]})
    nodes.append({"type": "MultipleChoice", "prompt_en": f"Translate '{v1[2]}'", "prompt_vi": f"Dịch '{v1[3]}'", "text": v1[0], "subtext": "", "answer": v1[2], "options": random.sample([v1[2], v2[2], v3[2], v4[2], v5[2], v6[2]], 4)})
    nodes.append({"type": "Flashcard", "hindi": v3[0], "transliteration": v3[1], "english": v3[2], "vietnamese": v3[3]})
    nodes.append({"type": "Flashcard", "hindi": v4[0], "transliteration": v4[1], "english": v4[2], "vietnamese": v4[3]})
    nodes.append({"type": "MatchPairs", "instruction": "Match the pairs", "pairs": [{"hindi": v1[0], "english": v1[2]}, {"hindi": v2[0], "english": v2[2]}, {"hindi": v3[0], "english": v3[2]}, {"hindi": v4[0], "english": v4[2]}]})
    
    opts = random.sample([v1, v2, v3, v4, v5, v6], 4)
    if v2 not in opts: opts[0] = v2
    random.shuffle(opts)
    nodes.append({"type": "Listening", "audio": v2[0], "translation_en": v2[2], "options_en": [o[2] for o in opts], "translation_vi": v2[3], "options_vi": [o[3] for o in opts]})
    
    nodes.append({"type": "SentenceBuilder", "englishSentence": f"Translate: {v3[2]}", "hindiWords": [v3[0], v1[0], v2[0], "है"], "correctHindiSentence": v3[0]})
    nodes.append({"type": "Flashcard", "hindi": v5[0], "transliteration": v5[1], "english": v5[2], "vietnamese": v5[3]})
    nodes.append({"type": "Flashcard", "hindi": v6[0], "transliteration": v6[1], "english": v6[2], "vietnamese": v6[3]})
    nodes.append({"type": "MatchPairs", "instruction": "Match the pairs", "pairs": [{"hindi": v3[0], "english": v3[2]}, {"hindi": v4[0], "english": v4[2]}, {"hindi": v5[0], "english": v5[2]}, {"hindi": v6[0], "english": v6[2]}]})
    nodes.append({"type": "SentenceBuilder", "englishSentence": f"Translate: {v6[2]}", "hindiWords": [v6[0], v5[0], v1[0], "हूँ"], "correctHindiSentence": v6[0]})
    nodes.append({"type": "RevisionSummary", "title": "Lesson Complete!", "takeaways": [f"{v1[0]} = {v1[2]}", f"{v3[0]} = {v3[2]}", f"{v5[0]} = {v5[2]}"]})
    return nodes

vocab_sets = {
    "pron": [
        ("क", "ka", "Unaspirated Ka", "Ka không bật hơi"), ("ख", "kha", "Aspirated Kha", "Kha bật hơi"), ("ग", "ga", "Unaspirated Ga", "Ga không bật hơi"), ("घ", "gha", "Aspirated Gha", "Gha bật hơi"), 
        ("च", "cha", "Unaspirated Cha", "Cha không bật hơi"), ("छ", "chha", "Aspirated Chha", "Chha bật hơi"), ("ज", "ja", "Unaspirated Ja", "Ja không bật hơi"), ("झ", "jha", "Aspirated Jha", "Jha bật hơi"),
        ("ट", "ta", "Retroflex Ta", "Ta quặt lưỡi"), ("ठ", "tha", "Aspirated Retro Ta", "Tha quặt lưỡi"), ("ड", "da", "Retroflex Da", "Da quặt lưỡi"), ("ढ", "dha", "Aspirated Retro Da", "Dha quặt lưỡi"),
        ("त", "ta", "Dental Ta", "Ta răng"), ("थ", "tha", "Aspirated Dental Ta", "Tha răng"), ("द", "da", "Dental Da", "Da răng"), ("ध", "dha", "Aspirated Dental Da", "Dha răng"),
        ("प", "pa", "Unaspirated Pa", "Pa không bật hơi"), ("फ", "pha", "Aspirated Pha", "Pha bật hơi"), ("ब", "ba", "Unaspirated Ba", "Ba không bật hơi"), ("भ", "bha", "Aspirated Bha", "Bha bật hơi")
    ],
    "speak": [
        ("नमस्ते", "namaste", "Hello", "Xin chào"), ("धन्यवाद", "dhanyavaad", "Thank you", "Cảm ơn"), ("हाँ", "haan", "Yes", "Vâng"), ("नहीं", "nahin", "No", "Không"), 
        ("कृपया", "kripya", "Please", "Làm ơn"), ("माफ़ कीजिए", "maaf kijiye", "Sorry", "Xin lỗi"), ("मेरा नाम", "mera naam", "My name", "Tên tôi"), ("क्या", "kya", "What", "Cái gì"), 
        ("कहाँ", "kahan", "Where", "Ở đâu"), ("पानी", "paani", "Water", "Nước"), ("खाना", "khaana", "Food", "Thức ăn"), ("चाय", "chaay", "Tea", "Trà"),
        ("कैसे", "kaise", "How", "Như thế nào"), ("कौन", "kaun", "Who", "Ai"), ("कब", "kab", "When", "Khi nào"), ("क्यों", "kyon", "Why", "Tại sao"),
        ("अच्छा", "achcha", "Good", "Tốt"), ("बुरा", "bura", "Bad", "Xấu"), ("बड़ा", "bada", "Big", "To"), ("छोटा", "chhota", "Small", "Nhỏ")
    ],
    "gram": [
        ("मैं", "main", "I", "Tôi"), ("हूँ", "hoon", "Am", "Là"), ("लड़का", "ladka", "Boy", "Con trai"), ("लड़की", "ladki", "Girl", "Con gái"), 
        ("तुम", "tum", "You", "Bạn"), ("हो", "ho", "Are", "Là"), ("वह", "vah", "He/She", "Anh ấy/Cô ấy"), ("है", "hai", "Is", "Là"), 
        ("हम", "hum", "We", "Chúng tôi"), ("हैं", "hain", "Are (Plural)", "Là (Số nhiều)"), ("यह", "yah", "This", "Này"), ("ये", "ye", "These", "Những cái này"),
        ("मेरा", "mera", "My (M)", "Của tôi (Nam)"), ("मेरी", "meri", "My (F)", "Của tôi (Nữ)"), ("तुम्हारा", "tumhara", "Your", "Của bạn"), ("उसका", "uska", "His", "Của anh ấy"),
        ("में", "mein", "In", "Trong"), ("पर", "par", "On", "Trên"), ("से", "se", "From", "Từ"), ("को", "ko", "To", "Đến")
    ],
    "listen": [
        ("किताब", "kitaab", "Book", "Sách"), ("कलम", "kalam", "Pen", "Bút"), ("मेज", "mez", "Table", "Bàn"), ("कुर्सी", "kursi", "Chair", "Ghế"), 
        ("घर", "ghar", "House", "Nhà"), ("कमरा", "kamra", "Room", "Phòng"), ("सूरज", "sooraj", "Sun", "Mặt trời"), ("चाँद", "chaand", "Moon", "Mặt trăng"),
        ("पेड़", "ped", "Tree", "Cây"), ("फूल", "phool", "Flower", "Hoa"), ("आसमान", "aasman", "Sky", "Bầu trời"), ("नदी", "nadi", "River", "Sông"),
        ("कुत्ता", "kutta", "Dog", "Chó"), ("बिल्ली", "billi", "Cat", "Mèo"), ("गाय", "gaay", "Cow", "Bò"), ("हाथी", "hathi", "Elephant", "Voi"),
        ("लाल", "laal", "Red", "Đỏ"), ("नीला", "neela", "Blue", "Xanh dương"), ("हरा", "hara", "Green", "Xanh lá"), ("पीला", "peela", "Yellow", "Vàng")
    ],
    "write": [
        ("अ", "a", "A", "A"), ("आ", "aa", "AA", "AA"), ("इ", "i", "I", "I"), ("ई", "ii", "II", "II"), ("उ", "u", "U", "U"), ("ऊ", "uu", "UU", "UU"),
        ("ए", "e", "E", "E"), ("ऐ", "ai", "AI", "AI"), ("ओ", "o", "O", "O"), ("औ", "au", "AU", "AU"), ("१", "ek", "1", "1"), ("२", "do", "2", "2"),
        ("३", "teen", "3", "3"), ("४", "chaar", "4", "4"), ("५", "paanch", "5", "5"), ("६", "chhah", "6", "6"), ("७", "saat", "7", "7"), ("८", "aath", "8", "8"),
        ("९", "nau", "9", "9"), ("१०", "das", "10", "10")
    ],
    "culture": [
        ("दिवाली", "Diwali", "Festival of Lights", "Lễ hội ánh sáng"), ("होली", "Holi", "Festival of Colors", "Lễ hội sắc màu"), ("नमस्ते", "Namaste", "Greeting", "Chào"), ("साड़ी", "Saree", "Dress", "Váy"), 
        ("मंदिर", "Mandir", "Temple", "Đền"), ("पूजा", "Pooja", "Prayer", "Cầu nguyện"), ("योग", "Yoga", "Yoga", "Yoga"), ("ध्यान", "Dhyan", "Meditation", "Thiền"),
        ("मिठाई", "Mithai", "Sweets", "Đồ ngọt"), ("रोटी", "Roti", "Bread", "Bánh mì"), ("दाल", "Daal", "Lentils", "Đậu lăng"), ("चाय", "Chaay", "Tea", "Trà"),
        ("मेहंदी", "Mehndi", "Henna", "Henna"), ("शादी", "Shaadi", "Wedding", "Đám cưới"), ("भगवान", "Bhagwan", "God", "Thần linh"), ("प्रसाद", "Prasad", "Offering", "Đồ cúng"),
        ("कुर्ता", "Kurta", "Shirt", "Áo"), ("बिंदी", "Bindi", "Forehead dot", "Chấm trán"), ("आरती", "Aarti", "Ritual", "Nghi lễ"), ("संस्कृति", "Sanskriti", "Culture", "Văn hóa")
    ],
    "travel": [
        ("हवाई अड्डा", "Hawai Adda", "Airport", "Sân bay"), ("टैक्सी", "Taxi", "Taxi", "Taxi"), ("होटल", "Hotel", "Hotel", "Khách sạn"), ("टिकट", "Ticket", "Ticket", "Vé"), 
        ("पैसा", "Paisa", "Money", "Tiền"), ("रास्ता", "Raasta", "Way", "Đường"), ("ट्रेन", "Train", "Train", "Tàu hỏa"), ("बस", "Bus", "Bus", "Xe buýt"),
        ("सामान", "Saamaan", "Luggage", "Hành lý"), ("पासपोर्ट", "Passport", "Passport", "Hộ chiếu"), ("वीज़ा", "Visa", "Visa", "Visa"), ("कमरा", "Kamra", "Room", "Phòng"),
        ("दाएं", "Daen", "Right", "Phải"), ("बाएं", "Baen", "Left", "Trái"), ("सीधे", "Seedhe", "Straight", "Thẳng"), ("नक्शा", "Naksha", "Map", "Bản đồ"),
        ("कितना", "Kitna", "How much", "Bao nhiêu"), ("महंगा", "Mahanga", "Expensive", "Đắt"), ("सस्ता", "Sasta", "Cheap", "Rẻ"), ("मदद", "Madad", "Help", "Giúp đỡ")
    ],
    "business": [
        ("बैठक", "Baithak", "Meeting", "Cuộc họp"), ("काम", "Kaam", "Work", "Công việc"), ("कार्यालय", "Karyalay", "Office", "Văn phòng"), ("बॉस", "Boss", "Boss", "Sếp"), 
        ("समय", "Samay", "Time", "Thời gian"), ("समझौता", "Samjhauta", "Agreement", "Thỏa thuận"), ("हस्ताक्षर", "Hastakshar", "Signature", "Chữ ký"), ("कागज़", "Kaagaz", "Paper", "Giấy"),
        ("प्रोजेक्ट", "Project", "Project", "Dự án"), ("प्रेजेंटेशन", "Presentation", "Presentation", "Thuyết trình"), ("मेहनत", "Mehnat", "Hard work", "Chăm chỉ"), ("छुट्टी", "Chhutti", "Leave", "Nghỉ phép"),
        ("तनख्वाह", "Tankhwah", "Salary", "Lương"), ("कर्मचारी", "Karmchari", "Employee", "Nhân viên"), ("मीटिंग", "Meeting", "Meeting", "Họp"), ("ईमेल", "Email", "Email", "Email"),
        ("कॉल", "Call", "Call", "Gọi"), ("सफल", "Safal", "Successful", "Thành công"), ("नुकसान", "Nuksaan", "Loss", "Lỗ"), ("फायदा", "Fayda", "Profit", "Lãi")
    ],
    "bollywood": [
        ("दोस्त", "Dost", "Friend", "Bạn bè"), ("प्यार", "Pyaar", "Love", "Tình yêu"), ("जिंदगी", "Zindagi", "Life", "Cuộc sống"), ("दिल", "Dil", "Heart", "Trái tim"), 
        ("गाना", "Gaana", "Song", "Bài hát"), ("हीरो", "Hero", "Hero", "Anh hùng"), ("हीरोइन", "Heroine", "Heroine", "Nữ anh hùng"), ("खलनायक", "Khalnayak", "Villain", "Ác nhân"),
        ("डांस", "Dance", "Dance", "Nhảy"), ("फिल्म", "Film", "Film", "Phim"), ("ड्रामा", "Drama", "Drama", "Kịch"), ("एक्शन", "Action", "Action", "Hành động"),
        ("खुशी", "Khushi", "Happiness", "Hạnh phúc"), ("गम", "Gham", "Sorrow", "Nỗi buồn"), ("रोमांस", "Romance", "Romance", "Lãng mạn"), ("कहानी", "Kahani", "Story", "Câu chuyện"),
        ("सुंदर", "Sundar", "Beautiful", "Đẹp"), ("पागल", "Pagal", "Crazy", "Điên"), ("दीवाना", "Deewana", "Crazy in love", "Si tình"), ("तारा", "Taara", "Star", "Ngôi sao")
    ],
    "whatsapp": [
        ("यार", "Yaar", "Buddy", "Bạn thân"), ("भाई", "Bhai", "Bro", "Anh em"), ("क्या हाल है", "Kya haal hai", "How are you", "Bạn khỏe không"), ("ठीक है", "Theek hai", "Okay", "Được rồi"), 
        ("सीन क्या है", "Scene kya hai", "What's the plan", "Kế hoạch là gì"), ("जुगाड़", "Jugaad", "Hack", "Giải pháp"), ("चिल मार", "Chill maar", "Relax", "Thư giãn"), ("मजाक", "Mazaak", "Joke", "Đùa"),
        ("पक्का", "Pakka", "For sure", "Chắc chắn"), ("सही", "Sahi", "Right/Cool", "Đúng/Ngầu"), ("गजब", "Gazab", "Awesome", "Tuyệt vời"), ("बकवास", "Bakwaas", "Nonsense", "Vớ vẩn"),
        ("ऑनलाइन", "Online", "Online", "Trực tuyến"), ("मैसेज", "Message", "Message", "Tin nhắn"), ("ग्रुप", "Group", "Group", "Nhóm"), ("स्टेटस", "Status", "Status", "Trạng thái"),
        ("प्रोफाइल", "Profile", "Profile", "Hồ sơ"), ("डीपी", "DP", "DP", "Ảnh đại diện"), ("कॉल कर", "Call kar", "Call me", "Gọi tôi"), ("बाद में", "Baad mein", "Later", "Sau")
    ]
}

module_counts = {
    "pron": 8, "speak": 10, "gram": 12, "listen": 6, "write": 8,
    "culture": 6, "travel": 8, "business": 6, "bollywood": 6, "whatsapp": 4
}

for mod, count in module_counts.items():
    vocab_pool = vocab_sets.get(mod, vocab_sets["speak"])
    for i in range(1, count + 1):
        # Sample 6 completely random unique words from the 20-word pool
        vocab = random.sample(vocab_pool, 6)
        episode_data = generate_15_node_episode(f"{mod.capitalize()} Lesson {i}", "Core Concept", "Notice the patterns in these words.", vocab)
        save_episode(f"episode_{mod}_{i}", episode_data)

found_vocab_pool = vocab_sets["write"] + vocab_sets["pron"]
for i in list(range(19, 26)) + list(range(28, 31)):
    vocab = random.sample(found_vocab_pool, 6)
    episode_data = generate_15_node_episode(f"Foundation {i}", "Vowels & Consonants", "Learn how vowels attach to consonants.", vocab)
    save_episode(f"episode_0_{i}", episode_data)

print("SUCCESS: Pedagogical curriculum generated with randomized combinations.")
