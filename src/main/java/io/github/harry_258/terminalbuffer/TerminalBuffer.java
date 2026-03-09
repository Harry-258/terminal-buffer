package io.github.harry_258.terminalbuffer;

public class TerminalBuffer {
    private final RingBuffer ringBuffer;
    private int cursorX;
    private int cursorY;

    public TerminalBuffer(int width, int height, int scrollbackSize) {
        this.ringBuffer = new RingBuffer(height, width, scrollbackSize);
    }


}
