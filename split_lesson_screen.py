import os

input_file = "app/src/main/java/com/example/hindilearn/ui/gamified/LessonScreen.kt"
output_screen = "app/src/main/java/com/example/hindilearn/ui/gamified/LessonScreen.kt.tmp"
output_exercises = "app/src/main/java/com/example/hindilearn/ui/gamified/LessonExercises.kt"

with open(input_file, "r") as f:
    lines = f.readlines()

split_index = -1
for i, line in enumerate(lines):
    if line.startswith("fun MultipleChoiceUI("):
        split_index = i
        break

if split_index != -1:
    imports = []
    for line in lines:
        if line.startswith("package ") or line.startswith("import "):
            imports.append(line)
        if line.startswith("@Composable"):
            break

    screen_lines = lines[:split_index]
    exercises_lines = lines[split_index:]

    with open(output_screen, "w") as f:
        f.writelines(screen_lines)
    
    with open(output_exercises, "w") as f:
        f.writelines(imports)
        f.write("\n")
        f.writelines(exercises_lines)

    os.rename(output_screen, input_file)
    print("Split successful!")
else:
    print("Could not find MultipleChoiceUI")
