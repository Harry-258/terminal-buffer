package io.github.harry_258.terminalbuffer.unit;

import io.github.harry_258.terminalbuffer.Character;
import io.github.harry_258.terminalbuffer.Row;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RowTests {
    // Property-based test to cover special characters.
    @Property
    void testWriteCharacter(@ForAll("randomCharacterList") List<Character> characters) {
        int rowSize = characters.size();
        Row row = new Row(rowSize);
        for (int i = 0; i < rowSize; i++) {
            row.writeCharacter(characters.get(i), i);
        }

        for (int i = 0; i < rowSize; i++) {
            assertEquals(characters.get(i), row.getCharacter(i));
        }
    }

    @Provide
    Arbitrary<List<Character>> randomCharacterList() {
        return Arbitraries.chars().map(Character::new).list().ofMinSize(1).ofMaxSize(10);
    }

    @Property
    void testWriteCharacterOutOfBounds(
            @ForAll @IntRange(min = Integer.MIN_VALUE, max = -1) int indexLowerBound,
            @ForAll @IntRange(min = 10) int indexUpperBound
    ) {
        Row row = new Row(10);

        row.writeCharacter(new Character('a'), 0);
        row.writeCharacter(new Character('b'), indexUpperBound);
        row.writeCharacter(new Character('c'), indexLowerBound);

        assertEquals('c', row.getCharacter(0).getChar());
        assertEquals('b', row.getCharacter(9).getChar());

        for (int i = 1; i < 9; i++) {
            assertEquals(' ', row.getCharacter(i).getChar());
        }
    }

    @Test
    void testRemoveCharacter() {
        Row row = new Row(2);

        row.writeCharacter(new Character('a'), 0);
        row.writeCharacter(new Character('b'), 1);

        // Remove 'a'
        row.removeCharacter(0);
        // 'b' should be at index 0, so this call should not remove it.
        row.removeCharacter(1);

        assertEquals('b', row.getCharacter(0).getChar());
        assertEquals(' ', row.getCharacter(1).getChar());
    }

    @Property
    void testGetCharacterOutOfBounds(
            @ForAll @IntRange(min = 1, max = 1000) int lowerBoundOffset,
            @ForAll @IntRange(min = 0, max = 1000) int upperBoundOffset,
            @ForAll @IntRange(min = 2, max = 1000) int rowSize
    ) {
        Row row = new Row(rowSize);

        assertEquals(' ', row.getCharacter(rowSize + upperBoundOffset).getChar());
        assertEquals(' ', row.getCharacter(-lowerBoundOffset).getChar());

        row.writeCharacter(new Character('a'), 0);
        row.writeCharacter(new Character('b'), rowSize - 1);

        assertEquals('b', row.getCharacter(rowSize + upperBoundOffset).getChar());
        assertEquals('a', row.getCharacter(-lowerBoundOffset).getChar());
    }

    @Test
    void testClear() {
        Row row = new Row(10);

        row.writeCharacter(new Character('a'), 0);
        row.writeCharacter(new Character('b'), 1);
        row.writeCharacter(new Character('c'), 2);

        row.clear();

        for (int i = 0; i < 10; i++) {
            assertEquals(' ', row.getCharacter(i).getChar());
        }
    }

    @Test
    void testFillWithCharacter() {
        Row row = new Row(10);
        row.fillWithCharacter('a');

        for (int i = 0; i < 10; i++) {
            assertEquals('a', row.getCharacter(i).getChar());
        }
    }
}
