import os
import shutil
from jinja2 import Template
from PIL import Image

BUILD_DIR = "build"
ASSETS_DIR = "assets"
BUILD_ASSETS_DIR = os.path.join(BUILD_DIR, "assets")
SOURCE_ARTWORK_DIR = os.path.join(ASSETS_DIR, "images", "artwork")
BUILD_ARTWORK_DIR = os.path.join(BUILD_ASSETS_DIR, "images", "artwork")
BUILD_OPTIMIZED_DIR = os.path.join(BUILD_ARTWORK_DIR, "optimized")

# get templates and components from files
template_string = open("template.html", "r").read()
artwork_template_string = open("artwork.html", "r").read()
index_content_template_string = open("index_content.html", "r").read()
home_content_template_string = open("home_content.html", "r").read()
about_content_html_string = open("about_content.html", "r").read()

# create templates
template = Template(template_string)
artwork_template = Template(artwork_template_string)
index_content_template = Template(index_content_template_string)
home_content_template = Template(home_content_template_string)

# prepare build output directories
if os.path.exists(BUILD_DIR):
    shutil.rmtree(BUILD_DIR)
os.makedirs(BUILD_DIR)

# copy static assets into build
shutil.copytree(ASSETS_DIR, BUILD_ASSETS_DIR)
shutil.copy(".htaccess", BUILD_DIR)
os.makedirs(BUILD_OPTIMIZED_DIR, exist_ok=True)

# get artwork files
artwork_files = []
for filename in os.listdir(SOURCE_ARTWORK_DIR):
    if filename.endswith(".jpg"):
        artwork_files.append(filename)

artwork_files.sort()

# build the artworks html string
artworks_html_string = ""
artwork_contexts = []
for artwork_file in artwork_files:
    source_path = os.path.join(SOURCE_ARTWORK_DIR, artwork_file)
    artwork_image = Image.open(source_path)

    # create optimized version in build
    optimized_path = os.path.join(BUILD_OPTIMIZED_DIR, artwork_file)
    if artwork_image.width > 1200:
        ratio = 1200 / artwork_image.width
        new_size = (1200, int(artwork_image.height * ratio))
        optimized_image = artwork_image.resize(new_size, Image.Resampling.LANCZOS)
        optimized_image.save(optimized_path, "JPEG", quality=85, optimize=True)
    else:
        artwork_image.save(optimized_path, "JPEG", quality=85, optimize=True)

    # get optimized image for dimensions
    optimized_image = Image.open(optimized_path)

    artwork_info = artwork_file.split(".")[0].split("_")
    artwork_dims = artwork_info[4].split("x")
    artwork_height = artwork_dims[0].split("in")[0]
    artwork_width = artwork_dims[1].split("in")[0]
    artwork_context = dict(
        filename="optimized/" + artwork_file,
        original_filename=artwork_file,
        title=" ".join(artwork_info[2].split("-")),
        medium=" ".join(artwork_info[3].split("-")),
        dimensions=artwork_info[4],
        height=artwork_height,
        width=artwork_width,
        year=artwork_info[5],
        file_size={
            "height": optimized_image.height,
            "width": optimized_image.width,
        },
    )
    artwork_contexts.append(artwork_context)

artwork_contexts_sorted = sorted(
    artwork_contexts,
    key=lambda d: (d["year"], d["height"], d["width"], d["title"]),
    reverse=True,
)

for artwork_context in artwork_contexts_sorted:
    artworks_html_string += artwork_template.render(**artwork_context)
    artworks_html_string += "\n"


# create index file using artworks html string
index_content_context = dict(artwork_content=artworks_html_string)
index_content_html_string = index_content_template.render(**index_content_context)

# create home content using about html string
home_content_html_string = home_content_template.render(
    about_content=about_content_html_string
)

# create index & art contexts for top-level template
index_context = dict(content=home_content_html_string, title="Home", scripts=[])
art_context = dict(
    content=index_content_html_string,
    title="Art",
    scripts=["assets/resize_media.js"],
)

# create the top-level html strings
index_html_string = template.render(**index_context)
art_html_string = template.render(**art_context)

# write the top-level html strings to the build folder
with open(os.path.join(BUILD_DIR, "index.html"), "w") as index_output:
    index_output.write(index_html_string)

with open(os.path.join(BUILD_DIR, "art.html"), "w") as art_output:
    art_output.write(art_html_string)
