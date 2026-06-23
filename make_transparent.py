from PIL import Image

def make_transparent(image_path, output_path):
    try:
        img = Image.open(image_path)
        img = img.convert("RGBA")
        
        data = img.getdata()
        new_data = []
        
        for item in data:
            if item[0] > 240 and item[1] > 240 and item[2] > 240:
                new_data.append((255, 255, 255, 0))
            else:
                new_data.append(item)
                
        img.putdata(new_data)
        img.save(output_path, "PNG")
        print("Logo made transparent.")
    except Exception as e:
        print(f"Error: {e}")

logo_path = "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/res/drawable/vietana_logo.png"
make_transparent(logo_path, logo_path)
