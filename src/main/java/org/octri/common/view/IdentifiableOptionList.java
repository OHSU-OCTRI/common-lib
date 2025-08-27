package org.octri.common.view;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.octri.common.customizer.IdentifiableEntityFinder;
import org.octri.common.domain.AbstractEntity;
import org.octri.common.domain.Identifiable;

/**
 * Convenience class for creating a lookup list from a class that is uniquely identified by a UUID and labelled.
 */
public class IdentifiableOptionList {

	/**
	 * Given an iterable of {@link Identifiable} objects and a selected object, provides a select options
	 * that can be used directly by mustache templates for rendering.
	 *
	 * @param <T>
	 *            a type that implements {@link Identifiable}
	 * @param iter
	 *            iterable collection
	 * @param selected
	 *            the current selection
	 * @return a list of select options
	 */
	public static <T extends Identifiable> List<IdentifiableSelectOption<T>> fromAll(
			Iterable<T> iter,
			T selected) {
		return StreamSupport.stream(iter.spliterator(), false)
				.map(entity -> new IdentifiableSelectOption<T>(entity, selected))
				.collect(Collectors.toList());
	}

	/**
	 * Given a {@link IdentifiableEntityFinder} and the UUID of the selected object, provides a list of
	 * select
	 * options that can be used directly by mustache templates for rendering.
	 * 
	 * @param <T>
	 *            a type that extends {@link AbstractEntity} and implements {@link Identifiable}
	 * @param repo
	 *            a repository/finder for the entity type
	 * @param selectedUuid
	 *            the UUID of the selected object; may be null
	 * @return a list of select options
	 */
	public static <T extends AbstractEntity & Identifiable> List<IdentifiableSelectOption<T>> fromAll(
			IdentifiableEntityFinder<T> repo, String selectedUuid) {
		var selected = (selectedUuid == null) ? null : repo.findByUuid(selectedUuid);
		return StreamSupport.stream(repo.findAll().spliterator(), false)
				.map(entity -> new IdentifiableSelectOption<T>(entity, selected))
				.collect(Collectors.toList());
	}
}
