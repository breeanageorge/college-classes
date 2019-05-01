package com.mycompany.a1;
import com.codename1.charts.util.ColorUtil; 
import java.util.Random;

public abstract class GameObject{
	private Random randomNumbers = new Random();
	private float x, y;
	private int color;

	public GameObject(){
		int myColor = ColorUtil.rgb(randomNumbers.nextInt(255),randomNumbers.nextInt(255),randomNumbers.nextInt(255));
		this.setColor(myColor);
		x = 0;
		y = 0;
	}

	//set method for both x and y location of object
	public void setLocation (float newX, float newY){
		x = newX;
		y = newY;
	}
	
	//get method for x location of object
	public float getLocationX (){
		return x;
	}
	
	//get method for y location of object
	public float getLocationY(){
		return y;
	}
	
	//get method for color
	public int getColor(){
		return color;
		
	}
	
	//set method for color
	public void setColor(int newColor){
		color = newColor;
		
	}
	
	//method to change color of obj
	public void changeColor(int newColor){
		setColor(newColor);	
	}
}