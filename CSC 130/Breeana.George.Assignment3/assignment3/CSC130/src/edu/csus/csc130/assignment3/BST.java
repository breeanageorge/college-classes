package edu.csus.csc130.assignment3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

//Edited by Breeana George
/**
 * 
 * Ordered Symbol Table implementation using Binary Search Tree
 */
public class BST<Key extends Comparable<Key>, Value> 
			extends AbstractOrderedSymbolTable<Key, Value>{
	private class Node {
		private Key key; // key
		private Value val; // associated value
		private Node left, right; // links to subtrees
		private int n; // # nodes in subtree rooted here

		public Node(Key key, Value val, int n) {
			this.key = key;
			this.val = val;
			this.n = n;
		}
	
	}
	
	private Node root; // root of BST
	
	// get the number of nodes from this tree
	public int size() {
		return size(root);
	}

	// get the number of nodes rooted at Node node
	private int size(Node node) {
		if (node == null)
			return 0;
		else
			return node.n;
	}
	
	public Value get(Key key) {
		return get(root, key);
	}
	
	// Return value associated with key in the subtree rooted at node;
	// return null if key not present in subtree rooted at node.
	private Value get(Node node, Key key) {
		if (node == null) return null;
		
		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			return get(node.left, key);
		else if (cmp > 0)
			return get(node.right, key);
		else
			return node.val;
	}

	public void put(Key key, Value val)	{
		root = put(root, key, val);
	}
	
	// Change key’s value to val if key in subtree rooted at node.
	// Otherwise, add new node to subtree associating key with val.
	private Node put(Node node, Key key, Value val) {
		if (node == null) return new Node(key, val, 1);
		
		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			node.left = put(node.left, key, val);
		else if (cmp > 0)
			node.right = put(node.right, key, val);
		else
			node.val = val;
		node.n = size(node.left) + size(node.right) + 1;
		
		return node;
	}

	@Override
	public Key min() {
		return min(root).key;
	}
	
	private Node min(Node node) {
		if (node.left == null)
			return node;
		return min(node.left);
	}

	@Override
	public Key max() {
		return max(root).key;
	}
	
	private Node max(Node node) {
		if (node.right==null) {
			return node;
		} else {
			return max(node.right);
		}
	}

	@Override
	// find the greatest key less or equal to the given key
	public Key floor(Key key) {
		// unimplemented
		return null;
	}

	@Override
	public Key ceiling(Key key) {
		// unimplemented
		return null;
	}

	@Override
	public int rank(Key key) {
		return rank(root, key);
	}
	
	// Return number of keys less than node.key in the subtree rooted at node.
	private int rank(Node node, Key key) {
		if (node == null) return 0;
		
		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			return rank(node.left, key);
		else if (cmp > 0)
			return 1 + size(node.left) + rank(node.right, key);
		else
			return size(node.left);
	}	

	@Override
	public Iterable<Key> keys(Key lo, Key hi) {
		Queue<Key> queue = new LinkedList<Key>();
		keys(root, queue, lo, hi);
		return queue;
	}
	
	private void keys(Node node, Queue<Key> queue, Key lo, Key hi) {
		if (node == null) return;
		
		int cmplo = lo.compareTo(node.key);
		int cmphi = hi.compareTo(node.key);
		if (cmplo < 0)
			keys(node.left, queue, lo, hi);
		if (cmplo <= 0 && cmphi >= 0)
			queue.add(node.key);
		if (cmphi > 0)
			keys(node.right, queue, lo, hi);
	}	

	@Override
	public void delete(Key key) {
		delete(root, key);
	}
	
	private Node delete(Node node, Key key) {
		if (node == null) return null;
		
		int cmp = key.compareTo(node.key);
		if (cmp < 0) {
			node.left = delete(node.left, key);
		} else if (cmp > 0) {
			node.right = delete(node.right, key);
		} else {
			if (node.right == null) {
				return node.left;
			} else if (node.left == null) {
				return node.right;
			} else {
				Node curNode = node;
				node = min(curNode.right); // curNode's successor
				node.right = deleteMin(curNode.right);
				node.left = curNode.left;
			}
		}
		node.n = size(node.left) + size(node.right) + 1;
		return node;
	}
	
	public void deleteMin() {
		root = deleteMin(root);
	}
	
	// delete the node with minimum key 
	// and return the new root of the subtree
	private Node deleteMin(Node node) {
		if (node.left == null) return node.right;
		node.left = deleteMin(node.left);
		node.n = size(node.left) + size(node.right) + 1;
		return node;
	}
	
	public void printInorder() {
		printInorder(root);
	}
	
	private void printInorder(Node node) {
		if (node == null) return;
		printInorder(node.left);
		System.out.print(node.key + ", ");
		printInorder(node.right);
	}
	
	public void printPreorder() {
		printPreorder(root);
	}
	
	private void printPreorder(Node node) {
		if (node == null) return;
		System.out.print(node.key + ", ");
		printPreorder(node.left);
		printPreorder(node.right);		
	}
	
	public void printPostorder() {
		printPostorder(root);
	}
	
	private void printPostorder(Node node) {
		if (node == null) return;
		printPostorder(node.left);
		printPostorder(node.right);	
		System.out.print(node.key + ", ");
	}
	

	@Override
	// return the kth key
	public Key select(int k) {
		Node node = select(root, k);
		if (node==null) {
			return null;
		} else {
			return node.key;
		}
	}
	
	// Return Node containing kth key (zero based)
	private Node select(Node node, int k) { 
		if (node == null) return null;
		
		int t = size(node.left);
		if (t > k)
			return select(node.left, k);
		else if (t < k)
			return select(node.right, k - t - 1);
		else
			return node;
	}	
	
	/**
	 * Provide a non-recursive implementation of select(...)  here
	 */
		
	public Key selectI(int k){	
		BST<Key, Value>.Node curr = null;
		curr = root;
		while(curr != null){
			int t = size(curr.left);
			if(t>k){
				curr = curr.left;
			}else if(t<k){
				curr = curr.right;
				k=(k-t-1);
			}else{
				return curr.key;
			}
		}
		return null;
	}	
	
	public  int getHeight() {
		return getHeight(root);
	}
	
	private int getHeight(Node node) {
		if (node==null) {
			return -1;
		} else {
			int leftHeight = getHeight(node.left);
			int rightHeight = getHeight(node.right);
			return Math.max(leftHeight, rightHeight) + 1;
		}
	}
	
	public int getHeightI() {
		return getHeightI(root);
	}
	
	/**
	 * Provide a non-recursive implementation of getHeight() here
	 */
	private int getHeightI(Node node) {
		// provide your implementation here
	/*int maxheight = 0;
	int lheight = 0;
	int rheight = 0;
	BST<Key, Value>.Node currRight = null;
	currRight = root.right;
	BST<Key, Value>.Node currLeft = null;
	currLeft = root.left;
	if(root == null){
		return -1;
	}else if(node.right==null && node.left == null){
		return 0;
	}else{
		while(currRight!=null){
			rheight++;
			currRight = currRight.right;	
		}
		while(currLeft!=null){
			lheight++;
			currLeft = currLeft.left;
		}
		if(lheight>=rheight){
			maxheight = lheight;
		}else{
			maxheight= rheight;
		}
	}
	return maxheight;	
	*/
	//new code
		int height = 0;
	    if (node == null) 
			return -1;
	    Stack<Node> s = new Stack<>();
	    Stack<Node> s2 = new Stack<>();
	    s.push (node);
	    while (!s.empty()) {
	        node = s.peek();
	        if (!s2.empty() && node == s2.peek()) {
	            if (s2.size() > height)
	                height = s2.size() - 1;
	            s2.pop();
	            s.pop();
	        } else {
	            s2.push(node);
	            if (node.right != null)
	                s.push(node.right);
	            if (node.left != null)
	                s.push(node.left);
	        }
	    }
	    return height;
		
	}
}
