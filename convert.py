import sys
import xml.etree.ElementTree as ET

class UMLParser:
    def __init__(self, inputFile):
        self.inputFile = inputFile
    
    def parse(self):
        try:
            tree = ET.parse(self.inputFile)
            root = tree.getroot()
            return self.parseNode(root)
        except Exception as e:
            print("Error parsing UML file:", str(e))
            return None

    def parseNode(self, node):
        if node.tag == "object":
            name = node.get("name")
            if name is None:
                name = "Unknown"
            type = node.get("type")
            if type == "UMLClass":
                return self.parseClass(node, name)
            elif type == "UMLInterface":
                return self.parseInterface(node, name)
        return ""

    def parseClass(self, node, name):
        code = f"class {name} {{\n"
        for child in node:
            if child.tag == "mxCell":
                continue
            if child.tag == "Attribute":
                code += f"\t{child.get('name')}: {child.get('type')}\n"
            elif child.tag == "Method":
                params = child.find("Parameter")
                paramStr = ""
                if params is not None:
                    for param in params:
                        paramStr += f"{param.get('name')}: {param.get('type')}, "
                    paramStr = paramStr[:-2]
                code += f"\t{child.get('name')}({paramStr})\n"
        code += "}"
        return code

    def parseInterface(self, node, name):
        code = f"interface {name} {{\n"
        for child in node:
            if child.tag == "mxCell":
                continue
            if child.tag == "Operation":
                params = child.find("Parameter")
                paramStr = ""
                if params is not None:
                    for param in params:
                        paramStr += f"{param.get('name')}: {param.get('type')}, "
                    paramStr = paramStr[:-2]
                code += f"\t{child.get('name')}({paramStr})\n"
        code += "}"
        return code

def main(args):
    try:
        filename = args[0]
        inputFile = open(filename)
        parser = UMLParser(inputFile)
        pseudoCode = parser.parse()

        print("Parsed pseudo code:")
        print(pseudoCode)

        with open("pseudo.txt", "w") as outfile:
            if pseudoCode is not None:
                outfile.write(pseudoCode)
                print("Successfully wrote pseudo code to file")
            else:
                print("Failed to parse UML file")
    except Exception as e:
        print(str(e))

if __name__ == "__main__":
    main(sys.argv[1:])
