# Terminal Buffer

This is a terminal text buffer used to store and manipulate text displayed on a terminal emulator. It defines the data structures used to store each character and its formatting, including methods to set up the terminal, edit text, access terminal content, and move the cursor on the screen.

The terminal is split into a screen, the editable part users interact with, and a scrollback, an unmodifiable history of lines that scroll off the top of the screen.

## Features
* **Grid Management:** Configurable width, height, and scrollback history.
* **Cursor Control:** Bounds-checked movement (Up, Down, Left, Right) and coordinate placement.
* **Text Editing:** Inline text insertion with wrapping, character replacement, and line clearing.
* **Styling:** 16-color foreground/background support and text styling (bold, dim, italic, underline, blink, hidden, etc.).

## Data Structures Used and My Thought Process Behind Them

I first considered storing the cells in a matrix of doubly linked lists to achieve constant-time insertion and deletion. I chose ArrayLists instead because you can still find cells in constant time, and the terminal's width will realistically always be limited to a small number, so inserting and removing elements will not take long. Moreover, ArrayLists are stored in contiguous blocks of memory, which is more efficient for the CPU to traverse than through scattered nodes from a linked list.

I use a ring buffer to store the rows. It has an index that points to the oldest element in the buffer. Whenever a new line is inserted on the screen, the oldest row in the scrollback gets cleared and used as the new line, making line insertions constant-time operations. When the screen height is changed, the scrollback buffer acts as a sliding window, taking in the newly inserted rows or the lines that get pushed out of the screen.

I created a record called TextAttributes to store the style attributes for all cells. Initially, all cells point to the same instance of this record, with a new one being created only if a cell has a style applied to it. This reduces memory usage for style attributes compared to storing booleans in each cell for every possible attribute. Moreover, the background and foreground colors are used as escape sequences, which should be easily recognized by terminal GUIs.

## Getting Started

### Clone the repository

```bash
git clone git@github.com:Harry-258/terminal-buffer.git
cd terminal-buffer
```

### Run the tests

The project contains unit tests, including property-based tests for edge cases, and integration tests. To run the tests, run the following command:

```bash
mvn clean verify
```

## Future Improvements

This project was made as part of a challenge. With more time, these are the improvements I would make:
- Add support for characters that occupy two cells in the terminal (e.g., CJK ideographs, emoji)
- Handle newline characters in operations that involve editing the text on the screen.
- Some methods use row indexes relative to the top of the screen, while others use absolute indexes. I would refactor these methods to use a consistent index type.
- Distinguish between soft and hard line wraps. I would do this by adding a boolean flag in each row that indicates whether the line is soft-wrapped or not.
- Write more integration tests.