// --== CS400 File Header Information ==--
// Name: Xihe Li
// Email: xli2272@wisc.edu
// Team: Blue
// Group: IG
// TA: Siddharth Mohan
// Lecturer: Florian Heimerl
// Notes to Grader: First implementation, all tests passed. Don't know how. Might have bugs.

import static java.lang.Math.abs;

import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

  private LinkedNode<KeyType, ValueType>[] map;
  private int size;

  /**
   * This constructor is the default constructor that initiates the map array with a default
   * capacity of 10. The constructor will initialize the map array.
   */
  public HashTableMap() {
    map = new LinkedNode[10];
  }

  /**
   * This constructor takes in a parameter that is used as the initial capacity of the map array
   * rather than using the default capacity (10). The constructor will initialize the map array.
   *
   * @param capacity initial capacity of the map array
   */
  public HashTableMap(int capacity) {
    map = new LinkedNode[capacity];
  }

  /**
   * Add a key-value pair to the map array where the index is the absolute value of the key's
   * hashCode() modulus the current capacity of the map array. Collisions are handled by using
   * chains. The method will return true only when successfully inserted the key-value pair, false
   * otherwise. If the load capacity becomes greater than or equal to 85%, double map capacity and
   * rehash.
   *
   * @param key   input key to be put into the map
   * @param value input value to be put into the map
   * @return true only when successfully inserted the key-value pair, false otherwise.
   */
  @Override
  public boolean put(KeyType key, ValueType value) {
    if (key == null || containsKey(key)) {
      return false;
    }

    int index = getIndex(key);
    if (map[index] != null) {
      LinkedNode<KeyType, ValueType> current = map[index];
      while (current.getNext() != null) {
        current = current.getNext();
      }
      LinkedNode<KeyType, ValueType> temp = new LinkedNode<>(key, value);
      current.setNext(temp);
    } else {
      map[index] = new LinkedNode<>(key, value);
    }

    size++;

    if (getLoadCapacity() >= 0.85) {
      expandAndRehash();
    }
    return true;
  }

  /**
   * Find and return the value that corresponds to the key entered.
   *
   * @param key input key to be used to get the value associated with it
   * @return the value corresponding to the key
   * @throws NoSuchElementException if the key is not in the map
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    ValueType result = null;
    for (int i = 0; i < map.length; i++) {
      if (map[i] != null) {
        if (map[i].getKey().equals(key)) {
          result = map[i].getValue();
          break;
        }
        LinkedNode<KeyType, ValueType> current = map[i];
        while (current.getNext() != null) {
          current = current.getNext();
          if (current.getKey().equals(key)) {
            result = current.getValue();
            break;
          }
        }
      }
    }
    if (result == null) {
      throw new NoSuchElementException("No such key in the current map.");
    }
    return result;
  }

  /**
   * Getter for the number of key-value pairs in the current map.
   *
   * @return number of key-value pairs in the current map
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Looks for the key and returns whether or not it exists in the current map.
   *
   * @param key input key to be looked up
   * @return true if found, false otherwise
   */
  @Override
  public boolean containsKey(KeyType key) {
    boolean found = false;
    for (int i = 0; i < map.length; i++) {
      if (map[i] != null) {
        if (map[i].getKey().equals(key)) {
          found = true;
          break;
        }
        LinkedNode<KeyType, ValueType> current = map[i];
        while (current.getNext() != null) {
          if (current.getNext().getKey().equals(key)) {
            found = true;
            break;
          }
          current = current.getNext();
        }
      }
    }
    return found;
  }

  /**
   * Remove the key-value pair based on the entered key and return the value.
   *
   * @param key input key used to identify the key-value pair to be removed
   * @return value that was removed
   */
  @Override
  public ValueType remove(KeyType key) {
    ValueType result = null;
    for (int i = 0; i < map.length; i++) {
      if (map[i] != null) {
        if (map[i].getKey().equals(key)) {
          result = map[i].getValue();
          map[i] = map[i].getNext();
          break;
        }
        LinkedNode<KeyType, ValueType> current = map[i];
        while (current.getNext() != null) {
          if (current.getNext().getKey().equals(key)) {
            result = current.getValue();
            current.setNext(current.getNext().getNext());
            break;
          }
          current = current.getNext();
        }
      }
    }
    if (result != null) {
      size--;
    }
    return result;
  }

  /**
   * This method clears the map array by assigning a new array to the map variable.
   */
  @Override
  public void clear() {
    map = new LinkedNode[map.length];
  }

  /**
   * Find the key's index by taking the absolute value of the key's hashCode() modulus the current
   * map capacity.
   *
   * @param key input key to be turned into an index
   * @return result index of the key
   */
  private int getIndex(KeyType key) {
    return abs(key.hashCode()) % map.length;
  }

  /**
   * Calculate and return the current load capacity.
   *
   * @return current load capacity
   */
  private float getLoadCapacity() {
    return (float) size / (float) map.length;
  }

  /**
   * Getter method for the current map.
   *
   * @return current map
   */
  public LinkedNode[] getMap() {
    return map;
  }

  /**
   * Expand the map array by doubling the capacity and then rehashing.
   */
  private void expandAndRehash() {
    HashTableMap temp = new HashTableMap(map.length * 2);
    for (int i = 0; i < map.length; i++) {
      if (map[i] != null) {
        temp.put(map[i].getKey(), map[i].getValue());
        LinkedNode current = map[i];
        while (current.getNext() != null) {
          current = current.getNext();
          temp.put(current.getKey(), current.getValue());
        }
      }
    }
    map = temp.getMap();
  }
}
