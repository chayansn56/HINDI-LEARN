# -*- coding: utf-8 -*-
import json
import os

base_path = '/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/english_episodes'
os.makedirs(base_path, exist_ok=True)

# Vocab arrays for Phase 0 (1 to 14)
p0_lessons = [
    {
        "id": "episode_0_1", "title": "The English Alphabet",
        "vocab": [
            {"eng": "Alphabet", "vi": "Bảng chữ cái"},
            {"eng": "Letter", "vi": "Chữ cái"},
            {"eng": "Word", "vi": "Từ"},
            {"eng": "Name", "vi": "Tên"},
            {"eng": "Spell", "vi": "Đánh vần"},
            {"eng": "Write", "vi": "Viết"}
        ]
    },
    {
        "id": "episode_0_2", "title": "Vowels & Consonants",
        "vocab": [
            {"eng": "Vowel", "vi": "Nguyên âm"},
            {"eng": "Consonant", "vi": "Phụ âm"},
            {"eng": "Sound", "vi": "Âm thanh"},
            {"eng": "Speak", "vi": "Nói"},
            {"eng": "Read", "vi": "Đọc"},
            {"eng": "Listen", "vi": "Nghe"}
        ]
    },
    {
        "id": "episode_0_3", "title": "Tricky Sounds (th, sh, ch)",
        "vocab": [
            {"eng": "Three", "vi": "Số ba"},
            {"eng": "Ship", "vi": "Tàu thủy"},
            {"eng": "Chair", "vi": "Cái ghế"},
            {"eng": "Thin", "vi": "Mỏng / Gầy"},
            {"eng": "Shop", "vi": "Cửa hàng"},
            {"eng": "Chew", "vi": "Nhai"}
        ]
    },
    {
        "id": "episode_0_4", "title": "Final Sounds (s, t, d)",
        "vocab": [
            {"eng": "Cats", "vi": "Những con mèo"},
            {"eng": "Beds", "vi": "Những cái giường"},
            {"eng": "Walked", "vi": "Đã đi bộ"},
            {"eng": "Played", "vi": "Đã chơi"},
            {"eng": "Dog", "vi": "Con chó"},
            {"eng": "Map", "vi": "Bản đồ"}
        ]
    },
    {
        "id": "episode_0_5", "title": "Word Stress Basics",
        "vocab": [
            {"eng": "Doctor", "vi": "Bác sĩ"},
            {"eng": "Banana", "vi": "Quả chuối"},
            {"eng": "Computer", "vi": "Máy tính"},
            {"eng": "Apple", "vi": "Quả táo"},
            {"eng": "Paper", "vi": "Giấy"},
            {"eng": "Accent", "vi": "Giọng điệu / Trọng âm"}
        ]
    },
    {
        "id": "episode_0_6", "title": "Basic Greetings",
        "vocab": [
            {"eng": "Hello", "vi": "Xin chào"},
            {"eng": "Goodbye", "vi": "Tạm biệt"},
            {"eng": "Thank you", "vi": "Cảm ơn bạn"},
            {"eng": "How are you?", "vi": "Bạn khỏe không?"},
            {"eng": "Good morning", "vi": "Chào buổi sáng"},
            {"eng": "Nice to meet you", "vi": "Rất vui được gặp bạn"}
        ]
    },
    {
        "id": "episode_0_7", "title": "Simple Pronouns",
        "vocab": [
            {"eng": "I", "vi": "Tôi"},
            {"eng": "You", "vi": "Bạn / Các bạn"},
            {"eng": "He", "vi": "Anh ấy"},
            {"eng": "She", "vi": "Cô ấy"},
            {"eng": "We", "vi": "Chúng tôi"},
            {"eng": "They", "vi": "Họ / Chúng nó"}
        ]
    },
    {
        "id": "episode_0_8", "title": "To Be: Present",
        "vocab": [
            {"eng": "Am", "vi": "Thì / Là / Ở (đi với I)"},
            {"eng": "Is", "vi": "Thì / Là / Ở (đi với số ít)"},
            {"eng": "Are", "vi": "Thì / Là / Ở (đi với số nhiều)"},
            {"eng": "Happy", "vi": "Hạnh phúc / Vui vẻ"},
            {"eng": "Sad", "vi": "Buồn bã"},
            {"eng": "Tired", "vi": "Mệt mỏi"}
        ]
    },
    {
        "id": "episode_0_9", "title": "Numbers 1-100",
        "vocab": [
            {"eng": "One", "vi": "Số một"},
            {"eng": "Ten", "vi": "Số mười"},
            {"eng": "Fifty", "vi": "Số năm mươi"},
            {"eng": "Hundred", "vi": "Một trăm"},
            {"eng": "First", "vi": "Đầu tiên / Thứ nhất"},
            {"eng": "Second", "vi": "Thứ hai / Giây"}
        ]
    },
    {
        "id": "episode_0_10", "title": "Telling Time",
        "vocab": [
            {"eng": "O'clock", "vi": "Giờ đúng"},
            {"eng": "Half past", "vi": "Rưỡi / Ba mươi phút qua"},
            {"eng": "Minute", "vi": "Phút"},
            {"eng": "Hour", "vi": "Giờ / Tiếng đồng hồ"},
            {"eng": "Early", "vi": "Sớm"},
            {"eng": "Late", "vi": "Muộn / Trễ"}
        ]
    },
    {
        "id": "episode_0_11", "title": "Family Members",
        "vocab": [
            {"eng": "Father", "vi": "Bố / Cha"},
            {"eng": "Mother", "vi": "Mẹ"},
            {"eng": "Brother", "vi": "Anh / Em trai"},
            {"eng": "Sister", "vi": "Chị / Em gái"},
            {"eng": "Son", "vi": "Con trai"},
            {"eng": "Daughter", "vi": "Con gái"}
        ]
    },
    {
        "id": "episode_0_12", "title": "Food & Drink",
        "vocab": [
            {"eng": "Rice", "vi": "Cơm / Gạo"},
            {"eng": "Bread", "vi": "Bánh mì"},
            {"eng": "Water", "vi": "Nước uống"},
            {"eng": "Milk", "vi": "Sữa"},
            {"eng": "Tea", "vi": "Trà"},
            {"eng": "Coffee", "vi": "Cà phê"}
        ]
    },
    {
        "id": "episode_0_13", "title": "Body Parts",
        "vocab": [
            {"eng": "Head", "vi": "Đầu"},
            {"eng": "Eye", "vi": "Mắt"},
            {"eng": "Nose", "vi": "Mũi"},
            {"eng": "Mouth", "vi": "Miệng"},
            {"eng": "Hand", "vi": "Bàn tay"},
            {"eng": "Foot", "vi": "Bàn chân"}
        ]
    },
    {
        "id": "episode_0_14", "title": "Colors & Shapes",
        "vocab": [
            {"eng": "Red", "vi": "Màu đỏ"},
            {"eng": "Blue", "vi": "Màu xanh dương"},
            {"eng": "Green", "vi": "Màu xanh lá"},
            {"eng": "Circle", "vi": "Hình tròn"},
            {"eng": "Square", "vi": "Hình vuông"},
            {"eng": "Triangle", "vi": "Hình tam giác"}
        ]
    }
]

