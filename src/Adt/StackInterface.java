package Adt;

import java.util.Iterator;

/**
 *
 * @author Yeap Ying Thung
 */
public interface StackInterface<T> {
    boolean push(T newEntry);
    boolean add(T newEntry, int newPosition);
    T pop();
    T peek(); 
    T getEntry(int givenPosition);
    boolean replace(T modifyEntry, int modifyPosition);
    boolean remove(int removePosition);
    boolean contains(T givenEntry);
    boolean isEmpty();
    boolean equals(StackInterface givenEntry); 
    void clear();
    int search(T givenEntry);
    int getNumberOfEntries();
    Iterator<T> getStackIterator();
    Iterator<T> getStackIterator(int givenPosition);
}
