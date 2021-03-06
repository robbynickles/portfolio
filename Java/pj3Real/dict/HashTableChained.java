/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
    int largePrime;
    SList[] buckets;
    int size;
    private static final double LOADFACTOR = .666;

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
      size = 0;
      int bigN = (int) Math.floor(sizeEstimate / LOADFACTOR);
      bigN = findPrime(bigN);
      largePrime = findPrime(bigN + 10000);//largePrime must be much bigger than # of buckets
      buckets = new SList[bigN];
      for (int i = 0; i < bigN; i++) {
          buckets[i] = new SList();
      }
      
  }
    /* Find a prime bigger than n.
       @parameter n is the number around which a prime number is found.
       @return int is the prime number returned. It is bigger than and near n.
    */
    private int findPrime(int n) {
        int bigN = n + 100;//give myself interval of 100 to find a prime.
        
        //Sieve all non-primes in [2, n + 100]
        boolean[] prime = new boolean[bigN + 1]; 
        for (int i = 2; i <= bigN; i++) {
            prime[i] = true;
        }
        for (int divisor = 2; divisor * divisor <= bigN; divisor++) {
            if (prime[divisor]) {
                for (int i = divisor * 2; i <= bigN; i = i + divisor) {
                    prime[i] = false;
                }
            }
        }
        //find a prime bigger than and near n.
        for (int i = n; i < bigN; i++) {
            if (prime[i]) {
                return i;
            }
        }
        return 2;
    }


  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
      size = 0;
      buckets = new SList[107];
      largePrime = findPrime(10000);
      for (int i = 0; i < 107; i++) {
          buckets[i] = new SList();
      }
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
      return Math.abs(((3 * code + 8) % largePrime) % buckets.length);
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return size;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    return size == 0;
  }
    /*This method is called at each call to insert(). It creates an array roughly 1/LOADFACTOR 
      times the size of the current array and then hashes all entries to the new array. 
      It does this only when (# entries / # buckets) exceeds LOADFACTOR.
    */
    private void resize() {
        if ((double) size / buckets.length > LOADFACTOR) {
            HashTableChained newTable = new HashTableChained(2 * buckets.length);
            for (int i = 0; i < buckets.length; i++) {
                SListNode current = (SListNode) buckets[i].front();
                try {
                    while (current.isValidNode()) {
                        Entry entry = (Entry) current.item();
                        newTable.insert(entry.key(), entry.value());
                        current = (SListNode) current.next();
                    }
                } catch(InvalidNodeException e1) {
                    System.err.println("Tried to use invalid node.");
                }
            }
            buckets = newTable.buckets;
            largePrime = newTable.largePrime;
        }
    }
        
     
            
  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
      resize();//only if it needs to.
      Entry e = new Entry();
      e.key = key;
      e.value = value;
      int i = compFunction(key.hashCode());
      buckets[i].insertFront(e);
      size++;
      return e;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
      int i = compFunction(key.hashCode());
      SList chain = buckets[i];
      try {
          for (SListNode n = (SListNode) chain.front(); n.isValidNode(); n = (SListNode) n.next()) {
              Entry e = (Entry) n.item();
              if (e.key.equals(key)) {
                  return e;
              }
          }
      } catch(InvalidNodeException e) {
          System.out.println(e);
      }
      return null;
  }
  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
      int i = compFunction(key.hashCode());
      SList chain = buckets[i];
      try {
          for (SListNode n = (SListNode) chain.front(); n.isValidNode(); n = (SListNode) n.next()) {
              Entry e = (Entry) n.item();
              if (e.key.equals(key)) {
                  n.remove();
                  size--;
                  return e;
              }
          }
      } catch(InvalidNodeException e) {
          System.out.println(e);
      }
      return null; 
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
      for (int i = 0; i < buckets.length; i++) {
          buckets[i] = new SList();
      }
      size = 0;
  }

    /* Print the number of collisions. Specifically, print
       the number of entries in the table, the number of buckets with 
       two entries, the number of buckets with 3 entries, with 4 entries, 
       and with 5 or more entries.
    */
    public void countCollisions() {
        int col2 = 0;
        int col3 = 0;
        int col4 = 0;
        int col5OrMore = 0;
       
        for (int x = 0; x < buckets.length; x++) {
            SList chain = (SList) buckets[x];
            if (chain.length() == 2) {
                col2++;
            }
            if (chain.length() == 3) {
                col3++;
            }
            if (chain.length() == 4) {
                col4++;
            }
            if (chain.length() >= 5) {
                col5OrMore++;
            }
        }
        System.out.println("=========HASHTABLE================");
        System.out.println("# of entries: " + size);
        System.out.println("Number of single collisions: "+col2);
        System.out.println("Number of double collisions: "+col3);
        System.out.println("Number of triple collisions: "+col4);
        System.out.println("Number of 5 or more collisions: "+col5OrMore);
    }

    //This is test code.
    public static void main(String[] args) {
        HashTableChained hashtable = new HashTableChained();
        int i = 1;
        while (i <= 50) {
            int rando = (int) Math.random() * 1000;
            hashtable.insert(new Integer(rando), "TestVal");
            i++;
        }
        hashtable.insert("Robby", "This is the one.");
        while (i <= 1000) {
            int rando = (int) Math.random() * 1000;
            hashtable.insert(new Integer(rando), "TestVal");
            i++;
        }
        System.out.println(hashtable.size());
        System.out.println(hashtable.find("Robby").value());
    }
}
                       