# Vocab arrays for Phase 1 (15 to 30)
p1_lessons = [
    {
        "id": "episode_1_1", "title": "Clothing",
        "vocab": [
            {"eng": "Shirt", "vi": "Áo sơ mi"},
            {"eng": "Pants", "vi": "Quần dài"},
            {"eng": "Dress", "vi": "Váy liền"},
            {"eng": "Skirt", "vi": "Chân váy"},
            {"eng": "Shoes", "vi": "Giày"},
            {"eng": "Hat", "vi": "Cái mũ"}
        ]
    },
    {
        "id": "episode_1_2", "title": "Simple Present Tense",
        "vocab": [
            {"eng": "Like", "vi": "Thích"},
            {"eng": "Love", "vi": "Yêu"},
            {"eng": "Have", "vi": "Có"},
            {"eng": "Want", "vi": "Muốn"},
            {"eng": "Live", "vi": "Sống"},
            {"eng": "Work", "vi": "Làm việc"}
        ]
    },
    {
        "id": "episode_1_3", "title": "Action Verbs",
        "vocab": [
            {"eng": "Run", "vi": "Chạy"},
            {"eng": "Walk", "vi": "Đi bộ"},
            {"eng": "Jump", "vi": "Nhảy"},
            {"eng": "Eat", "vi": "Ăn"},
            {"eng": "Drink", "vi": "Uống"},
            {"eng": "Sleep", "vi": "Ngủ"}
        ]
    },
    {
        "id": "episode_1_4", "title": "Daily Routine",
        "vocab": [
            {"eng": "Wake up", "vi": "Thức dậy"},
            {"eng": "Brush teeth", "vi": "Đánh răng"},
            {"eng": "Take a shower", "vi": "Tắm vòi hoa sen"},
            {"eng": "Eat breakfast", "vi": "Ăn sáng"},
            {"eng": "Go to school", "vi": "Đi học"},
            {"eng": "Go to bed", "vi": "Đi ngủ"}
        ]
    },
    {
        "id": "episode_1_5", "title": "Days & Months",
        "vocab": [
            {"eng": "Monday", "vi": "Thứ hai"},
            {"eng": "Sunday", "vi": "Chủ nhật"},
            {"eng": "January", "vi": "Tháng một"},
            {"eng": "December", "vi": "Tháng mười hai"},
            {"eng": "Week", "vi": "Tuần"},
            {"eng": "Month", "vi": "Tháng"}
        ]
    },
    {
        "id": "episode_1_6", "title": "Weather & Seasons",
        "vocab": [
            {"eng": "Sunny", "vi": "Có nắng"},
            {"eng": "Rainy", "vi": "Có mưa"},
            {"eng": "Windy", "vi": "Có gió"},
            {"eng": "Summer", "vi": "Mùa hè"},
            {"eng": "Winter", "vi": "Mùa đông"},
            {"eng": "Spring", "vi": "Mùa xuân"}
        ]
    },
    {
        "id": "episode_1_7", "title": "Adjectives: Opposites",
        "vocab": [
            {"eng": "Big", "vi": "To / Lớn"},
            {"eng": "Small", "vi": "Nhỏ / Bé"},
            {"eng": "Hot", "vi": "Nóng"},
            {"eng": "Cold", "vi": "Lạnh"},
            {"eng": "New", "vi": "Mới"},
            {"eng": "Old", "vi": "Cũ / Già"}
        ]
    },
    {
        "id": "episode_1_8", "title": "Prepositions of Place",
        "vocab": [
            {"eng": "In", "vi": "Trong / Ở trong"},
            {"eng": "On", "vi": "Trên / Ở trên"},
            {"eng": "Under", "vi": "Dưới / Ở dưới"},
            {"eng": "Next to", "vi": "Bên cạnh"},
            {"eng": "Between", "vi": "Ở giữa (hai vật)"},
            {"eng": "Behind", "vi": "Phía sau"}
        ]
    },
    {
        "id": "episode_1_9", "title": "Asking Questions",
        "vocab": [
            {"eng": "What", "vi": "Cái gì"},
            {"eng": "Who", "vi": "Ai"},
            {"eng": "Where", "vi": "Ở đâu"},
            {"eng": "When", "vi": "Khi nào"},
            {"eng": "Why", "vi": "Tại sao"},
            {"eng": "How", "vi": "Như thế nào / Bằng cách nào"}
        ]
    },
    {
        "id": "episode_1_10", "title": "Present Continuous",
        "vocab": [
            {"eng": "Am reading", "vi": "Đang đọc (đi với I)"},
            {"eng": "Is eating", "vi": "Đang ăn (đi với số ít)"},
            {"eng": "Are playing", "vi": "Đang chơi (đi với số nhiều)"},
            {"eng": "Now", "vi": "Bây giờ"},
            {"eng": "At the moment", "vi": "Vào lúc này"},
            {"eng": "Currently", "vi": "Hiện tại"}
        ]
    },
    {
        "id": "episode_1_11", "title": "Simple Past Tense",
        "vocab": [
            {"eng": "Went", "vi": "Đã đi (quá khứ của go)"},
            {"eng": "Ate", "vi": "Đã ăn (quá khứ của eat)"},
            {"eng": "Did", "vi": "Đã làm (quá khứ của do)"},
            {"eng": "Had", "vi": "Đã có (quá khứ của have)"},
            {"eng": "Saw", "vi": "Đã thấy (quá khứ của see)"},
            {"eng": "Yesterday", "vi": "Hôm qua"}
        ]
    },
    {
        "id": "episode_1_12", "title": "Past Continuous",
        "vocab": [
            {"eng": "Was studying", "vi": "Đang học bài (quá khứ số ít)"},
            {"eng": "Were cooking", "vi": "Đang nấu ăn (quá khứ số nhiều)"},
            {"eng": "While", "vi": "Trong khi"},
            {"eng": "At five o'clock", "vi": "Vào lúc 5 giờ"},
            {"eng": "All evening", "vi": "Cả buổi tối"},
            {"eng": "Interrupted", "vi": "Bị gián đoạn"}
        ]
    },
    {
        "id": "episode_1_13", "title": "Future with Will",
        "vocab": [
            {"eng": "Will go", "vi": "Sẽ đi"},
            {"eng": "Will help", "vi": "Sẽ giúp đỡ"},
            {"eng": "Will call", "vi": "Sẽ gọi điện"},
            {"eng": "Tomorrow", "vi": "Ngày mai"},
            {"eng": "Next week", "vi": "Tuần tới"},
            {"eng": "Maybe", "vi": "Có lẽ"}
        ]
    },
    {
        "id": "episode_1_14", "title": "Future with Going To",
        "vocab": [
            {"eng": "Going to travel", "vi": "Sẽ đi du lịch (lên kế hoạch)"},
            {"eng": "Going to study", "vi": "Sẽ học bài (lên kế hoạch)"},
            {"eng": "Going to visit", "vi": "Sẽ đi thăm (lên kế hoạch)"},
            {"eng": "Plan", "vi": "Kế hoạch"},
            {"eng": "Intention", "vi": "Ý định"},
            {"eng": "Tonight", "vi": "Tối nay"}
        ]
    },
    {
        "id": "episode_1_15", "title": "Modal Verbs",
        "vocab": [
            {"eng": "Can", "vi": "Có thể"},
            {"eng": "Should", "vi": "Nên"},
            {"eng": "Must", "vi": "Phải / Bắt buộc"},
            {"eng": "May", "vi": "Có thể / Xin phép"},
            {"eng": "Could", "vi": "Đã có thể / Có thể (lịch sự)"},
            {"eng": "Would", "vi": "Sẽ / Muốn"}
        ]
    },
    {
        "id": "episode_1_16", "title": "Foundation Final Exam",
        "vocab": [
            {"eng": "Exam", "vi": "Kỳ thi / Bài kiểm tra"},
            {"eng": "Question", "vi": "Câu hỏi"},
            {"eng": "Answer", "vi": "Câu trả lời"},
            {"eng": "Pass", "vi": "Vượt qua / Đậu"},
            {"eng": "Fail", "vi": "Trượt / Rớt"},
            {"eng": "Score", "vi": "Điểm số"}
        ]
    }
]

