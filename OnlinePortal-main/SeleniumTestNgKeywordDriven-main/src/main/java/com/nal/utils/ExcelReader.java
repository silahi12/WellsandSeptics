package com.nal.utils;

import com.nal.keywords.KeywordExecutor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelReader {

    private static final Object lock = new Object();

    /**
     * Path to the excel file being processed.
     */
    public static String excelPath = null;

    /**
     * Reads and executes test cases from a excel file.
     *
     * @param exelFile The path to the CSV file containing test cases.
     */
    public void executeTestCasesFromExcel(String exelFile) throws InterruptedException {
        excelPath = "./src/test/resources/excels/" + exelFile;
        List<Map<String,String>> keyList = getExcelDataAsMap(excelPath);
        System.out.println(keyList);
//        List<String> testSteps = new ArrayList<>();
        for( Map<String, String> testCaseMap : keyList) {
//            testSteps.add(testCaseMap.get("Keyword") + "|" + testCaseMap.get("Target") + "," + testCaseMap.get("Value"));
            KeywordExecutor keywordExecutor = new KeywordExecutor();
            keywordExecutor.executeTestSteps(testCaseMap);
//            testSteps.clear();

        }



    }

    /**
     * Prints the test cases from the given CSV file.
     *
     * @param excelFile The path to the CSV file containing test cases.
     */
    private void printTestCasesFromExcel(String excelFile) {
        synchronized (lock) {
            try (BufferedReader br = new BufferedReader(
                    new FileReader("./src/test/resources/csvs/" + excelFile))) {
                String line;
                System.out.println("Test cases from CSV file: " + excelFile);
                System.out.println(
                        "+-------------------------------------------------------------------------------------------+");
                System.out.printf("| %-15s | %-15s | %-50s | %-20s |\n", "Test Case", "Keyword", "Target", "Value");
                System.out.println(
                        "+-------------------------------------------------------------------------------------------+");
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String testCase = data[0];
                    String keyword = data[1];
                    String target = data[2];
                    String value = "";
                    if (data.length > 3) {
                        value = data[3];
                    }
                    System.out.printf("| %-15s | %-15s | %-50s | %-20s |\n", testCase, keyword, target, value);
                }
                System.out.println("+----------------------------------------------------------------+");
                System.out.println("Total test cases: " + getTestCaseCount(excelFile));
                System.out.println("+----------------------------------------------------------------+");
                System.out.println();
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Counts the number of test cases in the given CSV file.
     *
     * @param excelFile The path to the CSV file containing test cases.
     * @return The number of test cases in the CSV file.
     * @throws IOException If an I/O error occurs.
     */
    private int getTestCaseCount(String excelFile) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(
                new FileReader("./src/test/excels/excels/" + excelFile))) {
            while (br.readLine() != null) {
                count++;
            }
        }
        return count;
    }

    public List<Map<String,String>> getExcelDataAsMap(String excelPath){
        List<Map<String, String>> result = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(excelPath);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (!rows.hasNext()) {
                return Collections.emptyList();
            }
            // Reading header
            Row header = rows.next();
            List<String> keys = new ArrayList<>();
            for (Cell cell : header) {
                String value = cell.getStringCellValue();
                if (!value.isEmpty()) {
                    keys.add(value);
                } else {
                    break;
                }
            }
            //test steps
            while (rows.hasNext()) {
                Row row = rows.next();
                Map<String, String> rowMap = new HashMap<>();
                for (int i = 0; i < keys.size(); ++i) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value;
                   // value = cell.getStringCellValue();
                    switch (cell.getCellType()) {
                        case STRING:    //field that represents string cell type
                            value =  cell.getStringCellValue() ;
                            break;
                        case NUMERIC:    //field that represents number cell type
                           value = String.valueOf(cell.getNumericCellValue());
                           break;
                        case BOOLEAN:    //field that represents Date cell type
                           value = String.valueOf(cell.getBooleanCellValue());
                           break;
                        default:
                            value = "";
                    }
                    rowMap.put(keys.get(i), value);
                }
                // Only add rows which aren't empty
                if (!rowMap.values().stream().allMatch(String::isEmpty)) {
                    result.add(rowMap);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public Map<String, List<String>>  getDataFromExcel(String excelPath) {
        Map<String, List<String>> myMap = new LinkedHashMap<>();
        try {
            FileInputStream fis = new FileInputStream(excelPath);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file

            // CAREFUL HERE! use LinkedHashMap to guarantee the insertion order!


            // populate map with headers and empty list
            if (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> headerIterator = row.cellIterator();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    myMap.put(getCellValue(cell), new ArrayList<>());
                }
            }

            Iterator<List<String>> columnsIterator;
            // populate lists
            while (itr.hasNext()) {

                // get the list iterator every row to start from first list
                columnsIterator = myMap.values().iterator();
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    // here don't check hasNext() because if the file not contains problems
                    // the # of columns is same as # of headers
                    columnsIterator.next().add(getCellValue(cell));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myMap;

        // here your map should be filled with data as expected
    }


    public static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:    //field that represents string cell type
                return cell.getStringCellValue() + "\t\t\t";
            case NUMERIC:    //field that represents number cell type
                return cell.getNumericCellValue() + "\t\t\t";
            case BOOLEAN:    //field that represents Date cell type
                return cell.getBooleanCellValue() + "\t\t\t";
            default:
                return "";
        }
    }
}