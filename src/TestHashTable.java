// --== CS400 File Header Information ==--
// Name: Xihe Li
// Email: xli2272@wisc.edu
// Team: Blue
// Group: IG
// TA: Siddharth Mohan
// Lecturer: Florian Heimerl
// Notes to Grader: First implementation, all tests passed. Don't know how. Might have bugs.

import java.util.NoSuchElementException;

public class TestHashTable {

  /**
   * This method tests whether the constructors are working as intended. This test will create a
   * HashTableMap with and without a parameter and test whether the map array is created with the
   * correct capacity.
   *
   * @return true when working as intended, false otherwise
   */
  public static boolean test1() {
    HashTableMap<String, String> testMapA = new HashTableMap<>();
    boolean testA = testMapA.getMap().length == 10;

    HashTableMap<String, String> testMapB = new HashTableMap<>(100);
    boolean testB = testMapB.getMap().length == 100;

    return testA && testB;
  }

  /**
   * This method tests whether the put() method properly changes the result of the size() method. A
   * test HashTableMap will be created and 10 dummy key-value pair will be entered, and the size
   * will be checked each time.
   *
   * @return true when working as intended, false otherwise
   */
  public static boolean test2() {
    HashTableMap<Integer, Integer> testMapA = new HashTableMap<>(20);
    boolean result = true;
    for (int i = 0; i < 10; i++) {
      testMapA.put(i, i);
      if (testMapA.size() != i + 1) {
        result = false;
      }
    }
    return result;
  }

  /**
   * This method tests whether the put method extends the map array properly when the load capacity
   * reaches or exceeds 85%. This test will create a HashTableMap that has a capacity of 5, and add
   * 5 key-value pairs into the map, and then see whether the HashTableMap extends the map array to
   * a capacity of 10.
   *
   * @return true when working as intended, false otherwise
   */
  public static boolean test3() {
    HashTableMap<Integer, Integer> testMapA = new HashTableMap<>(5);
    for (int i = 0; i < 5; i++) {
      testMapA.put(i, i);
    }
    return testMapA.getMap().length == 10;
  }

  /**
   * This method tests whether a HashTableMap properly throws a NoSuchElementException when a key is
   * passed into the get method as a parameter. This method will create a test HashTableMap and call
   * the get method. Since there are no key-value pairs in the map, this should result in a
   * NoSuchElementException.
   *
   * @return true when working as intended, false otherwise
   */
  public static boolean test4() {
    try {
      HashTableMap<Integer, Integer> testMapA = new HashTableMap<>();
      Integer test = testMapA.get(1);
    } catch (NoSuchElementException nsee) {
      return true;
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  /**
   * This method tests whether the remove() method is functioning properly. This method will test
   * whether the remove method removes the key-value pair and whether this method will return null
   * if passed in a key that does not have a key-value pair in the map. This method will create a
   * test HashTableMap and add in dummy data. Then, it will call the remove method twice, where one
   * of them is previously added and the other is not.
   *
   * @return true when working as intended, false otherwise
   */
  public static boolean test5() {
    HashTableMap<Integer, Integer> testMapA = new HashTableMap<>(20);
    for (int i = 0; i < 10; i++) {
      testMapA.put(i, i);
    }
    boolean testA = testMapA.remove(5).equals(5);
    boolean testB = !testMapA.containsKey(5);
    boolean testC = testMapA.remove(100) == null;

    return testA && testB && testC;
  }

}
