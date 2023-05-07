package mappings;

import org.testng.Assert;
import org.testng.annotations.*;
import modules.Browser;
import pages.CurrencyExchangeCalculatorPage;
import pages.basePage;
import testSuites.CurrencyExchangeCalculator;
import utilities.*;

import java.lang.reflect.Method;

import com.aventstack.extentreports.testng.listener.ExtentITestListenerClassAdapter;

@Listeners({ExtentITestListenerClassAdapter.class})
public class TestNGMappings extends basePage{

    Browser browser = Browser.getInstance();
    Common common = new Common();

    /**
     * Setup to open browser and maximize
     *
     * @param browserName browserName
     */
    @BeforeClass
    @Parameters({"browserName"})
    public void preCondition(String browserName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Opening and maximizing '" + browserName + "' browser");
        browser.openBrowser(browserName);
        browser.maximize();
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Opened and maximized '" + browserName + "' browser");
    }

    /**
     * Post condition to close browser and rename extent report
     */
    @AfterClass
    @Parameters({"browserName"})
    public void postCondition(String browserName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Performing post condition activities");
        browser.quit();
        FilesUtil.renameFile(Common.outputDirectory + "/", "index.html", "Execution_Report_" + Common.reportGenerationTimeStamp + ".html");
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Performed post condition activities");
    }

    /**
     * Before specific test case execution
     */
    @BeforeMethod
    public void beforeSpecificTestCaseExecution() {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Deleting cookies before test case execution");
        browser.deleteCookies();
        browser.navigateTo("https://www.paysera.lt/v2/en-LT/fees/currency-conversion-calculator#/");
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Deleted cookies before test case execution");
    }

    /**
     * After specific test case execution
     */
    @AfterMethod
    public void afterSpecificTestCaseExecution() {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Updating report for '" + Common.executingTestCaseName.toString() + "' test case");
        Common.executingTestCaseName.setLength(0);
        Common.extentReports.flush();
        Common.totalFailureInCurrentTestCase = 0;
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Updated report for '" + Common.executingTestCaseName.toString() + "' test case");
    }

    /**
     * Log current test case status
     */
    public void logCurrentTestCaseStatus() {
        if (Common.totalFailureInCurrentTestCase > 0) {
            Assert.fail();
        }
    }

    /**
     * Prepare test data for current test case
     *
     * @param currentTestCaseMethodName Current test case method name
     */
    public void prepareTestData(String currentTestCaseMethodName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Preparing test data for '" + currentTestCaseMethodName + "' test case");
        Common.testDataList.clear();
        Common.currentTestCaseIterationCounter = 0;
        common.getTestCaseTestData(currentTestCaseMethodName);
        Common.executingTestCaseName.append(currentTestCaseMethodName);
        Common.extentTest = Common.extentReports.createTest(Common.executingTestCaseName.toString());
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Prepared test data for '" + currentTestCaseMethodName + "' test case");
    }

    /**
     * TC-01 : Validate if 'Buy' field is emptied after entering data in the 'Sell' field
     * TC-02 : Validate if 'Sell' field is emptied after entering data in the 'Buy' field
     *
     * @param currentTestCaseName current test case name
     */
    @Test
    public void TC_01(Method currentTestCaseName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Execution started for '" + currentTestCaseName.getName() + "' test case");
        prepareTestData(currentTestCaseName.getName());
        CurrencyExchangeCalculator currencyExchangeCalculator = new CurrencyExchangeCalculator();
        currencyExchangeCalculator.moveToElement(getElementBy(basePage.elementState.present, CurrencyExchangeCalculatorPage.dropdownLocator, true));
        currencyExchangeCalculator.waitForLoadingSpinner(60, 5);
        for (int i = 0; i < Common.testDataList.size(); i++) {
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Iteration started for  test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
            currencyExchangeCalculator.tC_01();

            common.changeExecutionCounter();
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Iteration Completed for test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
        }
        logCurrentTestCaseStatus();
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Execution completed for '" + currentTestCaseName.getName() + "' test case");
    }

    /**
     * TC-03 : Validate if rates are being updated based on the selected country from the country selection dropdown
     * TC-04 : Validate if currency option is being changed based on the selected country from the country selection dropdown
     *
     * @param currentTestCaseName current test case name
     */
    @Test
    public void TC_02(Method currentTestCaseName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Execution started for '" + currentTestCaseName.getName() + "' test case");
        prepareTestData(currentTestCaseName.getName());
        CurrencyExchangeCalculator currencyExchangeCalculator = new CurrencyExchangeCalculator();
        currencyExchangeCalculator.moveToElement(getElementBy(basePage.elementState.present, CurrencyExchangeCalculatorPage.dropdownLocator, true));
        currencyExchangeCalculator.waitForLoadingSpinner(60, 5);
        for (int i = 0; i < Common.testDataList.size(); i++) {
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Iteration started for  test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
            currencyExchangeCalculator.tC_02();

            common.changeExecutionCounter();
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Iteration Completed for test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
        }
        logCurrentTestCaseStatus();
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Execution completed for '" + currentTestCaseName.getName() + "' test case");
    }

