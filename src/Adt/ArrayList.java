package Adt;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Ng Theng Yang
 */

public class ArrayList<T> implements ListInterface<T>, Serializable{
    private T[] array;
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 5;
    
    public ArrayList(){
        this(DEFAULT_CAPACITY);
    }
    
    public ArrayList(int initialCapacity){
        numberOfEntries = 0;
        array = (T[]) new Object[initialCapacity];
    }
    
    @Override
    public boolean add(T newEntry){
        boolean successful = true;
        if(newEntry != null){
            if(isArrayFull())
                doubleArray();
            array[numberOfEntries] = newEntry;
            numberOfEntries += 1;
        }
        return successful;
    }

    @Override
    public boolean add(T newEntry, int newPosition){
        boolean successful = false;
        if(newPosition > 0 && newEntry != null){
            if(isArrayFull())
                doubleArray();
            if(newPosition > numberOfEntries){
                array[numberOfEntries] = newEntry;
            }else{
                makeRoom(newPosition);
                array[newPosition - 1] = newEntry;
            }
            numberOfEntries += 1;
            successful = true;
        }
        return successful;
    }
    
    @Override
    public boolean remove(int removePosition){
        boolean successful = false;
        if(removePosition <= numberOfEntries && removePosition > 0){
            if(removePosition < numberOfEntries)
                removeGap(removePosition);
            else
                array[removePosition - 1] = null;
            successful = true;
            numberOfEntries--;
        }
        return successful;
    }

    @Override
    public boolean replace(T replaceEntry, int replacePosition) {
        boolean successful = false;
        if(replacePosition > 0 && replacePosition <= numberOfEntries){
            array[replacePosition - 1] = replaceEntry;
            successful = true;
        }
        return successful;
    }

    @Override
    public T getEntry(int givenPosition) {
        if(givenPosition <= numberOfEntries && givenPosition > 0){
            return array[givenPosition - 1];
        }
        return null;
    }
    
    @Override
    public int search(T givenEntry){
        int matchPosition;
        if(givenEntry != null && !isEmpty()){
            matchPosition = 0;
            for(T arrayData:array){
                matchPosition++;
                if(arrayData == givenEntry){
                    return matchPosition;
                }
            }
        }
        matchPosition = -1;
        return matchPosition;
    }

    @Override
    public boolean contains(T givenEntry) {
        boolean successful = false;
        if(!isEmpty()){
            for(int index = 0; index < numberOfEntries && !successful; index++){
                if(givenEntry == array[index])
                    successful = true;
            }
        }
        return successful;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public void clear() {
        int arrayLength = array.length;
        array = (T[]) new Object[arrayLength];
        numberOfEntries = 0;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }
    
    @Override
    public boolean equals(ListInterface givenEntry){
        return givenEntry instanceof ArrayList;
    }

    private boolean isArrayFull() {
        return numberOfEntries == array.length;
    }

    private void doubleArray() {
        if(numberOfEntries == array.length){
            T[] oldArray = array;
            int newArrayLength = array.length * 2;
            array = (T[]) new Object[newArrayLength];
            int index = 0;
            for(T value: oldArray){
                array[index] = value;
                index++;
            }
            
        }
    }

    private void makeRoom(int newPosition){
        for (int position = numberOfEntries; position >= newPosition; position--) {
            array[position] = array[position - 1];
        }
    }
    
    private void removeGap(int removePosition) {
        for (int position = removePosition; position < numberOfEntries; position++) {
            array[position - 1] = array[position];
        }
    }
    
    @Override
    public Iterator<T> getIterator() {
        return new ArrayListIterator();
    }
    
    @Override
    public Iterator<T> getIterator(int givenPosition) {
        return new ArrayListIterator(givenPosition);
    }
    
    private class ArrayListIterator implements Iterator<T> {
        private int nextPosition;
       
        private ArrayListIterator() {
            nextPosition = 1;
        }
        
        private ArrayListIterator(int givenPosition) {
            nextPosition = givenPosition;
        }

        @Override
        public boolean hasNext() {
            return nextPosition <= numberOfEntries;
        }

        @Override
        public T next() {
            T nextEntry = null;
            if (hasNext()) {
                nextEntry = array[nextPosition-1];
                nextPosition++;
            }
            return nextEntry;
        }
        
        @Override
        public void remove(){
            removeGap(nextPosition - 2);
            numberOfEntries--;
        }
    }
    
    @Override
    public String toString(){
        String outputStr = "";
        if(!isEmpty()){  
            for(int index = 0; index < numberOfEntries; index++){
                outputStr += array[index] + ", ";
            }
        }
        return outputStr.substring(0, outputStr.length() - 2);
    }
}
