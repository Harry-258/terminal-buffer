package io.github.harry_258.terminalbuffer.unit;

import io.github.harry_258.terminalbuffer.Cell;
import io.github.harry_258.terminalbuffer.Row;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RowTests {
    // Property-based test to cover special characters.
    @Property
    void testWriteCharacter(@ForAll("randomCharacterList") List<Cell> cells) {
        int rowSize = cells.size();
        Row row = new Row(rowSize);
        for (int i = 0; i < rowSize; i++) {
            row.writeCharacter(cells.get(i), i);
        }

        for (int i = 0; i < rowSize; i++) {
            assertEquals(cells.get(i), row.getCell(i));
        }
    }

    @Provide
    Arbitrary<List<Cell>> randomCharacterList() {
        return Arbitraries.chars().map(Cell::new).list().ofMinSize(1).ofMaxSize(10);
    }

    @Property
    void testWriteCharacterOutOfBounds(
            @ForAll @IntRange(min = Integer.MIN_VALUE, max = -1) int indexLowerBound,
            @ForAll @IntRange(min = 10) int indexUpperBound
    ) {
        Row row = new Row(10);

        row.writeCharacter(new Cell('a'), 0);
        row.writeCharacter(new Cell('b'), indexUpperBound);
        row.writeCharacter(new Cell('c'), indexLowerBound);

        assertEquals('c', row.getCell(0).getChar());
        assertEquals('b', row.getCell(9).getChar());

        for (int i = 1; i < 9; i++) {
            assertEquals(' ', row.getCell(i).getChar());
        }
    }

    @Test
    void testRemoveCharacter() {
        Row row = new Row(2);

        row.writeCharacter(new Cell('a'), 0);
        row.writeCharacter(new Cell('b'), 1);

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

        row.writeCharacter(new Cell('a'), 0);
        row.writeCharacter(new Cell('b'), rowSize - 1);

        assertEquals('b', row.getCell(rowSize + upperBoundOffset).getChar());
        assertEquals('a', row.getCell(-lowerBoundOffset).getChar());
    }

    @Test
    void testClear() {
        Row row = new Row(10);

        row.writeCharacter(new Cell('a'), 0);
        row.writeCharacter(new Cell('b'), 1);
        row.writeCharacter(new Cell('c'), 2);

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
}
