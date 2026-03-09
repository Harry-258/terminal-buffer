package io.github.harry_258.terminalbuffer.unit;

import io.github.harry_258.terminalbuffer.TerminalBuffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferTests {
    @Test
    void testMovesCursor() {
        TerminalBuffer buffer = new TerminalBuffer(10, 10, 10);

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
        TerminalBuffer buffer = new TerminalBuffer(10, 20, 10);

        buffer.moveCursorDown(100);
        assertEquals(19, buffer.getCursorY());

        buffer.moveCursorUp(100);
        assertEquals(0, buffer.getCursorY());

        buffer.moveCursorLeft(100);
        assertEquals(0, buffer.getCursorX());

        buffer.moveCursorRight(100);
        assertEquals(9, buffer.getCursorX());

        buffer.moveCursorTo(20, 200);
        assertEquals(9, buffer.getCursorX());
        assertEquals(19, buffer.getCursorY());

        buffer.moveCursorTo(-1, -1);
        assertEquals(0, buffer.getCursorX());
        assertEquals(0, buffer.getCursorY());
    }
}
