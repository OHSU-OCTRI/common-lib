package org.octri.common.view;

import java.util.Collection;

/**
 * Used for UI select inputs. Wraps the choice along with its selected status. Value and label is the choice.
 *
 * @author lawhead
 *
 * @param <T>
 *            type of items in the select list
 */
public class SelectOption<T> {

	/**
	 * The item wrapped by the select option.
	 */
	protected T choice;

	/**
	 * The value used as the select option's <code>value</code> attribute.
	 */
	protected String value;

	/**
	 * The value used as the select option's text.
	 */
	protected String label;

	/**
	 * Whether the option is currently selected.
	 */
	protected Boolean selected;

	/**
	 * Constructor
	 *
	 * @param choice
	 *            - lookup list item
	 * @param selected
	 *            - The selected item; may be null
	 */
	public SelectOption(T choice, T selected) {
		this.choice = choice;
		this.label = choice.toString();
		this.value = choice.toString();
		this.selected = choice.equals(selected);
	}

	/**
	 * Constructor used for an option in a multi-select.
	 *
	 * @param choice
	 *            - lookup list item
	 * @param selected
	 *            - collection of selected items
	 */
	public SelectOption(T choice, Collection<T> selected) {
		this.choice = choice;
		this.label = choice.toString();
		this.value = choice.toString();
		this.selected = selected != null && selected.contains(choice);
	}

	/**
	 * Gets the item wrapped by the select option.
	 * 
	 * @return wrapped item
	 */
	public T getChoice() {
		return choice;
	}

	/**
	 * Gets the value used for the select option's <code>value</code> attribute.
	 * 
	 * @return the select option value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value used for the select option's <code>value</code> attribute.
	 * 
	 * @param value
	 *            the option value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value used for the select option's text.
	 * 
	 * @return select option text
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the value used for the select option's text.
	 * 
	 * @param label
	 *            select option text
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets whether the option is currently selected.
	 * 
	 * @return true if selected, false if not
	 */
	public Boolean getSelected() {
		return selected;
	}

	/**
	 * Sets whether the option is currently selected.
	 * 
	 * @param selected
	 *            true if selected, false if not
	 */
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
}
