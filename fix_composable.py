import os

file_path = "app/src/main/java/com/example/hindilearn/ui/gamified/LessonExercises.kt"
with open(file_path, "r") as f:
    lines = f.readlines()

new_lines = []
for i, line in enumerate(lines):
    if line.startswith("fun ") and "UI(" in line and "getMockExercises" not in line:
        if i > 0 and "@Composable" not in lines[i-1]:
            new_lines.append("@Composable\n")
    new_lines.append(line)

with open(file_path, "w") as f:
    f.writelines(new_lines)
