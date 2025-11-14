package org.octri.common.view;

import java.util.List;

/**
 * Response object for DataTables AJAX requests.
 *
 * @param <T>
 *            type of data in the table
 */
public class DataTableResponse<T> {

	public List<T> data;
	public long recordsTotal;
	public long recordsFiltered;

	public DataTableResponse(List<T> data, long total, long filtered) {
		this.data = data;
		this.recordsTotal = total;
		this.recordsFiltered = filtered;
	}

	public List<T> getData() {
		return data;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

}
