package org.octri.common.view;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for classes that export data to an output stream.
 */
public interface Exporter {

	public void export(OutputStream outputStream, Table table) throws IOException;

	public void export(OutputStream outputStream, List<Table> tables) throws IOException;

	public String getFileSuffix();

	default String outputFileName(String fileName) {
		String name = fileName.replace(" ", "_") + "_export";
		LocalDate today = LocalDate.now();
		return name + "_" + today.toString() + getFileSuffix();
	}
}