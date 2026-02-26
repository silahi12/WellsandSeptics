package com.nal.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {

    public static List<String> getHeaderColumns(WebElement table) {
        List<String> headerColumns = new ArrayList<>();
        List<WebElement> headerElements = table.findElement(By.tagName("thead")).findElements(By.tagName("th"));  // Assuming <th> for headers

        for (WebElement headerElement : headerElements) {
            headerColumns.add(headerElement.getText().trim());
        }
        return headerColumns;
    }

    public static List<List<String>> getRowValues(WebElement table) {
        List<List<String>> rowValues = new ArrayList<>();
        List<WebElement> rowElements = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr")); // Assuming <tr> for rows

        for (int i = 0; i < rowElements.size(); i++) {
            List<String> row = new ArrayList<>();
            List<WebElement> cellElements = rowElements.get(i).findElements(By.tagName("td")); // Assuming <td> for cells

            for (WebElement cellElement : cellElements) {
                row.add(cellElement.getText().trim());
            }
            rowValues.add(row);
        }
        return rowValues;
    }

    public static List<Map<String, String>> mapTableData(List<String> headerColumns, List<List<String>> rowValues) {
        List<Map<String, String>> mappedData = new ArrayList<>();

        for (List<String> row : rowValues) {
            Map<String, String> rowMap = new HashMap<>();
            for (int i = 0; i < headerColumns.size(); i++) {
                if (i < row.size()) { //check if row has enough cells.
                    rowMap.put(headerColumns.get(i), row.get(i));
                }
                else {
                    rowMap.put(headerColumns.get(i), ""); //or null, or some other default
                }
            }
            mappedData.add(rowMap);
        }
        return mappedData;
    }

    /**
     * Get cell value of a particular column based on given key column name and key column value
     *
     * @param table
     * @param keyColumnName
     * @param keyColumnValue
     * @param columnName     Of The Cell for which we need to get value
     * @return cellValue
     */
    public String getTableCellValue(WebElement table, String keyColumnName, String keyColumnValue, String columnName ){
        
        String cellValue = "";
        // Get header column names
        List<String> headerColumns = getHeaderColumns(table);

        // Get row values
        List<List<String>> rowValues = getRowValues(table);

        // Map header columns with row values
        List<Map<String, String>> mappedData = mapTableData(headerColumns, rowValues);

        //Assert the cell value
        for (Map<String, String> rowMap : mappedData) {
            if ((rowMap.get(keyColumnName).equals(keyColumnValue))){
                 cellValue = rowMap.get(columnName);
            }
        }
        return  cellValue;
    }
}
