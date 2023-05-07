package utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

public class PropertyReaderWriter extends DataDriver {

    public static Properties properties;

    /**
     * Write property in property file
     *
     * @param filePath      file Path
     * @param propertyKey   property key
     * @param propertyValue property value
     * @param comment       comment
     */
    public static void writeProperty(String filePath, String propertyKey, String propertyValue, String comment) {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            properties.load(fileInputStream);
            properties.setProperty(propertyKey, propertyValue);
            FileOutputStream output = new FileOutputStream(filePath);
            properties.store(output, comment);
            output.close();
            fileInputStream.close();
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Wrote property with '" + propertyKey + "' key and '" + propertyValue + "' value");
        } catch (Exception e) {
            e.printStackTrace();
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Failed to write property with '" + propertyKey + "' and '" + propertyValue + "' in '" + filePath + "' file, due to\n" + e.getMessage());
        }
    }

    /**
     * Read property from property file
     *
     * @param filePath    file path
     * @param propertyKey property key
     * @return property value
     */
    public static String readProperty(String filePath, String propertyKey) {
        String propertyValue = "";
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            properties.load(fileInputStream);
            String retrievedValue = properties.getProperty(propertyKey);
            fileInputStream.close();
            if (retrievedValue != null) {
                propertyValue = retrievedValue.trim();
            }
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Property key is '" + propertyKey + "' and corresponding value is '" + propertyValue + "'");
        } catch (Exception e) {
            e.printStackTrace();
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Failed to read property with '" + propertyKey + "' key from '" + filePath + "' file, due to\n" + e.getMessage());
        }
        return propertyValue;
    }

    /**
     * Write test configuration properties in properties file
     */
    public void writeTestConfigurationProperties() {
        readSheet(getExcelFilePath(Common.testConfigurationsExcelFile), Common.suiteConfigurationsSheet);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            writeProperty(Common.testConfigurationsPropertiesFilePath, getCellData(i, 1), getCellData(i, 2), "");
        }
        closeWorkbook();
        Common.maxNoOfFailurePerTestCase = Integer.parseInt(Common.getTestConfigurationProperty("MaxNoOfFailurePerTestCase"));
    }

    /**
     * Write test case settings in xml file
     */
    public void writeTestCaseSettings() {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            String sheetName = Common.getTestConfigurationProperty("Test Suite");
            Element suiteElement = document.createElement("suite");
            document.appendChild(suiteElement);
            suiteElement.setAttribute("name", sheetName);
            suiteElement.setAttribute("verbose", "1");

            Element browserName = document.createElement("parameter");
            browserName.setAttribute("name", "browserName");
            browserName.setAttribute("value", Common.getTestConfigurationProperty("Browser Name"));
            suiteElement.appendChild(browserName);

            Element testElement = document.createElement("test");
            testElement.setAttribute("name", sheetName.replace("_Suite", ""));
            testElement.setAttribute("preserve-order", "true");
            suiteElement.appendChild(testElement);

            Element classesElement = document.createElement("classes");
            testElement.appendChild(classesElement);

            readTestCaseDetails(sheetName);

            if (Common.testCasesWithExecutionFlag.containsValue("Yes")) {
                Element classElement = document.createElement("class");
                classElement.setAttribute("name", "mappings.TestNGMappings");
                classesElement.appendChild(classElement);

                Element testMethodsElement = document.createElement("methods");
                classElement.appendChild(testMethodsElement);

                for (Map.Entry<String, String> entry : Common.testCasesWithExecutionFlag.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase("yes")) {
                        Element includeTestMethodsElement = document.createElement("include");
                        testMethodsElement.appendChild(includeTestMethodsElement);
                        includeTestMethodsElement.setAttribute("name", entry.getKey());
                        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Test case :" + entry.getKey() + "' included for current execution");
                    }
                    if (entry.getValue().equalsIgnoreCase("")) {
                        LogWriter.writeToLogFile(LogWriter.logLevel.WARN, "Invalid/Empty Execution flag provided in excel file for :" + entry.getKey() + "' test case");
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "https://testng.org/testng-1.0.dtd");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource xmlDOMSource = new DOMSource(document);
            StreamResult outputTarget = new StreamResult(Common.testNGXmlFilePath);
            transformer.transform(xmlDOMSource, outputTarget);
        } catch (Exception e) {
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Failed to write in testng.xml due to " + e.getMessage());
        }
    }

}
