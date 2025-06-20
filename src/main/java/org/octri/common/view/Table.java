package org.octri.common.view;

import java.util.List;

/**
 * An interface for displaying or exporting tabular data
 */
public interface Table {

	/**
	 * The display name for the table
	 *
	 * @return
	 */
	public String displayName();

	/**
	 * The column header names
	 *
	 * @return
	 */
	public List<String> headers();

	/**
	 * The rows of data
	 *
	 * @return
	 */
	public List<List<String>> rows();

}
