package io.github.harry_258.terminalbuffer;

import java.util.ArrayList;

public class RingBuffer {
    private ArrayList<Row> buffer;
    private int rowCount;
    private int rowSize;
    private int scrollbackRowCount;
    private int index;
    private boolean isFull;

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
        this.isFull = false;

        for (int i = 0; i < this.rowCount; i++) {
            this.buffer.add(new Row(rowSize));
        }
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
        int startIndex = isFull ? this.index : 0;
        return buffer.get((startIndex + index) % rowCount);
    }

    /**
     * Changes the size of the buffer. If the new size is larger, it adds empty rows. Otherwise, it removes the extra rows.
     * @param newCount The new size of the buffer.
     */
    public void changeRowCount(int newCount) {
        if (newCount == this.rowCount) return;

        ArrayList<Row> newBuffer = new ArrayList<>(newCount);

        int validRows = isFull ? this.rowCount : this.index;
        int rowsToKeep = Math.min(validRows, newCount);
        int rowsToSkip = validRows - rowsToKeep;

        for (int i = 0; i < rowsToKeep; i++) {
            newBuffer.add(getRow(i + rowsToSkip));
        }

        for (int i = rowsToKeep; i < newCount; i++) {
            newBuffer.add(new Row(rowSize));
        }

        this.buffer = newBuffer;
        this.rowCount = newCount;
        this.index = rowsToKeep % newCount;
        this.isFull = (rowsToKeep == newCount);
    }
}
