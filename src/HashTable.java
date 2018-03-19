
public class HashTable<K, V> implements HashTableADT<K, V> {
    /* Instance variables and constructors
     */
    int numItems = 0;

    public HashTable(int initialCapacity, double loadFactor) {
        
    }
    
    @Override
    public V put(K key, V value) {
        //TODO: Implement put method - using efficient algorithm
        return null;
    }

    @Override
    public void clear() {
       //TODO: Implement this method
    }

    @Override
    public V get(K key) {
        //TODO: Implement the get method
        return null;
    }

    @Override
    public boolean isEmpty() {
        return (this.numItems == 0);
    }

    @Override
    public V remove(K key) {
       //TODO: Implement the remove method
        return null;
    }

    @Override
    public int size() {
        return this.numItems;
    }
}
