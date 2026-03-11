package io.github.harry_258.terminalbuffer;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private final List<Cell> row;
    private int size;

    /**
     * Initializes a new Row with the given size as the number of characters in the row.
     * @param size The number of characters in the row.
     */
    public Row(int size) {
        this.row = new ArrayList<>();
        this.size = size;

        for (int i = 0; i < size; i++) {
            row.add(new Cell(' '));
        }
    }

    /**
     * Resets all the characters from the row to spaces.
     */
    public void clear() {
        for (Cell cell : row) {
            cell.setCharacter(' ');
        }
    }

    /**
     * Overwrites the character at the specified index. The index is clamped between 0 and the size of the row.
     * @param character The character to insert.
     * @param index The index at which to insert the character.
     */
    public void writeCharacter(char character, int index) {
        row.get(Math.clamp(index, 0, row.size() - 1)).setCharacter(character);
    }

    /**
     * Sets the cell at the specified index. The index is clamped between 0 and the size of the row.
     * @param cell The cell to set.
     * @param index The index at which the cell should be set.
     */
    public void setCell(Cell cell, int index) {
        row.set(Math.clamp(index, 0, row.size() - 1), cell);
    }

    /**
     * Gets the cell at the specified index. The index is clamped between 0 and the size of the row.
     * @param index The index of the character to retrieve.
     * @return The cell at the specified index.
     */
    public Cell getCell(int index) {
        return row.get(Math.clamp(index, 0, row.size() - 1));
    }

    /**
     * Removes the character at the specified index.
     * @param index The index of the character to remove.
     */
    public void removeCharacter(int index) {
        Cell removedCell = row.remove(Math.clamp(index, 0, row.size() - 1));
        removedCell.setCharacter(' ').clearFormatting();
        row.addLast(removedCell);
    }

    /**
     * Changes the size of the row. If the new size is larger, it adds empty cells.
     * Otherwise, it returns the extra cells to be wrapped to the next row.
     * @param newSize The new size of the row.
     * @param reminderCells The reminder cells wrapped from the previous row.
     */
    public List<Cell> changeSize(int newSize, List<Cell> reminderCells) {
        List<Cell> allAvailableCells = new ArrayList<>(reminderCells);
        allAvailableCells.addAll(this.row);

        // Remove any spaces with default formatting from the end of the reminder cells.
        int index = allAvailableCells.size() - 1;
        while (index >= 0
                && allAvailableCells.get(index).getChar() == ' '
                && allAvailableCells.get(index).getAttributes().equals(TextAttributes.DEFAULT)
        ) {
            allAvailableCells.remove(index);
            index--;
        }

        row.clear();
        size = newSize;

        for (int i = 0; i < newSize; i++) {
            if (i < allAvailableCells.size()) {
                row.add(allAvailableCells.get(i));
            } else {
                row.add(new Cell(' '));
            }
        }

        List<Cell> extraCells = new ArrayList<>();
        for (int i = newSize; i < allAvailableCells.size(); i++) {
            extraCells.add(allAvailableCells.get(i));
        }
        return extraCells;
    }

    /**
     * Replaces all the characters in the row with the specified character.
     * @param character The character to use for replacement.
     */
    public void fillWithCharacter(char character) {
        for (Cell c : row) {
            c.setCharacter(character);
        }
    }

    /**
     * Formats all cells in the row.
     * @param attributes The attributes to apply to the cells.
     */
    public void formatCells(TextAttributes attributes) {
        for (Cell c : row) {
            c.setAttributes(attributes);
        }
    }

    /**
     * Formats a single cell in the row. Clamps the index between 0 and the size of the row.
     * @param index The index of the cell to format.
     * @param attributes The attributes to apply to the cell.
     */
    public void formatCell(int index, TextAttributes attributes) {
        row.get(Math.clamp(index, 0, row.size() - 1)).setAttributes(attributes);
    }

    /**
     * Clears the formatting of all the cells in the row.
     */
    public void clearFormatting() {
        for (Cell c : row) {
            c.clearFormatting();
        }
    }

    /**
     * Gets the size of the row.
     * @return The size of the row.
     */
    public int getSize() {
        return size;
    }
}
