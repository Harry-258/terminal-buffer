# Terminal Buffer

I first thought about using a matrix made out of doubly linked lists to have constant time insertion and deletion. However, I chose to use ArrayLists for each row to find cells in constant time, and because the width of the terminal will realistically always be limited to a small number. Therefore, insertions and removals should not be a bottleneck. Moreover, ArrayLists are stored in contiguous blocks of memory, which is more efficient for the CPU than traversing through scattered nodes from a linked list. 

To store each row, I use a ring buffer that replaces the oldest row when inserting rows while the buffer is full.

Instead of having each cell store it's own attributes, I decided to create a record that holds them and that each points to. This way, cells will only use memory for one instance of the attributes record. If they need to be formatted differently, a new instance of the record can be created. Also, since the default uses codes 39 and 49 for the foreground and background colors respectively, the terminal GUI can change its colors and the formatting will recognize the changes.

Changes the height of the screen by adding or removing rows that are in the scrollback buffer. Since the size of the scrollback buffer stays the same, it acts as a sliding window that increases or decreases the total amount of lines on the screen.

When writing, if the cursor is at the end of the line, it will jump to the next line and overwrite the first character. If this line was the bottom-most line on the screen, it inserts a new line at the bottom of the screen.