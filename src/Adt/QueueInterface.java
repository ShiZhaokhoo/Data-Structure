package Adt;

import java.util.Iterator;

/**
 *
 * @author Khoo Shi Zhao
 */

public interface QueueInterface<T> {
    void enqueue(T newEntry);//add a input into the end of the queue.
    boolean add(T newEntry,int newPosition);
    T dequeue();//return and remove the output from the front of the queue list.
    T getFront();//return the output from the front of the queue list
    T getEntry(int givenPosition);//return the output based on the given position
    boolean replace(T modifyEntry, int modifyPosition);
    boolean isEmpty();//check the array is empty or not.
    void clear();//make the array become empty
    int getNumberOfEntries();//get the number of entires
    int search(T givenEntry);
    boolean equals(QueueInterface givenEntry);
    boolean remove(int removePosition);
    Iterator<T> getQueueIterator();
    Iterator<T> getQueueIterator(int startIndex);
    boolean contains(T givenEntry);//check the entire queue list have the givenEntry or not.
}