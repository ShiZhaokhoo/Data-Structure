package Adt;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Yeap Ying Thung
 */

public class ArrayStack<T> implements StackInterface<T>, Serializable{
    private T[] array;
    private int topIndex;
    private static final int DEFAULT_CAPACITY = 5;

    public ArrayStack(){
        this(DEFAULT_CAPACITY);
    }
    
    public ArrayStack(int initialCapacity){
        array = (T[]) new Object[initialCapacity];
        topIndex = -1;
    }

    @Override
    public boolean push(T newEntry) {
        if(newEntry == null)
            return false;
        if(isArrayFull())
            doubleArray();
        topIndex++;
        array[topIndex] = newEntry;
        return true;
    }

    @Override
    public boolean add(T newEntry, int entryPosition){
        if(newEntry == null || entryPosition < 1)
            return false;
        if(isArrayFull())
            doubleArray();
        if(entryPosition > topIndex + 1)//If detect the entryPosition out of range, then will change the entryPosition to the last position
            entryPosition = topIndex + 1;
        if(entryPosition > 1)
            makeRoom(entryPosition);
        int entryIndex = (topIndex + 1) - entryPosition; //topPosition - entryPosition
        topIndex++;
        array[entryIndex] = newEntry;
        return true;
    }

    @Override
    public T pop() {
        T topValue = null;
        if(!isEmpty()){
            topValue = array[topIndex];
            array[topIndex] = null;
            topIndex--;
        }
        return topValue;
    }

    @Override
    public T peek() {
        if(!isEmpty()){
            return array[topIndex];
        }
        return null;
    }

    @Override
    public void clear() {
        array = (T[])new Object[DEFAULT_CAPACITY];
        topIndex = -1;
    }

    @Override
    public int search(T givenEntry) {
        int resultPosition;
        if(givenEntry != null && !isEmpty()){
            resultPosition = topIndex + 1;
            for(T arrayData: array){
                if(givenEntry == arrayData)
                    return resultPosition;
                resultPosition--;
            }
        }
        resultPosition=-1;
        return resultPosition;
    }
    
    @Override
    public boolean replace(T replaceEntry, int replacePosition){
        boolean successful = false;
        if(replacePosition > 0 && replacePosition <= topIndex + 1){
            successful = true;
            int replaceIndex = (topIndex + 1) - replacePosition;
            array[replaceIndex] = replaceEntry;
        }
        return successful;
    }

    @Override
    public boolean isEmpty() {
        return topIndex < 0;
    }
    
    private boolean isArrayFull(){
        return topIndex + 1 == array.length;
    }
    
    private void doubleArray(){
        if(isArrayFull()){
            T[] oldStack = array;
            int newArrayLength = array.length * 2;
            array = (T[]) new Object[newArrayLength];
            for(int index = 0; index <= topIndex; index++)
                array[index] = oldStack[index];
        }
    }

    @Override
    public boolean equals(StackInterface givenEntry) {
        return givenEntry instanceof ArrayStack;
    }
    
    private void makeRoom(int givenPosition){
        int entryIndex = (topIndex + 1) - givenPosition;
        for(int newIndex = topIndex; newIndex >= entryIndex; newIndex--)
            array[newIndex + 1] = array[newIndex];
    }
    
    private void removeGap(int givenPosition){
        int removeIndex = givenPosition - 1;
        for( ; removeIndex < array.length ; removeIndex++){
            if(removeIndex + 1 != array.length)
                array[removeIndex] = array[removeIndex + 1];
            else
                array[removeIndex] = null;
        }
    }

    @Override
    public T getEntry(int givenPosition) {
        T resultData = null;
        if(topIndex + 1 >= givenPosition){
            int resultIndex = (topIndex + 1) - givenPosition;
            resultData = array[resultIndex];
        }
        return resultData;
    }
    
    @Override
    public boolean remove(int removePosition) {
        boolean successful = false;
        if(topIndex + 1 >= removePosition){
            removePosition = topIndex + 2 - removePosition;
            removeGap(removePosition);
            topIndex-=1;
            successful = true;
        }
        return successful;
    }

    @Override
    public boolean contains(T givenEntry) {
        boolean match = false;
        if(givenEntry != null){
            Iterator<T> checkContains = getStackIterator();
            while(checkContains.hasNext() && !match){
                T arrayB = checkContains.next();
                match = givenEntry == arrayB;
            }
        }
        return match;
    }
    
    @Override
    public Iterator<T> getStackIterator() {
        return new ArrayStackIterator();
    }
    
    @Override
    public Iterator<T> getStackIterator(int startPosition) {
        return new ArrayStackIterator(startPosition);
    }

    @Override
    public int getNumberOfEntries() {
        return topIndex + 1;
    }
    
    private class ArrayStackIterator implements Iterator<T>{
        private int previousPosition;
        public ArrayStackIterator(){
            previousPosition = topIndex + 1;
        }
        
        public ArrayStackIterator(int startPosition){
            previousPosition = topIndex + 2 - startPosition;
        }
        
        @Override
        public boolean hasNext(){
            return previousPosition > 0;
        }
        
        @Override
        public T next(){
            if(hasNext()){
                previousPosition-=1;
                return array[previousPosition];
            }
            return null;
        }
        
        @Override
        public void remove(){
            removeGap(previousPosition + 1);
            topIndex-=1;
        }
    }
    
    @Override
    public String toString(){
        String outputStr = "";
        
        for(int index = topIndex; index >= 0; index--){
            outputStr += array[index] + ", ";
        }
        return outputStr.substring(0, outputStr.length() - 2);
    }
}