# Vocab arrays for Phase 2 (31 to 45)
p2_lessons = [
    {
        "id": "episode_2_1", "title": "Lesson 31: Advanced Family & Relationships",
        "vocab": [
            {"eng": "Uncle", "vi": "Bác / Chú / Cậu"},
            {"eng": "Aunt", "vi": "Cô / Dì / Mợ"},
            {"eng": "Father-in-law", "vi": "Bố chồng / Bố vợ"},
            {"eng": "Mother-in-law", "vi": "Mẹ chồng / Mẹ vợ"},
            {"eng": "Brother-in-law", "vi": "Anh/Em rể / Anh/Em vợ"},
            {"eng": "Relatives", "vi": "Họ hàng"}
        ],
        "builder": {"vi": "Bác của tôi đang đến.", "words": ["My", "uncle", "is", "coming"], "correct": "My uncle is coming"}
    },
    {
        "id": "episode_2_2", "title": "Lesson 32: Social Small Talk & Hobbies",
        "vocab": [
            {"eng": "Interest", "vi": "Sở thích / Mối quan tâm"},
            {"eng": "Hobby", "vi": "Thói quen sở thích"},
            {"eng": "Favorite movie", "vi": "Bộ phim yêu thích"},
            {"eng": "Listening to music", "vi": "Nghe nhạc"},
            {"eng": "Playing sports", "vi": "Chơi thể thao"},
            {"eng": "Traveling", "vi": "Đi du lịch"}
        ],
        "builder": {"vi": "Tôi thích nghe nhạc.", "words": ["I", "like", "listening", "to", "music"], "correct": "I like listening to music"}
    },
    {
        "id": "episode_2_3", "title": "Lesson 33: Present Continuous Tense",
        "vocab": [
            {"eng": "I am working", "vi": "Tôi đang làm việc"},
            {"eng": "She is reading", "vi": "Cô ấy đang đọc sách"},
            {"eng": "They are eating", "vi": "Họ đang ăn"},
            {"eng": "What are you doing?", "vi": "Bạn đang làm gì thế?"},
            {"eng": "He is running", "vi": "Anh ấy đang chạy"},
            {"eng": "We are learning", "vi": "Chúng tôi đang học"}
        ],
        "builder": {"vi": "Cô ấy đang đọc sách.", "words": ["She", "is", "reading", "a", "book"], "correct": "She is reading a book"}
    },
    {
        "id": "episode_2_4", "title": "Lesson 34: Simple Past (Used to)",
        "vocab": [
            {"eng": "I used to play", "vi": "Tôi đã từng chơi"},
            {"eng": "She used to live here", "vi": "Cô ấy đã từng sống ở đây"},
            {"eng": "Did you use to study?", "vi": "Bạn đã từng học chưa?"},
            {"eng": "We used to travel", "vi": "Chúng tôi đã từng đi du lịch"},
            {"eng": "They used to work", "vi": "Họ đã từng làm việc"},
            {"eng": "Memory", "vi": "Kỷ niệm / Trí nhớ"}
        ],
        "builder": {"vi": "Tôi từng sống ở đây.", "words": ["I", "used", "to", "live", "here"], "correct": "I used to live here"}
    },
    {
        "id": "episode_2_5", "title": "Lesson 35: Simple Future Tense",
        "vocab": [
            {"eng": "I will go tomorrow", "vi": "Tôi sẽ đi vào ngày mai"},
            {"eng": "She will help you", "vi": "Cô ấy sẽ giúp bạn"},
            {"eng": "We will meet later", "vi": "Chúng ta sẽ gặp nhau sau"},
            {"eng": "Will they come?", "vi": "Họ có đến không?"},
            {"eng": "I will do it", "vi": "Tôi sẽ làm điều đó"},
            {"eng": "Promise", "vi": "Lời hứa / Hứa hẹn"}
        ],
        "builder": {"vi": "Chúng tôi sẽ gặp nhau ngày mai.", "words": ["We", "will", "meet", "tomorrow"], "correct": "We will meet tomorrow"}
    },
    {
        "id": "episode_2_6", "title": "Lesson 36: Prepositions & Conjunctions",
        "vocab": [
            {"eng": "Because of", "vi": "Bởi vì / Do"},
            {"eng": "In front of", "vi": "Phía trước của"},
            {"eng": "Adjacent to", "vi": "Liền kề / Ngay sát"},
            {"eng": "Although", "vi": "Mặc dù"},
            {"eng": "Therefore", "vi": "Do đó / Vì vậy"},
            {"eng": "However", "vi": "Tuy nhiên"}
        ],
        "builder": {"vi": "Nó ở bên cạnh cái ghế.", "words": ["It", "is", "next", "to", "the", "chair"], "correct": "It is next to the chair"}
    },
    {
        "id": "episode_2_7", "title": "Lesson 37: At the Airport",
        "vocab": [
            {"eng": "Boarding pass", "vi": "Thẻ lên máy bay"},
            {"eng": "Luggage", "vi": "Hành lý"},
            {"eng": "Security check", "vi": "Kiểm tra an ninh"},
            {"eng": "Gate number", "vi": "Số cổng lên tàu"},
            {"eng": "Passport control", "vi": "Kiểm soát hộ chiếu"},
            {"eng": "Terminal", "vi": "Nhà ga sân bay"}
        ],
        "builder": {"vi": "Hành lý của tôi ở đâu?", "words": ["Where", "is", "my", "luggage"], "correct": "Where is my luggage"}
    },
    {
        "id": "episode_2_8", "title": "Lesson 38: In the Kitchen & Food",
        "vocab": [
            {"eng": "Recipe", "vi": "Công thức nấu ăn"},
            {"eng": "Ingredients", "vi": "Thành phần nguyên liệu"},
            {"eng": "To boil", "vi": "Luộc / Đun sôi"},
            {"eng": "Delicious", "vi": "Thơm ngon"},
            {"eng": "Spicy", "vi": "Cay"},
            {"eng": "Oven", "vi": "Lò nướng"}
        ],
        "builder": {"vi": "Món ăn này rất cay.", "words": ["This", "food", "is", "very", "spicy"], "correct": "This food is very spicy"}
    },
    {
        "id": "episode_2_9", "title": "Lesson 39: Weather & Climate",
        "vocab": [
            {"eng": "Forecast", "vi": "Dự báo thời tiết"},
            {"eng": "Humidity", "vi": "Độ ẩm"},
            {"eng": "Thunderstorm", "vi": "Bão kèm sấm sét"},
            {"eng": "Freezing cold", "vi": "Lạnh buốt"},
            {"eng": "Sunny day", "vi": "Ngày đầy nắng"},
            {"eng": "Temperature", "vi": "Nhiệt độ"}
        ],
        "builder": {"vi": "Hôm nay trời đang mưa.", "words": ["It", "is", "raining", "today"], "correct": "It is raining today"}
    },
    {
        "id": "episode_2_10", "title": "Lesson 40: Shopping & Prices",
        "vocab": [
            {"eng": "Receipt", "vi": "Hóa đơn thanh toán"},
            {"eng": "Refund", "vi": "Hoàn tiền"},
            {"eng": "Discount", "vi": "Sự giảm giá"},
            {"eng": "Fitting room", "vi": "Phòng thử đồ"},
            {"eng": "Is it on sale?", "vi": "Cái này có đang giảm giá không?"},
            {"eng": "Expensive", "vi": "Đắt tiền / Mắc"}
        ],
        "builder": {"vi": "Cái này giá bao nhiêu?", "words": ["How", "much", "does", "this", "cost"], "correct": "How much does this cost"}
    },
    {
        "id": "episode_2_11", "title": "Lesson 41: Illness & Doctor",
        "vocab": [
            {"eng": "Headache", "vi": "Đau đầu"},
            {"eng": "Prescription", "vi": "Đơn thuốc bác sĩ"},
            {"eng": "Sore throat", "vi": "Đau họng"},
            {"eng": "Fever", "vi": "Sốt"},
            {"eng": "Appointment", "vi": "Cuộc hẹn lịch hẹn"},
            {"eng": "Clinic", "vi": "Phòng khám"}
        ],
        "builder": {"vi": "Tôi bị đau đầu hôm nay.", "words": ["I", "have", "a", "headache", "today"], "correct": "I have a headache today"}
    },
    {
        "id": "episode_2_12", "title": "Lesson 42: Time & Meetings",
        "vocab": [
            {"eng": "Schedule", "vi": "Lịch trình biểu"},
            {"eng": "Meeting agenda", "vi": "Chương trình họp"},
            {"eng": "Quarter past", "vi": "Hơn mười lăm phút"},
            {"eng": "Half past the hour", "vi": "Nửa tiếng / Ba mươi phút qua"},
            {"eng": "Postpone", "vi": "Trì hoãn lịch"},
            {"eng": "Reschedule", "vi": "Đổi lịch / Sắp xếp lại"}
        ],
        "builder": {"vi": "Cuộc họp vào lúc hai giờ.", "words": ["The", "meeting", "is", "at", "two", "o'clock"], "correct": "The meeting is at two o'clock"}
    },
    {
        "id": "episode_2_13", "title": "Lesson 43: Emergency",
        "vocab": [
            {"eng": "Help me!", "vi": "Giúp tôi với!"},
            {"eng": "Emergency exit", "vi": "Lối thoát hiểm khẩn cấp"},
            {"eng": "Call the police", "vi": "Gọi cảnh sát"},
            {"eng": "I lost my passport", "vi": "Tôi bị mất hộ chiếu"},
            {"eng": "Ambulance", "vi": "Xe cứu thương"},
            {"eng": "Danger", "vi": "Sự nguy hiểm / Nguy hiểm"}
        ],
        "builder": {"vi": "Hãy gọi cảnh sát ngay.", "words": ["Please", "call", "the", "police", "now"], "correct": "Please call the police now"}
    },
    {
        "id": "episode_2_14", "title": "Lesson 44: Local Directions",
        "vocab": [
            {"eng": "Go straight ahead", "vi": "Đi thẳng về phía trước"},
            {"eng": "Turn left at the corner", "vi": "Rẽ trái ở góc đường"},
            {"eng": "Opposite the park", "vi": "Đối diện công viên"},
            {"eng": "Cross the street", "vi": "Băng qua đường"},
            {"eng": "Behind the building", "vi": "Phía sau tòa nhà"},
            {"eng": "Intersection", "vi": "Ngã tư / Điểm giao nhau"}
        ],
        "builder": {"vi": "Rẽ trái ở đây.", "words": ["Turn", "left", "here"], "correct": "Turn left here"}
    },
    {
        "id": "episode_2_15", "title": "Lesson 45: Phase 2 Review",
        "vocab": [
            {"eng": "Congratulations", "vi": "Xin chúc mừng"},
            {"eng": "Fluency progress", "vi": "Tiến trình trôi chảy"},
            {"eng": "Completed level", "vi": "Đã hoàn thành cấp độ"},
            {"eng": "Vocabulary review", "vi": "Ôn tập từ vựng"},
            {"eng": "Success", "vi": "Sự thành công"},
            {"eng": "Milestone", "vi": "Cột mốc quan trọng"}
        ],
        "builder": {"vi": "Chúc mừng thành công của bạn.", "words": ["Congratulations", "on", "your", "success"], "correct": "Congratulations on your success"}
    }
]

