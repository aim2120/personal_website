import os
from jinja2 import Template
from PIL import Image

# get templates and components from files
template_string = open("template.html", 'r').read()
artwork_template_string = open("artwork.html", 'r').read()
index_content_template_string = open("index_content.html", 'r').read()
contact_content_html_string = open("contact_content.html", 'r').read()

# create templates
template = Template(template_string)
artwork_template = Template(artwork_template_string)
index_content_template = Template(index_content_template_string)

# get artwork files
artwork_files = []
artwork_dir = os.getcwd() + '/images/artwork'
for filename in os.listdir(artwork_dir):
    if filename.endswith('.jpg'):
        artwork_files.append(filename)

artwork_files.sort()

# build the artworks html string
artworks_html_string = ''
artwork_contexts = []
for artwork_file in artwork_files:
    artwork_image = Image.open(artwork_dir + '/' + artwork_file)
    artwork_info = artwork_file.split('.')[0].split('_')
    artwork_dims = artwork_info[4].split('x')
    artwork_height = artwork_dims[0].split('in')[0]
    artwork_width = artwork_dims[1].split('in')[0]
    artwork_context = dict(
        filename = artwork_file,
        title = ' '.join(artwork_info[2].split('-')),
        medium = ' '.join(artwork_info[3].split('-')),
        dimensions = artwork_info[4],
        height = artwork_height,
        width = artwork_width,
        year = artwork_info[5],
        file_size = {
            'height': artwork_image.height,
            'width': artwork_image.width
        }
    )
    artwork_contexts.append(artwork_context)

artwork_contexts_sorted = sorted(artwork_contexts, key=lambda d: (d['year'], d['height'], d['width'], d['title']), reverse=True)

for artwork_context in artwork_contexts_sorted:
    artworks_html_string += artwork_template.render(**artwork_context)
    artworks_html_string += '\n'


# create index file using artworks html string
index_content_context =  dict(
    artwork_content = artworks_html_string
)
index_content_html_string = index_content_template.render(**index_content_context)

# create index & art contexts for top-level template
index_context = dict(
    content = contact_content_html_string,
    title = "Contact"
)
art_context = dict(
    content = index_content_html_string,
    title = "Art"
)

# create the top-level html strings
index_html_string = template.render(**index_context)
art_html_string = template.render(**art_context)

print(index_html_string)
print(art_html_string)

# write the top-level html strings to files
index_output = open('index.html', 'w')
index_output.write(index_html_string)
index_output.close()

art_output = open('art.html', 'w')
art_output.write(art_html_string)
art_output.close()
