package io.github.harry_258.terminalbuffer;

public class Character {
    private char character;
    private int foregroundColor;
    private int backgroundColor;
    private boolean isBold = false;
    private boolean isDim = false;
    private boolean isItalic = false;
    private boolean isUnderlined = false;
    private boolean isBlink = false;
    private boolean isRapidBlink = false;
    private boolean isReverse = false;
    private boolean isHidden = false;

    public enum Color {
        BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE;

        /**
         * Gets the ANSI escape code for using the color as foreground.
         * @return The ANSI escape code of the color.
         */
        public int getForegroundCode() {
            return 30 + ordinal();
        }

        /**
         * Gets the ANSI escape code for using the color as background..
         * @return The ANSI escape code of the color.
         */
        public int getBackgroundCode() {
            return 40 + ordinal();
        }
    }

    public Character(char character) {
        this.character = character;
        // Default colors
        this.foregroundColor = 39;
        this.backgroundColor = 49;
    }

    /**
     * Gets the character inside the cell.
     * @return The char value inside of the cell.
     */
    public char getChar() {
        return character;
    }

    /**
     * Sets the character inside the cell.
     * @param character The char value to be set.
     */
    public Character setCharacter(char character) {
        this.character = character;
        return this;
    }

    /**
     * Sets the foreground color of the cell.
     * @param color The color to set.
     * @param isBright Whether the cell foreground should have the bright version of the color.
     */
    public Character setForegroundColor(Color color, boolean isBright) {
        foregroundColor = color.getForegroundCode() + (isBright ? 60 : 0);
        return this;
    }

    /**
     * Sets the background color of the cell.
     * @param color The color to set.
     * @param isBright Whether the cell background should have the bright version of the color.
     */
    public Character setBackgroundColor(Color color, boolean isBright) {
        backgroundColor = color.getBackgroundCode() + (isBright ? 60 : 0);
        return this;
    }

    /**
     * Sets the bold style of the cell.
     * @param isBold Whether the cell should be bold.
     * @return The cell object.
     */
    public Character setBold(boolean isBold) {
        this.isBold = isBold;
        return this;
    }

    /**
     * Sets the dim style of the cell.
     * @param isDim Whether the cell should be dim.
     * @return The cell object.
     */
    public Character setDim(boolean isDim) {
        this.isDim = isDim;
        return this;
    }

    /**
     * Sets the italic style of the cell.
     * @param isItalic Whether the cell should be italic.
     * @return The cell object.
     */
    public Character setItalic(boolean isItalic) {
        this.isItalic = isItalic;
        return this;
    }

    /**
     * Sets the underline style of the cell.
     * @param isUnderline Whether the cell should be underlined.
     * @return The cell object.
     */
    public Character setUnderlined(boolean isUnderline) {
        this.isUnderlined = isUnderline;
        return this;
    }

    /**
     * Sets the blink style of the cell.
     * @param isBlink Whether the cell should blink.
     * @return The cell object.
     */
    public Character setBlink(boolean isBlink) {
        this.isBlink = isBlink;
        return this;
    }

    /**
     * Sets the rapid blink style of the cell.
     * @param isRapidBlink Whether the cell should rapidly blink.
     * @return The cell object.
     */
    public Character setRapidBlink(boolean isRapidBlink) {
        this.isRapidBlink = isRapidBlink;
        return this;
    }

    /**
     * Sets the reverse style of the cell.
     * @param isReverse Whether the cell should use reverse video.
     * @return The cell object.
     */
    public Character setReverse(boolean isReverse) {
        this.isReverse = isReverse;
        return this;
    }

    /**
     * Sets the hidden style of the cell.
     * @param isHidden Whether the cell should be hidden.
     * @return The cell object.
     */
    public Character setHidden(boolean isHidden) {
        this.isHidden = isHidden;
        return this;
    }

    /**
     * Gets whether the cell is bold.
     * @return Whether the cell is bold.
     */
    public boolean isBold() {
        return isBold;
    }

    /**
     * Gets whether the cell is dim.
     * @return Whether the cell is dim.
     */
    public boolean isDim() {
        return isDim;
    }

    /**
     * Gets whether the cell is italic.
     * @return Whether the cell is italic.
     */
    public boolean isItalic() {
        return isItalic;
    }

    /**
     * Gets whether the cell is underlined.
     * @return Whether the cell is underlined.
     */
    public boolean isUnderlined() {
        return isUnderlined;
    }

    /**
     * Gets whether the cell is blinking.
     * @return Whether the cell is blinking.
     */
    public boolean isBlink() {
        return isBlink;
    }

    /**
     * Gets whether the cell is rapidly blinking.
     * @return Whether the cell is rapidly blinking.
     */
    public boolean isRapidBlink() {
        return isRapidBlink;
    }

    /**
     * Gets whether the cell uses reverse video.
     * @return Whether the cell uses reverse video.
     */
    public boolean isReverse() {
        return isReverse;
    }

    /**
     * Gets whether the cell is hidden.
     * @return Whether the cell is hidden.
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Getter for the color code of the cell's foreground color.
     * @return The color code of the cell's foreground color.
     */
    public int getForegroundColor() {
        return foregroundColor;
    }

    /**
     * Getter for the color code of the cell's background color.
     * @return The color code of the cell's background color.
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Formats the cell as a string containing the ANSI escape sequence for applying the styles in a terminal.
     * @return The formatted character string.
     */
    public String getFormattedCellString() {
        StringBuilder stringBuilder = new StringBuilder("\033[");

        if (isBold) stringBuilder.append("1;");
        if (isDim) stringBuilder.append("2;");
        if (isItalic) stringBuilder.append("3;");
        if (isUnderlined) stringBuilder.append("4;");
        if (isBlink) stringBuilder.append("5;");
        if (isRapidBlink) stringBuilder.append("6;");
        if (isReverse) stringBuilder.append("7;");
        if (isHidden) stringBuilder.append("8;");

        stringBuilder.append(foregroundColor).append(";")
                .append(backgroundColor).append("m")
                .append(character)
                .append("\033[0m");

        return stringBuilder.toString();
    }
}
