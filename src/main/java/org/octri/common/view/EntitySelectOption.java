package org.octri.common.view;

import java.util.Collection;

import org.octri.common.domain.AbstractEntity;

/**
 * Used for UI select inputs. Wraps the choice along with its selected status.
 *
 * @author lawhead
 *
 * @param <T>
 *            an entity type that extends {@link AbstractEntity} and implements the {@link Labelled} interface
 */
public class EntitySelectOption<T extends AbstractEntity & Labelled> extends SelectOption<T> {

	/**
	 * Constructor
	 *
	 * @param choice
	 *            - lookup list item
	 * @param selected
	 *            - The selected item; may be null
	 */
	public EntitySelectOption(T choice, T selected) {
		super(choice, selected);
		this.setLabel(choice.getLabel());
		this.setValue(choice.getId().toString());
	}

	/**
	 * Constructor used for an option in a multi-select.
	 *
	 * @param choice
	 *            - lookup list item
	 * @param selected
	 *            - collection of selected items
	 */
	public EntitySelectOption(T choice, Collection<T> selected) {
		super(choice, selected);
		this.setLabel(choice.getLabel());
		this.setValue(choice.getId().toString());
	}

	/**
	 * Gets the entity's unique ID.
	 * 
	 * @return entity ID
	 */
	public Long getId() {
		return this.getChoice().getId();
	}
}
