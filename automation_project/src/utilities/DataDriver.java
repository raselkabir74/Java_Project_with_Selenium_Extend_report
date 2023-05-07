package utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataDriver {

    public static XSSFSheet sheet;
    private static XSSFWorkbook workbook;
    private static FileInputStream fileInputStream;

    /**
     * Get excel file path
     *
     * @param excelFileName Excel file name
     * @return Excel file path
     */
    public static String getExcelFilePath(String excelFileName) {
        String filePath = null;
        try {
            Path currentRelativePath = Paths.get("");
            if (excelFileName.equalsIgnoreCase("TestCaseSettings.xlsx"))
                filePath = currentRelativePath.toAbsolutePath() + "\\Data\\TestCaseSettings.xlsx";
            if (excelFileName.equalsIgnoreCase("TestConfigurations.xlsx"))
                filePath = currentRelativePath.toAbsolutePath() + "\\Data\\TestConfigurations.xlsx";
            if (excelFileName.equalsIgnoreCase("TestData.xlsx"))
                filePath = currentRelativePath.toAbsolutePath() + "\\Data\\TestData.xlsx";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * Get Sheet
     *
     * @param excelFilePath Excel file name
     * @param sheetName     Sheet name
     */
    public void readSheet(String excelFilePath, String sheetName) {
        try {
            fileInputStream = new FileInputStream(excelFilePath);
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to read sheet mentioned '" + sheetName + "' sheet name due to " + System.lineSeparator() + e.getMessage());
        }
    }

    /**
     * Find row index for a cell content
     *
     * @param cellContent cell content
     * @return row index
     */
    public int findRow(String cellContent) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (String.valueOf(cell).equals(cellContent)) {
                    return row.getRowNum();
                }
            }
        }
        return 9999;
    }

    /**
     * Find column index in a row
     *
     * @param rowNumber  row number
     * @param columnName Column name
     * @return column index
     */
    public int findColumnInRow(int rowNumber, String columnName) {
        for (int column = 0; column < sheet.getRow(rowNumber).getLastCellNum(); column++) {
            if (Objects.equals(sheet.getRow(rowNumber).getCell(column).getStringCellValue(), columnName)) {
                return sheet.getRow(rowNumber).getCell(column).getColumnIndex();
            }
        }
        return 9999;
    }

    /**
     * Get data from specific row and column
     *
     * @param row row index
     * @param col column index
     * @return cell data as string
     */
    public String getCellData(int row, int col) {
        String retrievedCellValue = "";
        XSSFCell cell;
        try {
            if (row > sheet.getLastRowNum()) {
                System.out.println("Physical row number '" + row + "' does not exist. Maximum is row number with value is '" + sheet.getLastRowNum() + "'");
            } else {
                if (Objects.equals(sheet.getRow(row).getCell(col), null)) {
                    sheet.getRow(row).createCell(col, CellType.BLANK);
                } else {
                    cell = sheet.getRow(row).getCell(col);
                    CellType retrievedCellType = cell.getCellType();
                    if (Objects.equals(retrievedCellType, CellType.NUMERIC)) {
                        if (!Objects.equals(String.valueOf(cell), "") || !Objects.equals(String.valueOf(cell), null)) {
                            if (String.valueOf(cell).matches("-?\\d+(\\.\\d+)?")) {
                                retrievedCellValue = String.valueOf(cell.getNumericCellValue()).replaceFirst("\\.0+$", "");
                            }
                            if (DateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = new SimpleDateFormat(Common.dateFormat);
                                retrievedCellValue = sdf.format(cell.getDateCellValue());
                            }
                        }
                    }
                    if (Objects.equals(retrievedCellType, CellType.STRING))
                        retrievedCellValue = cell.getStringCellValue();
                    if (Objects.equals(retrievedCellType, CellType.BOOLEAN))
                        retrievedCellValue = Boolean.toString(cell.getBooleanCellValue());
                    if (Objects.equals(retrievedCellType, CellType.FORMULA))
                        retrievedCellValue = cell.getStringCellValue();
                    retrievedCellValue = retrievedCellValue.trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Filed to read value from sheet due to \n" + e.getMessage());
        }
        return retrievedCellValue;
    }

    /**
     * Close workbook that has been opened recently
     */
    public void closeWorkbook() {
        try {
            workbook.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read test case details from excel file and store in LinkedHashMap
     *
     * @param sheetName sheet Name
     */
    public void readTestCaseDetails(String sheetName) {
        String testCaseName;
        readSheet(getExcelFilePath(Common.testCaseSettingsExcelFile), sheetName);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            testCaseName = getCellData(i, 0);
            Common.testCasesWithDescription.put(testCaseName, getCellData(i, 1));
            Common.testCasesWithExecutionFlag.put(testCaseName, getCellData(i, 2));
        }
        closeWorkbook();
    }

    /**
     * Get test case test data
     *
     * @param currentTestCaseName currentTestCaseName
     */
    public void getTestCaseTestData(String currentTestCaseName) {
        Dictionary<String, String> testDataDictionary;
        List<Integer> listOfRowWithYesExecutionFlag = new ArrayList<>();

        readSheet(getExcelFilePath(Common.testDataExcelFile), Common.getTestConfigurationProperty("TestDataSheetName"));
        int rowNumberCurrentTestCase = findRow(currentTestCaseName);
        String executionFlagValue;
        for (int row = 1; (row + rowNumberCurrentTestCase) <= sheet.getLastRowNum(); row++) {
            executionFlagValue = getCellData((rowNumberCurrentTestCase + row), findColumnInRow(rowNumberCurrentTestCase, "Execution Flag"));
            if (executionFlagValue.equalsIgnoreCase("Yes")) {
                listOfRowWithYesExecutionFlag.add(rowNumberCurrentTestCase + row);
            } else if (executionFlagValue.equalsIgnoreCase("No")) {
                LogWriter.writeToLogFile(LogWriter.logLevel.WARN, "Skipping test data row number '" + (rowNumberCurrentTestCase + row) + "' for test case '" + currentTestCaseName + "' as execution flag set to 'No'.");
            } else if (Objects.equals(executionFlagValue, "") || (executionFlagValue.equalsIgnoreCase("Execution Flag") && row > 1)) {
                break;
            }
        }

        for (Integer specificRow : listOfRowWithYesExecutionFlag) {
            testDataDictionary = new Hashtable<>();
            for (int column = 2; column < sheet.getRow(rowNumberCurrentTestCase).getLastCellNum(); column++) {
                if (!Objects.equals(getCellData(rowNumberCurrentTestCase, column), "")) {
                    testDataDictionary.put(getCellData(rowNumberCurrentTestCase, column), getCellData(specificRow, column));
                }
            }
            LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Test data " + testDataDictionary + "added for '" + currentTestCaseName + "'test case.");
            Common.testDataList.add(testDataDictionary);
        }
        closeWorkbook();
    }
}

