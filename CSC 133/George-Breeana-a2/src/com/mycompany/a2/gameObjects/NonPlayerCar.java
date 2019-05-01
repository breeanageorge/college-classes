package com.mycompany.a2.gameObjects;

import com.mycompany.a2.gameObjectCollection.GameObjectCollection;
import com.mycompany.a2.gameObjects.interfaces.ISteerable;
import com.mycompany.a2.gameObjects.interfaces.IStrategy;
import com.mycompany.a2.strategy.DemolitionDerbyStrategy;
import com.mycompany.a2.strategy.PylonStrategy;

public class NonPlayerCar extends Car implements ISteerable{
	protected IStrategy curStrategy;
	private int maxSpeedNPC = 30;
	private int damageLevelNPC = 0;
	private int recentPylon = 1;
	
	
	//Constructor sets heading, speed, location
	public NonPlayerCar(float x, float y){
		super(x,y);
		this.setHeading(0);
		this.setSpeed(10);
		this.setLocation(x, y);
	}
	
	@Override
	public void move(int objSpeed){
		int newLocationX = (int)(super.getLocationX() + Math.cos(Math.toRadians(90 - this.getHeading()))*objSpeed);
		int newLocationY = (int)(super.getLocationY() + Math.sin(Math.toRadians(90 - this.getHeading()))*objSpeed);
		super.setLocation(newLocationX, newLocationY);
		this.invokeStrategy();
	}
	
	//get method for max speed
	public int getMaxSpeed(){
		return maxSpeedNPC;
	}
	
	//sets Recenty Pylon
	public void setRecentPylon(int newPylon){
		recentPylon = newPylon;
	}
	
	//get recent pylon
	public int getRecentPylon(){
		return recentPylon;
	}
	
	//set method for damage level
	public void setDamageLevel(int newDamage){
		damageLevelNPC = (int) (newDamage * .75);
		maxSpeedNPC = (int)(maxSpeedNPC * (1 - (damageLevelNPC * .001)));		//adjusts max speed based off damage level
		if(getSpeed() > maxSpeedNPC)		//if current speed is greater than max speed
			changeSpeed(maxSpeedNPC);		//change speed to max speed
		
	}
	
	//sets strategy
	public void setStrategy(IStrategy strategy){
		curStrategy = strategy;
			
	}

	//gets current strategy
	public IStrategy getStrategy(){
		return this.curStrategy;
		
	}
	
	//invokes strategy
	public void invokeStrategy(){
		curStrategy.invokeStrategy();
		
	}
	
	public void changeStrategy(GameObjectCollection gameObjectsList) {
        if (getStrategy() instanceof DemolitionDerbyStrategy){
            setStrategy(new PylonStrategy(this, gameObjectsList));
        }else if (getStrategy() instanceof PylonStrategy){
            setStrategy(new DemolitionDerbyStrategy(this, gameObjectsList));
        }
    }
	
	//changes heading
	@Override
	public void changeHeading(int newHeading){
		this.setHeading(newHeading);
	}

	
	@Override
    public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" 
        		+ " color = " + this.getColor() + " heading = " + this.getHeading() + " speed = " + this.getSpeed() 
        		+ " width = " + this.getWidth() + " length = " + this.getLength() + " maxSpeed = " + this.getMaxSpeed() 
        		+ " Current Strategy: " + this.getStrategy()
        		+ " Damage Level: " + this.getDamageLevel()+ " Recent Pylon: " + this.getRecentPylon();
    }
}
