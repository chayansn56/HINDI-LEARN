import os
import glob

def replace_in_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Replace TextDark with MaterialTheme.colorScheme.onSurface
    new_content = content.replace("color = TextDark", "color = MaterialTheme.colorScheme.onSurface")
    new_content = new_content.replace("color = com.example.hindilearn.theme.TextDark", "color = MaterialTheme.colorScheme.onSurface")
    new_content = new_content.replace("tint = TextDark", "tint = MaterialTheme.colorScheme.onSurface")
    new_content = new_content.replace("TextDark.copy", "MaterialTheme.colorScheme.onSurface.copy")
    
    # Also we should probably ensure MaterialTheme is imported, but it usually is in these files.
    
    if content != new_content:
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Updated {filepath}")

for filepath in glob.glob("app/src/main/java/com/example/hindilearn/ui/gamified/*.kt"):
    replace_in_file(filepath)
