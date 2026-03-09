package io.github.harry_258.terminalbuffer;

import java.util.ArrayList;

public class RingBuffer {
    private final ArrayList<Row> buffer;
    private int rowCount;
    private int rowSize;
    private int scrollbackSize;
    private int index;
    private boolean isFull;

    /**
     * Initializes a new RingBuffer with the given size as the number of rows.
     * @param size The number of rows in the buffer.
     */
    public RingBuffer(int rowCount, int rowSize, int scrollbackSize) {
        this.rowCount = rowCount;
        this.rowSize = rowSize;
        this.scrollbackSize = scrollbackSize;
        this.buffer = new ArrayList<>();
        this.index = 0;
        this.isFull = false;

        for (int i = 0; i < rowCount; i++) {
            this.buffer.add(new Row(rowSize));
        }
    }

    /**
     * Inserts a new empty row in the ring buffer. If the buffer is full, it overwrites the oldest row.
     */
    public void insertRow() {
        buffer.get(index).clear();
        index = (index + 1) % rowCount;
    }

    /**
     * Gets the row at the specified index.
     * @param index The index of the row to retrieve.
     * @return The row at the specified index.
     */
    public Row getRow(int index) {
        return buffer.get((this.index + index) % rowCount);
    }

    /**
     * Changes the size of the buffer. If the new size is larger, it adds empty rows. Otherwise, it removes the extra rows.
     * @param newSize The new size of the buffer.
     */
    public void changeSize(int newSize) {
        if (newSize == rowCount) {
            return;
        }

        // TODO
    }

    // TODO
//    public void changeRowSize(int rowCount) {
//        for (int i = 0; i < size; i++) {
//            buffer.get(i).
//        }
//    }
}
