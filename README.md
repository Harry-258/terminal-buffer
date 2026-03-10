# Terminal Buffer

I first thought about using a matrix made out of doubly linked lists to have constant time insertion and deletion. However, I chose to use ArrayLists for each row to find cells in constant time, and because the width of the terminal will realistically always be limited to a small number. Therefore, insertions and removals should not be a bottleneck. Moreover, ArrayLists are stored in contiguous blocks of memory, which is more efficient for the CPU than traversing through scattered nodes from a linked list. 

To store each row, I use a ring buffer that replaces the oldest row when inserting rows while the buffer is full.