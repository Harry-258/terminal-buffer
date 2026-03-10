package io.github.harry_258.terminalbuffer;

/**
 * An immutable record representing the visual style of a cell.
 */
public record TextAttributes(
        Color foreground,
        Color background,
        boolean isBold,
        boolean isDim,
        boolean isItalic,
        boolean isUnderlined,
        boolean isBlink,
        boolean isRapidBlink,
        boolean isReverse,
        boolean isHidden
) {
    public enum Color {
        BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE, DEFAULT;

        /**
         * Gets the ANSI escape code for using the color as foreground.
         */
        public int getForegroundCode() {
            if (this == DEFAULT) return 39;
            return 30 + ordinal();
        }

        /**
         * Gets the ANSI escape code for using the color as background.
         */
        public int getBackgroundCode() {
            if (this == DEFAULT) return 49;
            return 40 + ordinal();
        }
    }

    /**
     * Static reference to the default styles that every cell will point to initially.
     */
    public static final TextAttributes DEFAULT = new TextAttributes(
            Color.DEFAULT, Color.DEFAULT, false, false, false, false, false, false, false, false
    );
}