package org.octri.common.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ExcelExporterTest {

	/**
	 * Test rules
	 *
	 * Sheet names cannot:
	 * - Be blank .
	 * - Contain more than 31 characters.
	 * - Contain any of the following characters: / \ ? * : [ ]
	 * - Begin or end with an apostrophe ('), but they can be used in between text or numbers in a name.
	 * - Be named "History". This is a reserved word Excel uses internally.
	 */
	@Test
	public void testSheetNameRemoveValues() {
		ExcelExporter exporter = new ExcelExporter();
		assertEquals("Survey Label", exporter.createSheetName("Survey Label"));

		assertEquals("Survey 02-17-2016", exporter.createSheetName("Survey 02-17-2016"));
		assertEquals("Survey 02 17 2016", exporter.createSheetName("Survey 02/17/2016"));

		assertEquals("Survey number 1", exporter.createSheetName("Survey [number 1]"));
		assertEquals("1 wk Followup Safety & Satisfa",
				exporter.createSheetName("1 wk Followup: Safety & Satisfaction"));

		assertEquals("Hello world", exporter.createSheetName("Hello *world*?"));

		assertEquals("I'm here", exporter.createSheetName("I'm here"));
		assertEquals("Hi 'world", exporter.createSheetName("Hi 'world'"));

		assertEquals("empty", exporter.createSheetName(""));
		assertEquals("null", exporter.createSheetName(null));

		assertEquals("hx", exporter.createSheetName("History"));
	}

	@Test
	public void testDuplicateSheetNameInWorkbook() {
		ExcelExporter exporter = new ExcelExporter();
		assertEquals("survey", exporter.createSheetName("survey"));
		assertEquals("survey 2", exporter.createSheetName("survey"));
		assertEquals("survey 3", exporter.createSheetName("survey"));
		assertEquals("survey 4", exporter.createSheetName("survey"));

		exporter.reset();
		assertEquals("survey", exporter.createSheetName("survey"));
	}

	@Test
	public void testDuplicateSheetNameWithMaxChars() {
		ExcelExporter exporter = new ExcelExporter();
		assertEquals("this is a name that exceeds len", exporter.createSheetName("this is a name that exceeds length"));
		assertEquals("this is a name that exceeds l 2", exporter.createSheetName("this is a name that exceeds length"));
		assertEquals("this is a name that exceeds l 3", exporter.createSheetName("this is a name that exceeds len**?"));
	}

	@Test
	public void testDuplicateSheetNameSpecialChars() {
		ExcelExporter exporter = new ExcelExporter();
		assertEquals("Survey 02 17 2016", exporter.createSheetName("Survey 02/17/2016"));
		assertEquals("Survey 02 17 2016 2", exporter.createSheetName("Survey 02 17 2016"));
	}

	@Test
	public void testDuplicateSheetNameEmpty() {
		ExcelExporter exporter = new ExcelExporter();
		assertEquals("empty", exporter.createSheetName(""));
		assertEquals("empty 2", exporter.createSheetName(""));
		assertEquals("empty 3", exporter.createSheetName("'[*?:]'"));
	}
}
