package org.octri.common.view;

import java.util.Collection;

/**
 * Used for UI select inputs. Wraps the choice along with its selected status.
 *
 * @param<T>
 */
public class EnumSelectOption<T extends Enum<T> & Labelled> extends SelectOption<T> {

	/**
	 * Constructor
	 *
	 * @param choice
	 *            - Enum item
	 * @param selected
	 *            - The selected item; may be null
	 */
	public EnumSelectOption(T choice, T selected) {
		super(choice, selected);
		this.setLabel(choice.getLabel());
	}

	/**
	 * Constructor used for an option in a multi-select.
	 *
	 * @param choice
	 *            - Enum item
	 * @param selected
	 *            - collection of selected items
	 */
	public EnumSelectOption(T choice, Collection<T> selected) {
		super(choice, selected);
		this.setLabel(choice.getLabel());
	}

}