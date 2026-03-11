package io.github.harry_258.terminalbuffer.unit;

import io.github.harry_258.terminalbuffer.RingBuffer;
import io.github.harry_258.terminalbuffer.TerminalBuffer;
import io.github.harry_258.terminalbuffer.TextAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferTests {
    private TerminalBuffer buffer;
    private RingBuffer mockRingBuffer;
    private final int width = 80;
    private final int height = 24;
    private final int scrollbackSize = 100;
    private final TextAttributes attributes = new TextAttributes(
            TextAttributes.Color.BLUE, TextAttributes.Color.RED, true, true,
            true, true, true, true, true, true
    );

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        buffer = new TerminalBuffer(width, height, scrollbackSize);

        mockRingBuffer = Mockito.mock(RingBuffer.class);
        Mockito.when(mockRingBuffer.getRowSize()).thenReturn(width);
        Mockito.when(mockRingBuffer.getScrollbackRowCount()).thenReturn(scrollbackSize);
        Mockito.when(mockRingBuffer.getRowCount()).thenReturn(height + scrollbackSize);
        Mockito.doNothing().when(mockRingBuffer).clearFormatting();
        Mockito.doNothing().when(mockRingBuffer).clearTerminal();
        Mockito.doNothing().when(mockRingBuffer).insertLineAtBottom();
        Mockito.doNothing().when(mockRingBuffer).changeScreenHeight(Mockito.anyInt());
        Mockito.doNothing().when(mockRingBuffer).changeScreenWidth(Mockito.anyInt());
        Mockito.doNothing().when(mockRingBuffer).removeCharacter(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doNothing().when(mockRingBuffer).clearScreen();
        Mockito.doNothing().when(mockRingBuffer).clearScreenFormatting();
        Mockito.doNothing().when(mockRingBuffer).fillRow(Mockito.anyInt(), Mockito.anyChar());
        Mockito.doNothing().when(mockRingBuffer).formatCell(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(TextAttributes.class));
        Mockito.doNothing().when(mockRingBuffer).formatRow(Mockito.anyInt(), Mockito.any(TextAttributes.class));
        Mockito.doNothing().when(mockRingBuffer).formatTerminal(Mockito.any(TextAttributes.class));
        Mockito.when(mockRingBuffer.getCharacter(Mockito.anyInt(), Mockito.anyInt())).thenReturn('a');
        Mockito.when(mockRingBuffer.getCellAttributes(Mockito.anyInt(), Mockito.anyInt())).thenReturn(TextAttributes.DEFAULT);
        Mockito.when(mockRingBuffer.getLineAsString(Mockito.anyInt())).thenReturn("JetBrains");
        Mockito.doNothing().when(mockRingBuffer).insertLine(Mockito.anyInt());

        Field ringBufferField = TerminalBuffer.class.getDeclaredField("ringBuffer");
        ringBufferField.setAccessible(true);
        ringBufferField.set(buffer, mockRingBuffer);
    }

    @Test
    void testMovesCursor() {
        assertEquals(0, buffer.getCursorX());
        assertEquals(0, buffer.getCursorY());

        buffer.moveCursorTo(5, 5);
        assertEquals(5, buffer.getCursorX());
        assertEquals(5, buffer.getCursorY());

        buffer.moveCursorUp(2);
        assertEquals(5, buffer.getCursorX());
        assertEquals(3, buffer.getCursorY());

        buffer.moveCursorDown(3);
        assertEquals(5, buffer.getCursorX());
        assertEquals(6, buffer.getCursorY());

        buffer.moveCursorLeft(2);
        assertEquals(3, buffer.getCursorX());
        assertEquals(6, buffer.getCursorY());

        buffer.moveCursorRight(3);
        assertEquals(6, buffer.getCursorX());
        assertEquals(6, buffer.getCursorY());
    }

    @Test
    void testMoveCursorOutOfBounds() {
        buffer.moveCursorDown(height + 100);
        assertEquals(height - 1, buffer.getCursorY());

        buffer.moveCursorUp(height + 100);
        assertEquals(0, buffer.getCursorY());

        buffer.moveCursorLeft(width + 100);
        assertEquals(0, buffer.getCursorX());

        buffer.moveCursorRight(width + 100);
        assertEquals(width, buffer.getCursorX());

        buffer.moveCursorTo(width + 100, height + 100);
        assertEquals(width, buffer.getCursorX());
        assertEquals(height - 1, buffer.getCursorY());

        buffer.moveCursorTo(-1, -1);
        assertEquals(0, buffer.getCursorX());
        assertEquals(0, buffer.getCursorY());
    }

    @Test
    void testInsertsLineAtBottom() {
        buffer.insertLineAtBottom();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).insertLineAtBottom();
    }

    @Test
    void testWrite() {
        buffer.moveCursorTo(15, 10);
        buffer.write('a');

        assertEquals(11, buffer.getCursorX());
        assertEquals(15, buffer.getCursorY());

        Mockito.verify(mockRingBuffer, Mockito.times(1)).write('a', scrollbackSize + 15, 10);
        Mockito.verify(mockRingBuffer, Mockito.never()).insertLineAtBottom();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).formatCell(scrollbackSize + 15, 10, TextAttributes.DEFAULT);
    }

    @Test
    void testWriteAtEndOfLine() {
        buffer.moveCursorTo(height - 1, width);
        buffer.write('a');

        assertEquals(1, buffer.getCursorX());
        assertEquals(height - 1, buffer.getCursorY());

        Mockito.verify(mockRingBuffer, Mockito.times(1)).insertLineAtBottom();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).write('a',  scrollbackSize + height - 1, 0);
    }

    @Test
    void testClearAllFormating() {
        buffer.clearAllFormatting();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).clearFormatting();
    }

    @Test
    void testClearAll() {
        buffer.clearTerminal();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).clearTerminal();
    }

    @Test
    void testClearTerminalAndFormatting() {
        buffer.clearTerminalAndFormatting();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).clearFormatting();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).clearTerminal();
    }

    @Test
    void testChangeScreenWidth() {
        buffer.changeScreenWidth(120);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).changeScreenWidth(120);

        buffer.changeScreenWidth(0);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).changeScreenWidth(1);

        buffer.changeScreenWidth(-50);
        Mockito.verify(mockRingBuffer, Mockito.times(2)).changeScreenWidth(1);
    }

    @Test
    void testChangeScreenHeight() {
        buffer.changeScreenHeight(40);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).changeScreenHeight(40);

        buffer.changeScreenHeight(0);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).changeScreenHeight(1);

        buffer.changeScreenHeight(-10);
        Mockito.verify(mockRingBuffer, Mockito.times(2)).changeScreenHeight(1);
    }

    @Test
    void testRemoveCharacter() {
        buffer.moveCursorTo(height - 1, 0);
        buffer.removeCharacter();

        Mockito.verify(mockRingBuffer, Mockito.times(1)).removeCharacter(scrollbackSize + height - 1, 0);
    }

    @Test
    void testClearScreen() {
        buffer.clearScreen();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).clearScreen();
    }

    @Test
    void testClearScreenFormatting() {
        buffer.clearScreenFormatting();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).clearScreenFormatting();
    }

    @Test
    void testClearScreenAndScreenFormatting() {
        buffer.clearScreenAndScreenFormatting();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).clearScreenFormatting();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).clearScreen();
    }

    @Test
    void testFillLineWithCharacter() {
        buffer.fillLineWithCharacter('a', 2);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).fillRow(scrollbackSize + 2, 'a');
    }

    @Test
    void testFormatCell() {
        buffer.formatCell(1, 2, attributes);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).formatCell(scrollbackSize + 1, 2, attributes);
    }

    @Test
    void testFormatRow() {
        buffer.formatRow(3, attributes);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).formatRow(scrollbackSize + 3, attributes);
    }

    @Test
    void testFormatTerminal() {
        buffer.formatTerminal(attributes);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).formatTerminal(attributes);
    }

    @Test
    void testGetCharacterAtCursor() {
        int row = 5;
        int column = 10;

        buffer.moveCursorTo(row, column);

        buffer.getCharacter();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getCharacter(scrollbackSize + row, column);
    }

    @Test
    void testGetCharacterAtCursorOutOfBounds() {
        int row = height + 100;
        int column = height + 100;

        buffer.moveCursorTo(row, column);

        buffer.getCharacter();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getCharacter(scrollbackSize + height - 1, width - 1);

        row = -100;
        column = -100;

        buffer.moveCursorTo(row, column);

        buffer.getCharacter();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getCharacter(scrollbackSize, 0);
    }

    @Test
    void testGetCharacter() {
        int row = 5;
        int column = 10;

        buffer.getCharacter(row, column);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getCharacter(row, column);
    }

    @Test
    void testGetCellAttributes() {
        int row = 5;
        int column = 10;

        buffer.getCellAttributes(row, column);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getCellAttributes(row, column);
    }

    @Test
    void testGetCellAttributesAtCursor() {
        int row = height + 100;
        int column = height + 100;

        buffer.moveCursorTo(row, column);

        buffer.getCellAttributes();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getCellAttributes(scrollbackSize + height - 1, width - 1);

        row = -100;
        column = -100;

        buffer.moveCursorTo(row, column);
        buffer.getCellAttributes();

        Mockito.verify(mockRingBuffer, Mockito.times(1)).getCellAttributes(scrollbackSize, 0);
    }

    @Test
    void testGetLineAsString() {
        int row = 100 + height;

        buffer.getLineAsString(row);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getLineAsString(scrollbackSize + height - 1);

        row = -100;

        buffer.getLineAsString(row);
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getLineAsString(0);
    }

    @Test
    void testGetLineAsStringAtCursor() {
        int row = 5;

        buffer.moveCursorTo(row, 0);

        buffer.getLineAsString();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getLineAsString(scrollbackSize + row);
    }

    @Test
    void testGetScreenAsString() {
        Mockito.when(mockRingBuffer.getScreenAsString()).thenReturn("JetBrains");
        buffer.getScreenAsString();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getScreenAsString();
    }

    @Test
    void testGetTerminalContent() {
        Mockito.when(mockRingBuffer.getTerminalContent()).thenReturn("JetBrains");
        buffer.getTerminalContentAsString();
        Mockito.verify(mockRingBuffer, Mockito.times(1)).getTerminalContent();
    }

    @Test
    void testInsertTextOnLine() {
        int stringLength = 100;
        int insertedRow = 13;
        String insertedText = "a".repeat(stringLength);

        buffer.insertTextOnLine(insertedRow, insertedText);

        Mockito.verify(mockRingBuffer, Mockito.times(Math.ceilDiv(stringLength, width))).insertLine(insertedRow + 1);
        assertEquals(insertedRow + 1, buffer.getCursorY());
        assertEquals(stringLength % width, buffer.getCursorX());
        Mockito.verify(mockRingBuffer, Mockito.times(stringLength)).write(Mockito.eq('a'), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void testInsertEmptyTextOnLine() {
        buffer.insertTextOnLine(10, "");

        Mockito.verify(mockRingBuffer, Mockito.never()).insertLine(Mockito.anyInt());
        Mockito.verify(mockRingBuffer, Mockito.never()).write(Mockito.anyChar(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void testWriteString() {
        String text = "Hello, world!";
        int initialCursorX = buffer.getCursorX();
        int initialCursorY = buffer.getCursorY();

        buffer.write(text);

        for (int i = 0; i < text.length(); i++) {
            Mockito.verify(mockRingBuffer, Mockito.times(1)).write(text.charAt(i), scrollbackSize + initialCursorY, initialCursorX + i);
        }
    }
}
