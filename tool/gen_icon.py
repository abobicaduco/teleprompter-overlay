from PIL import Image, ImageDraw
import os
import random

size = 1024
img = Image.new("RGBA", (size, size), (3, 1, 10, 255))
draw = ImageDraw.Draw(img)

cx, cy = size // 2, size // 2
for r in range(520, 0, -4):
    t = 1 - r / 520
    col = (
        int(30 + 93 * t),
        int(144 - 97 * t),
        int(255 - 65 * t),
        int(28 + 40 * (1 - t)),
    )
    draw.ellipse([cx - r, cy - r, cx + r, cy + r], fill=col)

rnd = random.Random(42)
for _ in range(120):
    x, y = rnd.randint(40, size - 40), rnd.randint(40, size - 40)
    if (x - cx) ** 2 + (y - cy) ** 2 > 380**2:
        s = rnd.randint(1, 3)
        a = rnd.randint(140, 255)
        draw.ellipse([x, y, x + s, y + s], fill=(232, 234, 246, a))

band = Image.new("RGBA", (size, size), (0, 0, 0, 0))
bd = ImageDraw.Draw(band)
bd.rounded_rectangle(
    [140, 310, 884, 714],
    radius=48,
    fill=(15, 15, 46, 210),
    outline=(30, 144, 255, 200),
    width=6,
)
for i, w in enumerate([0.55, 0.72, 0.48, 0.66, 0.40]):
    y = 380 + i * 52
    lw = int(420 * w)
    x0 = cx - lw // 2
    bd.rounded_rectangle(
        [x0, y, x0 + lw, y + 18],
        radius=9,
        fill=(232, 234, 246, 220 if i != 2 else 255),
    )
bd.rectangle([200, 500, 824, 506], fill=(178, 75, 243, 230))
img = Image.alpha_composite(img, band)

draw = ImageDraw.Draw(img)
for rr, aa in [(470, 90), (490, 50)]:
    draw.ellipse(
        [cx - rr, cy - rr, cx + rr, cy + rr],
        outline=(30, 144, 255, aa),
        width=4,
    )

out = r"c:\Users\carlo\dev\abobi\teleprompter-overlay\assets\icon"
os.makedirs(out, exist_ok=True)
img.save(os.path.join(out, "icon.png"))
fg = Image.new("RGBA", (size, size), (0, 0, 0, 0))
content = img.resize((760, 760), Image.Resampling.LANCZOS)
fg.paste(content, (132, 132), content)
fg.save(os.path.join(out, "icon_foreground.png"))
print("icons ok")
