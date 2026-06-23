import json
import os

episodes_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(episodes_dir, exist_ok=True)

episodes_data = [
    {
        "id": "episode_1",
        "title": "Welcome to Delhi",
        "story_en": "John steps out of the airport in Delhi. The air is warm and chaotic. He is terrified. He needs to find a taxi.",
        "story_vi": "Lan bước ra khỏi sân bay ở Delhi. Không khí ấm áp và hỗn loạn. Cô ấy rất sợ hãi. Cô ấy cần tìm một chiếc taxi.",
        "dialogue": [
            {"speaker": "John", "hindi": "नमस्ते।", "translation": "Hello."},
            {"speaker": "Ramesh", "hindi": "नमस्ते सर! टैक्सी चाहिए?", "translation": "Hello sir! Need a taxi?"},
            {"speaker": "John", "hindi": "हाँ।", "translation": "Yes."}
        ],
        "teacher": "Greetings are the first step. 'Namaste' means both hello and goodbye.",
        "vocab": {"hindi": "नमस्ते", "english": "Hello", "vietnamese": "Xin chào", "translit": "Namaste"},
        "achievements": ["Say hello", "Understand basic greetings", "Survive the airport"]
    },
    {
        "id": "episode_2",
        "title": "The Taxi Ride",
        "story_en": "Ramesh drives fast. John holds on for dear life. Ramesh talks non-stop about cricket.",
        "story_vi": "Ramesh lái xe nhanh. Lan bám chặt. Ramesh nói không ngừng về môn cricket.",
        "dialogue": [
            {"speaker": "Ramesh", "hindi": "मेरा नाम रमेश है। आपका नाम क्या है?", "translation": "My name is Ramesh. What is your name?"},
            {"speaker": "John", "hindi": "मेरा नाम जॉन है।", "translation": "My name is John."}
        ],
        "teacher": "To introduce yourself, say 'Mera naam [Name] hai'.",
        "vocab": {"hindi": "नाम", "english": "Name", "vietnamese": "Tên", "translit": "Naam"},
        "achievements": ["Introduce yourself", "Ask someone's name", "Survive Ramesh's driving"]
    },
    {
        "id": "episode_3",
        "title": "Meeting Rahul",
        "story_en": "John arrives at the guesthouse. He meets Rahul, who is drinking his fifth cup of chai today.",
        "story_vi": "Lan đến nhà khách. Cô gặp Rahul, người đang uống tách trà thứ năm trong ngày.",
        "dialogue": [
            {"speaker": "Rahul", "hindi": "नमस्ते जॉन! चाय पियोगे?", "translation": "Hello John! Will you drink tea?"},
            {"speaker": "John", "hindi": "हाँ, कृपया।", "translation": "Yes, please."}
        ],
        "teacher": "'Chai' is life in India. Learn to accept it graciously.",
        "vocab": {"hindi": "चाय", "english": "Tea", "vietnamese": "Trà", "translit": "Chai"},
        "achievements": ["Say please and thank you", "Order a chai", "Meet your first friend"]
    },
    {
        "id": "episode_4",
        "title": "Grandma's Kitchen",
        "story_en": "Rahul introduces John to his Grandma (Dadi). She immediately thinks John is too skinny.",
        "story_vi": "Rahul giới thiệu Lan với bà nội (Dadi). Bà ngay lập tức nghĩ Lan quá gầy.",
        "dialogue": [
            {"speaker": "Dadi", "hindi": "बेटा, और खाओ!", "translation": "Child, eat more!"},
            {"speaker": "John", "hindi": "नहीं, धन्यवाद। पेट भरा है।", "translation": "No, thank you. Stomach is full."}
        ],
        "teacher": "Dadi means Grandma. 'Beta' literally means son, but is used affectionately for any young person.",
        "vocab": {"hindi": "धन्यवाद", "english": "Thank you", "vietnamese": "Cảm ơn", "translit": "Dhanyavaad"},
        "achievements": ["Decline food politely", "Address elders with respect", "Experience Indian hospitality"]
    },
    {
        "id": "episode_5",
        "title": "The Market",
        "story_en": "John visits the local bazaar to buy fruit. It's loud, colorful, and overwhelming.",
        "story_vi": "Lan đến chợ địa phương để mua trái cây. Nó ồn ào, đầy màu sắc và quá tải.",
        "dialogue": [
            {"speaker": "John", "hindi": "यह कितने का है?", "translation": "How much is this?"},
            {"speaker": "Shopkeeper", "hindi": "पचास रुपये।", "translation": "Fifty rupees."}
        ],
        "teacher": "Shopping requires numbers. 'Kitne ka hai?' means 'How much is it?'.",
        "vocab": {"hindi": "कितने", "english": "How much", "vietnamese": "Bao nhiêu", "translit": "Kitne"},
        "achievements": ["Ask for prices", "Understand basic numbers", "Buy fruit successfully"]
    },
    {
        "id": "episode_6",
        "title": "Meeting Priya",
        "story_en": "Rahul introduces his friend Priya. She immediately corrects Rahul's terrible English joke.",
        "story_vi": "Rahul giới thiệu cô bạn Priya. Cô ấy ngay lập tức sửa trò đùa tiếng Anh tệ hại của Rahul.",
        "dialogue": [
            {"speaker": "Priya", "hindi": "तुम बहुत बोलते हो, राहुल।", "translation": "You speak too much, Rahul."},
            {"speaker": "Rahul", "hindi": "मुझे पता है!", "translation": "I know!"}
        ],
        "teacher": "Priya is smart and patient. Notice how verbs change: 'bolte' (speak) for masculine plural/respect.",
        "vocab": {"hindi": "बोलना", "english": "To speak", "vietnamese": "Nói", "translit": "Bolna"},
        "achievements": ["Understand basic verbs", "Meet Priya", "Notice gender in verbs"]
    },
    {
        "id": "episode_7",
        "title": "Getting Lost",
        "story_en": "John tries to walk to the park but gets completely turned around.",
        "story_vi": "Lan cố gắng đi bộ đến công viên nhưng bị lạc hoàn toàn.",
        "dialogue": [
            {"speaker": "John", "hindi": "माफ़ कीजिए, पार्क कहाँ है?", "translation": "Excuse me, where is the park?"},
            {"speaker": "Stranger", "hindi": "सीधे जाइए।", "translation": "Go straight."}
        ],
        "teacher": "Asking for directions is crucial. 'Kahan hai?' means 'Where is?'.",
        "vocab": {"hindi": "कहाँ", "english": "Where", "vietnamese": "Ở đâu", "translit": "Kahan"},
        "achievements": ["Ask for directions", "Understand 'straight' and 'turn'", "Find your way back"]
    },
    {
        "id": "episode_8",
        "title": "The Rickshaw",
        "story_en": "John takes an auto-rickshaw. The driver tries to charge him double. John negotiates.",
        "story_vi": "Lan đi xe kéo (auto-rickshaw). Tài xế cố tính phí gấp đôi. Lan thương lượng.",
        "dialogue": [
            {"speaker": "John", "hindi": "सौ रुपये? नहीं, पचास!", "translation": "A hundred rupees? No, fifty!"},
            {"speaker": "Driver", "hindi": "ठीक है, बैठिए।", "translation": "Okay, sit."}
        ],
        "teacher": "Bargaining is part of the culture. Be firm but polite.",
        "vocab": {"hindi": "रुपये", "english": "Rupees", "vietnamese": "Rupee", "translit": "Rupaye"},
        "achievements": ["Negotiate a price", "Take an auto-rickshaw", "Save 50 rupees"]
    },
    {
        "id": "episode_9",
        "title": "Arjun Arrives",
        "story_en": "Arjun bursts into the room, two hours late, carrying movie tickets.",
        "story_vi": "Arjun xông vào phòng, trễ hai tiếng đồng hồ, mang theo vé xem phim.",
        "dialogue": [
            {"speaker": "Arjun", "hindi": "देर हो गई, माफ़ करना!", "translation": "Got late, forgive me!"},
            {"speaker": "Priya", "hindi": "तुम हमेशा लेट होते हो।", "translation": "You are always late."}
        ],
        "teacher": "Apologies are important. 'Maaf karna' is the standard way to say sorry.",
        "vocab": {"hindi": "माफ़", "english": "Forgive", "vietnamese": "Tha thứ", "translit": "Maaf"},
        "achievements": ["Say sorry", "Understand time expressions", "Meet Arjun"]
    },
    {
        "id": "episode_10",
        "title": "Bollywood Night",
        "story_en": "The gang watches a Bollywood movie. It's three hours long and has five dance numbers.",
        "story_vi": "Cả nhóm xem một bộ phim Bollywood. Nó dài ba tiếng và có năm tiết mục khiêu vũ.",
        "dialogue": [
            {"speaker": "John", "hindi": "फ़िल्म बहुत अच्छी थी।", "translation": "The movie was very good."},
            {"speaker": "Arjun", "hindi": "हाँ, मुझे गाने पसंद आए।", "translation": "Yes, I liked the songs."}
        ],
        "teacher": "Expressing opinions makes you part of the group. 'Achhi thi' (It was good).",
        "vocab": {"hindi": "फ़िल्म", "english": "Movie", "vietnamese": "Phim", "translit": "Film"},
        "achievements": ["Express an opinion", "Survive a 3-hour movie", "Talk about preferences"]
    },
    {
        "id": "episode_11",
        "title": "A Rainy Day",
        "story_en": "The monsoon rains start. The streets flood. John stays indoors with a hot chai.",
        "story_vi": "Trưa gió mùa bắt đầu. Đường phố ngập lụt. Lan ở trong nhà với một tách trà nóng.",
        "dialogue": [
            {"speaker": "Rahul", "hindi": "आज बहुत बारिश हो रही है।", "translation": "It is raining heavily today."},
            {"speaker": "John", "hindi": "हाँ, मौसम ठंडा है।", "translation": "Yes, the weather is cold."}
        ],
        "teacher": "Talking about the weather is universal small talk.",
        "vocab": {"hindi": "बारिश", "english": "Rain", "vietnamese": "Mưa", "translit": "Baarish"},
        "achievements": ["Talk about the weather", "Describe temperature", "Enjoy monsoon chai"]
    },
    {
        "id": "episode_12",
        "title": "Spicy Food",
        "story_en": "John tries street food. It is entirely too spicy. He cries.",
        "story_vi": "Lan thử thức ăn đường phố. Nó hoàn toàn quá cay. Cô ấy khóc.",
        "dialogue": [
            {"speaker": "John", "hindi": "यह बहुत तीखा है!", "translation": "This is very spicy!"},
            {"speaker": "Priya", "hindi": "पानी पियो।", "translation": "Drink water."}
        ],
        "teacher": "Warning: Indian food is spicy! 'Teekha' means spicy/hot.",
        "vocab": {"hindi": "तीखा", "english": "Spicy", "vietnamese": "Cay", "translit": "Teekha"},
        "achievements": ["Express discomfort", "Ask for water", "Survive street food"]
    },
    {
        "id": "episode_13",
        "title": "WhatsApp Groups",
        "story_en": "John is added to the family WhatsApp group. Grandma sends him 'Good Morning' flowers every day.",
        "story_vi": "Lan được thêm vào nhóm WhatsApp gia đình. Bà nội gửi hoa 'Chào buổi sáng' mỗi ngày.",
        "dialogue": [
            {"speaker": "Dadi", "hindi": "सुप्रभात बेटा।", "translation": "Good morning child."},
            {"speaker": "John", "hindi": "सुप्रभात दादी जी।", "translation": "Good morning Grandma."}
        ],
        "teacher": "Adding 'Ji' to a name or title shows respect.",
        "vocab": {"hindi": "सुप्रभात", "english": "Good morning", "vietnamese": "Chào buổi sáng", "translit": "Suprabhat"},
        "achievements": ["Use respectful titles", "Send morning greetings", "Navigate family chats"]
    },
    {
        "id": "episode_14",
        "title": "The Cricket Match",
        "story_en": "India is playing a cricket match. Rahul is screaming at the TV. John has no idea what is happening.",
        "story_vi": "Ấn Độ đang chơi một trận cricket. Rahul đang la hét vào TV. Lan không biết chuyện gì đang xảy ra.",
        "dialogue": [
            {"speaker": "Rahul", "hindi": "क्या शॉट है!", "translation": "What a shot!"},
            {"speaker": "John", "hindi": "मुझे समझ नहीं आया।", "translation": "I didn't understand."}
        ],
        "teacher": "It's okay to admit confusion. 'Mujhe samajh nahi aaya' is a very useful phrase.",
        "vocab": {"hindi": "समझ", "english": "Understand", "vietnamese": "Hiểu", "translit": "Samajh"},
        "achievements": ["Admit confusion", "Ask for explanation", "Cheer for India"]
    },
    {
        "id": "episode_15",
        "title": "The Birthday Party",
        "story_en": "It's Arjun's birthday. There is cake, dancing, and too many people.",
        "story_vi": "Hôm nay là sinh nhật của Arjun. Có bánh, khiêu vũ và quá nhiều người.",
        "dialogue": [
            {"speaker": "All", "hindi": "जन्मदिन मुबारक हो!", "translation": "Happy Birthday!"},
            {"speaker": "Arjun", "hindi": "सबको धन्यवाद।", "translation": "Thank you everyone."}
        ],
        "teacher": "'Janamdin mubarak ho' is the standard birthday wish.",
        "vocab": {"hindi": "जन्मदिन", "english": "Birthday", "vietnamese": "Sinh nhật", "translit": "Janamdin"},
        "achievements": ["Wish a happy birthday", "Express gratitude", "Dance at a party"]
    },
    {
        "id": "episode_16",
        "title": "The Misunderstanding",
        "story_en": "John accidentally wears his shoes into Priya's house. She is shocked.",
        "story_vi": "Lan vô tình mang giày vào nhà Priya. Cô ấy bị sốc.",
        "dialogue": [
            {"speaker": "Priya", "hindi": "जूते बाहर उतारो!", "translation": "Take off shoes outside!"},
            {"speaker": "John", "hindi": "ओह, मुझे माफ़ कर दो।", "translation": "Oh, forgive me."}
        ],
        "teacher": "Cultural norms matter. Always take shoes off before entering an Indian home.",
        "vocab": {"hindi": "जूते", "english": "Shoes", "vietnamese": "Giày", "translit": "Joote"},
        "achievements": ["Learn cultural rules", "Apologize properly", "Follow household etiquette"]
    },
    {
        "id": "episode_17",
        "title": "The Train Station",
        "story_en": "The gang takes a train to Agra. The station is a sea of humanity.",
        "story_vi": "Cả nhóm đi tàu đến Agra. Nhà ga là một biển người.",
        "dialogue": [
            {"speaker": "John", "hindi": "हमारी ट्रेन कब आएगी?", "translation": "When will our train come?"},
            {"speaker": "Rahul", "hindi": "दस मिनट में।", "translation": "In ten minutes."}
        ],
        "teacher": "Travel vocabulary is essential. 'Kab' means when.",
        "vocab": {"hindi": "कब", "english": "When", "vietnamese": "Khi nào", "translit": "Kab"},
        "achievements": ["Ask about time", "Navigate a train station", "Travel with friends"]
    },
    {
        "id": "episode_18",
        "title": "The Taj Mahal",
        "story_en": "They arrive at the Taj Mahal. It is breathtaking. John is speechless.",
        "story_vi": "Họ đến đền Taj Mahal. Nó đẹp ngoạn mục. Lan không thốt nên lời.",
        "dialogue": [
            {"speaker": "John", "hindi": "यह बहुत सुंदर है।", "translation": "This is very beautiful."},
            {"speaker": "Priya", "hindi": "हाँ, सच में।", "translation": "Yes, truly."}
        ],
        "teacher": "Expressing admiration: 'Sundar' means beautiful.",
        "vocab": {"hindi": "सुंदर", "english": "Beautiful", "vietnamese": "Đẹp", "translit": "Sundar"},
        "achievements": ["Express admiration", "Describe beauty", "Visit a monument"]
    },
    {
        "id": "episode_19",
        "title": "Packing Up",
        "story_en": "The trip is over. John packs his bags. He feels a sense of sadness, but also deep connection.",
        "story_vi": "Chuyến đi đã kết thúc. Lan dọn hành lý. Cô cảm thấy buồn, nhưng cũng có một sự kết nối sâu sắc.",
        "dialogue": [
            {"speaker": "John", "hindi": "मुझे वापस नहीं जाना है।", "translation": "I don't want to go back."},
            {"speaker": "Rahul", "hindi": "तुम फिर आना, दोस्त।", "translation": "You come again, friend."}
        ],
        "teacher": "Emotions are complex. 'Dost' means friend, and it carries weight.",
        "vocab": {"hindi": "दोस्त", "english": "Friend", "vietnamese": "Bạn", "translit": "Dost"},
        "achievements": ["Express sadness", "Use endearing terms", "Make a promise to return"]
    },
    {
        "id": "episode_20",
        "title": "Diwali",
        "story_en": "It is Diwali. The house is lit with diyas. John is wearing a kurta. He realizes he is no longer a stranger.",
        "story_vi": "Hôm nay là lễ Diwali. Ngôi nhà được thắp sáng bằng những chiếc đèn diya. Lan mặc bộ kurta. Cô nhận ra mình không còn là người lạ nữa.",
        "dialogue": [
            {"speaker": "Dadi", "hindi": "शुभ दीपावली, बेटा।", "translation": "Happy Diwali, child."},
            {"speaker": "John", "hindi": "शुभ दीपावली। मुझे यहाँ घर जैसा लगता है।", "translation": "Happy Diwali. I feel at home here."}
        ],
        "teacher": "This is it. You've crossed the threshold from tourist to family.",
        "vocab": {"hindi": "घर", "english": "Home", "vietnamese": "Nhà", "translit": "Ghar"},
        "achievements": ["Celebrate a festival", "Wear traditional clothes", "Feel at home in India"]
    }
]

