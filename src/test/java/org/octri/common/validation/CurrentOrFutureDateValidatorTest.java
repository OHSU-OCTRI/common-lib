package org.octri.common.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class CurrentOrFutureDateValidatorTest {

	private CurrentOrFutureDateValidator validator = new CurrentOrFutureDateValidator();

	@Test
	public void testCurrentDateIsValid() {
		assertTrue(validator.isValid(LocalDate.now(), null), "Current LocalDate should be valid");
	}

	@Test
	public void testFutureDateIsValid() {
		assertTrue(validator.isValid(LocalDate.now().plusDays(1), null), "Future LocalDate should be valid");
	}

	@Test
	public void testNullIsValid() {
		assertTrue(validator.isValid(null, null), "Null LocalDate should be valid");
	}

	@Test
	public void testPastDateIsNotValid() {
		assertFalse(validator.isValid(LocalDate.now().minusDays(1), null), "Past date should not be valid");
	}

}
