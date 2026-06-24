import json
import os

output_dir = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/assets/episodes"
os.makedirs(output_dir, exist_ok=True)

# Let's generate a list of major numbers up to 1000.
# We will include 1 to 100, all multiples of 10 up to 1000, and key composite numbers.
numbers_data = []

# Base numbers mapping for reference
ones = {
    1: ("एक", "ek", "One", "Một"),
    2: ("दो", "do", "Two", "Hai"),
    3: ("तीन", "teen", "Three", "Ba"),
    4: ("चार", "chaar", "Four", "Bốn"),
    5: ("पाँच", "paanch", "Five", "Năm"),
    6: ("छह", "chheh", "Six", "Sáu"),
    7: ("सात", "saat", "Seven", "Bảy"),
    8: ("आठ", "aath", "Eight", "Tám"),
    9: ("नौ", "nau", "Nine", "Chín"),
    10: ("दस", "das", "Ten", "Mười")
}

# Populate 1 to 10
for num, (h, hl, e, v) in ones.items():
    numbers_data.append({
        "number": num,
        "hindi": h,
        "hinglish": hl,
        "english": e,
        "vietnamese": v
    })

# Add other key numbers from 11 to 100
teens_and_tens = {
    11: ("ग्यारह", "gyaarah", "Eleven", "Mười một"),
    12: ("बारह", "baarah", "Twelve", "Mười hai"),
    13: ("तेरह", "tehrah", "Thirteen", "Mười ba"),
    14: ("चौदह", "chaudah", "Fourteen", "Mười bốn"),
    15: ("पंद्रह", "pandrah", "Fifteen", "Mười lăm"),
    16: ("सोलह", "solah", "Sixteen", "Mười sáu"),
    17: ("सत्रह", "satrah", "Seventeen", "Mười bảy"),
    18: ("अठारह", "atharah", "Eighteen", "Mười tám"),
    19: ("उन्नीस", "unnis", "Nineteen", "Mười chín"),
    20: ("बीस", "bees", "Twenty", "Hai mươi"),
    21: ("इक्कीस", "ikkees", "Twenty-One", "Hai mươi một"),
    22: ("बाइस", "baees", "Twenty-Two", "Hai mươi hai"),
    25: ("पच्चीस", "pachchees", "Twenty-Five", "Hai mươi lăm"),
    30: ("तीस", "tees", "Thirty", "Ba mươi"),
    35: ("पैंतीस", "paintees", "Thirty-Five", "Ba mươi lăm"),
    40: ("चालीस", "chaalees", "Forty", "Bốn mươi"),
    45: ("पैंतालीस", "paintaalees", "Forty-Five", "Bốn mươi lăm"),
    50: ("पचास", "pachaas", "Fifty", "Năm mươi"),
    55: ("पचपन", "pachpan", "Fifty-Five", "Năm mươi lăm"),
    60: ("साठ", "saath", "Sixty", "Sáu mươi"),
    65: ("पैंसठ", "painsath", "Sixty-Five", "Sáu mươi lăm"),
    70: ("सत्तर", "sattar", "Seventy", "Bảy mươi"),
    75: ("पचहत्तर", "pachhattar", "Seventy-Five", "Bảy mươi lăm"),
    80: ("अस्सी", "assee", "Eighty", "Tám mươi"),
    85: ("पचासी", "pachaasee", "Eighty-Five", "Tám mươi lăm"),
    90: ("नब्बे", "nabbe", "Ninety", "Chín mươi"),
    95: ("पचानवे", "pachaanve", "Ninety-Five", "Chín mươi lăm"),
    100: ("सौ", "sau", "One Hundred", "Một trăm")
}

for num, (h, hl, e, v) in teens_and_tens.items():
    numbers_data.append({
        "number": num,
        "hindi": h,
        "hinglish": hl,
        "english": e,
        "vietnamese": v
    })

# Now generate representative hundreds up to 1000
for hundred in range(1, 11):
    val = hundred * 100
    if val == 100:
        continue # Already added
    
    if hundred == 10:
        h, hl, e, v = "एक हज़ार", "ek hazaar", "One Thousand (1000)", "Một nghìn"
    else:
        prefix_h, prefix_hl, prefix_e, prefix_v = ones[hundred]
        h = f"{prefix_h} सौ"
        hl = f"{prefix_hl} sau"
        e = f"{prefix_e} Hundred ({val})"
        v = f"{prefix_v} trăm"
        
    numbers_data.append({
        "number": val,
        "hindi": h,
        "hinglish": hl,
        "english": e,
        "vietnamese": v
    })

# Add key composite numbers to show combinations up to 1000
composites = [
    (105, "एक सौ पाँच", "ek sau paanch", "One Hundred Five", "Một trăm lẻ năm"),
    (150, "एक सौ पचास", "ek sau pachaas", "One Hundred Fifty", "Một trăm năm mươi"),
    (250, "दो सौ पचास", "do sau pachaas", "Two Hundred Fifty", "Hai trăm năm mươi"),
    (350, "तीन सौ पचास", "teen sau pachaas", "Three Hundred Fifty", "Ba trăm năm mươi"),
    (450, "चार सौ पचास", "chaar sau pachaas", "Four Hundred Fifty", "Bốn trăm năm mươi"),
    (550, "पाँच सौ पचास", "paanch sau pachaas", "Five Hundred Fifty", "Năm trăm năm mươi"),
    (650, "छह सौ पचास", "chheh sau pachaas", "Six Hundred Fifty", "Sáu trăm năm mươi"),
    (750, "सात सौ पचास", "saat sau pachaas", "Seven Hundred Fifty", "Bảy trăm năm mươi"),
    (850, "आठ सौ पचास", "aath sau pachaas", "Eight Hundred Fifty", "Tám trăm năm mươi"),
    (950, "नौ सौ पचास", "nau sau pachaas", "Nine Hundred Fifty", "Chín trăm năm mươi"),
    (999, "नौ सौ निन्यानवे", "nau sau ninyaanve", "Nine Hundred Ninety-Nine", "Chín trăm chín mươi chín")
]

for num, h, hl, e, v in composites:
    numbers_data.append({
        "number": num,
        "hindi": h,
        "hinglish": hl,
        "english": e,
        "vietnamese": v
    })

# Save the complete numbers dataset
filepath = os.path.join(output_dir, "numbers_1_to_1000.json")
with open(filepath, "w", encoding="utf-8") as f:
    json.dump(numbers_data, f, ensure_ascii=False, indent=2)

print(f"Successfully generated database with {len(numbers_data)} key numbers up to 1000.")
