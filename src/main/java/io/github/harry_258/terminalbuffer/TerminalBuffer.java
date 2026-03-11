package io.github.harry_258.terminalbuffer;

public class TerminalBuffer {
    private final RingBuffer ringBuffer;
    private int cursorX;
    private int cursorY;
    private TextAttributes defaultAttributes = TextAttributes.DEFAULT;

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
        ringBuffer.formatCell(cursorY + ringBuffer.getScrollbackRowCount(), cursorX, defaultAttributes);
        cursorX++;
    }

    /**
     * Removes the character at the current cursor position.
     */
    public void removeCharacter() {
        ringBuffer.removeCharacter(cursorY + ringBuffer.getScrollbackRowCount(), cursorX);
    }

    /**
     * Fills the row on the screen at the given index with the provided character.
     * @param character The character to fill the row with.
     * @param rowIndex The index of the row to fill. The index is relative to the top of the screen.
     */
    public void fillLineWithCharacter(char character, int rowIndex) {
        ringBuffer.fillRow(rowIndex + ringBuffer.getScrollbackRowCount(), character);
    }

    /**
     * Sets the default attributes for writing new characters to the screen.
     * The characters that are already on the screen or in the buffer will not change any attributes.
     * @param attributes The new attributes.
     */
    public void setDefaultAttributes(TextAttributes attributes) {
        this.defaultAttributes = attributes;
    }

    /**
     * Changes the style attributes of a cell.
     * @param row The row of the cell.
     * @param column The column of the cell.
     * @param attributes The new attributes.
     */
    public void formatCell(int row, int column, TextAttributes attributes) {
        ringBuffer.formatCell(row + ringBuffer.getScrollbackRowCount(), column, attributes);
    }

    /**
     * Changes the style attributes of all cells in a row.
     * @param row The row to change.
     * @param attributes The new attributes.
     */
    public void formatRow(int row, TextAttributes attributes) {
        ringBuffer.formatRow(row + ringBuffer.getScrollbackRowCount(), attributes);
    }

    /**
     * Changes the style attributes of all cells on the screen and in the scrollback buffer
     * @param attributes The new attributes.
     */
    public void formatTerminal(TextAttributes attributes) {
        ringBuffer.formatTerminal(attributes);
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
        this.cursorX = Math.clamp(column, 0, ringBuffer.getRowSize());
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
        ringBuffer.clearTerminal();
    }

    /**
     * Clears all the characters in the terminal and resets the formatting to default.
     */
    public void clearTerminalAndFormatting() {
        clearAllFormatting();
        clearTerminal();
    }

    /**
     * Clears all the characters on the screen. Does not clear the scrollback buffer.
     */
    public void clearScreen() {
        ringBuffer.clearScreen();
    }

    /**
     * Clears the formatting of all the characters on the screen. Does not influence the scrollback buffer.
     */
    public void clearScreenFormatting() {
        ringBuffer.clearScreenFormatting();
    }

    /**
     * Clears all the characters on the screen and resets their formatting to default. Does not influence the scrollback buffer.
     */
    public void clearScreenAndScreenFormatting() {
        clearScreenFormatting();
        clearScreen();
    }

    /**
     * Changes the height of the screen. If the new height is smalled than 1,
     * it sets the height to 1 line.
     * @param height The new height of the screen.
     */
    public void changeScreenHeight(int height) {
        ringBuffer.changeScreenHeight(Math.max(1, height));
    }

    /**
     * Changes the width of the screen. If the new width is smaller than 1,
     * it sets the width to 1 character.
     * @param width The new width of the screen.
     */
    public void changeScreenWidth(int width) {
        ringBuffer.changeScreenWidth(Math.max(1, width));
    }

    /**
     * Gets the character at the specified row and column.
     * @param row The row of the character to retrieve. This index takes the scrollback buffer into account.
     * @param column The column of the character to retrieve.
     * @return The character at the specified row and column.
     */
    public char getCharacter(int row, int column) {
        column = Math.clamp(column, 0, ringBuffer.getRowSize() - 1);
        row = Math.clamp(row, 0, ringBuffer.getRowCount() - 1);
        return ringBuffer.getCharacter(row, column);
    }

    /**
     * Gets the character at the current cursor position.
     * @return The character at the current cursor position.
     */
    public char getCharacter() {
        return getCharacter(cursorY + ringBuffer.getScrollbackRowCount(), cursorX);
    }

    /**
     * Gets the attributes of the character at the specified row and column.
     * @param row The row of the character to retrieve. This index takes the scrollback buffer into account.
     * @param column The column of the character to retrieve.
     * @return The attributes of the character at the specified row and column.
     */
    public TextAttributes getCellAttributes(int row, int column) {
        column = Math.clamp(column, 0, ringBuffer.getRowSize() - 1);
        row = Math.clamp(row, 0, ringBuffer.getRowCount() - 1);
        return ringBuffer.getCellAttributes(row, column);
    }

    /**
     * Gets the attributes of the character at the current cursor position.
     * @return The attributes of the character at the current cursor position.
     */
    public TextAttributes getCellAttributes() {
        return getCellAttributes(cursorY + ringBuffer.getScrollbackRowCount(), cursorX);
    }

    /**
     * Gets the line at the specified index as a string.
     * @param row The index of the line to retrieve. This index takes the scrollback buffer into account.
     * @return The line at the specified index as a string.
     */
    public String getLineAsString(int row) {
        row = Math.clamp(row, 0, ringBuffer.getRowCount() - 1);
        return ringBuffer.getLineAsString(row);
    }

    /**
     * Gets the line at the current cursor position as a string.
     * @return The line at the current cursor position as a string.
     */
    public String getLineAsString() {
        return getLineAsString(cursorY + ringBuffer.getScrollbackRowCount());
    }

    /**
     * Gets the content on the screen as a string.
     * @return A string containing the content on the screen.
     */
    public String getScreenAsString() {
        return ringBuffer.getScreenAsString();
    }
}
