package org.octri.common.view;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.octri.common.domain.AbstractEntity;

/**
 * Used for rendering mustache templates. Helper functions for creating a list of select input options.
 *
 * @author lawhead
 * @param <T>
 *            type of items in the select list
 *
 */
public class OptionList<T> {

	/**
	 * Given a Repository search result of lookups and the selected lookup item, provides a list of objects that can be
	 * used directly by mustachejs for rendering.
	 *
	 * @param <T>
	 *            an entity type extending {@link AbstractEntity} and implementing {@link Labelled}
	 * @param iter
	 *            iterable collection
	 * @param selected
	 *            the current selection
	 * @return a list of select options for the items in the collection
	 */
	public static <T extends AbstractEntity & Labelled> List<EntitySelectOption<T>> fromSearch(Iterable<T> iter,
			T selected) {
		return StreamSupport.stream(iter.spliterator(), false)
				.map(area -> new EntitySelectOption<T>(area, selected))
				.collect(Collectors.toList());
	}

	/**
	 * Used for multi-selects. Given a Repository search result of lookups and a list of selected lookup, provides a
	 * list of objects that can be used directly by mustachejs for rendering.
	 *
	 * @param <T>
	 *            an entity type extending {@link AbstractEntity} and implementing {@link Labelled}
	 * @param iter
	 *            iterable collection
	 * @param selected
	 *            the current selection
	 * @return a list of select options for the items in the collection
	 */
	public static <T extends AbstractEntity & Labelled> List<EntitySelectOption<T>> multiFromSearch(Iterable<T> iter,
			Collection<T> selected) {
		return StreamSupport.stream(iter.spliterator(), false)
				.map(area -> new EntitySelectOption<T>(area, selected))
				.collect(Collectors.toList());
	}

	/**
	 * Generates a list of integers in the given range from which to choose.
	 *
	 * @param start
	 *            lower bound of the range
	 * @param end
	 *            upper bound of the range
	 * @param selected
	 *            the current selection
	 * @return a list of select options from start to end
	 */
	public static List<SelectOption<Integer>> forRange(Integer start, Integer end, Integer selected) {
		return IntStream.rangeClosed(start, end)
				.mapToObj(i -> new SelectOption<Integer>(Integer.valueOf(i), selected))
				.collect(Collectors.toList());
	}

	/**
	 * Generates a list of strings.
	 *
	 * @param items
	 *            list of strings
	 * @param selected
	 *            the current selection
	 * @return a list of select options for the given strings
	 */
	public static List<SelectOption<String>> forStrings(List<String> items, String selected) {
		return items.stream()
				.map(value -> new SelectOption<String>(value, selected))
				.collect(Collectors.toList());
	}

	/**
	 * Given a collection of Enum values and the selected value, provides a list of objects that can be used directly by
	 * mustachejs for rendering.
	 *
	 * @param <T>
	 *            an Enum type that implements the {@link Labelled} interface
	 * @param iter
	 *            an iterable collection of Enum values
	 * @param selected
	 *            the current selection
	 * @return a list of select options for the enum values in the collection
	 */
	public static <T extends Enum<T> & Labelled> List<EnumSelectOption<T>> fromEnum(Iterable<T> iter, T selected) {
		return StreamSupport.stream(iter.spliterator(), false)
				.map(item -> new EnumSelectOption<T>(item, selected))
				.collect(Collectors.toList());
	}
}
