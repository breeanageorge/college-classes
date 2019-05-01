package edu.csus.csc130.assignment3;

public interface OrderedSymbolTable<Key extends Comparable<Key>, Value> 
		extends SymbolTable<Key, Value> {
	public Key min(); //smallest key
	
	public Key max(); //largest key
	
	public Key floor(Key key); //largest key less than or equal to key
	
	public Key ceiling(Key key); //smallest key greater than or equal to key
	
	public int rank(Key key); //number of keys less than key
	
	public Key select(int k); //key of rank k
	
	public void deleteMin(); //delete smallest key
	
	public void deleteMax(); //delete largest key
	
	public int size(Key lo, Key hi); //number of keys in [lo..hi]
	
	public Iterable<Key> keys(Key lo, Key hi); //keys in [lo..hi], in sorted order
}
