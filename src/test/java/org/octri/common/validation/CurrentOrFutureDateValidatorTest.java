package org.octri.common.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class CurrentOrFutureDateValidatorTest {

	private CurrentOrFutureDateValidator validator = new CurrentOrFutureDateValidator();

	@Test
	public void testCurrentDateIsValid() {
		assertTrue("Current LocalDate should be valid", validator.isValid(LocalDate.now(), null));
	}

	@Test
	public void testFutureDateIsValid() {
		assertTrue("Future LocalDate should be valid", validator.isValid(LocalDate.now().plusDays(1), null));
	}

	@Test
	public void testNullIsValid() {
		assertTrue("Null LocalDate should be valid", validator.isValid(null, null));
	}

	@Test
	public void testPastDateIsNotValid() {
		assertFalse("Past date should not be valid", validator.isValid(LocalDate.now().minusDays(1), null));
	}

}