# Vocab arrays for Phase 3 (46 to 60)
p3_lessons = [
    {
        "id": "episode_3_1", "title": "Lesson 46: Subjunctive Mood & Hypotheticals",
        "vocab": [
            {"eng": "If I were you", "vi": "Nếu tôi là bạn"},
            {"eng": "I wish I could", "vi": "Tôi ước gì tôi có thể"},
            {"eng": "As if", "vi": "Như thể là"},
            {"eng": "Unless you try", "vi": "Trừ khi bạn thử"},
            {"eng": "In case it rains", "vi": "Phòng trường hợp trời mưa"},
            {"eng": "Provided that", "vi": "Với điều kiện là"}
        ],
        "builder": {"vi": "Tôi ước gì tôi có thể đi.", "words": ["I", "wish", "I", "could", "go"], "correct": "I wish I could go"}
    },
    {
        "id": "episode_3_2", "title": "Lesson 47: Passive Voice",
        "vocab": [
            {"eng": "The book was written", "vi": "Cuốn sách đã được viết"},
            {"eng": "It is made of wood", "vi": "Nó được làm bằng gỗ"},
            {"eng": "The letter was sent", "vi": "Bức thư đã được gửi đi"},
            {"eng": "He was invited", "vi": "Anh ấy đã được mời"},
            {"eng": "Rules must be followed", "vi": "Các quy tắc phải được tuân thủ"},
            {"eng": "Completed project", "vi": "Dự án đã được hoàn thành"}
        ],
        "builder": {"vi": "Bức thư đã được gửi.", "words": ["The", "letter", "was", "sent"], "correct": "The letter was sent"}
    },
    {
        "id": "episode_3_3", "title": "Lesson 48: Phrasal Verbs",
        "vocab": [
            {"eng": "Give up", "vi": "Từ bỏ"},
            {"eng": "Look after", "vi": "Chăm sóc"},
            {"eng": "Find out", "vi": "Tìm ra phát hiện"},
            {"eng": "Run out of", "vi": "Hết cạn kiệt"},
            {"eng": "Take off", "vi": "Cất cánh / Cởi ra"},
            {"eng": "Carry on", "vi": "Tiếp tục làm gì"}
        ],
        "builder": {"vi": "Đừng từ bỏ.", "words": ["Do", "not", "give", "up"], "correct": "Do not give up"}
    },
    {
        "id": "episode_3_4", "title": "Lesson 49: Relative Clauses",
        "vocab": [
            {"eng": "The man who called", "vi": "Người đàn ông đã gọi"},
            {"eng": "The place where we met", "vi": "Nơi chúng ta đã gặp nhau"},
            {"eng": "The reason why she left", "vi": "Lý do cô ấy rời đi"},
            {"eng": "Whose car is this?", "vi": "Xe này của ai?"},
            {"eng": "Which one do you want?", "vi": "Bạn muốn cái nào hơn?"},
            {"eng": "Whom it may concern", "vi": "Gửi những ai quan tâm"}
        ],
        "builder": {"vi": "Đây là nơi chúng ta đã gặp nhau.", "words": ["This", "is", "the", "place", "where", "we", "met"], "correct": "This is the place where we met"}
    },
    {
        "id": "episode_3_5", "title": "Lesson 50: Obligation & Necessity",
        "vocab": [
            {"eng": "You must study", "vi": "Bạn bắt buộc phải học"},
            {"eng": "You should check", "vi": "Bạn nên kiểm tra"},
            {"eng": "It is necessary", "vi": "Nó là cần thiết"},
            {"eng": "No obligation", "vi": "Không có nghĩa vụ bắt buộc"},
            {"eng": "Duty", "vi": "Nhiệm vụ nghĩa vụ"},
            {"eng": "Mandatory", "vi": "Bắt buộc / Quy định"}
        ],
        "builder": {"vi": "Bạn nên kiểm tra lại.", "words": ["You", "should", "check", "it", "again"], "correct": "You should check it again"}
    },
    {
        "id": "episode_3_6", "title": "Lesson 51: Ability & Permission",
        "vocab": [
            {"eng": "I am able to drive", "vi": "Tôi có thể lái xe"},
            {"eng": "May I come in?", "vi": "Tôi xin phép vào được không?"},
            {"eng": "Could you help?", "vi": "Bạn có thể giúp được không?"},
            {"eng": "Permission granted", "vi": "Đã được cho phép"},
            {"eng": "Unable to proceed", "vi": "Không thể tiến hành tiếp"},
            {"eng": "Capable of", "vi": "Có năng lực làm gì"}
        ],
        "builder": {"vi": "Tôi có thể vào được không?", "words": ["May", "I", "come", "in"], "correct": "May I come in"}
    },
    {
        "id": "episode_3_7", "title": "Lesson 52: Desires & Intentions",
        "vocab": [
            {"eng": "I intend to go", "vi": "Tôi dự định đi"},
            {"eng": "My dream is", "vi": "Ước mơ của tôi là"},
            {"eng": "I look forward to", "vi": "Tôi rất mong chờ"},
            {"eng": "Purpose of visit", "vi": "Mục đích chuyến đi"},
            {"eng": "Ambition", "vi": "Hoài bão tham vọng"},
            {"eng": "Goal-oriented", "vi": "Định hướng mục tiêu"}
        ],
        "builder": {"vi": "Tôi mong chờ được gặp bạn.", "words": ["I", "look", "forward", "to", "meeting", "you"], "correct": "I look forward to meeting you"}
    },
    {
        "id": "episode_3_8", "title": "Lesson 53: Habitual Actions in the Past",
        "vocab": [
            {"eng": "We would go to the beach", "vi": "Chúng tôi đã thường ra bãi biển"},
            {"eng": "I used to read daily", "vi": "Tôi đã từng đọc sách mỗi ngày"},
            {"eng": "Habitual routine", "vi": "Thói quen thường nhật"},
            {"eng": "Past practice", "vi": "Tập quán trước đây"},
            {"eng": "Former days", "vi": "Những ngày trước kia"},
            {"eng": "Accustomed to", "vi": "Quen với việc gì"}
        ],
        "builder": {"vi": "Chúng tôi thường ra bãi biển.", "words": ["We", "would", "go", "to", "the", "beach"], "correct": "We would go to the beach"}
    },
    {
        "id": "episode_3_9", "title": "Lesson 54: Office & Professional English",
        "vocab": [
            {"eng": "Collaboration", "vi": "Sự cộng tác hợp tác"},
            {"eng": "Deadline", "vi": "Hạn chót hoàn thành"},
            {"eng": "Performance review", "vi": "Đánh giá hiệu suất làm việc"},
            {"eng": "Deliverable", "vi": "Sản phẩm bàn giao"},
            {"eng": "Conference call", "vi": "Cuộc họp qua điện thoại video"},
            {"eng": "Corporate culture", "vi": "Văn hóa doanh nghiệp"}
        ],
        "builder": {"vi": "Hạn chót là ngày mai.", "words": ["The", "deadline", "is", "tomorrow"], "correct": "The deadline is tomorrow"}
    },
    {
        "id": "episode_3_10", "title": "Lesson 55: Formal Emails & Writing",
        "vocab": [
            {"eng": "Dear Sir or Madam", "vi": "Kính gửi Ông hoặc Bà"},
            {"eng": "Best regards", "vi": "Trân trọng kính thư"},
            {"eng": "I am writing to inquire", "vi": "Tôi viết thư này để hỏi về"},
            {"eng": "Attached document", "vi": "Tài liệu đính kèm"},
            {"eng": "Sincerely yours", "vi": "Chân thành kính thư"},
            {"eng": "Please find enclosed", "vi": "Vui lòng xem đính kèm"}
        ],
        "builder": {"vi": "Trân trọng kính thư.", "words": ["Best", "regards"], "correct": "Best regards"}
    },
    {
        "id": "episode_3_11", "title": "Lesson 56: Common Proverbs & Idioms",
        "vocab": [
            {"eng": "Bite the bullet", "vi": "Cắn răng chịu đựng"},
            {"eng": "Break a leg", "vi": "Chúc may mắn"},
            {"eng": "Piece of cake", "vi": "Dễ như ăn bánh"},
            {"eng": "Actions speak louder than words", "vi": "Hành động quan trọng hơn lời nói"},
            {"eng": "Under the weather", "vi": "Cảm thấy không khỏe"},
            {"eng": "Hit the nail on the head", "vi": "Nói trúng tim đen / Nói cực chuẩn"}
        ],
        "builder": {"vi": "Hành động quan trọng hơn lời nói.", "words": ["Actions", "speak", "louder", "than", "words"], "correct": "Actions speak louder than words"}
    },
    {
        "id": "episode_3_12", "title": "Lesson 57: Expressing Opinions",
        "vocab": [
            {"eng": "From my perspective", "vi": "Theo quan điểm của tôi"},
            {"eng": "I strongly agree", "vi": "Tôi hoàn toàn đồng ý"},
            {"eng": "I disagree entirely", "vi": "Tôi không đồng ý chút nào"},
            {"eng": "In my opinion", "vi": "Theo ý kiến tôi"},
            {"eng": "Debatable topic", "vi": "Chủ đề đáng tranh luận"},
            {"eng": "Consensus", "vi": "Sự đồng thuận"}
        ],
        "builder": {"vi": "Theo ý kiến của tôi.", "words": ["In", "my", "opinion"], "correct": "In my opinion"}
    },
    {
        "id": "episode_3_13", "title": "Lesson 58: Cinema & Art Discussion",
        "vocab": [
            {"eng": "Director", "vi": "Đạo diễn phim"},
            {"eng": "Masterpiece", "vi": "Kiệt tác tác phẩm xuất sắc"},
            {"eng": "Genre of movie", "vi": "Thể loại phim"},
            {"eng": "Performance reviews", "vi": "Đánh giá diễn xuất"},
            {"eng": "Soundtrack", "vi": "Nhạc nền phim"},
            {"eng": "Exhibition", "vi": "Triển lãm nghệ thuật"}
        ],
        "builder": {"vi": "Nó là một kiệt tác.", "words": ["It", "is", "a", "masterpiece"], "correct": "It is a masterpiece"}
    },
    {
        "id": "episode_3_14", "title": "Lesson 59: Media & Current Affairs",
        "vocab": [
            {"eng": "Breaking news", "vi": "Tin nóng hổi"},
            {"eng": "Journalism", "vi": "Ngành báo chí phóng viên"},
            {"eng": "Broadcast", "vi": "Phát sóng chương trình"},
            {"eng": "Editorial article", "vi": "Bài viết xã luận"},
            {"eng": "Social media buzz", "vi": "Sự xôn xao trên mạng xã hội"},
            {"eng": "Coverage", "vi": "Độ phủ sóng / Tin tức đưa ra"}
        ],
        "builder": {"vi": "Đây là tin nóng hổi.", "words": ["This", "is", "breaking", "news"], "correct": "This is breaking news"}
    },
    {
        "id": "episode_3_15", "title": "Lesson 60: Phase 3 Graduation Exam",
        "vocab": [
            {"eng": "Graduation certificate", "vi": "Chứng chỉ tốt nghiệp"},
            {"eng": "Academic success", "vi": "Thành công học tập"},
            {"eng": "Fluent communication", "vi": "Giao tiếp trôi chảy"},
            {"eng": "Best of luck", "vi": "Chúc may mắn tốt lành nhất"},
            {"eng": "Knowledge acquired", "vi": "Kiến thức đã tích lũy"},
            {"eng": "Milestone achieved", "vi": "Đạt được cột mốc quan trọng"}
        ],
        "builder": {"vi": "Chúc bạn may mắn nhất.", "words": ["Best", "of", "luck", "to", "you"], "correct": "Best of luck to you"}
    }
]

