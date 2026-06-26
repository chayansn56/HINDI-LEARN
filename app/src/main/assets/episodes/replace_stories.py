import re
import json

file_path = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/java/com/example/hindilearn/ui/gamified/StoriesScreen.kt"
with open(file_path, "r") as f:
    content = f.read()

# I will write a simple python script to parse the `val stories = listOf(...)` block
# and write the kotlin code to use LaunchedEffect instead.

replacement_kt = """    var stories = remember { androidx.compose.runtime.mutableStateListOf<Story>() }
    
    androidx.compose.runtime.LaunchedEffect(Unit) {
        try {
            val inputStream = context.assets.open("episodes/stories.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            val jsonArray = org.json.JSONArray(jsonString)
            
            val parsedStories = mutableListOf<Story>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                
                val paragraphsArray = obj.getJSONArray("paragraphs")
                val paragraphs = mutableListOf<StoryParagraph>()
                for (j in 0 until paragraphsArray.length()) {
                    val pObj = paragraphsArray.getJSONObject(j)
                    paragraphs.add(
                        StoryParagraph(
                            hindi = pObj.getString("hindi"),
                            english = pObj.getString("english"),
                            vietnamese = pObj.getString("vietnamese")
                        )
                    )
                }
                
                val optionsArray = obj.getJSONArray("options")
                val options = mutableListOf<String>()
                for (j in 0 until optionsArray.length()) {
                    options.add(optionsArray.getString(j))
                }
                
                parsedStories.add(
                    Story(
                        id = obj.getString("id"),
                        titleHi = obj.getString("titleHi"),
                        titleEn = obj.getString("titleEn"),
                        titleVi = obj.getString("titleVi"),
                        descriptionEn = obj.getString("descriptionEn"),
                        descriptionVi = obj.getString("descriptionVi"),
                        characterIcon = obj.getString("characterIcon"),
                        imageDrawableName = obj.getString("imageDrawableName"),
                        paragraphs = paragraphs,
                        questionEn = obj.getString("questionEn"),
                        questionVi = obj.getString("questionVi"),
                        options = options,
                        correctOptionIdx = obj.getInt("correctOptionIdx")
                    )
                )
            }
            stories.clear()
            stories.addAll(parsedStories)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }"""

start_str = "    val stories = listOf("
end_str = "    PremiumBackground {"

start_idx = content.find(start_str)
end_idx = content.find(end_str)

new_content = content[:start_idx] + replacement_kt + "\n\n" + content[end_idx:]

with open(file_path, "w") as f:
    f.write(new_content)
    
print("Replaced kotlin.")
