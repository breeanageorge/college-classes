package com.mycompany.a2.gameObjects;

import com.mycompany.a2.gameObjects.interfaces.ISteerable;
import com.mycompany.a2.gameObjects.interfaces.MoveableObject;

public class Car extends MoveableObject implements ISteerable{
	//private variables for car
	private int width = 20;
	private int length = 40;
	private int steeringDirection = 0;
	private int maximumSpeed = 50;
	private int fuelLevel = 100;
	private int damageLevel = 0;
	
	//Constructor sets heading, speed, location
	public Car(float x, float y){
		this.setHeading(0);
		this.setSpeed(0);
		this.setLocation(x, y);
	}

	//set method for steering
	public void setSteering(int newDir){
		if(newDir > 0)		//if steering is to the right/positive
		{	
			if(steeringDirection < 0)		
				steeringDirection = 0;
			
			if(steeringDirection >= 40)		//prevents steering direction from being greater than 40
				steeringDirection = 40;
			else
				steeringDirection += newDir;
		}
		else			//if steering is to the left/negative
		{
			if(steeringDirection > 0)
				steeringDirection = 0;
			
			if(steeringDirection <= -40)	//prevents steering direction from being less than -40
				steeringDirection = -40;
			else
				steeringDirection += newDir;
		}
		
		
			
	}
	
	//get method for steering direction
	public int getSteering(){
		return steeringDirection;
	}
	
	//set method for fuel level
	public void setFuelLevel(int newFuel){
		fuelLevel = newFuel;
	}
	
	//get method for fuel level
	public int getFuelLevel(){
		return fuelLevel;
	}
	
	//set method for damage level
	public void setDamageLevel(int newDamage){
		damageLevel = newDamage;
		maximumSpeed = (int)(maximumSpeed * (1 - (damageLevel * .01)));		//adjusts max speed based off damage level
		if(getSpeed() > maximumSpeed)		//if current speed is greater than max speed
			changeSpeed(maximumSpeed);		//change speed to max speed
		
	}
	
	//get method for damage level
	public int getDamageLevel(){
		return damageLevel;
	}
	
	//get method for car width
	public int getWidth(){
		return width;
	}
	
	//get method for car length
	public int getLength(){
		return length;
	}
	
	//get method for max speed
	public int getMaxSpeed(){
		return maximumSpeed;
	}
	
	//method for changing speeds
	public void changeSpeed(int newSpeed){
		if(this.getSpeed() < 0)
			this.setSpeed(0);
		else if(newSpeed < maximumSpeed)
			this.setSpeed(newSpeed);
		else if(newSpeed >= maximumSpeed)
			this.setSpeed(maximumSpeed);
	}
	
	//overridden method for steerable object
	@Override
	public void changeHeading(int newHeading){
		this.setHeading(newHeading);
	}
	
	@Override
    public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" 
        		+ " color = " + this.getColor() + " heading = " + this.getHeading() + " speed = " + this.getSpeed() 
        		+ " width = " + this.getWidth() + " length = " + this.getLength() + " maxSpeed = " + this.getMaxSpeed() 
        		+" Steering Direction: " + this.getSteering() + " Fuel Level: " + this.getFuelLevel()
        		+ " Damage Level: " + this.getDamageLevel();
    }

}
