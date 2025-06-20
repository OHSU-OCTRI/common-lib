package org.octri.common.view;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.octri.common.domain.AbstractEntity;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

/**
 * A {@Table} implementation that uses reflection to access all fields on the class provided.
 */
public class ReflectiveTable<T> implements Table {

	private final String displayName;
	private final Class<T> clazz;
	private final List<T> data;
	private final List<Field> fields;

	public ReflectiveTable(String displayName, Class<T> clazz, List<T> data) {
		this.displayName = displayName;
		this.clazz = clazz;
		this.data = data;
		this.fields = Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> !Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()))
				.peek(f -> f.setAccessible(true))
				.collect(Collectors.toList());
	}

	@Override
	public String displayName() {
		return displayName;
	}

	@Override
	public List<String> headers() {
		return fields.stream()
				.map(Field::getName)
				.collect(Collectors.toList());
	}

	@Override
	public List<List<String>> rows() {
		return data.stream()
				.map(instance -> fields.stream()
						.map(field -> {
							try {
								Object value = field.get(instance);
								if (value == null) {
									return "";
								}
								if (field.isAnnotationPresent(ManyToOne.class)
										|| field.isAnnotationPresent(OneToOne.class)) {
									if (value instanceof AbstractEntity ae) {
										return ae.getId() != null ? ae.getId().toString() : value.toString();
									}
								}
								return value.toString();
							} catch (IllegalAccessException e) {
								throw new RuntimeException("Failed to read field: " + field.getName(), e);
							}
						})
						.collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	public Class<T> getReflectiveClass() {
		return clazz;
	}
}
