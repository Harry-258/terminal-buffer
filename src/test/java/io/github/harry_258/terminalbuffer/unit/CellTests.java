package io.github.harry_258.terminalbuffer.unit;

import io.github.harry_258.terminalbuffer.Cell;
import io.github.harry_258.terminalbuffer.TextAttributes;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class CellTests {
    @Property
    void testFormattedCellStringProperties(
            @ForAll char character,
            @ForAll TextAttributes.Color fg,
            @ForAll TextAttributes.Color bg,
            @ForAll boolean isBold,
            @ForAll boolean isDim,
            @ForAll boolean isItalic,
            @ForAll boolean isUnderlined,
            @ForAll boolean isBlink,
            @ForAll boolean isRapidBlink,
            @ForAll boolean isReverse,
            @ForAll boolean isHidden
    ) {
        TextAttributes attributes = new TextAttributes(
                fg, bg, isBold, isDim, isItalic, isUnderlined,
                isBlink, isRapidBlink, isReverse, isHidden
        );
        Cell cell = new Cell(character);
        cell.setAttributes(attributes);

        String result = cell.getFormattedCellString();

        assertTrue(result.startsWith("\033["), "Must start with ESC sequence");
        assertTrue(result.endsWith("\033[0m"), "Must end with reset sequence");

        String expectedColorAndChar = fg.getForegroundCode() + ";" + bg.getBackgroundCode() + "m" + character + "\033[0m";
        assertTrue(result.endsWith(expectedColorAndChar), "Must contain colors and character at the end");

        assertContainsStyle(isBold, 1, result);
        assertContainsStyle(isDim, 2, result);
        assertContainsStyle(isItalic, 3, result);
        assertContainsStyle(isUnderlined, 4, result);
        assertContainsStyle(isBlink, 5, result);
        assertContainsStyle(isRapidBlink, 6, result);
        assertContainsStyle(isReverse, 7, result);
        assertContainsStyle(isHidden, 8, result);
    }

    /**
     * Helper method to check if a string contains a style code using regex.
     * @param isExpected Whether the string should contain the style code or not.
     * @param styleCode The style code to check for.
     * @param result The string to check.
     */
    private void assertContainsStyle(boolean isExpected, int styleCode, String result) {
        String regex = "(?<=[\\[;])" + styleCode + ";";
        boolean actuallyContains = Pattern.compile(regex).matcher(result).find();

        assertEquals(isExpected, actuallyContains);
    }

    @Property
    void testClearFormatting(
            @ForAll char character,
            @ForAll TextAttributes.Color fg,
            @ForAll TextAttributes.Color bg,
            @ForAll boolean isBold,
            @ForAll boolean isDim,
            @ForAll boolean isItalic,
            @ForAll boolean isUnderlined,
            @ForAll boolean isBlink,
            @ForAll boolean isRapidBlink,
            @ForAll boolean isReverse,
            @ForAll boolean isHidden
    ) {
        TextAttributes textAttributes = new TextAttributes(
                fg, bg, isBold, isDim, isItalic, isUnderlined,
                isBlink, isRapidBlink, isReverse, isHidden
        );
        Cell cell = new Cell(character).setAttributes(textAttributes);

        cell.clearFormatting();

        TextAttributes result = cell.getAttributes();
        assertEquals(TextAttributes.Color.DEFAULT, result.background());
        assertEquals(TextAttributes.Color.DEFAULT, result.foreground());
        assertFalse(result.isBold());
        assertFalse(result.isDim());
        assertFalse(result.isItalic());
        assertFalse(result.isUnderlined());
        assertFalse(result.isBlink());
        assertFalse(result.isRapidBlink());
        assertFalse(result.isReverse());
        assertFalse(result.isHidden());
        assertEquals(character, cell.getChar());
    }
}
