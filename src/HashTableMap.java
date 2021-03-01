// --== CS400 File Header Information ==--
// Name: Chi Sing Thao
// Email: cthao45@wisc.edu
// Team: blue
// Group: IG
// TA: Siddharth Mohan
// Lecturer: Gary Dahl

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
	
	//Variables
	public int capacity;
	public int size;
	private Object[] hashTableKey;
	private Object[] hashTableValue;
	
	//Constructors
	public HashTableMap(int capacity) {
		this.capacity = capacity;
		hashTableKey = new Object[capacity];
		hashTableValue = new Object[capacity];
	}
	
	public HashTableMap() { //default capacity = 10
		this.capacity = 10;
		hashTableKey = new Object[capacity];
		hashTableValue = new Object[capacity];
	}
	
	//Private Helper Methods
	
	private void addToKeyTable(KeyType key, int keyHashCode, Object[] table) {
		if(table[keyHashCode] == null) { //Stores key in hashTableKey with hashCode
			table[keyHashCode] = new LinkedList<KeyType>();
			((LinkedList<KeyType>) table[keyHashCode]).add(key);
		}
		else {
			((LinkedList<KeyType>) table[keyHashCode]).add(key);
		}
	}
	
	private void addToValueTable(ValueType value, int keyHashCode, Object[] table ) {
		if(table[keyHashCode] == null) { //Stores value in hashTableValue with key
			table[keyHashCode] = new LinkedList<ValueType>();
			((LinkedList<ValueType>) table[keyHashCode]).add(value);
		}
		else {
			 ((LinkedList<ValueType>) table[keyHashCode]).add(value);
		}
	}
	
	
	private void doubleCapcity() {
		capacity *= 2;
		Object[] newHashTableKey = new Object[capacity];
		Object[] newHashTableValue = new Object[capacity];
		for(int i = 0; i < hashTableKey.length; i++) {
			if(hashTableKey[i] != null) {
				LinkedList<KeyType> keyLinkedList = ((LinkedList<KeyType>) hashTableKey[i]);
				LinkedList<ValueType> valueLinkedList = ((LinkedList<ValueType>) hashTableValue[i]);
				for(int j = 0; j < keyLinkedList.size(); j++) {
					KeyType key = keyLinkedList.get(j);
					ValueType value = valueLinkedList.get(j);
					int keyHashCode = hash(keyLinkedList.get(j));
					addToKeyTable(key, keyHashCode, newHashTableKey);
					addToValueTable(value, keyHashCode, newHashTableValue);
				}
			}
		}
		hashTableKey = newHashTableKey;
		hashTableValue = newHashTableValue;
	}
	
	private int hash(KeyType key) {
		int hashCode;
		hashCode = Math.abs(key.hashCode() % capacity);
		return hashCode;
	}
	
	public void printKeyTable() {
		String array = "";
		array += "[";
		for(int i = 0; i < hashTableKey.length; i++) {
			if(hashTableKey[i] == null) {
				array += "null";
			}
			else {
				array += hashTableKey[i].toString();
			}
			if(i != hashTableKey.length - 1) {
				array += ", ";
			}
		}
		array += "]";
		String varray = "";
		varray += "[";
		for(int i = 0; i < hashTableValue.length; i++) {
			if(hashTableValue[i] == null) {
				varray += "null";
			}
			else {
				varray += hashTableValue[i].toString();
			}
			if(i != hashTableValue.length - 1) {
				varray += ", ";
			}
		}
		varray += "]";
		
		
		System.out.println("hashTableKey: " + array);
		System.out.println("hashTableValue: " + varray);
	}
	
	
	@Override
	public boolean put(KeyType key, ValueType value) {
		if(key != null) { //Check if Key is null
			if(containsKey(key)) {
				return false;
			}
			
			double loadCapacity = ((size + 1) / capacity);
			if(loadCapacity >= 0.85) {
				doubleCapcity();
			}
			
			int keyHashCode = hash(key);
			
			addToKeyTable(key, keyHashCode, hashTableKey);
			addToValueTable(value,keyHashCode, hashTableValue);
			
			size ++;
			return true;
		}
		return false;
	}

	@Override
	public ValueType get(KeyType key) throws NoSuchElementException {
		if(key != null) { //Check if Key is null
			int keyHashCode = hash(key);
			if(hashTableKey[keyHashCode] != null) {
				LinkedList<KeyType> keyLinkedList = ((LinkedList<KeyType>) hashTableKey[keyHashCode]);
				LinkedList<ValueType> valueLinkedList = ((LinkedList<ValueType>) hashTableValue[keyHashCode]);
				if(keyLinkedList != null) {
					for(int i = 0; i < keyLinkedList.size(); i++) {
						if(key.equals(keyLinkedList.get(i))) {
							return valueLinkedList.get(i);
						}
					}
				}
			}
		}
		throw new NoSuchElementException();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public boolean containsKey(KeyType key) {
		int keyHashCode = hash(key);
		if(hashTableKey[keyHashCode] != null) {
			LinkedList<KeyType> keyLinkedList = ((LinkedList<KeyType>) hashTableKey[keyHashCode]);
			if(keyLinkedList != null) {
				for(int i = 0; i < keyLinkedList.size(); i++) {
					if(key.equals(keyLinkedList.get(i))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public ValueType remove(KeyType key) {
		if(containsKey(key)) {
			int keyHashCode = hash(key);
			if(hashTableKey[keyHashCode] != null) {
				LinkedList<KeyType> keyLinkedList = ((LinkedList<KeyType>) hashTableKey[keyHashCode]);
				LinkedList<ValueType> valueLinkedList = ((LinkedList<ValueType>) hashTableValue[keyHashCode]);
				if(keyLinkedList != null) {
					for(int i = 0; i < keyLinkedList.size(); i++) {
						if(key.equals(keyLinkedList.get(i))) {
							ValueType value = valueLinkedList.get(i);
							valueLinkedList.remove(i);
							keyLinkedList.remove(i);
							size --;
							return value;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public void clear() {
		size = 0;
		hashTableKey = new Object[capacity];
		hashTableValue = new Object[capacity];
	}

}
