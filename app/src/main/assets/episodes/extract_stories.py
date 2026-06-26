import re
import json

file_path = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/java/com/example/hindilearn/ui/gamified/StoriesScreen.kt"
with open(file_path, "r") as f:
    content = f.read()

# I will use a simple regex to replace the `val stories = listOf(...)` block.
# Wait, actually I already wrote the `stories.json` file for story_1 and story_2.
# Let's write the entire `stories.json` again with all 6 stories (up to story_6 which might be there).
