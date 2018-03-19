import java.util.ArrayList;
import java.util.NoSuchElementException;

///////////////////////////////////////////////////////////////////////////////
//Title:            P3
//Files:            Hash Table.java, PerformanceAnalysisTest.java, AnalysisTest.java, 
//                  HashTableADT.java, PerformanceAnalysis.java
//Semester:         CS 400 Spring 2018
//
//Authors:          Matt Zimmers, Tarun Mandalapu
//Email:            tmandalapu@wisc.edu, mzimmers@wisc.edu
//Lecturer's Name:  Debra Deppeler
//Source Credits:   
//Known Bugs:       bytes used differs greatly from sample file (might be due to our implementation)_
///////////////////////////////////////////////////////////////////////////////

public class HashTable<K, V> implements HashTableADT<K, V> {
    
    /**
     * Number of items in the hash table
     */
    private int numItems = 0;
    
    /**
     * The actual hash table for elements to be added into. It contains 
     * ArrayLists as buckets when the hash function yields the same
     * index for a given key
     */
    private ArrayList<ArrayList<kvHolder>> ht;
    
    /**
     * The current load factor of the hash table
     */
    private double LF = 0;
    
    /**
     * The upper limit for the load factor before resizing occurs
     */
    private double maxLF;
    
    protected class kvHolder {
        private K key;
        private V val;
        public kvHolder(K k, V v)
        {
            this.key = k;
            this.val = v;
        }
        public K getKey()
        {
            return key;
        }
        public V getVal()
        {
            return val;
        }
        public void setKey(K newKey)
        {
            this.key = newKey;
        }
        public void setVal(V newVal)
        {
            this.val = newVal;
        }
    }
    /**
     * Sets up the hash table and establishes the load factor rule for it
     * @param initialCapacity specifies the amount of null values that will
     * already be entered. This can optimize resizing
     * @param loadFactor the upper limit of the load factor of the hash table
     * before resizing
     */
    public HashTable(int initialCapacity, double loadFactor) {
        ht = new ArrayList<ArrayList<kvHolder>>();
        
        // Add null values until initial capacity reached
        for(int i = 0; i < initialCapacity; i++) {
            ht.add(null);
        }
        maxLF = loadFactor;
    }
    
    /**
     * Sends a key through a hash function to determine where in the hash table
     * the element will go. When the hash function returns an index that is already
     * occupied, the element is added to the end of that index's existing ArrayList.
     * This method also checks the load factor for every iteration.
     */
    @Override
    public V put(K key, V value) {
        if(key == null) throw new NullPointerException();
        
        int index = this.hashFunction(key);
        kvHolder kv = new kvHolder(key, value);
        if (ht.get(index) == null) { // No bucket yet
            //System.out.println("Worked");
            ArrayList<kvHolder> bucket = new ArrayList<kvHolder>();
            bucket.add(kv);
            ht.set(index, bucket);
        } else { // Element at index exists, append bucket
            ht.get(index).add(kv);
            //System.out.println("Bucket size: " + ht.get(index).size() + " for index: " + index);
        }
        numItems++;
        LF = (double)numItems/ht.size();
        if (LF >= maxLF) {
            this.resize();
        }    
        return null;
    }
    
    /**
     * Called when the maximum load factor is reached by the put method
     * only. Creates a temporary hash table and modifies the one the
     * instance of the class has a reference for.
     */
    private void resize() {
        // old hash table copied to a separate array list
        ArrayList<ArrayList<kvHolder>> oldHT = new ArrayList<ArrayList<kvHolder>>();
        for (ArrayList<kvHolder> a : ht) oldHT.add(a);
        
        // clear and resize local hash table
        int size = ht.size();
        ht.clear();
        for (int i = 0; i < size*2; i++) {
            ht.add(null);
        }
        
        // Rehash the local hash table using the old "copy"
        for (ArrayList<kvHolder> a : oldHT) {
            if (a != null) {
                for (kvHolder kv : a) {
                    put(kv.getKey(),kv.getVal());
                }
            }
        }
    }
    
    /**
     * This function uses lexicographic comparison of a constant character "a"
     * with each character in the toString representation of the key.
     * @param key the element being passed into for which the algorithm depends on
     * @return the index forumated as a result of the key
     */
    private int hashFunction(K key) {
        int[] primeNumbers = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47 };
        int pLength = primeNumbers.length;
        
        String s = key.toString();
        int index = 1;
        boolean add = true;
        int compareNum;
        
        for (int i = 0; i < s.length(); i++) {
            compareNum = Math.abs("a".compareTo(s.substring(i, i+1)));

            if (compareNum != 0 && add) index += compareNum*((int)Math.pow(primeNumbers[i%pLength],i));
            else if (compareNum != 0 && !add) index -= compareNum*(int)Math.pow(primeNumbers[i%pLength],i);
            
            index = Math.abs(index);
            if (index == Integer.MAX_VALUE) add = false;
            else if (index == 0) {
                add = true;
                index = 1;
            }
        }
        index = index%(ht.size());
        return Math.abs(index);
    }
    
    /**
     * Sets every element in the hash table to null
     */
    @Override
    public void clear() {
       for(int i = 0; i < ht.size(); i++) {
           ht.set(i, null);
       }
       numItems = 0;
    }

    /**
     * Searches for the key based on the index returned by
     * the hash function. If not found, it throws an exception.
     */
    @Override
    public V get(K key) {
        int index = this.hashFunction(key);
        if (ht.get(index) != null) {
            for (kvHolder kv : ht.get(index)) { // traverse bucket
                if (kv.getKey().equals(key)) {
                    return kv.getVal();
                }
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Simply returns whether or not the hash table is empty
     */
    @Override
    public boolean isEmpty() {
        return (this.numItems == 0);
    }

    /**
     * Uses the hash function to get the index at which the key exists
     * in the external ArrayList and then uses an ArrayList funciton to
     * delete the key from the internal ArrayList.
     */
    @Override
    public V remove(K key) {
       if (key == null) throw new NullPointerException();
       int index = this.hashFunction(key);
       ArrayList<kvHolder> bucket = ht.get(index);
       V result = null;
       if (bucket != null) {
           for (kvHolder kv : bucket) { // traverse bucket
               if (kv.getKey().equals(key)) {
                   bucket.remove(kv);
                   result = kv.getVal();
                   break;
               }
           }
       }
       numItems--;
       return result;
    }

    /**
     * Simply returns the numItems variable as the amount of items in the
     * hash table
     */
    @Override
    public int size() {
        return this.numItems;
    }
}
