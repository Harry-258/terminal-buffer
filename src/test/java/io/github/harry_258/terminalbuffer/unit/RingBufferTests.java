package io.github.harry_258.terminalbuffer.unit;

import io.github.harry_258.terminalbuffer.RingBuffer;
import io.github.harry_258.terminalbuffer.TextAttributes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RingBufferTests {
    int screenRowCount = 3;
    int rowSize = 5;
    int scrollbackSize = 1;

    @Test
    void testInsertLineAtBottom() {
        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        buffer.write('a', 0);
        buffer.insertLineAtBottom();
        buffer.write('b', 0);
        buffer.insertLineAtBottom();
        buffer.write('c', 0);

        for (int i = 0; i < rowSize; i++) {
            assertEquals(' ', buffer.getRow(0).getCell(i).getChar());
        }
        assertEquals('a', buffer.getRow(1).getCell(0).getChar());
        assertEquals('b', buffer.getRow(2).getCell(0).getChar());
        assertEquals('c', buffer.getRow(3).getCell(0).getChar());
    }

    @Test
    void testWrite() {
        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);

        buffer.write('a', 0, 3);
        buffer.write('b', 2, 4);

        assertEquals('a', buffer.getRow(0).getCell(3).getChar());
        assertEquals('b', buffer.getRow(2).getCell(4).getChar());

        // Overwrite 'a'
        buffer.write('c', 4, 3);
        assertEquals('c', buffer.getRow(0).getCell(3).getChar());
    }

    @Test
    void testWriteAtBottom() {
        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);

        buffer.write('a', 0);

        assertEquals('a', buffer.getRow(screenRowCount + scrollbackSize - 1).getCell(0).getChar());
        for (int i = 0; i < screenRowCount + scrollbackSize - 1; i++) {
            assertEquals(' ', buffer.getRow(i).getCell(0).getChar());
        }
    }

    @Test
    void testFormatCell() {
        TextAttributes textAttributes = new TextAttributes(
                TextAttributes.Color.BLUE, TextAttributes.Color.RED, true, true,
                true, true, true, true, true, true
        );
        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        buffer.formatCell(0, 0, textAttributes);
        buffer.formatCell(3, 4, textAttributes);

        assertEquals(textAttributes, buffer.getRow(0).getCell(0).getAttributes());
        assertEquals(textAttributes, buffer.getRow(3).getCell(4).getAttributes());
    }

    @Test
    void testClearFormatting() {
        TextAttributes textAttributes = new TextAttributes(
                TextAttributes.Color.BLUE, TextAttributes.Color.RED, true, true,
                true, true, true, true, true, true
        );
        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        buffer.formatCell(0, 0, textAttributes);
        buffer.formatCell(3, 4, textAttributes);

        buffer.clearFormatting();

        assertEquals(TextAttributes.DEFAULT, buffer.getRow(0).getCell(0).getAttributes());
        assertEquals(TextAttributes.DEFAULT, buffer.getRow(3).getCell(4).getAttributes());
    }

    @Test
    void testClear() {
        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        buffer.write('a', 0, 3);
        buffer.write('b', 1, 4);
        buffer.write('c', 2, 2);

        buffer.clear();

        for (int i = 0; i < screenRowCount + scrollbackSize; i++) {
            for (int j = 0; j < rowSize; j++) {
                assertEquals(' ', buffer.getRow(i).getCell(j).getChar());
            }
        }
    }

    @Test
    void testChangeScreenHeightToSmallerHeight() {
        int scrollbackSize = 1;
        int newScreenHeight = 1;

        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        buffer.write('a', 0);
        buffer.insertLineAtBottom();
        buffer.write('b', 1);
        buffer.insertLineAtBottom();
        buffer.write('c', 2);

        buffer.changeScreenHeight(newScreenHeight);

        assertEquals(newScreenHeight + scrollbackSize, buffer.getRowCount());
        assertEquals('b', buffer.getRow(0).getCell(1).getChar());
        assertEquals('c', buffer.getRow(1).getCell(2).getChar());
    }

    @Test
    void testChangeScreenHeightToLargerHeight() {
        int scrollbackSize = 1;
        int newScreenHeight = 10;

        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        buffer.write('a', 0);
        buffer.insertLineAtBottom();
        buffer.write('b', 1);
        buffer.insertLineAtBottom();
        buffer.write('c', 2);

        buffer.changeScreenHeight(newScreenHeight);

        assertEquals(newScreenHeight + scrollbackSize, buffer.getRowCount());
        assertEquals('a', buffer.getRow(newScreenHeight + scrollbackSize - 3).getCell(0).getChar());
        assertEquals('b', buffer.getRow(newScreenHeight + scrollbackSize - 2).getCell(1).getChar());
        assertEquals('c', buffer.getRow(newScreenHeight + scrollbackSize - 1).getCell(2).getChar());
    }

    @Test
    void testChangeScreenHeightToSameHeight() {
        int newScreenHeight = 3;
        int screenHeight = 3;

        RingBuffer buffer = new RingBuffer(screenHeight, rowSize, scrollbackSize);
        buffer.write('a', 0);
        buffer.insertLineAtBottom();
        buffer.write('b', 1);
        buffer.insertLineAtBottom();
        buffer.write('c', 2);

        buffer.changeScreenHeight(newScreenHeight);

        assertEquals(newScreenHeight + scrollbackSize, buffer.getRowCount());
        assertEquals('a', buffer.getRow(newScreenHeight + scrollbackSize - 3).getCell(0).getChar());
        assertEquals('b', buffer.getRow(newScreenHeight + scrollbackSize - 2).getCell(1).getChar());
        assertEquals('c', buffer.getRow(newScreenHeight + scrollbackSize - 1).getCell(2).getChar());
    }

    @Test
    void testChangeScreenWidthToSmallerWidth() {
        int newScreenWidth = 1;

        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        buffer.fillRow(screenRowCount + scrollbackSize - 1, 'a');
        buffer.fillRow(screenRowCount + scrollbackSize - 2, 'b');
        buffer.fillRow(screenRowCount + scrollbackSize - 3, 'c');
        buffer.fillRow(screenRowCount + scrollbackSize - 4, 'd');

        buffer.changeScreenWidth(newScreenWidth);

        assertEquals(newScreenWidth, buffer.getRowSize());

        for (int i = 0; i < screenRowCount; i++) {
            assertEquals(newScreenWidth, buffer.getRow(i).getSize());
            assertEquals('a', buffer.getRow(i).getCell(0).getChar());
        }
    }

    @Test
    void testChangeScreenWidthToLargerWidth() {
        int screenRowCount = 4;
        int rowSize = 4;
        int scrollbackSize = 0;
        int newScreenWidth = 10;

        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        List<Character> characters = List.of('a', 'b', 'c', 'd');

        for (int i = 0; i < screenRowCount; i++) {
            buffer.fillRow(i, characters.get(i));
        }

        buffer.changeScreenWidth(newScreenWidth);

        for (int i = 0; i < screenRowCount; i++) {
            for (int j = 0; j < rowSize; j++) {
                assertEquals(characters.get(i), buffer.getRow(i).getCell(j).getChar());
            }
            for (int j = rowSize; j < newScreenWidth; j++) {
                assertEquals(' ', buffer.getRow(i).getCell(j).getChar());
            }
        }
    }

    @Test
    void testChangeScreenWidthToSameWidth() {
        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);
        List<Character> characters = List.of('a', 'b', 'c', 'd');

        for (int i = 0; i < screenRowCount; i++) {
            buffer.fillRow(i, characters.get(i));
        }

        buffer.changeScreenWidth(rowSize);

        for (int i = 0; i < screenRowCount; i++) {
            assertEquals(rowSize, buffer.getRow(i).getSize());
            for (int j = 0; j < rowSize; j++) {
                assertEquals(characters.get(i), buffer.getRow(i).getCell(j).getChar());
            }
        }
    }

    @Test
    void testChangeScreenWidthToSmallerWidthAndHeight() {
        int screenRowCount = 4;
        int rowSize = 4;
        int scrollbackSize = 0;
        int newScreenWidth = 2;
        int newScreenHeight = 2;

        RingBuffer buffer = new RingBuffer(screenRowCount, rowSize, scrollbackSize);

        buffer.fillRow(0, 'a');
        buffer.fillRow(1, 'b');
        buffer.fillRow(2, 'c');
        buffer.fillRow(3, 'd');

        buffer.changeScreenWidth(newScreenWidth);
        buffer.changeScreenHeight(newScreenHeight);

        assertEquals(newScreenWidth, buffer.getRowSize());
        assertEquals(newScreenHeight, buffer.getRowCount());

        assertEquals('d', buffer.getRow(0).getCell(0).getChar());
        assertEquals('d', buffer.getRow(0).getCell(1).getChar());

        assertEquals('d', buffer.getRow(1).getCell(0).getChar());
        assertEquals('d', buffer.getRow(1).getCell(1).getChar());
    }
}
