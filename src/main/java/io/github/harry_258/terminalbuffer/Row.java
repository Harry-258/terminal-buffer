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
        Cell removedCell = row.remove(index);
        removedCell.setCharacter(' ').clearFormatting();
        row.addLast(removedCell);
    }

    /**
     * Changes the size of the row. If the new size is larger, it adds empty cells. Otherwise, it removes the extra cells.
     * @param size The new size of the row.
     */
    public void changeSize(int size) {
        while (this.size < size) {
            row.add(new Cell(' '));
            this.size++;
        }
        while (this.size > size) {
            row.removeLast();
            this.size--;
        }
        this.size = size;
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
     * Clears the formatting of all the cells in the row.
     */
    public void clearFormatting() {
        for (Cell c : row) {
            c.clearFormatting();
        }
    }
}
