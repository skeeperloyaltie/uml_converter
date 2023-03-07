import xml.etree.ElementTree as ET

# Parse the XML file
tree = ET.parse('ds.xml')
root = tree.getroot()

# Identify the classes and attributes
classes = {}
for child in root:
    if child.tag == '{http://www.w3.org/2003/11/xmldrawing}mxCell':
        if child.get('style', '').startswith('text;align=center;verticalAlign=middle;fontStyle=0;strokeWidth=0;fillColor=none;spacing=0;'):
            class_name = child.get('value')
            classes[class_name] = []
        elif child.get('style', '').startswith('text;align=left;verticalAlign=middle;fontStyle=0;strokeWidth=0;fillColor=none;spacing=0;'):
            attr_name = child.get('value')
            classes[class_name].append(attr_name)

# Print the classes and attributes
for class_name, attrs in classes.items():
    print(class_name)
    for attr_name in attrs:
        print(f'  - {attr_name}')
