package com.mycompany.a2.gameObjects.interfaces;
import com.codename1.charts.util.ColorUtil;
import com.mycompany.a2.gameObjects.GameObject;

public abstract class MoveableObject extends GameObject{
	private int heading, speed;
	private int newLocationX;
	private int newLocationY;
	
	public MoveableObject(){
		heading = 0;
		speed = 0;
	}

	//makes object move based on formula given
	public void move(int objSpeed){
		newLocationX = (int)(super.getLocationX() + Math.cos(Math.toRadians(90 - heading))*objSpeed);
		newLocationY = (int)(super.getLocationY() + Math.sin(Math.toRadians(90 - heading))*objSpeed);
		super.setLocation(newLocationX, newLocationY);
	}
	
	//get method for heading
	public int getHeading(){
		return heading;
	}
	
	//set method for changing heading
	public void setHeading(int newHeading){
		heading = newHeading;
	}
	
	//get method for speed
	public int getSpeed(){
		return speed;
	}
	
	//set method for speed
	public void setSpeed(int newSpeed){
		speed = newSpeed;
	}

	public void changeColor(int myColor) {
		// TODO Auto-generated method stub
		
	}

	public void changeSteering(int newDir) {
		// TODO Auto-generated method stub
		
	}
	
	public void changeHeading(int newHeading) {
		// TODO Auto-generated method stub
		
	}

	public void changeColor(ColorUtil newColor) {
		// TODO Auto-generated method stub
		
	}
}
