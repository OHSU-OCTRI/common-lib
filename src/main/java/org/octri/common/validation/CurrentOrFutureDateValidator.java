package org.octri.common.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator that ensures that the given {@link LocalDate} is today or in the future (if set).
 */
public class CurrentOrFutureDateValidator implements ConstraintValidator<CurrentOrFutureDate, LocalDate> {

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		var today = LocalDate.now();
		return value.equals(today) || value.isAfter(today);
	}

}
