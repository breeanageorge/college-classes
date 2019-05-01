package breeana.george.assignment1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


/**
 * Modified by Breeana George 
 *
 */
public class ListStackQueue {
	/**
	 * @param parentheses
	 *            a string consists of only the following characters: {, [, (, ), ], }
	 * @return true is the parentheses are balanced, otherwise false
	 * Balanced examples: null, string with zero length, "[()]{}{[()()]()}"
	 * Unbalanced examples: "[(])", "{{}"
	 * Hint: 
	 * Use java.util.Stack as the stack implementation	  
	 */
	public static boolean isBalanced(String parentheses) {
		// provide your implementation here
		return false;
	}
	
	/**
	 * @param head the head of the input linked list
	 * @param item the given value
	 * @return the head of the linked list with nodes contains the given value removed
	 * Assume for any node in the  linked list, node.item cannot be null
	 */
	public static Node<String> removeNodes(Node<String> head, String item) {
		// provide your implementation here
		Node.curr=head;                                             //set current node 
		for(int i=0;i<getLength(head);i++){          //for loop to go through list
			if(Node.curr.item==item){                     //compare node content to provided string
				                                     //reassign next and prev pointers, then remove 
				/*e.previous.next = e.next;          //contents of node
				e.next.previous = e.previous;
				e.next = e.previous = null;
				e.element = null;
				*/
			}
			if(Node.prev == null){                   //if head is removed, set first node to head   
				Node.curr=head;
			}
		}
		return null;
	}
	
	/**
	 * @param n number of people
	 * @param m the position to be eliminated
	 * @return the position to be to avoid being eliminated, return -1 if (n<1) or (m<1)
	 * Hint:
	 * Use java.util.LinkedList, which implements Queue interface
	 * LinnkedList.add(E e) is the enqueue method
	 * LinkedList.remove() is the dequeue method
	 */
	public  static int getJosephusPosition(int n, int m) {
		// provide your implementation here
		return -1;
	}
	
	/**
	 * @param items input array
	 * @return the first node of the linked list build from the input array
	 */
	public static <E> Node<E>  buildList(E[] items) {
		Node<E> head = null;
		if (items!=null && items.length>0) {
			head = new Node<E> (items[0], null);
			Node<E> tail = head;
			for (int i=1; i<items.length; i++) {
				tail.next = new Node<E>(items[i], null);
				tail = tail.next;
			}
		}
		return head;
	}
	
	/**
	 * @param head the first node of the linked list
	 * @return the length of the linked list
	 */
	public static <E> int getLength(Node<E> head) {
		int length = 0;
		Node<E> node = head;
		while (node!=null) {
			length++;
			node = node.next;
		}
		return length;
	}
	
	public static <E> E get(Node<E> head, int index) {
		E item = null;
		Node<E> node = head;
		for (int i=0; i<index; i++) {
			if (node != null) {
				node = node.next;
			} else {
				break;
			}
		}
		if (node!=null) {
			item = node.item;
		}
		return item;
	}
	
	public static class Node<E> {
		public static Node<String> curr;
		public static Object prev;
		E item;
		Node<E> next;
		
		public Node(E item) {
			this.item = item;
			this.next = null;
		}
		
		public Node(E item, Node<E> next) {
			this.item = item;
			this.next = next;
		}
	}

}
