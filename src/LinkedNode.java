// --== CS400 File Header Information ==--
// Name: Xihe Li
// Email: xli2272@wisc.edu
// Team: Blue
// Group: IG
// TA: Siddharth Mohan
// Lecturer: Florian Heimerl
// Notes to Grader: First implementation, all tests passed. Don't know how. Might have bugs.

public class LinkedNode<KeyType, ValueType> {

  private KeyType key;
  private ValueType value;
  private LinkedNode<KeyType, ValueType> next;

  public LinkedNode(KeyType key, ValueType value, LinkedNode next) {
    this.key = key;
    this.value = value;
    this.next = next;
  }

  public LinkedNode(KeyType key, ValueType value) {
    this.key = key;
    this.value = value;
    this.next = null;
  }

  public LinkedNode(LinkedNode<KeyType, ValueType> next) {
    this.key = null;
    this.value = null;
    this.next = next;
  }

  public LinkedNode() {
    this.key = null;
    this.value = null;
    this.next = null;
  }

  public KeyType getKey() {
    return key;
  }

  public ValueType getValue() {
    return value;
  }

  public LinkedNode<KeyType, ValueType> getNext() {
    return next;
  }

  public void setKey(KeyType key) {
    this.key = key;
  }

  public void setValue(ValueType value) {
    this.value = value;
  }

  public void setNext(LinkedNode<KeyType, ValueType> next) {
    this.next = next;
  }

}
