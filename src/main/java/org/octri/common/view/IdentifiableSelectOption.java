package org.octri.common.view;

import java.util.Collection;

import org.octri.common.domain.Identifiable;

/**
 * Used for UI select inputs for choices that implement {@link Identifiable} and the value is a UUID.
 *
 * @param <T>
 *            the type being wrapped
 */
public class IdentifiableSelectOption<T extends Identifiable> extends SelectOption<T> {

	/**
	 * Constructor for single select.
	 *
	 * @param choice
	 *            - The choice to configure
	 * @param selected
	 *            - The selected item; may be null
	 */
	public IdentifiableSelectOption(T choice, T selected) {
		super(choice, selected);
		this.setLabel(choice.getLabel());
		this.setValue(choice.getUuid());
	}

	/**
	 * Constructor used for an option in a multi-select.
	 *
	 * @param choice
	 *            - The choice to configure
	 * @param selected
	 *            - collection of selected items
	 */
	public IdentifiableSelectOption(T choice, Collection<T> selected) {
		super(choice, selected);
		this.setLabel(choice.getLabel());
		this.setValue(choice.getUuid());
	}

	public String getUuid() {
		return this.getChoice().getUuid();
	}
}