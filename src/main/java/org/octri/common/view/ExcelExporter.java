package org.octri.common.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Exporter that exports an Excel document (xlsx). When provided with multiple tables, a new sheet will be created
 * for each.
 */
public class ExcelExporter implements Exporter {

	private final Integer MAX_WORKSHEET_NAME_CHARS = 31;
	private Map<String, Integer> sheetNames = new HashMap<String, Integer>();

	/**
	 * Export the tables in Excel format. Each table will be a separate sheet in the workbook.
	 */
	@Override
	public void export(OutputStream outputStream, List<Table> tables) throws IOException {
		this.reset();
		Workbook workbook = new XSSFWorkbook();

		for (Table table : tables) {
			appendSheet(workbook, table);
		}

		workbook.write(outputStream);
		workbook.close();
	}

	/**
	 * Export the table in Excel format.
	 */
	@Override
	public void export(OutputStream outputStream, Table table) throws IOException {
		export(outputStream, List.of(table));
	}

	public String getFileSuffix() {
		return ".xlsx";
	}

	protected void reset() {
		this.sheetNames = new HashMap<String, Integer>();
	}

	/**
	 * Append a sheet to the given workbook. Sheet will have a header and a row for each response.
	 *
	 * @param workbook
	 *            - workbook to modify
	 * @param table
	 *            - the table implementation
	 */
	protected void appendSheet(Workbook workbook, Table table) {
		Sheet sheet = workbook.createSheet(createSheetName(table.displayName()));
		addHeaderRow(sheet, table.headers(), headerStyle(workbook));
		addDataRows(sheet, table.rows(), bodyStyle(workbook));
	}

	/**
	 * Construct a valid sheetname. Note that sheet names cannot:
	 * - Be blank.
	 * - Contain more than 31 characters.
	 * - Contain any of the following characters: / \ ? * : [ ]
	 * - Begin or end with an apostrophe ('), but they can be used in between text or numbers in a name.
	 * - Be named "History". This is a reserved word Excel uses internally.
	 * - Be used twice in the same workbook.
	 *
	 * @param label
	 * @return
	 */
	protected String createSheetName(String label) {
		var safeName = WorkbookUtil.createSafeSheetName(label);
		// strip out double spaces
		var name = safeName.replace("  ", " ").trim();

		if (name.equals("")) {
			name = "empty";
		}

		// replace keyword
		if (name.toLowerCase().equals("history")) {
			name = "hx";
		}

		// Append version if there is a sheet with the same name.
		var count = 1;
		if (this.sheetNames.containsKey(name)) {
			var newCount = this.sheetNames.get(name) + 1;
			// increment count
			this.sheetNames.put(name, newCount);

			// append version
			var digits = String.valueOf(count).length();
			var endIndex = Math.min(name.length(), MAX_WORKSHEET_NAME_CHARS - (digits + 1));
			name = name.substring(0, endIndex) + " " + newCount;
		} else {
			this.sheetNames.put(name, 1);
		}

		return name;
	}

	/**
	 * Adds a header row to the given sheet.
	 *
	 * @param sheet
	 * @param columnNames
	 * @param headerStyle
	 */
	protected void addHeaderRow(Sheet sheet, List<String> columnNames, CellStyle headerStyle) {
		Row header = sheet.createRow(0);
		int col = 0;
		for (String columnName : columnNames) {
			Cell headerCell = header.createCell(col);
			headerCell.setCellValue(columnName);
			headerCell.setCellStyle(headerStyle);
			sheet.setColumnWidth(col, getColumnWidth(columnName));
			col++;
		}
	}

	/**
	 * Adds a row to the sheet for each item in the data array.
	 *
	 * @param sheet
	 * @param data
	 * @param bodyStyle
	 */
	protected void addDataRows(Sheet sheet, List<List<String>> data, CellStyle bodyStyle) {
		int rownum = 1;
		for (var dataRow : data) {
			Row row = sheet.createRow(rownum);
			Object[] rowValues = dataRow.toArray();
			for (int i = 0; i < rowValues.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(rowValues[i] != null ? rowValues[i].toString() : null);
				cell.setCellStyle(bodyStyle);
			}
			rownum++;
		}
	}

	/**
	 * Defines a cell style for header cells.
	 *
	 * @param workbook
	 * @return
	 */
	protected CellStyle headerStyle(Workbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setBold(true);
		headerStyle.setFont(font);
		return headerStyle;
	}

	/**
	 * Defines a cell style for body cells.
	 *
	 * @param workbook
	 * @return
	 */
	protected CellStyle bodyStyle(Workbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setWrapText(true);
		return bodyStyle;
	}

	/**
	 * Computes the column width based on the header name.
	 *
	 * @param columnName
	 * @return
	 */
	Integer getColumnWidth(String columnName) {
		var minLength = 5;
		// Hard-code some values based on trial and error
		if (columnName.contains("date") || columnName.contains("created_at") || columnName.contains("updated_at")) {
			minLength = 10;
		}

		// width units is 1/256 of a character
		return Math.max(minLength, columnName.length()) * 256;
	}
}
