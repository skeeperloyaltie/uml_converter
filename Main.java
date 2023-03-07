import java.io.File;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <uml_file>");
            System.exit(1);
        }

        String fileName = args[0];

        try {
                        
            // Parse the UML file
            File umlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(umlFile);
            System.out.println("Parsed pseudo code:");
            System.out.println(doc);

            // Convert the UML file to pseudo-code
            StringBuilder pseudoCode = new StringBuilder();
            NodeList classNodes = doc.getElementsByTagName("UML:Class");
            for (int i = 0; i < classNodes.getLength(); i++) {
                Node classNode = classNodes.item(i);
                String className = ((Element) classNode).getAttribute("name");
                pseudoCode.append("class " + className + " {\n");

                NodeList attributeNodes = ((Element) classNode).getElementsByTagName("UML:Attribute");
                for (int j = 0; j < attributeNodes.getLength(); j++) {
                    Node attributeNode = attributeNodes.item(j);
                    String attributeName = ((Element) attributeNode).getAttribute("name");
                    String attributeType = ((Element) attributeNode).getAttribute("type");
                    pseudoCode.append("\t" + attributeType + " " + attributeName + ";\n");
                }

                NodeList methodNodes = ((Element) classNode).getElementsByTagName("UML:Operation");
                for (int j = 0; j < methodNodes.getLength(); j++) {
                    Node methodNode = methodNodes.item(j);
                    String methodName = ((Element) methodNode).getAttribute("name");
                    String returnType = ((Element) methodNode).getAttribute("returnType");
                    pseudoCode.append("\t" + returnType + " " + methodName + "(");

                    NodeList parameterNodes = ((Element) methodNode).getElementsByTagName("UML:Parameter");
                    for (int k = 0; k < parameterNodes.getLength(); k++) {
                        Node parameterNode = parameterNodes.item(k);
                        String parameterName = ((Element) parameterNode).getAttribute("name");
                        String parameterType = ((Element) parameterNode).getAttribute("type");
                        pseudoCode.append(parameterType + " " + parameterName);
                        if (k < parameterNodes.getLength() - 1) {
                            pseudoCode.append(", ");
                        }
                    }

                    pseudoCode.append(") {\n");
                    pseudoCode.append("\t\t// Method body goes here\n");
                    pseudoCode.append("\t}\n");
                }

                pseudoCode.append("}\n");
            }

            // Write the pseudo-code to the output file
            File outputFile = new File("pseudo_.txt");
            FileWriter writer = new FileWriter(outputFile);
            writer.write(pseudoCode.toString());
            writer.flush(); // flush the buffer
            writer.close(); // close the file
        } catch (Exception e) {
            System.err.println("Error parsing UML file: " + e.getMessage());
            System.exit(1);
        }
    }
}
