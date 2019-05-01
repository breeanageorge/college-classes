package edu.csus.csc130.assignment3;

public abstract class AbstractOrderedSymbolTable<Key extends Comparable<Key>, Value> 
	extends AbstractSymbolTable<Key, Value> implements OrderedSymbolTable<Key, Value> {

	public void deleteMin() {
		delete(min());
	}

	public void deleteMax() {
		delete(max());
	}

	public int size(Key lo, Key hi) {
		if (hi.compareTo(lo) < 0)
			return 0;
		else if (contains(hi))
			return rank(hi) - rank(lo) + 1;
		else
			return rank(hi) - rank(lo);
	}
	
	public Iterable<Key> keys() {
		return keys(min(), max());
	}	

}
