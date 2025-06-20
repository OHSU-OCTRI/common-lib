package org.octri.common.view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import com.opencsv.CSVWriter;

/**
 * Exporter that exports as a csv file.
 */
public class CsvExporter implements Exporter {

	/**
	 * Export responses for a single table.
	 */
	@Override
	public void export(OutputStream outputStream, Table table) throws IOException {
		PrintWriter printWriter = new PrintWriter(outputStream);
		CSVWriter writer = new CSVWriter(printWriter);
		writer.writeNext(header(table));
		for (String[] row : rows(table)) {
			writer.writeNext(row);
		}
		writer.close();
	}

	/**
	 * Export responses for multiple tables.
	 */
	@Override
	public void export(OutputStream outputStream, List<Table> tables) throws IOException {
		// TODO: implementation that creates a single wide, sparse table.
		throw new UnsupportedOperationException("Unimplemented method 'export'");
	}

	@Override
	public String getFileSuffix() {
		return ".csv";
	}

	String[] header(Table summary) {
		return summary.headers().toArray(String[]::new);
	}

	List<String[]> rows(Table summary) {
		return summary.rows().stream()
				.map(row -> row.toArray(String[]::new))
				.toList();
	}
}
