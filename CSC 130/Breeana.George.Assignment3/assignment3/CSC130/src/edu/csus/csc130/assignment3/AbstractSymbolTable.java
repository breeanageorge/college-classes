package edu.csus.csc130.assignment3;

public abstract class AbstractSymbolTable<Key extends Comparable<Key>, Value> 
		implements SymbolTable<Key, Value> {

	@Override
	public boolean contains(Key key) {
		return get(key) != null;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

}
