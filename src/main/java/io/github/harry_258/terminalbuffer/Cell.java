package io.github.harry_258.terminalbuffer;

public class Cell {
    private char character;
    private TextAttributes attributes;

    public Cell(char character) {
        this.character = character;
        this.attributes = TextAttributes.DEFAULT;
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
    public Cell setCharacter(char character) {
        this.character = character;
        return this;
    }

    /**
     * Formats the cell as a string containing the ANSI escape sequence for applying the styles in a terminal.
     * @return The formatted character string.
     */
    public String getFormattedCellString() {
        StringBuilder stringBuilder = new StringBuilder("\033[");

        if (attributes.isBold()) stringBuilder.append("1;");
        if (attributes.isDim()) stringBuilder.append("2;");
        if (attributes.isItalic()) stringBuilder.append("3;");
        if (attributes.isUnderlined()) stringBuilder.append("4;");
        if (attributes.isBlink()) stringBuilder.append("5;");
        if (attributes.isRapidBlink()) stringBuilder.append("6;");
        if (attributes.isReverse()) stringBuilder.append("7;");
        if (attributes.isHidden()) stringBuilder.append("8;");

        stringBuilder.append(attributes.foreground().getForegroundCode()).append(";")
                .append(attributes.background().getBackgroundCode()).append("m")
                .append(character)
                .append("\033[0m");

        return stringBuilder.toString();
    }

    /**
     * Clears all formatting from the cell.
     */
    public void clearFormatting() {
        this.attributes = TextAttributes.DEFAULT;
    }

    /**
     * Sets the attributes of the cell.
     * @param attributes The attribute values to be set.
     */
    public Cell setAttributes(TextAttributes attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * Gets the attributes of the cell.
     * @return The attribute values of the cell.
     */
    public TextAttributes getAttributes() {
        return attributes;
    }
}
