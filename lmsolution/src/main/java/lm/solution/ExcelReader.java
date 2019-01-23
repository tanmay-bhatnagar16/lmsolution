package lm.solution;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReader {

	public static String fileName;
	private File file;

	public ExcelReader() {
		file = new File( System.getProperty("user.dir") + "\\" +fileName); 

	}

	public String readExcel(String sheetName, String keyValue) throws IOException {

		// Create an object of FileInputStream class to read excel file
		String value = null;
		FileInputStream inputStream = new FileInputStream(file);

		Workbook woorkbook = new HSSFWorkbook(inputStream);

		// Read sheet inside the workbook by its name

		Sheet workSheet = woorkbook.getSheet(sheetName);

		// Find number of rows in excel file

		int rowCount = workSheet.getLastRowNum() - workSheet.getFirstRowNum();

		// Create a loop over all the rows of excel file to read it

		for (int i = 0; i < rowCount + 1; i++) {

			if (workSheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(keyValue)) {
				value = workSheet.getRow(i).getCell(1).getStringCellValue();
			}

		}
		return value;

	}

}
