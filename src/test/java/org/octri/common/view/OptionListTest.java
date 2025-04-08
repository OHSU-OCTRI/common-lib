package org.octri.common.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

public class OptionListTest {

    enum Answer implements Labelled {

        YES, NO, MAYBE;

        @Override
        public String getLabel() {
            return this.name().toLowerCase();
        }
    }

    @Test
    void testForRange() {
        var opts = OptionList.forRange(1, 10, 3);
        assertEquals(10, opts.size());
        assertEquals(1, opts.get(0).getChoice());
        assertFalse(opts.get(0).getSelected());

        assertEquals(3, opts.get(2).getChoice());
        assertTrue(opts.get(2).getSelected());
    }

    @Test
    void testFromEnum() {
        var opts = OptionList.fromEnum(EnumSet.allOf(Answer.class), Answer.NO);
        assertEquals(3, opts.size());
        assertEquals(Answer.YES, opts.get(0).getChoice());
        assertFalse(opts.get(0).getSelected());

        assertEquals(Answer.NO, opts.get(1).getChoice());
        assertTrue(opts.get(1).getSelected());
    }
}
