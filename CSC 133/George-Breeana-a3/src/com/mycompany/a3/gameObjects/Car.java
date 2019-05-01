package com.mycompany.a3.gameObjects;

import java.util.ArrayList;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Rectangle;
import com.codename1.util.MathUtil;
import com.mycompany.a3.game.GameWorld;
import com.mycompany.a3.gameObjects.interfaces.ICollider;
import com.mycompany.a3.gameObjects.interfaces.ISteerable;
import com.mycompany.a3.gameObjects.interfaces.MoveableObject;

public class Car extends MoveableObject implements ISteerable, ICollider{
	//private variables for car
	private int width = 20;
	private int length = 40;
	private int size = 60;
	private int steeringDirection = 0;
	private int maximumSpeed = 50;
	private double fuelLevel = 100;
	private int damageLevel = 0;
	private int currentPylon;
	
	private ArrayList<GameObject> collisionList = new ArrayList<GameObject>();
	
	public Car(double x, double y){
		super.setSize(size);
		super.setColor(ColorUtil.rgb(255, 0, 0));
		this.setHeading(0);
		this.setSpeed(0);
		this.setLocationX(x);
		this.setLocationY(y);
		this.setPylonNum(1);
		collisionList = new ArrayList<GameObject>();
	}

	public void setPylonNum(int newPylon) {
		currentPylon = newPylon;
	}
	
	public int getPylonNum() {
		return currentPylon;
	}
	
	public ArrayList<GameObject> getList() {
		return collisionList;
	}
	
	//set method for steering
	public void setSteering(int newDir){
		if(newDir > 0){	
			if(steeringDirection < 0){		
				steeringDirection = 0;
			}else if(steeringDirection >= 40){
				steeringDirection = 40;
			}else{
				steeringDirection += newDir;
			}	
		}else{
			if(steeringDirection > 0){
				steeringDirection = 0;
			}else if(steeringDirection <= -40){
				steeringDirection = -40;
			}else{
				steeringDirection += newDir;
			}	
		}		
	}
	
	//get method for steering direction
	public int getSteering(){
		return steeringDirection;
	}
	
	//set method for fuel level
	public void setFuelLevel(double newFuel){
		fuelLevel = newFuel;
	}
	
	//get method for fuel level
	public double getFuelLevel(){
		return fuelLevel;
	}
	
	//set method for damage level
	public void setDamageLevel(int newDamage){
		damageLevel = newDamage;
		maximumSpeed = (int)(maximumSpeed * (1 - (damageLevel * .01)));
		if(getSpeed() > maximumSpeed){
			changeSpeed(maximumSpeed);
		}
		
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
	//set method for max speed
	public void setMaxSpeed(int newMaxSpeed){
		maximumSpeed = newMaxSpeed;
	}
	//get method for max speed
	public int getMaxSpeed(){
		return maximumSpeed;
	}
	
	//method for changing speeds
	public void changeSpeed(int newSpeed){
		if(this.getSpeed() < 0){
			this.setSpeed(0);
		}else if(newSpeed < maximumSpeed){
			this.setSpeed(newSpeed);
		}else if(newSpeed >= maximumSpeed){
			this.setSpeed(maximumSpeed);
		}	
	}
	
	@Override
	public int changeColor(){
        int damage = this.getDamageLevel();
		switch (damage){
            case 75:
            	super.setColor(ColorUtil.rgb(255, 150, 150));
                return ColorUtil.rgb(255, 150, 150);
            case 50:
            	super.setColor(ColorUtil.rgb(255, 102, 102));
                return ColorUtil.rgb(255, 102, 102);
            case 25:
            	super.setColor(ColorUtil.rgb(255, 26, 26));
                return ColorUtil.rgb(255, 26, 26);
            default:
            	super.setColor(ColorUtil.rgb(255, 0, 0));
                return ColorUtil.rgb(255, 0, 0);
        }
    }
	
	@Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        int xLoc = pCmpRelPrnt.getX() + (int)this.getLocationX()-(super.getSize()/2);
        int yLoc = pCmpRelPrnt.getY() + (int)this.getLocationY()-(super.getSize()/2);
        g.setColor(super.getColor());
        g.drawArc(xLoc, yLoc, super.getSize(), super.getSize(), 0, 360);
        g.fillArc(xLoc, yLoc, super.getSize(), super.getSize(), 0, 360);
        //for debug
        super.setBounds(new Rectangle(xLoc, yLoc, super.getSize(), super.getSize()));
        //g.drawRect(xLoc, yLoc, super.getSize(), super.getSize());
    }

	
    public boolean collidesWith(Object otherObj) {
    	boolean result = false;
        int thisCenterX = (int) this.getLocationX(); // find centers int thisCenterY = this.yLoc + (OBJECT_SIZE/2);
        int thisCenterY = (int) this.getLocationY(); // find centers int thisCenterY = this.yLoc + (OBJECT_SIZE/2);
        GameObject obj = (GameObject) otherObj;
        int otherCenterX = (int) obj.getLocationX();
        int otherCenterY = (int) obj.getLocationY();
        // find dist between centers (use square, to avoid taking roots)
        int dx = thisCenterX - otherCenterX;
        int dy = thisCenterY - otherCenterY;
        int distBetweenCentersSqr = ((dx * dx) + (dy * dy));
        // find square of sum of radii
        int thisRadius = this.getSize()/2;
        int otherRadius = obj.getSize()/2;
        int radiiSqr = (int) MathUtil.pow(thisRadius + otherRadius, 2);
        if (distBetweenCentersSqr <= radiiSqr) {
            result = true;
        }
      
      return result;
	}

