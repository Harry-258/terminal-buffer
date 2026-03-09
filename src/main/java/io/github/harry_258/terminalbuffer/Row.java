package io.github.harry_258.terminalbuffer;

import java.util.ArrayList;

public class Row {
    private final ArrayList<Character> row;
    private int size;

    /**
     * Initializes a new Row with the given size as the number of characters in the row.
     * @param size The number of characters in the row.
     */
    public Row(int size) {
        this.row = new ArrayList<>();
        this.size = size;

        for (int i = 0; i < size; i++) {
            row.add(new Character(' '));
        }
    }

    /**
     * Resets all the characters from the row to spaces.
     */
    public void clear() {
        for (Character character : row) {
            character.setCharacter(' ');
        }
    }

    /**
     * Writes a character at the specified index. The index is clamped between 0 and the size of the row.
     * @param character The character to insert.
     * @param index The index at which to insert the character.
     */
    public void writeCharacter(Character character, int index) {
        row.set(Math.clamp(index, 0, row.size() - 1), character);
    }

    /**
     * Gets the character at the specified index.
     * @param index The index of the character to retrieve.
     * @return The character at the specified index. If the row is empty, it returns a space character.
     */
    public Character getCharacter(int index) {
        return row.get(Math.clamp(index, 0, row.size() - 1));
    }

    /**
     * Removes the character at the specified index.
     * @param index The index of the character to remove.
     */
    public void removeCharacter(int index) {
        row.remove(index);
        row.add(size - 1, new Character(' '));
    }

//    public ArrayList<Character> changeSize(int size) {
//        this.size = size;
//    }

    /**
     * Replaces all the characters in the row with the specified character.
     * @param character The character to use for replacement.
     */
    public void fillWithCharacter(char character) {
        for (Character c : row) {
            c.setCharacter(character);
        }
    }
}
