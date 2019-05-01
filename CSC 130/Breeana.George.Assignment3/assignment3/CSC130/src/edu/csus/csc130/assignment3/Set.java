package edu.csus.csc130.assignment3;

public interface Set <Key extends Comparable<Key>>{
	void add(Key key); //add key into the set
	void delete(Key key); //remove key from the set
	boolean contains(Key key); //is key in the set?
	boolean isEmpty(); //is the set empty?
	int size(); //number of keys in the set
}
