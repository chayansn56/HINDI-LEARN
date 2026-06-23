from PIL import Image

def make_transparent(input_path, output_path):
    img = Image.open(input_path).convert("RGBA")
    datas = img.getdata()
    
    new_data = []
    for item in datas:
        # If the pixel is near white, make it transparent
        if item[0] > 230 and item[1] > 230 and item[2] > 230:
            new_data.append((255, 255, 255, 0))
        else:
            new_data.append(item)
            
    img.putdata(new_data)
    
    # Trim transparent borders
    bbox = img.getbbox()
    if bbox:
        img = img.crop(bbox)
        
    img.save(output_path, "PNG")
    print(f"Saved transparent logo to {output_path}")

if __name__ == "__main__":
    make_transparent(
        "/Users/chayansoni/.gemini/antigravity/brain/a866ac48-690b-402f-bb7f-d7b594b84786/media__1781778337334.jpg",
        "/Users/chayansoni/.gemini/antigravity/scratch/HindiLearn/app/src/main/res/drawable/vietana_logo.png"
    )
