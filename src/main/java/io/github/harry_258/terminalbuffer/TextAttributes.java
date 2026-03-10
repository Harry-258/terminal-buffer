package io.github.harry_258.terminalbuffer;

/**
 * An immutable record representing the visual style of a cell.
 */
public record TextAttributes(
        int foregroundCode,
        int backgroundCode,
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
        BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE;

        /**
         * Gets the ANSI escape code for using the color as foreground.
         */
        public int getForegroundCode() {
            return 30 + ordinal();
        }

        /**
         * Gets the ANSI escape code for using the color as background.
         */
        public int getBackgroundCode() {
            return 40 + ordinal();
        }
    }

    /**
     * Static reference to the default styles that every cell will point to initially.
     */
    public static final TextAttributes DEFAULT = new TextAttributes(
            39, 49, false, false, false, false, false, false, false, false
    );
}