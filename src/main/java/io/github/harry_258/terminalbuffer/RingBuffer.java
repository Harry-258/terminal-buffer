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
     * @param newRowCount The new height of the screen.
     */
    public void changeScreenHeight(int newRowCount) {
        List<Row> newBuffer = new ArrayList<>();
        int padding = newRowCount - rowCount;
        int skippedRows = rowCount - newRowCount;

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
        rowCount = newRowCount;
        buffer = newBuffer;
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
    public void clear() {
        for (Row row : buffer) {
            row.clear();
        }
    }
}
