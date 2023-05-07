package testSuites;

import pages.CurrencyExchangeCalculatorPage;
import pages.basePage;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class CurrencyExchangeCalculator extends basePage {


    /**
     * TC-01 : Validate if 'Buy' field is emptied after entering data in the 'Sell' field
     * TC-02 : Validate if 'Sell' field is emptied after entering data in the 'Buy' field
     */
    public void tC_01() {
        CurrencyExchangeCalculatorPage currencyExchangeCalculatorPage = new CurrencyExchangeCalculatorPage();
        String inputFieldValue = retrieveTestData("Input Field Value");

        testStepInfo("[Start]: TC-01 & TC-02");
        currencyExchangeCalculatorPage.setValueIntoSellField(inputFieldValue);
        assertEquals(currencyExchangeCalculatorPage.getTextFromSpecificInputField(CurrencyExchangeCalculatorPage.buyFieldName).equals(""), true,
                "'" + CurrencyExchangeCalculatorPage.buyFieldName + "' field should be empty");
        currencyExchangeCalculatorPage.setValueIntoBuyField(inputFieldValue);
        assertEquals(currencyExchangeCalculatorPage.getTextFromSpecificInputField(CurrencyExchangeCalculatorPage.sellFieldName).equals(""), true,
                "'" + CurrencyExchangeCalculatorPage.sellFieldName + "' field should be empty");
        testStepInfo("[End]: TC-01 & TC-02");
    }

    /**
     * TC-03 : Validate if rates are being updated based on the selected country from the country selection dropdown
     * TC-04 : Validate if currency option is being changed based on the selected country from the country selection dropdown
     *
     */
    public void tC_02() {
        CurrencyExchangeCalculatorPage currencyExchangeCalculatorPage = new CurrencyExchangeCalculatorPage();
        String sellDropdownValue = retrieveTestData("Sell Dropdown Value"),
                officialRates = retrieveTestData("Official Rates"),
                payseraRates = retrieveTestData("Paysera Rates"),
                payseraAmounts = retrieveTestData("Paysera Amounts"),
                currencyValues = retrieveTestData("Currencies"),
                countryName = retrieveTestData("Country Name");

        List<String> officialRateValues = Arrays.asList(officialRates.split(",")),
                payseraRateValues = Arrays.asList(payseraRates.split(",")),
                payseraAmountValues = Arrays.asList(payseraAmounts.split(",")),
                currencyValueList = Arrays.asList(currencyValues.split(","));

        testStepInfo("[Start]: TC-03 & TC-04");
        currencyExchangeCalculatorPage.selectCountryFromFooterDropdown(countryName);
        assertEquals(currencyExchangeCalculatorPage.getSelectedItemFromSellDropdown().equals(sellDropdownValue), true,
                "Sell dropdown field value should be updated after selecting the country");

        for (String officialRateValue : officialRateValues) {
            int currentIndex = officialRateValues.indexOf(officialRateValue);

            assertEquals(currencyExchangeCalculatorPage.getSpecificGridColumnValueBasedOnTheCurrencyColumnValue(CurrencyExchangeCalculatorPage.officialRateColumnName,
                    currencyValueList.get(currentIndex).trim()).equals(officialRateValue.trim()), true,
                    "'" + CurrencyExchangeCalculatorPage.officialRateColumnName + "' column value should be '" + officialRateValue.trim() + "' for currency: '" + currencyValueList.get(currentIndex).trim() + "'");

            assertEquals(currencyExchangeCalculatorPage.getSpecificGridColumnValueBasedOnTheCurrencyColumnValue(CurrencyExchangeCalculatorPage.payseraRateColumnName,
                    currencyValueList.get(currentIndex).trim()).equals(payseraRateValues.get(currentIndex).trim()), true,
                    "'" + CurrencyExchangeCalculatorPage.payseraRateColumnName + "' column value should be '" + payseraRateValues.get(currentIndex).trim() + "' for currency: '" + currencyValueList.get(currentIndex).trim() + "'");

            assertEquals(currencyExchangeCalculatorPage.getSpecificGridColumnValueBasedOnTheCurrencyColumnValue(CurrencyExchangeCalculatorPage.payseraAmountColumnName,
                    currencyValueList.get(currentIndex).trim()).equals(payseraAmountValues.get(currentIndex).trim()), true,
                    "'" + CurrencyExchangeCalculatorPage.payseraAmountColumnName + "' column value should be '" + payseraAmountValues.get(currentIndex).trim() + "' for currency: '" + currencyValueList.get(currentIndex).trim() + "'");
        }
        testStepInfo("[End]: TC-03 & TC-04");
    }

    /**
     * TC-05 : Validate if the bank provider's exchange amount for selling (X) is lower than the amount, provided by Paysera (Y), then a text box is displayed, representing the loss (X-Y)
     *
     */
    public void tC_05() {
        CurrencyExchangeCalculatorPage currencyExchangeCalculatorPage = new CurrencyExchangeCalculatorPage();

        String inputFieldValue = retrieveTestData("Input Field Value"),
                currencyValues = retrieveTestData("Currencies");

        List<String> currencyValueList = Arrays.asList(currencyValues.split(",")),
             columnNameList = Arrays.asList("Swedbank amount", "SEB amount", "Citadele amount", "Luminor amount");

        testStepInfo("[Start]: TC-05");
        currencyExchangeCalculatorPage.setValueIntoSellField(inputFieldValue);
        currencyExchangeCalculatorPage.clickOnSpecificButton(CurrencyExchangeCalculatorPage.filterButtonName);

        for (String currencyValue : currencyValueList) {
            String payseraAmount = currencyExchangeCalculatorPage.getSpecificGridColumnValueBasedOnTheCurrencyColumnValue(CurrencyExchangeCalculatorPage.payseraAmountColumnName,
                    currencyValue.trim());
            for (String columnName : columnNameList){
                String columnStringValue = currencyExchangeCalculatorPage.getSpecificGridColumnValueBasedOnTheCurrencyColumnValue(columnName, currencyValue);
                columnStringValue = columnStringValue.split("\\(")[0];
                float columnValue = Float.parseFloat(columnStringValue.trim());
                if (Float.parseFloat(payseraAmount.trim()) > columnValue){
                    float lossAmount = columnValue - Float.parseFloat((payseraAmount.trim()));
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    lossAmount = Float.parseFloat(decimalFormat.format(lossAmount));
                    assertEquals(Float.parseFloat(currencyExchangeCalculatorPage.getSpecificGridColumnLossAmountBasedOnTheCurrencyColumnValue(columnName, currencyValue).trim()) == lossAmount, true,
                            "'" + columnName + "' column loss amount should be: '" +  lossAmount + "'");
                }
            }
        }
        testStepInfo("[End]: TC-05");
    }

    /**
     * TC-06 : Validate the 'Clear filter' button functionality
     *
     */
    public void tC_06() {
        CurrencyExchangeCalculatorPage currencyExchangeCalculatorPage = new CurrencyExchangeCalculatorPage();

        String inputFieldValue = retrieveTestData("Input Field Value"),
                sellCurrencyDropdownItem = retrieveTestData("Sell Currency Dropdown Item"),
                buyCurrencyDropdownItem = retrieveTestData("Buy Currency Dropdown Item");

        testStepInfo("[Start]: TC-06");
        currencyExchangeCalculatorPage.setValueIntoSellField(inputFieldValue);
        currencyExchangeCalculatorPage.selectSellDropdownValue(sellCurrencyDropdownItem);
        currencyExchangeCalculatorPage.selectSBuyDropdownValue(buyCurrencyDropdownItem);
        moveToElement(getElementBy(basePage.elementState.present, CurrencyExchangeCalculatorPage.dropdownLocator, true));
        currencyExchangeCalculatorPage.clickOnSpecificButton(CurrencyExchangeCalculatorPage.clearFilterButtonName);
        assertEquals(currencyExchangeCalculatorPage.getSelectedItemFromSellDropdown().equals("EUR"), true,
                "Sell dropdown field value should be: 'EUR'");
        assertEquals(currencyExchangeCalculatorPage.getSelectedItemFromBuyDropdown().equals("All"), true,
                "Sell dropdown field value should be: 'All'");
        assertEquals(currencyExchangeCalculatorPage.getTextFromSpecificInputField(CurrencyExchangeCalculatorPage.buyFieldName).equals(""), true,
                "'" + CurrencyExchangeCalculatorPage.buyFieldName + "' field should be empty");
        String sdds = currencyExchangeCalculatorPage.getTextFromSpecificInputField(CurrencyExchangeCalculatorPage.sellFieldName);
        assertEquals(currencyExchangeCalculatorPage.getTextFromSpecificInputField(CurrencyExchangeCalculatorPage.sellFieldName).equals("100"), true,
                "'" + CurrencyExchangeCalculatorPage.sellFieldName + "' field value should be: '100'");
        testStepInfo("[End]: TC-06");
    }

    /**
     * TC-07 : Validate Paysera Amount column value according to the Paysera Rate column value by giving sell value
     * TC-13 : Validate round up decimal values under 'Paysera amount' column
     * TC-14 : Validate column names
     *
     */
    public void tC_07() {
        CurrencyExchangeCalculatorPage currencyExchangeCalculatorPage = new CurrencyExchangeCalculatorPage();

        String inputFieldValue = retrieveTestData("Input Field Value"), payseraRateColumnValue, payseraAmountColumnValue,
                currencyValues = retrieveTestData("Currencies");

        float paySeraAmount;

        List<String> columnNameList = Arrays.asList(CurrencyExchangeCalculatorPage.currencyColumnName, CurrencyExchangeCalculatorPage.officialRateColumnName,
                CurrencyExchangeCalculatorPage.payseraRateColumnName, CurrencyExchangeCalculatorPage.payseraAmountColumnName);

        String[] currencyValueList = currencyValues.split(",");

        testStepInfo("[Start]: TC-07");
        currencyExchangeCalculatorPage.setValueIntoSellField(inputFieldValue);
        currencyExchangeCalculatorPage.clickOnSpecificButton(CurrencyExchangeCalculatorPage.filterButtonName);

        for (String currencyValue : currencyValueList) {
            payseraRateColumnValue = currencyExchangeCalculatorPage.getSpecificGridColumnValueBasedOnTheCurrencyColumnValue(CurrencyExchangeCalculatorPage.payseraRateColumnName,
                    currencyValue);
            payseraAmountColumnValue = currencyExchangeCalculatorPage.getSpecificGridColumnValueBasedOnTheCurrencyColumnValue(CurrencyExchangeCalculatorPage.payseraAmountColumnName,
                    currencyValue);
            paySeraAmount = Integer.parseInt(inputFieldValue) * Float.parseFloat(payseraRateColumnValue);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            paySeraAmount = Float.parseFloat(decimalFormat.format(paySeraAmount));
            assertEquals(Float.parseFloat(payseraAmountColumnValue) == paySeraAmount, true,
                    "'" + CurrencyExchangeCalculatorPage.payseraAmountColumnName + "' column value should be: '" + paySeraAmount + "'");
            testStepInfo("[End]: TC-07");

            testStepInfo("[Start]: TC-13");
            assertEquals(payseraAmountColumnValue.split("\\.")[1].length() == 2, true,
                    "'" + CurrencyExchangeCalculatorPage.payseraAmountColumnName + "' column's decimal value should be round up by 2 digits");
            testStepInfo("[End]: TC-13");
        }

        testStepInfo("[Start]: TC-14");
        for (String columnName : columnNameList){
            assertEquals(currencyExchangeCalculatorPage.isSpecificGridColumnAvailable(columnName), true,
                    "'" + columnName + "' column should be available in the grid");
        }
        testStepInfo("[End]: TC-14");
    }

    /**
     * TC-08 : Validate warning message after giving invalid value in the 'Buy' field
     * TC-09 : Validate warning message after giving invalid value in the 'Sell' field
     * TC-10 : Validate if user is able to close the Invalid message
     * TC-11 : Validate input limit in 'Sell' field
     * TC-12 : Validate input limit in 'Buy' field
     *
     */
    public void tC_08() {
        CurrencyExchangeCalculatorPage currencyExchangeCalculatorPage = new CurrencyExchangeCalculatorPage();

        testStepInfo("[Start]: TC-08");
        String inputFieldValue = getRandomString(5);
        currencyExchangeCalculatorPage.setValueIntoBuyField(inputFieldValue);
        currencyExchangeCalculatorPage.clickOnSpecificButton(CurrencyExchangeCalculatorPage.filterButtonName);
        assertEquals(currencyExchangeCalculatorPage.isSpecificWarningMessageAvailable(CurrencyExchangeCalculatorPage.invalidParametersMessageText), true,
                "'" + CurrencyExchangeCalculatorPage.invalidParametersMessageText + "' warning message should be available in the screen");
        testStepInfo("[End]: TC-08");

        testStepInfo("[Start]: TC-10");
        currencyExchangeCalculatorPage.closeInvalidParametersWarningMessage();
        assertEquals(currencyExchangeCalculatorPage.isSpecificWarningMessageAvailable(CurrencyExchangeCalculatorPage.invalidParametersMessageText), false,
                "'" + CurrencyExchangeCalculatorPage.invalidParametersMessageText + "' warning message should not be available in the screen");
        testStepInfo("[End]: TC-10");

        testStepInfo("[Start]: TC-09");
        currencyExchangeCalculatorPage.setValueIntoSellField(inputFieldValue);
        currencyExchangeCalculatorPage.clickOnSpecificButton(CurrencyExchangeCalculatorPage.filterButtonName);
        assertEquals(currencyExchangeCalculatorPage.isSpecificWarningMessageAvailable(CurrencyExchangeCalculatorPage.invalidParametersMessageText), true,
                "'" + CurrencyExchangeCalculatorPage.invalidParametersMessageText + "' warning message should be available in the screen");
        testStepInfo("[End]: TC-09");

        testStepInfo("[Start]: TC-11");
        inputFieldValue = getRandomString(250);
        currencyExchangeCalculatorPage.setValueIntoSellField(inputFieldValue);
        assertEquals(currencyExchangeCalculatorPage.getTextFromSpecificInputField(CurrencyExchangeCalculatorPage.sellFieldName).equals(inputFieldValue), false,
                "User should not be able to enter more than 200 characters in the Sell input field");
        testStepInfo("[End]: TC-11");

        testStepInfo("[Start]: TC-12");
        inputFieldValue = getRandomString(250);
        currencyExchangeCalculatorPage.setValueIntoBuyField(inputFieldValue);
        assertEquals(currencyExchangeCalculatorPage.getTextFromSpecificInputField(CurrencyExchangeCalculatorPage.buyFieldName).equals(inputFieldValue), false,
                "User should not be able to enter more than 200 characters in the Buy input field");
        testStepInfo("[End]: TC-12");
    }
}
