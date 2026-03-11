package io.github.harry_258.terminalbuffer.unit;

import io.github.harry_258.terminalbuffer.Cell;
import io.github.harry_258.terminalbuffer.Row;
import io.github.harry_258.terminalbuffer.TextAttributes;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RowTests {
    // Property-based test to cover special characters.
    @Property
    void testWriteCharacter(@ForAll List<Character> cells) {
        int rowSize = cells.size();
        Row row = new Row(rowSize);
        for (int i = 0; i < rowSize; i++) {
            row.writeCharacter(cells.get(i), i);
        }

        for (int i = 0; i < rowSize; i++) {
            assertEquals(cells.get(i), row.getCell(i).getChar());
        }
    }

    @Property
    void testWriteCharacterOutOfBounds(
            @ForAll @IntRange(min = Integer.MIN_VALUE, max = -1) int indexLowerBound,
            @ForAll @IntRange(min = 10) int indexUpperBound
    ) {
        Row row = new Row(10);

        row.writeCharacter('a', 0);
        row.writeCharacter('b', indexUpperBound);
        row.writeCharacter('c', indexLowerBound);

        assertEquals('c', row.getCell(0).getChar());
        assertEquals('b', row.getCell(9).getChar());

        for (int i = 1; i < 9; i++) {
            assertEquals(' ', row.getCell(i).getChar());
        }
    }

    @Test
    void testRemoveCharacter() {
        Row row = new Row(2);

        row.writeCharacter('a', 0);
        row.writeCharacter('b', 1);

        // Remove 'a'
        row.removeCharacter(0);
        // 'b' should be at index 0, so this call should not remove it.
        row.removeCharacter(1);

        assertEquals('b', row.getCell(0).getChar());
        assertEquals(' ', row.getCell(1).getChar());
    }

    @Property
    void testGetCellOutOfBounds(
            @ForAll @IntRange(min = 1, max = 1000) int lowerBoundOffset,
            @ForAll @IntRange(min = 0, max = 1000) int upperBoundOffset,
            @ForAll @IntRange(min = 2, max = 1000) int rowSize
    ) {
        Row row = new Row(rowSize);

        assertEquals(' ', row.getCell(rowSize + upperBoundOffset).getChar());
        assertEquals(' ', row.getCell(-lowerBoundOffset).getChar());

        row.writeCharacter('a', 0);
        row.writeCharacter('b', rowSize - 1);

        assertEquals('b', row.getCell(rowSize + upperBoundOffset).getChar());
        assertEquals('a', row.getCell(-lowerBoundOffset).getChar());
    }

    @Test
    void testClear() {
        Row row = new Row(10);

        row.writeCharacter('a', 0);
        row.writeCharacter('b', 1);
        row.writeCharacter('c', 2);

        row.clear();

        for (int i = 0; i < 10; i++) {
            assertEquals(' ', row.getCell(i).getChar());
        }
    }

    @Test
    void testFillWithCharacter() {
        Row row = new Row(10);
        row.fillWithCharacter('a');

        for (int i = 0; i < 10; i++) {
            assertEquals('a', row.getCell(i).getChar());
        }
    }

    @Test
    void testClearFormatting() {
        Row row = new Row(10);

        row.writeCharacter('a', 0);
        row.getCell(0).setAttributes(new TextAttributes(
                TextAttributes.Color.BLUE, TextAttributes.Color.RED, true, true,
                true, true, true, true, true, true
        ));
        row.writeCharacter('b', 5);
        row.getCell(5).setAttributes(new TextAttributes(
                TextAttributes.Color.CYAN, TextAttributes.Color.WHITE, true, true,
                false, false, true, false, true, true
        ));
        row.writeCharacter('c', 8);

        row.clearFormatting();

        for (int i = 0; i < 10; i++) {
            assertEquals(TextAttributes.DEFAULT, row.getCell(i).getAttributes());
        }
    }

    @Test
    void testChangeSizeEmptyRow() {
        Row row = new Row(10);
        int newSize = 4;

        List<Cell> extraCells = row.changeSize(newSize, List.of());

        assertEquals(newSize, row.getSize());
        assertEquals(0, extraCells.size());

        newSize = 12;
        extraCells = row.changeSize(newSize, List.of());

        assertEquals(newSize, row.getSize());
        assertEquals(0, extraCells.size());
    }

    @Test
    void testChangeSizeOverflowingRow() {
        Row row = new Row(5);
        int newSize = 2;

        row.writeCharacter('a', 0);
        row.writeCharacter('b', 1);
        row.writeCharacter('c', 2);
        row.writeCharacter('d', 3);
        row.writeCharacter('e', 4);

        List<Character> result = row.changeSize(newSize, List.of()).stream().map(Cell::getChar).toList();

        assertIterableEquals(
                List.of('c', 'd', 'e'),
                result
        );

        assertEquals(newSize, row.getSize());
        assertEquals('a', row.getCell(0).getChar());
        assertEquals('b', row.getCell(1).getChar());
    }

    @Test
    void testChangeSizeWithReminderCells() {
        Row row = new Row(5);
        row.writeCharacter('x', 1);
        row.writeCharacter('y', 2);
        row.writeCharacter('z', 3);

        int newSize = 10;

        List<Cell> reminderCells = row.changeSize(newSize, List.of(
                new Cell('a'), new Cell('b'), new Cell('c')
        ));

        assertEquals(0, reminderCells.size());
        assertEquals(newSize, row.getSize());
        assertEquals('a', row.getCell(0).getChar());
        assertEquals('b', row.getCell(1).getChar());
        assertEquals('c', row.getCell(2).getChar());
        assertEquals(' ', row.getCell(3).getChar());
        assertEquals('x', row.getCell(4).getChar());
        assertEquals('y', row.getCell(5).getChar());
        assertEquals('z', row.getCell(6).getChar());

        newSize = 4;

        List<Cell> reminderCells2 =  row.changeSize(newSize, List.of(
                new Cell('d'), new Cell('e')
        ));

        assertEquals(newSize, row.getSize());
        assertEquals('d', row.getCell(0).getChar());
        assertEquals('e', row.getCell(1).getChar());
        assertEquals('a', row.getCell(2).getChar());
        assertEquals('b', row.getCell(3).getChar());
        assertIterableEquals(
                List.of('c', ' ', 'x', 'y', 'z'),
                reminderCells2.stream().map(Cell::getChar).toList()
        );
    }

    @Test
    void testChangeSizeKeepsFormattedTrailingSpaces() {
        Row row = new Row(3);

        TextAttributes customAttributes = new TextAttributes(
                TextAttributes.Color.BLUE, TextAttributes.Color.RED, true, true,
                true, true, true, true, true, true
        );
        row.getCell(2).setAttributes(customAttributes);

        List<Cell> extraCells = row.changeSize(1, List.of());

        assertEquals(1, row.getSize());
        assertEquals(2, extraCells.size());
        assertEquals(' ', extraCells.get(1).getChar());
        assertEquals(customAttributes, extraCells.get(1).getAttributes());
    }

    @Test
    void testSetCellOutOfBounds() {
        int size = 5;
        Row row = new Row(size);

        row.setCell(new Cell('a'), - size - 100);
        row.setCell(new Cell('b'), size + 100);

        assertEquals('a', row.getCell(0).getChar());
        assertEquals('b', row.getCell(size - 1).getChar());
    }
}
