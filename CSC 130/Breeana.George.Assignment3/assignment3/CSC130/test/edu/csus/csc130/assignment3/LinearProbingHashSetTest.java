package edu.csus.csc130.assignment3;

import java.util.Random;

import org.junit.Assert;

import org.junit.Test;

public class LinearProbingHashSetTest {
	
	@Test
	public void test1() {
		LinearProbingHashSet<Integer> set = new LinearProbingHashSet<Integer>(7);
		Assert.assertEquals(0, set.size());
		Assert.assertTrue(set.isEmpty());
		Assert.assertFalse(set.contains(1));
		
		set = new LinearProbingHashSet<Integer>();
		Assert.assertEquals(0, set.size());
		Assert.assertTrue(set.isEmpty());	
		Assert.assertFalse(set.contains(2));
				
	}
	@Test
	public void test2() {
		LinearProbingHashSet<Integer> set = new LinearProbingHashSet<Integer>(7);	
		int[] a = {69, 82, 82, 29, 20, 64, 27, 74, 25, 75, 8, 71};
		for (int  i=0; i<a.length; i++) {
			set.add(a[i]);
			Assert.assertFalse(set.isEmpty());
		}
		
		Assert.assertEquals(a.length-1, set.size());
		
		for (int i=0; i<a.length; i++) {
			Assert.assertTrue(set.contains(a[i]));
		}
			
		Random random = new Random();
		Integer key = a[random.nextInt(a.length)];	
		set.delete(key);
		Assert.assertFalse(set.contains(key));
	}
	
	@Test public void test3() {
		LinearProbingHashSet<Integer> set = new LinearProbingHashSet<Integer>();	
		int[] a = {37, 97, 5, 65, 16, 50, 19, 89, 45, 70, 99};
		for (int  i=0; i<a.length; i++) {
			set.add(a[i]);
			Assert.assertFalse(set.isEmpty());
			Assert.assertEquals(i+1, set.size());
		}
		
		for (int i=0; i<a.length-1; i++) {
			Assert.assertTrue(set.contains(a[i]));
			set.delete(a[i]);
			Assert.assertFalse(set.isEmpty());
			Assert.assertEquals(a.length-1-i, set.size());	
			Assert.assertFalse(set.contains(a[i]));
		}
		
		set.delete(a[a.length-1]);
		Assert.assertEquals(0, set.size());
		Assert.assertTrue(set.isEmpty());
	}

}
