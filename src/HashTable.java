import java.util.ArrayList;
import java.util.NoSuchElementException;

public class HashTable<K, V> implements HashTableADT<K, V> {
    /* Instance variables and constructors
     */
    int numItems = 0;
    private ArrayList<ArrayList<K>> ht;
    double LF = 0;
    double maxLF;
    
    public HashTable(int initialCapacity, double loadFactor) {
        ht = new ArrayList<ArrayList<K>>();
        for(int i = 0; i < initialCapacity; i++)
        {
            ht.add(null);
        }
        maxLF = loadFactor;
    }
    
    @Override
    public V put(K key, V value) {
        if(key == null)
        {
            throw new NullPointerException();
        }
        int index = this.hashFunction(key);
        if(ht.get(index) == null)
        {
            ArrayList<K> bucket = new ArrayList<K>();
            bucket.add(key);
            ht.set(index, bucket);
        }
        else
        {
            ht.get(index).add(key);
        }
        numItems++;
        LF = numItems/ht.size();
        if(LF > maxLF)
        {
            this.resize();
        }
        
        return null;
    }
    
    private void resize()
    {
        ArrayList<ArrayList<K>> oldHT = new ArrayList<ArrayList<K>>();
        for(ArrayList<K> a : ht) oldHT.add(a);
        ht.clear();
        int size = ht.size();
        for (int i = 0; i < size; i++) {
            ht.add(null);
        }
        
        for (ArrayList<K> a : oldHT) {
            if (a != null) {
                for (K key : a) {
                    put(key,null);
                }
            }
        }
    }
    
    private int hashFunction(K key) {
        String s = key.toString();
        int index = 1;
        boolean multiply = true;
        int compareNum;
        
        for (int i = 0; i < s.length(); i++) {
            compareNum = Math.abs("a".compareTo(s.substring(i, i+1)));
            if (compareNum != 0 && multiply) index *= compareNum;
            else if (compareNum != 0 && !multiply) index /= compareNum;
            
            if (index == Integer.MAX_VALUE) multiply = false;
            else if (index == 0) {
                multiply = true;
                index = 1;
            }
        }
        index = index%ht.size();
        return index;
    }
    
    @Override
    public void clear() {
       for(int i = 0; i < ht.size(); i++)
       {
           ht.set(i, null);
       }
    }

    @Override
    public V get(K key) {
        boolean found = false;
        int index = this.hashFunction(key);
        if (ht.get(index) != null) {
            for (K k : ht.get(index)) {
                if (k.equals(key)) {
                    found = true;
                }
            }
        }
        
        if (!found) throw new NoSuchElementException();
        
        return null;
    }

    @Override
    public boolean isEmpty() {
        return (this.numItems == 0);
    }

    @Override
    public V remove(K key) {
       if (key == null) throw new NullPointerException();
       int index = this.hashFunction(key);
       ht.get(index).remove(key);
       
       numItems--;
        return null;
    }

    @Override
    public int size() {
        return this.numItems;
    }
}