    public void handleCollision(Object otherObject, GameWorld gw) {
    	int newDamage = 0;
    	if(otherObject instanceof Pylon){
        	if(this.getPylonNum() == (((Pylon) otherObject).getSeqNum() - 1)){
				this.setPylonNum(((Pylon) otherObject).getSeqNum());
				if(!(this instanceof NonPlayerCar)){
					gw.setLastPylon(((Pylon) otherObject).getSeqNum());
				}
        	}	
        	
        }else if(otherObject instanceof FuelCan){
        	double newFuel = 0;
        	if(!(((FuelCan) otherObject).getIsEmpty())){
				newFuel = ((FuelCan) otherObject).getSize();
				newFuel = newFuel + this.getFuelLevel();
				if(!(this instanceof NonPlayerCar)){
					gw.setFuelLevel(newFuel);
					if (gw.getSound()) gw.fuelSlurp.play();
				}
				this.setFuelLevel(newFuel);
				((FuelCan) otherObject).setIsEmpty(true);
				((FuelCan) otherObject).changeColor();
	    		gw.gameObjectsList.add(new FuelCan());
        	}
        }else if(otherObject instanceof Bird){
			newDamage = this.getDamageLevel() + 25;
			this.setDamageLevel(newDamage);
			if(!(this instanceof NonPlayerCar)){
        		if (gw.getSound()) gw.birdCrash.play();
        		gw.setDamageLevel(newDamage);
        	}
			
        }else if(otherObject instanceof NonPlayerCar){
			newDamage = this.getDamageLevel() + 50;
			this.setDamageLevel(newDamage);
			if(!(this instanceof NonPlayerCar)){
				if (gw.getSound()) gw.carCrash.play();
				gw.setDamageLevel(newDamage);
			}
        }
    	if(!(this instanceof NonPlayerCar)){
    		this.changeColor();
	        //change lives if damage too much
	        if(newDamage >= 100){
				gw.setLives(gw.getLives()-1);
				this.setMaxSpeed(50);
				if (gw.getSound()) gw.lifeLost.play();
				newDamage = 0;
			}
    	}    
    }
	
	@Override
    public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" 
        		+ " color = " + this.getColor() + " heading = " + this.getHeading() + " speed = " + this.getSpeed() 
        		+ " width = " + this.getWidth() + " length = " + this.getLength() + " maxSpeed = " + this.getMaxSpeed() 
        		+" Steering Direction: " + this.getSteering() + " Fuel Level: " + this.getFuelLevel()
        		+ " Damage Level: " + this.getDamageLevel();
    }
	
	@Override
	public void changeHeading(int newHeading) {
		this.setHeading(newHeading);
	}

}
