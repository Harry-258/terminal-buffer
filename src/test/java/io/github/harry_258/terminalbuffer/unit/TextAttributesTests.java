package io.github.harry_258.terminalbuffer.unit;

import io.github.harry_258.terminalbuffer.TextAttributes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TextAttributesTests {
    @Test
    void testDefaultForegroundCode() {
        assertEquals(39, TextAttributes.DEFAULT.foreground().getForegroundCode());
    }

    @Test
    void testDefaultBackgroundCode() {
        assertEquals(49, TextAttributes.DEFAULT.background().getBackgroundCode());
    }

    @Test
    void testDefaultAttributes() {
        assertEquals(TextAttributes.Color.DEFAULT, TextAttributes.DEFAULT.foreground());
        assertEquals(TextAttributes.Color.DEFAULT, TextAttributes.DEFAULT.background());
        assertFalse(TextAttributes.DEFAULT.isBold());
        assertFalse(TextAttributes.DEFAULT.isDim());
        assertFalse(TextAttributes.DEFAULT.isItalic());
        assertFalse(TextAttributes.DEFAULT.isUnderlined());
        assertFalse(TextAttributes.DEFAULT.isBlink());
        assertFalse(TextAttributes.DEFAULT.isRapidBlink());
        assertFalse(TextAttributes.DEFAULT.isReverse());
        assertFalse(TextAttributes.DEFAULT.isHidden());
    }

    @Test
    void testForegroundCode() {
        assertEquals(30, TextAttributes.Color.BLACK.getForegroundCode());
        assertEquals(31, TextAttributes.Color.RED.getForegroundCode());
        assertEquals(32, TextAttributes.Color.GREEN.getForegroundCode());
        assertEquals(33, TextAttributes.Color.YELLOW.getForegroundCode());
        assertEquals(34, TextAttributes.Color.BLUE.getForegroundCode());
        assertEquals(35, TextAttributes.Color.MAGENTA.getForegroundCode());
        assertEquals(36, TextAttributes.Color.CYAN.getForegroundCode());
        assertEquals(37, TextAttributes.Color.WHITE.getForegroundCode());
    }

    @Test
    void testBackgroundCode() {
        assertEquals(40, TextAttributes.Color.BLACK.getBackgroundCode());
        assertEquals(41, TextAttributes.Color.RED.getBackgroundCode());
        assertEquals(42, TextAttributes.Color.GREEN.getBackgroundCode());
        assertEquals(43, TextAttributes.Color.YELLOW.getBackgroundCode());
        assertEquals(44, TextAttributes.Color.BLUE.getBackgroundCode());
        assertEquals(45, TextAttributes.Color.MAGENTA.getBackgroundCode());
        assertEquals(46, TextAttributes.Color.CYAN.getBackgroundCode());
        assertEquals(47, TextAttributes.Color.WHITE.getBackgroundCode());
    }
}
