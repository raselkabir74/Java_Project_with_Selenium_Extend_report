package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CurrencyExchangeCalculatorPage extends basePage {

    //[Start]: Locators
    public static String dropdownLocator = "Dropdown locator#xpath=//span[@class='js-localization-popover']";
    //[End]: Locators

    //[Start]: Input Field Names
    public static final String sellFieldName = "Sell";
    public static final String buyFieldName = "Buy";
    //[End]: Input Field Names

    //[Start]: Column Names
    public static final String currencyColumnName = "Currency";
    public static final String officialRateColumnName = "Official rate";
    public static final String payseraRateColumnName = "Paysera rate";
    public static final String payseraAmountColumnName = "Paysera amount";
    //[End]: Column Names

    //[Start]: Button Names
    public static final String filterButtonName = "Filter";
    public static final String clearFilterButtonName = "Clear filter";
    public static final String closeButtonName = "×";
    //[End]: Button Names

    //[Start]: Message text
    public static final String invalidParametersMessageText = "Invalid parameters";
    //[End]: Message text

    /**
     * Constructor to initialize page factory elements
     */
    public CurrencyExchangeCalculatorPage() {
        AjaxElementLocatorFactory ajaxElementLocatorFactory = new AjaxElementLocatorFactory(getCurrentWebDriver(), defaultTimeOut);
        PageFactory.initElements(ajaxElementLocatorFactory, this);
    }

    /**
     * Set value into the Sell field
     *
     * @param inputText Input Text
     */
    public void setValueIntoSellField(String inputText){
        testStepInfo("[Start]: "+getMethodName());
        String sellInputField = "Sell input field locator#xpath=//label[normalize-space()= 'Sell']/..//input";
        try {
            moveToElement(getElementBy(elementState.present, sellInputField, true));
            setValueIntoSpecificElement(sellInputField, inputText);
            testStepPassed("Successfully entered value in the Sell field");
        } catch (Exception e) {
            testStepFailed("Failed to enter value in the Sell field due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
    }

    /**
     * Set value into the Buy field
     *
     * @param inputText Input Text
     */
    public void setValueIntoBuyField(String inputText){
        testStepInfo("[Start]: "+getMethodName());
        String buyInputField = "Sell input field locator#xpath=//label[normalize-space()= 'Buy']/..//input";
        try {
            setValueIntoSpecificElement(buyInputField, inputText);
            testStepPassed("Successfully entered value in the Sell field");
        } catch (Exception e) {
            testStepFailed("Failed to enter value in the Sell field due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
    }

    /**
     * Get text from specific input field
     *
     * @return Field value
     */
    public String getTextFromSpecificInputField(String fieldName){
        testStepInfo("[Start]: "+getMethodName());
        String fieldValue = "",
                buyInputField = "Input field locator#xpath=//label[normalize-space()= '" + fieldName + "']/..//input";
        try {
            fieldValue = getElementBy(elementState.present, buyInputField, false).getAttribute("value");
            testStepInfo("Successfully retrieved value from the '" + fieldName + "' field");
        } catch (Exception e) {
            testStepFailed("Failed to retrieve value from  the '" + fieldName + "' field due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
        return fieldValue;
    }

    /**
     * Get specific grid column value based on the Currency column value
     *
     * @param columnName Column name
     * @param currencyColumnValue Currency column value
     * @return Column value
     */
    public String getSpecificGridColumnValueBasedOnTheCurrencyColumnValue(String columnName, String currencyColumnValue){
        testStepInfo("[Start]: "+getMethodName());
        List<WebElement> allColumnCount, allCurrencyColumnValues;
        int columnIndex = 0, trIndex = 0;
        String columnValue = "", columnValueLocator,
         allColumnLocator = "All column locator#xpath=//div[@id= 'currency-exchange-app']//table//thead/tr/th",
         currencyColumnValueLocator = "Currency column value locator#xpath=//div[@id= 'currency-exchange-app']//table//tbody/tr/td[1]";
        try {
            WebDriverWait wait = driverWait(5);
            allColumnCount = wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(formLocatorBy(allColumnLocator))));
            allCurrencyColumnValues = wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(formLocatorBy(currencyColumnValueLocator))));
            for (int iteration = 0; iteration < allColumnCount.size(); iteration++){
                if (allColumnCount.get(iteration).getText().trim().equalsIgnoreCase(columnName.trim())){
                    columnIndex = iteration + 1;
                    break;
                }
            }
            for (int iteration = 0; iteration < allColumnCount.size(); iteration++){
                if (allCurrencyColumnValues.get(iteration).getText().trim().toLowerCase().contains(currencyColumnValue.toLowerCase().trim())){
                    trIndex = iteration + 1;
                    break;
                }
            }
            columnValueLocator = "Column value locator#xpath=//div[@id='currency-exchange-app']//table//tbody/tr[" + trIndex + "]//td[" + columnIndex + "] |" +
                    " //div[@id='currency-exchange-app']//table//tbody/tr[" + trIndex + "]//td["+ columnIndex + "]//span[@class='ng-binding']";
            moveToElement(getElementBy(elementState.present, columnValueLocator, true));
            columnValue = getTextFromSpecificElement(columnValueLocator);
        } catch (Exception e) {
            testStepFailed("Failed to retrieve value from  the '" + columnName + "' column due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
        return columnValue;
    }

    /**
     * Get specific grid column loss amount based on the Currency column value
     *
     * @param columnName Column name
     * @param currencyColumnValue Currency column value
     * @return Column value
     */
    public String getSpecificGridColumnLossAmountBasedOnTheCurrencyColumnValue(String columnName, String currencyColumnValue){
        testStepInfo("[Start]: "+getMethodName());
        List<WebElement> allColumnCount, allCurrencyColumnValues;
        int columnIndex = 0, trIndex = 0;
        String columnValue = "", columnValueLocator,
                allColumnLocator = "All column locator#xpath=//div[@id= 'currency-exchange-app']//table//thead/tr/th",
                currencyColumnValueLocator = "Currency column value locator#xpath=//div[@id= 'currency-exchange-app']//table//tbody/tr/td[1]";
        try {
            WebDriverWait wait = driverWait(5);
            allColumnCount = wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(formLocatorBy(allColumnLocator))));
            allCurrencyColumnValues = wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(formLocatorBy(currencyColumnValueLocator))));
            for (int iteration = 0; iteration < allColumnCount.size(); iteration++){
                if (allColumnCount.get(iteration).getText().trim().equalsIgnoreCase(columnName.trim())){
                    columnIndex = iteration + 1;
                    break;
                }
            }
            for (int iteration = 0; iteration < allColumnCount.size(); iteration++){
                if (allCurrencyColumnValues.get(iteration).getText().trim().toLowerCase().contains(currencyColumnValue.toLowerCase().trim())){
                    trIndex = iteration + 1;
                    break;
                }
            }
            columnValueLocator = "Column value locator#xpath=//div[@id='currency-exchange-app']//table//tbody/tr[" + trIndex + "]//td["+ columnIndex + "]//span[@class='other-bank-loss ng-binding ng-scope']";
            moveToElement(getElementBy(elementState.present, columnValueLocator, true));
            columnValue = getTextFromSpecificElement(columnValueLocator);
            columnValue = columnValue.replaceAll("[() ]", "");
        } catch (Exception e) {
            testStepFailed("Failed to retrieve value from  the '" + columnName + "' column due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
        return columnValue;
    }

    /**
     * Select country from footer dropdown
     *
     * @param countryName Country name
     */
    public void selectCountryFromFooterDropdown(String countryName){
        testStepInfo("[Start]: "+getMethodName());
        String dropdownLocator = "Dropdown locator#xpath=//span[@class='js-localization-popover']",
        dropdownItemLocator = "Dropdown item locator#xpath=//li//a[normalize-space()='" + countryName + "']",
        countryDropdownLocator = "Country dropdown item locator#xpath=//button[@id='countries-dropdown']";
        try {
            moveToElement(getElementBy(elementState.present, dropdownLocator, true));
            clickElement(dropdownLocator);
            clickElement(countryDropdownLocator);
            clickElement(dropdownItemLocator);
            testStepPassed("Successfully selected option: '" + countryName + "'");
        } catch (Exception e) {
            testStepFailed("Failed to select country from the  footer dropdown due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
    }

    /**
     * Select Sell dropdown value
     *
     * @param dropDownItem DropDown item
     */
    public void selectSellDropdownValue(String dropDownItem){
        testStepInfo("[Start]: "+getMethodName());
        String dropdownLocator = "Dropdown locator#xpath=//label[normalize-space()='Sell']/..//i[@class='caret pull-right']",
                dropdownItemLocator = "Dropdown item locator#xpath=//span[@class='ng-binding ng-scope' and normalize-space()='" + dropDownItem + "']";
        try {
            moveToElement(getElementBy(elementState.present, dropdownLocator, true));
            clickElement(dropdownLocator);
            clickElement(dropdownItemLocator);
            testStepPassed("Successfully selected option: '" + dropDownItem + "'");
        } catch (Exception e) {
            testStepFailed("Failed to select Sell currency from the Sell dropdown due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
    }

    /**
     * Select Buy dropdown value
     *
     * @param dropDownItem DropDown item
     */
    public void selectSBuyDropdownValue(String dropDownItem){
        testStepInfo("[Start]: "+getMethodName());
        String dropdownLocator = "Dropdown locator#xpath=//label[normalize-space()='Buy']/..//i[@class='caret pull-right']",
                dropdownItemLocator = "Dropdown item locator#xpath=//span[@class='ng-binding ng-scope' and normalize-space()='" + dropDownItem + "']";
        try {
            moveToElement(getElementBy(elementState.present, dropdownLocator, true));
            clickElement(dropdownLocator);
            clickElement(dropdownItemLocator);
            testStepPassed("Successfully selected option: '" + dropDownItem + "'");
        } catch (Exception e) {
            testStepFailed("Failed to select Sell currency from the Buy dropdown due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
    }

    /**
     * Click on specific button
     *
     * @param buttonName Button name
     */
    public void clickOnSpecificButton(String buttonName){
        testStepInfo("[Start]: "+getMethodName());
        String buttonLocator = "Button locator#xpath=//button[normalize-space()='" + buttonName + "']";
        try {
            clickElement(buttonLocator);
            testStepPassed("Successfully clicked on the '" + buttonName + "' button");
        } catch (Exception e) {
            testStepFailed("Failed to click on the '" + buttonName + "' button due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
    }

    /**
     * Get selected item from sell dropdown
     *
     * @return Selected item
     */
    public String getSelectedItemFromSellDropdown(){
        testStepInfo("[Start]: "+getMethodName());
        String selectedItem = "Selected item locator#xpath=(//span[@class='ng-binding ng-scope'])[1]", item = "";
        try {
            item = getTextFromSpecificElement(selectedItem);
            testStepInfo("Successfully retrieved selected item from the Sell dropdown");
        } catch (Exception e) {
            testStepFailed("Failed to retrieve selected item from the Sell dropdown due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
        return item;
    }

    /**
     * Get selected item from Buy dropdown
     *
     * @return Selected item
     */
    public String getSelectedItemFromBuyDropdown(){
        testStepInfo("[Start]: "+getMethodName());
        String selectedItem = "Selected item locator#xpath=(//span[@class='ng-binding ng-scope'])[2]", item = "";
        try {
            item = getTextFromSpecificElement(selectedItem);
            testStepInfo("Successfully retrieved selected item from the Buy dropdown");
        } catch (Exception e) {
            testStepFailed("Failed to retrieve selected item from the Buy dropdown due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
        return item;
    }

    /**
     * Is specific warning message available
     *
     * @param messageText Message text
     * @return true/false
     */
    public boolean isSpecificWarningMessageAvailable(String messageText){
        testStepInfo("[Start]: "+getMethodName());
        boolean isAvailable = false;
        String messageLocator = "Message locator#xpath=//span[normalize-space()='" + messageText + "']";
        try {
            isAvailable = isElementExist(messageLocator);
            testStepInfo("Warning message: '" + messageLocator + "' is available in the screen");
        } catch (Exception e) {
            testStepFailed("Failed to verify warning message due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
        return isAvailable;
    }

    /**
     * Is specific grid column available
     *
     * @param columnName Column name
     * @return true/false
     */
    public boolean isSpecificGridColumnAvailable(String columnName){
        testStepInfo("[Start]: "+getMethodName());
        boolean isAvailable = false;
        String columnLocator;
        try{
        if (columnName.equalsIgnoreCase("Paysera amount")) {
            columnName = "Paysera";
            columnLocator = "Column locator#xpath=//div[@id= 'currency-exchange-app']//table//thead/tr/th[contains(text(), '" + columnName + "')]";
            isAvailable = isElementExist(columnLocator);
            columnName = "amount";
            columnLocator = "Column locator#xpath=//div[@id= 'currency-exchange-app']//table//thead/tr/th[contains(text(), '" + columnName + "')]";
            isAvailable = isElementExist(columnLocator);
        } else {
            columnLocator = "Column locator#xpath=//div[@id= 'currency-exchange-app']//table//thead/tr/th[normalize-space()='" + columnName + "']";
            isAvailable = isElementExist(columnLocator);
        }
            testStepInfo("Grid column '" + columnName + "' is available in the screen");
        } catch (Exception e) {
            testStepFailed("Failed to verify grid column due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
        return isAvailable;
    }

    /**
     * Close invalid parameters warning message
     *
     */
    public void closeInvalidParametersWarningMessage(){
        testStepInfo("[Start]: "+getMethodName());
        String buttonLocator = "Button locator#xpath=//button[@ng-click='!message.dismissOnClick && dismiss()']";
        try {
            clickElement(buttonLocator);
            testStepPassed("Successfully clicked on the Close button");
        } catch (Exception e) {
            testStepFailed("Failed to click on the Close button due to \n" + getStackTrackAsString(e));
        }
        testStepInfo("[End]: "+getMethodName());
    }
}
