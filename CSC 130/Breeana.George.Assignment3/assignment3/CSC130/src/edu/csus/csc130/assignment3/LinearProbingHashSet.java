package edu.csus.csc130.assignment3;

//Edited by Breeana George

public class LinearProbingHashSet<Key extends Comparable<Key>> implements Set<Key> {
	private int n; // number of key-value pairs in the table
	private int m = 16; // size of linear-probing table
	private Key[] keys; // the keys
	
	@SuppressWarnings("unchecked")
	public LinearProbingHashSet() {
		keys = (Key[]) new Comparable[m];
	}
	
	@SuppressWarnings("unchecked")
	public LinearProbingHashSet(int capacity) {
		m = capacity;
		keys = (Key[]) new Comparable[m];
		// provide your implementation here
	}
	
	private int hash(Key key) {
		return (key.hashCode() & 0x7fffffff) % m;
	}
	
	private void resize(int cap) {
		LinearProbingHashSet<Key> t;
		t = new LinearProbingHashSet<Key>(cap);
		// insert key value pairs into the new table
		for (int i = 0; i < m; i++) {
			if (keys[i] != null) {
				t.add(keys[i]);
			}
		}
		// copy the new table fields to the existing table
		keys = t.keys;
		m = t.m;
	}	
	
	public Key get(Key key) {
		for (int i = hash(key); keys[i] != null; i = (i + 1) % m) {
			if (keys[i].equals(key))
				return keys[i];
		}
		return null;
	}

	
	@Override
	public void add(Key key) {
		if (n >= m / 2) resize(2 * m); 
		int i;
		for (i = hash(key); keys[i] != null; i = (i + 1) % m) {
			// search hit, replace the value
			if (keys[i].equals(key)) {
				//vals[i] = val;
				return;
			}
		}
		// search miss, insert the new key value pair
		keys[i] = key;
		//vals[i] = val;
		n++;

	}

	@Override
	public void delete(Key key) {
		if (!contains(key)) return;
		
		int i = hash(key);
		while (!key.equals(keys[i])) {
			i = (i + 1) % m;
		}
		keys[i] = null;
		
		i = (i + 1) % m;
		while (keys[i] != null) {
			Key keyToRedo = keys[i];
			keys[i] = null;
			n--;
			add(keyToRedo);
			i = (i + 1) % m;
		}
		
		n--;
		//ensure that the table is at least one-eighth full
		if (n > 0 && n == m / 8) {
			resize(m / 2);
		}

	}

	@Override
	public boolean contains(Key key) {
		return (get(key) != null);
	}

	@Override
	public boolean isEmpty() {
		return n==0;
	}

	@Override
	public int size() {
		return n;
	}
	
}
