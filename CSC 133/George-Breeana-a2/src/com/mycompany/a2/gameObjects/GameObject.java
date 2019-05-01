package com.mycompany.a2.gameObjects;
import java.util.Vector;

public abstract class GameObject{
	private Vector<Float> location;
	protected int color;
	private int size;

	public GameObject() {
		location = new Vector<Float>();
		location.setSize(2);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public float getLocationX() {
		return location.get(0);
	}

	public float getLocationY() {
		return location.get(1);
	}

	public void setLocation(float x, float y) {
		location.set(0, x);
		location.set(1, y);
	}
}