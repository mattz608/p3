import java.util.ArrayList;

public class HashTable<K, V> implements HashTableADT<K, V> {
    /* Instance variables and constructors
     */
    int numItems = 0;
    private ArrayList<K> ht;
    

    public HashTable(int initialCapacity, double loadFactor) {
        ht = new ArrayList<K>();
        for(int i = 0; i < initialCapacity; i++)
        {
            ht.add(null);
        }
    }
    
    @Override
    public V put(K key, V value) {
        if(key == null)
        {
            throw new NullPointerException();
        }
        return null;
    }

    @Override
    public void clear() {
       //TODO: Implement this method
    }

    @Override
    public V get(K key) {
//        V val = ht.get();
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