def create_json(ep):
    return [
        {
            "type": "StoryMode",
            "title_en": ep["title"],
            "title_vi": "Chương " + ep["id"].split("_")[1],
            "paragraphs": [
                {"hindi": "यह एक नई कहानी है।", "translation": ep["story_en"]}
            ],
            "question_en": "How does the main character feel?",
            "question_vi": "Nhân vật chính cảm thấy thế nào?",
            "options": [
                {"text": "Experiencing new things", "isCorrect": True},
                {"text": "Sleeping", "isCorrect": False}
            ]
        },
        {
            "type": "DialogueMode",
            "title": "Conversation Practice",
            "lines": ep["dialogue"]
        },
        {
            "type": "TeachRule",
            "title": "Teacher's Tip",
            "explanation": ep["teacher"],
            "simpleRule": "Practice makes perfect."
        },
        {
            "type": "Flashcard",
            "hindi": ep["vocab"]["hindi"],
            "transliteration": ep["vocab"]["translit"],
            "english": ep["vocab"]["english"],
            "vietnamese": ep["vocab"]["vietnamese"],
            "audio": ep["vocab"]["hindi"]
        },
        {
            "type": "MultipleChoice",
            "prompt": "Choose the correct translation:",
            "prompt_vi": "Chọn bản dịch đúng:",
            "text": ep["vocab"]["hindi"],
            "subtext": ep["vocab"]["translit"],
            "answer": ep["vocab"]["english"],
            "answer_vi": ep["vocab"]["vietnamese"],
            "options": [ep["vocab"]["english"], "Car", "House", "Tree"],
            "options_vi": [ep["vocab"]["vietnamese"], "Xe hơi", "Nhà", "Cây"]
        },
        {
            "type": "RevisionSummary",
            "title": "Episode Summary",
            "takeaways": ep["achievements"]
        }
    ]

# Generate first 20 episodes based on the deeply crafted narrative
for ep in episodes_data:
    file_path = os.path.join(episodes_dir, f"{ep['id']}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(create_json(ep), f, ensure_ascii=False, indent=2)

# For episodes 21-100, generate basic placeholders just to avoid crash if clicked
for i in range(21, 101):
    file_path = os.path.join(episodes_dir, f"episode_{i}.json")
    ep_data = [{
        "type": "StoryMode",
        "title_en": f"Episode {i}",
        "title_vi": f"Chương {i}",
        "paragraphs": [{"hindi": "कहानियाँ आ रही हैं।", "translation": "Stories are coming soon."}],
        "question_en": "Is this episode ready?",
        "question_vi": "Tập này đã sẵn sàng chưa?",
        "options": [{"text": "Soon", "isCorrect": True}, {"text": "No", "isCorrect": False}]
    }]
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(ep_data, f, ensure_ascii=False, indent=2)

print("Generated 20 emotional episodes and 80 placeholders!")
