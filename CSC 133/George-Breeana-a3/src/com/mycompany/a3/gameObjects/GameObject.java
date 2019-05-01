package com.mycompany.a3.gameObjects;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Rectangle;
import com.mycompany.a3.gameObjects.interfaces.IDrawable;

public abstract class GameObject implements IDrawable{
    private Rectangle bounds;
	protected int color;
	private int size;
	private double locationX;
	private double locationY;

	public GameObject() {
		
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setLocationX(double x) {
		this.locationX = x;
	}
	
	public double getLocationX() {
		return locationX;
	}

	public void setLocationY(double y) {
		this.locationY = y;
	}
	
	public double getLocationY() {
		return locationY;
	}
	
	public int getColor() {
		return color;
	}

	public int changeColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}


	public Rectangle getBounds() {
        return bounds;
    }

	public void setBounds(Rectangle rect) {
	    this.bounds = rect;
	}
	
	public abstract void draw(Graphics g,Point pCmpRelPrnt);
}