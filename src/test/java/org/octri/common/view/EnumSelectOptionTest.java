package org.octri.common.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class EnumSelectOptionTest {

	enum Color implements Labelled {

		RED, GREEN, BLUE;

		@Override
		public String getLabel() {
			return this.name().toLowerCase();
		}
	}

	@Test
	void testNotSelected() {
		var opt = new EnumSelectOption<Color>(Color.RED, Color.BLUE);
		assertEquals(Color.RED, opt.getChoice());
		assertFalse(opt.getSelected());
		assertEquals("red", opt.getLabel());
		assertEquals("RED", opt.getValue());
	}

	@Test
	void testSelected() {
		var opt = new EnumSelectOption<Color>(Color.RED, Color.RED);
		assertEquals(Color.RED, opt.getChoice());
		assertTrue(opt.getSelected());
		assertEquals("red", opt.getLabel());
		assertEquals("RED", opt.getValue());
	}

	@Test
	void testCollectionNoneSelected() {
		var opt = new EnumSelectOption<Color>(Color.RED, List.of());
		assertEquals(Color.RED, opt.getChoice());
		assertFalse(opt.getSelected());
		assertEquals("red", opt.getLabel());
		assertEquals("RED", opt.getValue());
	}

	@Test
	void testCollectionNotSelected() {
		var opt = new EnumSelectOption<Color>(Color.RED, List.of(Color.BLUE, Color.GREEN));
		assertEquals(Color.RED, opt.getChoice());
		assertFalse(opt.getSelected());
		assertEquals("red", opt.getLabel());
		assertEquals("RED", opt.getValue());
	}

	@Test
	void testCollectionSelected() {
		var opt = new EnumSelectOption<Color>(Color.RED, List.of(Color.BLUE, Color.RED));
		assertTrue(opt.getSelected());
	}
}
