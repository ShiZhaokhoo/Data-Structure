package Adt;

import java.util.Iterator;

/**
 *
 * @author Ng Theng Yang
 */

public interface ListInterface<T>{
    boolean add(T newEntry);
    boolean add(T newEntry, int newPosition);
    boolean remove(int removePosition);
    boolean replace(T replaceEntry, int replacePosition);
    int search(T givenEntry);
    T getEntry(int givenPosition);
    boolean contains(T givenEntry);
    int getNumberOfEntries();
    void clear();
    boolean isEmpty();
    boolean equals(ListInterface givenEntry);
    Iterator<T> getIterator();
    Iterator<T> getIterator(int givenPosition);
}
