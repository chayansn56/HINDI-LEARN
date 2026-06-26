import re
import json

file_path = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/java/com/example/hindilearn/ui/gamified/StoriesScreen.kt"
with open(file_path, "r") as f:
    content = f.read()

start_str = "    val stories = listOf("
end_str = "    PremiumBackground {"
start_idx = content.find(start_str)
end_idx = content.find(end_str)

stories_block = content[start_idx:end_idx]

# I will write the hardcoded JSON myself since parsing kotlin object initialization with regex is too error-prone.
# Wait, let's just run the code replacement first, then I'll use another script to populate the JSON properly.
