package utilities;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.*;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.base.Throwables;
import org.apache.log4j.Logger;

import java.util.*;

public class Common extends DataDriver {

    public static Logger logger;
    public static String reportGenerationTimeStamp;
    public static String outputDirectory;
    public static String testConfigurationsExcelFile = "TestConfigurations.xlsx";
    public static String testCaseSettingsExcelFile = "TestCaseSettings.xlsx";
    public static String testDataExcelFile = "TestData.xlsx";
    public static String dateFormat = "MM/dd/yyyy";
    public static String suiteConfigurationsSheet = "Suite_Configurations";
    public static String testConfigurationsPropertiesFilePath = "./Configurations/TestConfigurations.properties";
    public static String testNGXmlFilePath = "./Configurations/testng.xml";
    public static LinkedHashMap<String, String> testCasesWithExecutionFlag = new LinkedHashMap<>();
    public static LinkedHashMap<String, String> testCasesWithDescription = new LinkedHashMap<>();
    public static ArrayList<Dictionary<String, String>> testDataList = new ArrayList<>();
    public static int currentTestCaseIterationCounter = 0;
    public static StringBuilder executingTestCaseName = new StringBuilder();
    public static ExtentReports extentReports;
    public static ExtentTest extentTest;
    public static StringBuilder currentScreenShotPath = new StringBuilder();
    public static ExtentSparkReporter extentSparkReporter;
    public static int maxNoOfFailurePerTestCase;
    public static int totalFailureInCurrentTestCase;
    public static int defaultTimeOut = 0;
    /**
     * Get test configuration property from property file
     *
     * @param propertyKey Property key to retrieve
     * @return Property value
     */
    public static String getTestConfigurationProperty(String propertyKey) {
        return PropertyReaderWriter.readProperty(testConfigurationsPropertiesFilePath, propertyKey);
    }

    /**
     * Perform start up activity
     */
    public void performStartUpActivity() {
        LogWriter logWriter = new LogWriter();
        PropertyReaderWriter propertyReaderWriter = new PropertyReaderWriter();
        logWriter.generateOutputDirectory();
        logWriter.createLogFile();
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Startup activity completed");
        propertyReaderWriter.writeTestConfigurationProperties();
        propertyReaderWriter.writeTestCaseSettings();
        initializeVariables();
    }

    /**
     * Retrieve test data
     *
     * @param columnName Column name of TestData.xlsx file for test data retrieval
     * @return column data
     */
    public String retrieveTestData(String columnName) {
        return Common.testDataList.get(currentTestCaseIterationCounter).get(columnName);
    }

    /**
     * Initialize extent report
     */
    public void initializeExtentReport() {
        extentSparkReporter = new ExtentSparkReporter(outputDirectory + "/");
        extentSparkReporter.config().enableTimeline(true);
        extentSparkReporter.config().enableOfflineMode(false);
        extentSparkReporter.config().setCSS("css-string");
        extentSparkReporter.config().setDocumentTitle("Regular_Test_Execution_Report");
        extentSparkReporter.config().setEncoding("utf-8");
        extentSparkReporter.config().setJS("js-string");
        extentSparkReporter.config().setProtocol(Protocol.HTTPS);
        extentSparkReporter.config().setReportName(getTestConfigurationProperty("Test Suite"));
        extentSparkReporter.config().setTheme(Theme.DARK);
        extentSparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        extentSparkReporter.config().setReportName("Execution Report");
        extentReports = new ExtentReports();
        extentReports.attachReporter(extentSparkReporter);
        extentReports.setAnalysisStrategy(AnalysisStrategy.TEST);
        extentReports.setSystemInfo("Execute Remaining Steps On Failure", getTestConfigurationProperty("ExecuteRemainingStepsOnFailure(Yes/No)"));
        extentReports.setSystemInfo("Max Number of Failures Per Test Case", getTestConfigurationProperty("MaxNoOfFailurePerTestCase"));
        extentReports.setSystemInfo("Test Data Sheet Used From TestData.xlsx File", getTestConfigurationProperty("TestDataSheetName"));
    }

    /**
     * Change execution counter
     */
    public void changeExecutionCounter(){
        currentTestCaseIterationCounter ++;
    }

    /**
     * Initialize variables
     */
    public void initializeVariables(){
        defaultTimeOut = Integer.parseInt(Common.getTestConfigurationProperty("DefaultTimeOut"));
    }

    /**
     * Get stack track as string
     *
     * @param exception exception
     * @return Stack track as string
     */
    public static String getStackTrackAsString(Exception exception){
        return Throwables.getStackTraceAsString(exception);
    }
}
