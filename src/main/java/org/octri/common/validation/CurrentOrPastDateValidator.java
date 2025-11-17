package org.octri.common.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator that ensures that the given {@link LocalDate} is today or in the past (if set).
 */
public class CurrentOrPastDateValidator implements ConstraintValidator<CurrentOrPastDate, LocalDate> {

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		var today = LocalDate.now();
		return value.equals(today) || value.isBefore(today);
	}

}
