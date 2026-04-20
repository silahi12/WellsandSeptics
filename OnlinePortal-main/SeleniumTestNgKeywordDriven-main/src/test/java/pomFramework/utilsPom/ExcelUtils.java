package pomFramework.utilsPom;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ExcelUtils {

    /**
     * Reads data from an Excel sheet and returns it as a 2D Object array.
     * Automatically skips the first row (headers) and converts all cells to Strings.
     */
    public static Object[][] getExcelData(String filePath, String sheetName) {
        Object[][] data = null;
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum(); // Total rows (excluding 0-indexed header)
            int colCount = sheet.getRow(0).getLastCellNum(); // Total columns

            data = new Object[rowCount][colCount];
            DataFormatter formatter = new DataFormatter(); // Safely converts numbers/booleans to Strings

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    // Store as string in the array. i-1 maps Excel row 1 to array index 0.
                    data[i - 1][j] = formatter.formatCellValue(cell);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the Excel file: " + e.getMessage());
        }
        return data;
    }

}
