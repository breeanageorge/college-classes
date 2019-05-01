package edu.csus.csc130.assignment3;

import org.junit.Assert;
import org.junit.Test;

public class BSTTest {

	@Test
	public void testSelect1() {
		int[] a = {6, 61, 21, 75, 36, 92, 89, 57, 86, 41, 29, 17};
		BST<Integer, Integer> bst = createBST(a);
		Assert.assertEquals(6, (int) bst.selectI(0));
	}
	
	@Test
	public void testSelect2() {
		int[] a = {6, 61, 21, 75, 36, 92, 89, 57, 86, 41, 29, 17};
		BST<Integer, Integer> bst = createBST(a);
		Assert.assertEquals(92, (int) bst.selectI(11));
	}
	
	@Test
	public void testSelect3() {
		int[] a = {6, 61, 21, 75, 36, 92, 89, 57, 86, 41, 29, 17};
		BST<Integer, Integer> bst = createBST(a);
		Assert.assertEquals(41, (int) bst.selectI(5));
	}
	
	@Test
	public void testSelect4() {
		int[] a = {6, 61, 21, 75, 36, 92, 89, 57, 86, 41, 29, 17};
		BST<Integer, Integer> bst = createBST(a);
		Assert.assertEquals(57, (int) bst.selectI(6));
	}	
	
	@Test
	public void testHeight1() {
		int[] a = {6};
		BST<Integer, Integer> bst = createBST(a);
		Assert.assertEquals(0, bst.getHeightI());
	}
	
	@Test
	public void testHeight2() {
		int[] a = {1, 2, 3, 4, 5};
		BST<Integer, Integer> bst = createBST(a);
		Assert.assertEquals(4, bst.getHeightI());
	}	
	
	@Test
	public void testHeight3() {
		int[] a = {5, 4, 3, 2, 1};
		BST<Integer, Integer> bst = createBST(a);
		Assert.assertEquals(4, bst.getHeightI());
	}	
	
	@Test
	public void testHeight4() {
		int[] a = {4, 5, 3, 2, 1};
		BST<Integer, Integer> bst = createBST(a);
		Assert.assertEquals(3, bst.getHeightI());
	}	
	
	private BST<Integer, Integer> createBST(int[] a) {
		BST<Integer, Integer> bst = new BST<Integer, Integer>();
		for (int i=0; i<a.length; i++) {
			bst.put(a[i], i);
		}
		return bst;
	}

}
