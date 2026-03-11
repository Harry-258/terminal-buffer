package io.github.harry_258.terminalbuffer;

import java.util.ArrayList;
import java.util.List;

public class RingBuffer {
    private List<Row> buffer;
    private int rowCount;
    private int rowSize;
    private int scrollbackRowCount;
    private int index;

    /**
     * Initializes a new RingBuffer with the given size as the number of rows.
     * @param screenRowCount The number of rows in the screen buffer.
     * @param rowSize The number of characters in each row.
     * @param scrollbackRowCount The number of rows to keep in the scrollback buffer.
     */
    public RingBuffer(int screenRowCount, int rowSize, int scrollbackRowCount) {
        this.rowCount = screenRowCount + scrollbackRowCount;
        this.rowSize = rowSize;
        this.scrollbackRowCount = scrollbackRowCount;
        this.buffer = new ArrayList<>(screenRowCount);
        this.index = 0;

        for (int i = 0; i < this.rowCount; i++) {
            this.buffer.add(new Row(rowSize));
        }
    }

    /**
     * Writes a character to the specified row and column.
     * @param character The character to write.
     * @param row The row to write the character to.
     * @param column The column to write the character to.
     */
    public void write(char character, int row, int column) {
        getRow(row).writeCharacter(character, column);
    }

    /**
     * Writes a character at the specified index of the bottom-most row on the screen.
     * @param character The character to write.
     * @param index The index at which the character should be written.
     */
    public void write(char character, int index) {
        getRow(rowCount - 1).writeCharacter(character, index);
    }

    /**
     * Fills the specified row with the specified character.
     * @param row The row to fill.
     * @param character The character to fill the row with.
     */
    public void fillRow(int row, char character) {
        getRow(row).fillWithCharacter(character);
    }

    /**
     * Inserts an empty line by clearing the oldest line in the scrollback buffer and moving the index forward.
     */
    public void insertLineAtBottom() {
        Row oldestRow = getRow(0);

        oldestRow.clear();
        oldestRow.clearFormatting();

        index = (index + 1) % rowCount;
    }

    /**
     * Gets the size of each row.
     * @return The size of each row.
     */
    public int getRowSize() {
        return rowSize;
    }

    /**
     * Gets the number of rows in the buffer.
     * @return The number of rows in the buffer.
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Gets the number of rows in the scrollback buffer.
     * @return The number of rows in the scrollback buffer.
     */
    public int getScrollbackRowCount() {
        return scrollbackRowCount;
    }

    /**
     * Gets the row at the specified index.
     *
     * @param index The index of the row to retrieve.
     * @return The row at the specified index.
     */
    public Row getRow(int index) {
        return buffer.get((this.index + index) % rowCount);
    }

    /**
     * Changes the height of the screen. If the new size is larger, it adds empty rows
     * at the top of the scrollback buffer. Otherwise, it removes the extra rows from the scrollback buffer.
     * @param newScreenHeight The new height of the screen.
     */
    public void changeScreenHeight(int newScreenHeight) {
        if (newScreenHeight <= 0) {
            return;
        }

        int newTotalRowCount = newScreenHeight + scrollbackRowCount;
        if (newTotalRowCount == rowCount) {
            return;
        }

        List<Row> newBuffer = new ArrayList<>();

        int padding = newTotalRowCount - rowCount;
        int skippedRows = rowCount - newTotalRowCount;

        while (padding > 0) {
            newBuffer.add(new Row(rowSize));
            padding--;
        }

        int logicalIndex = Math.max(0, skippedRows);
        while (logicalIndex < rowCount) {
            newBuffer.add(getRow(logicalIndex));
            logicalIndex++;
        }

        index = 0;
        rowCount = newTotalRowCount;
        buffer = newBuffer;
    }

    /**
     * Changes the width of the screen. If the new width is smaller, it wraps the
     * overflowing characters on each line to the next line. Otherwise, it appends
     * empty cells to the end of each row.
     * @param newWidth The new width of the screen.
     */
    public void changeScreenWidth(int newWidth) {
        rowSize = newWidth;
        List<Cell> reminderCells = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            reminderCells = getRow(i).changeSize(newWidth, reminderCells);
        }

        int count = 0;
        for (Cell cell : reminderCells) {
            if (count % newWidth == 0) {
                insertLineAtBottom();
            }
            getRow(rowCount - 1).setCell(cell, count % newWidth);
            count++;
        }
    }

    /**
     * Clears all formatting from each row on the screen and scrollback.
     */
    public void clearFormatting() {
        for (Row row : buffer) {
            row.clearFormatting();
        }
    }

    /**
     * Sets all characters on the screen and in the scrollback to spaces.
     */
    public void clearTerminal() {
        for (Row row : buffer) {
            row.clear();
        }
    }

    /**
     * Clears all the characters on the screen. Leaves the scrollback buffer intact.
     */
    public void clearScreen() {
        for (int i = scrollbackRowCount; i < rowCount; i++) {
            getRow(i).clear();
        }
    }

    /**
     * Clears the formatting of all the characters on the screen.
     */
    public void clearScreenFormatting() {
        for (int i = scrollbackRowCount; i < rowCount; i++) {
            getRow(i).clearFormatting();
        }
    }

    /**
     * Formats a cell on the screen at the specified row and column.
     * @param row The row of the cell to format.
     * @param column The column of the cell to format.
     * @param attributes The attributes to apply to the cell.
     */
    public void formatCell(int row, int column, TextAttributes attributes) {
        getRow(row).formatCell(column, attributes);
    }

    /**
     * Formats all characters on a row with the specified attributes.
     * @param row The row to format.
     * @param attributes The attributes to apply to the row.
     */
    public void formatRow(int row, TextAttributes attributes) {
        getRow(row).formatCells(attributes);
    }

    /**
     * Changes the style attributes of all characters on the screen and scrollback.
     * @param attributes The new attributes.
     */
    public void formatTerminal(TextAttributes attributes) {
        for (int i = 0; i < rowCount; i++) {
            formatRow(i, attributes);
        }
    }
}
