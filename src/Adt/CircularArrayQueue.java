package Adt;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Khoo Shi Zhao
 */

public class CircularArrayQueue<T> implements QueueInterface<T>, Serializable{
    private T[] array;
    private int frontIndex;
    private int backIndex;
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 5;

    public CircularArrayQueue(){
        this(DEFAULT_CAPACITY);
    }
    
    public CircularArrayQueue(int initialCapacity){
        array = (T[]) new Object[initialCapacity];
        frontIndex = 0;
        numberOfEntries = 0;
        backIndex = initialCapacity - 1;
    }
    
    @Override
    public void enqueue(T newEntry) {
        if(newEntry != null){
            if(isFull())
                doubleArray();
            backIndex = (backIndex + 1) % array.length;
            array[backIndex] = newEntry;
            numberOfEntries++;
        }
    }
    
    @Override
    public boolean add(T newEntry, int newPosition){
        boolean successful = false;
        if(newPosition > 0 && newEntry != null){
            successful = true;
            if(isFull())
                doubleArray();
            if(newPosition > numberOfEntries){
                array[numberOfEntries] = newEntry;
            }else{
                if(newPosition != numberOfEntries){
                    makeRoom(newPosition);
                }
                array[newPosition - 1] = newEntry;
            }
            numberOfEntries++;
            backIndex = (backIndex + 1) % array.length;
        }
        return successful;
    }

    @Override
    public T dequeue() {
        T returnValue = null;
        if(!isEmpty()){
            returnValue = array[frontIndex];
            frontIndex = (frontIndex + 1) % array.length;
            numberOfEntries--;
        }
        return returnValue;
    }

    @Override
    public T getFront() {
        if(!isEmpty())
            return array[frontIndex];
        return null;
    }
    
    @Override
    public T getEntry(int givenPosition){
        T resultValue = null;
        if(givenPosition > 0 ||givenPosition <= numberOfEntries){
            rearrangeQueue();
            resultValue = array[givenPosition - 1];
        }
        return resultValue;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public void clear() {
        array = (T[]) new Object[DEFAULT_CAPACITY];
        frontIndex = 0;
        numberOfEntries = 0;
        backIndex = DEFAULT_CAPACITY - 1;        
    }
    
    @Override
    public boolean replace(T modifyEntry, int modifyPosition){
        boolean successful = false;
        if(modifyPosition > 0 && modifyPosition <= numberOfEntries && modifyEntry != null){
            successful = true;
            rearrangeQueue();
            array[modifyPosition - 1] = modifyEntry;
        }
        return successful;
    }
    
    @Override
    public boolean remove(int removePosition){
        boolean successful = false;
        if(!isEmpty()&&(removePosition > 0 && removePosition <= numberOfEntries)){
            successful = true;
            removeGap(removePosition);
            numberOfEntries--;
            backIndex = (backIndex == 0 ? array.length - 1 : backIndex - 1 );
        }
        return successful;
    }
    
    private void removeGap(int removePosition){
        int sourceIndex = (frontIndex + removePosition)%array.length;
        int destIndex = (frontIndex + removePosition - 1)%array.length;
        for ( ;removePosition < numberOfEntries; removePosition++,
                destIndex = (destIndex + 1)%array.length,sourceIndex = (sourceIndex + 1)%array.length)
            array[destIndex] = array[sourceIndex];
    }
    
    private void makeRoom(int newPosition){
        int sourceIndex = backIndex;
        int destIndex = (backIndex + 1)%array.length;
        int newIndex = (frontIndex + (newPosition - 1)) % array.length;
        for (; destIndex!= newIndex; sourceIndex = (sourceIndex + 1)%array.length,
                destIndex = (sourceIndex + 1)%array.length)
            array[destIndex] = array[sourceIndex];
    }
    
    private boolean isFull(){
        return numberOfEntries == array.length - 1;
    }
    
    private void doubleArray(){
        if(isFull()){
            rearrangeQueue();
            T[] oldQueue = array;
            int newQueueLength = array.length * 2;
            array = (T[]) new Object[newQueueLength];
            System.arraycopy(oldQueue, 0, array, 0, numberOfEntries);
        }
    }
    
    private void rearrangeQueue(){
        if(!isEmpty()){
            T[] temperaryArray = array;
            for(int i = 0, front = frontIndex; i > 0; i++, front = (front + 1) % array.length)
                array[i] = temperaryArray[front];
            frontIndex = 0;
            backIndex = numberOfEntries - 1;
        }
    }

    @Override
    public boolean equals(QueueInterface givenEntry) {
        return givenEntry instanceof CircularArrayQueue;
    }

    @Override
    public boolean contains(T givenEntry) {
        boolean successful = false;
        if(givenEntry != null){
            Iterator<T> checkContains = getQueueIterator();
            for(int index = 0 ; index < numberOfEntries && !successful ; index++){
                if(givenEntry == array[index])
                    successful = true;
            }
        }
        return successful;
    }
    
    @Override
    public int search(T givenEntry) {
        int matchPosition;
        if(givenEntry != null && !isEmpty()){
            Iterator<T> searchEntry = getQueueIterator();
            matchPosition = 0;
            while(searchEntry.hasNext()){
                matchPosition++;
                if(searchEntry.next() == givenEntry)
                    return matchPosition;
            }
        }
        matchPosition = -1;
        return matchPosition;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }
    
    @Override
    public Iterator<T> getQueueIterator() {
        return new QueueIterator();
    }
    
    @Override
    public Iterator<T> getQueueIterator(int startIndex) {
        if(numberOfEntries > startIndex && startIndex >= 0)
            return new QueueIterator(startIndex);
        return new QueueIterator();
    }
    
    private class QueueIterator implements Iterator<T>{
        private int front;
        private int times;
        
        public QueueIterator(){
            front = frontIndex;
            times = numberOfEntries;
        }
        
        public QueueIterator(int startIndex){
            front = (frontIndex + startIndex) % array.length;
            times = numberOfEntries - startIndex;
        }
        
        @Override
        public boolean hasNext(){
            return times > 0;
        }
        
        @Override
        public T next(){
            T returnValue = null;
            if(hasNext()){
                returnValue = array[front];
                front = (front + 1) % array.length;
                times--;
            }
            return returnValue;
        }
        
        @Override
        public void remove(){
            front = (front==0?array.length - 1:front - 1);
            array[front] = null;
            int removePosition;
            if(front >= frontIndex)
                removePosition = (front - frontIndex) + 1;//+1 is to get the position
            else
                removePosition = ((array.length - 1) - frontIndex) + (front + 1) + 1;//array.length - 1 is to get the index, front + 1 is because front may include 0.
            removeGap(removePosition);
            backIndex = (backIndex == 0? array.length - 1 : backIndex - 1);
            numberOfEntries--;
        }
    }

    @Override
    public String toString(){
        String outputStr = null;
        if(!isEmpty()){  
            for(int index = 0; index < numberOfEntries; index++){
                outputStr += array[index] + ", ";
            }
        }
        return outputStr.substring(0, outputStr.length() - 2);
    }
}
