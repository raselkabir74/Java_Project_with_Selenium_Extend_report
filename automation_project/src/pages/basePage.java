package pages;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import modules.Browser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.Common;
import utilities.LogWriter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class basePage extends Common {

    public enum identifierType {
        xpath, name, id, lnktext, partiallinktext, classname, cssselector, tagname
    }
    public enum elementState {
        present,
        clickable,
        visible,
    }
    public String identifier, locator, locatorDescription;
    Browser browser = Browser.getInstance();

    /**
     * Parse identify By and locator
     *
     * @param identifyByAndLocator identifyByAndLocator
     */
    public void parseidentifyByAndlocator(String identifyByAndLocator) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Parsing Locator: " + identifyByAndLocator);
        try {
            locatorDescription = identifyByAndLocator.substring(0, identifyByAndLocator.indexOf("#"));
            identifyByAndLocator = identifyByAndLocator.substring(identifyByAndLocator.indexOf("#") + 1);
            identifier = identifyByAndLocator.substring(0, identifyByAndLocator.indexOf("=")).toLowerCase();
            locator = identifyByAndLocator.substring(identifyByAndLocator.indexOf("=") + 1);
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Locator Description : " + locatorDescription);
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Identify Type: " + identifier);
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Locator: " + locator);
        } catch (Exception e) {
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Locator : '" + identifyByAndLocator + "' is not valid due to \n" + e.getMessage());
        }
    }

    /**
     * Form locator using By functionality
     *
     * @param fullLocator full Locator
     * @return formed locator using By
     */
    public By formLocatorBy(String fullLocator) {
        By formedLocator;
        parseidentifyByAndlocator(fullLocator);
        identifierType i = identifierType.valueOf(identifier);
        formedLocator = switch (i) {
            case xpath -> By.xpath(locator);
            case id -> By.id(locator);
            case name -> By.name(locator);
            case lnktext -> By.linkText(locator);
            case partiallinktext -> By.partialLinkText(locator);
            case classname -> By.className(locator);
            case cssselector -> By.cssSelector(locator);
            case tagname -> By.tagName(locator);
        };
        return formedLocator;
    }

    /**
     * Driver wait
     *
     * @param timeout timeout
     * @return WebDriverWait
     */
    public WebDriverWait driverWait(int timeout) {
        return browser.getWebDriverWait(timeout);
    }

    /**
     * Implementing navigate To URL functionality
     *
     * @param url URl for navigation
     */
    public void navigateToURL(String url) {
        browser.navigateTo(url);
        testStepInfo("Navigated to URL: " + url);
    }

    /**
     * Move to given element
     *
     * @param element element to hover
     */
    public void moveToElement(WebElement element) {
        browser.moveToElement(element);
    }

    /**
     * Get element By locator
     *
     * @param state                state
     * @param locator              locator
     * @param timeout              timeout
     * @param useDriverFindElement useDriverFindElement
     * @return WebElement
     */
    public WebElement getElementBy(elementState state, String locator, int timeout, boolean useDriverFindElement) {
        WebElement element = null;
        if (useDriverFindElement) {
            element = browser.getDriver().findElement(formLocatorBy(locator));
        } else {
            if (state.equals(elementState.visible))
                element = driverWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(formLocatorBy(locator))));
            if (state.equals(elementState.clickable))
                element = driverWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(formLocatorBy(locator))));
            if (state.equals(elementState.present))
                element = driverWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(formLocatorBy(locator))));
        }
        return element;
    }

    /**
     * Get element By locator
     *
     * @param state                state
     * @param locator              locator
     * @param useDriverFindElement useDriverFindElement
     * @return WebElement
     */
    public WebElement getElementBy(elementState state, String locator, boolean useDriverFindElement) {
        return getElementBy(state, locator, 0, useDriverFindElement);
    }

    /**
     * click on element
     *
     * @param locator locator
     */
    public void clickElement(String locator) {
        getElementBy(elementState.present, locator, 0, false).click();
        waitForLoadingSpinner(20, 3);
    }

    /**
     * click on element
     *
     * @param locator locator
     * @param value Input value
     */
    public void setValueIntoSpecificElement(String locator, String value) {
        WebElement element = null;
        element = getElementBy(elementState.present, locator, 0, false);
        element.clear();
        element.sendKeys(value);
    }

    /**
     * click on element
     *
     * @param locator locator
     */
    public String getTextFromSpecificElement(String locator) {
        WebElement element = null;
        String text;
        element = getElementBy(elementState.present, locator, 0, false);
        text = element.getText();
        return text;
    }

    /**
     * Wait for loading spinner visibility in application
     *
     * @param timeout        time out
     */
    public void waitForLoadingSpinner(long timeout) {
        List<WebElement> spinner_control;
        String loadingSpinner = "Loading spinner locator#xpath=//img[@class=' ls-is-cached lazyloaded']";
        try {
            WebDriverWait wait = driverWait(5);
            spinner_control  = wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(formLocatorBy(loadingSpinner))));
            long end_time = System.currentTimeMillis() + timeout*1000;
            while (spinner_control.size() > 0) {
                spinner_control  = wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(formLocatorBy(loadingSpinner))));
                if (System.currentTimeMillis() > end_time) {
                    break;
                }
            }
        }
        catch(Exception e) {
            //Not handling any exception
        }
    }

    /**
     * Wait for loading spinner visibility in application
     *
     * @param timeout Time out
     * @param waitForLoadingSpinner Wait For Loading Spinner
     */
    public void waitForLoadingSpinner(long timeout, int waitForLoadingSpinner) {
        List<WebElement> spinner_control;
        String loadingSpinner = "Loading spinner locator#xpath=//img[@class=' ls-is-cached lazyloaded']";
        try {
            WebDriverWait wait = driverWait(waitForLoadingSpinner);
            spinner_control  = wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(formLocatorBy(loadingSpinner))));
            long end_time = System.currentTimeMillis() + timeout*1000;
            while (spinner_control.size() > 0) {
                spinner_control  = wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(formLocatorBy(loadingSpinner))));
                if (System.currentTimeMillis() > end_time) {
                    break;
                }
            }
        }
        catch(Exception e) {
            //Not handling any exception
        }
    }

    /**
     * Get random string
     *
     * @param lengthOfString Length of random string
     * @return randomString
     */
    public String getRandomString(int lengthOfString) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder(lengthOfString);
        for (int i = 0; i < lengthOfString; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            randomString.append(AlphaNumericString.charAt(index));
        }
        return randomString.toString().toUpperCase();
    }

    /**
     * Get element status By locator
     *
     * @param state                state
     * @param locator              locator
     * @param timeout              timeout
     * @param useDriverFindElement useDriverFindElement
     * @return element status
     */
    public boolean getElementStatusBy(elementState state, String locator, int timeout, boolean useDriverFindElement) {
        boolean elementStatus = false;
        try {
            if (useDriverFindElement) {
                browser.getDriver().findElement(formLocatorBy(locator));
            } else {
                if (state.equals(elementState.visible))
                    driverWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(formLocatorBy(locator))));
                if (state.equals(elementState.clickable))
                    driverWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(formLocatorBy(locator))));
                if (state.equals(elementState.present))
                    driverWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(formLocatorBy(locator))));
            }
            elementStatus = true;
        } catch (WebDriverException ignored) {

        } catch (Exception e) {
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Failed to get Status of '" + locator + " locator due to \n" + e.getMessage());
        }
        return elementStatus;
    }

    /**
     * Get element status By locator
     *
     * @param state                state
     * @param locator              locator
     * @param useDriverFindElement useDriverFindElement
     * @return element status
     */
    public boolean getElementStatusBy(elementState state, String locator, boolean useDriverFindElement) {
        return getElementStatusBy(state, locator, 0, useDriverFindElement);
    }

    /**
     * Verify element is exist in page
     *
     * @param objectLocator object locator
     * @return true/false depending on element present
     */
    public boolean isElementExist(String objectLocator) {
        return getElementStatusBy(elementState.present, objectLocator, true);
    }

    /**
     * assertEquals with boolean for pass fail
     *
     * @param actualValue   actualValue
     * @param expectedValue expectedValue
     * @param message       message
     */
    public void assertEquals(boolean actualValue, boolean expectedValue, String message) {
        if (Objects.equals(actualValue, expectedValue)) {
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, message + ". Actual value: '" + actualValue + "' is same as expected value: '" + expectedValue + "'");
            testStepPassed("Actual value: '" + actualValue + "' is same as expected value: '" + expectedValue + "'");
        }
        else {
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Actual value: '" + actualValue + "' is not same as expected value: '" + expectedValue + "'");
            testStepFailed("Actual value: '" + actualValue + "' is not same as expected value: '" + expectedValue + "'. " + message + "");
        }
    }

    /**
     * Get current method name
     *
     * @return current method name
     */
    public static String getMethodName() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        return stackTrace[1].getMethodName();
    }

    /**
     * Test step passed
     *
     * @param passMessage Pass Message
     */
    public void testStepPassed(String passMessage) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, passMessage);
        extentTest.log(Status.PASS, MarkupHelper.createLabel("[PASSED] " + passMessage, ExtentColor.GREEN));
    }

    /**
     * Test step failed
     *
     * @param failedMessage failed Message
     */
    public void testStepFailed(String failedMessage) {
        try {
            totalFailureInCurrentTestCase ++;
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, failedMessage);
            browser.captureScreenShot();
            extentTest.log(Status.FAIL, MarkupHelper.createLabel("[FAILURE] " + failedMessage, ExtentColor.RED));
            extentTest.fail("Screenshot of failure", MediaEntityBuilder.createScreenCaptureFromBase64String(encodeFileToBase64String(currentScreenShotPath.toString())).build());

            if(getTestConfigurationProperty("ExecuteRemainingStepsOnFailure(Yes/No)").equalsIgnoreCase("No")){
                LogWriter.writeToLogFile(LogWriter.logLevel.FATAL, "ExecuteRemainingStepsOnFailure value was set to 'No', hence stopping the execution");
                extentTest.log(Status.FATAL, "ExecuteRemainingStepsOnFailure value was set to 'No', hence stopping the execution");
                Assert.fail("ExecuteRemainingStepsOnFailure value was set to 'No', hence stopping the execution");
                }
            if(totalFailureInCurrentTestCase >= maxNoOfFailurePerTestCase){
                LogWriter.writeToLogFile(LogWriter.logLevel.FATAL, "MaxNoOfFailurePerTestCase value was set to '" + maxNoOfFailurePerTestCase + "', hence stopping the execution");
                extentTest.log(Status.FATAL, "MaxNoOfFailurePerTestCase value was set to '" + maxNoOfFailurePerTestCase + "', hence stopping the execution");
                Assert.fail("MaxNoOfFailurePerTestCase value was set to '" + maxNoOfFailurePerTestCase + "', hence stopping the execution");
            }
        } catch (Exception e) {
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Failed to add screenshot in extend report due to\n" + getStackTrackAsString(e));
        } finally {
            currentScreenShotPath.setLength(0);
        }
    }

    /**
     * Test step information
     *
     * @param infoMessage Info Message
     */
    public void testStepInfo(String infoMessage) {
        LogWriter.writeToLogFile(LogWriter.logLevel.INFO, infoMessage);
        extentTest.log(Status.INFO,  infoMessage);
    }

    /**
     * Encode file to base64 string
     *
     * @return Base64 string
     */
    public String encodeFileToBase64String(String filePath) {
        String base64String = "";
        try{
            base64String =  Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(filePath)));
        }catch (Exception e){
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Failed to convert screenshot to Base64 string due to \n'" + getStackTrackAsString(e));
        }
        return base64String;
    }

    /**
     * Get Current Web Driver
     *
     * @return Web driver
     */
    public WebDriver getCurrentWebDriver() {
        return browser.getDriver();
    }
}
