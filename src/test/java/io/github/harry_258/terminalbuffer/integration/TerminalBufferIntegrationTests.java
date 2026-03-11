package io.github.harry_258.terminalbuffer.integration;

import io.github.harry_258.terminalbuffer.TerminalBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferIntegrationTests {
    private final int width = 80;
    private final int height = 24;
    private final int scrollbackSize = 100;
    private TerminalBuffer buffer;

    @BeforeEach
    void setup() {
        buffer = new TerminalBuffer(width, height, scrollbackSize);
    }

    @Test
    void testInsertTextAtCursor() {
        String text = "Hello, world!";

        for (int i = 0; i < height; i++) {
            buffer.moveCursorTo(i, 0);
            buffer.write(text);
        }

        String insertedText = "Jetbrains ";
        String expectedLine = "Hello, Jetbrains world!";
        int targetRow = height / 2;
        buffer.moveCursorTo(targetRow, 7);

        buffer.insertText(insertedText);

        assertEquals(expectedLine, buffer.getLineAsString(targetRow + scrollbackSize));
        assertEquals(targetRow, buffer.getCursorY());
        assertEquals(expectedLine.length(), buffer.getCursorX());
    }
}
