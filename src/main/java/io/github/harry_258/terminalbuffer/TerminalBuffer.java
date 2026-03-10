package io.github.harry_258.terminalbuffer;

public class TerminalBuffer {
    private final RingBuffer ringBuffer;
    private int cursorX;
    private int cursorY;

    public TerminalBuffer(int width, int height, int scrollbackSize) {
        this.ringBuffer = new RingBuffer(height, width, scrollbackSize);
        this.cursorX = 0;
        this.cursorY = 0;
    }

    /**
     * Writes the provided character at the current cursor position and advances the cursor. If the cursor
     * is at the end of the line, it wraps to the next line and overwrites the first character of the line.
     * @param character The character to write.
     */
    public void write(char character) {
        if (cursorX >= ringBuffer.getRowSize()) {
            cursorX = 0;
            cursorY++;
        }

        int screenHeight = ringBuffer.getRowCount() - ringBuffer.getScrollbackRowCount();
        if (cursorY >= screenHeight) {
            insertLineAtBottom();
            cursorY = screenHeight - 1;
        }

        ringBuffer.write(character, cursorY + ringBuffer.getScrollbackRowCount(), cursorX);
        cursorX++;
    }

    /**
     * Inserts an empty line at the bottom of the screen.
     */
    public void insertLineAtBottom() {
        ringBuffer.insertLineAtBottom();
    }

    /**
     * Moves the cursor up by the specified number of rows.
     * @param rows The number of rows to move up.
     */
    public void moveCursorUp(int rows) {
        this.cursorY = Math.max(0, this.cursorY - rows);
    }

    /**
     * Moves the cursor down by the specified number of rows.
     * @param rows The number of rows to move down.
     */
    public void moveCursorDown(int rows) {
        this.cursorY = Math.min(ringBuffer.getRowCount() - ringBuffer.getScrollbackRowCount() - 1, this.cursorY + rows);
    }

    /**
     * Moves the cursor left by the specified number of columns.
     * @param columns The number of columns to move left.
     */
    public void moveCursorLeft(int columns) {
        this.cursorX = Math.max(0, this.cursorX - columns);
    }

    /**
     * Moves the cursor right by the specified number of columns.
     * @param columns The number of columns to move right.
     */
    public void moveCursorRight(int columns) {
        this.cursorX = Math.min(ringBuffer.getRowSize(), this.cursorX + columns);
    }

    /**
     * Moves the cursor to the specified row and column inside the screen. Clamps the coordinates to the screen dimensions.
     * @param row The row to move to.
     * @param column The column to move to.
     */
    public void moveCursorTo(int row, int column) {
        this.cursorX = Math.clamp(column, 0, ringBuffer.getRowSize() - 1);
        this.cursorY = Math.clamp(row, 0, ringBuffer.getRowCount() - ringBuffer.getScrollbackRowCount() - 1);
    }

    /**
     * Gets the current X position of the cursor.
     * @return The current X position of the cursor.
     */
    public int getCursorX() {
        return cursorX;
    }

    /**
     * Gets the current Y position of the cursor.
     * @return The current Y position of the cursor.
     */
    public int getCursorY() {
        return cursorY;
    }

    /**
     * Clears all formatting from the terminal.
     */
    public void clearAllFormatting() {
        ringBuffer.clearFormatting();
    }

    /**
     * Clears the terminal of all characters.
     */
    public void clearTerminal() {
        ringBuffer.clear();
    }

    /**
     * Clears all the characters in the terminal and resets the formatting to default.
     */
    public void clearTerminalAndFormatting() {
        clearAllFormatting();
        clearTerminal();
    }
}