def generate_lessons(lessons, include_builder=False):
    for lesson in lessons:
        nodes = []
        
        # 1. Introduction
        nodes.append({
            "type": "Introduction",
            "title": lesson["title"],
            "description": f"Master vocabulary and expressions for {lesson['title']}.",
            "keyPoints": [f"Learn {lesson['vocab'][0]['eng']} and related terms."]
        })
        
        # 2. Flashcards
        for item in lesson["vocab"]:
            nodes.append({
                "type": "Flashcard",
                "hindi": item["eng"],
                "english": item["eng"],
                "vietnamese": item["vi"],
                "audio": item["eng"]
            })
            
        # 3. Multiple Choice
        for idx, item in enumerate(lesson["vocab"]):
            options_vi = [item["vi"]]
            options_en = [item["eng"]]
            
            for other_idx, other_item in enumerate(lesson["vocab"]):
                if other_idx != idx and len(options_vi) < 4:
                    options_vi.append(other_item["vi"])
                    options_en.append(other_item["eng"])
                    
            while len(options_vi) < 4:
                options_vi.append("N/A")
                options_en.append("N/A")
                
            nodes.append({
                "type": "MultipleChoice",
                "prompt": "Choose the correct translation:",
                "prompt_en": "Choose the correct translation:",
                "prompt_vi": "Chọn bản dịch đúng:",
                "hindi": item["eng"],
                "text": item["eng"],
                "subtext": "",
                "answer": item["eng"],
                "answer_vi": item["vi"],
                "options": options_en,
                "options_en": options_en,
                "options_vi": options_vi
            })
            
        # 4. Sentence Builder (if active)
        if include_builder and "builder" in lesson:
            b = lesson["builder"]
            nodes.append({
                "type": "SentenceBuilder",
                "englishSentence": b["vi"],
                "hindiWords": b["words"],
                "correctHindiSentence": b["correct"]
            })
            
        # 5. Match Pairs
        pairs = []
        for item in lesson["vocab"][:4]:
            pairs.append({
                "hindi": item["eng"],
                "english": item["vi"]
            })
        nodes.append({
            "type": "MatchPairs",
            "instruction": "Match the English words with their Vietnamese meanings",
            "pairs": pairs
        })
        
        # Write to file
        file_name = f"{lesson['id']}.json"
        file_path = os.path.join(base_path, file_name)
        with open(file_path, 'w', encoding='utf-8') as f:
            json.dump(nodes, f, ensure_ascii=False, indent=2)
            
        print(f"Generated {file_name}")

print("Generating English Phase 0...")
generate_lessons(p0_lessons, include_builder=False)
print("Generating English Phase 1...")
generate_lessons(p1_lessons, include_builder=False)
print("Generating English Phase 2...")
generate_lessons(p2_lessons, include_builder=True)
print("Generating English Phase 3...")
generate_lessons(p3_lessons, include_builder=True)
print("All English Phases regenerated successfully!")