    /**
     * TC-05 : Validate if the bank provider's exchange amount for selling (X) is lower than the amount, provided by Paysera (Y), then a text box is displayed, representing the loss (X-Y)
     *
     * @param currentTestCaseName current test case name
     */
    @Test
    public void TC_05(Method currentTestCaseName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Execution started for '" + currentTestCaseName.getName() + "' test case");
        prepareTestData(currentTestCaseName.getName());
        CurrencyExchangeCalculator currencyExchangeCalculator = new CurrencyExchangeCalculator();
        currencyExchangeCalculator.moveToElement(getElementBy(basePage.elementState.present, CurrencyExchangeCalculatorPage.dropdownLocator, true));
        currencyExchangeCalculator.waitForLoadingSpinner(60, 5);
        for (int i = 0; i < Common.testDataList.size(); i++) {
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Iteration started for  test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
            currencyExchangeCalculator.tC_05();

            common.changeExecutionCounter();
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Iteration Completed for test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
        }
        logCurrentTestCaseStatus();
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Execution completed for '" + currentTestCaseName.getName() + "' test case");
    }

    /**
     * TC-06 : Validate the 'Clear filter' button functionality
     *
     * @param currentTestCaseName current test case name
     */
    @Test
    public void TC_06(Method currentTestCaseName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Execution started for '" + currentTestCaseName.getName() + "' test case");
        prepareTestData(currentTestCaseName.getName());
        CurrencyExchangeCalculator currencyExchangeCalculator = new CurrencyExchangeCalculator();
        currencyExchangeCalculator.moveToElement(getElementBy(basePage.elementState.present, CurrencyExchangeCalculatorPage.dropdownLocator, true));
        currencyExchangeCalculator.waitForLoadingSpinner(60, 5);
        for (int i = 0; i < Common.testDataList.size(); i++) {
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Iteration started for  test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
            currencyExchangeCalculator.tC_06();

            common.changeExecutionCounter();
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Iteration Completed for test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
        }
        logCurrentTestCaseStatus();
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Execution completed for '" + currentTestCaseName.getName() + "' test case");
    }

    /**
     * TC-07 : Validate Paysera Amount column value according to the Paysera Rate column value by giving sell value
     * TC-13 : Validate round up decimal values under 'Paysera amount' column
     * TC-14 : Validate column names
     *
     * @param currentTestCaseName current test case name
     */
    @Test
    public void TC_07(Method currentTestCaseName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Execution started for '" + currentTestCaseName.getName() + "' test case");
        prepareTestData(currentTestCaseName.getName());
        CurrencyExchangeCalculator currencyExchangeCalculator = new CurrencyExchangeCalculator();
        currencyExchangeCalculator.moveToElement(getElementBy(basePage.elementState.present, CurrencyExchangeCalculatorPage.dropdownLocator, true));
        currencyExchangeCalculator.waitForLoadingSpinner(60, 5);
        for (int i = 0; i < Common.testDataList.size(); i++) {
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Iteration started for  test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
            currencyExchangeCalculator.tC_07();

            common.changeExecutionCounter();
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Iteration Completed for test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
        }
        logCurrentTestCaseStatus();
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Execution completed for '" + currentTestCaseName.getName() + "' test case");
    }

    /**
     * TC-08 : Validate warning message after giving invalid value in the 'Buy' field
     * TC-09 : Validate warning message after giving invalid value in the 'Sell' field
     * TC-10 : Validate if user is able to close the Invalid message
     * TC-11 : Validate input limit in 'Sell' field
     * TC-12 : Validate input limit in 'Buy' field
     *
     * @param currentTestCaseName current test case name
     */
    @Test
    public void TC_08(Method currentTestCaseName) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Execution started for '" + currentTestCaseName.getName() + "' test case");
        prepareTestData(currentTestCaseName.getName());
        CurrencyExchangeCalculator currencyExchangeCalculator = new CurrencyExchangeCalculator();
        currencyExchangeCalculator.moveToElement(getElementBy(basePage.elementState.present, CurrencyExchangeCalculatorPage.dropdownLocator, true));
        currencyExchangeCalculator.waitForLoadingSpinner(60, 5);
        for (int i = 0; i < Common.testDataList.size(); i++) {
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[Start]: Iteration started for  test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
            currencyExchangeCalculator.tC_08();

            common.changeExecutionCounter();
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Iteration Completed for test data set **'" + i + "'** of '" + currentTestCaseName.getName() + "' test case");
        }
        logCurrentTestCaseStatus();
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "[End]: Execution completed for '" + currentTestCaseName.getName() + "' test case");
    }
}

