package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author jad nasser
 */
public class ReadExcel {

	private final String excel_file_path;

	protected ReadExcel(String excel_file_path) {
		this.excel_file_path = excel_file_path;
	}

	// cleaning the string from unnecessary spaces and semicolons
	private String cleanString(String s) {
		// removing all extra spaced
		String cleanedString = s.replaceAll("\\s+", " ");
		// replace all commas with semicolons
		cleanedString = cleanedString.replaceAll(",", ";");
		// removing all extra semicolons
		cleanedString = cleanedString.replaceAll(";+", ";");
		// removing the sibling space characters of semicolons
		cleanedString = cleanedString.replaceAll(" ;", ";");
		cleanedString = cleanedString.replaceAll("; ", ";");
		// removing all extra semicolons again
		cleanedString = cleanedString.replaceAll(";+", ";");
		// removing first or last char if they are space characters or semicolons
		if (cleanedString.charAt(0) == ' ' || cleanedString.charAt(0) == ';') {
			cleanedString = cleanedString.substring(1);
		}
		char cleanedStringLastChar = cleanedString.charAt(cleanedString.length() - 1);
		if (cleanedStringLastChar == ' ' || cleanedStringLastChar == ';') {
			cleanedString = cleanedString.substring(0, cleanedString.length() - 1);
		}
		return cleanedString;
	}

	// this method is for correcting column name structure by replacing spaces and
	// "-" with "_"
	private String correctColumnName(String s) {
		// replace all "-" with "_"
		String correctedColumnName = s.replaceAll("-", "_");
		// replace all spaces with "_"
		correctedColumnName = correctedColumnName.replaceAll(" ", "_");
		return correctedColumnName;
	}

	// this method reads the excel file and returns the data
	protected ArrayList<ArrayList<String>> getData() throws IOException {
		ArrayList<ArrayList<String>> data = new ArrayList<>();
		// getting the data from the excel file
		try (FileInputStream excel_file = new FileInputStream(excel_file_path)) {
			// creating iterators for the excel sheet
			XSSFWorkbook workbook = new XSSFWorkbook(excel_file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();
			Iterator<Cell> cellIterator;
			XSSFCell cell;
			DataFormatter dataFormatter = new DataFormatter();
			// getting the data and clean them from unnecessary spaces, semicolons, and
			// commas
			while (rowIterator.hasNext()) {
				ArrayList<String> dataRow = new ArrayList<>();
				cellIterator = ((XSSFRow) rowIterator.next()).cellIterator();
				while (cellIterator.hasNext()) {
					cell = (XSSFCell) cellIterator.next();
					String cellValue = dataFormatter.formatCellValue(cell);
					String entry = cleanString(cellValue);
					dataRow.add(entry);
				}
				data.add(dataRow);
			}
			workbook.close();
			// correcting column names by replacing spaces and "-" with "_"
			for (int i = 0; i < data.get(0).size(); i++) {
				String columnName = data.get(0).get(i);
				columnName = correctColumnName(columnName);
				data.get(0).set(i, columnName);
			}
		}
		return data;
	}

}
