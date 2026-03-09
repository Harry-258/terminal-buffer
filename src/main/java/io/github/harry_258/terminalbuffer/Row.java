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
    }

    public void clear() {
        row.clear();
    }

    public void insertCharacter(Character character, int index) {

    }

    public void removeCharacter(int index) {

    }

//    public ArrayList<Character> changeSize(int size) {
//        this.size = size;
//    }
}
